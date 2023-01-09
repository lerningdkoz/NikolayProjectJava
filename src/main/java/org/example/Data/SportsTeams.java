package org.example.Data;

import com.opencsv.bean.CsvBindByName;

public class SportsTeams {
    @CsvBindByName(column = "Name")
    private String name;
    @CsvBindByName(column = "\"Team\"")
    private String team;
    @CsvBindByName(column = "\"Position\"")
    private String position;
    @CsvBindByName(column = "\"Height(inches)\"")
    private Integer height;
    @CsvBindByName(column = "\"Weight(lbs)\"")
    private Integer weight;
    @CsvBindByName(column = "\"Age\"")
    private Double age;

    @Override
    public String toString() {
        return "SportsTeams{" +
                "name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", position='" + position + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }
}
