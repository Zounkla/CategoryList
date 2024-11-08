import {Component, Input, OnInit, Output} from '@angular/core';
import {Category} from '../category';
import {CategoryService} from '../category-service.service';
import {EventEmitter} from 'protractor';
import {v} from '@angular/core/src/render3';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css'],
})
export class CategoryListComponent implements OnInit {

  categories: Category[];

  parentName: string;

  constructor(private categoryService: CategoryService) {
    this.categoryService.currentCategories.subscribe(
      value => this.categories = value
    );
    this.parentName = 'None';
  }

  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.categoryService.findParent(this.parentName).subscribe(data => {
      this.categories = Object.values(data);
    });
  }

  changeParentCategory(category: Category) {
    this.parentName = category.name;
    this.fetchData();
    this.categoryService.lastParentName.next(category.name);
  }
}
