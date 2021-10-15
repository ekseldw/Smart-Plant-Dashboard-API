package icns.smartplantdashboardapi.dto.sensorPos;

import icns.smartplantdashboardapi.domain.SensorPos;
import lombok.*;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SensorPosRequest {
    private String posName;
    private String posDtl;

    public SensorPos toEntity(){
        return SensorPos.builder()
                .posName(posName)
                .posDtl(posDtl)
                .build();

    }

    @Override
    public String toString() {
        return "SensorPosRequest{" +
                "posName='" + posName + '\'' +
                ", posDtl='" + posDtl + '\'' +
                '}';
    }
}
