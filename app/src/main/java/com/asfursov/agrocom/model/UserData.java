package com.asfursov.agrocom.model;

import java.util.ArrayList;
import java.util.UUID;

public class UserData {
    private UUID id;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    private String Name;
    private ArrayList<Role> roles;

    public String getRolesAsText() {
        StringBuilder sb = new StringBuilder();
        for (Role r:roles
             ) {
            sb.append(r.getName());
        }
        return sb.toString();
    }

    public boolean  hasRole(Role role) {
        if (roles.indexOf(role)>=0) return true;
        return false;
    }

    public boolean newPasswordRequired() {
        return true;
    }
}
