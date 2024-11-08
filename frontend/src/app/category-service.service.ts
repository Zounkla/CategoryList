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
  constructor(private http: HttpClient) {
    this.categoryUrl = 'http://localhost:8080/category';
  }

  public lastParentName: BehaviorSubject<string> = new BehaviorSubject<string>('None');

  public findParent(name: string): Observable<Category[]> {
    this.categoriesUrl = 'http://localhost:8080/category/children?name=' + name;
    return this.http.get<Category[]>(this.categoriesUrl);
  }

  public save(category: Category) {
    return this.http.post<Category>(this.categoryUrl, category);
  }

  public changeCategories(categories: Category[]) {
    this.categories.next(categories);
  }
}
