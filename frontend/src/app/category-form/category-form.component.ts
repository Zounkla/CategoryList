import {Component, OnInit} from '@angular/core';
import {Category} from '../category';
import {ActivatedRoute, Router} from '@angular/router';
import {CategoryService} from '../category-service.service';

@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent implements OnInit {

  category: Category;

  categories: Category[];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private categoryService: CategoryService) {
    this.category = new Category();
  }

  onSubmit() {
    this.categoryService.save(this.category).subscribe(() => this.gotoCategoryList());
  }

  onChange(newValue) {
    this.category.parentName = newValue;
  }

  ngOnInit() {
    this.categories = [];
    this.fetchData();
  }

  fetchData() {
    this.categoryService.findCategoriesByPageAndParent(0, 'None').subscribe(data => {
      this.categories = Object.values(data);
    });
  }

  gotoCategoryList() {
    this.router.navigate(['/home']);
  }

}
