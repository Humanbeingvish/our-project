package com.cognicx.AppointmentRemainder.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AppointmentReminderUtil {

	public static String getCallerChoice(String choice) {
		final Map<String, String> callerChoice = new LinkedHashMap<>();
		callerChoice.put("0", "No Response");
		callerChoice.put("1", "Confirmed");
		callerChoice.put("2", "Cancelled");
		callerChoice.put("3", "Reschedule");
		return callerChoice.get(choice);
	}

}
