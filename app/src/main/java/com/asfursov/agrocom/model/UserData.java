package com.asfursov.agrocom.model;

import java.util.ArrayList;
import java.util.UUID;

public class UserData {
    private UUID id;

    public UserData() {
        roles = new ArrayList<>();
    }

    private boolean pwdUpdated;
    private boolean pwdUpdateRequired;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    private String name;

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
        return roles.contains(role);
    }

    public boolean newPasswordRequired() {
        return pwdUpdateRequired;
    }

    public boolean authorized() {
        return id!=null;
    }
}
