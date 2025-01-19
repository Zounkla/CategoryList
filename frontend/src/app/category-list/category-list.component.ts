import {Component, OnInit} from '@angular/core';
import {Category} from '../models/category';
import {Subscription} from 'rxjs/Subscription';
import {CategoryService} from '../services/category.service';
import {CategoryResponse} from '../models/category-response';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css'],
})
export class CategoryListComponent implements OnInit {

  private subscriber: Subscription = new Subscription();

  categories: Category[];

  parentName: string;

  pageCount: number;

  currentPage: number;

  constructor(private categoryService: CategoryService) {
    this.categoryService.currentCategories.subscribe(
      (value: Category[]) => this.categories = value
    );
    this.categoryService.pages.subscribe(
      value => this.pageCount = value
    );
    this.categoryService.currentPage.subscribe(
      value => this.currentPage = value
    );
    this.categoryService.lastParentName.subscribe(value =>
      this.parentName = value
    );
    this.currentPage = this.categoryService.currentPage.value;
  }

  ngOnInit() {
    this.subscriber = this.categoryService.currentCategories.subscribe(
      () => {this.fetchData();
      }
    );
    this.fetchData();
  }

  fetchData() {
    this.categoryService.searchCategories(this.parentName).subscribe((data: CategoryResponse) => {
      this.categories = data.categories;
      this.pageCount = data.totalPages;
      this.categoryService.pages.next(this.pageCount);
    });
    this.categoryService.currentPage.subscribe(value =>
        this.currentPage = value,
    );
  }

  changeParentCategory(category: Category) {
    this.parentName = category.name;
    this.categoryService.changeIsRoot('false');
    this.categoryService.currentPage.next(0);
    this.fetchData();
    this.categoryService.lastParentName.next(category.name);
    this.categoryService.creationDate.next(new Date(category.creationDate));
  }

  displayFirstPage() {
    this.categoryService.currentPage.next(0);
    this.fetchData();
  }

  displayLastPage() {
    this.categoryService.currentPage.next(this.pageCount - 1);
    this.fetchData();
  }

  displayPreviousPage() {
    this.categoryService.currentPage.next(this.currentPage - 1);
    this.fetchData();
  }

  displayNextPage() {
    this.categoryService.currentPage.next(this.currentPage + 1);
    this.fetchData();
  }

  isNotPossibleToDisplayPreviousPage(): boolean {
    return this.currentPage <= 0;
  }

  isNotPossibleToDisplayNextPage(): boolean {
    return this.currentPage >= this.pageCount - 1;
  }
}
