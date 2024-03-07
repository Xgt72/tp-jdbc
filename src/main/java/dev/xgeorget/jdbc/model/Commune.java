package dev.xgeorget.jdbc.model;

public class Commune {

  private String zipCode;
  private String name;
  private int maireId;

  public Commune(String zipCode, String name, int maireId) {
    this.zipCode = zipCode;
    this.name = name;
    this.maireId = maireId;
  }

  public Commune(String zipCode, String name) {
    this.zipCode = zipCode;
    this.name = name;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMaireId() {
    return maireId;
  }

  public void setMaireId(int maireId) {
    this.maireId = maireId;
  }

  @Override
  public String toString() {
    return "Commune [zipCode=" + zipCode + ", name=" + name + ", maireId=" + maireId + "]";
  }
}
