package phuc.controller.service.user;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phuc.controller.repo.ICustomerRepo;
import phuc.entity.Customer;

@Service
public class ProfileService implements IProfileService {

	private static final Logger logger = Logger.getLogger(ProfileService.class);
	
	@Autowired
	private ICustomerRepo repo;
	
	@Override
	public boolean checkExistUser(String id) {
		Customer cus = repo.getCustomer(id);
		return cus != null;
	}

	@Override
	public boolean registUser(Customer customer) {
		try{
			repo.saveCustomer(customer);
			return true;
		}
		catch (Exception e) {
			logger.debug("registUser: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateProfile(Customer customer) {
		try{
			repo.updateCustomer(customer);
			return true;
		}
		catch (Exception e) {
			logger.debug("registUser: " + e.getMessage());
			return false;
		}
	}

}
