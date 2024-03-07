package dev.xgeorget.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dev.xgeorget.jdbc.model.Commune;
import dev.xgeorget.jdbc.model.Maire;

public class PlayWithMaire {
  public static void main(String[] args) {
    Map<String, Commune> communes = new HashMap<>();

    Map<String, Maire> maires = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try (Connection connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/tp_jdbc_villes",
        "XgtJava",
        "j5Riu7!o,DAegocdeHhE");) {

      String sqlMaire = "INSERT INTO maire (firstname, lastname, civility, birthday) VALUES (?, ?, ?, ?)";
      String sqlCommune = "INSERT INTO commune (zip_code, name, maire_id) VALUES (?, ?, ?)";

      PreparedStatement preparedStatementMaire = connection.prepareStatement(sqlMaire);
      PreparedStatement preparedStatementCommune = connection.prepareStatement(sqlCommune);

      Path path = Path.of("data/maires-25-04-2014.csv");

      try (BufferedReader reader = Files.newBufferedReader(path);) {
        String line = reader.readLine();
        line = reader.readLine();

        int maireId = 1;
        while (line != null) {
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

          String zipCode = codeDepartement + codeInsee;
          String cityName = splitLine[3];

          String lastName = splitLine[5];
          String firstName = splitLine[6];
          String civility = splitLine[7];

          LocalDate birthDay = LocalDate.parse(splitLine[8], formatter);

          Maire maire = new Maire(firstName, lastName, civility, birthDay);

          Maire previousMaire = maires.put(lastName + " " + firstName + "-" + birthDay.toString(), maire);

          Commune commune = new Commune(zipCode, cityName, maireId);

          Commune previousCommune = communes.put(zipCode, commune);

          // if (!civility.equals("M") && !civility.equals("Mme")) {
          // System.out.println("No civility = " + civility);
          // }

          if (previousMaire != null | previousCommune != null) {
            System.out.println("Doublon maire = " + previousMaire);
            System.out.println("Doublon commune = " + previousCommune);
          } else {

            // System.out.println("Maire = " + maire);

            preparedStatementMaire.setString(1, maire.getFirstName());
            preparedStatementMaire.setString(2, maire.getLastName());
            preparedStatementMaire.setString(3, maire.getCivility());
            preparedStatementMaire.setString(4, maire.getBirthDay().toString());
            preparedStatementMaire.addBatch();

            preparedStatementCommune.setString(1, commune.getZipCode());
            preparedStatementCommune.setString(2, commune.getName());
            preparedStatementCommune.setInt(3, commune.getMaireId());
            preparedStatementCommune.addBatch();

            maireId++;
          }

          line = reader.readLine();

        }

        int[] countsMaire = preparedStatementMaire.executeBatch();
        int countMaire = Arrays.stream(countsMaire).sum();

        int[] countsCommune = preparedStatementCommune.executeBatch();
        int countCommune = Arrays.stream(countsCommune).sum();

        System.out.println("Nombre de maire créés = " + countMaire);
        System.out.println("Nombre de commune créées = " + countCommune);
        System.out.println("Done!");

      } catch (IOException e) {
        e.printStackTrace();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
