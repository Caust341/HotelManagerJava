package model.entity;

import model.enums.ROOM_STATUS;
import model.enums.ROOM_TIER;

import java.util.Objects;

public class RoomEntity {
    private Integer roomNumber;
    private Short floor;
    private ROOM_TIER tier;
    private ROOM_STATUS status;

    public RoomEntity() {
    }

    public RoomEntity(Integer roomNumber, Short floor, ROOM_TIER tier, ROOM_STATUS status) {
        if (roomNumber == null || roomNumber <= 0) {
            throw new IllegalArgumentException(" Room number must be a positive integer.");
        }
        if (floor == null) {
            throw new IllegalArgumentException(" Floor cannot be null.");
        }
        if (tier == null) {
            throw new IllegalArgumentException(" Room tier cannot be null.");
        }
        if (status == null) {
            throw new IllegalArgumentException(" Room status cannot be null.");
        }

        this.roomNumber = roomNumber;
        this.floor = floor;
        this.tier = tier;
        this.status = status;
    }

    public Short getFloor() {
        return floor;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }


    public ROOM_TIER getTier() {
        return tier;
    }


    public void setTier(ROOM_TIER tier) {
        if (tier == null) {
            throw new IllegalArgumentException(" Room tier cannot be null.");
        }
        this.tier = tier;
    }

    public ROOM_STATUS getStatus() {
        return status;
    }

    public void setStatus(ROOM_STATUS status) {
        if (status == null) {
            throw new IllegalArgumentException(" Room status cannot be null.");
        }
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity that = (RoomEntity) o;
        return Objects.equals(roomNumber, that.roomNumber) && Objects.equals(floor, that.floor) && tier == that.tier && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, floor, tier, status);
    }


}