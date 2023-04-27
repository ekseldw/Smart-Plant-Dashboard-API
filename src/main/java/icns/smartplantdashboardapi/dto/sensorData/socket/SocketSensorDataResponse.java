package icns.smartplantdashboardapi.dto.sensorData.socket;

import icns.smartplantdashboardapi.domain.EState; // 새로 추가
import icns.smartplantdashboardapi.domain.SensorData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocketSensorDataResponse {
    private Long ssId;
    private float inputData;

    private LocalDateTime createdAt;

    private Integer sensorState;

    public SocketSensorDataResponse(SensorData sensorData){
        this.ssId = sensorData.getSensorManage().getSsId();
        this.inputData = sensorData.getInputData();
        this.createdAt = sensorData.getCreatedAt();
        //this.sensorState = sensorData.getSensorManage().getSensorState(); 새로주석처리

        /* 
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
        */
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
    }
}
