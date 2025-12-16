import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

/**
 * ApiService serves as a base service for making HTTP requests to the backend API.
 * 
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
  providedIn: 'root'
})
export class ApiService {

  /** Base URL for the backend API */
  protected baseUrl = 'http://localhost:9000/api';

  /**
   * Constructs an instance of ApiService.
   * 
   * @param http HttpClient used for making HTTP requests.
   */
  constructor(protected http: HttpClient) { }
}
