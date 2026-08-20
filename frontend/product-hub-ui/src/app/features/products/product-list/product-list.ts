import { Component, OnInit } from '@angular/core';
import { Product } from '../../../core/models/product.model';
import { ProductService } from '../../../core/services/product';

@Component({
  selector: 'app-product-list',
  imports: [],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css',
})
export class ProductList implements OnInit{

products: Product[] = [];
constructor(private productService: ProductService){}

ngOnInit(): void {
  this.loadProducts();
}

loadProducts():void {
  this.productService.getProducts().subscribe({
    next:(data)=> {
      this.products = data;
    },
    error:(error)=>{
      console.error("Failed to load products ",error);
    }
  })
}
}
