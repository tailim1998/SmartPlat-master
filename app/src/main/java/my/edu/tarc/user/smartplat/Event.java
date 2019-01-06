package my.edu.tarc.user.smartplat;

public class Event {
    private String title;
    private String description;
    private String datetime;
    private String venue;
    private double fee;
    private int image;

    public Event(String title, String description, String datetime, String venue, double fee, int image) {
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.venue = venue;
        this.fee = fee;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", datetime='" + datetime + '\'' +
                ", venue='" + venue + '\'' +
                ", fee=" + fee +
                ", image=" + image +
                '}';
    }
}
