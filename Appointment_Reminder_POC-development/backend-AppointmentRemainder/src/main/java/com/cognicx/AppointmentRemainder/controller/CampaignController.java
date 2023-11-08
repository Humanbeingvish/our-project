package com.cognicx.AppointmentRemainder.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.CampaignWeekDetRequest;
import com.cognicx.AppointmentRemainder.Request.ReportRequest;
import com.cognicx.AppointmentRemainder.Request.UpdateCallDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponseReport;
import com.cognicx.AppointmentRemainder.service.CampaignService;
import com.cognicx.AppointmentRemainder.service.impl.CampaignServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@RestController
@CrossOrigin
@RequestMapping("/campaign")
public class CampaignController {

	@Autowired
	CampaignService campaignService;

	@Value("${app.isFTP}")
	private String isFTP;

	@Value("${app.fileDirectory}")
	private String fileDirectory;

	@Value("${call.apiurl}")
	private String callApi;

	@Value("${failure.filediectory}")
	private String failureDirectory;

	private static Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

	// @Scheduled(cron = "0 0/2 * * * *")
	@PostMapping("/uploadSftpContact")
	public void setupJsch() throws JSchException, SftpException, IOException {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		boolean isFileFound = true;
		try {
			String fileTimestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
			if (campaignDetList != null && !campaignDetList.isEmpty()) {
				for (CampaignDetRequest campaignDetRequest : campaignDetList) {
					isFileFound = true;
					String fileName = campaignDetRequest.getFileName();
					logger.info("****Getting SFTP session****");
					session = jsch.getSession(campaignDetRequest.getFtpUsername(), campaignDetRequest.getFtpLocation(),
							22);
					session.setConfig("StrictHostKeyChecking", "no");
					session.setPassword(campaignDetRequest.getFtpPassword());
					InputStream stream = null;
					if (isFTP != null && "true".equalsIgnoreCase(isFTP)) {
						session.connect();
						Channel channel = session.openChannel("sftp");
						channel.connect();
						sftpChannel = (ChannelSftp) channel;
						logger.info("****Got SFTP channel ****");
						// sftpChannel.cd("/www/eappzz.com/reminder1");
						logger.info(
								"****Getting '" + campaignDetRequest.getFileName() + "' file from SFTP channel ****");
						try {
							stream = sftpChannel.get(campaignDetRequest.getFileName());
						} catch (SftpException e) {
							logger.error("SftpException occurred in Retriving" + campaignDetRequest.getFileName()
									+ "file from SFTP");
							isFileFound = false;
						} catch (Exception e) {
							logger.error("Exception occurred in Retriving" + campaignDetRequest.getFileName()
									+ "file from SFTP");
							isFileFound = false;
						}
					} else {
						stream = null;
					}
					if (isFileFound) {
						BigInteger historyId = getUploadHistoryid(campaignDetRequest, fileName);
						List<ContactDetDto> contactDetList = csvToData(stream, historyId, isFTP, fileDirectory,
								fileTimestamp, failureDirectory, campaignDetList, new ArrayList<>());
						logger.info("****Converted CSV DATA to Object****");
						if (stream != null)
							stream.close();
						logger.info("****Inserting contact details to DB Table****");
						for (ContactDetDto contactDetDto : contactDetList) {
							campaignService.createContact(contactDetDto);
						}
						String[] file = fileName.split("\\.");
						if (sftpChannel != null) {
							sftpChannel.rename(fileName, file[0] + "_" + fileTimestamp + "." + file[1]);
							sftpChannel.exit();
						}
					}
					session.disconnect();
				}
			}
		} catch (IOException io) {
			logger.error("IO Exception occurred file upload from SFTP server due to " + io.getMessage());
		} catch (JSchException e) {
			logger.error("JSchException occurred file upload from SFTP server due to " + e.getMessage());
			e.printStackTrace();
		} catch (SftpException e) {
			logger.error("SftpException occurred file upload from SFTP server due to " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception occurred during file upload from SFTP server due to " + e.getMessage());
		} finally {
			if (sftpChannel != null)
				sftpChannel.exit();
			session.disconnect();
		}
	}

	private BigInteger getUploadHistoryid(CampaignDetRequest campaignDetRequest, String fileName) {
		UploadHistoryDto uploadHistoryDto = new UploadHistoryDto();
		uploadHistoryDto.setCampaignId(campaignDetRequest.getCampaignId());
		uploadHistoryDto.setCampaignName(campaignDetRequest.getCampaignName());
		uploadHistoryDto.setFilename(fileName);
		BigInteger historyId = campaignService.insertUploadHistory(uploadHistoryDto);
		return historyId;
	}

	// @Scheduled(cron = "0 0/2 * * * *")
	@PostMapping("/uploadContact")
	public void uploadContact() {
		FTPClient client = new FTPClient();
		InputStream in;
		try {
			List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
			if (campaignDetList != null && !campaignDetList.isEmpty()) {
				for (CampaignDetRequest campaignDetRequest : campaignDetList) {
					client.connect(campaignDetRequest.getFtpLocation(), 21);
					boolean isSuccess = client.login(campaignDetRequest.getFtpUsername(),
							campaignDetRequest.getFtpPassword());
//					List<ContactDetDto> contactDetList = csvToData(null);
//					for (ContactDetDto contactDetDto : contactDetList) {
//						campaignService.createContact(contactDetDto);
//					}
					if (isSuccess) {
						List<String> fileName = new ArrayList<>();
						// client.changeWorkingDirectory("/eappzz.com/reminder1");
						FTPFile[] files = client.listFiles();
						for (FTPFile file : files) {
							if (file.isFile()) {
								fileName.add(file.getName());
								logger.info("File Names file.getName()");
							}
						}
						if (fileName.contains(campaignDetRequest.getFileName())) {
							logger.info("Inside If condition");
							in = client.retrieveFileStream(campaignDetRequest.getFileName());
							// List<ContactDetDto> contactDetList = csvToData(in, null);
							boolean store = client.storeFile("tez.csv", in);
							in.close();
							String newFileName = "campaign_new.csv";
							boolean isRenamed = client.rename(campaignDetRequest.getFileName(), "test.csv");
							logger.info("Renamed Status:: " + isRenamed);
							client.disconnect();
//							for (ContactDetDto contactDetDto : contactDetList) {
//								campaignService.createContact(contactDetDto);
//							}
						} else {
							logger.info("In ftp Fileupload:: expected file '" + campaignDetRequest.getFileName()
									+ "' is not there");
							client.disconnect();
						}

					}
				}
			}
//			client.connect("eappzz.com", 21);
//			boolean isSuccess = client.login("test1@eappzz.com", "2u42*(1t5#to");
//			client.changeWorkingDirectory("/eappzz.com/reminder1");
//
//			String filename = "campaign.csv";
//			InputStream in = client.retrieveFileStream(filename);

			// List<ContactDetDto> contactDetList = csvToData(in);
			// client.disconnect();
			// in.close();
			// for (ContactDetDto contactDetDto : contactDetList) {
			// campaignService.createContact(contactDetDto);
			// }
			// retrieveFile("/" + filename, in);
		} catch (Exception e) {
			logger.error("Error occured in FTP File upload:: " + e);
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// return null;
	}

	private static List<ContactDetDto> csvToData(InputStream is, BigInteger historyId, String isFTP,
			String fileDirectory, String fileTimestamp, String failureDirectory,
			List<CampaignDetRequest> campaignDetList, List<ContactDetDto> failureList) {
		List<ContactDetDto> contactList = null;
		// List<ContactDetDto> failureList = null;
		CSVPrinter csvPrinter = null;
		CSVParser csvParser = null;
		BufferedReader fileReader = null;
		try {
			StringBuilder reason = null;
			if (isFTP != null && "true".equalsIgnoreCase(isFTP)) {
				fileReader = new BufferedReader(new InputStreamReader(is));
			} else {
				fileReader = new BufferedReader(new FileReader(fileDirectory));
			}
			csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

			contactList = new ArrayList<>();
			// failureList = new ArrayList<>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
				reason = new StringBuilder();
				ContactDetDto contactDet = new ContactDetDto();
				contactDet.setCampaignId(csvRecord.get("campaign id"));
				contactDet.setCampaignName(csvRecord.get("campaign name"));
				contactDet.setDoctorName(csvRecord.get("doctor name"));
				contactDet.setPatientName(csvRecord.get("patient name"));
				contactDet.setContactNo(csvRecord.get("contact number"));
				contactDet.setAppointmentDate(csvRecord.get("appointment date"));
				contactDet.setLanguage(csvRecord.get("language"));
				contactDet.setHistoryId(historyId);
				if (validateFileData(csvRecord, reason, campaignDetList, contactDet)) {
					contactList.add(contactDet);
				} else {
					contactDet.setFailureReason(reason.toString());
					failureList.add(contactDet);
				}
			}
			csvParser.close();
			if (!failureList.isEmpty()) {
				// csvPrinter = failureCsvData(fileTimestamp, failureList, failureDirectory);
			}
		} catch (IOException e) {
			logger.error("fail to parse CSV file: " + e.getMessage());
		} catch (Exception e) {
			logger.error("fail to parse CSV file: " + e.getMessage());
		} finally {
			try {
				csvParser.close();
				if (csvPrinter != null)
					csvPrinter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contactList;
	}

	private static CSVPrinter failureCsvData(String fileTimestamp, List<ContactDetDto> failureList,
			String failureDirectory) throws IOException {
		CSVPrinter csvPrinter;
		List<String> headerlist = new ArrayList<>(Arrays.asList("campaign id", "campaign name", "doctor name",
				"patient name", "contact number", "appointment date", "language", "reason"));
		final CSVFormat format = CSVFormat.DEFAULT.withHeader(headerlist.toArray(new String[0]));
		Writer writer = Files.newBufferedWriter(Paths.get(failureDirectory + fileTimestamp + ".csv"));
		csvPrinter = new CSVPrinter(writer, format);
		for (ContactDetDto contactDet : failureList) {
			csvPrinter
					.printRecord(new ArrayList<>(Arrays.asList(contactDet.getCampaignId(), contactDet.getCampaignName(),
							contactDet.getDoctorName(), contactDet.getPatientName(), contactDet.getContactNo(),
							contactDet.getAppointmentDate(), contactDet.getLanguage(), contactDet.getFailureReason())));
		}
		csvPrinter.flush();
		return csvPrinter;
	}

	private static boolean validateFileData(CSVRecord csvRecord, StringBuilder reason,
			List<CampaignDetRequest> campaignDetList, ContactDetDto contactDet) {
		boolean isValid = true;
		if (csvRecord.get("campaign id") == null || csvRecord.get("campaign id").isEmpty()) {
			reason.append("Campaign ID is missing;");
			isValid = false;
		} else {
			CampaignDetRequest commonDetail = campaignDetList.stream()
					.filter(x -> csvRecord.get("campaign id").equalsIgnoreCase(x.getCampaignId())).findAny()
					.orElse(null);
			if (commonDetail == null) {
				reason.append("Campaign Id is Incorrect;");
				isValid = false;
			}
		}
		if (csvRecord.get("campaign name") == null || csvRecord.get("campaign name").isEmpty()) {
			reason.append("Campaign name is missing;");
			isValid = false;
		}
		if (csvRecord.get("doctor name") == null || csvRecord.get("doctor name").isEmpty()) {
			reason.append("Doctor name is missing;");
			isValid = false;
		}
		if (csvRecord.get("Patient name") == null || csvRecord.get("Patient name").isEmpty()) {
			reason.append("Patient name is missing;");
			isValid = false;
		}
		if (csvRecord.get("contact number") == null || csvRecord.get("contact number").isEmpty()) {
			reason.append("Contact name is missing;");
			isValid = false;
		}
		if (csvRecord.get("language") == null || csvRecord.get("language").isEmpty()) {
			reason.append("language is missing;");
			isValid = false;
		}
		if (csvRecord.get("appointment date") == null && csvRecord.get("appointment date").isEmpty()) {
			reason.append("Appointment date is missing;");
			isValid = false;
		} else {
			try {
				new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(csvRecord.get("appointment date"));
			} catch (Exception e) {
				reason.append("Appointment date format is incorrect;");
				isValid = false;
			}
		}
		return isValid;
	}

	@Scheduled(cron = "0 0/2 * * * *")
	@PostMapping("/httpurl")
	public void executeFailure() {

		try {
			int concurrent;
			long timeDifference;
			long timeDifference1;
			long retryDifference;
			boolean isMaxAdvTime = true;
			Date currentDate = new Date();
			Date weekStartDate = null;
			Date weekEndDate = null;
			DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat time = new SimpleDateFormat("hh:mm a");
			DateFormat WeekDaytimeFormat = new SimpleDateFormat("HH:mm:ss");
			DateFormat weekDayFormat = new SimpleDateFormat("EEEE");
			String weekDay = String.valueOf(weekDayFormat.format(currentDate));
			List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
			Map<String, List<ContactDetDto>> contactDetMap = campaignService.getContactDet();
			if (campaignDetList != null && !campaignDetList.isEmpty()) {
				for (CampaignDetRequest campaignDetRequest : campaignDetList) {
					if (contactDetMap != null && contactDetMap.containsKey(campaignDetRequest.getCampaignId())) {
						if (campaignDetRequest.getConcurrentCall() != null
								&& !campaignDetRequest.getConcurrentCall().isEmpty())
							concurrent = Integer.parseInt(campaignDetRequest.getConcurrentCall());
						else
							concurrent = 5;
						String campaignDateStr = campaignDetRequest.getStartDate() + " "
								+ campaignDetRequest.getStartTime();
						Date campaignStartdate = dateTimeformat.parse(campaignDateStr);
						Date campaignEndDate = dateTimeformat
								.parse(campaignDetRequest.getEndDate() + " " + campaignDetRequest.getEndTime());
						for (CampaignWeekDetRequest campaignWeekDetRequest : campaignDetRequest.getWeekDaysTime()) {
							if (weekDay.equalsIgnoreCase(campaignWeekDetRequest.getDay())) {
								weekStartDate = WeekDaytimeFormat.parse(campaignWeekDetRequest.getStartTime());
								weekEndDate = WeekDaytimeFormat.parse(campaignWeekDetRequest.getEndTime());
							}
						}
						List<ContactDetDto> contactDetList = contactDetMap.get(campaignDetRequest.getCampaignId());
						if (contactDetList != null && !contactDetList.isEmpty()) {
							int i = 1, j = 1;
							for (ContactDetDto contactDetDto : contactDetList) {
								isMaxAdvTime = true;
								Date appdate = dateTimeformat.parse(contactDetDto.getAppointmentDate());
								Date appdateCallBefore = dateformat.parse(contactDetDto.getAppointmentDate());
								if (appdate.after(campaignStartdate) && appdate.before(campaignEndDate)) {
									timeDifference = appdate.getTime() - currentDate.getTime();
									timeDifference1 = appdateCallBefore.getDate() - currentDate.getDate();
									// int dayDifference = (int) (TimeUnit.MILLISECONDS.toDays(timeDifference) %
									// 365);
									int dayDifference = (int) (TimeUnit.DAYS.toDays(timeDifference1));
									if (dayDifference == Integer.parseInt(campaignDetRequest.getCallBefore())) {
										if ("0".equalsIgnoreCase(campaignDetRequest.getCallBefore())) {
											long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
											String[] hourMin = campaignDetRequest.getMaxAdvNotice().split(":");
											long minutes = (Integer.parseInt(hourMin[0]) * 60)
													+ Integer.parseInt(hourMin[1]);
											if (minutesDifference < minutes) {
												isMaxAdvTime = false;
											}
										}
										if (isMaxAdvTime) {
											if (WeekDaytimeFormat.parse(WeekDaytimeFormat.format(currentDate))
													.after(weekStartDate)
													&& WeekDaytimeFormat.parse(WeekDaytimeFormat.format(currentDate))
															.before(weekEndDate)) {
												if (contactDetDto.getCallRetryCount() != null && (Integer
														.parseInt(contactDetDto.getCallRetryCount()) <= Integer
																.parseInt(campaignDetRequest.getRetryCount()))) {
													Date updateddate = dateTimeformat
															.parse(contactDetDto.getUpdatedDate());
													retryDifference = TimeUnit.MILLISECONDS
															.toMinutes(currentDate.getTime() - updateddate.getTime());
													if ("New".equalsIgnoreCase(contactDetDto.getCallStatus())
															|| retryDifference > Integer
																	.parseInt(campaignDetRequest.getRetryDelay())) {
														logger.info(
																"**** All Conditions are satisfied going to make call For****");
														Runnable obj1 = () -> {
															System.out.println("Request Success");
															CloseableHttpClient httpclient = HttpClients
																	.createDefault();
															HttpPost httppost = new HttpPost(callApi);
															List<NameValuePair> nvps = new ArrayList<NameValuePair>();
															nvps.add(new BasicNameValuePair("custphone",
																	"9" + contactDetDto.getContactNo()));
															nvps.add(new BasicNameValuePair("custname",
																	contactDetDto.getPatientName()));
															nvps.add(new BasicNameValuePair("docname",
																	contactDetDto.getDoctorName()));
															nvps.add(new BasicNameValuePair("time",
																	time.format(appdate)));
															nvps.add(new BasicNameValuePair("lang",
																	contactDetDto.getLanguage()));
															nvps.add(new BasicNameValuePair("contactId",
																	contactDetDto.getContactId()));
															nvps.add(new BasicNameValuePair("retryCount",
																	contactDetDto.getCallRetryCount()));
															nvps.add(new BasicNameValuePair("isToday",
																	"0".equalsIgnoreCase(
																			campaignDetRequest.getCallBefore()) ? "true"
																					: "false"));
															nvps.add(new BasicNameValuePair("aptDate",
																	dateformat.format(appdate)));
															try {
																httppost.setEntity(
																		new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
															} catch (UnsupportedEncodingException e) {
																e.printStackTrace();
															}
															HttpResponse httpresponse;
															try {
																httpresponse = httpclient.execute(httppost);
																logger.info(
																		"**** Call made successfully for below details****");
																logger.info("custphone===== "
																		+ contactDetDto.getContactNo());
																logger.info("custname===== "
																		+ contactDetDto.getPatientName());
																logger.info("docname===== "
																		+ contactDetDto.getDoctorName());
																logger.info(
																		"aptDate===== " + dateformat.format(appdate));
																logger.info("time===== " + time.format(appdate));
																System.out.println(
																		"Time for " + System.currentTimeMillis() + " : "
																				+ contactDetDto.getContactNo());
																Scanner sc = new Scanner(
																		httpresponse.getEntity().getContent());
																System.out.println(httpresponse.getEntity().getContent()
																		.toString());
																while (sc.hasNext()) {
																	logger.info("***Call response***");
																	logger.info(sc.nextLine());
																}
															} catch (IOException e) {
																e.printStackTrace();
															}

															try {
																httpclient.close();
															} catch (IOException e) {
																e.printStackTrace();
															}
														};
//														updateCallDet(i, contactDetDto.getContactId(),
//																contactDetDto.getCallRetryCount());
														Thread t = new Thread(obj1);
														t.start();
														if (j > concurrent) {
															Thread.sleep(15000 * concurrent);
															j = 0;
														}
														i++;
														j++;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
//		catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		catch (Exception e) {
			logger.info("Error Occured in call Making due to : " + e.getMessage());
			e.printStackTrace();
		}
		// return null;
	}

	@PostMapping("/createCampaign")
	public ResponseEntity<GenericResponse> createCampaign(@RequestBody CampaignDetRequest campaignDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.createCampaign(campaignDetRequest);
	}

	@GetMapping("/getCampaignDetail")
	public ResponseEntity<GenericResponse> getCampaignDetail()
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.getCampaignDetail();
	}

	@PostMapping("/updateCampaign")
	public ResponseEntity<GenericResponse> updateCampaign(@RequestBody CampaignDetRequest campaignDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.updateCampaign(campaignDetRequest);
	}

	@PostMapping("/updateCallDetail")
	public ResponseEntity<GenericResponse> updateCallDetail(@RequestBody UpdateCallDetRequest updateCallDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.updateCallDetail(updateCallDetRequest);
	}

	@PostMapping("/validateCampaignName")
	public ResponseEntity<GenericResponse> validateCampaignName(@RequestBody CampaignDetRequest campaignDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.validateCampaignName(campaignDetRequest);
	}

	@PostMapping("/summaryReport")
	public ResponseEntity<GenericResponseReport> summaryReport(@RequestBody ReportRequest reportRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.summaryReport(reportRequest);
	}

	@PostMapping("/detailReport")
	public ResponseEntity<GenericResponseReport> detailReport(@RequestBody ReportRequest reportRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.detailReport(reportRequest);
	}

	@PostMapping("/downloadSummaryReport")
	public ResponseEntity<InputStreamResource> downloadSummaryReport(@RequestBody ReportRequest reportRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.downloadSummaryReport(reportRequest);
	}

	@PostMapping("/downloadDetailReport")
	public ResponseEntity<InputStreamResource> downloadDetailReport(@RequestBody ReportRequest reportRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.downloadDetailReport(reportRequest);
	}

	@PostMapping("/getUploadhistory")
	public ResponseEntity<GenericResponse> getUploadHistory(@RequestBody ReportRequest reportRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.getUploadHistory(reportRequest);
	}

	@PostMapping("/deleteContactByHistory")
	public ResponseEntity<GenericResponse> deleteContactByHistory(
			@RequestBody UpdateCallDetRequest updateCallDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return campaignService.deleteContactByHistory(updateCallDetRequest);
	}

	@PostMapping("/uploadContactDetail")
	public ResponseEntity<GenericResponse> uploadContactDetail(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "campaignId", required = false) String campaignId,
			@RequestParam(name = "campaignName", required = false) String campaignName)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		String message = null;
		CSVPrinter csvPrinter = null;
		boolean isUploaded = true;
		List<ContactDetDto> failureList = null;
		if ("text/csv".equalsIgnoreCase(file.getContentType()) || file.getOriginalFilename().endsWith(".csv")) {
			try {
				failureList = new ArrayList<>();
				CampaignDetRequest campaignDetRequest = new CampaignDetRequest();
				campaignDetRequest.setCampaignId(campaignId);
				campaignDetRequest.setCampaignName(campaignName);
				List<CampaignDetRequest> campaignDetList = campaignService.getCampaignDetList();
				BigInteger historyId = getUploadHistoryid(campaignDetRequest, file.getOriginalFilename());
				List<ContactDetDto> contactDetList = csvToData(file.getInputStream(), historyId, isFTP, fileDirectory,
						new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()), failureDirectory, campaignDetList,
						failureList);
				ContactDetDto commonDetail = contactDetList.stream()
						.filter(x -> campaignId.equalsIgnoreCase(x.getCampaignId())).findAny().orElse(null);
				logger.info("****Converted CSV DATA to Object****");
				if (contactDetList.isEmpty()) {
					message = "Upload failed! Invalid data or Contact details already exist for same Appointment date and time ";
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
							.body(new GenericResponse(message, "Failed"));
				}
				if (commonDetail != null) {
					logger.info("****Inserting contact details to DB Table****");
					for (ContactDetDto contactDetDto : contactDetList) {
						isUploaded = campaignService.createContact(contactDetDto);
						if (!isUploaded) {
							contactDetDto.setFailureReason(
									"Contact details already exist for same Appointment Date and Time");
							failureList.add(contactDetDto);
						}
					}
					message = "Uploaded the file successfully: " + file.getOriginalFilename();
					if (!failureList.isEmpty()) {
						csvPrinter = failureCsvData(new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date()),
								failureList, failureDirectory);
						message = "One or more Contacts not uploaded due to some invalid data!";
					}
					return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(message, "Success"));
				} else if (commonDetail == null && !contactDetList.isEmpty()) {
					message = "Upload failed. Identified incorrect Campaign id, expected Id is " + campaignId;
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
							.body(new GenericResponse(message, "Failed"));
				}
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				logger.error("Error occured in uploadContactDetail:: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
						.body(new GenericResponse(message, "Failed"));
			} finally {
				if (csvPrinter != null)
					csvPrinter.close();
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(message, "Failed"));
	}

	private void updateCallDet(int i, String contactId, String C) {
		UpdateCallDetRequest UpdateCallDetRequest = new UpdateCallDetRequest();
		UpdateCallDetRequest.setContactId(contactId);
		UpdateCallDetRequest.setRetryCount(Integer.parseInt(contactId));
		if (i == 0) {
			UpdateCallDetRequest.setCallStatus("Failed");
		} else if (i % 3 == 0) {
			UpdateCallDetRequest.setCallStatus("ANSWERED");
			UpdateCallDetRequest.setCallerResponse("2");
			UpdateCallDetRequest.setCallDuration("20");
		} else if (i % 5 == 0) {
			UpdateCallDetRequest.setCallStatus("Failed");
		} else if (i % 7 == 0) {
			UpdateCallDetRequest.setCallStatus("ANSWERED");
			UpdateCallDetRequest.setCallerResponse("3");
			UpdateCallDetRequest.setCallDuration("20");
		} else if (i % 2 == 0) {
			UpdateCallDetRequest.setCallStatus("ANSWERED");
			UpdateCallDetRequest.setCallerResponse("1");
			UpdateCallDetRequest.setCallDuration("20");
		} else {
			UpdateCallDetRequest.setCallStatus("ANSWERED");
			UpdateCallDetRequest.setCallDuration("5");
		}
		campaignService.updateCallDetail(UpdateCallDetRequest);
	}

	private static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			logger.error("IOException: %s%n", e);
		}
	}
}
