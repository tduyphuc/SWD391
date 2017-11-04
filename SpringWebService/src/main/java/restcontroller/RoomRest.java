package restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.service.room.IRoomInfo;
import utils.IJSonHelper;
import utils.ResponseCode;

@RestController
@RequestMapping(value="room")
public class RoomRest {
	
	@Autowired
	private IRoomInfo roomInfo;
	
	@Autowired
	private IJSonHelper json;
	
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
		String mes = json.toResponseMessage(ResponseCode.OK_CODE, result + "");
 		return mes;		
	}
	
	@RequestMapping(value = "/getServices", 
			method = RequestMethod.GET)
	public String getRoomService(@RequestParam(value="typeId") String typeId){		
		String result = roomInfo.getAllService(typeId);
		return result;		
	}
	
	@RequestMapping(value = "/getAllRooms", 
			method = RequestMethod.GET)
	public String getAllRooms(){	
		return roomInfo.getAllRooms();		
	}
	
	@RequestMapping(value = "/updateRooms", 
			method = RequestMethod.POST)
	public String updateRooms(@RequestBody String roomMap){	
		roomInfo.updateAllRooms(roomMap);
		return "OK";
	}
	
}
