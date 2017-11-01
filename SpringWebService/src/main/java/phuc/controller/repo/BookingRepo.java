package phuc.controller.repo;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import phuc.entity.Booking;

@Repository
public class BookingRepo implements IBookingRepo {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public void saveBooking(Booking booking) {
		entityManager.persist(booking);
	}

	@Override
	public Collection<Booking> getAllBooking() {
		Collection<Booking> list = entityManager.createQuery("SELECT r FROM Booking r", Booking.class).getResultList();
		return list;
	}

}
