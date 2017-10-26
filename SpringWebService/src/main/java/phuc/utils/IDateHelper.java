package phuc.utils;

import java.util.Date;

public interface IDateHelper {
	public Date convertToDate(String date);
	public Date convertToDate(long date);
	public Date getCurrentDay();
	public Date getCurrentDayToSecond();
}
