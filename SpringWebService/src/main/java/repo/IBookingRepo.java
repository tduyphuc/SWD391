package repo;

import java.util.Collection;

import entity.Booking;

public interface IBookingRepo {
	public void saveBooking(Booking booking);
	public Collection<Booking> getAllBooking();
}
