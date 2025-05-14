package RHMS.usermanagement;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String phone;
    private boolean loggedIn;

    public User(String id, String name, String email, String password, String role, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.loggedIn = false;
    }

    public void updateProfile(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getPhone() { return phone; }
    public boolean isLoggedIn() { return loggedIn; }

    public boolean login(String password) {
        boolean result = password.equals(this.password);
        if (result) loggedIn = true;
        return result;
    }

    public void logout() {
        System.out.println(name + " has logged out.");
        loggedIn = false;
    }

    public void displayUserInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Role: " + role);
        System.out.println("Phone: " + phone);
    }
}