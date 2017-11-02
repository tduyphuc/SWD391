package repo;

import java.util.Collection;
import java.util.Set;

import entity.Room;
import entity.RoomType;
import entity.Service;

public interface IRoomRepo {
	public Collection<RoomType> selectAllRoomType();
	public RoomType getRoomType(Integer typeID);
	public Room getRoom(Integer roomId);
	public void updateRoom(Room room);
	public Set<Service> getServices(Integer typeID);
	public Collection<Room> selectAllRoom();
}
