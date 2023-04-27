package icns.smartplantdashboardapi.dto.sensorData;

import com.fasterxml.jackson.annotation.JsonFormat;
import icns.smartplantdashboardapi.domain.SensorData;
import icns.smartplantdashboardapi.domain.EState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataGraphResponse {
    private Long ssId;

    private Long posId;

    private Long typeId;

    private String typeName;

    private float inputData;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private Integer rangeType;
    
    private Integer sensorState;

    private float rlev1;
    private float rlev2;
    private float rlev3;
    private float rlev4;
    private float rlev5;
    private float rlev6;
    private float rlev7;
    private float rlev8;

    public SensorDataGraphResponse(SensorData sensorData) {
        this.ssId = sensorData.getSensorManage().getSsId();
        this.posId = sensorData.getSensorManage().getSsPos().getPosId();
        this.typeId = sensorData.getSensorManage().getSsType().getTypeId();
        this.typeName = sensorData.getSensorManage().getSsType().getTypeName();
        this.inputData = sensorData.getInputData();
        this.createdAt = sensorData.getCreatedAt();
        this.rangeType = sensorData.getSensorManage().getRangeType();
        this.rlev8 = sensorData.getSensorManage().getRlev8();
        this.rlev7 = sensorData.getSensorManage().getRlev7();
        this.rlev6 = sensorData.getSensorManage().getRlev6();
        this.rlev5 = sensorData.getSensorManage().getRlev5();
        this.rlev4 = sensorData.getSensorManage().getRlev4();
        this.rlev3 = sensorData.getSensorManage().getRlev3();
        this.rlev2 = sensorData.getSensorManage().getRlev2();
        this.rlev1 = sensorData.getSensorManage().getRlev1();
      
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
