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
import java.util.Optional;

@Service
public class FlightReservationService {

    private final FlightRepository flightRepository;
    private final FlightReservationRepository flightReservationRepository;

    public FlightReservationService(FlightRepository flightRepository, FlightReservationRepository flightReservationRepository) {
        this.flightRepository = flightRepository;
        this.flightReservationRepository = flightReservationRepository;
    }

    public FlightReservation makeReservation(FlightReservationRequest request) throws NoSuchElementException{
        FlightReservation foundReservation = flightReservationRepository.findByReservationId(request.getReservationId());
        if (foundReservation != null) return foundReservation;
        Optional<Flight> optionalFlight = flightRepository.findByFlightNumber(request.getFlightNumber());
        Flight flight = new Flight();
        if (optionalFlight.isPresent()) flight = optionalFlight.get();
        FlightReservation flightReservation = new FlightReservation();
        flightReservation.setReservationId(request.getReservationId());
        flightReservation.setFlightId(flight.getId() == null ? 0L : flight.getId());
        flightReservation.setFlightNumber(request.getFlightNumber());
        flightReservation.setSeatNumber(request.getSeatNumber());
        flightReservation.setDepartureDate(request.getDepartureDate());
        flightReservation.setReturnDate(request.getReturnDate());
        try {
            flightReservation.setStatus(flightReservation.getFlightId() == 0L ? StatusEnum.CANCELLED : StatusEnum.RESERVED);
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
