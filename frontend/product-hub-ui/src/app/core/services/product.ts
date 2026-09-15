import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product, ProductPage } from '../models/product.model';
import { ProductCreateRequest } from '../models/product-create-request';
import { ProductUpdateRequest } from '../models/product-update-request.model';
import { environment } from '../../../environments/environment';

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



getProducts(
  page: number,
  size: number,
  search?: string,
  category?: string,
  available?: boolean,
  minPrice?: number,
  maxPrice?: number
): Observable<ProductPage> {

  let params = new HttpParams()
    .set('page', page)
    .set('size', size);

  if (search) {
    params = params.set('search', search);
  }

  if (category) {
    params = params.set('category', category);
  }

  if (available !== undefined) {
    params = params.set('available', available);
  }

  if (minPrice !== undefined) {
    params = params.set('minPrice', minPrice);
  }

  if (maxPrice !== undefined) {
    params = params.set('maxPrice', maxPrice);
  }

  // return this.http.get<ProductPage>(
  //   'http://localhost:8082/api/products',
  //   { params }
  const url = `${environment.apiUrl}/products`;

console.log(
  'REQUEST URL:',
  `${url}?${params.toString()}`
);

return this.http.get<ProductPage>(
  url,
  { params }
  );
}

createProduct(product: ProductCreateRequest): Observable<Product>{
  return this.http.post<Product>(
   `${environment.apiUrl}/products`,
   product
  );
}

getProductById(id: number): Observable<Product>{
  return this.http.get<Product>(
      `${environment.apiUrl}/products/${id}`
  )
}

updateProduct(
  id: number,
  product: ProductUpdateRequest
): Observable<Product>{
  return this.http.put<Product>(
    `${environment.apiUrl}/products/${id}`,
     product
  )

}

deleteProduct(id: number): Observable<void> {
  return this.http.delete<void>(
    `http://localhost:8082/api/products/${id}`
  );
}
}
