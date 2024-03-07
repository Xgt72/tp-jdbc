package dev.xgeorget.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirstJDBCConnection {

  public static void main(String[] args) {
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_jdbc", "XgtJava",
        "j5Riu7!o,DAegocdeHhE");) {

      System.out.println("Connection = " + connection);

      String sql1 = "SELECT COUNT(*) count FROM User";
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql1);

      System.out.println("\n" + sql1);
      while (resultSet.next()) {
        long count = resultSet.getLong("count");
        System.out.println("Count = " + count);
      }

      String sql2 = "SELECT name FROM User";
      resultSet = statement.executeQuery(sql2);

      System.out.println("\n" + sql2);
      while (resultSet.next()) {
        String name = resultSet.getString("name");
        System.out.println("Name = " + name);
      }

      String sql3 = "SELECT id, name FROM User";
      resultSet = statement.executeQuery(sql3);

      System.out.println("\n" + sql3);
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        System.out.println("id, name = " + id + ", " + name);
      }

      String sql4 = "SELECT id, name FROM User WHERE id = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(sql4);
      preparedStatement.setInt(1, 2);
      resultSet = preparedStatement.executeQuery();

      System.out.println("\n" + sql4);
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        System.out.println("id, name = " + id + ", " + name);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
