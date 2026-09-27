import { Routes } from '@angular/router';
import { ProductList } from './features/products/product-list/product-list';
import { ProductForm } from './features/products/product-form/product-form';
import { ProductDetails } from './features/products/product-details/product-details';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
    {path: "products",
        canActivate:[authGuard],
         component: ProductList,
     
    },
    {
        path: "products/new",
          canActivate:[authGuard],
        component:ProductForm
    },
    {
        path: 'products/edit/:id',
          canActivate:[authGuard],
        component: ProductForm
    },
     {
     path: 'products/cursor',
       canActivate:[authGuard],
     loadComponent: () =>
       import('./features/products/product-cursor-list/product-cursor-list')
      .then(m => m.ProductCursorList)
    },
    {
  path: 'login',
  loadComponent: () =>
    import('./features/auth/login/login')
      .then(m => m.Login)
},
{
  path: 'register',
  loadComponent: () =>
    import('./features/auth/register/register')
      .then(m => m.Register)
},
    {
         path: 'products/:id',
           canActivate:[authGuard],
         component: ProductDetails
    },

   
];
