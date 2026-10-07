package model.entity;

import error.OverloadException;
import model.enums.ROOM_TIER;
import java.util.Objects;

public class RoomTierEntity {
    private final ROOM_TIER roomTier; // Primary Key (Immutable Identifier)
    private Double basePrice;
    private Integer maxCap;

    public RoomTierEntity(ROOM_TIER roomTier, Double basePrice, Integer maxCap) {
        if (roomTier == null) {
            throw new IllegalArgumentException(" Room tier identifier cannot be null.");
        }
        this.roomTier = roomTier;
        setBasePrice(basePrice); // Reuses validation logic safely
        setMaxCap(maxCap);
    }

    // --- GETTERS & SETTERS ---

    public ROOM_TIER getRoomTier() {
        return roomTier;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        if (basePrice == null || basePrice < 0.0) {
            throw new IllegalArgumentException(" Base price cannot be null or negative.");
        }
        this.basePrice = basePrice;
    }

    public Integer getMaxCap() {
        return maxCap;
    }

    public void setMaxCap(Integer maxCap) {
        if (maxCap == null || maxCap <= 0) {
            throw new IllegalArgumentException(" Maximum capacity must be at least 1.");
        }
        this.maxCap = maxCap;
    }

    public void checkMax(Long cap) {
        if (cap > this.maxCap) {
            throw new OverloadException(" Max Room Capacity Reach");
        }
    }

    // --- EQUALS & HASHCODE (Primary Key Identity) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomTierEntity that = (RoomTierEntity) o;
        return roomTier == that.roomTier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomTier);
    }
}
