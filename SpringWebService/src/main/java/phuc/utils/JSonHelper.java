package phuc.utils;

import com.google.gson.Gson;

public class JSonHelper implements IJSonHelper {

	private Gson gson = new Gson();
	
	@Override
	public String convertToJson(Object obj) {
		return gson.toJson(obj);
	}

}
