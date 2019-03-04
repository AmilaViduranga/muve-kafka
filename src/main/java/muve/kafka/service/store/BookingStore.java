package muve.kafka.service.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import muve.kafka.service.model.BookingModel;


public class BookingStore {
	private Map<String,BookingModel> bookings = new HashMap<String, BookingModel>();

    public void save(BookingModel book) {
        bookings.put(Long.toString(book.getOffsetId()),book);
    }

    public Collection<BookingModel> getAll() {
        return bookings.values();
    }

    public BookingModel get(String id) {
        return bookings.get(id);
    }

    public long getSize() {
        return bookings.size();
    }

    public void delete(String key) {
        bookings.remove(key);
    }
    
    public Collection<BookingModel> getByBookingId(String id) {
    	Map<String, BookingModel> availableBookings = new HashMap<String, BookingModel>();
    	for(Map.Entry<String, BookingModel> entry : bookings.entrySet()) {
    	    BookingModel searchedResult = entry.getValue();
    	    if(searchedResult.getId().equals(id)) {
    	    	availableBookings.put(Long.toString(searchedResult.getOffsetId()), searchedResult);
    	    }
    	}
    	return availableBookings.values();
    }
}
