package icns.smartplantdashboardapi.repository;

import icns.smartplantdashboardapi.domain.AbnormalDetection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;

public interface AbnormalDetectionRepository extends JpaRepository<AbnormalDetection, Long>{
    Page<AbnormalDetection> findBySensorManage_SsPos_PosId(@Param(value="posId") Long posId, Pageable pageable);

    List<AbnormalDetection> findBySensorManage_SsPos_PosId(@Param(value="posId") Long posId);

    Page<AbnormalDetection> findBySensorManage_SsPos_PosIdAndSensorManage_SsType_TypeId(@Param(value="posId") Long posId,@Param(value="typeId")Long typeId, Pageable pageable);

}
