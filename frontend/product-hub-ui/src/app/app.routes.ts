import { Routes } from '@angular/router';
import { ProductList } from './features/products/product-list/product-list';
import { ProductForm } from './features/products/product-form/product-form';
import { ProductDetails } from './features/products/product-details/product-details';

export const routes: Routes = [
    {path: "products",
     component: ProductList,
     
    },
    {
        path: "products/new",
        component:ProductForm
    },
    {
        path: 'products/edit/:id',
        component: ProductForm
    },
    {
         path: 'products/:id',
         component: ProductDetails
    }
];
