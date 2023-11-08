package com.cognicx.AppointmentRemainder.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.CampaignWeekDetRequest;
import com.cognicx.AppointmentRemainder.Request.ReportRequest;
import com.cognicx.AppointmentRemainder.Request.UpdateCallDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.CampaignQueryConstant;
import com.cognicx.AppointmentRemainder.dao.CampaignDao;
import com.cognicx.AppointmentRemainder.model.UploadHistoryDet;

@Repository("CampaignDao")
@Transactional
public class CampaignDaoImpl implements CampaignDao {
	private Logger logger = LoggerFactory.getLogger(CampaignDaoImpl.class);

	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;

	@Autowired
	@Qualifier("firstJdbcTemplate")
	JdbcTemplate firstJdbcTemplate;

	// private SessionFactory sessionFactory;

	@Override
	public String createCampaign(CampaignDetRequest campaignDetRequest) throws Exception {
		String campaignId = null;
		boolean isInserted;
		int insertVal;
		try {
			int idValue = getCampaignId();
			if (idValue > 9)
				campaignId = "C_" + String.valueOf(idValue);
			else
				campaignId = "C_0" + String.valueOf(idValue);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CAMPAIGN_DET);
			queryObj.setParameter("campaignId", campaignId);
			queryObj.setParameter("name", campaignDetRequest.getCampaignName());
			queryObj.setParameter("desc", campaignDetRequest.getCampaignName());
			if ("true".equalsIgnoreCase(campaignDetRequest.getCampaignActive()))
				queryObj.setParameter("status", 1);
			else
				queryObj.setParameter("status", 0);
			queryObj.setParameter("maxAdvTime", campaignDetRequest.getMaxAdvNotice());
			queryObj.setParameter("retryDelay", campaignDetRequest.getRetryDelay());
			queryObj.setParameter("retryCount", campaignDetRequest.getRetryCount());
			queryObj.setParameter("concurrentCall", campaignDetRequest.getConcurrentCall());
			queryObj.setParameter("startDate", campaignDetRequest.getStartDate());
			queryObj.setParameter("startTime", campaignDetRequest.getStartTime());
			queryObj.setParameter("endDate", campaignDetRequest.getEndDate());
			queryObj.setParameter("endTime", campaignDetRequest.getEndTime());
			queryObj.setParameter("ftpLocation", campaignDetRequest.getFtpLocation());
			if (!"".equalsIgnoreCase(campaignDetRequest.getFtpUsername())
					&& !"".equalsIgnoreCase(campaignDetRequest.getFtpPassword()))
				queryObj.setParameter("ftpCredentials",
						campaignDetRequest.getFtpUsername() + ";" + campaignDetRequest.getFtpPassword());
			else
				queryObj.setParameter("ftpCredentials", null);
			queryObj.setParameter("fileName", campaignDetRequest.getFileName());
			queryObj.setParameter("callBefore", campaignDetRequest.getCallBefore());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				campaignDetRequest.setCampaignId(campaignId);
				isInserted = createCampaignWeek(campaignDetRequest);
				if (isInserted)
					return campaignId;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::createCampaign" + e);
			return null;
		}
		return null;
	}

	private Integer getCampaignId() {
		String maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_ID);
			maxVal = (String) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignId" + e);
			return 1;
		}
		return Integer.valueOf(maxVal) + 1;
	}

	private boolean createCampaignWeek(CampaignDetRequest campaignDetRequest) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CAMPAIGN_WEEK_DET);
			for (CampaignWeekDetRequest campaignWeekDetRequest : campaignDetRequest.getWeekDaysTime()) {
				queryObj.setParameter("campaignId", campaignDetRequest.getCampaignId());
				queryObj.setParameter("day", campaignWeekDetRequest.getDay());
				if ("true".equalsIgnoreCase(campaignWeekDetRequest.getActive()))
					queryObj.setParameter("status", 1);
				else
					queryObj.setParameter("status", 0);
				queryObj.setParameter("startTime", campaignWeekDetRequest.getStartTime());
				queryObj.setParameter("endTime", campaignWeekDetRequest.getEndTime());
				queryObj.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::createCampaignWeek" + e);
			throw e;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCampaignDet() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_DET);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignDet" + e);
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<CampaignWeekDetRequest>> getCampaignWeekDet() {
		List<Object[]> resultList;
		Map<String, List<CampaignWeekDetRequest>> campaignWeekDet = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_WEEK_DET);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				String preVal = "";
				campaignWeekDet = new LinkedHashMap<>();
				for (Object[] obj : resultList) {
					if (!preVal.equalsIgnoreCase(String.valueOf(obj[1]))) {
						preVal = String.valueOf(obj[1]);
						campaignWeekDet.put(preVal, new ArrayList<CampaignWeekDetRequest>());
					}
					CampaignWeekDetRequest campaignWeekDetRequest = new CampaignWeekDetRequest();
					campaignWeekDetRequest.setCampaignId(preVal);
					campaignWeekDetRequest.setCampaignWeekId(String.valueOf(obj[0]));
					campaignWeekDetRequest.setDay(String.valueOf(obj[2]));
					campaignWeekDetRequest.setActive(String.valueOf(obj[3]));
					campaignWeekDetRequest.setStartTime(String.valueOf(obj[4]));
					campaignWeekDetRequest.setEndTime(String.valueOf(obj[5]));
					campaignWeekDet.get(preVal).add(campaignWeekDetRequest);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignWeekDet" + e);
			return campaignWeekDet;
		}
		return campaignWeekDet;
	}

	@Override
	public boolean updateCampaign(CampaignDetRequest campaignDetRequest) throws Exception {
		boolean isupdated;
		int insertVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_CAMPAIGN_DET);
			queryObj.setParameter("name", campaignDetRequest.getCampaignName());
			if ("true".equalsIgnoreCase(campaignDetRequest.getCampaignActive()))
				queryObj.setParameter("status", 1);
			else
				queryObj.setParameter("status", 0);
			queryObj.setParameter("maxAdvTime", campaignDetRequest.getMaxAdvNotice());
			queryObj.setParameter("retryDelay", campaignDetRequest.getRetryDelay());
			queryObj.setParameter("retryCount", campaignDetRequest.getRetryCount());
			queryObj.setParameter("concurrentCall", campaignDetRequest.getConcurrentCall());
			queryObj.setParameter("startDate", campaignDetRequest.getStartDate());
			queryObj.setParameter("startTime", campaignDetRequest.getStartTime());
			queryObj.setParameter("endDate", campaignDetRequest.getEndDate());
			queryObj.setParameter("endTime", campaignDetRequest.getEndTime());
			queryObj.setParameter("ftpLocation", campaignDetRequest.getFtpLocation());
			if (!"".equalsIgnoreCase(campaignDetRequest.getFtpUsername())
					&& !"".equalsIgnoreCase(campaignDetRequest.getFtpPassword()))
				queryObj.setParameter("ftpCredentials",
						campaignDetRequest.getFtpUsername() + ";" + campaignDetRequest.getFtpPassword());
			else
				queryObj.setParameter("ftpCredentials", null);
			queryObj.setParameter("callBefore", campaignDetRequest.getCallBefore());
			queryObj.setParameter("fileName", campaignDetRequest.getFileName());
			queryObj.setParameter("campaignId", campaignDetRequest.getCampaignId());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				isupdated = updateCampaignWeek(campaignDetRequest);
				if (isupdated)
					return true;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::updateCampaign" + e);
			return false;
		}
		return false;
	}

	private boolean updateCampaignWeek(CampaignDetRequest campaignDetRequest) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_CAMPAIGN_WEEK_DET);
			for (CampaignWeekDetRequest campaignWeekDetRequest : campaignDetRequest.getWeekDaysTime()) {
				queryObj.setParameter("day", campaignWeekDetRequest.getDay());
				if ("true".equalsIgnoreCase(campaignWeekDetRequest.getActive()))
					queryObj.setParameter("status", 1);
				else
					queryObj.setParameter("status", 0);
				queryObj.setParameter("startTime", campaignWeekDetRequest.getStartTime());
				queryObj.setParameter("endTime", campaignWeekDetRequest.getEndTime());
				queryObj.setParameter("campaignWeekId", campaignWeekDetRequest.getCampaignWeekId());
				queryObj.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::updateCampaignWeek" + e);
			throw e;
		}
		return true;
	}

	@Override
	public boolean updateCallDetail(UpdateCallDetRequest updateCallDetRequest) throws Exception {
		int insertVal, retryCount = 0;
		try {
			if (!"ANSWERED".equalsIgnoreCase(updateCallDetRequest.getCallStatus())) {
				retryCount = updateCallDetRequest.getRetryCount() + 1;
			} else if ("ANSWERED".equalsIgnoreCase(updateCallDetRequest.getCallStatus())) {
				if (updateCallDetRequest.getCallerResponse() == null
						|| updateCallDetRequest.getCallerResponse().isEmpty()) {
					updateCallDetRequest.setCallerResponse("0");
				}
				retryCount = updateCallDetRequest.getRetryCount();
			}
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_CALL_DET);
			queryObj.setParameter("callerResponse", updateCallDetRequest.getCallerResponse());
			queryObj.setParameter("callStatus", updateCallDetRequest.getCallStatus());
			queryObj.setParameter("callDuration", updateCallDetRequest.getCallDuration());
			queryObj.setParameter("retryCount", retryCount);
			queryObj.setParameter("contactId", updateCallDetRequest.getContactId());
			insertVal = queryObj.executeUpdate();
			queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CALL_RETRY_DET);
			queryObj.setParameter("contactId", updateCallDetRequest.getContactId());
			queryObj.setParameter("callStatus", updateCallDetRequest.getCallStatus());
			queryObj.setParameter("retryCount", retryCount);
			queryObj.executeUpdate();
			if (insertVal > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::updateCallDetail" + e);
			return false;
		}
		return false;
	}

	@Override
	public boolean createContact(ContactDetDto contactDetDto) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CONTACT_DET);
			queryObj.setParameter("campaignId", contactDetDto.getCampaignId());
			queryObj.setParameter("campaignName", contactDetDto.getCampaignName());
			queryObj.setParameter("doctorName", contactDetDto.getDoctorName());
			queryObj.setParameter("patientName", contactDetDto.getPatientName());
			queryObj.setParameter("contactNo", contactDetDto.getContactNo());
			queryObj.setParameter("appDate", contactDetDto.getAppointmentDate());
			queryObj.setParameter("language", contactDetDto.getLanguage());
			queryObj.setParameter("callStatus", "New");
			queryObj.setParameter("historyId", contactDetDto.getHistoryId());
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::createContact" + e);
			throw e;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<ContactDetDto>> getContactDet() {
		List<Object[]> resultList;
		Map<String, List<ContactDetDto>> campaignDetMap = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CONTACT_DET);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				String preVal = "";
				campaignDetMap = new LinkedHashMap<>();
				for (Object[] obj : resultList) {
					if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
						preVal = String.valueOf(obj[0]);
						campaignDetMap.put(preVal, new ArrayList<ContactDetDto>());
					}
					ContactDetDto contactDetDto = new ContactDetDto();
					contactDetDto.setCampaignId(preVal);
					contactDetDto.setCampaignName(String.valueOf(obj[1]));
					contactDetDto.setDoctorName(String.valueOf(obj[2]));
					contactDetDto.setPatientName(String.valueOf(obj[3]));
					contactDetDto.setContactNo(String.valueOf(obj[4]));
					contactDetDto.setAppointmentDate(String.valueOf(obj[5]));
					contactDetDto.setLanguage(String.valueOf(obj[6]));
					contactDetDto.setContactId(String.valueOf(obj[7]));
					contactDetDto.setCallRetryCount(String.valueOf(obj[8]));
					contactDetDto.setUpdatedDate(String.valueOf(obj[9]));
					contactDetDto.setCallStatus(String.valueOf(obj[10]));
					campaignDetMap.get(preVal).add(contactDetDto);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getContactDet" + e);
			return campaignDetMap;
		}
		return campaignDetMap;
	}

	@Override
	public boolean validateCampaignName(CampaignDetRequest campaignDetRequest) {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.VALIDATE_CAMPAIGN_NAME);
			queryObj.setParameter("name", campaignDetRequest.getCampaignName());
			int result = (int) queryObj.getSingleResult();
			if (result > 0)
				return false;
			else
				return true;
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::validateCampaignName" + e);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getSummaryReportDet(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SUMMARY_REPORT_DET);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			queryObj.setParameter("name", reportRequest.getCampaignId());
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getSummaryReportDet" + e);
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getContactDetailReport(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		StringBuilder query = null;
		try {
			query = new StringBuilder(CampaignQueryConstant.GET_CONTACT_DET_REPORT);
			if (reportRequest.getStartDate() != null && !reportRequest.getStartDate().isEmpty()
					&& reportRequest.getEndDate() != null && !reportRequest.getEndDate().isEmpty()) {
				query.append(" cast(appointment_date as date) between :startDate and :endDate and ");
			}
			if (reportRequest.getCampaignId() != null && !reportRequest.getCampaignId().isEmpty()) {
				query.append(" campaign_id=:name and ");
			}
			if (reportRequest.getContactNo() != null && !reportRequest.getContactNo().isEmpty()) {
				query.append(" contact_number=:contactNo and ");
			}
			if (reportRequest.getDoctorName() != null && !reportRequest.getDoctorName().isEmpty()) {
				query.append(" doctor_name=:doctorName and ");
			}
			if (reportRequest.getCallerChoice() != null && !reportRequest.getCallerChoice().isEmpty()) {
				query.append(" caller_response=:callerResponse and ");
			}
			query.append(" call_status is not null ");
			Query queryObj = firstEntityManager.createNativeQuery(query.toString());
			if (reportRequest.getStartDate() != null && !reportRequest.getStartDate().isEmpty()
					&& reportRequest.getEndDate() != null && !reportRequest.getEndDate().isEmpty()) {
				queryObj.setParameter("startDate", reportRequest.getStartDate());
				queryObj.setParameter("endDate", reportRequest.getEndDate());
			}
			if (reportRequest.getCampaignId() != null && !reportRequest.getCampaignId().isEmpty()) {
				queryObj.setParameter("name", reportRequest.getCampaignId());
			}
			if (reportRequest.getContactNo() != null && !reportRequest.getContactNo().isEmpty()) {
				queryObj.setParameter("contactNo", reportRequest.getContactNo());
			}
			if (reportRequest.getDoctorName() != null && !reportRequest.getDoctorName().isEmpty()) {
				queryObj.setParameter("doctorName", reportRequest.getDoctorName());
			}
			if (reportRequest.getCallerChoice() != null && !reportRequest.getCallerChoice().isEmpty()) {
				queryObj.setParameter("callerResponse", reportRequest.getCallerChoice());
			}

			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getContactDetailReport" + e);
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Map<Object, Object>>> getCallRetryDetail(List<String> contactIdList) {
		List<Object[]> resultList;
		Map<String, List<Map<Object, Object>>> callRetryDetMap = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_RETRY_DET);
			queryObj.setParameter("contactIdList", contactIdList);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				String preVal = "";
				callRetryDetMap = new LinkedHashMap<>();
				for (Object[] obj : resultList) {
					if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
						preVal = String.valueOf(obj[0]);
						callRetryDetMap.put(preVal, new ArrayList<Map<Object, Object>>());
					}
					Map<Object, Object> retryDetailsMap = new LinkedHashMap<>();
					retryDetailsMap.put("callStatus", obj[1]);
					retryDetailsMap.put("date", obj[2]);
					callRetryDetMap.get(preVal).add(retryDetailsMap);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCallRetryDetail" + e);
			return callRetryDetMap;
		}
		return callRetryDetMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUploadHistory(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_UPLOAD_HISTORY_DETIALS);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCallRetryDetail" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public boolean deleteContactByHistory(UpdateCallDetRequest updateCallDetRequest) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.DELETE_CONTACT_BY_HISTORY);
			queryObj.setParameter("historyId", updateCallDetRequest.getHistoryId());
			queryObj.executeUpdate();
			queryObj = firstEntityManager.createNativeQuery(
					"Update appointment_remainder.upload_history_det set status=0 where upload_history_id=:historyId");
			queryObj.setParameter("historyId", updateCallDetRequest.getHistoryId());
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::deleteContactByHistory" + e);
			return false;
		}
		return true;
	}

	@Override
	public Integer getTotalContactNo(String HistoryId) {
		int count;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(
					"select count(*) from appointment_remainder.contact_det where upload_history_id=:historyId");
			queryObj.setParameter("historyId", HistoryId);
			count = (int) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getTotalContactNo" + e);
			return 0;
		}
		return count;
	}

	@Override
	public BigInteger insertUploadHistory(UploadHistoryDto uploadHistoryDto) throws Exception {
		UploadHistoryDet uploadHistoryDet = null;
		try {
			uploadHistoryDet = new UploadHistoryDet();
			uploadHistoryDet.setCampaignId(uploadHistoryDto.getCampaignId());
			uploadHistoryDet.setCampaignName(uploadHistoryDto.getCampaignName());
			// uploadHistoryDet.setUploadedOn(new Date());
			uploadHistoryDet.setFilename(uploadHistoryDto.getFilename());
			firstEntityManager.persist(uploadHistoryDet);
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::insertUploadHistory" + e);
			throw e;
		}
		return uploadHistoryDet.getUploadHistoryId();
	}

}
