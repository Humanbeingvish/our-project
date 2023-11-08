package com.cognicx.AppointmentRemainder.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "upload_history_det", schema = "appointment_remainder")
@NamedQuery(name = "UploadHistoryDet.findAll", query = "SELECT u FROM UploadHistoryDet u")
public class UploadHistoryDet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "upload_history_id")
	private BigInteger uploadHistoryId;

	@Column(name = "campaign_id")
	private String campaignId;

	@Column(name = "campaign_name")
	private String campaignName;

	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "uploaded_on")
	private Date uploadedOn;

	@Column(name = "file_name")
	private String filename;

	public BigInteger getUploadHistoryId() {
		return uploadHistoryId;
	}

	public void setUploadHistoryId(BigInteger uploadHistoryId) {
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

	public Date getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
