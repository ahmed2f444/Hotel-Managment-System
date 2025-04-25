package project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reservation {
    private static int idCounter;              // Static counter for unique IDs
    private int reservationId;                    // Unique ID for the reservation
    private int guestId;                          // Guest ID associated with the reservation
    private LocalDate checkInDate;                // Check-in date
    private LocalDate checkOutDate;               // Check-out date
    private ArrayList<Room> rooms;                // List of rooms in the reservation
    private ArrayList<AdditionalServices> services; // List of additional services
    private String status;  
    private List<Float> ratings; // Ratings for the reservation
    private static final String ID_COUNTER_FILE = "ReservationIDCounter.txt";
        private int totalCost;            // Total cost of the reservation// Reservation status: Active or Completed

    /**
     * Constructor to initialize a Reservation with auto-generated ID and dates.
     *
     */
        
        
        
         public Reservation() {
        this.rooms = new ArrayList<>();
        this.services = new ArrayList<>();
          this.reservationId = generateRandomReservationId();
        this.ratings = new ArrayList<>();
    }
         
    public Reservation(int guestId, int stayDays) {
        this.reservationId = idCounter++; // Generate a unique reservation ID
        this.guestId = guestId;
        this.checkInDate = LocalDate.now(); // Automatically sets today's date
        this.checkOutDate = checkInDate.plusDays(stayDays); // Calculates check-out date
        this.rooms = new ArrayList<>();
        this.services = new ArrayList<>();
        this.status = "Active";
        
    }
    
    public Reservation(int reservationId, int guestId, LocalDate checkInDate, 
                   LocalDate checkOutDate, ArrayList<Room> rooms, 
                   ArrayList<AdditionalServices> services, int totalCost) {
    this.reservationId = reservationId;
    this.guestId = guestId;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.rooms = rooms != null ? rooms : new ArrayList<>();
    this.services = services != null ? services : new ArrayList<>();
    this.status = "Active"; // Default status for new reservations
}
    
    
    public Reservation(int guestId, LocalDate checkInDate, LocalDate checkOutDate,
                   ArrayList<Room> rooms, ArrayList<AdditionalServices> services, int totalCost) {
    this.reservationId = generateNewId(); // Generate a unique reservation ID
    this.guestId = guestId;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.rooms = rooms != null ? rooms : new ArrayList<>();
    this.services = services != null ? services : new ArrayList<>();
    this.totalCost = totalCost;
    this.status = "Active"; // Default status
}
     static {
        idCounter = loadIdCounter();
    }


private static int loadIdCounter() {
    File file = new File(ID_COUNTER_FILE);
    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading reservation ID counter: " + e.getMessage());
        }
    }
    return 1; // Start from 1 if no file exists
}

private static void saveIdCounter() {
    try {
        Files.move(Paths.get(ID_COUNTER_FILE), Paths.get(ID_COUNTER_FILE + ".bak"), StandardCopyOption.REPLACE_EXISTING);
        BufferedWriter writer = new BufferedWriter(new FileWriter(ID_COUNTER_FILE));
        writer.write(String.valueOf(idCounter));
        writer.close();
    } catch (IOException e) {
        System.err.println("Failed to save Reservation ID Counter.");
    }
}


