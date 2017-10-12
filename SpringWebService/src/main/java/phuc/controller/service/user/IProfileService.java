package phuc.controller.service.user;

import phuc.entity.Customer;

public interface IProfileService {
	public boolean checkExistUser(String id);
	public boolean registUser(Customer customer);
	public boolean updateProfile(Customer customer);
}
