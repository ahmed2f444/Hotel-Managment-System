package project;

import java.io.*;
import java.util.ArrayList;

public class Admin extends User {
    private ArrayList<Room> roomList = new ArrayList<>();
    private ArrayList<AdditionalServices> serviceList = new ArrayList<>();
    private final String filePath = "SaveFile.txt";

    public Admin() {
        super(0, "Admin", "admin", "admin123", "admin"); // Default Admin credentials
        this.roomList = new ArrayList<>();
        this.serviceList = new ArrayList<>();
        loadDataFromFile(); // Load existing data from SaveFile.txt
    }

    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    public ArrayList<AdditionalServices> getServiceList() {
        return serviceList;
    }
    
      public Room getRoomById(int roomId) {
        for (Room room : roomList) {
            if (room.getRoomId() == roomId) {
                return room;
            }
        }
        return null; // Return null if the room is not found
    }

    public void addRoom(Room room) {
        roomList.add(room);
        saveToFile();
        System.out.println("Room added successfully: " + room);
    }

    public boolean removeRoom(int roomId) {
        for (Room room : roomList) {
            if (room.getRoomId() == roomId) {
                roomList.remove(room);
                saveToFile(); // Save changes after removal
                return true;
            }
        }
        return false;
    }

    public void viewRoomData() {
        if (roomList.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            System.out.println("\n========== Room List ==========");
            for (Room room : roomList) {
                System.out.println(room);
            }
        }
    }

    public void viewAdditionalServicesData() {
        if (serviceList.isEmpty()) {
            System.out.println("No additional services available.");
        } else {
            System.out.println("\n========== Additional Services List ==========");
            for (AdditionalServices service : serviceList) {
                System.out.println(service);
            }
        }
    }

    public void addService(AdditionalServices service) {
        serviceList.add(service);
        saveToFile();
        System.out.println("Service added successfully: " + service);
    }

    public boolean removeService(int serviceId) {
        for (AdditionalServices service : serviceList) {
            if (service.getServiceId() == serviceId) {
                serviceList.remove(service);
                saveToFile(); // Save changes after removal
                return true;
            }
        }
        return false;
    }

   private void saveToFile() {
    File file = new File(filePath);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        for (Room room : roomList) {
            writer.write("Room," + room.toCSV() + "\n");
        }
        for (AdditionalServices service : serviceList) {
            writer.write("Service," + service.toCSV() + "\n");
        }
    } catch (IOException e) {
        System.err.println("Error saving data: " + e.getMessage());
    }
}


  private void loadDataFromFile() {
    roomList.clear();
    serviceList.clear();
    File file = new File(filePath);

    if (!file.exists()) {
        System.out.println("SaveFile.txt not found. Starting fresh.");
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Room,")) {
                String[] data = line.substring(5).split(",");
                int roomId = Integer.parseInt(data[0]);
                String category = data[1];
                double price = Double.parseDouble(data[2]);
                String status = data[3];
                Room room = new Room(roomId, category, (int) price);
                if ("Occupied".equalsIgnoreCase(status)) {
                    room.markAsOccupied();
                }
                roomList.add(room);
            } else if (line.startsWith("Service,")) {
                String[] data = line.substring(8).split(",");
                int serviceId = Integer.parseInt(data[0]);
                String serviceName = data[1];
                double servicePrice = Double.parseDouble(data[2]);
                AdditionalServices service = new AdditionalServices(serviceName, (int) servicePrice);
                serviceList.add(service);
            }
        }
    } catch (IOException | NumberFormatException e) {
        System.err.println("Error loading data from SaveFile.txt: " + e.getMessage());
    }
}
}

