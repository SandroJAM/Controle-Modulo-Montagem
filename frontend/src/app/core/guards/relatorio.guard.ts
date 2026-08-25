import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const relatorioGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.podeVerRelatorios()) {
    return true;
  }

  router.navigate(['/ordens-montagem']);
  return false;
};
