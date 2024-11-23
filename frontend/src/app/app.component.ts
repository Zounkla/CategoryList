import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {CategoryService} from './category-service.service';
import {Category} from './category';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

  dateBefore: string = '';

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
    this.service.findPageCategoriesCount(this.actualParentName).subscribe(data => {
      this.pageCount = data;
      this.service.pages.next(data);
    });
    this.service.findCategoriesByPageAndParent(this.actualParentName).subscribe(data => {
      this.categories = Object.values(data);
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
    this.dateBefore = '';
    this.service.currentPage.next(0);
    this.service.changePageCount(this.pageCount);
    this.service.changeCategories(this.categories);
    this.changeBeforeDate(this.dateBefore);
    this.service.creationDate.next(new Date());
  }

  changeBeforeDate(value: string) {
    this.service.changeBeforeDate(value);
    this.service.triggerChangeCategoryList();
  }

  deleteCurrentCategory() {
    this.service.deleteCategory(this.actualParentName).subscribe(() => this.resetParent());
  }
}
