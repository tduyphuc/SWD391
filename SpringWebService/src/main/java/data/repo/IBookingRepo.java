package data.repo;

import java.util.Collection;

import data.entity.Booking;

public interface IBookingRepo {
	public void saveBooking(Booking booking);
	public Collection<Booking> getAllBooking();
}
