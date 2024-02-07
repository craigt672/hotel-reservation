import api.AdminResource;
import model.*;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;

public class AdminMenu {
    private static final AdminResource adminResource = new AdminResource().getReference();
    public static void run() {
        boolean shouldExit = false;
        Scanner scanner = new Scanner(System.in);
        while (!shouldExit) {
            AdminMenu.displayMenuOptions();
            String userInput = scanner.nextLine();
            try {
                switch (userInput) {
                    case "1":
                        seeAllCustomers();
                        break;
                    case "2":
                        seeAllRooms();
                        break;
                    case "3":
                        seeAllReservations();
                        break;
                    case "4":
                        addRoom();
                        break;
                    case "5":
                        shouldExit = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid option");
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        MainMenu.run();
    }

    public static void seeAllCustomers() throws Exception {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        if(allCustomers.isEmpty()) {
            throw new Exception("There are no registered Customers");
        }

        System.out.println();
        System.out.println("|All Registered Customers");
        for (Customer customer : allCustomers) {
            displayCustomerDetails(customer);
        }
        System.out.println();
    }

    public static void seeAllRooms() throws Exception {
        Collection<Room> allRooms = adminResource.getAllRooms();
        if(allRooms.isEmpty()) {
            throw new Exception("There are no rooms");
        }

        System.out.println();
        System.out.println("|All Rooms");
        for (IRoom room : allRooms) {
            displayRoomDetails(room);
        }
        System.out.println();
    }

    public static void seeAllReservations() throws Exception {
        adminResource.displayAllReservations();
    }

    public static void addRoom() throws Exception {
        boolean shouldExit = false;

        while(!shouldExit) {
            String roomNumber = getUserInputFromPrompt("Enter room number", AdminMenu::validateRoomNumber);
            IRoom room = adminResource.getReference().getARoom(roomNumber);

            String price = getUserInputFromPrompt("Enter price per night", AdminMenu::validatePrice);
            String roomType = getUserInputFromPrompt("Enter room type: 1 for single bed, 2 for double bed", AdminMenu::validateRoomType);

            if (room != null) {
                throw new Exception("That Room already exists");
            }

            RoomType singleOrDouble = null;
            switch (roomType) {
                case "1":
                    singleOrDouble = RoomType.SINGLE;
                    break;
                case "2":
                    singleOrDouble = RoomType.DOUBLE;
                    break;
                default:
                    throw new Exception("Not a valid RoomType Option");
            }

            adminResource.addRoom(roomNumber, Double.parseDouble(price), singleOrDouble);

            String shouldAddRoom = getUserInputFromPrompt("Would you like to add another room? y/n", AdminMenu::validateYesOrNo);

            if (Objects.equals(shouldAddRoom.toLowerCase(), "n")) {
                shouldExit = true;
            }
        }
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
        System.out.println("Admin Menu");
        System.out.println("----------------------------------------------------");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Back to Main Menu");
        System.out.println("----------------------------------------------------");
        System.out.println("Please select a number for the menu option");
    }

    public static void displayCustomerDetails(Customer customer) {
        System.out.println("|----------------------------");
        System.out.println("|Name: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("|Email: " + customer.getEmail());
    }

    public static void displayRoomDetails(IRoom room) {
        System.out.println("|-------------------------");
        System.out.println("|Number: " + room.getRoomNumber());
        System.out.println("|Type: " + room.getRoomType());
        System.out.println("|Price: " + "$" + room.getRoomPrice() + " " + "price per night");
    }
    public static void displayReservationDetails(Reservation reservation) {
        String pattern = "E MMM dd yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        System.out.println("|-------------------------");
        System.out.println("|Customer: " + reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName());
        System.out.println("|Room: " + reservation.getRoom().getRoomNumber() + " - " + reservation.getRoom().getRoomType());
        System.out.println("|Price: " + "$" + reservation.getRoom().getRoomPrice() + " " + "price per night");
        System.out.println("|Checkin Date: " + simpleDateFormat.format(reservation.getCheckInDate()));
        System.out.println("|Checkout Date: " + simpleDateFormat.format(reservation.getCheckOutDate()));
    }

    public static String validatePrice(String price) {
        String priceRegex = "^(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$";
        Pattern pattern = Pattern.compile(priceRegex);

        if(!pattern.matcher(price).matches()) {
            throw new IllegalArgumentException("Not a valid price");
        }
        return price;
    }

    public static String validateRoomType(String roomTypeOptions) {
        String roomTypeRegex = "[12]";
        Pattern pattern = Pattern.compile(roomTypeRegex);

        if(!pattern.matcher(roomTypeOptions).matches()) {
            throw new IllegalArgumentException("Not a valid room type option");
        }

        if (roomTypeOptions.equals("1")) {
            return "1";
        }
        return "2";
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
        String optionRegex = "[yn]|[YN]";
        Pattern pattern = Pattern.compile(optionRegex);

        if(!pattern.matcher(yesOrNo).matches()){
            throw new IllegalArgumentException("Please enter Y (Yes) or N (No)");
        }
        return yesOrNo;
    }
}
