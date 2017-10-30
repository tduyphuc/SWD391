package phuc.controller.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phuc.controller.repo.ICustomerRepo;
import phuc.entity.Booking;
import phuc.entity.BookingDetails;
import phuc.entity.Customer;
import phuc.utils.IDateHelper;
import phuc.utils.IJSonHelper;

@Service
public class ProfileService implements IProfileService {

	private static final Logger logger = Logger.getLogger(ProfileService.class);
	
	@Autowired
	private ICustomerRepo repo;
	
	@Autowired
	private IJSonHelper json;
	
	@Autowired
	private IDateHelper dateHelper;
	
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

	@Override
	public Map<String, String> getUserInfo(String id) {
		Customer cus = repo.getCustomer(id);
		Map<String, String> mapInfo = new HashMap<>();
		mapInfo.put("id", cus.getCustomerId());
		mapInfo.put("firstName", cus.getFirstName());
		mapInfo.put("lastName", cus.getLastName());
		mapInfo.put("address", cus.getAddress());
		mapInfo.put("postal", cus.getPostalCode());
		mapInfo.put("country", cus.getCountry());
		mapInfo.put("email", cus.getEmail());
		mapInfo.put("phone", cus.getPhone());
		mapInfo.put("imageLink", cus.getImageLink());
		return mapInfo;
	}

	@Override
	public List<Map<String, String>> getUserHistory(String id) {
		Customer cus = repo.getCustomer(id);
		List<Map<String, String>> historyList = new ArrayList<>();
		Set<Booking> bookings = cus.getTblBookings();
		for(Booking booking : bookings){
			Map<String, String> history = new HashMap<>();
			history.put("adult", String.valueOf(booking.getAdult()));
			history.put("child", String.valueOf(booking.getChild()));
			history.put("comment", booking.getComment());
			history.put("arrivalDay", dateHelper.formatDate(booking.getArrivalDay()));
			history.put("checkOutDay", dateHelper.formatDate(booking.getCheckOutDay()));
			history.put("bookingDay", dateHelper.formatDate(booking.getBookingDay()));
			Map<String, String> details = new HashMap<>();
			for(BookingDetails detail : booking.getTblBookingDetailses()){
				details.put(detail.getTblRoomType().getName(), detail.getAmount() + "");
			}
			history.put("details", json.convertToJson(details));		
			// ...
			historyList.add(history);			
		}
		return historyList;
	}

}
