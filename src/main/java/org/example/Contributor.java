package org.example;


/**
 * Contributor class represents contributors to a repository
 */
public class Contributor{
    private String login;
    private String location;
    private String company;
    private int contributions;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return
                "user: '" + login + '\'' +
                ", location: '" + location + '\'' +
                ", company: '" + company + '\'' +
                ", contributions: " + contributions;
    }
}
