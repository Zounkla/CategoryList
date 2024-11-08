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

  category: Category;
  categories: Category[];

  constructor(private router: Router, private service: CategoryService) {
    this.title = 'CategoryList';
    this.category = new Category();
    this.actualParentName = 'None';
    this.service.lastParentName.subscribe( value => {
      this.actualParentName = value;
    });
    this.service.findCategoriesByPageAndParent(0, this.actualParentName).subscribe(data => {
      this.categories = Object.values(data);
    });
  }

  ngOnInit() {
    this.router.navigate(['/home']);
  }

  resetParent() {
    this.actualParentName = 'None';
    this.service.changeCategories(this.categories);
  }
}
