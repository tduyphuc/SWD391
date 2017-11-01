package phuc.controller.repo;

import java.util.Collection;
import java.util.Set;

import phuc.entity.Room;
import phuc.entity.RoomType;
import phuc.entity.Service;

public interface IRoomRepo {
	public Collection<RoomType> selectAllRoomType();
	public RoomType getRoomType(Integer typeID);
	public Room getRoom(Integer roomId);
	public void updateRoom(Room room);
	public Set<Service> getServices(Integer typeID);
	public Collection<Room> selectAllRoom();
}
