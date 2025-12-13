import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class ArticleService extends ApiService {

  private prefix = '/article';
}
