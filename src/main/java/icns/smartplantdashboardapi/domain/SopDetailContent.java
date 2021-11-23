package icns.smartplantdashboardapi.domain;

import icns.smartplantdashboardapi.dto.sopDetail.SopDetailContentRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SopDetailContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(targetEntity = SopDetail.class, fetch=FetchType.LAZY)
    @JoinColumn(name="sopdetail_id")
    private SopDetail sopDetail;

    @ManyToOne(targetEntity = SensorPos.class, fetch=FetchType.LAZY)
    @JoinColumn(name="sensorpos_id", nullable = true)
    private SensorPos ssPos;

    @Column
    private String text;

    @Column
    private boolean message;

    @Column
    private boolean complete;

    public SopDetailContent update(SopDetailContentRequest sopDetailContentRequest, SensorPos sensorPos){
        text = sopDetailContentRequest.getText();
        message = sopDetailContentRequest.isMessage();
        ssPos = sensorPos;
        return this;
    }

}