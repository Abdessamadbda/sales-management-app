export class Seller {
    id: number;
    nom_complet: string;
    ville: string;
    agence:string;
    phone: string;
    username: string;
    password:string;
    

  
    constructor(id: number, nom_complet: string, ville: string,agence:string, phone: string, username: string, password:string) {
      this.id = id;
      this.nom_complet = nom_complet;
      this.ville = ville;
      this.agence=agence;
      this.phone = phone;
      this.username = username;
      this.password=password;
      
    }
  }
  