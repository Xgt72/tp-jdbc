package dev.xgeorget.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dev.xgeorget.jdbc.model.Commune;

public class PlayWithCommune {
  public static void main(String[] args) {

    Map<String, Commune> communes = new HashMap<>();

    try (Connection connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/tp_jdbc_communes",
        "XgtJava",
        "j5Riu7!o,DAegocdeHhE");) {

      String sql = "INSERT INTO commune (code_postal, nom) VALUES (?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);

      Path path = Path.of("data/maires-25-04-2014.csv");
      try (BufferedReader reader = Files.newBufferedReader(path);) {
        String line = reader.readLine();
        line = reader.readLine();

        while (line != null) {
          // System.out.println(line);
          String[] splitLine = line.split(";");
          String codeDepartement = splitLine[0];

          // ZA, ZB, ZC ,ZD ,ZM ,ZN ,ZP ,ZS
          if (codeDepartement.length() == 1) {
            codeDepartement = "0" + codeDepartement;
          } else if (codeDepartement.startsWith("Z")) {

            if (codeDepartement.equals("ZA") | codeDepartement.equals("ZB") | codeDepartement.equals("ZC")
                | codeDepartement.equals("ZD")
                | codeDepartement.equals("ZS") | codeDepartement.equals("ZM")) {
              codeDepartement = "97";
            } else if (codeDepartement.equals("ZP")) {
              codeDepartement = "987";
            } else if (codeDepartement.equals("ZN")) {
              codeDepartement = "98";
            }
          }

          String codeInsee = splitLine[2];
          if (codeInsee.length() == 1) {
            codeInsee = "00" + codeInsee;
          } else if (codeInsee.length() == 2 && !codeDepartement.equals("987")) {
            codeInsee = "0" + codeInsee;

          }

          String codePostal = codeDepartement + codeInsee;
          // System.out.println("Code Postal = " + codePostal);

          String nom = splitLine[3];

          Commune commune = new Commune(codePostal, nom);

          Commune previousCommune = communes.put(codePostal, commune);
          if (previousCommune != null) {
            // System.out.println("Doublon = " + previousCommune);
          } else {
            preparedStatement.setString(1, codePostal);
            preparedStatement.setString(2, nom);
            preparedStatement.addBatch();
          }

          line = reader.readLine();
        }

        int[] counts = preparedStatement.executeBatch();
        int count = Arrays.stream(counts).sum();

        System.out.println("Nombre de communes créées = " + count);
        System.out.println("Done!");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
