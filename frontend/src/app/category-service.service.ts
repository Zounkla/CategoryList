import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Category} from './category';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

@Injectable()
export class CategoryService {

  private categoryUrl: string;

  private categoriesUrl: string;

  private isRoot: string;

  private beforeDate: string;
  private afterDate: string;
  private orderName: string;
  private orderDate: string;
  private orderChildren: string;
  private categories = new BehaviorSubject(new Array<Category>());
  currentCategories = this.categories.asObservable();


  constructor(private http: HttpClient) {
    this.categoryUrl = 'http://localhost:8080/category';
    this.isRoot = 'None';
    this.beforeDate = '';
    this.afterDate = '';
    this.orderName = 'None';
    this.orderDate = 'None';
    this.orderChildren = 'None';
  }

  public currentPage = new BehaviorSubject<number>(0);

  public pages = new BehaviorSubject<number>(5);

  public lastParentName: BehaviorSubject<string> = new BehaviorSubject<string>('None');

  public creationDate: BehaviorSubject<Date> = new BehaviorSubject<Date>(new Date());

  public changeIsRoot(newValue: string) {
    this.isRoot = newValue;
  }
  public changeBeforeDate(newValue: string) {
    this.beforeDate = newValue;
  }

  public changeAfterDate(newValue: string) {
    this.afterDate = newValue;
  }

  public changeOrderName(newValue: string) {
    this.orderName = newValue;
  }

  public changeOrderDate(newValue: string) {
    this.orderDate = newValue;
  }

  public changeOrderChildren(newValue: string) {
    this.orderChildren = newValue;
  }

  public findCategoriesByPageAndParent(parentName: string): Observable<Category[]> {
    const page: number = this.currentPage.getValue();
    this.categoriesUrl = 'http://localhost:8080/category/search?page=' + page + '' +
      '&parentName=' + parentName;
    if (this.isRoot !== 'None') {
      this.categoriesUrl += '&isRoot=' + this.isRoot;
    }
    if (this.beforeDate !== '') {
      this.categoriesUrl += '&beforeDate=' + this.beforeDate;
    }
    if (this.afterDate !== '') {
      this.categoriesUrl += '&afterDate=' + this.afterDate;
    }
    if (this.orderName !== 'None') {
      this.categoriesUrl += '&orderByName=' + this.orderName;
    }
    if (this.orderDate !== 'None') {
      this.categoriesUrl += '&orderByCreationDate=' + this.orderDate;
    }
    if (this.orderChildren !== 'None') {
      this.categoriesUrl += '&orderByChildrenNumber=' + this.orderChildren;
    }
    console.log(this.categoriesUrl);
    return this.http.get<Category[]>(this.categoriesUrl);
  }

  public findPageCategoriesCount(parentName: string) {
    const pageCountUrl = 'http://localhost:8080/category/getPageCount?parentName=' + parentName;
    return this.http.get<number>(pageCountUrl);
  }

  public findAll() {
    const pageUrl = 'http://localhost:8080/category';
    return this.http.get<Category[]>(pageUrl);
  }

  public save(category: Category) {
    console.log(category);
    return this.http.post<Category>(this.categoryUrl, category);
  }

  public changeCategories(categories: Category[]) {
    this.lastParentName.next('None');
    this.categories.next(categories);
  }

  public triggerChangeCategoryList() {
    this.categories.next(this.categories.value);
  }

  public changePageCount(pageCount: number) {
    this.pages.next(pageCount);
  }

  public deleteCategory(categoryName: string) {
    const deleteCategoryUrl = 'http://localhost:8080/category/deleteCategory?categoryName=' + categoryName;
    return this.http.delete(deleteCategoryUrl);
  }
}
