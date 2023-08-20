package com.fcom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "admin", schema = "fcomuser")

public class admin {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	String username;
	String password;
	String nom_complet;
	
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
	public admin(Long id, String username, String password, String nom_complet) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nom_complet = nom_complet;

	}
	public admin() {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nom_complet = nom_complet;
	}
	

	
	

}
