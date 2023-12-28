package com.example.bottomnavigation;

public class Target {

    public Target(String g,String d) {
        this.date=d;
        this.goal=g;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    private String date;

    private String goal;
}

