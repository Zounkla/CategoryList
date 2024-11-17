import {Component, OnInit} from '@angular/core';
import {Category} from '../category';
import {CategoryService} from '../category-service.service';
import {Subscription} from 'rxjs/Subscription';

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
      value => this.categories = value
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
    this.categoryService.findCategoriesByPageAndParent(this.parentName).subscribe(data => {
      this.categories = Object.values(data);
    });
    this.categoryService.findPageCategoriesCount(this.parentName).subscribe(data => {
      this.pageCount = data;
      this.categoryService.pages.next(data);
    });
    this.categoryService.currentPage.subscribe(value =>
        this.currentPage = value,
    );
  }

  changeParentCategory(category: Category) {
    this.parentName = category.name;
    this.categoryService.currentPage.next(0);
    this.fetchData();
    this.categoryService.lastParentName.next(category.name);
    this.categoryService.creationDate.next(new Date(category.date));
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
