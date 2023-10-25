package com.fcom.model;

public class UserDTO {
    private Long id;
    private String username;
    private String nom_complet;
    private String ville;
    private String phone;
    private String agence;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String nom_complet, String ville, String phone, String agence) {
        this.id = id;
        this.username = username;
        this.nom_complet = nom_complet;
        this.ville = ville;
        this.phone = phone;
        this.agence = agence;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNom_complet() {
        return nom_complet;
    }

    public void setNom_complet(String nom_complet) {
        this.nom_complet = nom_complet;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
