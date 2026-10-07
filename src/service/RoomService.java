package service;

import error.ItemNotFoundException;
import model.entity.RoomEntity;
import model.enums.ROOM_STATUS;
import model.enums.ROOM_TIER;
import repository.RoomRepository;
import util.comparator.RoomComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoomService {
    private final RoomRepository repo = new RoomRepository();
    private final List<RoomEntity> roomList;
    private static RoomService instance;

    private RoomService() {
        roomList = repo.read();
    }

    public static RoomService getInstance() {
        if (instance == null) {
            instance = new RoomService();
        }
        return instance;
    }

    public List<RoomEntity> showAll() {
        return roomList;
    }


    public RoomEntity findByNumber(Integer input) {
        for (RoomEntity r : roomList) {
            if (r.getRoomNumber() == input.intValue()) {
                return r;
            }
        }
        throw new ItemNotFoundException(" Room " +input +" does not exist");
    }




    public void updateTier(RoomEntity oldT, ROOM_TIER newTier) {
        oldT.setTier(newTier);
        repo.write(roomList);
    }

    public void updateStatus(RoomEntity oldT, ROOM_STATUS status) {
        oldT.setStatus(status);
        repo.write(roomList);
    }

    public List<RoomEntity> sort(RoomComparator comparator,boolean isReversed) {
        List<RoomEntity> sortedList = new ArrayList<>(showAll());
        Comparator<RoomEntity> c = comparator.getComparator();

        if (isReversed) {
            c = c.reversed();
        }
        sortedList.sort(c);
        return sortedList;
    }


    public List<RoomEntity> findByTier(ROOM_TIER targetTier) {
        List<RoomEntity> searched = new ArrayList<>();
        for (RoomEntity room : roomList) {
            if(room.getTier() == targetTier) {
                searched.add(room);
            }
        }
        return searched;
    }
}
