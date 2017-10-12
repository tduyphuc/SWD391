package phuc.controller.service.booking;
import java.util.Map;

import phuc.entity.Booking;

public interface IBooking {
	public boolean bookRoom(Booking booking, String cusId, Map<Integer, Integer> details);
	public boolean checkValidBooking(Booking booking, Map<Integer, Integer> details);
}
