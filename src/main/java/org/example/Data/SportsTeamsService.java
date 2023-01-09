package org.example.Data;

import com.opencsv.bean.CsvToBeanBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportsTeamsService {

    private final Connection connection;

    public SportsTeamsService(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        String drop = "DROP TABLE IF EXISTS teams";
        String query = "CREATE TABLE IF NOT EXISTS teams(\n" +
                "id INT AUTO_INCREMENT,\n" +
                "name_player VARCHAR(250),\n" +
                "name_team VARCHAR(250),\n" +
                "position_player VARCHAR(250),\n" +
                "height INT,\n" +
                "weight INT,\n" +
                "age DOUBLE\n" +
                ");";
        try {
            Statement statement = connection.createStatement();
            statement.execute(drop);
            statement.execute(query);
            statement.close();
            System.out.println("Успешное создание таблицы");
        } catch (SQLException e) {
            throw new RuntimeException("Таблица не создана");
        }
    }

    public void read(String path) {
        try {
            List<SportsTeams> teams = new CsvToBeanBuilder(new FileReader(path))
                    .withType(SportsTeams.class)
                    .build()
                    .parse();
            for (var team : teams) {
                String query = "INSERT INTO teams (name_player, name_team, position_player, height, weight, age) VALUES " +
                        "('" + team.getName().replaceAll("'", "") + "', " +
                        "'" + team.getTeam().replaceAll("\"", "").replaceAll(" ", "") + "', " +
                        "'" + team.getPosition().replaceAll("\"", "").replaceAll(" ", "") + "', " +
                        "" + team.getHeight() + ", " +
                        "" + team.getWeight() + ", " + team.getAge() + ")";
                try {
                    Statement statement = connection.createStatement();
                    statement.execute(query);
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Ошибка записи: " + team.toString());
                }
            }
            System.out.println("Данные успешно добавлены");
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка в добавлении данных");
        }
    }

    public void graph() {
        Map<String, Double> map = new HashMap<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name_team as 'name_team', AVG(age) as 'age' " +
                    "FROM teams " +
                    "GROUP BY name_team");
            while (rs.next()) {
                map.put(rs.getString("name_team"), rs.getDouble("age"));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Ошибка в подгрузке данных к графику");
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (var entry : map.entrySet()) {
            dataset.addValue(entry.getValue(), entry.getKey(), "Команда");
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Средний возраст",
                "Команда",
                "Средний возраст",
                dataset);
        chart.setBackgroundPaint(Color.white);

        JFrame frame =
                new JFrame("Первое задание");
        frame.getContentPane()
                .add(new ChartPanel(chart));
        frame.setSize(700, 500);
        frame.setVisible(true);
    }

    public void getSecond() {
        String name = "";
        String query = "SELECT name_team as 'name_team', AVG(height) as 'height' FROM teams " +
                "GROUP BY name_team ";
        double max_value = Double.MIN_VALUE;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int counter = 0;
            while (rs.next()) {
                if (rs.getDouble("height") > max_value) {
                    name = rs.getString("name_team");
                    max_value = rs.getDouble("height");
                }
            }
            System.out.println("(Второе задание)");
            System.out.println("Team: " + name + ", avg height: " + max_value);
            String secondQuery = "SELECT name_player as 'name_player' FROM teams " +
                    "WHERE teams.name_team = '" + name + "' " +
                    "ORDER BY teams.height DESC";
            rs = statement.executeQuery(secondQuery);
            while (rs.next()) {
                if (counter < 5) {
                    System.out.println("Player " + (counter + 1)+": " + rs.getString("name_player"));
                } else {
                    break;
                }
                counter++;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка во втором задании");
        }
    }

    public void getThird() {
        double max_value = Double.MIN_VALUE;
        String name = "";
        String query = "SELECT name_team as 'name', AVG(age) as 'age', AVG(teams.height) as 'height', " +
                "AVG(teams.weight) as 'weight' " +
                "FROM teams " +
                "GROUP BY teams.name_team ";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getDouble("height") >= 74 && rs.getDouble("height") <= 78
                        && rs.getDouble("weight") >= 190 && rs.getDouble("weight") <= 210) {
                    if (rs.getDouble("age") > max_value) {
                        name = rs.getString("name");
                        max_value = rs.getDouble("age");
                    }
                }
            }
            System.out.println("(Третье задание)");
            System.out.println("Team: " + name);
        } catch (SQLException e) {
            System.out.println("Ошибка в третьем задании");
        }
    }


}
