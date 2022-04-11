package com.saga.saga_poc__flight_reservation_service.service;

import com.saga.saga_poc__flight_reservation_service.model.Flight;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservation;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservationRequest;
import com.saga.saga_poc__flight_reservation_service.model.StatusEnum;
import com.saga.saga_poc__flight_reservation_service.repository.FlightRepository;
import com.saga.saga_poc__flight_reservation_service.repository.FlightReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightReservationServiceTest {

    @InjectMocks
    private FlightReservationService underTest;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightReservationRepository flightReservationRepository;

    @Captor
    private ArgumentCaptor<FlightReservation> flightReservationArgumentCaptor;

    private FlightReservation flightReservation;

    private Long flightReservationId;

    @BeforeEach
    void setup() {
        flightReservationId = 3L;
        flightReservation = new FlightReservation();
        flightReservation.setId(flightReservationId);
    }

    @Test
    void makeReservationSavesFlightPassedIn() throws ParseException {
        Long flightId = 1L;
        Long reservationId = 2L;
        final String flightNumber = "880";
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setArrivalCity("Boston");
        flight.setDepartureCity("New York");
        flight.setDepartureTime("1300");
        flight.setFlightNumber(flightNumber);
        when(flightRepository.findByFlightNumber(anyString())).thenReturn(Optional.of(flight));
        when (flightReservationRepository.save(any(FlightReservation.class))).thenReturn(flightReservation);
        final String seatNumber = "1A";
        final Date departureDate = new SimpleDateFormat("dd MMM yyyy").parse("12 Feb 2022");
        final Date returnDate = new SimpleDateFormat("dd MMM yyyy").parse("19 Feb 2022");
        FlightReservationRequest request = FlightReservationRequest.builder()
                .reservationId(reservationId)
                .flightNumber(flightNumber)
                .seatNumber(seatNumber)
                .departureDate(departureDate)
                .returnDate(returnDate)
                .build();
        FlightReservation actual = underTest.makeReservation(request);
        verify(flightRepository, times(1)).findByFlightNumber(flightNumber);
        verify(flightReservationRepository, times(1)).save(any(FlightReservation.class));
        assertAll(() -> assertEquals(flightReservationId, actual.getId()));
    }

    @Test
    void cancelReservationDeletesEntity() {
        underTest.cancelReservation(1L);
        verify(flightReservationRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testGetReservationByIdCallsFindById() {
        when(flightReservationRepository.findById(anyLong())).thenReturn(Optional.of(flightReservation));
        FlightReservation actual = underTest.getReservationById(1L);
        verify(flightReservationRepository, times(1)).findById(anyLong());
        assertEquals(flightReservation.getId(), actual.getId());
    }

    @Test
    void getAllReservationsCallsFindAll() {
        underTest.getAllReservations();
        verify(flightReservationRepository, times(1)).findAll();
    }

    @Test
    void setsStatusToCancelledIfCannotFindFlight() throws ParseException {
        Long flightId = 1L;
        Long reservationId = 2L;
        final String flightNumber = "666";
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setArrivalCity("Boston");
        flight.setDepartureCity("New York");
        flight.setDepartureTime("1300");
        flight.setFlightNumber(flightNumber);
        when(flightRepository.findByFlightNumber(anyString())).thenReturn(Optional.empty());
        when (flightReservationRepository.save(any(FlightReservation.class))).thenReturn(flightReservation);
        final String seatNumber = "1A";
        final Date departureDate = new SimpleDateFormat("dd MMM yyyy").parse("12 Feb 2022");
        final Date returnDate = new SimpleDateFormat("dd MMM yyyy").parse("19 Feb 2022");
        FlightReservationRequest request = FlightReservationRequest.builder()
                .reservationId(reservationId)
                .flightNumber(flightNumber)
                .seatNumber(seatNumber)
                .departureDate(departureDate)
                .returnDate(returnDate)
                .build();
        underTest.makeReservation(request);
        assertAll(() -> {
            verify(flightRepository, times(1)).findByFlightNumber(flightNumber);
            verify(flightReservationRepository, times(1)).save(flightReservationArgumentCaptor.capture());
            FlightReservation actual = flightReservationArgumentCaptor.getValue();
            assertEquals(0L, actual.getFlightId());
            assertEquals(StatusEnum.CANCELLED, actual.getStatus());
        });
    }

    @Test
    void ifFlightReservationFoundByReservationIdDoNotReturnNewFlightReservation() throws ParseException {
        Long flightId = 1L;
        Long reservationId = 2L;
        final String flightNumber = "666";
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setArrivalCity("Boston");
        flight.setDepartureCity("New York");
        flight.setDepartureTime("1300");
        flight.setFlightNumber(flightNumber);
        final String seatNumber = "1A";
        final Date departureDate = new SimpleDateFormat("dd MMM yyyy").parse("12 Feb 2022");
        final Date returnDate = new SimpleDateFormat("dd MMM yyyy").parse("19 Feb 2022");
        FlightReservationRequest request = FlightReservationRequest.builder()
                .reservationId(reservationId)
                .flightNumber(flightNumber)
                .seatNumber(seatNumber)
                .departureDate(departureDate)
                .returnDate(returnDate)
                .build();
        FlightReservation foundReservation = new FlightReservation();
        foundReservation.setReservationId(reservationId);
        foundReservation.setFlightId(0L);
        foundReservation.setStatus(StatusEnum.CANCELLED);
        when(flightReservationRepository.findByReservationId(reservationId)).thenReturn(foundReservation);
        FlightReservation actual = underTest.makeReservation(request);
        assertAll(() -> {
            verify(flightReservationRepository, times(1)).findByReservationId(reservationId);
            verify(flightReservationRepository, times(0)).save(any(FlightReservation.class));
            assertEquals(0L, actual.getFlightId());
            assertEquals(StatusEnum.CANCELLED, actual.getStatus());
        });
    }
}