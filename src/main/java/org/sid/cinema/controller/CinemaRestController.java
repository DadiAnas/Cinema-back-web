package org.sid.cinema.controller;

import org.sid.cinema.Authentification;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@RestController
@CrossOrigin("*")
public class CinemaRestController {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CinemaRepository cinemaRepository;

    @GetMapping(path = "imageFilm/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable(name = "id") Long id) throws Exception {
        Film f = filmRepository.findById(id).get();
        String photoName = f.getPhoto();
        File file = new File(System.getProperty("user.home") + "/eclipse-workspace2/cinema/src/main/resources/static/images/" + photoName);
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);

    }

    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketFrom ticketFrom) {
        List<Ticket> listTickets = new ArrayList<>();
        ticketFrom.getTickets().forEach(idTicket -> {
            Ticket ticket = ticketRepository.findById(idTicket).get();
            ticket.setNomClient(ticketFrom.getNomClient());
            ticket.setReserve(true);
            ticket.setCodePayement(ticketFrom.getCodePayement());
            ticketRepository.save(ticket);
            listTickets.add(ticket);

        });
        return listTickets;
    }

    @DeleteMapping("deleteCinemas/{id}/{VilleId}")
    @Transactional
    public void deleteCinema(@PathVariable("id") Long id,
                             @PathVariable("VilleId") Long VilleId) {
        cinemaRepository.deleteCinemaByIdAndVilleId(id, VilleId);
    }

    @RequestMapping("/log")
    public boolean login(@RequestBody User user) {
        return
                user.getUsername().equals("admin") && user.getPassword().equals("123");
    }

    @RequestMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
                .substring("Basic".length()).trim();
        return () -> new String(Base64.getDecoder()
                .decode(authToken)).split(":")[0];
    }

    @CrossOrigin(origins = "http://localhost:4200/login")
    @GetMapping(path = "/login")
    public Authentification basicauth() {
        return new Authentification("You are authenticated");
    }
}


