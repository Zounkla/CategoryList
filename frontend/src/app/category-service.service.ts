import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Category} from './category';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

@Injectable()
export class CategoryService {

  private categoryUrl: string;

  private categoriesUrl: string;

  private categories = new BehaviorSubject(new Array<Category>());
  currentCategories = this.categories.asObservable();


  constructor(private http: HttpClient) {
    this.categoryUrl = 'http://localhost:8080/category';
  }

  public currentPage = new BehaviorSubject<number>(0);

  public pages = new BehaviorSubject<number>(5);

  public lastParentName: BehaviorSubject<string> = new BehaviorSubject<string>('None');

  public findCategoriesByPageAndParent(page: number, parentName: string): Observable<Category[]> {
    this.categoriesUrl = 'http://localhost:8080/category/search?page=' + page + '' +
      '&parentName=' + parentName;
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
    return this.http.post<Category>(this.categoryUrl, category);
  }

  public changeCategories(categories: Category[]) {
    this.categories.next(categories);
    this.lastParentName.next('None');
  }

  public changePageCount(pageCount: number) {
    this.pages.next(pageCount);
  }
}
