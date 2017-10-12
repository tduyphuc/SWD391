package phuc.controller.repo;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import phuc.entity.RoomType;

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

}
