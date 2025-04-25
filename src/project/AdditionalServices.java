package project;

public class AdditionalServices {
    private static int idCounter = 1; // Static counter for unique service IDs
    private int serviceId;            // Unique ID for the service
    private String serviceName;       // Name of the service
    private double servicePrice;         // Price of the service

    public AdditionalServices(String name, int price) {
        this.serviceId = idCounter++; // Generate unique ID
        this.serviceName = name;
        this.servicePrice = price;
    }
    
    public AdditionalServices(String serviceName) {
        this.serviceName = serviceName;
        this.servicePrice = 0.0; // Default value
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServicePrice() {
        return servicePrice;
    }
      public void setServicePrice(double price) {
        this.servicePrice = price;
    }

    @Override
    public String toString() {
        return "Service ID: " + serviceId + ", Name: " + serviceName + ", Price: $" + servicePrice;
    }
    public String toCSV() {
        return serviceId + "," + serviceName + "," + servicePrice;
    }
}
