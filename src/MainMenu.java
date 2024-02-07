import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class MainMenu {
    private static final HotelResource hotelResource = new HotelResource().getReference();
    public static void run() {
        boolean shouldExit = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Hotel Reservation Application");
        while (!shouldExit) {
            MainMenu.displayMenuOptions();
            String userInput = scanner.nextLine();
            try {
                switch (userInput) {
                    case "1":
                        reserveRoom();
                        break;
                    case "2":
                        seeReservations();
                        break;
                    case "3":
                        createAccount();
                        break;
                    case "4":
                        AdminMenu.run();
                        break;
                    case "5":
                        System.out.println("Good Bye!");
                        shouldExit = true;
                        scanner.close();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid option");
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        scanner.close();
    }

    public static void reserveRoom() throws Exception {
        String checkInDateString = getUserInputFromPrompt("Enter CheckIn date mm/dd/yyyy ex: 02/01/2020", MainMenu::validateDate);
        String checkOutDateString = getUserInputFromPrompt("Enter CheckOut date mm/dd/yyyy ex: 02/01/2020", MainMenu::validateDate);
        Date checkInDate = new Date(checkInDateString);
        Date checkOutDate = new Date(checkOutDateString);

        Collection<Room> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
        if(availableRooms.isEmpty()) {
            throw new Exception("There's no available rooms");
        }
        displayAvailableRooms(availableRooms);

        String shouldBookRoomAnswer = getUserInputFromPrompt("Would you like to book a room? y/n", MainMenu::validateYesOrNo);

        if(Objects.equals(shouldBookRoomAnswer, "n")) {
            throw new Exception("Main Menu:");
        }

        String hasAccountAnswer = getUserInputFromPrompt("Do you have an account with us? y/n", MainMenu::validateYesOrNo);

        if(Objects.equals(hasAccountAnswer, "n")) {
            throw new Exception("Please create an account first.");
        }

        String customerEmail = getUserInputFromPrompt("Enter Email format: name@domain.com", MainMenu::validateEmail);
        Customer customer = hotelResource.getCustomer(customerEmail);
        if(customer == null) {
            throw new Exception("That account doesn't exists");
        }

        boolean hasRoomNumber = false;
        Room selectedRoom = null;
        while (!hasRoomNumber) {
            String roomNumber = getUserInputFromPrompt("What room number would you like to reserve?", MainMenu::validateRoomNumber);

            for(Room room: availableRooms) {
                if(Objects.equals(room.getRoomNumber(), roomNumber)) {
                    hasRoomNumber = true;
                    selectedRoom = room;
                }
            }
            if(!hasRoomNumber) {
                System.out.println("That room is not available");
            }
        }

        Reservation bookedReservation = hotelResource.bookARoom(customerEmail, selectedRoom, checkInDate, checkOutDate);
        displayReservationDetails(bookedReservation);
    }

    public static void seeReservations() throws Exception {
        String customerEmail = getUserInputFromPrompt("Enter Email format: name@domain.com", MainMenu::validateEmail);
        Customer customer = hotelResource.getCustomer(customerEmail);
        if(customer == null) {
            throw new Exception("That account doesn't exists");
        }

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(customerEmail);

        if(reservations.isEmpty()) {
            throw new Exception("There's no reservation for this account");
        }

        for(Reservation reservation: reservations) {
            displayReservationDetails(reservation);
        }

    }

    public static void createAccount() throws Exception {
        String customerEmail = getUserInputFromPrompt("Enter Email format: name@domain.com", MainMenu::validateEmail);
        Customer customer = hotelResource.getCustomer(customerEmail);
        if(customer != null) {
            throw new Exception("That account already exists");
        }

        String customerFirstName = getUserInputFromPrompt("First Name", MainMenu::validateName);
        String customerLastName = getUserInputFromPrompt("Last Name", MainMenu::validateName);

        hotelResource.createCustomer(customerEmail, customerFirstName, customerLastName);
        Customer createdCustomer = hotelResource.getCustomer(customerEmail);

        displayCustomerDetails(createdCustomer);
    }


    public static String getUserInputFromPrompt(String promptMessage, Function<String, String> validator) {
        Scanner scanner = new Scanner(System.in);
        String requestedInput = null;
        boolean shouldExit = false;

        while(!shouldExit) {
            System.out.println(promptMessage);
            String userInput = scanner.nextLine();
            try {
                if(userInput != null) {
                    requestedInput = validator.apply(userInput);

                    if (requestedInput != null) {
                        shouldExit = true;
                    }
                }
            } catch(Exception ex){
                System.out.println(ex.getLocalizedMessage());
            }
        }
        return requestedInput;
    }

    public static void displayMenuOptions() {
        System.out.println();
        System.out.println("Main Menu");
        System.out.println("----------------------------------------------------");
        System.out.println("1. Find and Reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("----------------------------------------------------");
        System.out.println("Please select a number for the menu option");
    }

    public static void displayAvailableRooms(Collection<Room> availableRooms) {
        System.out.println();
        System.out.println("|Available Rooms:");
        for(IRoom room: availableRooms) {
            System.out.println("|Room number: " + room.getRoomNumber());
        }
    }

    public static void displayCustomerDetails(Customer customer) {
        System.out.println();
        System.out.println("|Customer:");
        System.out.println("|-------------------------");
        System.out.println("|Name: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("|Email: " + customer.getEmail());
    }

    public static void displayReservationDetails(Reservation reservation) {
        String pattern = "E MMM dd yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        System.out.println();
        System.out.println("|Reservation:");
        System.out.println("---------------------------------");
        System.out.println("|Customer: " + reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName());
        System.out.println("|Room: " + reservation.getRoom().getRoomNumber() + " - " + reservation.getRoom().getRoomType());
        System.out.println("|Price: " + "$" + reservation.getRoom().getRoomPrice() + " " + "price per night");
        System.out.println("|Checkin Date: " + simpleDateFormat.format(reservation.getCheckInDate()));
        System.out.println("|Checkout Date: " + simpleDateFormat.format(reservation.getCheckOutDate()));
    }

    public static String validateEmail(String email) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);

        if(!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("Not a valid email");
        }
        return email;
    }

    public static String validateName(String name) {
        String nameRegex = "^[a-zA-Z\\s]{3,30}$";
        Pattern pattern = Pattern.compile(nameRegex);

        if(!pattern.matcher(name).matches()){
            throw new IllegalArgumentException("Not a valid name");
        }
        return (name.substring(0, 1).toUpperCase() + name.substring(1)).replaceAll("\\s", "");
    }

    public static String validateDate(String dateString) {
        String dateRegex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(dateRegex);

        if(!pattern.matcher(dateString).matches()){
            throw new IllegalArgumentException("Not a valid date");
        }
        return dateString;
    }

    public static String validateRoomNumber(String roomNumberString) {
        String roomNumberRegex = "\\d{3}$";
        Pattern pattern = Pattern.compile(roomNumberRegex);

        if(!pattern.matcher(roomNumberString).matches()){
            throw new IllegalArgumentException("Not a valid Room Number");
        }
        return roomNumberString;
    }

    public static String validateYesOrNo(String yesOrNo) {
        String optionRegex = "[yn]";
        Pattern pattern = Pattern.compile(optionRegex);

        if(!pattern.matcher(yesOrNo).matches()){
            throw new IllegalArgumentException("Not a valid option");
        }
        return yesOrNo;
    }
}
