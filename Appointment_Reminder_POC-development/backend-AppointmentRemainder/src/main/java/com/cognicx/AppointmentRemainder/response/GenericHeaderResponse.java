package com.cognicx.AppointmentRemainder.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GenericHeaderResponse {
	
	public String label;
	
	public String value;
	
	public Object subHeaders;
	
	
	public GenericHeaderResponse() {}
	
	public GenericHeaderResponse(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
	public GenericHeaderResponse(String label, String value, Object subHeaders) {
		this.label = label;
		this.value = value;
		this.subHeaders = subHeaders;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getSubHeaders() {
		return subHeaders;
	}

	public void setSubHeaders(Object subHeaders) {
		this.subHeaders = subHeaders;
	}
	
	
	
}
