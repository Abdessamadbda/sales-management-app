import { DatePipe, registerLocaleData } from '@angular/common';
import { Component, LOCALE_ID, NgModule } from '@angular/core';
import localeFr from '@angular/common/locales/fr';
import { SalesService } from './sales.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {AuthenticationService} from '../login/authentication.service'
import { Profile1Component } from '../profile1/profile1.component';
import { AuthService } from '../login/auth.service';
import { Sale } from './Sale.component';
import { Router } from '@angular/router';
import * as FileSaver from 'file-saver';
import { Recharge } from '../update/Recharge.component';
import { Tpe } from '../update/Tpe.component';



@Component({
  selector: 'app-declaration',
  templateUrl: './declaration.component.html',
  styleUrls: ['./declaration.component.css'],
  providers: [
    DatePipe,
    { provide: LOCALE_ID, useValue: 'fr' } 
  ]
})



export class DeclarationComponent {
  
  recharge:number | null = null;
    tpe:number | null = null;
  formSubmitted:boolean=false;
  

  formNonSubmitted:boolean=false;
  feedbackTimeout:any;
saved:boolean=false;
nom_complet:string='';
  model: any = {
    date:'',
    nom: '',
    type:'',
    quantite: 0 || null, 
    prix: 0.0 || null, 
    nomComplet:'',
    ville:'',
    telephone:'',
    imei:'',
    card:'',
    depart1: 0.0 || null,
    dealer1: 0.0 || null,
    transfert1: 0.0 || null,
    reste1: 0.0 || null,
    depart2: 0.0 || null,
    dealer2: 0.0 || null,
    transfert2: 0.0 || null,
    reste2: 0.0 || null,
    activation:0.0 || null,
    

  };
  typeOptions: { value: string; label: string }[] = [] ;
  currentDate: Date;
  validation:boolean=false;
  validation1:boolean=false;
  sellerId:number=1
  showNumericValidationMessage1: boolean=false; 
  showNumericValidationMessage2: boolean=false;  
  showNumericValidationMessage3: boolean=false;  
  showNumericValidationMessage4: boolean=false;  
  showNumericValidationMessage5: boolean=false;  
  showNumericValidationMessage6: boolean=false;  
  showNumericValidationMessage7: boolean=false;  
  showNumericValidationMessage8: boolean=false;  
  showNumericValidationMessage9: boolean=false;  
  showNumericValidationMessage10: boolean=false;  
  showNumericValidationMessage11: boolean=false;  
  showNumericValidationMessage12: boolean=false;  

  count:number=0;
  lastRechargeDate: boolean =false;
  lastTpeDate: boolean =false;
  currentInputId: string='';
  recharges: any[]=[];
  tpes: any[]=[];
  showLengthValidationMessage: boolean=false;


  constructor(private salesService: SalesService,private datePipe: DatePipe,private http: HttpClient,private authService:AuthenticationService,private authenticationService: AuthService,private router:Router) {
    this.currentDate = new Date();

registerLocaleData(localeFr, 'fr');  }


onSubmit() {
  this.calculateRecharge();
  this.calculateTpe();
  const formattedDate = this.datePipe.transform(this.currentDate, 'yyyy-MM-dd');
  const sale = {
    date:formattedDate,
    nom: this.model.nom,
    type: this.model.type,
    quantite: +this.model.quantite, 
    prix: +this.model.prix, 
    nomComplet:this.authenticationService.getNomComplet(),
    ville:this.authenticationService.getVille(),
    telephone:this.model.telephone,
    imei:this.model.imei,
    card:this.model.card,
    recharge:this.recharge,
    tpe:this.tpe,
        sellerId: this.authenticationService.getId(),
  };
  const recharge = {
    date:formattedDate,
    depart:+this.model.depart1,
    dealer:+this.model.dealer1,
    transfert:+this.model.transfert1,
    reste:+this.model.reste1,
    result:this.recharge,
    sellerId: this.authenticationService.getId(),
  }
  const tpe = {
    date:formattedDate,
    depart:+this.model.depart2,
    dealer:+this.model.dealer2,
    transfert:+this.model.transfert2,
    reste:+this.model.reste2,
    activation:+this.model.activation,
    result:this.tpe,
    sellerId: this.authenticationService.getId(),
  }
  const token=this.authService.getToken();
  const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + this.authService.getToken(), 
  });

  const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.getItem('token')
    })
  };
