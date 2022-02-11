package com.saga.saga_poc__flight_reservation_service.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private Timestamp departureTimestamp;
    private Timestamp arrivalTimestamp;
}
