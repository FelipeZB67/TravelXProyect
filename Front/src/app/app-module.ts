import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { BienvenidaComponent } from './components/bienvenida/bienvenida';
import { LoginComponent } from './components/login/login';
import { RegistroComponent } from './components/registro/registro';

@NgModule({
  declarations: [App, BienvenidaComponent, LoginComponent, RegistroComponent],
  imports: [BrowserModule, CommonModule, RouterModule, FormsModule, AppRoutingModule],
  bootstrap: [App]
})
export class AppModule {}
