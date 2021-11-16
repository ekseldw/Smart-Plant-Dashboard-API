package icns.smartplantdashboardapi.repository;

import icns.smartplantdashboardapi.domain.Contact;
import icns.smartplantdashboardapi.domain.SensorManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findBySsPos_PosId(@Param(value="posId") Long posId, Pageable pageable);

}
