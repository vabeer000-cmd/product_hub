import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { Auth } from '../services/auth';

export const authGuard: CanActivateFn = () => {

  const auth = inject(Auth);
  const router = inject(Router);

  const accessToken = auth.getAccessToken();

  if (accessToken) {
    return true;
  }

  return router.createUrlTree(['/login']);
};