package com.leroymerlin.corp.fr.nuxeo.labs.site.contact;

public class ContactDto {
    private String ldap;
    private String displayName;
    private String email;
    
    public ContactDto(String ldap, String displayName, String email) {
        this.ldap = ldap;
        this.displayName = displayName;
        this.email = email;
    }

    public String getLdap() {
        return ldap;
    }

    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
