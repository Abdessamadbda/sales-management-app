package com.fcom.model;

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
@Table(name = "recharge", schema = "fcomuser")
public class recharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String date;
    Float depart;
    Float dealer;
    Float transfert;
    Float reste;
    Float result;
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

    public Float getDepart() {
        return depart;
    }

    public void setDepart(Float depart) {
        this.depart = depart;
    }

    public Float getDealer() {
        return dealer;
    }

    public void setDealer(Float dealer) {
        this.dealer = dealer;
    }

    public Float getTransfert() {
        return transfert;
    }

    public void setTransfert(Float transfert) {
        this.transfert = transfert;
    }

    public Float getReste() {
        return reste;
    }

    public void setReste(Float reste) {
        this.reste = reste;
    }

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public recharge() {
        super();
    }

    public recharge(Long id, Float depart, String date, Float dealer, Float transfert, Float reste, Float result,
            Long sellerId) {
        super();
        this.id = id;
        this.depart = depart;
        this.dealer = dealer;
        this.transfert = transfert;
        this.reste = reste;
        this.result = result;
        this.date = date;
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "user [id=" + id + ", depart=" + depart + ", dealer=" + dealer + ", transfert=" + transfert
                + ", reste=" + reste + ", result=" + result + ", date=" + date + ", sellerId=" + sellerId + "]";
    }

}