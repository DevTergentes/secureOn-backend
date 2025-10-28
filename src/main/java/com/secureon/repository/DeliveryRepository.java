package com.secureon.repository;

import com.secureon.domain.model.Delivery;
import com.secureon.domain.model.valueobjects.DeliveryState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByEmployeeId(Long employeeId);
    List<Delivery> findAllByState(DeliveryState state);

}
