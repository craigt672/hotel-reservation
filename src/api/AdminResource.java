package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    public static AdminResource reference = new AdminResource();

    public Customer getCustomer(String email) {
        return new CustomerService().getReference().getCustomer(email);
    }

    public void addRoom(IRoom room) {
        new ReservationService().getReference().addRoom(room);
    }

    public Collection<IRoom> getAllRooms() {
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
