package icns.smartplantdashboardapi.dto.sensorManage.range;

import icns.smartplantdashboardapi.domain.SensorManage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/*
public class SensorRangeResponse {
    private Long ssId;

    private float   ;

    private float rlev1;

    private float rlev2;

    private float rlev3;

    private float rlev4;

    private float rend;

    private String sensorTypeName;

    private String ssCode;

    private Long sensorPosId;

    public SensorRangeResponse(SensorManage sensorManage){
        this.ssId = sensorManage.getSsId();
        this.rstart= sensorManage.getRstart();
        this.rlev1 = sensorManage.getRlev1();
        this.rlev2 = sensorManage.getRlev2();
        this.rlev3 = sensorManage.getRlev3();
        this.rlev4 = sensorManage.getRlev4();
        this.rend = sensorManage.getRend();
        this.ssCode = sensorManage.createSensorCode();
        this.sensorPosId = sensorManage.getSsPos().getPosId();
        this.sensorTypeName = sensorManage.getSsType().getTypeName();
    }

}
*/
public class SensorRangeResponse {
    private Long ssId;

    private float rlev1;

    private float rlev2;

    private float rlev3;

    private float rlev4;

    private float rlev5;

    private float rlev6;

    private float rlev7;

    private float rlev8;

    //private float rend;
    private Integer rangeType;

    private String sensorTypeName;

    private String ssCode;

    private Long sensorPosId;

    public SensorRangeResponse(SensorManage sensorManage){

        this.ssId = sensorManage.getSsId();
        this.rlev1 = sensorManage.getRlev1();
        this.rlev2 = sensorManage.getRlev2();
        this.rlev3 = sensorManage.getRlev3();
        this.rlev4 = sensorManage.getRlev4();
        this.rlev5 = sensorManage.getRlev5();
        this.rlev6 = sensorManage.getRlev6();
        this.rlev7 = sensorManage.getRlev7();
        this.rlev8 = sensorManage.getRlev8();
        this.rangeType = sensorManage.getRangeType();
        this.ssCode = sensorManage.createSensorCode();
        this.sensorPosId = sensorManage.getSsPos().getPosId();
        this.sensorTypeName = sensorManage.getSsType().getTypeName();
    }

}