package phuc.controller.service.room;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phuc.controller.repo.IRoomRepo;
import phuc.entity.Room;
import phuc.entity.RoomType;
import phuc.utils.IJSonHelper;

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

}
