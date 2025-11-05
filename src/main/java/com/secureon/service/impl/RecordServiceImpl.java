package com.secureon.service.impl;

import com.secureon.domain.model.Delivery;
import com.secureon.domain.model.Device;
import com.secureon.domain.model.Incident;
import com.secureon.domain.model.RecordLog;
import com.secureon.domain.model.valueobjects.DeliveryState;
import com.secureon.domain.model.valueobjects.InputDataSensor;
import com.secureon.domain.services.validate.ValidateSensorStrategyImplements;
import com.secureon.repository.*;
import com.secureon.service.RecordService;
import com.secureon.service.dto.RecordLogSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private IncidentRepository incidentRepository;
    @Autowired
    private GeolocationServiceImpl geolocationService;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<RecordLog> getAllRecordsByDeliveryId(Long id) {
        return recordRepository.findByDeliveryId(id);
    }

    @Override
    public Long timeDifferenceInMinutesByDeliveryId(Long id) {
        List<RecordLog> records = recordRepository.findByDeliveryId(id);

        if (records.isEmpty()) {
            return 0L;
        }
        // dto which returns a comparison of the first and last record timestamp
        return RecordLogSummary.calculateMinutesBetween(records);
    }

    @Override
    public Optional<RecordLog> getLatestRecordByDeliveryId(Long deliveryId) {
        return recordRepository.findFirstByDeliveryIdOrderByTimestampDesc(deliveryId);
    }

    @Override
    public RecordLog createRecord(RecordLog record) {
        System.out.println("=== [createRecord] - INICIO ===");
        System.out.println("Record recibido: " + record);

        Optional<Delivery> delivery = deliveryRepository.findById(record.getDeliveryId());
        if (delivery.isEmpty()) {
            System.out.println("[ERROR] Delivery no encontrado: deliveryId=" + record.getDeliveryId());
            throw new RuntimeException("Delivery not found");
        }

        DeliveryState state = delivery.get().getState();
        System.out.println("Estado de Delivery: " + state);
        if (state == DeliveryState.PENDING || state == DeliveryState.COMPLETED) {
            System.out.println("[ERROR] Delivery en estado inválido para recibir records: " + state);
            throw new RuntimeException("Delivery cannot accept records in this state: " + state);
        }

        // generación de input para strategy
        InputDataSensor inputDataSensor = new InputDataSensor(
                record.getHeartRateValue(), // BPM primero
                record.getGasValue(), // luego gas
                record.getTemperatureValue(), // luego temperatura
                record.getLatitude(),
                record.getLongitude());

        Device sensor = sensorRepository.findById(record.getSensorId())
                .orElseThrow(() -> {
                    return new RuntimeException("Sensor not found");
                });

        // validación de estado de sensor
        boolean ok = ValidateSensorStrategyImplements.isValid(inputDataSensor);

        sensor.updateSafeState(inputDataSensor);
        sensor.updateSafeState(inputDataSensor);

        if (!sensor.isSafe()) {
            System.out.println("Sensor NO está seguro. Guardando sensor e incidente...");

            sensorRepository.save(sensor);

            String place = geolocationService.getDisplayNameFromCoordinates(record.getLatitude(),
                    record.getLongitude());
            System.out.println("Lugar del incidente: " + place);

            String description = record.generateDescription(place, sensor, inputDataSensor);
            System.out.println("Descripción de incidente: " + description);

            // establecer timestamp si es null para el incidente
            LocalDateTime incidentDate = record.getTimestamp() != null
                    ? record.getTimestamp()
                    : LocalDateTime.now();

            Incident incident = Incident.builder()
                    .incidentPlace(place)
                    .date(incidentDate)
                    .description(description)
                    .serviceId(delivery.get().getId())
                    .build();

            incidentRepository.save(incident);
            System.out.println("Incidente guardado: " + incident);

            // IMPLEMENTAR NOTIFICACION

        } else {
            System.out.println("Sensor está seguro. Guardando sensor...");
            sensor.isTrue();
            sensorRepository.save(sensor);
        }

        // establecer timestamp si es null
        if (record.getTimestamp() == null) {
            record.setTimestamp(LocalDateTime.now());
            System.out.println("Timestamp establecido: " + record.getTimestamp());
        }

        // guardado de record
        try {
            RecordLog savedRecord = recordRepository.save(record);
            System.out.println("Record guardado exitosamente: " + savedRecord);
            System.out.println("=== [createRecord] - FIN ===");
            return savedRecord;
        } catch (Exception e) {
            System.out.println("[ERROR] Error al guardar record: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving record: " + e.getMessage(), e);
        }
    }

}
