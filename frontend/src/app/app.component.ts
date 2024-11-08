import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CategoryService} from './category-service.service';
import {Category} from './category';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

  title: string;
  actualParentName: string;
  lastParentName: string;
  category: Category;
  categories: Category[];

  constructor(private router: Router, private service: CategoryService) {
    this.title = 'CategoryList';
    this.category = new Category();
    this.actualParentName = 'None';
    this.lastParentName = this.actualParentName;
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
