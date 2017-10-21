package phuc.controller.repo;

import java.util.Collection;
import java.util.Set;

import phuc.entity.RoomType;
import phuc.entity.Service;

public interface IRoomRepo {
	public Collection<RoomType> selectAllRoomType();
	public RoomType getRoomType(Integer typeID);
	public Set<Service> getServices(Integer typeID);
}
