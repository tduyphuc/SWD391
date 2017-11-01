package phuc.controller.service.room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import phuc.controller.repo.IRoomRepo;
import phuc.entity.Room;
import phuc.entity.RoomType;
import phuc.utils.IJSonHelper;
import phuc.utils.ResponseCode;

@Service
public class RoomInfoService implements IRoomInfo {

	@Autowired
	private IRoomRepo repo;
	
	@Autowired
	private IJSonHelper jsonHelper;
	
	@Override
	public String getAllRoomType() {
		String json = getRoomTypeMap(repo.selectAllRoomType());
		return json;
	}

	private String getRoomTypeMap(Collection<RoomType> list){
		List<Map<String, String>> maps = new LinkedList<Map<String, String>>();
		for(RoomType rt : list){
			Map<String, String> map = new HashMap<>();
			map.put("typeId", String.valueOf(rt.getTypeId()));
			map.put("name", rt.getName());
			map.put("pricePerNight", String.valueOf(rt.getPricePerNight()));
			map.put("singleBedAmount", String.valueOf(rt.getSingleBedAmount()));
			map.put("doubleBedAmount", String.valueOf(rt.getDoubleBedAmount()));
			map.put("numberOfWindow", String.valueOf(rt.getNumberOfWindow()));
			map.put("capacity", String.valueOf(rt.getCapacity()));
			map.put("imageLink", rt.getImageLink());
			maps.add(map);
		}
		return jsonHelper.convertToJson(maps);
	}
	
	@Override
	public Integer getAvailableRoom(Integer typeID) {
		RoomType roomType = repo.getRoomType(typeID);
		int available = 0;
		if(roomType != null){
			Set<Room> rooms = roomType.getTblRooms();
			for(Room r : rooms){
				if(r.isIsAvailable()){
					available++;
				}
			}
		}
		return available;
	}

	@Override
	public Double getTotalPrice(Map<Integer, Integer> details) {
		Double total = 0.00;
		for(Integer key : details.keySet()){
			RoomType type = repo.getRoomType(key);
			total += type.getPricePerNight() * details.get(key);
		}
		return total;
	}

	@Override
	public String getAllService(String typeId) {
		Set<phuc.entity.Service> services = repo.getServices(Integer.valueOf(typeId));
		if(services == null){
			return jsonHelper.toResponseMessage(ResponseCode.ERROR_CODE_NOT_FOUND, "NO SERVICE FOUND");
		}
		String json = getServiceMap(services);
		return json;
	}
	
	private String getServiceMap(Set<phuc.entity.Service> services){
		List<Map<String, String>> maps = new LinkedList<Map<String, String>>();
		for(phuc.entity.Service service : services){
			Map<String, String> map = new HashMap<>();
			map.put("name", service.getServiceName());
			map.put("description", service.getDescription());
			maps.add(map);
		}
		return jsonHelper.convertToJson(maps);
	}

	@Override
	public String getAllRooms() {
		List<Map<String, String>> roomList = new ArrayList<>();
		Collection<Room> list = repo.selectAllRoom();
		for(Room r : list){
			Map<String, String> map = new HashMap<>();
			map.put("roomId", String.valueOf(r.getRoomId()));
			map.put("roomType", r.getTblRoomType().getName());
			map.put("available", String.valueOf(r.isIsAvailable()));
			
			roomList.add(map);
		}
		return jsonHelper.convertToJson(roomList);
	}

	@Override
	public void updateAllRooms(String body) {
		Gson gson = new Gson();
		Map<String, String> map = gson.fromJson(body, HashMap.class);		
		for(Entry<String, String> entry : map.entrySet()){
			Room room = repo.getRoom(Integer.valueOf(entry.getKey()));
			room.setIsAvailable(Boolean.valueOf(entry.getValue()));
			repo.updateRoom(room);
		}		
	}

}
