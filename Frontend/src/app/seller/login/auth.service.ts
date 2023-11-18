import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly NOM_COMPLET_KEY = 'nomComplet';
  private readonly VILLE_KEY = 'ville';
  private readonly PHONE_KEY = 'phone';
  private readonly AGENCE_KEY = 'agence';

  private readonly PASSWORD_KEY = 'password';
  private readonly ID_KEY = 'id';
  setNomComplet(nomComplet: string) {
    localStorage.setItem(this.NOM_COMPLET_KEY, nomComplet);
  }

  getNomComplet(): string {
    return localStorage.getItem(this.NOM_COMPLET_KEY) || '';
  }
  getPassword(): string {
    return localStorage.getItem(this.PASSWORD_KEY) || '';
  }
  setId(id: string) {
    localStorage.setItem(this.ID_KEY, id);
  }
  getId(): number {
    const sellerIdString = localStorage.getItem(this.ID_KEY);
    if (sellerIdString) {
      return +sellerIdString;
    } else {
      return 0; 
    }
  }
  
  
  setVille(ville: string) {
    localStorage.setItem(this.VILLE_KEY, ville);
  }

  getVille(): string {
    return localStorage.getItem(this.VILLE_KEY) || '';
  }

  setPhone(phone: string) {
    localStorage.setItem(this.PHONE_KEY, phone);
  }

  getPhone(): string {
    return localStorage.getItem(this.PHONE_KEY) || '';
  }
  setAgence(agence: string) {
    localStorage.setItem(this.AGENCE_KEY, agence);
  }

  getAgence(): string {
    return localStorage.getItem(this.AGENCE_KEY) || '';
  }
}
