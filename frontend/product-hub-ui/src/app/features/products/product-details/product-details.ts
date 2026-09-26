import { Component, OnInit, signal } from '@angular/core';
import { Product } from '../../../core/models/product.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../core/services/product';

@Component({
  selector: 'app-product-details',
  imports: [],
  templateUrl: './product-details.html',
  styleUrl: './product-details.css',
})
export class ProductDetails implements OnInit{
  product = signal<Product | null>(null);
  loading = signal(false);
  notFound = signal(false);

constructor(
  private route: ActivatedRoute,
  private router: Router,
  private productService: ProductService
) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      return;
    }

    this.loading.set(true);

    this.productService.getProductById(Number(id)).subscribe({
      next: (product) => {
        this.product.set(product);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Failed to load product', error);
        this.loading.set(false);
         if (error.status === 404) {
            this.notFound.set(true);
          }
      }
    });
  }

  editProduct(): void {
  const id = this.product()?.id;

  if (id) {
    this.router.navigate(['/products/edit', id]);
  }
}

goBack(): void {
  this.router.navigate(['/products']);
}
}
