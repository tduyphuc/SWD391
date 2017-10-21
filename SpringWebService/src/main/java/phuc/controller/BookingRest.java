package phuc.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import phuc.controller.repo.IPaymentRepo;
import phuc.controller.repo.IPaymentTypeRepo;
import phuc.controller.service.booking.IBooking;
import phuc.controller.service.room.IRoomInfo;
import phuc.entity.Booking;
import phuc.entity.Payment;
import phuc.entity.PaymentType;
import phuc.utils.IJSonHelper;
import phuc.utils.ResponseCode;

@RestController
@RequestMapping(value="booking")
public class BookingRest {
	
	private Gson gson = new Gson();

	@Autowired
	private IBooking bookingService;
	
	@Autowired
	private IRoomInfo roomInfo;
	
	@Autowired
	private IPaymentTypeRepo typeRepo;
	
	@Autowired
	private IPaymentRepo paymentRepo;
	
	@Autowired
	private IJSonHelper json;
	
	@RequestMapping(value = "/book", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String regist(@RequestBody MultiValueMap<String, String> params) {
		
		Booking booking = createBooking(params);
		Map<Integer, Integer> details = createDetails(params);
		String cusId = params.getFirst("cusId");
		
		boolean isBookingSuccess = bookingService.bookRoom(booking, cusId, details);
		if(isBookingSuccess){
			Payment payment = createPayment(params, details);
			try {
				paymentRepo.savePayment(payment);
				return json.toResponseMessage(ResponseCode.OK_CODE, "REGIST OK");
			} catch (Exception e) {
				System.out.println("Payment_Exception: " + e.getMessage());
			}
		}
		return json.toResponseMessage(ResponseCode.ERROR_CODE_DENIED, "REGIST FAILED");		
	}
	
	private Booking createBooking(MultiValueMap<String, String> params){
		Booking booking = new Booking();
		Integer numberOfPerson = Integer.valueOf(params.getFirst("numberOfPerson"));
		Date arrivalDay = new Date(Long.valueOf(params.getFirst("arrivalDay")));
		Date checkOutDay = new Date(Long.valueOf(params.getFirst("checkOutDay")));
		String comment = params.getFirst("comment");
		
		booking.setNumberOfPerson(numberOfPerson);
		booking.setArrivalDay(arrivalDay);
		booking.setCheckOutDay(checkOutDay);
		booking.setComment(comment);
		return booking;
	}
	
	private Map<Integer, Integer> createDetails(MultiValueMap<String, String> params){
		String detailsParam = params.getFirst("details");		
		Map<Integer, Integer> details = new HashMap<>();		
		details = gson.fromJson(detailsParam, details.getClass());
		return details;
	}
	
	private Payment createPayment(MultiValueMap<String, String> params, Map<Integer, Integer> details){
		Payment payment = new Payment();
		Date payTime = new Date(Long.valueOf(params.getFirst("payTime")));
		Double paid = Double.valueOf(params.getFirst("paid"));
		Double amount = roomInfo.getTotalPrice(details);
		PaymentType paymentType = typeRepo.getPaymentType(Integer.valueOf(params.getFirst("paymentType")));
		
		payment.setPayTime(payTime);
		payment.setPaid(paid);
		payment.setAmount(amount);
		payment.setTblPaymentType(paymentType);
		
		return payment;
	}
	
}
