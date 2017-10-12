package phuc.controller.service.booking;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import phuc.controller.repo.IBookingDetailRepo;
import phuc.controller.repo.IBookingRepo;
import phuc.controller.repo.ICustomerRepo;
import phuc.controller.repo.IRoomRepo;
import phuc.controller.service.room.IRoomInfo;
import phuc.controller.service.user.IProfileService;
import phuc.entity.Booking;
import phuc.entity.BookingDetails;
import phuc.entity.Customer;
import phuc.entity.RoomType;
import phuc.entity.TblBookingDetailsId;

public class BookingService implements IBooking {

	@Autowired
	private IRoomInfo roomInfo;
	
	@Autowired
	private IProfileService profile;
	
	@Autowired
	private IBookingRepo bookingRepo;
	
	@Autowired
	private IBookingDetailRepo detailRepo;
	
	@Autowired
	private ICustomerRepo cusRepo;
	
	@Autowired
	private IRoomRepo roomRepo;
	
	@Override
	public boolean bookRoom(Booking booking, String cusId, Map<Integer, Integer> details) {
		if(profile.checkExistUser(cusId)){
			if(checkValidBooking(booking, details)){
				Customer cus = cusRepo.getCustomer(cusId);
				booking.setTblCustomer(cus);
				bookingRepo.saveBooking(booking);
				// 
				saveDetails(booking, details);
				//
			}
		}
		return false;
	}
	
	private void saveDetails(Booking booking, Map<Integer, Integer> details){
		for (Integer key : details.keySet()){
			TblBookingDetailsId id = new TblBookingDetailsId(booking.getBookingId(), key);
			RoomType type = roomRepo.getRoomType(key);
			BookingDetails detail = new BookingDetails(id, booking, type, details.get(key));
			detailRepo.saveBookingDetails(detail);
		}
	}

	@Override
	public boolean checkValidBooking(Booking booking, Map<Integer, Integer> details) {
		if(booking != null && details != null || !details.isEmpty()){
			return checkValidDay(booking.getArrivalDay(), booking.getCheckOutDay())
					&& checkEnoughCapacity(booking.getNumberOfPerson(), details)
					&& checkEnoughRoom(details);
		}
		return false;
	}
	
	private boolean checkValidDay(Date arrivalDay, Date checkoutDay){
		if(arrivalDay.getTime() < checkoutDay.getTime()){
			long totalTime = checkoutDay.getTime() - arrivalDay.getTime();
			return totalTime >= (60 * 1000);
		}
		return false;
	}
	
	private boolean checkEnoughCapacity(int numOfPerson, Map<Integer, Integer> details){
		if(numOfPerson > 0){
			int capacity = 0;
			for(Integer key : details.keySet()){
				capacity += details.get(key);
			}
			return numOfPerson <= capacity;
		}
		return false;
	}
	
	private boolean checkEnoughRoom(Map<Integer, Integer> details){
		for(Integer key : details.keySet()){
			if(roomInfo.getAvailableRoom(key) <= 0){
				return false;
			}
		}
		return true;
	}

}
