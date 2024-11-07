import { Component, OnInit } from '@angular/core';
import { Output, EventEmitter } from '@angular/core';
import {Category} from '../category';
import {CategoryService} from '../category-service.service';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {

  categories: Category[];

  constructor(private categoryService: CategoryService) {
    this.categories = [];
  }

  ngOnInit() {
    this.categories = [];
    this.fetchData();
  }

  fetchData() {
    this.categoryService.findAll().subscribe(data => {
      this.categories = Object.values(data);
    });
  }

  printCategory(category: Category) {
    console.log(category.name);
  }
}
