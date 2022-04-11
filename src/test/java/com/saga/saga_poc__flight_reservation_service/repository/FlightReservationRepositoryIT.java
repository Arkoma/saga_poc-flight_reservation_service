package com.saga.saga_poc__flight_reservation_service.repository;

import com.saga.saga_poc__flight_reservation_service.model.FlightReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightReservationRepositoryIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FlightReservationRepository underTest;

    private static final Long reservationId = 1L;

    @BeforeEach
    void setup() {
        underTest.deleteAll();
    }

    @Test
    void testFlightReservationRepository() {
        assertTrue(applicationContext.containsBean("flightReservationRepository"));
    }

    @Test
    void returnsNullIfFlightReservationCannotBeFoundByReservationId() {
        FlightReservation foundReservation = underTest.findByReservationId(reservationId);
        assertNull(foundReservation);
    }

    @Test
    void returnsSavedFlightReservationBySameReservationId() {
        FlightReservation reservationToSave = new FlightReservation();
        reservationToSave.setReservationId(reservationId);
        FlightReservation savedReservation = underTest.save(reservationToSave);
        FlightReservation foundReservation = underTest.findByReservationId(reservationId);
        assertEquals(savedReservation.getId(), foundReservation.getId());
    }

}