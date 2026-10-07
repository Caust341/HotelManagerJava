package service;

import error.ItemNotFoundException;
import model.entity.RoomTierEntity;
import model.enums.ROOM_TIER;
import repository.RoomTierRepository;

import java.util.List;

public class RoomTierService {
    private  final RoomTierRepository repo = new RoomTierRepository();
    private final List<RoomTierEntity> roomTierList;
    private static RoomTierService instance;

    private RoomTierService() {
        roomTierList = repo.read();
    }

    public static RoomTierService getInstance() {
        if (instance == null) {
            instance = new RoomTierService();
        }
        return instance;
    }
     
    public List<RoomTierEntity> showAll() {
        return roomTierList;
    }

     
    public RoomTierEntity findByTier(ROOM_TIER tier) {
        for (RoomTierEntity ct : roomTierList) {
            if (ct.getRoomTier() == tier) {
                return ct;
            }
        }
        throw new ItemNotFoundException(" Room " + tier + " does not exist");
    }

    public void updatePrice(RoomTierEntity oldT, Double price) {
        oldT.setBasePrice(price);
        repo.write(roomTierList);
    }


}
