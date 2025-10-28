package com.secureon.service;

import com.secureon.domain.model.Delivery;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {
    List<Delivery> getAllDeliveries();
    Delivery createDelivery(Delivery delivery);
    Optional<Delivery> findById(Long id);
    void deleteDelivery(Long id);
    void inProcessDelivery(Long id,Long employeeId);
    void completedDelivery(Long id);
    List<Delivery> getAllDeliveriesByState( String state);
    List<Delivery> findByEmployeeId(Long employeeId);
// New delete method
}
