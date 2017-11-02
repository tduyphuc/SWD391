package repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.Customer;

@Repository
public class CustomerRepo implements ICustomerRepo {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Customer getCustomer(String id) {
		Customer cus = entityManager.find(Customer.class, id);
		return cus;
	}

	@Override
	@Transactional
	public void saveCustomer(Customer cus) throws Exception {
		entityManager.persist(cus);
	}

	@Override
	@Transactional
	public void updateCustomer(Customer cus) throws Exception {
		entityManager.merge(cus);
	}

}
