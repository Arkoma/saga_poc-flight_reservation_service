package com.saga.saga_poc__flight_reservation_service;

import com.saga.saga_poc__flight_reservation_service.model.Flight;
import com.saga.saga_poc__flight_reservation_service.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FlightRepository flightRepository;

    public DataSeeder(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadFlightData();
    }

    private void loadFlightData() throws ParseException {
        if (flightRepository.count() == 0) {
            Flight flight1 = new Flight();
            flight1.setFlightNumber("880");
            flight1.setDepartureCity("Dallas");
            flight1.setArrivalCity("Boston");
            flight1.setArrivalTime("1300");
            flight1.setDepartureTime("1700");

            Flight flight2 = new Flight();
            flight2.setFlightNumber("881");
            flight2.setDepartureCity("New York");
            flight2.setArrivalCity("Miami");
            flight2.setArrivalTime("1400");
            flight2.setDepartureTime("1800");

            Flight flight3 = new Flight();
            flight3.setFlightNumber("882");
            flight3.setDepartureCity("Los Angeles");
            flight3.setArrivalCity("Orlando");
            flight3.setArrivalTime("1700");
            flight3.setDepartureTime("2100");

            System.out.println("saving flight " + flightRepository.save(flight1));
            System.out.println("saving flight " + flightRepository.save(flight2));
            System.out.println("saving flight " + flightRepository.save(flight3));

        }
    }
}
