import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarComponent } from './home/nav-bar/nav-bar.component';
import { HeroSectionComponent } from './home/hero-section/hero-section.component';
import { FooterComponent } from './home/footer/footer.component';
import { LoginComponent } from './seller/login/login.component';
import { AloginComponent } from './admin/alogin/alogin.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { Profile1Component } from './seller/profile1/profile1.component';
import { Aprofile1Component } from './admin/aprofile1/aprofile1.component';
import { StatesComponent } from './admin/states/states.component';
import { NewsellerComponent } from './admin/newseller/newseller.component';
import { NavBar1Component } from './seller/nav-bar1/nav-bar1.component';
import { NavBar2Component } from './admin/nav-bar2/nav-bar2.component';
import { DeclarationComponent } from './seller/declaration/declaration.component'
import { JWTGeneratorService } from './jwt-generator.service';
import { JWT_GENERATOR } from './jwt-generator.token';
import { DatePipe } from '@angular/common';
import { SellersComponent } from './admin/sellers/sellers.component';
import { UpdateComponent } from './seller/update/update.component';
import { SstatesComponent } from './seller/sstates/sstates.component';
import { SelectSellerComponent } from './seller/selectSeller/selectSeller.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    HeroSectionComponent,
    FooterComponent,
    LoginComponent,
    AloginComponent,
    Profile1Component,
    Aprofile1Component,
    StatesComponent,
    NewsellerComponent,
    NavBar1Component,
    NavBar2Component,
    DeclarationComponent,
    SellersComponent,
    UpdateComponent,
    SstatesComponent,
    SelectSellerComponent
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [JWTGeneratorService,
    { provide: JWT_GENERATOR, useClass: JWTGeneratorService },DatePipe],
  bootstrap: [AppComponent]

})
export class AppModule { }
