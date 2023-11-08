package com.cognicx.AppointmentRemainder.constant;

public class CampaignQueryConstant {

	public static final String INSERT_CAMPAIGN_DET = "INSERT INTO appointment_remainder.campaign_det(campaign_id,name,description,status,max_adv_time,retry_delay,retry_count,concurrent_call,start_date,start_time,end_date,end_time,ftp_location,ftp_credentials,file_name,call_before) "
			+ "VALUES (:campaignId,:name,:desc,:status,:maxAdvTime,:retryDelay,:retryCount,:concurrentCall,:startDate,:startTime,:endDate,:endTime,:ftpLocation,:ftpCredentials,:fileName,:callBefore)";

	public static final String INSERT_CAMPAIGN_WEEK_DET = "insert into appointment_remainder.campaign_week_det (campaign_id,day,status,start_time,end_time) values(:campaignId,:day,:status,:startTime,:endTime)";

	public static final String GET_CAMPAIGN_ID = "select max(SUBSTRING(campaign_id, 3, 100)) from appointment_remainder.campaign_det";

	public static final String GET_CAMPAIGN_DET = "SELECT campaign_id,name,description,status,max_adv_time,retry_delay,retry_count,concurrent_call,start_date,start_time,campaign_det.end_date,end_time,ftp_location,ftp_credentials,file_name,call_before FROM appointment_remainder.campaign_det";

	public static final String GET_CAMPAIGN_WEEK_DET = "SELECT campaign_week_id,campaign_id,day,status,start_time,end_time from appointment_remainder.campaign_week_det";

	public static final String UPDATE_CAMPAIGN_DET = "UPDATE appointment_remainder.campaign_det SET name = :name,status = :status,max_adv_time = :maxAdvTime,retry_delay = :retryDelay,retry_count = :retryCount,concurrent_call = :concurrentCall,start_date = :startDate,start_time = :startTime,end_date = :endDate,end_time = :endTime,ftp_location = :ftpLocation,ftp_credentials = :ftpCredentials,rec_updt_dt = getdate(),call_before=:callBefore,file_name=:fileName WHERE campaign_id = :campaignId";

	public static final String UPDATE_CAMPAIGN_WEEK_DET = "UPDATE appointment_remainder.campaign_week_det SET day=:day, status=:status, start_time=:startTime, end_time=:endTime where campaign_week_id=:campaignWeekId";

	public static final String UPDATE_CALL_DET = "UPDATE appointment_remainder.contact_det SET caller_response=:callerResponse, call_status=:callStatus, call_duration=:callDuration, call_retry_count=:retryCount,rec_upt_date=getdate() where contact_id=:contactId";

	public static final String INSERT_CONTACT_DET = "insert into appointment_remainder.contact_det (campaign_id,campaign_name,doctor_name,patient_name,contact_number,appointment_date,language,call_status,upload_history_id) values(:campaignId,:campaignName,:doctorName,:patientName,:contactNo,:appDate,:language,:callStatus,:historyId)";

	public static final String GET_CONTACT_DET = "select campaign_id,campaign_name,doctor_name,patient_name,contact_number,appointment_date,language,contact_id,call_retry_count,rec_upt_date,call_status from appointment_remainder.contact_det where call_status !='ANSWERED' order by appointment_date asc";

	public static final String VALIDATE_CAMPAIGN_NAME = "select count(1) from [appointment_remainder].[campaign_det] where name=:name";

	public static final String GET_SUMMARY_REPORT_DET = "select campaign_name,format(appointment_date,'dd-MMM-yyyy'),count(contact_id) as totalContact,SUM(CASE WHEN call_status!='New' THEN 1 ELSE 0 END) as contactCalled,"
			+ "  SUM(CASE WHEN call_status in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as connectedCall,"
			+ "  SUM(CASE WHEN call_status='NOT ANSWERED'  THEN 1 ELSE 0 END) as answered,"
			+ "  SUM(CASE WHEN call_status='BUSY'  THEN 1 ELSE 0 END) as busy,"
			+ "  SUM(CASE WHEN caller_response='1'  THEN 1 ELSE 0 END) as confirmed,"
			+ "  SUM(CASE WHEN caller_response='2'  THEN 1 ELSE 0 END) as cancelled,"
			+ "  SUM(CASE WHEN caller_response='3'  THEN 1 ELSE 0 END) as rescheduled,"
			+ "  SUM(CASE WHEN caller_response='0'  THEN 1 ELSE 0 END) as noresponse,"
			+ "  SUM(CASE WHEN call_status not in ('ANSWERED','BUSY','NOANSWER','NOT ANSWERED')  THEN 1 ELSE 0 END) as others"
			+ "  from appointment_remainder.contact_det"
			+ "  where cast(appointment_date as date) between :startDate and :endDate and campaign_id =:name"
			+ "  group by campaign_name,format(appointment_date,'dd-MMM-yyyy')";

	public static final String GET_CONTACT_DET_REPORT = "SELECT contact_id,campaign_id,campaign_name,doctor_name,patient_name,contact_number,format(appointment_date,'dd-MMM-yyyy hh:mm:ss tt'),caller_response ,call_status ,call_duration ,call_retry_count FROM appointment_remainder .contact_det where";

	public static final String GET_CALL_RETRY_DET = "select contact_id,call_status,format(rec_add_dt,'dd-MMM-yyyy hh:mm:ss tt') from appointment_remainder.call_retry_det where contact_id in (:contactIdList) order by contact_id asc";

	public static final String INSERT_CALL_RETRY_DET = "insert into appointment_remainder.call_retry_det (contact_id,call_status,retry_count) values (:contactId,:callStatus,:retryCount)";

	public static final String GET_UPLOAD_HISTORY_DETIALS = "select upload_history_id,campaign_id,campaign_name,uploaded_on,file_name from appointment_remainder.upload_history_det where status=1 and cast(uploaded_on as date) between :startDate and :endDate";

	public static final String DELETE_CONTACT_BY_HISTORY = "delete from appointment_remainder.contact_det where upload_history_id=:historyId";
}
