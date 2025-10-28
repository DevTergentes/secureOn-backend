package com.secureon.repository;

import com.secureon.domain.model.RecordLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<RecordLog, Long> {

    List<RecordLog> findByDeliveryId(Long deliveryId);
    Optional<RecordLog> findFirstByDeliveryIdOrderByTimestampDesc(Long deliveryId);
}
