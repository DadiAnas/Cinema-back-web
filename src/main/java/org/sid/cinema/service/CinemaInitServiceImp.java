package org.sid.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.cinema.dao.CategoryRepository;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.PlaceRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.SalleRepository;
import org.sid.cinema.dao.SeanceRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Place;
import org.sid.cinema.entities.Projection;
import org.sid.cinema.entities.Salle;
import org.sid.cinema.entities.Seance;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CinemaInitServiceImp implements IcinemaInitService{
@Autowired private CategoryRepository categoryRepository;
@Autowired private CinemaRepository cinemaRepository;
@Autowired private FilmRepository filmRepository;
@Autowired private PlaceRepository placeRepository;
@Autowired private ProjectionRepository projectionRepository;
@Autowired private SalleRepository salleRepository;
@Autowired private SeanceRepository seanceRepository;
@Autowired private TicketRepository ticketRepository;
@Autowired private VilleRepository villeRepository;

@Override
	public void initCategory() {
		// TODO Auto-generated method stub
		Stream.of("Actions","Fiction","Drama","Policier","Comedie")
		.forEach(cat->{
			Categorie categorie=new Categorie();
			categorie.setName(cat);
			categoryRepository.save(categorie);
		});
	}

	@Override
	public void initCinemas() {
		// TODO Auto-generated method stub
		villeRepository.findAll().forEach(v->{
			Stream.of("MegaRama","IMax","FOUNOUN","CHAHRAZAD","DAOULIZ")
			.forEach(nameCinema->{
				Cinema cinema=new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinema.setLatitude(Math.random());
				cinema.setAltitude(Math.random());
				cinema.setLongitude(Math.random());
				cinemaRepository.save(cinema);
			});
		});
	}

	@Override
	public void initFilms() {
		// TODO Auto-generated method stub
		double[]durees=new double[] {1,1.5,2,2.5,3};
		List<Categorie>categories=categoryRepository.findAll();
		Stream.of("Black Panther","Aladdin","Avengers End Game","Bad Boys 3","Frozen2","Home Alone","The Terminal","HobbsandShaw","Dora")
		.forEach(titreFilm->{
			Film film=new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ","")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			film.setDateSortie(new Date());
			filmRepository.save(film);
		});
	}

	@Override
	public void initPlaces() {
		// TODO Auto-generated method stub
		salleRepository.findAll()
		.forEach(salle->{
			for(int i=0;i<salle.getNombrePlace();i++) {
				Place place=new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				place.setAltitude(Math.random());
				place.setLatitude(Math.random());
				place.setLongitude(Math.random());
				placeRepository.save(place);
			}
		});
	}

	@Override
	public void initProjections() {
		// TODO Auto-generated method stub
		double[]prices=new double[] {30,50,60,70,90,100};
		List<Film>films=filmRepository.findAll();
		villeRepository.findAll()
		.forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index=new Random().nextInt(films.size());
				
					
					Film film=films.get(index);
						seanceRepository.findAll()
						.forEach(seance->{
							Projection projection=new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					
				});
			});
		});
	}

	@Override
	public void initSales() {
		// TODO Auto-generated method stub
		cinemaRepository.findAll()
		.forEach(cinema->{
			for(int i=0;i<cinema.getNombreSalles();i++) {
				Salle salle=new Salle();
				salle.setName("Salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
	}

	@Override
	public void initSeance() {
		// TODO Auto-generated method stub
		DateFormat dateFormat=new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:30","19:00","21:30")
		.forEach(s->{
			Seance seance=new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		 
		

	
	}

	@Override
	public void initTicket() {
		// TODO Auto-generated method stub
		projectionRepository.findAll()
		.forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket=new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
				
			});
		});

	}

	@Override
	public void initVilles() {
		// TODO Auto-generated method stub
		Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
			Ville ville=new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
	}

}
