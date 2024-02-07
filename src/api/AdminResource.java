package api;

import model.*;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;

public class AdminResource {
    public static AdminResource reference = new AdminResource();

    public void addRoom(String roomNumber, Double price, RoomType roomType) {
        new ReservationService().getReference().addRoom(roomNumber, price, roomType);
    }

    public IRoom getARoom(String roomNumber) {
        return new ReservationService().getReference().getARoom(roomNumber);
    }

    public Collection<Room> getAllRooms() {
        return new ReservationService().getReference().getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return new CustomerService().getReference().getAllCustomers();
    }

    public void displayAllReservations() {
        new ReservationService().getReference().printAllReservation();
    }

    public AdminResource getReference() {
        return reference;
    }
}
