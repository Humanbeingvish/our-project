package com.cognicx.AppointmentRemainder.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.CampaignWeekDetRequest;
import com.cognicx.AppointmentRemainder.Request.ReportRequest;
import com.cognicx.AppointmentRemainder.Request.UpdateCallDetRequest;

public interface CampaignDao {

	String createCampaign(CampaignDetRequest campaignDetRequest) throws Exception;

	Map<String, List<CampaignWeekDetRequest>> getCampaignWeekDet();

	List<Object[]> getCampaignDet();

	boolean updateCampaign(CampaignDetRequest campaignDetRequest) throws Exception;

	boolean updateCallDetail(UpdateCallDetRequest updateCallDetRequest) throws Exception;

	boolean createContact(ContactDetDto contactDetDto) throws Exception;

	Map<String, List<ContactDetDto>> getContactDet();

	boolean validateCampaignName(CampaignDetRequest campaignDetRequest);

	List<Object[]> getSummaryReportDet(ReportRequest reportRequest);

	List<Object[]> getContactDetailReport(ReportRequest reportRequest);

	Map<String, List<Map<Object, Object>>> getCallRetryDetail(List<String> contactIdList);

	List<Object[]> getUploadHistory(ReportRequest reportRequest);

	boolean deleteContactByHistory(UpdateCallDetRequest updateCallDetRequest) throws Exception;

	Integer getTotalContactNo(String HistoryId);

	BigInteger insertUploadHistory(UploadHistoryDto uploadHistoryDto) throws Exception;

}
