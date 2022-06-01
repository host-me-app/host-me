package ch.epfl.sweng.hostme.users;

import java.io.Serializable;

public class User implements Serializable {
    private String name, image, email, token, id;

    public User() {
    }

    public User(String name, String image, String email, String token, String id) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.token = token;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(String id) {
        this.id = id;
    }
}
