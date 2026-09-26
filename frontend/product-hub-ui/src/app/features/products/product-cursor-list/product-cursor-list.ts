import { Component, inject, OnInit, signal } from '@angular/core';
import { ProductService } from '../../../core/services/product';
import { Product } from '../../../core/models/product.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-cursor-list',
  imports: [FormsModule],
  templateUrl: './product-cursor-list.html',
  styleUrl: './product-cursor-list.css',
})
export class ProductCursorList implements OnInit{

  constructor(){}

  private productService = inject(ProductService);

  // products: Product[] = [];

  limit = 20;
  // nextCursor:String | null = null;
  // hasNext = false;

  products = signal<Product[]>([]);
nextCursor = signal<string | null>(null);
hasNext = signal(false);
search = signal('');
category = signal('');
available = signal<boolean | undefined>(undefined);
minPrice = signal<number | undefined>(undefined);
maxPrice = signal<number | undefined>(undefined);

  ngOnInit(): void {
      this.loadFirstPage();
  }

  loadFirstPage(): void {
    this.productService
    .getProductsByCursor(this.limit)
    .subscribe(
      response => {

        console.log("Response of cursor : ${response}");
        this.products.set(response.content);
        this.nextCursor.set(response.nextCursor);
        this.hasNext.set(response.hasNext);

            }
    )
console.log("Products cursor : ${products}")
  }

  loadProducts() {
  this.productService
    .getProductsByCursor(
      20,
      undefined,
      this.search(),
      this.category(),
      this.available(),
      this.minPrice(),
      this.maxPrice()
    )
    .subscribe(response => {
      this.products.set(response.content);
      this.nextCursor.set(response.nextCursor);
      this.hasNext.set(response.hasNext);
    });
}
  loadMore() {
  this.productService
    .getProductsByCursor(
      20,
      this.nextCursor() ?? undefined,
      this.search(),
      this.category(),
      this.available(),
      this.minPrice(),
      this.maxPrice()
    )
    .subscribe(response => {
      this.products.update(current => [
        ...current,
        ...response.content
      ]);

      this.nextCursor.set(response.nextCursor);
      this.hasNext.set(response.hasNext);
    });
}

applyFilters(): void {
  this.products.set([]);
  this.nextCursor.set(null);
  this.hasNext.set(false);

  this.loadProducts();
}

clearFilters(): void {
  this.search.set('');
  this.category.set('');
  this.available.set(undefined);
  this.minPrice.set(undefined);
  this.maxPrice.set(undefined);

  this.products.set([]);
  this.nextCursor.set(null);
  this.hasNext.set(false);

  this.loadProducts();
}
}
