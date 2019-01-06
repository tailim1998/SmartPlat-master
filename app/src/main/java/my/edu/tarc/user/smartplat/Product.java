package my.edu.tarc.user.smartplat;

public class Product {
    private String title;
    private String description;
    private double price;
    private String location;
    private int image;

    public Product(String title, String description, double price, String location, int image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.image = image;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", location='" + location + '\'' +
                ", image=" + image +
                '}';
    }
}
