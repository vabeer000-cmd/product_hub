import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

// @Injectable() tells Angular:

// This class can participate in Angular's Dependency Injection system.

// @Injectable({
//   providedIn: 'root'
// })

// It tells Angular to provide this service at the root application level.

// In practical terms:

// The service is available throughout the application.
// Angular manages its instance.
// You don't need to manually add it to every component's providers.
// Typically, you get a singleton instance for the application.

// “Root level” does not mean the root folder of your project. It means the top-level Angular dependency-injection scope.
// Make ProductService available to the entire Angular application.
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  
  constructor(
    private http: HttpClient
  ){}

  getProducts():Observable<Product[]>{

    return this.http.get<Product[]>('http://localhost:8080/api/products');
  }
}
