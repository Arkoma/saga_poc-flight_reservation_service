package com.saga.saga_poc__flight_reservation_service.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saga.saga_poc__flight_reservation_service.model.Flight;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservation;
import com.saga.saga_poc__flight_reservation_service.model.FlightReservationRequest;
import com.saga.saga_poc__flight_reservation_service.model.StatusEnum;
import com.saga.saga_poc__flight_reservation_service.repository.FlightRepository;
import com.saga.saga_poc__flight_reservation_service.repository.FlightReservationRepository;
import com.saga.saga_poc__flight_reservation_service.service.FlightReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
class FlightReservationControllerIT {

    private final WebApplicationContext webApplicationContext;
    private final FlightReservationService flightReservationService;
    private final FlightRepository flightRepository;
    private final FlightReservationRepository flightReservationRepository;
    private final FlightReservationController underTest;

    @Autowired
    public FlightReservationControllerIT(WebApplicationContext webApplicationContext,
                                         FlightReservationService flightReservationService,
                                         FlightReservationController flightReservationController,
                                         FlightRepository flightRepository,
                                         FlightReservationRepository flightReservationRepository) throws ParseException {
        this.webApplicationContext = webApplicationContext;
        this.flightReservationService = flightReservationService;
        this.underTest = flightReservationController;
        this.flightRepository = flightRepository;
        this.flightReservationRepository = flightReservationRepository;
    }

