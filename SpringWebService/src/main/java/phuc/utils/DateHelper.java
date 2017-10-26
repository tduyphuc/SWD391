package phuc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper implements IDateHelper {
	private static final String DATE_FORMAT_STRING = "dd-MM-yyyy";
	
	private SimpleDateFormat sdf;
	
	public DateHelper() {
		sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
	}
	
	@Override
	public Date convertToDate(String date) {
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// Log error
		}
		return null;
	}

	@Override
	public Date convertToDate(long date) {
		return new Date(date);
	}

	@Override
	public Date getCurrentDay() {
		String s =  sdf.format(Calendar.getInstance().getTime());
		return convertToDate(s);
	}

	@Override
	public Date getCurrentDayToSecond() {
		return Calendar.getInstance().getTime();
	}

}
