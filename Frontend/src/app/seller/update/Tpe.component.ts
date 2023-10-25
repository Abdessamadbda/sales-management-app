export class Tpe {
    id: number;
    date:string;
    depart:number;
    dealer:number;
    transfert:number;
    reste:number;
    activation:number;
    result:number;
    sellerId:number

    

  
    constructor(id: number,date:string, depart: number, dealer: number,transfert: number, reste: number,activation:number, result: number,sellerId:number) {
      this.id = id;
      this.date = date;
      this.depart = depart;
      this.dealer=dealer;
      this.transfert = transfert;
      this.reste = reste;
      this.result=result;
      this.sellerId= sellerId;
      this.activation=activation;
    }
  }
  