import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CategoryListComponent} from './category-list/category-list.component';
import {CategoryFormComponent} from './category-form/category-form.component';

const routes: Routes = [
  { path: 'category/getCategories', component: CategoryListComponent },
  { path: 'category', component: CategoryFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
