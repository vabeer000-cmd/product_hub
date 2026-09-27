import { Component, inject, OnInit,signal } from '@angular/core';
import { Product } from '../../../core/models/product.model';
import { ProductService } from '../../../core/services/product';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Auth } from '../../../core/services/auth';

@Component({
  selector: 'app-product-list',
  imports: [FormsModule],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css',
})
export class ProductList implements OnInit{

// products: Product[] = [];
products = signal<Product[]>([]);
  loading = signal(false);
  private auth = inject(Auth);
constructor(private productService: ProductService,
            private router: Router
){}

currentPage = signal(0);
pageSize = signal(10);
totalPages = signal(0);
search = signal('');
category = signal('');
available = signal< boolean | undefined> (undefined);
// minPrice: number | undefined = undefined;
minPrice = signal<number | undefined>(undefined)
maxPrice = signal<number | undefined>(undefined)

ngOnInit(): void {
  this.loadProducts();
}



loadProducts(): void {
  this.loading.set(true);

  this.productService.getProducts(
    this.currentPage(),
    this.pageSize(),
    this.search(),
    this.category(),
    this.available(),
    this.minPrice(),
    this.maxPrice()
  ).subscribe({
    next: (data) => {
       console.log('FULL API DATA:', data);

  console.log('CONTENT:', data.content);

  console.log('PAGE:', data.page);

  console.log('TOTAL PAGES:', data.page?.totalPages);
      this.products .set(data.content);
      this.totalPages .set(data.page.totalPages);
      this.loading .set(false);
      
  console.log('Loding flag After completion of response ', this.loading);
   
    },
    error: (error) => {
      console.error('Failed to load products', error);
      this.loading .set(false);
    }
    
    
  });
   
}
nextPage(): void {
    if (this.loading()) {
    return;
  }
  if (this.currentPage() < this.totalPages() - 1) {
    // this.currentPage++;
    this.currentPage.update(page => page + 1);
    this.loadProducts();
  }
}

previousPage(): void {
    if (this.loading()) {
    return;
  }
  if (this.currentPage() > 0) {
    // this.currentPage--;
    this.currentPage.update(page => page - 1);

    this.loadProducts();
  }
}

applyFilters(): void {
    if (this.loading()) {
    return;
  }
  this.currentPage .set( 0);
  this.loadProducts();
}

clearFilters(): void {
    if (this.loading()) {
    return;
  }
  this.search .set('');
  this.category.set( '');
  this.available .set(undefined);
  this.minPrice .set(undefined);
  this.maxPrice .set( undefined);

  this.currentPage .set( 0);
  this.loadProducts();
}

editProduct(id: number): void {
  this.router.navigate(['/products/edit',id])
}
createProduct(): void {
  this.router.navigate(['/products/new']);
}
viewProduct(id: number): void {
  this.router.navigate(['/products', id]);
}

deleteProduct(id: number): void {
  const confirmed = confirm(
    'Are you sure you want to delete this product?'
  );

  if (!confirmed) {
    return;
  }

  this.productService.deleteProduct(id).subscribe({
    next: () => {
      this.loadProducts();
    },
    error: (error) => {
      console.error('Failed to delete product', error);
    }
  });
}

testConcurrentRequests(): void {

  forkJoin([
    this.productService.getProducts(0, 10),
    this.productService.getProducts(1, 10),
    this.productService.getProducts(2, 10),
    this.productService.getProducts(3, 10),
    this.productService.getProducts(4, 10)
  ]).subscribe({
    next: responses => {
      console.log('All requests completed', responses);
    },
    error: error => {
      console.error('Request failed', error);
    }
  });

}

logout(): void {
  this.auth.logout().subscribe({
    next: () => {
      this.router.navigate(['/login']);
    },
    error: error => {
      console.error('Logout failed', error);
    }
  });
}
}
