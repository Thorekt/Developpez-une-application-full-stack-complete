import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  protected baseUrl = 'http://localhost:9000/api';

  constructor(protected http: HttpClient) { }
}
