package muve.kafka.service.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import muve.kafka.service.model.Booking;


public class BookingStore {
	private Map<String,Booking> bookings = new HashMap<String, Booking>();

    public void save(Booking book) {
        bookings.put(Long.toString(book.getOffsetId()),book);
    }

    public Collection<Booking> getAll() {
        return bookings.values();
    }

    public Booking get(String id) {
        return bookings.get(id);
    }

    public long getSize() {
        return bookings.size();
    }

    public void delete(String key) {
        bookings.remove(key);
    }
    
    public Collection<Booking> getByBookingId(String id) {
    	Map<String, Booking> availableBookings = new HashMap<String, Booking>();
    	for(Map.Entry<String, Booking> entry : bookings.entrySet()) {
    	    Booking searchedResult = entry.getValue();
    	    if(searchedResult.getId().equals(id)) {
    	    	availableBookings.put(Long.toString(searchedResult.getOffsetId()), searchedResult);
    	    }
    	}
    	return availableBookings.values();
    }
}
