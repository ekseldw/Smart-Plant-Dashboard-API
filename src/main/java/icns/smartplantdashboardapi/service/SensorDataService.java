package icns.smartplantdashboardapi.service;

import icns.smartplantdashboardapi.advice.exception.SensorManageNotFoundException;
import icns.smartplantdashboardapi.advice.exception.SensorPosNotFoundException;
import icns.smartplantdashboardapi.domain.*;
import icns.smartplantdashboardapi.dto.abnormalDetection.socket.SocketAbnormalDetectionResponse;
import icns.smartplantdashboardapi.dto.sensorData.SensorDataGraphRequest;
import icns.smartplantdashboardapi.dto.sensorData.SensorDataGraphResponse;
import icns.smartplantdashboardapi.dto.sensorData.SensorDataRequest;
import icns.smartplantdashboardapi.dto.sensorData.SensorDataResponse;
import icns.smartplantdashboardapi.dto.sensorData.socket.SocketSensorDataResponse;
import icns.smartplantdashboardapi.repository.AbnormalDetectionRepository;
import icns.smartplantdashboardapi.repository.SensorDataRepository;
import icns.smartplantdashboardapi.repository.SensorManageRepository;
import icns.smartplantdashboardapi.repository.SensorPosRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorDataService {
    private final SensorDataRepository sensorDataRepository;
    private final SensorManageRepository sensorManageRepository;
    private final SensorPosRepository sensorPosRepository;
    private final AbnormalDetectionRepository abnormalDetectionRepository;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Transactional
    public Long save(SensorDataRequest sensorDataRequest) throws Exception{
        SensorManage sensorManage = sensorManageRepository.findById(sensorDataRequest.getSensorManageId()).orElseThrow(SensorManageNotFoundException::new);

        // update sensor state
        Integer pastState = sensorManage.getSensorState();
        sensorManage.setSensorState(sensorDataRequest.getInputData());

        // save
        SensorData saved = sensorDataRepository.save(sensorDataRequest.toEntity(sensorManage));

        // detect
        detectAbnormal(pastState, sensorManage);

        return saved.getDataId();
    }

    @Transactional
    public void detectAbnormal(Integer pastState, SensorManage sensorManage) throws Exception {

        Integer currState = sensorManage.getSensorState();
        if(pastState == null){
            System.out.println("Sensor Data Start");
        }
        
        else if( currState > 0 && pastState < currState){
            AbnormalDetection abnormalDetection = new AbnormalDetection(sensorManage, sensorManage.getSensorState());
            abnormalDetectionRepository.save(abnormalDetection);
            alertState(abnormalDetection);
        }
        else {
            System.out.println("detected but not displayed");
        }
    }

    public void alertState(AbnormalDetection abnormalDetection){
        messageSendingOperations.convertAndSend("/alert", new SocketAbnormalDetectionResponse(abnormalDetection));
    }

    @Transactional(readOnly = true)
    public List<SensorDataResponse> findByPosId(Long posId){
        SensorPos ssPos = sensorPosRepository.findById(posId).orElseThrow(SensorPosNotFoundException::new);
        return sensorDataRepository.findBySensorManage_SsPosOrderByCreatedAtDesc(ssPos).stream().map(SensorDataResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SocketSensorDataResponse sendData(Long ssId) throws Exception{
        SensorManage sensorManage = sensorManageRepository.findById(ssId).get();
        SensorData sensorData = sensorDataRepository.findTop1BySensorManageOrderByCreatedAtDesc(sensorManage);
        return new SocketSensorDataResponse(sensorData);
    }

    @Transactional
    public List<SensorData> clearOldData(Long month){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusMonths(month);
        System.out.println(threshold);

        List<SensorData> deleted = sensorDataRepository.deleteByCreatedAtLessThan(threshold);
        return deleted;
    }

    @Transactional(readOnly = true)
    public String saveCsv(Long month) throws IOException {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusMonths(month);
        System.out.println(threshold);
        String fileName = now.toString()+"-"+month.toString()+"ago.csv";
        String csvPath = new File("").getAbsolutePath() + "/data-csv/"+fileName;


        BufferedWriter fw = new BufferedWriter(new FileWriter(csvPath, true));

        List<SensorData> sensorDataList = sensorDataRepository.findByCreatedAtGreaterThan(threshold);

        fw.write("no,날짜,구역,센서,식별코드,데이터");
        fw.newLine();

        int cnt = 1;
        for (SensorData sensorData : sensorDataList){
            fw.write(cnt+","
                +sensorData.getCreatedAt()+","
                + sensorData.getSensorManage().getSsPos().getPosName()+","
                +sensorData.getSensorManage().getSsType().getTypeName()+","
                +sensorData.getSensorManage().createSensorCode()+","
                +sensorData.getInputData()
            );
            fw.newLine();
            cnt++;
        }
        fw.flush();

        fw.close();


        return csvPath;

    }

    @Transactional(readOnly = true)
    public List<SensorDataGraphResponse> findByPosIdSsId(SensorDataGraphRequest request){
        LocalDateTime startDatetime = LocalDateTime.of(request.getStartTime(), LocalTime.of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(request.getEndTime(), LocalTime.of(23,59,59));

        return sensorDataRepository.findBySensorManage_SsIdAndSensorManage_SsPos_PosIdAndCreatedAtBetweenOrderByCreatedAtDesc(request.getSensorManageId(),request.getSensorPositionId(),startDatetime, endDatetime).stream().map(SensorDataGraphResponse::new).collect(Collectors.toList());
    }

    
    @Transactional(readOnly = true)
    public List<SensorDataGraphResponse> findByPosIdDate(SensorDataGraphRequest request){
        LocalDateTime startDatetime = LocalDateTime.of(request.getStartTime(), LocalTime.of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(request.getStartTime(), LocalTime.of(23,59,59));

        return sensorDataRepository.findBySensorManage_SsPos_PosIdAndCreatedAtBetweenOrderByCreatedAtDesc(request.getSensorPositionId(),startDatetime,endDatetime).stream().map(SensorDataGraphResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SensorDataGraphResponse> findByAllList(SensorDataGraphRequest request){
        LocalDateTime startDatetime = LocalDateTime.of(request.getStartTime(), LocalTime.of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(request.getEndTime(), LocalTime.of(23,59,59));
        return sensorDataRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDatetime, endDatetime).stream().map(SensorDataGraphResponse::new).collect(Collectors.toList());
    }
    

   
    
}
