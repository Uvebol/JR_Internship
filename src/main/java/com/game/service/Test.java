package com.game.service;

import java.sql.*;

public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        //test.test();
        //test.sql();
    }

    public void test() {
        //java -classpath c:\Java\mysql-connector-java-8.0.11.jar;c:\Java Program
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public void sql() {
        String url = "jdbc:mysql://localhost/rpg?serverTimezone=Europe/Moscow&useSSL=false";
        String username = "root";
        String password = "root";

        String query = "SELECT COUNT * FROM rpg.player";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rpg.player WHERE race = 'ORC'");
            while (resultSet.next()){
                String str = resultSet.getString("name");
                System.out.println(str);
            }

            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}