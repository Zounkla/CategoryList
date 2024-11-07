import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Category} from './category';
import {Subject} from 'rxjs/Subject';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

@Injectable()
export class CategoryService {

  private categoryUrl: string;

  private categoriesUrl: string;

  private parentName: string;

  private parentNameChange: Subject<string> = new Subject<string>();

  constructor(private http: HttpClient) {
    this.categoryUrl = 'http://localhost:8080/category';
    this.categoriesUrl = 'http://localhost:8080/category/getCategories';
  }

  public findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.categoriesUrl);
  }

  public save(category: Category) {
    return this.http.post<Category>(this.categoryUrl, category);
  }

  public lastParentName: BehaviorSubject<string> = new BehaviorSubject<string>('None');

}
