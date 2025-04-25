package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


public class Guest {
    private int guestId;                         // Unique ID for the guest
    private String name;                         // Guest's full name
    private ArrayList<Reservation> history;      // Reservation history for the guest

    /**
     * Constructor to initialize a Guest.
     *
     * @param guestId Unique ID for the guest.
     * @param name    Full name of the guest.
     */
    
    
    
    public Guest(String name) {
        this.guestId = generateRandomGuestId();
        this.name = name;
    }
    
    
    
    public Guest(int guestId, String name) {
        this.guestId = guestId;
        this.name = name;
        this.history = new ArrayList<>();
    }

    // Getters and Setters

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Reservation> getHistory() {
        return history;
    }

    /**
     * Add a completed reservation to the guest's history.
     *
     * @param reservation The completed reservation to add.
     */
    public void addReservationToHistory(Reservation reservation) {
        if (reservation.getStatus().equals("Completed")) {
            history.add(reservation);
            System.out.println("Reservation added to guest history: " + reservation);
        } else {
            System.out.println("Error: Only completed reservations can be added to history.");
        }
    }

    /**
     * Display the guest's reservation history.
     */
  public void displayReservationHistory() {
    if (history.isEmpty()) {
        System.out.println("No reservation history available for guest: " + name);
    } else {
        System.out.println("\n========== Reservation History for " + name + " ==========");
        int totalCost = 0;
        for (Reservation reservation : history) {
            System.out.println(reservation);
            totalCost += reservation.calculateTotalCost();
        }
        System.out.println("Total Cost of All Reservations: $" + totalCost);
    }
}
  
  
  
  
     private static int generateRandomGuestId() {
        Random random = new Random();
        return random.nextInt(1_000_000); // ID up to 1,000,000
    }


    /**
     * Convert guest details and history to a readable format.
     *
     * @return A string representation of the Guest object.
     */
    @Override
    public String toString() {
        return "Guest ID: " + guestId +
                ", Name: " + name +
                ", Total Reservations: " + history.size();
    }
     private void saveGuestHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Guest_" + guestId + "_History.csv"))) {
            writer.write("ReservationID,CheckInDate,CheckOutDate,TotalCost\n");
            for (Reservation res : history) {
                writer.write(String.format("%d,%s,%s,%d\n",
                    res.getReservationId(),
                    res.getCheckInDate(),
                    res.getCheckOutDate(),
                    res.calculateTotalCost()));
            }
        } catch (IOException e) {
            System.err.println("Error saving guest history: " + e.getMessage());
        }
    }

    public void loadGuestHistory() {
        File file = new File("Guest_" + guestId + "_History.csv");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine(); // Skip header
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    int reservationId = Integer.parseInt(data[0]);
                    LocalDate checkInDate = LocalDate.parse(data[1]);
                    LocalDate checkOutDate = LocalDate.parse(data[2]);
                    int totalCost = Integer.parseInt(data[3]);

                    history.add(new Reservation(reservationId, guestId, checkInDate, checkOutDate, new ArrayList<>(), new ArrayList<>(), totalCost));
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error loading guest history: " + e.getMessage());
            }
        }
    }
     
}
