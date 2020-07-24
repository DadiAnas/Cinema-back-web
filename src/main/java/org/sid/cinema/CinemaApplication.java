package org.sid.cinema;

import org.sid.cinema.entities.*;
import org.sid.cinema.service.IcinemaInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner {
    @Autowired
    private IcinemaInitService cinemaInitService;
    @Autowired
    private RepositoryRestConfiguration restConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
        restConfiguration.exposeIdsFor(Film.class, Salle.class, Ticket.class, Ville.class, Cinema.class);
        cinemaInitService.initVilles();
        cinemaInitService.initCinemas();
        cinemaInitService.initSales();
        cinemaInitService.initPlaces();
        cinemaInitService.initSeance();
        cinemaInitService.initCategory();
        cinemaInitService.initFilms();
        cinemaInitService.initProjections();
        cinemaInitService.initTicket();


    }

}
