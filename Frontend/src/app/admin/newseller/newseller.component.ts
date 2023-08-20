import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-newseller',
  templateUrl: './newseller.component.html',
  styleUrls: ['./newseller.component.css']
})
export class NewsellerComponent {
  formSubmitted: boolean=false ;
  formNonSubmitted: boolean=false ;
  feedbackTimeout: any;
  showPassword: boolean = false;
  model: any = {
    nom_complet: '',
    ville: '',
    username: '',
    password: '',
    phone:''
  };

  constructor(private http: HttpClient) {}
  


  handleSubmit() {

    if (this.isValid()) {
      const user = {
        nom_complet: this.model.nom_complet,
        ville: this.model.ville,
        username: this.model.username,
        password: this.model.password,
        phone: this.model.phone,
      };

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

  isValid(): boolean {

    return (
      this.model.nom_complet.trim() !== '' &&
      this.model.ville.trim() !== '' &&
      this.model.username.trim() !== '' &&
      this.model.password.trim() !== '' &&
      this.model.phone.trim() !== ''
    );
  }
  clearForm() {
    this.model = {
      nom_complet: '',
      ville: '',
      username: '',
      password: '',
      phone: '',
    };
  }
  setFeedbackTimeout(duration: number) {
    clearTimeout(this.feedbackTimeout); 
    this.feedbackTimeout = setTimeout(() => {
      this.formSubmitted = false;
      this.formNonSubmitted = false;

    }, duration);
  }
  scrollToBottom() {
    window.scrollTo(0, document.body.scrollHeight);
  }
  show(){
    this.showPassword=!this.showPassword;
  }
}
