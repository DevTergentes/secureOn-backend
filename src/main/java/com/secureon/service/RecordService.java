package com.secureon.service;

import com.secureon.domain.model.RecordLog;

import java.util.List;
import java.util.Optional;

public interface RecordService {
    List<RecordLog> getAllRecordsByDeliveryId(Long id);
    Long timeDifferenceInMinutesByDeliveryId(Long id);
    RecordLog createRecord(RecordLog record);
    Optional<RecordLog> getLatestRecordByDeliveryId(Long deliveryId);
}
