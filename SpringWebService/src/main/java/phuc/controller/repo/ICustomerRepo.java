package phuc.controller.repo;

import phuc.entity.Customer;

public interface ICustomerRepo {
	public Customer getCustomer(String id);
	public void saveCustomer(Customer cus) throws Exception;
	public void updateCustomer(Customer cus) throws Exception;
}
