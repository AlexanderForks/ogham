package fr.sii.notification.context;

import java.util.Date;

public class SimpleBean {
	private String name;
	
	private int value;
	
	private Date date;

	public SimpleBean(String name, int value, Date date) {
		super();
		this.name = name;
		this.value = value;
		this.date = date;
	}

	public SimpleBean(String name, int value) {
		this(name, value, new Date());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"name\": \"").append(name).append("\", \"value\": ").append(value).append(", \"date\": \"").append(date).append("\"}");
		return builder.toString();
	}
	
	
}
