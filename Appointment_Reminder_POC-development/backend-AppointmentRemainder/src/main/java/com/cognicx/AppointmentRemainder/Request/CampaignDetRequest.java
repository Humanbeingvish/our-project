package com.cognicx.AppointmentRemainder.Request;

import java.util.List;

public class CampaignDetRequest {

	private String campaignId;
	private String campaignName;
	private String campaignActive;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String maxAdvNotice;
	private String retryDelay;
	private String retryCount;
	private String concurrentCall;
	private String ftpLocation;
	private String ftpUsername;
	private String ftpPassword;
	private String fileName;
	private String callBefore;
	private List<CampaignWeekDetRequest> weekDaysTime;

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignActive() {
		return campaignActive;
	}

	public void setCampaignActive(String campaignActive) {
		this.campaignActive = campaignActive;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMaxAdvNotice() {
		return maxAdvNotice;
	}

	public void setMaxAdvNotice(String maxAdvNotice) {
		this.maxAdvNotice = maxAdvNotice;
	}

	public String getRetryDelay() {
		return retryDelay;
	}

	public void setRetryDelay(String retryDelay) {
		this.retryDelay = retryDelay;
	}

	public String getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(String retryCount) {
		this.retryCount = retryCount;
	}

	public String getConcurrentCall() {
		return concurrentCall;
	}

	public void setConcurrentCall(String concurrentCall) {
		this.concurrentCall = concurrentCall;
	}

	public String getFtpLocation() {
		return ftpLocation;
	}

	public void setFtpLocation(String ftpLocation) {
		this.ftpLocation = ftpLocation;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public List<CampaignWeekDetRequest> getWeekDaysTime() {
		return weekDaysTime;
	}

	public void setWeekDaysTime(List<CampaignWeekDetRequest> weekDaysTime) {
		this.weekDaysTime = weekDaysTime;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCallBefore() {
		return callBefore;
	}

	public void setCallBefore(String callBefore) {
		this.callBefore = callBefore;
	}

}
