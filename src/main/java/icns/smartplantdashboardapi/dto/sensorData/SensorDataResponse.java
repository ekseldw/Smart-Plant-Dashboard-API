package icns.smartplantdashboardapi.dto.sensorData;

import icns.smartplantdashboardapi.domain.EState; //새로추가
import icns.smartplantdashboardapi.domain.SensorData;
import icns.smartplantdashboardapi.dto.sensorManage.SensorManageResponse;
import icns.smartplantdashboardapi.dto.sensorManage.SensorManageSimpleResponse;
import icns.smartplantdashboardapi.dto.sensorManage.range.SensorRangeResponse; //새로 추가
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataResponse {
    private Long dataId;

    private float inputData;

    private LocalDateTime createdAt;

    private SensorManageResponse sensorManage;

    private Integer sensorState;


    public SensorDataResponse(SensorData sensorData){
        this.dataId = sensorData.getDataId();
        this.sensorManage = new SensorManageResponse(sensorData.getSensorManage());
        this.inputData = sensorData.getInputData();
        this.createdAt = sensorData.getCreatedAt();
        
        if(sensorData.getSensorManage().getRangeType()==0){
            if(this.inputData > sensorData.getSensorManage().getRlev4()){
                this.sensorState = EState.SERIOUS.ordinal();
            }else if(this.inputData > sensorData.getSensorManage().getRlev3()){
                this.sensorState = EState.WANRNING.ordinal();
            }else if(this.inputData > sensorData.getSensorManage().getRlev2()){
                this.sensorState = EState.CAUTION.ordinal();
            }else if(this.inputData > sensorData.getSensorManage().getRlev1()) {
                this.sensorState = EState.ATTENTION.ordinal();
            }else{
                this.sensorState = EState.SAFE.ordinal();
            }
        }
        else{
            if(this.inputData > sensorData.getSensorManage().getRlev8()){
                this.sensorState = EState.SERIOUS.ordinal();
            }else if(this.inputData > sensorData.getSensorManage().getRlev7()){
                this.sensorState = EState.WANRNING.ordinal();
            }else if(this.inputData > sensorData.getSensorManage().getRlev6()){
                this.sensorState = EState.CAUTION.ordinal();
            }else if(this.inputData > sensorData.getSensorManage().getRlev5()) {
                this.sensorState = EState.ATTENTION.ordinal();
            }else if(this.inputData < sensorData.getSensorManage().getRlev5() && this.inputData > sensorData.getSensorManage().getRlev4()){
                this.sensorState = EState.SAFE.ordinal();
            }else if(this.inputData < sensorData.getSensorManage().getRlev4()){
                this.sensorState = EState.ATTENTION.ordinal();
            }else if(this.inputData < sensorData.getSensorManage().getRlev3()){
                this.sensorState = EState.CAUTION.ordinal();
            }else if(this.inputData < sensorData.getSensorManage().getRlev2()){
                this.sensorState = EState.WANRNING.ordinal();
            }else if(this.inputData < sensorData.getSensorManage().getRlev1()){
                this.sensorState = EState.SERIOUS.ordinal();
            }
            
            

        }
        
      
        //



    }


}
