package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;

public class Project extends Application {

    private Admin admin = new Admin(); // Admin instance
    private static HashMap<Integer, Guest> guestRegistry = new HashMap<>();
    private static List<Reservation> activeReservations = Receptionist.loadReservations("ActiveReservations.txt");
private static List<Reservation> completedReservations = Receptionist.loadReservations("CompletedReservations.txt");
StackPane contentPane = new StackPane(); // Example initialization

    public static List<Reservation> getActiveReservations() {
        return activeReservations;
    }

    public static List<Reservation> getCompletedReservations() {
        return completedReservations;
    }

    public static void saveActiveReservations() {
        Receptionist.saveActiveReservations(activeReservations);
    }

    public static void saveCompletedReservations() {
        Receptionist.saveCompletedReservations(completedReservations);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new javafx.scene.image.Image("file:C:/Users/ahmed/OneDrive/Pictures/img.jpg"));

        
// Frame settings
    
           loadAllFiles(); // Load all files at startup
        // Login Screen
        VBox loginLayout = new VBox(20);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setStyle("-fx-alignment: center; -fx-background-color: #f3f3f3;");

        Label loginTitle = new Label("Hotel Management System");
        loginTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button signupButton = new Button("Sign Up"); // New Signup Button
        Button loginButton = new Button("Log In");
        Label loginError = new Label("");
        loginError.setStyle("-fx-text-fill: red;");

        loginLayout.getChildren().addAll(loginTitle, usernameField, passwordField, loginButton, loginError, signupButton);
        Scene loginScene = new Scene(loginLayout, 400, 300);

        // Admin and Receptionist Dashboards
        Scene adminScene = getAdminScene(primaryStage, loginScene);
        Scene receptionistScene = getReceptionistScene(primaryStage, loginScene);
          //signup option
        signupButton.setOnAction(e -> showSignupScreen(primaryStage, loginScene));
        
        

        // Login Action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            String role = validateLogin(username, password);

