import {Category} from './category';

export interface CategoryResponse {
  categories: Category[];
  totalPages: number;
}
