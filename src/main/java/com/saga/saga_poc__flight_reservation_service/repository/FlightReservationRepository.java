package com.saga.saga_poc__flight_reservation_service.repository;

import com.saga.saga_poc__flight_reservation_service.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {
}
