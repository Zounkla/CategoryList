import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title: string;
  actualParentName: string;

  constructor(private router: Router) {
    this.title = 'CategoryList';
    this.actualParentName = 'None';
  }

  ngOnInit() {
    this.router.navigate(['/category/getCategories']);
  }
}
