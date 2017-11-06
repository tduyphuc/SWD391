package data.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import data.entity.PaymentType;

@Repository
public class PaymentTypeRepo implements IPaymentTypeRepo {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public PaymentType getPaymentType(Integer id) {
		PaymentType type = entityManager.find(PaymentType.class, id);
		return type;
	}

}