public static int generateNewId() {
        int newId = idCounter++;
        saveIdCounter();
        return newId;
    }



    // Getters and Setters

    public int getReservationId() {
        return reservationId;
    }

    public int getGuestId() {
        return guestId;
    }
    
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }
     public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }
     public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
     public void setRooms(List<Room> rooms) {
        this.rooms = new ArrayList<>(rooms);
    }

    public ArrayList<AdditionalServices> getServices() {
        return services;
    }
    
    public void setServices(List<AdditionalServices> services) {
        this.services = new ArrayList<>(services);
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    
  public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
    /**
     * Add a room to the reservation.
     *
     * @param room Room object to add.
     */
    public void addRoom(Room room) {
        room.markAsOccupied();
        rooms.add(room);
         totalCost = calculateTotalCost(); // Recalculate total cost
        System.out.println("Room added to reservation: " + room);
    }

    /**
     * Add a service to the reservation.
     *
     * @param service Additional service to add.
     */
    public void addService(AdditionalServices service) {
        services.add(service);
        totalCost = calculateTotalCost(); // Recalculate total cost
        System.out.println("Service added to reservation: " + service);
    }

    /**
     * List all services associated with this reservation.
     *
     * @return A string representation of all additional services.
     */
    public String listServices() {
        StringBuilder serviceDetails = new StringBuilder();
        for (AdditionalServices service : services) {
            serviceDetails.append(service).append("\n");
        }
        return serviceDetails.toString().isEmpty() ? "No services added yet." : serviceDetails.toString();
    }

    /**
     * Calculate the total cost of the reservation.
     *
     * @return Total cost including rooms and services.
     */
    public int calculateTotalCost() {
        int total = 0;
        for (Room room : rooms) {
            total += room.getPrice();
        }
        for (AdditionalServices service : services) {
            total += service.getServicePrice();
        }
        return total;
    }

    /**
     * Complete the reservation and mark all rooms as available.
     */
    public void completeReservation() {
        for (Room room : rooms) {
            room.markAsAvailable();
        }
        this.status = "Completed";
        System.out.println("Reservation completed successfully.");
    }

    /**
     * Convert reservation data to CSV-friendly format.
     *
     * @return A string containing reservation data in CSV format.
     */
 
public String toCSV() {
    StringBuilder csv = new StringBuilder();
    csv.append("ReservationID:").append(reservationId).append("\n");
    csv.append("GuestID:").append(guestId).append("\n");
    csv.append("CheckInDate:").append(checkInDate).append("\n");
    csv.append("CheckOutDate:").append(checkOutDate).append("\n");
    csv.append("Status:").append(status).append("\n");

    csv.append("Rooms:[");
    for (Room room : rooms) {
        csv.append(room.getRoomId()).append(",").append(room.getPrice()).append(";");
    }
    csv.append("]\n");

    csv.append("Services:[");
    for (AdditionalServices service : services) {
        csv.append(service.getServiceName()).append(",").append(service.getServicePrice()).append(";");
    }
    csv.append("]\n");

    csv.append("TotalCost:").append(calculateTotalCost()).append("\n");

    return csv.toString();
}





    
    /**
     * String representation of the Reservation object.
     *
     * @return A readable string displaying reservation details.
     */
 @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ReservationID: ").append(reservationId).append("\n");
    sb.append("GuestID: ").append(guestId).append("\n");
    sb.append("CheckInDate: ").append(checkInDate).append("\n");
    sb.append("CheckOutDate: ").append(checkOutDate).append("\n");
    sb.append("Status: ").append(status).append("\n");

    sb.append("Rooms: [");
    for (Room room : rooms) {
        sb.append("Room ").append(room.getRoomId()).append(" ($").append(room.getPrice()).append("); ");
    }
    sb.append("]\n");

    sb.append("Services: [");
    for (AdditionalServices service : services) {
        sb.append(service.getServiceName()).append(" ($").append(service.getServicePrice()).append("); ");
    }
    sb.append("]\n");

    sb.append("Total Cost: $").append(calculateTotalCost()).append("\n");
    return sb.toString();
}





private String servicesToStringWithCosts() {
    double totalCost = services.stream().mapToDouble(AdditionalServices::getServicePrice).sum();
    String servicesDetail = services.stream()
                                     .map(service -> service.getServiceName() + " ($" + service.getServicePrice() + ")")
                                     .collect(Collectors.joining("; "));
    return "[" + servicesDetail + "] Total Cost: $" + String.format("%.2f", totalCost);
}



private String roomsToString() {
    return "[" + rooms.stream()
                      .map(Room::getRoomId)
                      .map(String::valueOf)
                      .collect(Collectors.joining(";")) + "]";
}

 public void addRating(float rating) {
        if (rating >= 0 && rating <= 5) {
            ratings.add(rating);
            System.out.println("Thank you! Rating added for Reservation ID: " + reservationId);
            saveRatingToFile(reservationId, rating);
        } else {
            System.out.println("Invalid rating. Please provide a value between 0 and 5.");
        }
    }
 
 
 
  private void saveRatingToFile(int reservationId, float rating) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Ratings.txt", true))) {
            writer.write("ReservationID: " + reservationId + ", Rating: " + rating + "\n");
        } catch (IOException e) {
            System.err.println("Error saving rating: " + e.getMessage());
        }
    }
  
  
  
  public static List<Float> getRatings(int reservationId) {
        List<Float> ratings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Ratings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ReservationID: " + reservationId)) {
                    float rating = Float.parseFloat(line.split(":")[2].trim());
                    ratings.add(rating);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading ratings for ReservationID " + reservationId + ": " + e.getMessage());
        }
        return ratings;
    }
  
  
  
  
   public void displayRatings() {
        List<Float> reservationRatings = getRatings(reservationId);
        if (reservationRatings.isEmpty()) {
            System.out.println("No ratings available for Reservation ID: " + reservationId);
        } else {
            System.out.println("Ratings for Reservation ID: " + reservationId + " -> " + reservationRatings);
        }
    }
   
   
   
   
   
   
   private static int generateRandomReservationId() {
        return (int) (Math.random() * 1_000_000); // Random ID up to 1,000,000
    }



}
