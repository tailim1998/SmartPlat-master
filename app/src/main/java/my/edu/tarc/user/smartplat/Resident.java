package my.edu.tarc.user.smartplat;

public class Resident {
    private String Name;
    private String Contact;
    private String Email;
    private String Address;
    private String Image;
    private String Username;
    private String Password;


    public Resident() {
    }

    public Resident(String email, String username, String password) {
        Email = email;
        Username = username;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
