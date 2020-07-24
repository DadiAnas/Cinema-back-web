package org.sid.cinema.controller;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
class TicketFrom {
    private String nomClient;
    private Integer codePayement;
    private List<Long> tickets = new ArrayList<>();

}
