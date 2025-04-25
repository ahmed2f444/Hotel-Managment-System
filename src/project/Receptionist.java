package project;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receptionist {
    private static final String ACTIVE_RESERVATIONS_FILE = "ActiveReservations.txt";
    private static final String COMPLETED_RESERVATIONS_FILE = "CompletedReservations.txt";
    

    

    // Save active reservations
    public static void saveActiveReservations(List<Reservation> reservations) {
        saveReservations(reservations, ACTIVE_RESERVATIONS_FILE);
    }
 
       
    // Save completed reservations
    public static void saveCompletedReservations(List<Reservation> reservations) {
        saveReservations(reservations, COMPLETED_RESERVATIONS_FILE);
    }

    // General method to save reservations to a  file :)
public static void saveReservations(List<Reservation> reservations, String fileName) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        for (Reservation reservation : reservations) {
            writer.write(formatReservationForFile(reservation));
            writer.write("\n---\n"); // Separator for reservations
        }
    } catch (IOException e) {
        System.err.println("Error saving reservations to " + fileName + ": " + e.getMessage());
    }
}
private static String formatReservationForFile(Reservation res) {
    return String.format(
        "ReservationID:%d\nGuestID:%d\nCheckInDate:%s\nCheckOutDate:%s\nStatus:%s\nRooms:[%s]\nServices:[%s]\nTotalCost:%d",
        res.getReservationId(),
        res.getGuestId(),
        res.getCheckInDate(),
        res.getCheckOutDate(),
        res.getStatus() == null ? "Active" : res.getStatus(), // Default to Active
        formatRoomsForFile(res.getRooms()),
        formatServicesForFile(res.getServices()),
        res.getTotalCost()
    );
}


 // format rooms and services in the two methods below to string for saving.
 
private static String formatRoomsForFile(List<Room> rooms) {
    if (rooms.isEmpty()) {
        return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Room room : rooms) {
        sb.append(room.getRoomId()).append(",").append(room.getPrice()).append(";");
    }
    return sb.toString();
}


private static String formatServicesForFile(List<AdditionalServices> services) {
    if (services.isEmpty()) {
        return "";
    }
    StringBuilder sb = new StringBuilder();
    for (AdditionalServices service : services) {
        sb.append(service.getServiceName()).append(",").append(service.getServicePrice()).append(";");
    }
    return sb.toString();
}







    // Load reservations from a specified file
 public static List<Reservation> loadReservations(String fileName) {
    List<Reservation> reservations = new ArrayList<>();
    File file = new File(fileName);

    if (!file.exists()) {
        System.out.println(fileName + " not found. Initializing with empty reservations.");
        return reservations;
    }

    System.out.println("Loading reservations from: " + fileName);

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        StringBuilder reservationData = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.equals("---")) {
                // Parse and add reservation
                Reservation reservation = parseReservation(reservationData.toString());
                if (reservation != null) {
                    reservations.add(reservation);
                    System.out.println("Loaded Reservation: " + reservation); // Debugging
                } else {
                    System.err.println("Skipping invalid reservation block.");
                }
                reservationData.setLength(0); // Clear buffer for next reservation
            } else {
                reservationData.append(line).append("\n");
            }
        }

        // Handle last reservation block if file doesn't end with "---"
        if (reservationData.length() > 0) {
            Reservation reservation = parseReservation(reservationData.toString());
            if (reservation != null) {
                reservations.add(reservation);
                System.out.println("Loaded Reservation: " + reservation); // Debugging
            }
        }

        System.out.println("Finished loading reservations. Total: " + reservations.size());
    } catch (IOException e) {
        System.err.println("Error loading reservations from " + fileName + ": " + e.getMessage());
    }

    return reservations;
}



