package data.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import data.entity.Payment;

@Repository
public class PaymentRepo implements IPaymentRepo {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public void savePayment(Payment payment) throws Exception {
		entityManager.persist(payment);
	}

}
