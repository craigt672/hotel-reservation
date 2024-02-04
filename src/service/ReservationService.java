package service;

import model.Customer;
import model.FreeRoom;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    private static final ReservationService reference = new ReservationService();
    private final HashMap<String, IRoom> rooms = new HashMap<String, IRoom>();
    private final HashMap<Integer, Reservation> reservations = new HashMap<Integer, Reservation>();

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        return reservations.put(newReservation.hashCode(), newReservation);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> foundRooms = new ArrayList<IRoom>();

        for (Reservation reservation : reservations.values()) {
            if((checkInDate.equals(reservation.getCheckInDate()) || checkInDate.after(reservation.getCheckInDate()) &&
                    (checkOutDate.equals(reservation.getCheckOutDate()) || checkOutDate.before(reservation.getCheckOutDate()))
                    )) {
                foundRooms.add(reservation.getRoom());
            }
        }
        return foundRooms;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> foundReservations = new ArrayList<Reservation>();

        for (Reservation reservation : reservations.values()) {
            if(reservation.getCustomer().equals(customer)) {
                foundReservations.add(reservation);
            }
        }
        return foundReservations;
    }

    public void printAllReservation() {
        for (Reservation reservation : reservations.values()) {
            System.out.println(reservation);
        }
    }

    public ReservationService getReference() {
        return reference;
    }
}
