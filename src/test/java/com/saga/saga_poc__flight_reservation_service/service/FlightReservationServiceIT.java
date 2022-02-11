package com.saga.saga_poc__flight_reservation_service.service;

import com.saga.saga_poc__flight_reservation_service.repository.FlightRepository;
import com.saga.saga_poc__flight_reservation_service.repository.FlightReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightReservationServiceIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FlightReservationService underTest;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightReservationRepository flightReservationRepository;

    @Test
    void flightReservationServiceBeanGetsCreated() {
        assertTrue(applicationContext.containsBean("flightReservationService"));
    }

    @Test
    void flightReservationServiceContainsFlightRepository() {
        FlightRepository injectedFlightRepository = (FlightRepository) ReflectionTestUtils.getField(underTest, "flightRepository");
        assertSame(flightRepository, injectedFlightRepository);
    }

    @Test
    void flightReservationServiceContainsFlightReservationRepository() {
        FlightReservationRepository injectedFlightReservationRepository =(FlightReservationRepository) ReflectionTestUtils.getField(underTest, "flightReservationRepository");
        assertSame(flightReservationRepository, injectedFlightReservationRepository);
    }

}