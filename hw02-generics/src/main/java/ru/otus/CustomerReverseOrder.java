package ru.otus;

import java.util.ArrayDeque;

public class CustomerReverseOrder {

    private final ArrayDeque<Customer> customerQueue = new ArrayDeque<>();

    public void add(Customer customer) {
        customerQueue.add(customer);
    }

    public Customer take() {
        return customerQueue.pollLast();
    }
}
