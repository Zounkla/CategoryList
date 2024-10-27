import { Component } from '@angular/core';
import {Category} from '../category';
import {ActivatedRoute, Router} from '@angular/router';
import {CategoryService} from '../category-service.service';

@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent {

  category: Category;
  constructor(private route: ActivatedRoute,
              private router: Router,
              private categoryService: CategoryService) {
    this.category = new Category();
  }

  onSubmit() {
    this.categoryService.save(this.category).subscribe(() => this.gotoCategoryList());
  }

  gotoCategoryList() {
    this.router.navigate(['/category/getCategories']);
  }

}
