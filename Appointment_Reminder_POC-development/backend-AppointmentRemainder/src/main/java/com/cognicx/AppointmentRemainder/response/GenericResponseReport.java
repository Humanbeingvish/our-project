package com.cognicx.AppointmentRemainder.response;

import java.sql.Timestamp;
import java.util.Date;

import com.cognicx.AppointmentRemainder.constant.MessageConstants;

public class GenericResponseReport {
	
private String timestamp;
private int status;
private String error;
private String message;
private Object header;
private Object value;

public GenericResponseReport() {}
public GenericResponseReport(GenericResponseReport genericResponse) {
	Date date= new Date();
	long time = date. getTime();
	Timestamp ts = new Timestamp(time);
	this.timestamp = ts.toString();
	this.status = genericResponse.status;
	this.error = genericResponse.error;
	this.message = genericResponse.message;
	this.value = genericResponse.value;
	this.header= genericResponse.header;
}

public GenericResponseReport(String messageType, String message) {
	
	Date date= new Date();
	long time = date. getTime();
	Timestamp ts = new Timestamp(time);
	this.timestamp = ts.toString();
	
	if(MessageConstants.SUCCESS.equalsIgnoreCase(messageType)) {
		this.status = MessageConstants.SUCCESS_STATUS_CODE;	
		this.message = message;		
	}
	else {
		this.status = MessageConstants.ERROR_STATUS_CODE;
		this.error = message;
	}
}

public GenericResponseReport(String messageType, Object value) {
	
	Date date= new Date();
	long time = date. getTime();
	Timestamp ts = new Timestamp(time);
	this.timestamp = ts.toString();
	
	if(MessageConstants.SUCCESS.equalsIgnoreCase(messageType)) {
		this.status = MessageConstants.SUCCESS_STATUS_CODE;	
		this.value = value;		
	}
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
public Object getValue() {
	return value;
}
public void setValue(Object value) {
	this.value = value;
}
public Object getHeader() {
	return header;
}
public void setHeader(Object header) {
	this.header = header;
}
}