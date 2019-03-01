package muve.kafka.service.model;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;

public class Booking {
	private String id;
    private String name;
    private BigDecimal price;
    private String description;
    private String eventId;
    private String createdDate;
    private long offsetId;
    private String action;
    
    
    
    
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public long getOffsetId() {
		return offsetId;
	}
	public void setOffsetId(long offsetId) {
		this.offsetId = offsetId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public Booking() {
		Date date = new Date();
		java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
		this.eventId = Long.toString(date.getTime());
		this.createdDate = ts.toString();
	}
}
