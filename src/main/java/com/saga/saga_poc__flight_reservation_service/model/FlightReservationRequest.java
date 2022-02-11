package com.saga.saga_poc__flight_reservation_service.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FlightReservationRequest {
    private Flight flight;
    private Long reservationId;
    private String flightNumber;
    private String seatNumber;
    private Date departureDate;
}