    private MockMvc mockMvc;
    private FlightReservationRequest flightReservationRequest;
    private Flight flight;
    private final Long reservationId = 1L;
    private final String flightNumber = "801";
    private final String seatNumber = "1A";
    private final Date departureDate = new SimpleDateFormat("dd MMM yyyy").parse("12 Feb 2022");
    private final Date returnDate = new SimpleDateFormat("dd MMM yyyy").parse("19 Feb 2022");
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        flight = new Flight();
        flight.setFlightNumber("880");
        flight.setDepartureCity("Miami");
        flight.setArrivalCity("Los Angeles");
        flight.setArrivalTime("1300");
        flight.setDepartureTime("0900");
        this.flightRepository.deleteAll();
        flight = this.flightRepository.save(flight);
        flightReservationRequest = FlightReservationRequest.builder()
                .reservationId(reservationId)
                .flight(flight)
                .flightNumber(flightNumber)
                .seatNumber(seatNumber)
                .departureDate(departureDate)
                .returnDate(returnDate)
                .build();
        this.flightReservationRepository.deleteAll();
    }

    @Test
    void flightReservationControllerExistsAsABean() {
        assertTrue(webApplicationContext.containsBean("flightReservationController"));
    }

    @Test
    void flightReservationServiceIsInjectedInTheController() {
        FlightReservationService injectedFlightReservationService =(FlightReservationService) ReflectionTestUtils.getField(underTest, "flightReservationService");
        assertSame(flightReservationService, injectedFlightReservationService);
    }

    @Test
    void makeReservationEndpointExists() throws Exception {
        String json = mapper.writeValueAsString(this.flightReservationRequest);
        this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void makeReservationEndpointReturnsReservation() throws Exception {
        final MvcResult result = this.makeReservation(this.flightReservationRequest.getReservationId());
        String responseJson = result.getResponse().getContentAsString();
        FlightReservation actualResponse = mapper.readValue(responseJson, FlightReservation.class);
        assertAll(() -> {
            assertEquals(StatusEnum.RESERVED, actualResponse.getStatus());
            assertEquals(flight.getId(), actualResponse.getFlightId());
            assertEquals(this.reservationId, actualResponse.getReservationId());
            assertEquals(this.flightNumber, actualResponse.getFlightNumber());
            assertEquals(this.seatNumber, actualResponse.getSeatNumber());
            assertEquals(this.departureDate, actualResponse.getDepartureDate());
                }
        );
    }

    private MvcResult makeReservation(Long reservationId) throws Exception {
        this.flightReservationRequest.setReservationId(reservationId);
        String json = mapper.writeValueAsString(this.flightReservationRequest);
        return this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
    }

    @Test
    @Transactional
    void makeReservationEndpointSavesReservation() throws Exception {
        final MvcResult result = this.makeReservation(this.flightReservationRequest.getReservationId());
        String responseJson = result.getResponse().getContentAsString();
        FlightReservation actualResponse = mapper.readValue(responseJson, FlightReservation.class);
        FlightReservation actualEntity = this.flightReservationRepository.getById(actualResponse.getId());
        assertAll(() -> {
            assertEquals(StatusEnum.RESERVED ,actualEntity.getStatus());
            assertEquals(flight.getId(), actualEntity.getFlightId());
            assertEquals(this.reservationId, actualEntity.getReservationId());
            assertEquals(this.flightNumber, actualResponse.getFlightNumber());
            assertEquals(this.seatNumber, actualEntity.getSeatNumber());
            assertEquals(this.departureDate, actualEntity.getDepartureDate());
            assertEquals(this.returnDate, actualEntity.getReturnDate());
                }
        );
    }

    @Test
    void cancelReservationEndpointExists() throws Exception {
        final MvcResult result = this.makeReservation(this.flightReservationRequest.getReservationId());
        String responseJson = result.getResponse().getContentAsString();
        FlightReservation reservation = mapper.readValue(responseJson, FlightReservation.class);
        Long id = reservation.getId();
        this.mockMvc.perform(delete("/reservation/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void cancelReservationEndpointRemovesReservation() throws Exception {
        final MvcResult result = this.makeReservation(this.flightReservationRequest.getReservationId());
        String responseJson = result.getResponse().getContentAsString();
        FlightReservation reservation = mapper.readValue(responseJson, FlightReservation.class);
        Long id = reservation.getId();
        FlightReservation reservationFromDb = this.flightReservationRepository.getById(id);
        assertEquals(id, reservationFromDb.getId());
        this.mockMvc.perform(delete("/reservation/" + id))
                .andExpect(status().isNoContent());
        reservationFromDb = this.flightReservationRepository.findById(id).orElse(null);
        assertNull(reservationFromDb);
    }
    
    @Test
    void getReservationEndpointExists() throws Exception {
        final MvcResult result = this.makeReservation(this.flightReservationRequest.getReservationId());
        String responseJson = result.getResponse().getContentAsString();
        FlightReservation reservation = mapper.readValue(responseJson, FlightReservation.class);
        Long id = reservation.getId();
        final MvcResult foundResult = this.mockMvc.perform(get("/reservation/" + id))
            .andExpectAll(
                    status().isOk(),
                    content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        String foundResponseJson = foundResult.getResponse().getContentAsString();
        FlightReservation foundReservation = mapper.readValue(foundResponseJson, FlightReservation.class);
        assertAll(() -> {
                    assertEquals(StatusEnum.RESERVED ,foundReservation.getStatus());
                    assertEquals(this.flight.getId(), foundReservation.getFlightId());
                    assertEquals(this.reservationId, foundReservation.getReservationId());
                    assertEquals(this.flightNumber, foundReservation.getFlightNumber());
                    assertEquals(this.seatNumber, foundReservation.getSeatNumber());
                    assertEquals(this.departureDate, foundReservation.getDepartureDate());
                    assertEquals(this.returnDate, foundReservation.getReturnDate());
                }
        );
    }

    @Test
    void getReservationReturnsNotFoundIfReservationDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/reservation/" + 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAll() throws Exception {
        this.makeReservation(this.flightReservationRequest.getReservationId());
        final Long newReservationId = 456L;
        this.makeReservation(newReservationId);
        final MvcResult foundResult = this.mockMvc.perform(get("/reservations"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String foundResponseJson = foundResult.getResponse().getContentAsString();
        List<FlightReservation> foundReservations = mapper.readValue(foundResponseJson, new TypeReference<>(){});
        assertAll(() -> {
                    assertEquals(StatusEnum.RESERVED ,foundReservations.get(0).getStatus());
                    assertEquals(this.flight.getId(), foundReservations.get(0).getFlightId());
                    assertEquals(this.reservationId, foundReservations.get(0).getReservationId());
                    assertEquals(this.flightNumber, foundReservations.get(0).getFlightNumber());
                    assertEquals(this.seatNumber, foundReservations.get(0).getSeatNumber());
                    assertEquals(this.departureDate, foundReservations.get(0).getDepartureDate());
                    assertEquals(this.returnDate, foundReservations.get(0).getReturnDate());
                    assertEquals(2, foundReservations.size());
                }
        );
    }

}

