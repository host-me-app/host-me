package ch.epfl.sweng.hostme.users;

import java.io.Serializable;

public class User implements Serializable {
    public String name, image, email, token;

    public User() {
    }

    public User(String name, String image, String email, String token) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.token = token;
    }
}
