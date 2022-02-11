package com.saga.saga_poc__flight_reservation_service.repository;

import com.saga.saga_poc__flight_reservation_service.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByName(String name);
}
