import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {CategoryService} from './category-service.service';
import {CategoryListComponent} from './category-list/category-list.component';
import {Subject} from 'rxjs/Subject';
import {Category} from './category';
import {c} from '@angular/core/src/render3';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

  title: string;
  actualParentName: string;

  category: Category;
  categories: Category[];

  constructor(private router: Router, private service: CategoryService) {
    this.title = 'CategoryList';
    this.category = new Category();
    this.actualParentName = 'None';
    this.service.lastParentName.subscribe( value => {
      this.actualParentName = value;
    });
  }

  ngOnInit() {
    this.router.navigate(['/category/getCategories']);
  }

  resetParent() {
    this.category = new Category();
    this.category.name = 'None';
    this.actualParentName = 'None';
    this.service.findParent(this.actualParentName).subscribe(data => {
      this.categories = Object.values(data);
    });
    this.service.changeCategories(this.categories);
  }
}
