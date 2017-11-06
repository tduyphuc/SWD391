package service.booking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;

import data.entity.Booking;
import data.entity.BookingDetails;
import data.entity.Customer;
import data.entity.Payment;
import data.entity.PaymentType;
import data.entity.RoomType;
import data.entity.TblBookingDetailsId;
import data.repo.IBookingDetailRepo;
import data.repo.IBookingRepo;
import data.repo.ICustomerRepo;
import data.repo.IPaymentRepo;
import data.repo.IPaymentTypeRepo;
import data.repo.IRoomRepo;
import service.room.IRoomInfo;
import service.user.IProfileService;
import utils.IDateHelper;

@Service
public class BookingService implements IBooking {

	private Gson gson = new Gson();
	
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
	
	@Autowired
	private IPaymentRepo paymentRepo;
	
	@Autowired
	private IPaymentTypeRepo typeRepo;
	
	@Autowired
	private IDateHelper dateHelper;
	
	@Override
	public boolean bookRoom(Booking booking, String cusId, MultiValueMap<String, String> params) {
		if(profile.checkExistUser(cusId)){
			Map<Integer, Integer> details = createDetails(params);
			if(checkValidBooking(booking, details)){
				Customer cus = cusRepo.getCustomer(cusId);
				booking.setTblCustomer(cus);
				bookingRepo.saveBooking(booking);
				// 
				saveDetails(booking, details);
				//
				System.out.println("BOOKING: " + booking.getBookingId());
				Payment payment = createPayment(booking, params, details);
				try {
					paymentRepo.savePayment(payment);
					return true;
				} catch (Exception e) {
					System.out.println("Payment_Exception: " + e.getMessage());
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> createDetails(MultiValueMap<String, String> params){
		String detailsParam = params.getFirst("details");		
		Map<Integer, Integer> details = new HashMap<>();		
		Map<String, String> parse_details = gson.fromJson(detailsParam, HashMap.class);
		for(Entry<String, String> entry : parse_details.entrySet()){
			Integer key = Integer.valueOf(entry.getKey());
			Integer value = Integer.valueOf(entry.getValue());
			details.put(key, value);
		}
		return details;
	}
	
	private Payment createPayment(Booking booking, MultiValueMap<String, String> params, Map<Integer, Integer> details){
		Payment payment = new Payment();
		Date payTime = dateHelper.getCurrentDayToSecond();
		long gap = booking.getCheckOutDay().getTime() - booking.getArrivalDay().getTime();
		long oneDayGap = 1000 * 3600 * 24;
		if(gap < 0){
			gap = 0;
		}
		Double amount = roomInfo.getTotalPrice(details) * (gap / oneDayGap);
		PaymentType paymentType = typeRepo.getPaymentType(Integer.valueOf(params.getFirst("paymentType")));
		
		payment.setTblBooking(booking);
		payment.setPayTime(payTime);
		payment.setPaid(amount);
		payment.setAmount(amount);
		payment.setTblPaymentType(paymentType);
		
		return payment;
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
			System.out.println("CHECKING");
			boolean checkValidDay = checkValidDay(booking.getArrivalDay(), booking.getCheckOutDay(), booking.getBookingDay());
			boolean checkEnoughCapacity = checkEnoughCapacity(booking.getAdult(), booking.getChild(), details);
			boolean checkEnoughRoom = checkEnoughRoom(details);
			System.out.println("checkValidDay: " + checkValidDay);
			System.out.println("checkEnoughCapacity: " + checkEnoughCapacity);
			System.out.println("checkEnoughRoom: " + checkEnoughRoom);
			return checkValidDay && checkEnoughCapacity && checkEnoughRoom;
		}
		return false;
	}
	
	private boolean checkValidDay(Date arrivalDay, Date checkoutDay, Date bookingDay){
		long oneDayGap = 1000 * 3600 * 24;
		long stay = checkoutDay.getTime() - arrivalDay.getTime();
		long bookBefore = arrivalDay.getTime() - bookingDay.getTime();
		return (stay >= oneDayGap) && (bookBefore >= oneDayGap);
	}
	
	private boolean checkEnoughCapacity(int adult, int child, Map<Integer, Integer> details){
		if(adult > 0){
			int capacity = 0;
			for(Entry<Integer, Integer> entry : details.entrySet()){
				RoomType type = roomRepo.getRoomType(entry.getKey());
				capacity += entry.getValue() * type.getCapacity();
			}
			return adult <= capacity && child <= 2 * (capacity - adult) + adult;
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

	@Override
	public String getAllBooking() {
		Collection<Booking> list = bookingRepo.getAllBooking();
		List<Map<String, String>> bookingList = new ArrayList<>();
		for(Booking booking : list){
			Map<String, String> map = new HashMap<>();
			map.put("cusId", String.valueOf(booking.getTblCustomer().getCustomerId()));
			map.put("adult", String.valueOf(booking.getAdult()));
			map.put("child", String.valueOf(booking.getChild()));
			map.put("comment", booking.getComment());
			map.put("arrivalDay", dateHelper.formatDate(booking.getArrivalDay()));
			map.put("checkOutDay", dateHelper.formatDate(booking.getCheckOutDay()));
			map.put("bookingDay", dateHelper.formatDate(booking.getBookingDay()));
			Map<String, String> details = new HashMap<>();
			for(BookingDetails detail : booking.getTblBookingDetailses()){
				details.put(detail.getTblRoomType().getName(), detail.getAmount() + "");
			}
			map.put("details", gson.toJson(details));
			
			bookingList.add(map);
		}
		return gson.toJson(bookingList);
	}

}
