import { Component } from '@angular/core';
import { Sale } from './Sale.component';
import { SalesService } from './sales.service';
import { AuthService } from '../login/auth.service';


@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css']
})
export class UpdateComponent {
  salesForToday: Sale[] = [];
  model: any[] = [];
  sellerId:number=0;
  updatedNum:string='';
  updatedNom:string='';

  typeOptions: { value: string; label: string }[] = [] ;

  constructor(private salesService: SalesService,private authenticationService: AuthService){}
  ngOnInit(): void {
    this.fetchSalesForToday();
  }
  
  fetchSalesForToday(): void {
    this.sellerId=this.authenticationService.getId(),
    this.salesService.getSalesForToday(this.sellerId).subscribe(
      (sales: Sale[]) => {
        this.salesForToday = sales;
        this.model = this.salesForToday.map(sale => ({
        updatedNom: '',
        updatedType: '',
        updatedQuantite: sale.quantite,
        updatedPrix: sale.prix,
        updatedNum:sale.telephone,
        updatedImei:sale.imei,
        updatedCard:sale.card,
      }));
      },
      (error) => {
        console.error('Error fetching sales for today:', error);
      }
    );
  }
  
  updateSale(sale: Sale, index: number): void {
    if(!this.shouldDisplayPhoneNumber1(this.model[index].updatedNom)){
      this.model[index].updatedNum=''
    }
    if(!this.shouldDisplayIMEI1(this.model[index].updatedNom)){
      this.model[index].updatedImei=''
    }
    if(!this.shouldDisplayCard1(this.model[index].updatedNom)){
      this.model[index].updatedCard=''
    }
    const updatedNom = this.model[index].updatedNom;
    const updatedType = this.model[index].updatedType;
  const updatedQuantite = this.model[index].updatedQuantite;
  const updatedPrix = this.model[index].updatedPrix;
  const updatedNum = this.model[index].updatedNum;
  const updatedImei = this.model[index].updatedImei;
  const updatedCard = this.model[index].updatedCard;
  
    const updatedSale: Sale = {
      id: sale.id,
      date: sale.date,
      nom: updatedNom,
      type:updatedType,
      quantite: updatedQuantite, 
      prix:updatedPrix,   
      telephone: updatedNum,
      imei:updatedImei,
      card:updatedCard,
      recharge:0,
      tpe:0,
      
      sellerId: sale.sellerId
    };
  
    this.salesService.updateSale(updatedSale).subscribe(
      (response: Sale) => {
        const index = this.salesForToday.findIndex(s => s.id === response.id);
        if (index !== -1) {
          this.salesForToday[index] = response;
        }
        alert("modifié")
      },
      (error) => {
        alert("error")
      }
    );
  }
  clearFields(): void {
    this.model.forEach(item => {
      item.updatedNom = '';
      item.updatedType = '';
      item.updatedQuantite = 0;
      item.updatedPrix = 0;
      item.updatedNum='';
      item.updatedImei='';
      item.updatedCard=''
    });
  }
  updateTypeOptions(i:number) {
    this.typeOptions = [];
    switch (this.model[i].updatedNom) {
      case 'FORFAIT VOIX':
        this.typeOptions.push(
          { value: "Forfait Yo 11GO+1H 49DH", label: "Forfait Yo 11GO+1H 49DH" },
          { value: 'Forfait YO RS 49 DH', label: 'Forfait YO RS 49 DH' },
          { value: 'FORFAIT 3H+3GO EC', label: 'FORFAIT 3H+3GO EC' },
          { value: 'FORFAIT 11H+11GO EC', label: 'FORFAIT 11H+11GO EC' },
          { value: 'Forfait Yo RS 99DH', label: 'Forfait Yo RS 99DH' },
          { value: 'FORFAIT YO RS 149 DH', label: 'FORFAIT YO RS 149 DH' },
          { value: 'FORFAIT YO RS 149 DH', label: 'FORFAIT YO RS 149 DH' },
          { value: 'FORFAIT ILLIMITE PLAF EC', label: 'FORFAIT ILLIMITE PLAF EC' },
          { value: 'FORFAIT YO RS 199 DH', label: 'FORFAIT YO RS 199 DH' },
          { value: 'autre', label: 'autre' },
        );
        break;
      case 'FORFAIT DATA':
        this.typeOptions.push(
          { value: 'BOX 4G 199 SIM Only', label: 'BOX 4G 199 SIM Only' },
          { value: 'DARBOX 4G 199 SIM ONLY', label: 'DARBOX 4G 199 SIM ONLY' },
          { value: 'Darbox 4G+ 249Dh SIM Only', label: 'Darbox 4G+ 249Dh SIM Only' },
          { value: 'Darbox 4G+ 249 Dh', label: 'Darbox 4G+ 249 Dh' },
          { value: 'autre', label: 'autre' },
        );
        break;
      case 'forfait fixe':
        this.typeOptions.push(
          { value: 'FIXE PARTICULIER ADSL', label: 'FIXE PARTICULIER ADSL' },
          { value: '20 Méga Fibre', label: '20 Méga Fibre' },
          { value: '50 Méga Fibre', label: '50 Méga Fibre' },
          { value: '100 Méga Fibre', label: '100 Méga Fibre' },
          { value: '200 Méga Fibre', label: '200 Méga Fibre' },
        );
        break;
        case 'FORFAIT ESE  VOIX':
        this.typeOptions.push(
          { value: 'FF Orange Pro 6H+6Go ST', label: 'FF Orange Pro 6H+6Go ST' },
          { value: 'Forfait Pro Connect 11 ST', label: 'Forfait Pro Connect 11 ST' },
          { value: 'Forfait Orange Pro 15H+15Go ST', label: 'Forfait Orange Pro 15H+15Go ST' },
          { value: 'autre', label: 'autre' },
        );
        break;
        case 'FORFAIT ESE DATA':
        this.typeOptions.push(
          { value: 'BUSINESS BOX 4G 249', label: 'BUSINESS BOX 4G 249' },
          { value: 'SIM ONLY – BUSINESS BOX 4G+ 249', label: 'SIM ONLY – BUSINESS BOX 4G+ 249' },
          { value: 'BUSINESS BOX 4G 349', label: 'BUSINESS BOX 4G 349' },
          { value: 'SIM ONLY – BUSINESS BOX 4G+ 349', label: 'SIM ONLY – BUSINESS BOX 4G+ 349' },
        );
        break;
        case 'forfait ESE FIXE':
        this.typeOptions.push(
          { value: 'FIXE ESE ADSL', label: 'FIXE ESE ADSL' },
          { value: '20 Méga ESE Fibre', label: '20 Méga ESE Fibre' },
          { value: '50 Méga ESE Fibre', label: '50 Méga ESE Fibre' },
          { value: '100 Méga ESE Fibre', label: '100 Méga ESE Fibre' },
          { value: '200 Méga ESE Fibre', label: '200 Méga ESE Fibre' },
        );
        break;
        case 'SIM':
        this.typeOptions.push(
          { value: 'SIM PREPAYEE', label: 'SIM PREPAYEE' },
          { value: 'SIM POSTPIED', label: 'SIM POSTPIED' },
          { value: 'SIMSWAP PR', label: 'SIMSWAP PR' },
          { value: 'SIMSWAP PO', label: 'SIMSWAP PO' },
          { value: 'E-SIM PREPAYEE', label: 'E-SIM PREPAYEE' },
          { value: 'E-SIM POSTPIED', label: 'E-SIM POSTPIED' },
          { value: 'E-SIM SWAP PR', label: 'E-SIM SWAP PR' },
          { value: 'E-SIM SWAP PO', label: 'E-SIM SWAP PO' },
          { value: 'SIM PREPAYEE DAR BOX', label: 'SIM PREPAYEE DAR BOX' },
          { value: 'autre', label: 'autre' },
        );
        break;
        case 'ACCESSOIRE':
        this.typeOptions.push(
          { value: 'Type ACCESSOIRE', label: 'Type ACCESSOIRE' }
        );
        break;
        case 'GSM':
        this.typeOptions.push(
          { value: 'Type GSM', label: 'Type GSM' }
        );
        break;
        case 'TERMINAL DATA':
        this.typeOptions.push(
          { value: 'Type TERMINAL', label: 'Type TERMINAL' }
        );
        break;
        case 'SERVICE':
        this.typeOptions.push(
          { value: 'Type SERVICE', label: 'Type SERVICE' }
        );
        break;
      default :
        break;
    }
  }
  updateAndClear(sale: Sale, index: number): void {
    
    this.updateSale(sale, index);
    this.clearFields();
  }
  shouldDisplayPhoneNumber(sale: Sale,index:number): boolean {
    const updatedNom = this.model[index].updatedNom;
    if(updatedNom==''){
    const productTypesWithPhoneNumber = ['FORFAIT VOIX', 'SIM','FORFAIT DATA','forfait fixe','FORFAIT ESE  VOIX','FORFAIT ESE DATA','forfait ESE FIXE'];
    return productTypesWithPhoneNumber.includes(sale.nom);}
    else{
      return false;
    }
  }
  shouldDisplayIMEI(sale: Sale,index:number): boolean {
    const updatedNom = this.model[index].updatedNom;
    if(updatedNom==''){
    const productTypesWithImei = ['GSM', 'TERMINAL DATA'];
    return productTypesWithImei.includes(sale.nom);
  }
  else{
    return false;
  }
  }
  shouldDisplayCard(sale: Sale,index:number): boolean {
    const updatedNom = this.model[index].updatedNom;
    if(updatedNom==''){
    const productTypesWithCard = ['FORFAIT DATA'];
    return productTypesWithCard.includes(sale.nom);
  }
  else{
    return false;
  }
  }
  shouldDisplayPhoneNumber1(sale: string): boolean {
    const productTypesWithPhoneNumber = ['FORFAIT VOIX', 'SIM','FORFAIT DATA','forfait fixe','FORFAIT ESE  VOIX','FORFAIT ESE DATA','forfait ESE FIXE'];
    return productTypesWithPhoneNumber.includes(sale);
  }
  shouldDisplayIMEI1(sale: string): boolean {
    const productTypesWithImei = ['GSM', 'TERMINAL DATA'];
    return productTypesWithImei.includes(sale);
  }
  shouldDisplayCard1(sale: string): boolean {
    const productTypesWithCard = ['FORFAIT DATA'];
    return productTypesWithCard.includes(sale);
  }

 
shouldDisplayRecharge(sale: Sale): boolean {
  const productTypesWithPhoneNumber = ['recharge'];
  return productTypesWithPhoneNumber.includes(sale.nom);
}
shouldDisplayTpe(sale: Sale): boolean {
  const productTypesWithPhoneNumber = ['tpe'];
  return productTypesWithPhoneNumber.includes(sale.nom);
}
shouldDisplayTpeAndRecharge(sale: Sale): boolean{
  return !this.shouldDisplayRecharge(sale) &&
  !this.shouldDisplayTpe(sale);
}

  shouldReturnToline(sale: Sale,index:number){
    this.shouldDisplayPhoneNumber(sale,index);
    this.shouldDisplayIMEI(sale,index);
    this.shouldDisplayCard(sale,index);
  }
  removeSale(index: number): void {
    if (confirm('Voulez-vous vraiment supprimer cet élément ?')) {
      const saleToRemove = this.salesForToday[index];
      this.salesService.removeSale(saleToRemove.id).subscribe(
        () => {
          this.salesForToday.splice(index, 1);
          alert('Élément supprimé');
        },
        (error) => {
          console.error('Error removing sale:', error);
          alert('Erreur lors de la suppression');
        }
      );
    }
  }
}
