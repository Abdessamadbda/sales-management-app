import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './seller/login/login.component';
import { AloginComponent } from './admin/alogin/alogin.component';
import { Aprofile1Component } from './admin/aprofile1/aprofile1.component';
import { StatesComponent } from './admin/states/states.component';
import { NewsellerComponent } from './admin/newseller/newseller.component';
import { Profile1Component } from './seller/profile1/profile1.component';
import { DeclarationComponent } from './seller/declaration/declaration.component';
import { sellerAuthGuard } from './seller/seller-auth.guard';
import { AdminAuthGuardService } from './admin/admin-auth-guard.service';
import { SellersComponent } from './admin/sellers/sellers.component';
import { UpdateComponent } from './seller/update/update.component';
import { SstatesComponent } from './seller/sstates/sstates.component';
import { SelectSellerComponent } from './seller/selectSeller/selectSeller.component';

const routes: Routes = [
  { path: 'seller/login', component: LoginComponent },
  { path: 'seller/profile1', component: Profile1Component, canActivate: [sellerAuthGuard] },
  { path: 'seller/declaration', component: DeclarationComponent, canActivate: [sellerAuthGuard]  },
  { path: 'admin/alogin', component: AloginComponent },
  { path: 'admin/aprofile1', component: Aprofile1Component,canActivate: [AdminAuthGuardService] },
  { path: 'admin/states', component: StatesComponent,canActivate: [AdminAuthGuardService] },
  { path: 'admin/newseller', component: NewsellerComponent,canActivate: [AdminAuthGuardService] },
  { path: 'admin/sellers', component: SellersComponent,canActivate: [AdminAuthGuardService] },

  { path: 'seller/update', component: UpdateComponent,canActivate: [sellerAuthGuard] },

  { path: 'seller/sstates', component: SstatesComponent,canActivate: [sellerAuthGuard] },
    { path: 'seller/selectSeller', component: SelectSellerComponent,canActivate: [sellerAuthGuard] },



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
