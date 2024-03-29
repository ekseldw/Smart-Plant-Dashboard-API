package icns.smartplantdashboardapi.service;

import icns.smartplantdashboardapi.domain.*;
import icns.smartplantdashboardapi.dto.sopDetail.SopDetailContentRequest;
import icns.smartplantdashboardapi.dto.sopDetail.SopDetailContentResponse;
import icns.smartplantdashboardapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SopDetailContentService {

    private final SopDetailRepository sopDetailRepository;
    private final SensorPosRepository sensorPosRepository;
    private final SopDetailContentRepository sopDetailContentRepository;
    private final SopCheckLogRepository sopCheckLogRepository;
    private final SituationRepository situationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save( SopDetailContentRequest sopDetailContentRequest){
        System.out.println(sopDetailContentRequest.getEfunction());
        System.out.println(sopDetailContentRequest.toString());
        SopDetail sopDetail = sopDetailRepository.findById(sopDetailContentRequest.getTitleId()).get();


        SopDetailContent sopDetailContent;
        if(sopDetailContentRequest.getEfunction() ==1 || sopDetailContentRequest.getEfunction()==2){
            SensorPos sensorPos = sensorPosRepository.findById(sopDetailContentRequest.getPosId()).get();
            sopDetailContent = sopDetailContentRepository.save(sopDetailContentRequest.toEntity(sopDetail, sensorPos));

        }else{
            sopDetailContent = sopDetailContentRepository.save(sopDetailContentRequest.toEntity(sopDetail, null));
        }
        return sopDetailContent.getId();
    }

    @Transactional
    public Long update(Long contentId, SopDetailContentRequest sopDetailContentRequest){
        SopDetailContent sopDetailContent = sopDetailContentRepository.findById(contentId).get();
        if(sopDetailContentRequest.getEfunction()==1 || sopDetailContentRequest.getEfunction()==2){
            SensorPos sensorPos = sensorPosRepository.findById(sopDetailContentRequest.getPosId()).get();
            sopDetailContent.update(sopDetailContentRequest, sensorPos);
        }else{
            sopDetailContent.update(sopDetailContentRequest, null);
        }

        return sopDetailContent.getId();
    }
    @Transactional(readOnly = true)
    public SopDetailContentResponse find(Long contentId){
        SopDetailContent sopDetailContent = sopDetailContentRepository.findById(contentId).get();
        return new SopDetailContentResponse(sopDetailContent);
    }
    @Transactional(readOnly = true)

    public List<SopDetailContentResponse> findAll(Long titleId){
        List<SopDetailContentResponse> sopDetailContentResponseList = sopDetailContentRepository.findBySopDetail_Id(titleId).stream().map(SopDetailContentResponse::new).collect(Collectors.toList());
        return sopDetailContentResponseList;
    }

    @Transactional
    public Long delete(Long contentId){
        sopDetailContentRepository.deleteById(contentId);
        return contentId;
    }

    @Transactional
    public Long complete(String userName, Long contentId){ //User user
        SopDetailContent sopDetailContent = sopDetailContentRepository.findById(contentId).get();
        if(sopDetailContent.isComplete()){
            sopDetailContent.completeFalse();
        }else{
            sopDetailContent.completeTrue();

            SopCheckLog sopCheckLog = SopCheckLog.builder()
                    .text(sopDetailContent.getText())
                    .situation(sopDetailContent.getSopDetail().getSituation().getName())
                    .level(sopDetailContent.getSopDetail().getLevel())
                    .user(userName)
                    .build();
            sopCheckLogRepository.save(sopCheckLog);
        }
        return contentId;
    }

    @Transactional
    public boolean endSop(Long situationId, Integer level){
        Situation situation = situationRepository.findById(situationId).get();
        List<SopDetail> sopDetailList = sopDetailRepository.findBySituationAndLevel(situation, level);
        for(SopDetail sopDetail : sopDetailList){
            List<SopDetailContent> sopDetailContentList = sopDetailContentRepository.findBySopDetail_Id(sopDetail.getId());

            for(SopDetailContent sopDetailContent : sopDetailContentList){
                sopDetailContent.completeFalse();
            }
        }
        return true;
    }
}
