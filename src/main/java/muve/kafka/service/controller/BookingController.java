package muve.kafka.service.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import muve.kafka.service.model.BookingModel;
import muve.kafka.service.store.BookingStore;
import muve.kafka.service.utils.MUVEProducer;


@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private MUVEProducer muveProducer;
	
	@Autowired
    private BookingStore store;

    Gson gson = new Gson();
	
	@PostMapping
    public ResponseEntity<Void> save(@RequestBody BookingModel book,UriComponentsBuilder uriBuilder) {
		muveProducer.sendMessages(book);
        URI location = uriBuilder
                .path("/booking/{id}")
                .buildAndExpand(book.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
	
	@GetMapping
	@Primary
    public Collection<BookingModel> getArticles() {
        return store.getAll();
    }
	
	@GetMapping("/{id}")
    public Collection<BookingModel> getArticle(@PathVariable("id") String id) {
        return store.getByBookingId(id);
    }
	
//	@DeleteMapping("/{id}")
//    public void delete(@PathVariable("id") String id) {
//        muveProducer.deleteMessage(id);
//    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") String id, @RequestBody BookingModel book) {
        muveProducer.updateMessage(id, book);
    }

    @GetMapping("count")
    public long getCount() {
        return store.getSize();
    }
}
