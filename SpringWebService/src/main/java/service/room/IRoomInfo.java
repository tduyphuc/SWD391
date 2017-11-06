package service.room;

import java.util.Map;

public interface IRoomInfo {
	public String getAllRoomType();
	public Integer getAvailableRoom(Integer typeID);
	public Double getTotalPrice(Map<Integer, Integer> details);
	public String getAllService(String typeId);
	public String getAllRooms();
	public void updateAllRooms(String body);
}
