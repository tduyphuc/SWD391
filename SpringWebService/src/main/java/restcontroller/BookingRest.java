package restcontroller;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import data.entity.Booking;
import service.booking.IBooking;
import utils.IDateHelper;
import utils.IJSonHelper;
import utils.ResponseCode;

@RestController
@RequestMapping(value="booking")
public class BookingRest {
	
	@Autowired
	private IBooking bookingService;
	
	@Autowired
	private IJSonHelper json;
	
	@Autowired
	private IDateHelper dateHelper;
	
	@RequestMapping(value = "/book", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String regist(@RequestBody MultiValueMap<String, String> params) {
		
		Booking booking = createBooking(params);
		String cusId = params.getFirst("cusId");		
		boolean isBookingSuccess = bookingService.bookRoom(booking, cusId, params);
		if(isBookingSuccess){
			return json.toResponseMessage(ResponseCode.OK_CODE, "BOOKING SUCCESS");
		}
		return json.toResponseMessage(ResponseCode.ERROR_CODE_DENIED, "BOOKING FAILED");		
	}
	
	@RequestMapping(value = "/getAllBooking", method = RequestMethod.GET)
	public String getAllBooking() {		
		return bookingService.getAllBooking();		
	}
	
	private Booking createBooking(MultiValueMap<String, String> params){
		Booking booking = new Booking();
		System.out.println("Adult: " + params.getFirst("adult"));
		System.out.println("Child: " + params.getFirst("child"));
		Integer adult = Integer.valueOf(params.getFirst("adult"));
		Integer child = Integer.valueOf(params.getFirst("child"));
		Date arrivalDay = dateHelper.convertToDate(params.getFirst("arrivalDay"));
		Date checkOutDay = dateHelper.convertToDate(params.getFirst("checkOutDay"));
		Date bookingDay = dateHelper.getCurrentDay();
		String comment = params.getFirst("comment");
		
		booking.setAdult(adult);
		booking.setChild(child);
		booking.setArrivalDay(arrivalDay);
		booking.setCheckOutDay(checkOutDay);
		booking.setComment(comment);
		booking.setBookingDay(bookingDay);
		return booking;
	}
		
}
