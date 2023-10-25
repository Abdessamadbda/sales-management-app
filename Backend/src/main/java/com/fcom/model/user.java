package com.fcom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "fcomuser")
public class user {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String username;
	String password;
	String nom_complet;
	String ville;
	String phone;
	String agence;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public user() {
		super();
	}

	public user(Long id, String username, String password, String nom_complet, String ville, String agence,
			String phone) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nom_complet = nom_complet;
		this.ville = ville;
		this.agence = agence;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "user [id=" + id + ", username=" + username + ", password=" + password + ", nom_complet=" + nom_complet
				+ ", ville=" + ville + ", agence=" + agence + ", phone=" + phone + "]";
	}

	public Object getRoles() {
		return null;
	}

}
