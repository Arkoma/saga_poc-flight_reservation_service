package com.saga.saga_poc__flight_reservation_service.service;

import com.saga.saga_poc__flight_reservation_service.model.Flight;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservation;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservationRequest;
import com.saga.saga_poc__flight_reservation_service.repository.FlightRepository;
import com.saga.saga_poc__flight_reservation_service.repository.FlightReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private FlightReservation flightReservation;

    private Long hotelReservationId;

    @BeforeEach
    void setup() {
        hotelReservationId = 3L;
        flightReservation = new FlightReservation();
        flightReservation.setId(hotelReservationId);
    }

    @Test
    void makeReservationSavesHotelPassedIn() throws ParseException {
        String hotelName = "Holiday Inn";
        Long hotelId = 1L;
        Long reservationId = 2L;
        Flight hotel = new Flight();
        hotel.setId(hotelId);
        hotel.setFlightNumber(hotelName);
        when(flightRepository.findByName(anyString())).thenReturn(Optional.of(hotel));
        when (flightReservationRepository.save(any(FlightReservation.class))).thenReturn(flightReservation);
        final int roomNumber = 666;
        final Date checkinDate = new SimpleDateFormat("d MMM yyyy").parse("9 Feb 2022");
        final Date checkoutDate = new SimpleDateFormat("dd MMM yyyy").parse("12 Feb 2022");
        FlightReservationRequest request = FlightReservationRequest.builder()
                .reservationId(reservationId)
                .flight(hotel)
                .flightNumber(roomNumber)
                .seatNumber(checkinDate)
                .departureDate(checkoutDate)
                .build();
        FlightReservation actual = underTest.makeReservation(request);
        verify(flightRepository, times(1)).findByName(hotelName);
        verify(flightReservationRepository, times(1)).save(any(FlightReservation.class));
        assertAll(() -> {
            assertEquals(hotelReservationId, actual.getId());
        });
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
}