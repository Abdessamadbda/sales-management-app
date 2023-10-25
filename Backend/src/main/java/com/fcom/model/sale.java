package com.fcom.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales", schema = "fcomuser")
public class sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String date;
	String nom;
	String type;
	Long quantite;
	Float prix;
	String telephone;
	String imei;
	String card;
	Float recharge;
	Float tpe;
	@Column(name = "seller_id")
	private Long sellerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id", referencedColumnName = "id", insertable = false, updatable = false)

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getQuantite() {
		return quantite;
	}

	public void setQuantite(Long quantite) {
		this.quantite = quantite;
	}

	public Float getPrix() {
		return prix;
	}

	public void setPrix(Float prix) {
		this.prix = prix;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public Float getRecharge() {
		return recharge;
	}

	public void setRecharge(Float recharge) {
		this.recharge = recharge;
	}

	public Float getTpe() {
		return tpe;
	}

	public void setTpe(Float tpe) {
		this.tpe = tpe;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public sale(Long id, String date, String nom, String type, Long quantite, Float prix, String telephone, String imei,
			String card, Float recharge, Float tpe, Long sellerId, user seller) {
		super();
		this.id = id;
		this.date = date;
		this.nom = nom;
		this.type = type;
		this.quantite = quantite;
		this.prix = prix;
		this.telephone = telephone;
		this.imei = imei;
		this.card = card;
		this.recharge = recharge;
		this.tpe = tpe;
		this.sellerId = sellerId;
	}

	public sale() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "sale [id=" + id + ", date=" + date + ", nom=" + nom + ", type=" + type + ", quantite=" + quantite
				+ ", prix=" + prix + ", telephone=" + telephone + ", imei=" + imei + ", card=" + card + ", recharge="
				+ recharge + ", tpe=" + tpe + ", sellerId=" + sellerId + "]";
	}

}
