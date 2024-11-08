import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Category} from './category';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {EventEmitter} from 'protractor';

@Injectable()
export class CategoryService {

  private categoryUrl: string;

  private categoriesUrl: string;

  private categories = new BehaviorSubject(new Array<Category>());
  currentCategories = this.categories.asObservable();

  private pages = new BehaviorSubject();
  pageCount = this.pages.asObservable();

  constructor(private http: HttpClient) {
    this.categoryUrl = 'http://localhost:8080/category';
  }

  public lastParentName: BehaviorSubject<string> = new BehaviorSubject<string>('None');

  public findCategoriesByPageAndParent(page: number, parentName: string): Observable<Category[]> {
    this.categoriesUrl = 'http://localhost:8080/category/getPaginatedCategories?page=' + page +'' +
      '&parentName=' + parentName;
    return this.http.get<Category[]>(this.categoriesUrl);
  }

  public findPageCategoriesCount(parentName: string): Observable<number> {
    let pageCountUrl = 'http://localhost:8080/category/getPageCount?parentName=' + parentName;
    return this.http.get(pageCountUrl);
  }

  public save(category: Category) {
    return this.http.post<Category>(this.categoryUrl, category);
  }

  public changeCategories(categories: Category[]) {
    this.categories.next(categories);
  }

  public changePageCount(pageCount: number) {
    this.pages.next(pageCount);
  }
}