            if (role.equals("admin")) {
                primaryStage.setScene(adminScene);
            } else if (role.equals("receptionist")) {
                primaryStage.setScene(receptionistScene);
            } else {
                loginError.setText("Invalid username or password!");
            }
        });

        // Stage Settings
        primaryStage.setTitle("Hotel Management System");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private Scene getAdminScene(Stage primaryStage, Scene loginScene) {
        BorderPane adminLayout = new BorderPane();

        // Header
        Label header = new Label("Admin Dashboard");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10;");

        // Navigation Buttons
        VBox nav = new VBox(15);
        nav.setPadding(new Insets(20));
        nav.setStyle("-fx-background-color: #0078D7;");

        Button addRoomButton = new Button("Add Room");
        Button viewRoomsButton = new Button("View Rooms");
        Button removeRoomButton = new Button("Remove Room");
        Button viewServicesButton = new Button("View Services");
        Button addServiceButton = new Button("Create Additional Service");
        Button logoutButton = new Button("Log Out");

        nav.getChildren().addAll(addRoomButton, viewRoomsButton, removeRoomButton, viewServicesButton, addServiceButton, logoutButton);

        // Content Area
        StackPane contentPane = new StackPane();
        Label placeholder = new Label("Welcome to the Admin Dashboard");
        contentPane.getChildren().add(placeholder);

        adminLayout.setTop(header);
        adminLayout.setLeft(nav);
        adminLayout.setCenter(contentPane);

        // Button Actions
        addRoomButton.setOnAction(e -> showAddRoomDialog(contentPane));
        viewRoomsButton.setOnAction(e -> showViewRoomsDialog(contentPane));
        removeRoomButton.setOnAction(e -> showRemoveRoomDialog(contentPane));
        viewServicesButton.setOnAction(e -> showViewServicesDialog(contentPane));
        addServiceButton.setOnAction(e -> showAddServiceDialog(contentPane));
        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));

        return new Scene(adminLayout, 800, 600);
    }

    private Scene getReceptionistScene(Stage primaryStage, Scene loginScene) {
        BorderPane receptionistLayout = new BorderPane();

        // Header
        Label header = new Label("Receptionist Dashboard");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10;");

        // Navigation Buttons
        VBox nav = new VBox(15);
        nav.setPadding(new Insets(20));
        nav.setStyle("-fx-background-color: #0078D7;");

        Button createReservationButton = new Button("Create Reservation");
        Button addRoomToReservationButton = new Button("Add Room to Reservation");
        Button addServiceToReservationButton = new Button("Add Service to Reservation");
        Button completeReservationButton = new Button("Complete Reservation");
        Button viewHistoryButton = new Button("View  History");
        Button viewRatingsButton = new Button("View All Ratings");
        Button logoutButton = new Button("Log Out");

        nav.getChildren().addAll(createReservationButton, addRoomToReservationButton, addServiceToReservationButton,
                completeReservationButton, viewHistoryButton,viewRatingsButton, logoutButton);

        // Content Area
        StackPane contentPane = new StackPane();
        Label placeholder = new Label("Welcome to the Receptionist Dashboard");
        contentPane.getChildren().add(placeholder);

        receptionistLayout.setTop(header);
        receptionistLayout.setLeft(nav);
        receptionistLayout.setCenter(contentPane);

        // Button Actions
        createReservationButton.setOnAction(e -> showCreateReservationDialog(contentPane));
        addRoomToReservationButton.setOnAction(e -> showAddRoomToReservationDialog(contentPane));
        addServiceToReservationButton.setOnAction(e -> showAddServiceToReservationDialog(contentPane));
        completeReservationButton.setOnAction(e -> showCompleteReservationDialog(contentPane));
        viewHistoryButton.setOnAction(e -> showViewHistoryDialog(contentPane));
        viewRatingsButton.setOnAction(e -> showRatingsDialog(contentPane));

        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));

        return new Scene(receptionistLayout, 800, 600);
    }

    private String validateLogin(String username, String password) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[1].equals(username) && userData[2].equals(password)) {
                    return userData[3]; // Return role: admin or receptionist
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        return "";
    }
    

    private Reservation findReservation(int reservationId) {
        for (Reservation res : activeReservations) {
            if (res.getReservationId() == reservationId) {
                return res; // Found the matching reservation
            }
        }
        return null; // No matching reservation found
    }

    private void showCreateReservationDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Create Reservation");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField guestIdField = new TextField();
    guestIdField.setPromptText("Guest ID");
    TextField stayDurationField = new TextField();
    stayDurationField.setPromptText("Stay Duration (days)");

    Button submitButton = new Button("Create Reservation");
    Label statusLabel = new Label();

    submitButton.setOnAction(e -> {
        try {
            int guestId = Integer.parseInt(guestIdField.getText());
            int stayDuration = Integer.parseInt(stayDurationField.getText());

            Reservation res = new Reservation(); // Automatically generates a random ID
            res.setGuestId(guestId);
            res.setCheckInDate(LocalDate.now());
            res.setCheckOutDate(LocalDate.now().plusDays(stayDuration));

            activeReservations.add(res);
            saveActiveReservations();

            statusLabel.setText("Reservation created successfully! Reservation ID: " + res.getReservationId());
            statusLabel.setStyle("-fx-text-fill: green;");
            guestIdField.clear();
            stayDurationField.clear();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, guestIdField, stayDurationField, submitButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}
    
    
    private void showAddRatingDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Add Rating to Reservation");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField reservationIdField = new TextField();
    reservationIdField.setPromptText("Reservation ID");
    TextField ratingField = new TextField();
    ratingField.setPromptText("Rating (0-5)");

    Button submitButton = new Button("Add Rating");
    Label statusLabel = new Label();

    submitButton.setOnAction(e -> {
        try {
            int resId = Integer.parseInt(reservationIdField.getText());
            float rating = Float.parseFloat(ratingField.getText());

            Reservation res = findReservation(resId); // Find the reservation by ID
            if (res == null) throw new Exception("Reservation not found!");

            res.addRating(rating); // Add the rating to the reservation

            statusLabel.setText("Rating added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            reservationIdField.clear();
            ratingField.clear();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, reservationIdField, ratingField, submitButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}
    
    
    private void showDisplayRatingsDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Display Ratings for Reservation");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField reservationIdField = new TextField();
    reservationIdField.setPromptText("Reservation ID");

    Button fetchButton = new Button("Fetch Ratings");
    Label statusLabel = new Label();

    ListView<String> ratingList = new ListView<>();

    fetchButton.setOnAction(e -> {
        try {
            int resId = Integer.parseInt(reservationIdField.getText());

            List<Float> ratings = Reservation.getRatings(resId); // Fetch ratings
            ratingList.getItems().clear();

            if (ratings.isEmpty()) {
                throw new Exception("No ratings found for Reservation ID: " + resId);
            }

            for (float rating : ratings) {
                ratingList.getItems().add("Rating: " + rating);
            }

            statusLabel.setText("Ratings fetched successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, reservationIdField, fetchButton, ratingList, statusLabel);
    contentPane.getChildren().setAll(layout);
}




    private void showCompleteReservationDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Complete Reservation");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField reservationIdField = new TextField();
    reservationIdField.setPromptText("Reservation ID");

    Button completeButton = new Button("Complete Reservation");
    Label statusLabel = new Label();

    completeButton.setOnAction(e -> {
        try {
            int resId = Integer.parseInt(reservationIdField.getText());
            Reservation res = findReservation(resId);

            if (res == null) throw new Exception("Reservation not found!");

            VBox ratingBox = new VBox(10);
            TextField ratingField = new TextField();
            ratingField.setPromptText("Rating (0-5)");
            Button submitRating = new Button("Submit Rating");

            submitRating.setOnAction(ev -> {
                try {
                    float rating = Float.parseFloat(ratingField.getText());
                    res.addRating(rating);
                    statusLabel.setText("Rating added successfully!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            });

            res.completeReservation();
            activeReservations.remove(res);
            completedReservations.add(res);
            saveActiveReservations();
            saveCompletedReservations();
              Receptionist.completeReservation(res, activeReservations, completedReservations);
            layout.getChildren().addAll(ratingBox, ratingField, submitRating);

            statusLabel.setText("Reservation completed successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, reservationIdField, completeButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}


   
    private void showAddRoomDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Add Room");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField roomIdField = new TextField();
    roomIdField.setPromptText("Enter Room ID");

    TextField categoryField = new TextField();
    categoryField.setPromptText("Enter Room Category (e.g., single, double, suite)");

    TextField priceField = new TextField();
    priceField.setPromptText("Enter Room Price");

    Button submitButton = new Button("Add Room");
    Label statusLabel = new Label();

    submitButton.setOnAction(e -> {
        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText());

            if (category.isEmpty() || price <= 0) {
                throw new Exception("Category cannot be empty and price must be positive!");
            }

            Room newRoom = new Room(roomId, category, price); // Assumes Room constructor accepts these arguments
            admin.addRoom(newRoom); // Ensure Admin class has addRoom method
            Project.saveActiveReservations(); // Save rooms to active reservations or a separate room file if needed

            statusLabel.setText("Room added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            roomIdField.clear();
            categoryField.clear();
            priceField.clear();
        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid input! Room ID and price must be numbers.");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, roomIdField, categoryField, priceField, submitButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}


private void showViewRoomsDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("View Rooms");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    ListView<String> roomList = new ListView<>();
    for (Room room : admin.getRoomList()) {
        roomList.getItems().add(room.toString());
    }

    layout.getChildren().addAll(title, roomList);
    contentPane.getChildren().setAll(layout);
}

private void showRemoveRoomDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Remove Room");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField roomIdField = new TextField();
    roomIdField.setPromptText("Enter Room ID");

    Button submitButton = new Button("Remove Room");
    Label statusLabel = new Label();

    submitButton.setOnAction(e -> {
        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            admin.removeRoom(roomId); // Ensure admin has a removeRoom method
            Project.saveActiveReservations();

            statusLabel.setText("Room removed successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            roomIdField.clear();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, roomIdField, submitButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}

private void showViewServicesDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("View Services");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    ListView<String> serviceList = new ListView<>();
    for (AdditionalServices service : admin.getServiceList()) {
        serviceList.getItems().add(service.toString());
    }

    layout.getChildren().addAll(title, serviceList);
    contentPane.getChildren().setAll(layout);
}

private void showAddServiceDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Add Service");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField serviceNameField = new TextField();
    serviceNameField.setPromptText("Enter Service Name");

    TextField servicePriceField = new TextField();
    servicePriceField.setPromptText("Enter Service Price");

    Button submitButton = new Button("Add Service");
    Label statusLabel = new Label();

    submitButton.setOnAction(e -> {
        try {
            String serviceName = serviceNameField.getText();
            double servicePrice = Double.parseDouble(servicePriceField.getText());

            AdditionalServices service = new AdditionalServices(serviceName);
            service.setServicePrice(servicePrice);
            admin.addService(service); // Ensure admin has an addService method
            Project.saveActiveReservations();

            statusLabel.setText("Service added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            serviceNameField.clear();
            servicePriceField.clear();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, serviceNameField, servicePriceField, submitButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}


private void showAddRoomToReservationDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Add Room to Reservation");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField reservationIdField = new TextField();
    reservationIdField.setPromptText("Reservation ID");

    TextField roomIdField = new TextField();
    roomIdField.setPromptText("Room ID");

    Button submitButton = new Button("Add Room");
    Label statusLabel = new Label();

   submitButton.setOnAction(e -> {
    try {
        int resId = Integer.parseInt(reservationIdField.getText());
        int roomId = Integer.parseInt(roomIdField.getText());

        Reservation res = findReservation(resId);
        if (res == null) {
            throw new Exception("Reservation not found!");
        }

        Room room = admin.getRoomById(roomId); // Retrieve room by ID
        if (room == null || !room.isAvailable()) {
            throw new Exception("Room not available!");
        }

        room.markAsOccupied(); // Mark the room as occupied
        res.addRoom(room); // Add room to reservation
        Project.saveActiveReservations();

        statusLabel.setText("Room added successfully!");
        statusLabel.setStyle("-fx-text-fill: green;");
        reservationIdField.clear();
        roomIdField.clear();
    } catch (Exception ex) {
        statusLabel.setText("Error: " + ex.getMessage());
        statusLabel.setStyle("-fx-text-fill: red;");
    }
});


    layout.getChildren().addAll(title, reservationIdField, roomIdField, submitButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}
private void showAddServiceToReservationDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("Add Service to Reservation");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField reservationIdField = new TextField();
    reservationIdField.setPromptText("Reservation ID");

    ComboBox<String> serviceDropdown = new ComboBox<>();
    for (AdditionalServices service : admin.getServiceList()) {
        serviceDropdown.getItems().add(service.getServiceName());
    }

    Button submitButton = new Button("Add Service");
    Label statusLabel = new Label();

    submitButton.setOnAction(e -> {
        try {
            int resId = Integer.parseInt(reservationIdField.getText());
            int selectedIndex = serviceDropdown.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                throw new Exception("No service selected!");
            }

            Reservation res = findReservation(resId);
            if (res == null) {
                throw new Exception("Reservation not found!");
            }

            AdditionalServices selectedService = admin.getServiceList().get(selectedIndex);
            res.addService(selectedService);
            Project.saveActiveReservations();

            statusLabel.setText("Service added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            reservationIdField.clear();
            serviceDropdown.getSelectionModel().clearSelection();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    layout.getChildren().addAll(title, reservationIdField, serviceDropdown, submitButton, statusLabel);
    contentPane.getChildren().setAll(layout);
}

private void showViewHistoryDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("View Reservations History");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    ListView<String> reservationList = new ListView<>();

    // Load active and completed reservations
    List<String> activeReservations = readReservationsFromFile("ActiveReservations.txt");
    List<String> completedReservations = readReservationsFromFile("CompletedReservations.txt");

    // Combine active and completed reservations
    List<String> allReservations = new ArrayList<>();
    allReservations.addAll(activeReservations);
    allReservations.addAll(completedReservations);

    // Populate the ListView with all reservations
    reservationList.getItems().addAll(allReservations);

    // Add the title and the ListView to the layout
    layout.getChildren().addAll(title, reservationList);
    contentPane.getChildren().setAll(layout);
}




/**
 * Helper method to read reservations from a file line-by-line.
 */
private List<String> readReservationsFromFile(String fileName) {
    List<String> reservations = new ArrayList<>();
    File file = new File(fileName);

    if (!file.exists()) {
        System.out.println(fileName + " not found. Returning empty list.");
        return reservations;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        StringBuilder reservationData = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.equals("---")) {
                reservations.add(reservationData.toString().trim());
                reservationData.setLength(0); // Clear buffer for the next reservation
            } else {
                reservationData.append(line).append("\n");
            }
        }

        // Handle last reservation block if the file doesn't end with "---"
        if (reservationData.length() > 0) {
            reservations.add(reservationData.toString().trim());
        }

    } catch (IOException e) {
        System.err.println("Error reading reservations from " + fileName + ": " + e.getMessage());
    }

    return reservations;
}



private void showRatingsDialog(StackPane contentPane) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));

    Label title = new Label("All Ratings");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    ListView<String> ratingsList = new ListView<>();

    // Read ratings from the file
    List<String> ratings = readRatingsFromFile("Ratings.txt");
    ratingsList.getItems().addAll(ratings);

    // Create a back button to close the dialog
    Button backButton = new Button("Back");
    backButton.setOnAction(event -> contentPane.getChildren().clear());

    layout.getChildren().addAll(title, ratingsList, backButton);
    contentPane.getChildren().setAll(layout);
}

private List<String> readRatingsFromFile(String fileName) {
    List<String> ratings = new ArrayList<>();
    File file = new File(fileName);

    if (!file.exists()) {
        System.out.println(fileName + " not found. Returning empty list.");
        return ratings;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            ratings.add(line.trim());
        }
    } catch (IOException e) {
        System.err.println("Error reading ratings from " + fileName + ": " + e.getMessage());
    }

    return ratings;
}






/**
 * Helper method to format reservation details for display.
 */
private String formatReservationDetails(Reservation res) {
    return String.format(
        "ReservationID: %d | GuestID: %d | Check-In: %s | Check-Out: %s | Total Cost: $%d | Status: %s",
        res.getReservationId(),
        res.getGuestId(),
        res.getCheckInDate(),
        res.getCheckOutDate(),
        res.calculateTotalCost(),
        res.getStatus()
    );
}


/**
 * Format reservation details for display in the ListView.
 */






private void showSignupScreen(Stage primaryStage, Scene loginScene) {
    VBox signupLayout = new VBox(10);
    signupLayout.setPadding(new Insets(20));

    Label title = new Label("Sign Up");
    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextField nameField = new TextField();
    nameField.setPromptText("Enter Name");

    TextField usernameField = new TextField();
    usernameField.setPromptText("Enter Username");

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Enter Password");

    ComboBox<String> roleDropdown = new ComboBox<>();
    roleDropdown.getItems().addAll("admin", "receptionist"); // User roles
    roleDropdown.setPromptText("Select Role");

    Button submitButton = new Button("Sign Up");
    Label statusLabel = new Label();

    submitButton.setOnAction(e -> {
        try {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleDropdown.getValue();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
                throw new Exception("All fields are required!");
            }

            saveUser(name, username, password, role); // Save user to file
            statusLabel.setText("Signup successful! You can now log in.");
            statusLabel.setStyle("-fx-text-fill: green;");
            nameField.clear();
            usernameField.clear();
            passwordField.clear();
            roleDropdown.getSelectionModel().clearSelection();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> primaryStage.setScene(loginScene)); // Navigate back to login screen

    signupLayout.getChildren().addAll(title, nameField, usernameField, passwordField, roleDropdown, submitButton, backButton, statusLabel);
    Scene signupScene = new Scene(signupLayout, 400, 300);
    primaryStage.setScene(signupScene);
}


private void saveUser(String name, String username, String password, String role) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
        writer.write(name + "," + username + "," + password + "," + role + "\n");
    }
}



private void saveData(String data, String fileName, boolean append) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append))) {
        writer.write(data);
        writer.newLine(); // Add a newline to separate entries
    } catch (IOException e) {
        System.err.println("Error saving data to " + fileName + ": " + e.getMessage());
    }
}
private void loadAllFiles() {
    System.out.println("Loading all files...");
    activeReservations = Receptionist.loadReservations("ActiveReservations.txt");
    completedReservations = Receptionist.loadReservations("CompletedReservations.txt");
    System.out.println("Loaded active reservations: " + activeReservations.size());
    System.out.println("Loaded completed reservations: " + completedReservations.size());
}




    public static void main(String[] args) {
        launch(args);
    }
}