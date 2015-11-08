package br.com.fono.config;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class Util {
	private static final String timeZoneString = "GMT";
	private static final int fuso = -3;
	
	public static TimeZone getTimeZone() {
		return TimeZone.getTimeZone(timeZoneString);
	}
	
	public static Date getDataAtual() {
		Calendar cal = Calendar.getInstance(getTimeZone());
		cal.add(Calendar.HOUR_OF_DAY, fuso);
		return cal.getTime();
	}
	
	public String getTimeZoneString() {
		return timeZoneString;
	}
	
	public static Date trunc(Date data) {
		Calendar cal = Calendar.getInstance(getTimeZone());
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
