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

  pageCount: number;

  currentPage: number;

  constructor(private categoryService: CategoryService) {
    this.categoryService.currentCategories.subscribe(
      value => this.categories = value
    );
    this.categoryService.pageCount.subscribe(
      value => this.pageCount = value
    )
    this.parentName = 'None';
    this.currentPage = 0
  }

  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.categoryService.findCategoriesByPageAndParent(this.currentPage, this.parentName).subscribe(data => {
      this.categories = Object.values(data);
    });
    this.categoryService.findPageCategoriesCount(this.parentName).subscribe(data => {
      this.pageCount = data.id;
    })
  }

  changeParentCategory(category: Category) {
    this.parentName = category.name;
    this.currentPage = 0;
    this.fetchData();
    this.categoryService.lastParentName.next(category.name);
  }

  displayFirstPage() {
    this.currentPage = 0
    this.fetchData();
  }

  displayLastPage() {
    this.currentPage = this.pageCount - 1;
    this.fetchData();
  }

  displayPreviousPage() {
    this.currentPage = this.currentPage - 1;
    this.fetchData();
  }

  displayNextPage() {
    this.currentPage = this.currentPage + 1;
    this.fetchData();
  }

  isNotPossibleToDisplayPreviousPage(): boolean {
    return this.currentPage <= 0;
  }

  isNotPossibleToDisplayNextPage(): boolean {
    return this.currentPage >= this.pageCount - 1;
  }
}
