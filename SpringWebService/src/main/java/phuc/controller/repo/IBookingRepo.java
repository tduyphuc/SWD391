package phuc.controller.repo;

import java.util.Collection;
import phuc.entity.Booking;

public interface IBookingRepo {
	public void saveBooking(Booking booking);
	public Collection<Booking> getAllBooking();
}
