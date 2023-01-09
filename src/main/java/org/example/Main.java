package org.example;

import org.example.Data.SportsTeamsService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static Connection connection;

    public static void main(String[] args) {
        open();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите путь до csv-файла: ");
        //Показатели спортивных команд.csv
        String path = scanner.nextLine();
        SportsTeamsService sportsTeamsService = new SportsTeamsService(connection);
        //sportsTeamsService.createTable();
        //sportsTeamsService.read(path);
        sportsTeamsService.graph();
        sportsTeamsService.getSecond();
        sportsTeamsService.getThird();

        close();
    }


    public static void open(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:sportsTeams.db");
            System.out.println("Успешное подключение к БД");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Не удалось подключиться к БД");
        }
    }

    public static void close(){
        try{
            connection.close();
            System.out.println("Успешный разрыв соединения с БД");
        } catch (SQLException e) {
            System.out.println("Не удалось разорвать соединение с БД");
        }
    }
}