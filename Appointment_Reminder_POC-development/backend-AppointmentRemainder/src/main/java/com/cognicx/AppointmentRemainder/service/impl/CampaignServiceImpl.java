package com.cognicx.AppointmentRemainder.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.CampaignWeekDetRequest;
import com.cognicx.AppointmentRemainder.Request.ReportRequest;
import com.cognicx.AppointmentRemainder.Request.UpdateCallDetRequest;
import com.cognicx.AppointmentRemainder.dao.CampaignDao;
import com.cognicx.AppointmentRemainder.response.GenericHeaderResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponseReport;
import com.cognicx.AppointmentRemainder.service.CampaignService;
import com.cognicx.AppointmentRemainder.util.AppointmentReminderUtil;
import com.cognicx.AppointmentRemainder.util.ExcelUtil;

@Service
public class CampaignServiceImpl implements CampaignService {

	@Autowired
	CampaignDao campaignDao;

	private Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

	@Override
	public ResponseEntity<GenericResponse> createCampaign(CampaignDetRequest campaignDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			String campaignId = campaignDao.createCampaign(campaignDetRequest);
			if (campaignId != null) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Campaign created successfully, Campaign Id: " + campaignId);
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while creating Campaign");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::createCampaign " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while creating Campaign");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponse> getCampaignDetail() {
		GenericResponse genericResponse = new GenericResponse();
		List<CampaignDetRequest> campaignDetList = null;
		try {
			campaignDetList = getCampaignDetList();
			genericResponse.setStatus(200);
			genericResponse.setValue(campaignDetList);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::createCampaign " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public List<CampaignDetRequest> getCampaignDetList() {
		List<CampaignDetRequest> campaignDetList;
		campaignDetList = new ArrayList<>();
		List<Object[]> campainDetObjList = campaignDao.getCampaignDet();
		Map<String, List<CampaignWeekDetRequest>> campainWeekDetList = campaignDao.getCampaignWeekDet();
		if (campainDetObjList != null && !campainDetObjList.isEmpty()) {
			for (Object[] obj : campainDetObjList) {
				CampaignDetRequest campaignDetRequest = new CampaignDetRequest();
				campaignDetRequest.setCampaignId(String.valueOf(obj[0]));
				campaignDetRequest.setCampaignName(String.valueOf(obj[1]));
				campaignDetRequest.setCampaignActive(String.valueOf(obj[3]));
				campaignDetRequest.setMaxAdvNotice(String.valueOf(obj[4]));
				campaignDetRequest.setRetryDelay(String.valueOf(obj[5]));
				campaignDetRequest.setRetryCount(String.valueOf(obj[6]));
				campaignDetRequest.setConcurrentCall(String.valueOf(obj[7]));
				campaignDetRequest.setStartDate(String.valueOf(obj[8]));
				campaignDetRequest.setStartTime(String.valueOf(obj[9]));
				campaignDetRequest.setEndDate(String.valueOf(obj[10]));
				campaignDetRequest.setEndTime(String.valueOf(obj[11]));
				campaignDetRequest.setFtpLocation(String.valueOf(obj[12]));
				if (obj[13] != null && !";".equalsIgnoreCase(String.valueOf(obj[13]))) {
					String[] ftpCredendials = String.valueOf(obj[13]).split(";");
					campaignDetRequest.setFtpUsername(ftpCredendials[0]);
					campaignDetRequest.setFtpPassword(ftpCredendials[1]);
				}
				campaignDetRequest.setFileName(String.valueOf(obj[14]));
				campaignDetRequest.setCallBefore(String.valueOf(obj[15]));
				if (campainWeekDetList != null && campainWeekDetList.containsKey(campaignDetRequest.getCampaignId()))
					campaignDetRequest.setWeekDaysTime(campainWeekDetList.get(campaignDetRequest.getCampaignId()));
				campaignDetList.add(campaignDetRequest);
			}
		}
		return campaignDetList;
	}

	@Override
	public ResponseEntity<GenericResponse> updateCampaign(CampaignDetRequest campaignDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			boolean isUpdated = campaignDao.updateCampaign(campaignDetRequest);
			if (isUpdated) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Campaign updated successfully");
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while updating Campaign");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::updateCampaign " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while updating Campaign");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponse> updateCallDetail(UpdateCallDetRequest updateCallDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			logger.info("**********UPDATE CALL DETAILS INPUT**********");
			logger.info("Caller response: " + updateCallDetRequest.getCallerResponse());
			logger.info("Call Status: " + updateCallDetRequest.getCallStatus());
			logger.info("Call Duration: " + updateCallDetRequest.getCallDuration());
			logger.info("Contact Id: " + updateCallDetRequest.getContactId());
			logger.info("Contact Id: " + updateCallDetRequest.getContactId());
			logger.info("Hangupcode: " + updateCallDetRequest.getHangupcode());
			boolean isUpdated = campaignDao.updateCallDetail(updateCallDetRequest);
			if (isUpdated) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Call Details updated successfully");
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while updating Call Details");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::updateCampaign " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while updating Call Details");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public boolean createContact(ContactDetDto contactDetDto) {
		boolean isCreated;
		try {
			isCreated = campaignDao.createContact(contactDetDto);
		} catch (Exception e) {
			return false;
		}
		return isCreated;
	}

	@Override
	public Map<String, List<ContactDetDto>> getContactDet() {
		return campaignDao.getContactDet();
	}

	@Override
	public ResponseEntity<GenericResponse> validateCampaignName(CampaignDetRequest campaignDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			boolean isValidated = campaignDao.validateCampaignName(campaignDetRequest);
			genericResponse.setStatus(200);
			genericResponse.setValue(isValidated);
			genericResponse.setMessage("Validation done");
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::updateCampaign " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue(true);
			genericResponse.setMessage("Error occured Validating Details");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponseReport> summaryReport(ReportRequest reportRequest) {
		GenericResponseReport genericResponse = new GenericResponseReport();
		List<GenericHeaderResponse> headerlist = null;
		List<GenericHeaderResponse> subHeaderlist = null;
		List<Map<Object, Object>> valueList = null;
		try {
			List<Object[]> resultList = campaignDao.getSummaryReportDet(reportRequest);
			if (resultList != null && !resultList.isEmpty()) {
				headerlist = new ArrayList<GenericHeaderResponse>();
				subHeaderlist = new ArrayList<GenericHeaderResponse>();
				valueList = new ArrayList<Map<Object, Object>>();
				subHeaderlist.add(new GenericHeaderResponse("Campaign Name", "campaignName"));
				subHeaderlist.add(new GenericHeaderResponse("Appointment Date", "date"));
				subHeaderlist.add(new GenericHeaderResponse("Total Contact", "totalContact"));
				subHeaderlist.add(new GenericHeaderResponse("Contacts Called", "contactCalled"));
				subHeaderlist.add(new GenericHeaderResponse("Contacts Connected", "contactConnected"));
				subHeaderlist.add(new GenericHeaderResponse("Ring No Answered", "answered"));
				subHeaderlist.add(new GenericHeaderResponse("Busy", "busy"));
				subHeaderlist.add(new GenericHeaderResponse("Others", "others"));
				subHeaderlist.add(new GenericHeaderResponse("Confirmed", "confirmed"));
				subHeaderlist.add(new GenericHeaderResponse("Cancelled", "canceleld"));
				subHeaderlist.add(new GenericHeaderResponse("Rescheduled", "rescheduled"));
				subHeaderlist.add(new GenericHeaderResponse("No Response", "noResponse"));
				headerlist.add(new GenericHeaderResponse("Campaign Summary Report", "", subHeaderlist));
				for (Object[] obj : resultList) {
					Map<Object, Object> valueMap = new LinkedHashMap<>();
					valueMap.put("campaignName", obj[0]);
					valueMap.put("date", obj[1]);
					valueMap.put("totalContact", obj[2]);
					valueMap.put("contactCalled", obj[3]);
					valueMap.put("contactConnected", obj[4]);
					valueMap.put("answered", obj[5]);
					valueMap.put("busy", obj[6]);
					valueMap.put("others", obj[11]);
					valueMap.put("confirmed", obj[7]);
					valueMap.put("canceleld", obj[8]);
					valueMap.put("rescheduled", obj[9]);
					valueMap.put("noResponse", obj[10]);
					valueList.add(valueMap);
				}
				genericResponse.setStatus(200);
				genericResponse.setHeader(headerlist);
				genericResponse.setValue(valueList);
				genericResponse.setMessage("Data fetched sucessfully");
			} else {
				genericResponse.setStatus(200);
				genericResponse.setValue(null);
				genericResponse.setMessage("No data found");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::summaryReport " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue(null);
			genericResponse.setMessage("Error occured generating report");
		}
		return new ResponseEntity<GenericResponseReport>(new GenericResponseReport(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponseReport> detailReport(ReportRequest reportRequest) {
		GenericResponseReport genericResponse = new GenericResponseReport();
		List<GenericHeaderResponse> headerlist = null;
		List<GenericHeaderResponse> subHeaderlist = null;
		List<Map<Object, Object>> valueList = null;
		List<String> contactIdList = null;
		Map<String, List<Map<Object, Object>>> callRetryDetMap = null;
		Map<String, String> callerChoice = new LinkedHashMap<>();
		callerChoice.put("1", "Confirmed");
		callerChoice.put("2", "Cancelled");
		callerChoice.put("3", "Reschedule");
		callerChoice.put("0", "No Response");
		try {
			List<Object[]> resultList = campaignDao.getContactDetailReport(reportRequest);
			if (resultList != null && !resultList.isEmpty()) {
				contactIdList = new ArrayList<String>();
				for (Object[] obj : resultList) {
					contactIdList.add(String.valueOf(obj[0]));
				}
				callRetryDetMap = campaignDao.getCallRetryDetail(contactIdList);
				headerlist = new ArrayList<GenericHeaderResponse>();
				subHeaderlist = new ArrayList<GenericHeaderResponse>();
				valueList = new ArrayList<Map<Object, Object>>();
				subHeaderlist.add(new GenericHeaderResponse("Patient Name", "patientName"));
				subHeaderlist.add(new GenericHeaderResponse("Campaign name", "campaignName"));
				subHeaderlist.add(new GenericHeaderResponse("Doctor Name", "doctorName"));
				subHeaderlist.add(new GenericHeaderResponse("Patient Contact No.", "contactNo"));
				subHeaderlist.add(new GenericHeaderResponse("Call Status", "callStatus"));
				subHeaderlist.add(new GenericHeaderResponse("Caller Choice", "callerChoice"));
				subHeaderlist.add(new GenericHeaderResponse("Appointment Date", "appointmentDate"));
				headerlist.add(new GenericHeaderResponse("Call Detail Report", "", subHeaderlist));
				for (Object[] obj : resultList) {
					Map<Object, Object> valueMap = new LinkedHashMap<>();
					valueMap.put("patientName", obj[4]);
					valueMap.put("campaignName", obj[2]);
					valueMap.put("doctorName", obj[3]);
					valueMap.put("contactNo", obj[5]);
					valueMap.put("callStatus", obj[8]);
					valueMap.put("callerChoice", obj[7] != null ? callerChoice.get(obj[7]) : null);
					valueMap.put("appointmentDate", obj[6]);
					Map<Object, Object> callRetryDetail = new LinkedHashMap<>();
					callRetryDetail.put("lastRetryStatus", obj[8]);
					callRetryDetail.put("retryCount", obj[10]);
					if (callRetryDetMap != null && callRetryDetMap.containsKey(String.valueOf(obj[0])))
						callRetryDetail.put("retryHistory", callRetryDetMap.get(String.valueOf(obj[0])));
					else
						callRetryDetail.put("retryHistory", null);
					valueMap.put("callRetryDetail", callRetryDetail);
					valueList.add(valueMap);
				}
				genericResponse.setStatus(200);
				genericResponse.setHeader(headerlist);
				genericResponse.setValue(valueList);
				genericResponse.setMessage("Data fetched sucessfully");
			} else {
				genericResponse.setStatus(200);
				genericResponse.setValue(null);
				genericResponse.setMessage("No data found");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::detailReport " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue(null);
			genericResponse.setMessage("Error occured generating report");
		}
		return new ResponseEntity<GenericResponseReport>(new GenericResponseReport(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<InputStreamResource> downloadDetailReport(ReportRequest reportRequest) {
		// String previousVal = null;
		Map<String, List<Map<Object, Object>>> callRetryDetMap = null;
		List<String> contactIdList = null;
		int maxHistoryCount = 0;
		Workbook workbook = new XSSFWorkbook();
		try {
			String currentDirectory = System.getProperty("user.dir");
			Sheet sheet1 = null;
			final String fileName = currentDirectory + "\\Detail_Report.xlsx";
			sheet1 = workbook.createSheet("Detail Report");
			sheet1.setDefaultColumnWidth(12);
			CellStyle style = ExcelUtil.getCellStyleForHeader(workbook);
			CellStyle styleContent = ExcelUtil.getCellStyleForContent(workbook);
			List<Object[]> contactDetList = campaignDao.getContactDetailReport(reportRequest);
			if (contactDetList != null && !contactDetList.isEmpty()) {
				contactIdList = new ArrayList<String>();
				for (Object[] obj : contactDetList) {
					contactIdList.add(String.valueOf(obj[0]));
				}
			}
			callRetryDetMap = campaignDao.getCallRetryDetail(contactIdList);
			for (Map.Entry<String, List<Map<Object, Object>>> entry : callRetryDetMap.entrySet()) {
				if (maxHistoryCount < entry.getValue().size())
					maxHistoryCount = entry.getValue().size();
			}
			int row = 0;
			// Summary View Sheet

			row++;
			Row searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Report Input Criteria");
			ExcelUtil.frameMerged(new CellRangeAddress(row, row, 2, 3), sheet1, workbook);

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Campaign Name");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3, reportRequest.getCampaignName());

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Start Date");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3, reportRequest.getStartDate());

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "End Date");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3, reportRequest.getEndDate());

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Doctor Name");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3,
					reportRequest.getDoctorName() == null ? "" : reportRequest.getDoctorName());

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Contact No.");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3,
					reportRequest.getContactNo() == null ? "" : reportRequest.getContactNo());
			ExcelUtil.setRegionBorderWithMedium(new CellRangeAddress(2, row, 2, 3), sheet1, workbook);

			row = row + 3;
			int firstRow = row;
			Row headerRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, headerRow, 0, "Call Detail Report");
			ExcelUtil.frameMerged(new CellRangeAddress(row, row, 0, 7), sheet1, workbook);
			int reportCell = 8;
			for (int i = 1; i <= maxHistoryCount; i++) {
				ExcelUtil.setCellValue(style, headerRow, reportCell, "Retry-" + i);
				ExcelUtil.frameMerged(new CellRangeAddress(row, row, reportCell, reportCell + 1), sheet1, workbook);
				reportCell = reportCell + 2;
			}

			row++;

			Row subHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, subHeaderRow, 0, "Patient Name");
			ExcelUtil.setCellValue(style, subHeaderRow, 1, "Campaign name");
			ExcelUtil.setCellValue(style, subHeaderRow, 2, "Doctor Name");
			ExcelUtil.setCellValue(style, subHeaderRow, 3, "Patient Contact No.");
			ExcelUtil.setCellValue(style, subHeaderRow, 4, "Call Status");
			ExcelUtil.setCellValue(style, subHeaderRow, 5, "Caller Choice");
			ExcelUtil.setCellValue(style, subHeaderRow, 6, "Appointment Date");
			ExcelUtil.setCellValue(style, subHeaderRow, 7, "Retry Count");
			int column = 7;
			for (int i = 1; i <= maxHistoryCount; i++) {
				ExcelUtil.setCellValue(style, subHeaderRow, column + 1, "Called On");
				ExcelUtil.setCellValue(style, subHeaderRow, column + 2, "Call Status");
				column = column + 2;
			}

			try {
				row++;
				if (contactDetList != null && !contactDetList.isEmpty()) {
					for (Object[] obj : contactDetList) {
						Row subHeaderValue = sheet1.createRow(row);
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 0, String.valueOf(obj[4]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 1, String.valueOf(obj[2]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 2, String.valueOf(obj[3]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 3, String.valueOf(obj[5]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 4, String.valueOf(obj[8]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 5,
								obj[7] != null ? AppointmentReminderUtil.getCallerChoice(String.valueOf(obj[7]))
										: null);
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 6, String.valueOf(obj[6]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 7, String.valueOf(obj[10]));
						column = 7;
						if (callRetryDetMap != null && callRetryDetMap.containsKey(String.valueOf(obj[0]))) {
							for (Map<Object, Object> retryHistory : callRetryDetMap.get(String.valueOf(obj[0]))) {
								ExcelUtil.setCellValue(styleContent, subHeaderValue, column + 1,
										String.valueOf(retryHistory.get("date")));
								ExcelUtil.setCellValue(styleContent, subHeaderValue, column + 2,
										(String) retryHistory.get("callStatus"));
								column = column + 2;
							}
						}
						row++;
					}
				}
				ExcelUtil.setRegionBorderWithMedium(
						new CellRangeAddress(firstRow, row - 1, 0, 7 + (maxHistoryCount * 2)), sheet1, workbook);
				sheet1.autoSizeColumn(2, false);
				sheet1.autoSizeColumn(3, true);
				sheet1.autoSizeColumn(6, false);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				FileOutputStream outputStream = new FileOutputStream(fileName);
				workbook.write(outputStream);
				workbook.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File file = new File(fileName);
			InputStreamResource resource1 = null;
			try {
				resource1 = new InputStreamResource(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
					.contentType(MediaType.parseMediaType("application/octet-stream")).contentLength(file.length())
					.body(resource1);

		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public ResponseEntity<InputStreamResource> downloadSummaryReport(ReportRequest reportRequest) {
		// String previousVal = null;
		Workbook workbook = new XSSFWorkbook();
		try {
			String currentDirectory = System.getProperty("user.dir");
			Sheet sheet1 = null;
			final String fileName = currentDirectory + "\\Summary_Report.xlsx";
			sheet1 = workbook.createSheet("Summary Report");
			sheet1.setDefaultColumnWidth(12);
			CellStyle style = ExcelUtil.getCellStyleForHeader(workbook);
			CellStyle styleContent = ExcelUtil.getCellStyleForContent(workbook);
			List<Object[]> contactSummaryList = campaignDao.getSummaryReportDet(reportRequest);

			int row = 0;
			// Summary View Sheet
			row++;
			Row searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Report Input Criteria");
			ExcelUtil.frameMerged(new CellRangeAddress(row, row, 2, 3), sheet1, workbook);

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Campaign Name");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3, reportRequest.getCampaignName());

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "Start Date");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3, reportRequest.getStartDate());

			row++;
			searchHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, searchHeaderRow, 2, "End Date");
			ExcelUtil.setCellValue(styleContent, searchHeaderRow, 3, reportRequest.getEndDate());
			ExcelUtil.setRegionBorderWithMedium(new CellRangeAddress(2, row, 2, 3), sheet1, workbook);

			row++;
			row++;
			row++;
			Row headerRow = sheet1.createRow(row);
			int firstRow = row;
			ExcelUtil.setCellValue(style, headerRow, 0, "Call Summary Report");
			ExcelUtil.frameMerged(new CellRangeAddress(row, row, 0, 11), sheet1, workbook);
			row++;

			Row subHeaderRow = sheet1.createRow(row);
			ExcelUtil.setCellValue(style, subHeaderRow, 0, "Campaign Name");
			ExcelUtil.setCellValue(style, subHeaderRow, 1, "Appointment Date");
			ExcelUtil.setCellValue(style, subHeaderRow, 2, "Total Contact");
			ExcelUtil.setCellValue(style, subHeaderRow, 3, "Contacts Called");
			ExcelUtil.setCellValue(style, subHeaderRow, 4, "Contacts Connected");
			ExcelUtil.setCellValue(style, subHeaderRow, 5, "Ring No Answered");
			ExcelUtil.setCellValue(style, subHeaderRow, 6, "Busy");
			ExcelUtil.setCellValue(style, subHeaderRow, 7, "Others");
			ExcelUtil.setCellValue(style, subHeaderRow, 8, "Confirmed");
			ExcelUtil.setCellValue(style, subHeaderRow, 9, "Cancelled");
			ExcelUtil.setCellValue(style, subHeaderRow, 10, "Rescheduled");
			ExcelUtil.setCellValue(style, subHeaderRow, 11, "No Response");

			try {
				row++;
				if (contactSummaryList != null && !contactSummaryList.isEmpty()) {
					for (Object[] obj : contactSummaryList) {
						Row subHeaderValue = sheet1.createRow(row);
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 0, String.valueOf(obj[0]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 1, String.valueOf(obj[1]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 2, String.valueOf(obj[2]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 3, String.valueOf(obj[3]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 4, String.valueOf(obj[4]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 5, String.valueOf(obj[5]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 6, String.valueOf(obj[6]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 7, String.valueOf(obj[11]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 8, String.valueOf(obj[7]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 9, String.valueOf(obj[8]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 10, String.valueOf(obj[9]));
						ExcelUtil.setCellValue(styleContent, subHeaderValue, 11, String.valueOf(obj[10]));
						row++;
					}
				}
				ExcelUtil.setRegionBorderWithMedium(new CellRangeAddress(firstRow, row - 1, 0, 11), sheet1, workbook);
				sheet1.autoSizeColumn(2, false);
				sheet1.autoSizeColumn(3, false);
				sheet1.autoSizeColumn(6, false);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				FileOutputStream outputStream = new FileOutputStream(fileName);
				workbook.write(outputStream);
				workbook.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File file = new File(fileName);
			InputStreamResource resource1 = null;
			try {
				resource1 = new InputStreamResource(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
					.contentType(MediaType.parseMediaType("application/octet-stream")).contentLength(file.length())
					.body(resource1);

		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public ResponseEntity<GenericResponse> getUploadHistory(ReportRequest reportRequest) {
		GenericResponse genericResponse = new GenericResponse();
		List<Object[]> resultList = null;
		List<UploadHistoryDto> uploadHistoryList = null;
		try {
			resultList = campaignDao.getUploadHistory(reportRequest);
			if (resultList != null && !resultList.isEmpty()) {
				uploadHistoryList = new ArrayList<>();
				for (Object[] obj : resultList) {
					UploadHistoryDto uploadHistoryDto = new UploadHistoryDto();
					uploadHistoryDto.setUploadHistoryId(String.valueOf(obj[0]));
					uploadHistoryDto.setCampaignId(String.valueOf(obj[1]));
					uploadHistoryDto.setCampaignName(String.valueOf(obj[2]));
					uploadHistoryDto.setUploadedOn(String.valueOf(obj[3]));
					uploadHistoryDto.setFilename(String.valueOf(obj[4]));
					uploadHistoryDto
							.setContactUploaded(campaignDao.getTotalContactNo(uploadHistoryDto.getUploadHistoryId()));
					uploadHistoryList.add(uploadHistoryDto);
				}
			}
			genericResponse.setStatus(200);
			genericResponse.setValue(uploadHistoryList);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::createCampaign " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponse> deleteContactByHistory(UpdateCallDetRequest updateCallDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			boolean isDeleted = campaignDao.deleteContactByHistory(updateCallDetRequest);
			if (isDeleted) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Contact deleted successfully");
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while deleting contact details");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::deleteContactByHistory " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured deleting contact details");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public BigInteger insertUploadHistory(UploadHistoryDto uploadHistoryDto) {
		BigInteger historyId = null;
		try {
			historyId = campaignDao.insertUploadHistory(uploadHistoryDto);
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::deleteContactByHistory " + e);
			return historyId;
		}
		return historyId;
	}

}
