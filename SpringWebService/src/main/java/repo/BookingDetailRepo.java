package repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.BookingDetails;

@Repository
public class BookingDetailRepo implements IBookingDetailRepo {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public void saveBookingDetails(BookingDetails detail) {
		entityManager.persist(detail);
	}

}
