package icns.smartplantdashboardapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbnormalDetection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(targetEntity = SensorManage.class)
    @JoinColumn(name="seneormanage_id")
    private SensorManage sensorManage;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Integer sensorState;

    @PrePersist
    public void createdAt(){
        this.createdAt = LocalDateTime.now();
    }

    public AbnormalDetection(SensorManage sensorManage, Integer state) {
        this.sensorManage = sensorManage;
        this.sensorState = state;
    }
}