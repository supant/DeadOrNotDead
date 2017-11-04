package com.example.antoine.deadornotdead;

/**
 * Created by Antoine on 04/11/2017.
 */

public class Personne {

    private String dateNaissance;
    private String lieuNaissance;
    private int age;
    private String dateDeces;
    private String lieuDeces;
    private String nom;
    private String photo;
    private boolean isMort;

    public Personne(String nom) {
        this.nom=nom;
        this.isMort=false;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "dateNaissance='" + dateNaissance + '\'' +
                ", lieuNaissance='" + lieuNaissance + '\'' +
                ", age='" + age + '\'' +
                ", dateDeces='" + dateDeces + '\'' +
                ", lieuDeces='" + lieuDeces + '\'' +
                ", nom='" + nom + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public String getLieuDeces() {
        return lieuDeces;
    }

    public void setLieuDeces(String lieuDeces) {
        this.lieuDeces = lieuDeces;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isMort() {
        return isMort;
    }

    public void setMort(boolean mort) {
        isMort = mort;
    }

    public void calculAge() {
        try {
            age = Integer.parseInt(dateDeces.split(" ")[2]) -  Integer.parseInt(dateNaissance.split(" ")[2]);
        } catch (Exception e) {
            age=-1;
        }
    }
}
