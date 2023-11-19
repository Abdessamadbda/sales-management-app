import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, Renderer2 } from '@angular/core';
import { SellersListService } from './sellers-list.service';
import { Seller } from './Seller.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sellers-list',
  templateUrl: './sellers-list.component.html',
  styleUrls: ['./sellers-list.component.css']
})
export class SellersListComponent {
  editedSeller: any = {};  
  editedIndex: number=0;
  sellersData!: any[];
  model: any[] = [];
  seller!:Seller;
  index:number=0;


  constructor( private router: Router,private http: HttpClient, private sellersListService: SellersListService,private renderer: Renderer2, private el: ElementRef) {};
  ngOnInit() {
    this.fetchSellersData();
  }
  fetchSellersData() {
    
    this.http.get<any[]>('http://localhost:8080/admin/sellers').subscribe(
      (data) => {
        this.sellersData = data;
        this.model = this.sellersData.map(seller => ({
          updatedNom: seller.nom_complet,
          updatedVille: seller.ville,
          updatedAgence: seller.agence,
          updatedTelephone: seller.phone,
          updatedUsername:seller.username,
          updatedPassword:seller.password,
        }));

      },
      (error) => {
        console.error('Error fetching sales data:', error);
      }
    );
  }
  updateSale(seller: Seller, index: number): void {
   
    const updatedNom = this.model[index].updatedNom;
    const updatedVille = this.model[index].updatedVille;
  const updatedAgence = this.model[index].updatedAgence;
  const updatedTelephone = this.model[index].updatedTelephone;
  const updatedUsername = this.model[index].updatedUsername;
  const updatedPassword = this.model[index].updatedPassword;
  
    const updatedSeller: Seller = {
      id: seller.id,
      nom_complet: updatedNom,
      ville:updatedVille,
      agence: updatedAgence, 
      phone: updatedTelephone,
      username:updatedUsername,
      password:updatedPassword,
      
    };
    
    this.sellersListService.updateSeller(updatedSeller).subscribe(
      (response: Seller) => {
        const index = this.sellersData.findIndex(s => s.id === response.id);
        if (index !== -1) {
          this.sellersData[index] = response;
        }
        alert("modifié")
      },
      (error) => {
        alert("error")
      }
    );
  }
  removeSale(index: number): void {
    if (confirm('Voulez-vous vraiment supprimer cet élément ?')) {
      const sellerToRemove = this.sellersData[index];
      this.sellersListService.removeSeller(sellerToRemove.id).subscribe(
        () => {
          this.sellersData.splice(index, 1);
          alert('Élément supprimé');
        },
        (error) => {
          console.error('Error removing sale:', error);
          alert('Erreur lors de la suppression');
        }
      );
    }
  }
  updateAndClear(seller: Seller, index: number): void {
  
    this.updateSale(seller, index);
    this.clearFields();
  }
  showEditModal(seller: Seller, index: number) {
    this.seller=seller;
    this.index=index;
    this.editedSeller = { ...seller };
    this.editedIndex = index;
    
        const modalElement = this.el.nativeElement.querySelector('#editSellerModal');
    this.renderer.addClass(modalElement, 'show'); 
  }
  clearFields(): void {
    this.model.forEach(item => {
      item.updatedNom = '';
      item.updatedVille = '';
      item.updatedAgence = '';
      item.updatedTelephone = '';
      item.updatedUsername='';
      item.updatedPassword='';
    });
  }
  cancelEdit() {
    const modalElement = this.el.nativeElement.querySelector('#editSellerModal');
        this.renderer.removeClass(modalElement, 'show');
  }
}
