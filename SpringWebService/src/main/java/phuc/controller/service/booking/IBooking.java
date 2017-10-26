package phuc.controller.service.booking;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import phuc.entity.Booking;

public interface IBooking {
	public boolean bookRoom(Booking booking, String cusId, MultiValueMap<String, String> params);
	public boolean checkValidBooking(Booking booking, Map<Integer, Integer> details);
}
