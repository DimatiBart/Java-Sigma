package com.DimatiBart.sigma.resources.dataPackages;

import java.io.Serializable;

/**
 * Created by Dimati_Bart on 30.11.15.
 */
public class UserData implements Serializable{
    private String email;
    private String password;
    private int id;
    private boolean type;
    private String command;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
