package project;

import java.util.ArrayList;

public class Room {
    private int roomId;                // Unique identifier for the room
    private String category;           // Room category
    private double price;                 // Room price
    private String status;             // Room status: Available or Occupied
    private ArrayList<Float> ratings;  // List to store guest ratings

    public Room(int roomId, String category, double price) {
        this.roomId = roomId;
        this.category = category;
        this.price = price;
        this.status = "Available"; // Default status
        this.ratings = new ArrayList<>();
    }
      public Room(int roomId) {
        this.roomId = roomId;
        this.category = "Unknown"; // Default value
        this.price = 0.0;          // Default value
        this.status = "Available"; // Default value
        this.ratings = new ArrayList<>();
    }
      
      
        public boolean isAvailable() {
        return "Available".equalsIgnoreCase(status);
    }


    public String toCSV() {
        return roomId + "," + category + "," + price + "," + status;
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return "Room ID: " + roomId +
                ", Category: " + category +
                ", Price: $" + price +
                ", Status: " + status +
                ", Avg. Rating: " + String.format("%.2f", getAverageRating());
    }

    public float getAverageRating() {
        if (ratings.isEmpty()) return 0.0f;
        float sum = 0;
        for (float r : ratings) sum += r;
        return sum / ratings.size();
    }

    public int getRoomId() {
        return roomId;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public void markAsOccupied() {
        this.status = "Occupied";
    }

    public void markAsAvailable() {
        this.status = "Available";
    }

    public void addRating(float rating) {
        if (rating >= 0 && rating <= 5) {
            ratings.add(rating);
            System.out.println("Thank you! Rating added for Room ID: " + roomId);
        } else {
            System.out.println("Invalid rating. Please provide a value between 0 and 5.");
        }
    }
}
