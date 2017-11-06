package service.user;

import java.util.List;
import java.util.Map;

import data.entity.Customer;

public interface IProfileService {
	public boolean checkExistUser(String id);
	public boolean registUser(Customer customer);
	public boolean updateProfile(Customer customer);
	public Map<String, String> getUserInfo(String id);
	public List<Map<String, String>> getUserHistory(String id);
}
