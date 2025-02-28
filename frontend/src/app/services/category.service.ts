import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Category} from '../models/category';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {CategoryResponse} from '../models/category-response';
import { catchError } from 'rxjs/operators';

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
    this.isRoot = 'true';
    this.beforeDate = '';
    this.afterDate = '';
    this.orderName = 'None';
    this.orderDate = 'None';
    this.orderChildren = 'None';
  }

  public currentPage = new BehaviorSubject<number>(0);

  public pages = new BehaviorSubject<number>(5);

  public lastParentName: BehaviorSubject<string> = new BehaviorSubject<string>('None');

  public oldName: BehaviorSubject<string> = new BehaviorSubject<string>('');

  public creationDate: BehaviorSubject<Date | null> = new BehaviorSubject<Date | null>(null);

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

  public searchCategories(parentName: string): Observable<CategoryResponse> {
    const page: number = this.currentPage.getValue();
    this.categoriesUrl = 'http://localhost:8080/category/search?page=' + page;
    if (parentName !== 'None') {
      this.categoriesUrl += '&parentName=' + parentName;
    }
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
    return this.http.get<CategoryResponse>(this.categoriesUrl);
  }
  public findAll() {
    const pageUrl = 'http://localhost:8080/category/all';
    return this.http.get<CategoryResponse>(pageUrl);
  }

  public save(category: Category) {
    const categoryDTO = {
      name: category.name,
      parentName: category.parentName
    };
    return this.http.post<Category>(this.categoryUrl, categoryDTO, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      catchError(() => {
        throw new Error('Category name already exists or can\'t be parent of itself');
      })
    );
  }

  public edit(category: Category) {
    const categoryDTO = {
      name: category.name,
      oldName: category.oldName,
      parentName: category.parentName
    };
    return this.http.put<Category>(this.categoryUrl, categoryDTO, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      catchError(() => {
        throw new Error('Category name already exists or can\'t be parent of itself');
      })
    );
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
    const deleteCategoryUrl = 'http://localhost:8080/category?categoryName=' + categoryName;
    return this.http.delete(deleteCategoryUrl);
  }
  public setOldName(newName: string) {
    this.oldName.next(newName);
  }

}
