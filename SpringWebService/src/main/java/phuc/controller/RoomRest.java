package phuc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import phuc.controller.service.room.IRoomInfo;

@RestController
@RequestMapping(value="room")
public class RoomRest {
	
	@Autowired
	private IRoomInfo roomInfo;
	
	@RequestMapping(value = "/getAll", 
			method = RequestMethod.GET)
	public String getAll(){		
		String result = roomInfo.getAllRoomType();
		return result;		
	}
	
	@RequestMapping(value = "/getAvailable", 
			method = RequestMethod.GET)
	public String getAvailable(@RequestParam(value="typeId") String typeId){		
		Integer result = roomInfo.getAvailableRoom(Integer.valueOf(typeId));
		String mes = "Type: " + typeId + ", available: " + result;
 		return mes;		
	}
}
