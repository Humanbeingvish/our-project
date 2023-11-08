package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

public class ContactDetDto {

	private String contactId;
	private String campaignId;
	private String campaignName;
	private String doctorName;
	private String language;
	private String patientName;
	private String contactNo;
	private String appointmentDate;
	private String callRetryCount;
	private String updatedDate;
	private String callStatus;
	private String failureReason;
	private BigInteger historyId;

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getCallRetryCount() {
		return callRetryCount;
	}

	public void setCallRetryCount(String callRetryDelay) {
		this.callRetryCount = callRetryDelay;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public BigInteger getHistoryId() {
		return historyId;
	}

	public void setHistoryId(BigInteger historyId) {
		this.historyId = historyId;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

}
