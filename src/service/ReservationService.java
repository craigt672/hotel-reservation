package service;

import model.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReservationService {
    private static final ReservationService reference = new ReservationService();
    private final HashMap<String, Room> rooms = new HashMap<String, Room>();
    private final HashMap<Integer, Reservation> reservations = new HashMap<Integer, Reservation>();

    public void addRoom(String roomNumber, Double price, RoomType roomType) {
        Room room = new Room(roomNumber, price, roomType);
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, Room room, Date checkInDate, Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        newReservation.getRoom().bookRoom();
        reservations.put(newReservation.hashCode(), newReservation);
        return newReservation;
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public Collection<Room> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<Room> foundRooms = new ArrayList<Room>();

        for (Room room : getAllRooms()) {
            if(room.isFree()){
                foundRooms.add(room);
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
        String pattern = "E MMM dd yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        if(reservations.values().isEmpty()) {
            System.out.println("There's no reservations");
            return;
        }

        for (Reservation reservation : reservations.values()) {
            System.out.println("|Reservations: ");
            System.out.println("|-------------------------");
            System.out.println("|Customer: " + reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName());
            System.out.println("|Room: " + reservation.getRoom().getRoomNumber() + " - " + reservation.getRoom().getRoomType());
            System.out.println("|Price: " + "$" + reservation.getRoom().getRoomPrice() + " " + "price per night");
            System.out.println("|Checkin Date: " + simpleDateFormat.format(reservation.getCheckInDate()));
            System.out.println("|Checkout Date: " + simpleDateFormat.format(reservation.getCheckOutDate()));
        }
    }

    public ReservationService getReference() {
        return reference;
    }
}
