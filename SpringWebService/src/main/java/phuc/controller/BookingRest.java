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

@RestController
@RequestMapping(value="booking")
public class BookingRest {

	@Autowired
	private IBooking booking;
	
	@RequestMapping(value = "/regist", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String regist(@RequestBody MultiValueMap<String, String> params) {
		
		return "RESULT: ";		
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
	
	
}
