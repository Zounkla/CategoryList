import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import {CategoryListComponent} from './category-list/category-list.component';
import {CategoryFormComponent} from './category-form/category-form.component';
import {CategoryService} from './category-service.service';
import {BrowserModule} from '@angular/platform-browser';


@NgModule({
  declarations: [
    AppComponent,
    CategoryListComponent,
    CategoryFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [CategoryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
