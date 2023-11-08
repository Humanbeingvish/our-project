package com.cognicx.AppointmentRemainder.response;

import java.sql.Timestamp;
import java.util.Date;

public class GenericResponse {

	private String timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
	private Object value;

	public GenericResponse() {
	}

	public GenericResponse(String message, String error) {
		this.message = message;
		this.error = error;
	}

	public GenericResponse(GenericResponse genericResponse) {
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		this.timestamp = ts.toString();
		this.status = genericResponse.status;
		this.error = genericResponse.error;
		this.message = genericResponse.message;
		this.path = genericResponse.path;
		this.value = genericResponse.value;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}