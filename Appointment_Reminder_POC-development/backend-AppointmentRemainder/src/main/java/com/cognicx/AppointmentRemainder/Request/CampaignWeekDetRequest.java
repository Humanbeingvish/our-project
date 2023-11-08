package com.cognicx.AppointmentRemainder.Request;

public class CampaignWeekDetRequest {

	private String campaignWeekId;
	private String campaignId;
	private String day;
	private String startTime;
	private String endTime;
	private String active;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCampaignWeekId() {
		return campaignWeekId;
	}

	public void setCampaignWeekId(String campaignWeekId) {
		this.campaignWeekId = campaignWeekId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

}
