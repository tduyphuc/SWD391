package model.repo;

import java.util.Collection;

import model.entity.Booking;

public interface IBookingRepo {
	public void saveBooking(Booking booking);
	public Collection<Booking> getAllBooking();
}
