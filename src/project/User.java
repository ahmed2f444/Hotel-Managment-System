package project;

/**
 * User class serves as the base class for all users in the system.
 */
public class User {
    private int id;            // Unique ID for the user
    private String name;       // Full name of the user
    private String username;   // Username for login
    private String password;   // Password for login
    private String role;       // Role: admin or receptionist

    /**
     * Constructor to initialize a User.
     *
     * @param id       Unique ID for the user.
     * @param name     Full name of the user.
     * @param username Username for login.
     * @param password Password for login.
     * @param role     Role of the user: admin or receptionist.
     */
    public User(int id, String name, String username, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role.toLowerCase();
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.toLowerCase();
    }

    /**
     * Validate if the given username and password match this user's credentials.
     *
     * @param username Input username.
     * @param password Input password.
     * @return True if credentials are valid, false otherwise.
     */
    public boolean validateCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Check if the user's role matches the given role.
     *
     * @param role Role to check (e.g., "admin" or "receptionist").
     * @return True if the role matches, false otherwise.
     */
    public boolean isRole(String role) {
        return this.role.equalsIgnoreCase(role);
    }

    /**
     * String representation of the User object.
     *
     * @return User details.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Username: " + username + ", Role: " + role;
    }
}
