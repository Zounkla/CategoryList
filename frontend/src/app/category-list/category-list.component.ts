import {Component, OnInit} from '@angular/core';
import {Category} from '../category';
import {Subscription} from 'rxjs/Subscription';
import {CategoryService} from '../category.service';

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
    this.categoryService.searchCategories(this.parentName).subscribe((data: Category[]) => {
      this.categories = data;
      this.pageCount = data.length;
      this.categoryService.pages.next(Math.ceil(this.pageCount / 2));
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
    console.log(category);
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
