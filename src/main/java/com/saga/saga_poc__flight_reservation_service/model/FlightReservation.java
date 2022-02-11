package com.saga.saga_poc__flight_reservation_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Entity
public class FlightReservation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long reservationId;
    private StatusEnum status;
    private Long flightId;
    private String seatNumber;
    private Date departureDate;
}
