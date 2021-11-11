package icns.smartplantdashboardapi.service;

import icns.smartplantdashboardapi.advice.exception.SensorManageNotFoundException;
import icns.smartplantdashboardapi.advice.exception.SensorPosNotFoundException;
import icns.smartplantdashboardapi.domain.*;
import icns.smartplantdashboardapi.dto.abnormalDetection.AbnormalDetectionRequest;
import icns.smartplantdashboardapi.dto.abnormalDetection.AbnormalDetectionResponse;
import icns.smartplantdashboardapi.dto.sensorData.SensorDataRequest;
import icns.smartplantdashboardapi.dto.sensorData.SensorDataResponse;
import icns.smartplantdashboardapi.dto.socket.sensorData.SocketSensorDataResponse;
import icns.smartplantdashboardapi.repository.AbnormalDetectionRepository;
import icns.smartplantdashboardapi.repository.SensorDataRepository;
import icns.smartplantdashboardapi.repository.SensorManageRepository;
import icns.smartplantdashboardapi.repository.SensorPosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.ServerSocket;
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
    public Long save(SensorDataRequest sensorDataRequest){
        SensorManage sensorManage = sensorManageRepository.findById(sensorDataRequest.getSensorManageId()).orElseThrow(SensorManageNotFoundException::new);
        SensorData saved = sensorDataRepository.save(sensorDataRequest.toEntity(sensorManage));
        detectAbnormal(sensorManage,sensorDataRequest.getInputData());
        return saved.getDataId();
    }

    @Transactional
    public void detectAbnormal(SensorManage sensorManage, float data){
        Integer pastState = sensorManage.getSensorState();
        sensorManage.setSensorState(data);
        Integer currState = sensorManage.getSensorState();

        if( pastState < currState){
            AbnormalDetection abnormalDetection = new AbnormalDetection(sensorManage, sensorManage.getSensorState());
            abnormalDetectionRepository.save(abnormalDetection);
            alertState(abnormalDetection);
        }

    }

    public void alertState(AbnormalDetection abnormalDetection){
        messageSendingOperations.convertAndSend("/alert", new AbnormalDetectionResponse(abnormalDetection));
    }


    @Transactional(readOnly = true)
    public List<SensorDataResponse> findByPosId(Long posId){
        SensorPos ssPos = sensorPosRepository.findById(posId).orElseThrow(SensorPosNotFoundException::new);
        return sensorDataRepository.findBySensorManage_SsPos(ssPos).stream().map(SensorDataResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SocketSensorDataResponse sendData(Long ssId){
        SensorManage sensorManage = sensorManageRepository.findById(ssId).get();
        SensorData sensorData = sensorDataRepository.findTop1BySensorManageOrderByCreatedAtDesc(sensorManage);
        return new SocketSensorDataResponse(sensorData.getSensorManage().getSsId(), sensorData.getInputData());
    }
}
