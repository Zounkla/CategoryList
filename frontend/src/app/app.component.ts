import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Category} from './category';
import {CategoryService} from './category.service';
import {CategoryResponse} from './category-response';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

  isRoot = 'true';
  dateBefore = '';
  dateAfter = '';
  orderName = 'None';
  orderDate = 'None';
  orderChildren = 'None';
  title: string;
  actualParentName: string;
  pageCount: number;
  currentPage: number;
  creationDate: Date;

  category: Category;
  categories: Category[];

  constructor(private router: Router, private service: CategoryService) {
    this.title = 'CategoryList';
    this.category = new Category();
    this.actualParentName = 'None';
    this.creationDate = new Date('');
    this.service.lastParentName.subscribe( value => {
      this.actualParentName = value;
    });
    this.pageCount = this.service.pages.value;
    this.service.searchCategories(this.actualParentName).subscribe((data: CategoryResponse) => {
      this.categories = Object.values(data.categories);
    });
    this.service.creationDate.subscribe(data => {
      this.creationDate = data;
    });
    this.currentPage = this.service.currentPage.value;
  }

  ngOnInit() {
    this.router.navigate(['/home']);
  }

  resetParent() {
    this.actualParentName = 'None';
    this.isRoot = 'true';
    this.dateBefore = '';
    this.dateAfter = '';
    this.orderName = 'None';
    this.orderDate = 'None';
    this.orderChildren = 'None';
    this.service.currentPage.next(0);
    this.service.changePageCount(this.pageCount);
    this.service.changeCategories(this.categories);
    this.service.changeIsRoot(this.isRoot);
    this.changeBeforeDate(this.dateBefore);
    this.changeAfterDate(this.dateAfter);
    this.service.creationDate.next(new Date());
  }

  changeIsRoot(value: string) {
    this.service.changeIsRoot(value);
    this.service.triggerChangeCategoryList();
  }

  changeBeforeDate(value: string) {
    this.service.changeBeforeDate(value);
    this.service.triggerChangeCategoryList();
  }

  changeAfterDate(value: string) {
    this.service.changeAfterDate(value);
    this.service.triggerChangeCategoryList();
  }

  changeOrderName(value: string) {
    this.service.changeOrderName(value);
    this.service.triggerChangeCategoryList();
  }

  changeOrderDate(value: string) {
    this.service.changeOrderDate(value);
    this.service.triggerChangeCategoryList();
  }

  changeOrderChildren(value: string) {
    this.service.changeOrderChildren(value);
    this.service.triggerChangeCategoryList();
  }

  deleteCurrentCategory() {
    this.service.deleteCategory(this.actualParentName).subscribe(() => this.resetParent());
  }

  changeOldName(value: string) {
    this.service.setOldName(value);
  }
}
