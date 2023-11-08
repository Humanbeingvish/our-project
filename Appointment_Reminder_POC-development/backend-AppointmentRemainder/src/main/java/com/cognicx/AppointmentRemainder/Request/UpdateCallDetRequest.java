package com.cognicx.AppointmentRemainder.Request;

public class UpdateCallDetRequest {

	private String contactId;
	private String callerResponse;
	private String callStatus;
	private String callDuration;
	private String hangupcode;
	private String historyId;
	private int retryCount;

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getCallerResponse() {
		return callerResponse;
	}

	public void setCallerResponse(String callerResponse) {
		this.callerResponse = callerResponse;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getHangupcode() {
		return hangupcode;
	}

	public void setHangupcode(String hangupcode) {
		this.hangupcode = hangupcode;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

}
