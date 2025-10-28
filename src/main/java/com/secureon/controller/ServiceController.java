package com.secureon.controller;


import com.secureon.domain.model.Service;
import com.secureon.service.ServiceServicio;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Services", description = "Services Management Endpoints")
@RequestMapping("/api/secureon/v1/services")
public class ServiceController {

    @Autowired
    private ServiceServicio serviceServicio;

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return new ResponseEntity<>(serviceServicio.getAllServices(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return new ResponseEntity<>(serviceServicio.createService(service), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceServicio.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
