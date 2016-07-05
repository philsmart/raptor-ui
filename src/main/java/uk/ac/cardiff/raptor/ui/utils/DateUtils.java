package uk.ac.cardiff.raptor.ui.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtils {

	public static Date getStartOfYear() {
		final LocalDateTime currentTime = LocalDateTime.now();
		final LocalDateTime startOfYear = currentTime.withDayOfMonth(1).withMonth(1).withHour(0).withMinute(0);
		final ZonedDateTime zdt = startOfYear.atZone(ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}

	public static Date getStartOfToday() {
		final LocalDateTime currentTime = LocalDateTime.now();
		final LocalDateTime startOfYear = currentTime.withHour(0).withMinute(0);
		final ZonedDateTime zdt = startOfYear.atZone(ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}

}
