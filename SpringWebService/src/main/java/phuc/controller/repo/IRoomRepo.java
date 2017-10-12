package phuc.controller.repo;

import java.util.Collection;

import phuc.entity.RoomType;

public interface IRoomRepo {
	public Collection<RoomType> selectAllRoomType();
	public RoomType getRoomType(Integer typeID);
}
