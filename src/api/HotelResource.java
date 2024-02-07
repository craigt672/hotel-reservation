package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    static final HotelResource reference = new HotelResource();

    public Customer getCustomer(String email) {
        return new CustomerService().getReference().getCustomer(email);
    }

    public void createCustomer(String email, String firstName, String lastName) {
        new CustomerService().getReference().addCustomer(firstName, lastName, email);
    }

    public Reservation bookARoom(String customerEmail, Room room, Date checkInDate, Date checkOutDate) {
        Customer customer = new CustomerService().getReference().getCustomer(customerEmail);
        return new ReservationService().getReference().reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = new CustomerService().getReference().getCustomer(customerEmail);
        return new ReservationService().getReference().getCustomersReservation(customer);
    }

    public Collection<Room> findARoom(Date checkIn, Date checkOut) {
        return new ReservationService().getReference().findRooms(checkIn, checkOut);
    }

    public HotelResource getReference() {
        return reference;
    }
}
