package icns.smartplantdashboardapi.repository;

import icns.smartplantdashboardapi.domain.SensorData;
import icns.smartplantdashboardapi.domain.SensorManage;
import icns.smartplantdashboardapi.domain.SensorPos;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findBySensorManage_SsPosOrderByCreatedAtDesc(@Param(value="ssPos")SensorPos ssPos);
    SensorData findTop1BySensorManageOrderByCreatedAtDesc(@Param(value="sensorManage") SensorManage sensorManage);
    List<SensorData> deleteByCreatedAtLessThan(LocalDateTime threshold);
    List<SensorData> findByCreatedAtGreaterThan(LocalDateTime threshold);
    List<SensorData> findBySensorManage_SsIdAndSensorManage_SsPos_PosIdAndCreatedAtBetweenOrderByCreatedAtDesc(@Param(value="ssId") Long ssId, @Param(value="ssPos")Long ssPos, LocalDateTime startTime, LocalDateTime endTime);
    List<SensorData> findBySensorManage_SsPos_PosIdAndCreatedAtBetweenOrderByCreatedAtDesc(@Param(value="ssPos")Long ssPos,LocalDateTime startTime, LocalDateTime endTime);
    List<SensorData> findByCreatedAtBetweenOrderByCreatedAtDesc( LocalDateTime startTime, LocalDateTime endTime);

}
