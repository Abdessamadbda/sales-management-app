export class Sale {
    id: number;
    date: string;
    nom: string;
    type:string;
    quantite: number;
    prix: number;
    nomComplet: string;
    ville:string;
    telephone:string;
    imei:string;
    card:string;
    recharge:number;
    tpe:number;
    sellerId:number;
    

  
    constructor(id: number, date: string, nom: string,type: string, quantite: number, prix: number, nom_complet: string,ville:string,telephone:string,imei:string,card:string,recharge:number,tpe:number,sellerId:number) {
      this.id = id;
      this.date = date;
      this.nom = nom;
      this.type = type;
      this.quantite = quantite;
      this.prix = prix;
      this.nomComplet = nom_complet;
      this.ville=ville;
      this.telephone=telephone;
      this.imei=imei;
      this.card=card;
      this.recharge=recharge;
      this.tpe=tpe;
      this.sellerId=sellerId;
      
    }
  }
  