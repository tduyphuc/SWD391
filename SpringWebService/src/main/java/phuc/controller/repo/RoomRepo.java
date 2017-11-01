package phuc.controller.repo;

import java.util.Collection;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import phuc.entity.Room;
import phuc.entity.RoomType;
import phuc.entity.Service;

@Repository
public class RoomRepo implements IRoomRepo {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Collection<RoomType> selectAllRoomType() {
		Collection<RoomType> list = entityManager.createQuery("SELECT r FROM RoomType r", RoomType.class).getResultList();
		return list;
	}

	@Override
	public RoomType getRoomType(Integer typeID) {
		RoomType roomType = entityManager.find(RoomType.class, typeID);
		return roomType;
	}

	@Override
	public Set<Service> getServices(Integer typeID) {
		RoomType roomType = entityManager.find(RoomType.class, typeID);
		if(roomType != null){
			Set<Service> services = roomType.getTblServices();
			return services;
		}
		return null;
	}

	@Override
	public Collection<Room> selectAllRoom() {
		Collection<Room> list = entityManager.createQuery("SELECT r FROM Room r", Room.class).getResultList();
		return list;
	}

	@Override
	public Room getRoom(Integer roomId) {
		Room room = entityManager.find(Room.class, roomId);
		return room;
	}

	@Override
	@Transactional
	public void updateRoom(Room room) {
		entityManager.merge(room);	
	}

}