private static Reservation parseReservation(String data) {
    Reservation reservation = new Reservation();
    try {
        String[] lines = data.split("\n");

        for (String line : lines) {
            if (line.startsWith("ReservationID:")) {
                reservation.setReservationId(Integer.parseInt(line.split(":")[1].trim()));
            } else if (line.startsWith("GuestID:")) {
                reservation.setGuestId(Integer.parseInt(line.split(":")[1].trim()));
            } else if (line.startsWith("CheckInDate:")) {
                reservation.setCheckInDate(LocalDate.parse(line.split(":")[1].trim()));
            } else if (line.startsWith("CheckOutDate:")) {
                reservation.setCheckOutDate(LocalDate.parse(line.split(":")[1].trim()));
            } else if (line.startsWith("Status:")) {
                String status = line.split(":")[1].trim();
                // Default to "Active" if status is null or invalid
                reservation.setStatus(status.isEmpty() || status.equalsIgnoreCase("null") ? "Active" : status);
            } else if (line.startsWith("Rooms:[")) {
                String roomData = line.substring(7, line.length() - 1); // Extract room data
                reservation.setRooms(roomData.isEmpty() ? new ArrayList<>() : parseRooms(roomData));
            } else if (line.startsWith("Services:[")) {
                String serviceData = line.substring(10, line.length() - 1); // Extract service data
                reservation.setServices(serviceData.isEmpty() ? new ArrayList<>() : parseServices(serviceData));
            } else if (line.startsWith("TotalCost:")) {
                reservation.setTotalCost(Integer.parseInt(line.split(":")[1].trim()));
            }
        }
    } catch (Exception e) {
        System.err.println("Error parsing reservation: " + data);
        System.err.println("Details: " + e.getMessage());
    }
    return reservation;
}











  
  
 private static List<Room> parseRooms(String roomData) {
    List<Room> rooms = new ArrayList<>();
    try {
        String[] roomEntries = roomData.split(";");
        for (String entry : roomEntries) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                int roomId = Integer.parseInt(parts[0].trim());
                double price = Double.parseDouble(parts[1].trim());
                rooms.add(new Room(roomId, "Unknown", price)); // Default category: Unknown
            }
        }
    } catch (Exception e) {
        System.err.println("Error parsing rooms: " + roomData);
        System.err.println("Details: " + e.getMessage());
    }
    return rooms;
}

 
 
 
 private void ensureReservationFilesExist() {
    try {
        File activeReservationsFile = new File("ActiveReservations.txt");
        if (!activeReservationsFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(activeReservationsFile))) {
                System.out.println("ActiveReservations.txt created as an empty file.");
            }
        }

        File completedReservationsFile = new File("CompletedReservations.txt");
        if (!completedReservationsFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(completedReservationsFile))) {
                System.out.println("CompletedReservations.txt created as an empty file.");
            }
        }
    } catch (IOException e) {
        System.err.println("Error ensuring reservation files exist: " + e.getMessage());
    }
}






    
  private static List<AdditionalServices> parseServices(String serviceData) {
    List<AdditionalServices> services = new ArrayList<>();
    try {
        String[] serviceEntries = serviceData.split(";");
        for (String entry : serviceEntries) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                String serviceName = parts[0].trim();
                double servicePrice = Double.parseDouble(parts[1].trim());
                services.add(new AdditionalServices(serviceName, (int) servicePrice));
            }
        }
    } catch (Exception e) {
        System.err.println("Error parsing services: " + serviceData);
        System.err.println("Details: " + e.getMessage());
    }
    return services;
}


    
    private static List<Float> parseRatings(String ratingsData) {
    List<Float> ratings = new ArrayList<>();
    if (ratingsData.isEmpty() || ratingsData.equals("[]")) {
        return ratings; // Return empty list if no ratings
    }

    ratingsData = ratingsData.replace("[", "").replace("]", "").trim(); // Remove brackets
    String[] ratingPairs = ratingsData.split(";");
    for (String pair : ratingPairs) {
        if (!pair.isEmpty() && pair.contains(":")) {
            try {
                String[] parts = pair.split(":");
                Float rating = Float.parseFloat(parts[1].trim());
                ratings.add(rating);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing rating pair: " + pair);
            }
        }
    }
    return ratings;
}
    
    
    
  private static void assignRatingsToRooms(List<Room> rooms, List<Float> ratings) {
    if (rooms == null || ratings == null) {
        System.err.println("Rooms or ratings are null, cannot assign ratings.");
        return;
    }

    for (int i = 0; i < rooms.size() && i < ratings.size(); i++) {
        try {
            rooms.get(i).addRating(ratings.get(i)); // Assumes Room has an addRating method
        } catch (Exception e) {
            System.err.println("Error assigning rating to room: " + rooms.get(i) + " - " + e.getMessage());
        }
    }
}
  
  
 public static void completeReservation(Reservation res, 
                                       List<Reservation> activeReservations, 
                                       List<Reservation> completedReservations) {
    if (activeReservations.remove(res)) {
        res.completeReservation(); // Mark as completed
        completedReservations.add(res);

        // Save updated reservations
        saveActiveReservations(activeReservations);
        saveCompletedReservations(completedReservations);
        System.out.println("Reservation completed and saved.");
    } else {
        System.err.println("Error: Reservation not found in active list.");
    }
}







    
    
}

