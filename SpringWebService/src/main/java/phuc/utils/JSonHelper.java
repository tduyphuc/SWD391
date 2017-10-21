package phuc.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class JSonHelper implements IJSonHelper {

	private Gson gson = new Gson();
	
	@Override
	public String convertToJson(Object obj) {
		return gson.toJson(obj);
	}

	@Override
	public String toResponseMessage(int code, String message) {
		Map<String, String> map = new HashMap<>();
		map.put("code", code + "");
		map.put("message", message);
		return convertToJson(map);
	}

}
