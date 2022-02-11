package com.saga.saga_poc__flight_reservation_service.controller;

import com.saga.saga_poc__flight_reservation_service.model.FlightReservation;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservationRequest;
import com.saga.saga_poc__flight_reservation_service.service.FlightReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class FlightReservationController {

    private final FlightReservationService flightReservationService;

    public FlightReservationController(FlightReservationService flightReservationService) {
        this.flightReservationService = flightReservationService;
    }


    @PostMapping("/reservation")
    public ResponseEntity<FlightReservation> makeReservation(@RequestBody FlightReservationRequest request) {
        FlightReservation flightReservation = this.flightReservationService.makeReservation(request);
        return new ResponseEntity<>(flightReservation, HttpStatus.CREATED);
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity cancelReservation(@PathVariable Long id) {
        this.flightReservationService.cancelReservation(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<FlightReservation> getReservationById(@PathVariable Long id) {
        FlightReservation reservationById = this.flightReservationService.getReservationById(id);
        return new ResponseEntity<> (reservationById, null == reservationById ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<FlightReservation>> getAllReservations() {
        return new ResponseEntity<>(this.flightReservationService.getAllReservations(), HttpStatus.OK);
    }
}
