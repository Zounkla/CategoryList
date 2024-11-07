import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CategoryService} from './category-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title: string;
  actualParentName: string;

  constructor(private router: Router, private service: CategoryService) {
    this.title = 'CategoryList';
    this.actualParentName = 'None';
    this.service.lastParentName.subscribe( value => {
      this.actualParentName = value;
    });
  }

  ngOnInit() {
    this.router.navigate(['/category/getCategories']);
  }
}
