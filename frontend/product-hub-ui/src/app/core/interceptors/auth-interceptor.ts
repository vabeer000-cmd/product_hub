import {
  HttpErrorResponse,
  HttpInterceptorFn
} from '@angular/common/http';

import { inject } from '@angular/core';
import { Router } from '@angular/router';

import {
  catchError,
  finalize,
  shareReplay,
  switchMap,
  throwError
} from 'rxjs';

import { Auth } from '../services/auth';

let refreshRequest$: ReturnType<Auth['refreshToken']> | null = null;

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const auth = inject(Auth);
  const router = inject(Router);

  const accessToken = auth.getAccessToken();

  const isAuthRequest =
    req.url.includes('/v1/auth/login') ||
    req.url.includes('/v1/auth/register') ||
    req.url.includes('/v1/auth/refresh');


  // Add access token
  if (accessToken) {

    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`
      }
    });

  }


  return next(req).pipe(

    catchError((error: HttpErrorResponse) => {

      // Don't refresh authentication endpoints
      if (error.status !== 401 || isAuthRequest) {

        return throwError(() => error);

      }


      // Start refresh only if one isn't already running
      if (!refreshRequest$) {

        refreshRequest$ = auth.refreshToken().pipe(

          shareReplay(1),

          finalize(() => {
            refreshRequest$ = null;
          })

        );

      }


      // All failed requests share the same refresh request
      return refreshRequest$.pipe(

        switchMap(response => {

          const retryRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${response.accessToken}`
            }
          });

          return next(retryRequest);

        }),

        catchError(refreshError => {

          // Refresh token is invalid/expired
          auth.clearTokens();

          router.navigate(['/login']);

          return throwError(
            () => refreshError
          );

        })

      );

    })

  );

};