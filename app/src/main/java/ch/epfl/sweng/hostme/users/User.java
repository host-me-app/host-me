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

    /**
     * get the name of the user
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of the user
     *
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the image of the user
     *
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * set the image of the user
     *
     * @param image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * get the email of the user
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set the email of the user
     *
     * @param email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get the token of the user
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * set the token of the user
     *
     * @param token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * get the id of the user
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * set the id of the user
     *
     * @param id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
