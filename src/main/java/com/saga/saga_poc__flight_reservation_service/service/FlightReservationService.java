package com.saga.saga_poc__flight_reservation_service.service;

import com.saga.saga_poc__flight_reservation_service.model.Flight;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservation;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservationRequest;
import com.saga.saga_poc__flight_reservation_service.model.StatusEnum;
import com.saga.saga_poc__flight_reservation_service.repository.FlightRepository;
import com.saga.saga_poc__flight_reservation_service.repository.FlightReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FlightReservationService {

    private final FlightRepository flightRepository;
    private final FlightReservationRepository flightReservationRepository;

    public FlightReservationService(FlightRepository flightRepository, FlightReservationRepository flightReservationRepository) {
        this.flightRepository = flightRepository;
        this.flightReservationRepository = flightReservationRepository;
    }

    public FlightReservation makeReservation(FlightReservationRequest request) throws NoSuchElementException{
        Flight hotel = flightRepository.findByFlightNumber(request.getFlight().getFlightNumber()).orElseThrow();
        FlightReservation flightReservation = new FlightReservation();
        flightReservation.setFlightId(hotel.getId());
        flightReservation.setReservationId(request.getReservationId());
        flightReservation.setDepartureDate(request.getDepartureDate());
        flightReservation.setSeatNumber(request.getSeatNumber());
        try {
            flightReservation.setStatus(StatusEnum.RESERVED);
            flightReservation = flightReservationRepository.save(flightReservation);
        } catch (Exception e) {
            flightReservation.setStatus(StatusEnum.CANCELLED);
            flightReservation = flightReservationRepository.save(flightReservation);
        }
        return flightReservation;
    }

    public void cancelReservation(Long id) {
        this.flightReservationRepository.deleteById(id);
    }

    public FlightReservation getReservationById(Long id) {
        return this.flightReservationRepository.findById(id).orElse(null);
    }

    public List<FlightReservation> getAllReservations() {
        return this.flightReservationRepository.findAll();
    }
}
