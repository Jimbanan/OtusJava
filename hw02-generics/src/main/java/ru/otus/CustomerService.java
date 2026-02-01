package ru.otus;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customers = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customers.firstEntry();
        if (entry == null) {
            return null;
        }

        Customer copy = new Customer(
                entry.getKey().getId(),
                entry.getKey().getName(),
                entry.getKey().getScores()
        );

        return Map.entry(copy, entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customers.higherEntry(customer);
        if (entry == null) return null;

        Customer copy = new Customer(
                entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());

        return Map.entry(copy, entry.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
