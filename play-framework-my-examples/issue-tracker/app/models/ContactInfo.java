package models;

public class ContactInfo {

    public final String telephone;
    public final String email;
    public final String address;

    public ContactInfo(String telephone, String email, String address) {
        this.telephone = telephone;
        this.email = email;
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}
