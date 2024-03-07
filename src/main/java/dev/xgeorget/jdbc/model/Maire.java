package dev.xgeorget.jdbc.model;

import java.time.LocalDate;

public class Maire {

  private String firstName;
  private String lastName;
  private String civility;
  private LocalDate birthDay;

  public Maire(String firstName, String lastName, String civility, LocalDate birthDay) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.civility = this.getCivilityFromString(civility);
    this.birthDay = birthDay;
  }

  private String getCivilityFromString(String civility) {
    if (civility.equals("M") | civility.equals("Mr")) {
      return Civilities.MR;
    } else if (civility.equals("Mme")) {
      return Civilities.MME;
    }
    return Civilities.MLLE;

  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCivility() {
    return civility;
  }

  public void setCivility(String civility) {
    this.civility = civility;
  }

  public LocalDate getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(LocalDate birthDay) {
    this.birthDay = birthDay;
  }

  @Override
  public String toString() {
    return "Maire [firstName=" + firstName + ", lastName=" + lastName + ", civility=" + civility + ", birthDay="
        + birthDay + "]";
  }
}

/**
 * Civilities
 */
interface Civilities {
  public static final String MR = "Mr";
  public static final String MME = "Mme";
  public static final String MLLE = "Mlle";
}