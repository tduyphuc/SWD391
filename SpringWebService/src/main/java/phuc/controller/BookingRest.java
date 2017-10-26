package phuc.controller;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import phuc.controller.service.booking.IBooking;
import phuc.entity.Booking;
import phuc.utils.IDateHelper;
import phuc.utils.IJSonHelper;
import phuc.utils.ResponseCode;

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
	
	private Booking createBooking(MultiValueMap<String, String> params){
		Booking booking = new Booking();
		Integer numberOfPerson = Integer.valueOf(params.getFirst("numberOfPerson"));
		Date arrivalDay = dateHelper.convertToDate(params.getFirst("arrivalDay"));
		Date checkOutDay = dateHelper.convertToDate(params.getFirst("checkOutDay"));
		Date bookingDay = dateHelper.getCurrentDay();
		String comment = params.getFirst("comment");
		
		booking.setNumberOfPerson(numberOfPerson);
		booking.setArrivalDay(arrivalDay);
		booking.setCheckOutDay(checkOutDay);
		booking.setComment(comment);
		booking.setBookingDay(bookingDay);
		return booking;
	}
		
}
