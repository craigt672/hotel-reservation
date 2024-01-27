package service;

import model.Customer;

import java.util.*;

public class CustomerService {
    private static final CustomerService reference = new CustomerService();
    private final HashMap<String, Customer> customers = new HasMap<String, Customer>();
    public void addCustomer(String firstName, String lastName, String email) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }

    public CustomerService getReference() {
        return reference;
    }
}
