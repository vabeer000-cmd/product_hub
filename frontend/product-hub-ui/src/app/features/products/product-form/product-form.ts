import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { ProductCreateRequest } from '../../../core/models/product-create-request';
import { ProductService } from '../../../core/services/product';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiError } from '../../../core/models/api-error.model';
import { HttpErrorResponse } from '@angular/common/http';
import { ProductUpdateRequest } from '../../../core/models/product-update-request.model';
 

@Component({
  selector: 'app-product-form',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './product-form.html',
  styleUrl: './product-form.css',
})
export class ProductForm implements OnInit  {

  errorMessage = signal('')
  fieldErrors = signal<Record<string,string>>({})
  isEditingMode = signal(false);
  constructor(
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute){
  }

  productForm = new FormGroup({
    name: new FormControl('',
    ),
    description: new FormControl(''),
    price: new FormControl<number|null>(null,),
    category: new FormControl('',),
    imageUrl:new FormControl(''),
    version: new FormControl<number | null>(null)
  });

  ngOnInit(): void {
      const id = this.route.snapshot.paramMap.get('id')

      console.log("PRODUCT ID: ",id)

      if(id){
        this.isEditingMode.set(true);
          this.productService.getProductById(Number(id)).subscribe({
        next: (product)=>{
          console.log('PRODUCT: ',product)
            this.productForm.patchValue({
            name: product.name,
            description: product.description,
            price: product.price,
            category: product.category,
            imageUrl: product.imageUrl,
            version: product.version
          })
        },
        error:(error) => {
          console.error('FAILED TO LOAD PRODUCT: ',error)

        
        }

      })
      }

    
  }
  // createProduct():void {
  //   if(this.productForm.invalid){
  //     return
  //   }

  //   console.log('FORM VALUE: ',this.productForm.value)

  //   const product = this.productForm.value as ProductCreateRequest;

  //   this.productService.createProduct(product).subscribe({
  //     next: (response) => {
  //       console.log('PRODUCT CREATED: ',response)
  //       this.router.navigate(['/products'])
  //     },
  //     error: (error: HttpErrorResponse) => {
  //       const apiError = error.error as ApiError
  //       console.error('CREATE PRODUCT FAILED:',apiError)
  //       console.log('FULL ERROR:', error);
  // console.log('BACKEND ERROR:', error.error);
  // console.log('FIELD ERRORS:', error.error?.errors);
  //       if(error.status === 400 && apiError.errors){
  //         console.log('VALIDATION ERRORS: ',apiError.errors)
  //         this.fieldErrors.set(apiError.errors)
  //       }
  //       else{
  //           this.errorMessage.set(
  //         'Failed to create product. Please try again.'
  //       )
  //       }
      
  //     }
  //   })
  // }

  createProduct(): void {

  const product = this.productForm.value as ProductCreateRequest;

  this.productService.createProduct(product).subscribe({
    next: (response) => {
      console.log('PRODUCT CREATED:', response);
      this.router.navigate(['/products']);
    },
    error: (error: HttpErrorResponse) => {
      this.handleApiError(error);
    }
  });
}
  updateProduct(id: number): void {

  const product = this.productForm.value as ProductUpdateRequest;

  this.productService.updateProduct(id, product).subscribe({
    next: (response) => {
      console.log('PRODUCT UPDATED:', response);
      this.router.navigate(['/products']);
    },
    error: (error: HttpErrorResponse) => {
      this.handleApiError(error);
    }
  });
}
  submitForm(): void {

  if (this.productForm.invalid) {
    return;
  }

 const id = this.route.snapshot.paramMap.get('id')

  if (id) {
    console.log("UPDATE FORM");
    
    this.updateProduct(Number(id));
  } else {
    console.log("CREATE FORM");
    this.createProduct();
  }
}
  getFieldError(fieldName: string): string | null{

    return this.fieldErrors()[fieldName] ?? null

  }

 private handleApiError(error: HttpErrorResponse): void {
  const apiError = error.error as ApiError;

  console.error('API ERROR:', apiError);

  if (apiError.status === 400 && apiError.errors) {
    this.fieldErrors.set(apiError.errors);
  } else if (apiError.status === 409) {
    this.errorMessage.set(
      'This product was modified by another user. Please reload the product and try again.'
    );
  } else {
    this.errorMessage.set(
      'Something went wrong. Please try again.'
    );
  }
}
}
