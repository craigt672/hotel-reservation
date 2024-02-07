package model;

public class Room implements IRoom {
    private Boolean isFree = true;
    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType() {
        return this.roomType;
    }

    @Override
    public Boolean isFree() {
        return isFree;
    }

    public void bookRoom() {
        this.isFree = false;
    }

    public void setRoomFree() {
        this.isFree = true;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