if(!this.validation || !this.validation1){
  if(sale.nom!="recharge" && sale.nom!="tpe"){
  this.http.post('http://localhost:8080/seller/declaration', sale).subscribe(
    (response) => {
      this.formSubmitted=true
      this.clearForm();
      this.setFeedbackTimeout(3000)
    },
    (error) => {
      this.formNonSubmitted=true
      this.setFeedbackTimeout(3000)

    }
  );
  }
  else if (sale.nom == 'recharge') {
    this.http.get<any[]>(`http://localhost:8080/recharge/${sale.sellerId}`).subscribe(
      (data) => {
        this.recharges = data; 
        let lastRechargeDate = false;
        for (const recharge of this.recharges) {
          if (recharge.date == formattedDate) {
            lastRechargeDate=true;
            break;
          }
        }
        if (lastRechargeDate==false) {
          this.http.post('http://localhost:8080/seller/recharge', recharge).subscribe(
            (response) => {
              this.formSubmitted = true;
              this.clearForm();
              this.setFeedbackTimeout(3000);
            },
            (error) => {
              this.formNonSubmitted = true;
              this.setFeedbackTimeout(3000);
            }
          );
        } else {
          alert("Une déclaration de recharge a déjà été effectuée aujourd'hui");
          this.clearForm();
        }
      },
      
      (error) => {
        console.error('Erreur lors de la récupération des données :', error);
      }
    );

      
      
  }
  else if(sale.nom=='tpe'){
    this.http.get<any[]>(`http://localhost:8080/tpe/${sale.sellerId}`).subscribe(
      (data) => {
        this.tpes = data; 
        let lastTpeDate = false;

        for (const tpe of this.tpes) {
          if (tpe.date == formattedDate) {
            lastTpeDate=true
          }
        }
      if (lastTpeDate==false) {
  
      this.http.post('http://localhost:8080/seller/tpe', tpe).subscribe(
        (response) => {
          this.formSubmitted=true
          this.clearForm();
          this.setFeedbackTimeout(3000)
        },
        (error) => {
          this.formNonSubmitted=true
          this.setFeedbackTimeout(3000)
    
        }
      );
    }
  else{
    alert("Une déclaration de tpe a déjà été effectuée aujourd'hui");
        this.clearForm();
  }
      },
      (error) => {
        console.error('Erreur lors de la récupération des données :', error);
      }
    );

      
  }
}


}
areDatesEqual(date1: Date, date2: Date): boolean {
  return (
    date1.getFullYear() === date2.getFullYear() &&
    date1.getMonth() === date2.getMonth() &&
    date1.getDate() === date2.getDate()
  );
}
clearForm() {
  this.model = {
    nom: '',
    quantite: '',
    prix: '',
    type:'',
    telephone:'',
    imei:'',
    card:'',
    depart1: 0,
    dealer1: 0,
    transfert1: 0,
    reste1: 0,
    depart2: 0,
    dealer2: 0,
    transfert2: 0,
    reste2: 0,
    activation:0,

  };
}
setFeedbackTimeout(duration: number) {
  clearTimeout(this.feedbackTimeout); 
  this.feedbackTimeout = setTimeout(() => {
    this.formSubmitted = false;
    this.formNonSubmitted = false;
    this.validation=false;

  }, duration);
}
isValid(): boolean {

  return (
    this.model.nom.trim() !== '' &&
    this.model.quantite.trim() !== '' &&
    this.model.prix.trim() !== '' 
  );
}
update(){
  this.router.navigate(['/seller/update'])

}
updateTypeOptions() {
  this.typeOptions = [];
  switch (this.model.nom) {
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
shouldDisplayPhoneNumber(): boolean {
  const productTypesWithPhoneNumber = ['FORFAIT VOIX', 'SIM','FORFAIT DATA','forfait fixe','FORFAIT ESE  VOIX','FORFAIT ESE DATA','forfait ESE FIXE'];

  return productTypesWithPhoneNumber.includes(this.model.nom);
}
shouldDisplayIMEI(): boolean {
  const productTypesWithPhoneNumber = ['GSM', 'TERMINAL DATA'];

  return productTypesWithPhoneNumber.includes(this.model.nom);
}
shouldDisplayCard(): boolean {
  const productTypesWithPhoneNumber = ['FORFAIT DATA'];

  return productTypesWithPhoneNumber.includes(this.model.nom);
}
shouldDisplayRecharge(): boolean {
  const productTypesWithPhoneNumber = ['recharge'];
  return productTypesWithPhoneNumber.includes(this.model.nom);
}
shouldDisplayTpe(): boolean {
  const productTypesWithPhoneNumber = ['tpe'];
  return productTypesWithPhoneNumber.includes(this.model.nom);
}
shouldDisplayTpeAndRecharge(): boolean{
  return !this.shouldDisplayRecharge() &&
  !this.shouldDisplayTpe();
}
generateReport(): void {
  this.nom_complet = this.authenticationService.getNomComplet();
  this.sellerId = this.authenticationService.getId();

  const currentDate = new Date();
  const formattedDate = currentDate.toISOString().split('T')[0]; 
  this.salesService.generateSalesReport(this.sellerId).subscribe(
    (reportBlob: Blob) => {
      const fileName = `${formattedDate}_${this.nom_complet}_etat_de_vente.pdf`; 
      FileSaver.saveAs(reportBlob, fileName);
    },
    (error) => {
      console.error('Error generating report:', error);
    }
  );
}
calculateRecharge(): void {
  const soldeDepart = this.model.depart1 || 0;
  const alimentationDealer = this.model.dealer1 || 0;
  const transfert = this.model.transfert1 || 0;
  const reste = this.model.reste1 || 0;

  if (soldeDepart !== 0 || alimentationDealer !== 0 || transfert !== 0 || reste !== 0) {
    this.recharge = soldeDepart + alimentationDealer - transfert - reste;
  } else {
    this.recharge = null;
  }
}

calculateTpe(): void {
  const soldeDepart = this.model.depart2 || 0;
  const alimentationDealer = this.model.dealer2 || 0;
  const activation = this.model.activation || 0;
  const transfert = this.model.transfert2 || 0;
  const reste = this.model.reste2 || 0;

  if (soldeDepart !== 0 || alimentationDealer !== 0 || activation !== 0 || transfert !== 0 || reste !== 0) {
    this.tpe = soldeDepart + alimentationDealer - activation - transfert - reste;
  } else {
    this.tpe = null;
  }
}
isValidFloat(value: any): boolean {
  return typeof value === 'number' && !isNaN(value) ;
}

areModelValuesValidFloats(): boolean {
  return (
    this.isValidFloat(this.model.depart1) &&
    this.isValidFloat(this.model.dealer1) &&
    this.isValidFloat(this.model.transfert1) &&
    this.isValidFloat(this.model.reste1)
  );
}
areModelValuesValidFloats1(): boolean {
  return (
    this.isValidFloat(this.model.depart2) &&
    this.isValidFloat(this.model.dealer2) &&
    this.isValidFloat(this.model.transfert2) &&
    this.isValidFloat(this.model.reste2) &&
    this.isValidFloat(this.model.activation)
  );
}

updateModelValues1(): void {
  if(this.shouldDisplayRecharge()){
  if (!this.areModelValuesValidFloats()) {
    this.validation=true
    this.setFeedbackTimeout(3000)
    this.formSubmitted=false
  }
  }
}
updateModelValues2(): void {
  if(this.shouldDisplayTpe()){
if (!this.areModelValuesValidFloats1()) {
  this.validation1=true
  this.setFeedbackTimeout(3000)
  this.formSubmitted=false
}
  }
}
updateModelValues(): void {
  this.updateModelValues1();
  this.updateModelValues2()
}

isNumericInputAllowed() {
  return !isNaN(Number(this.model.quantite)) || !isNaN(Number(this.model.telephone)) || !isNaN(Number(this.model.prix)) || !isNaN(Number(this.model.prix)) || !isNaN(Number(this.model.depart1)) || !isNaN(Number(this.model.dealer1)) || !isNaN(Number(this.model.transfert)) || !isNaN(Number(this.model.reste1)) || !isNaN(Number(this.model.depart2)) || !isNaN(Number(this.model.dealer2)) || !isNaN(Number(this.model.transfert2)) || !isNaN(Number(this.model.reste2)) || !isNaN(Number(this.model.activation)) ;
}

validatePhoneNumberLength(phone: string): boolean {
  return phone.length === 10;
}
validateNumericInput(event: any, inputId: string) {
  if (this.isNumericInputAllowed()) {   
    if (!(event.key.match(/^[0-9]$/) || event.key === 'Backspace' || event.key === ',' || event.key === '.' || event.key === 'Delete' || event.key === 'ArrowLeft' || event.key === 'ArrowRight')) {
      event.preventDefault();
      if(inputId==='input1'){
        this.showNumericValidationMessage1 = true;
      }
      else if(inputId==='input2'){
        this.showNumericValidationMessage2 = true;
      }
      else if(inputId==='input3'){
        this.showNumericValidationMessage3 = true;
      }
      else if(inputId==='input4'){
        this.showNumericValidationMessage4 = true;
      }
      else if(inputId==='input5'){
        this.showNumericValidationMessage5 = true;
      }
      else if (inputId==='input6'){
        this.showNumericValidationMessage6 = true;
      }
      else if(inputId==='input7'){
        this.showNumericValidationMessage7 = true;
      }
      else if(inputId==='input8'){
        this.showNumericValidationMessage8 = true;
      }
      else if(inputId==='input9'){
        this.showNumericValidationMessage9 = true;
      }
      else if(inputId==='input10'){
        this.showNumericValidationMessage10 = true;
      }
      else if(inputId==='input11'){
        this.showNumericValidationMessage11 = true;
      }
      else if(inputId==='input12'){
        this.showNumericValidationMessage12 = true;
      }
        
    } 
    else{
      this.showNumericValidationMessage1 = false;
      this.showNumericValidationMessage2 = false;
      this.showNumericValidationMessage3 = false;
      this.showNumericValidationMessage4 = false;this.showNumericValidationMessage9 = false;
      this.showNumericValidationMessage5 = false;
      this.showNumericValidationMessage6 = false;
      this.showNumericValidationMessage7 = false;
      this.showNumericValidationMessage8 = false;
      this.showNumericValidationMessage10 = false;
      this.showNumericValidationMessage11 = false;
      this.showNumericValidationMessage12 = false;
    }  
    if (this.validatePhoneNumberLength((this.model.telephone).toString())) {
      if (!(  event.key === 'Backspace'   || event.key === 'Delete' || event.key === 'ArrowLeft' || event.key === 'ArrowRight' ) ) {
        event.preventDefault();
      }
      this.showLengthValidationMessage = true; 
    } else {
      this.showLengthValidationMessage = false; 
    }
  }
}



}
