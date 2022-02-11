package com.saga.saga_poc__flight_reservation_service;

import com.saga.saga_poc__flight_reservation_service.model.Flight;
import com.saga.saga_poc__flight_reservation_service.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FlightRepository flightRepository;

    public DataSeeder(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadHotelData();
    }

    private void loadHotelData() {
        if (flightRepository.count() == 0) {
            Flight hotel1 = new Flight();
            hotel1.setFlightNumber("Holiday Inn");
            hotel1.setDepartureCity("42 McVernon st");
            hotel1.setArrivalCity("Fort Worth");
            hotel1.setState("TX");
            hotel1.setZip("78702");
            hotel1.setArrivalTimestamp("customerService@holidayInn.com");
            hotel1.setDepartureTimestamp("555-555-1212");
            Flight hotel2 = new Flight();
            hotel2.setFlightNumber("Dallas Suites");
            hotel2.setDepartureCity("33 Highland Park Ave");
            hotel2.setArrivalCity("Dallas");
            hotel2.setState("TX");
            hotel2.setZip("78702");
            hotel2.setArrivalTimestamp("customerService@DallasSuites.com");
            hotel2.setDepartureTimestamp("555-555-1212");
            Flight hotel3 = new Flight();
            hotel3.setFlightNumber("Austin Comfort stays");
            hotel3.setDepartureCity("83 Thomas Corner");
            hotel3.setArrivalCity("Austin");
            hotel3.setState("TX");
            hotel3.setZip("78702");
            hotel3.setArrivalTimestamp("customerService@asc.com");
            hotel3.setDepartureTimestamp("555-555-1212");
            System.out.println("saving hotel " + flightRepository.save(hotel1));
            System.out.println("saving hotel " + flightRepository.save(hotel2));
            System.out.println("saving hotel " + flightRepository.save(hotel3));
        }
    }
}
