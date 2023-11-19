import { HttpClient } from '@angular/common/http';
import { Component, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-newseller',
  templateUrl: './newseller.component.html',
  styleUrls: ['./newseller.component.css']
})

export class NewsellerComponent {
  usernameExistant: boolean=false ;
  formSubmitted: boolean=false ;
  formNonSubmitted: boolean=false ;
  feedbackTimeout: any;
  showPassword: boolean = false;
  model: any = {
    nom_complet: '',
    ville: '',
    agence:'',
    username: '',
    password: '',
    phone:''
  };
  showNumericValidationMessage: boolean=false;
  showLengthValidationMessage: boolean=false;

  constructor(private http: HttpClient, private router:Router) {}
  


  handleSubmit() {

    if (this.isValid()) {
      const user = {
        nom_complet: this.model.nom_complet,
        ville: this.model.ville,
        username: this.model.username,
        password: this.model.password,
        phone: this.model.phone,
        agence: this.model.agence,

      };
      this.http.get<any[]>('http://localhost:8080/admin/sellers').subscribe(
        (data) => {
          for(let user1 of data){
            if(user1.username === this.model.username){
              this.usernameExistant=true;
              this.setFeedbackTimeout(4000);
              setTimeout(() => {
                this.scrollToBottom();
              }, 200); 
            }
          }
          
        },
        (error) => {
          console.error('Error fetching users data:', error);
        }
      );
      if(this.usernameExistant){
        this.http.post('http://localhost:8080/user/save', user).subscribe(
  (response) => {

    this.formSubmitted = true;
    this.clearForm();
    this.setFeedbackTimeout(4000);
    setTimeout(() => {
      this.scrollToBottom();
    }, 200); 
  },
  (error) => {
    this.formNonSubmitted = true;
    this.setFeedbackTimeout(4000);
    setTimeout(() => {
      this.scrollToBottom();
    }, 200); 
  }
);
      }
    }
    }
  

  isValid(): boolean {

    return (
      this.model.nom_complet.trim() !== '' &&
      this.model.ville.trim() !== '' &&
      this.model.agence.trim() !== '' &&
      this.model.username.trim() !== '' &&
      this.model.password.trim() !== '' &&
      this.model.phone.trim() !== ''
    );
  }
  clearForm() {
    this.model = {
      nom_complet: '',
      ville: '',
      agence: '',
      username: '',
      password: '',
      phone: '',
    };
  }
  setFeedbackTimeout(duration: number) {
    clearTimeout(this.feedbackTimeout); 
    this.feedbackTimeout = setTimeout(() => {
      this.formSubmitted = false;
      this.usernameExistant=false;
      this.formNonSubmitted = false;

    }, duration);
  }
  scrollToBottom() {
    window.scrollTo(0, document.body.scrollHeight);
  }
  show(){
    this.showPassword=!this.showPassword;
  }
  isNumericInputAllowed() {
    return !isNaN(Number(this.model.phone))  ;
  }
  
  validateNumericInput(event: any) {
    if (this.isNumericInputAllowed()) {
      if (!(event.key.match(/^[0-9]$/) || event.key === 'Backspace' || event.key === ',' || event.key === '.' || event.key === 'Delete' || event.key === 'ArrowLeft' || event.key === 'ArrowRight' ) ) {
        event.preventDefault();
        this.showNumericValidationMessage = true; 
      } else {
        this.showNumericValidationMessage = false; 
      }
      if (this.validatePhoneNumberLength((this.model.phone).toString())) {
        if (!(  event.key === 'Backspace'   || event.key === 'Delete' || event.key === 'ArrowLeft' || event.key === 'ArrowRight' ) ) {
          event.preventDefault();
        }
        this.showLengthValidationMessage = true; 
      } else {
        this.showLengthValidationMessage = false; 
      }
    }
    
    
  }
  validatePhoneNumberLength(phone: string): boolean {
    return phone.length === 10;
  }
  sellersList(){
    this.router.navigate(['/admin/sellersList']);
  }
}
