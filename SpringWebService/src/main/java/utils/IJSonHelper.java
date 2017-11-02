package utils;

public interface IJSonHelper {
	public String convertToJson(Object obj);
	public String toResponseMessage(int code, String message);
}
