export class Sale {
    id: number;
    date: string;
    nom: string;
    type:string;
    quantite: number;
    prix: number;
    telephone:string;
    imei:string;
    card:string
    recharge:number;
    tpe:number
    sellerId:number

    
    // Add the updatedQuantite and updatedPrix properties

  
    constructor(id: number, date: string, nom: string,type:string, quantite: number, prix: number, telephone:string,imei:string,card:string,    sellerId:number, recharge:number,tpe:number
      ) {
      this.id = id;
      this.date = date;
      this.nom = nom;
      this.type=type;
      this.quantite = quantite;
      this.prix = prix;
      this.telephone=telephone;
      this.imei=imei;
      this.card=card;
      this.sellerId= sellerId;
      this.recharge=recharge
this.tpe=tpe

      // Initialize the updatedQuantite and updatedPrix properties
      
    }
  }
  