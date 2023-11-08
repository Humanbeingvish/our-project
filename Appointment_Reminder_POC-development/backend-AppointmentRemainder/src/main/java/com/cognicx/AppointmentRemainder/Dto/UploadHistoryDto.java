package com.cognicx.AppointmentRemainder.Dto;

public class UploadHistoryDto {

	private String uploadHistoryId;
	private String campaignId;
	private String campaignName;
	private String uploadedOn;
	private String filename;
	private int ContactUploaded;

	public String getUploadHistoryId() {
		return uploadHistoryId;
	}

	public void setUploadHistoryId(String uploadHistoryId) {
		this.uploadHistoryId = uploadHistoryId;
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

	public String getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(String uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getContactUploaded() {
		return ContactUploaded;
	}

	public void setContactUploaded(int contactUploaded) {
		ContactUploaded = contactUploaded;
	}

}
