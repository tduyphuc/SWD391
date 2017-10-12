package phuc.controller.service.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phuc.controller.repo.ICustomerRepo;
import phuc.entity.Customer;

@Service
public class AuthenService implements IAuthenService {

	@Autowired
	private ICustomerRepo repo;
	
	@Override
	public boolean validateUser(String id, String password) {
		Customer cus = repo.getCustomer(id);
		if(cus != null){
			return cus.getPassword().equals(password);
		}
		return false;
	}

}
