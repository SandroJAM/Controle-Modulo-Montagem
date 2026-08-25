import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { relatorioGuard } from './core/guards/relatorio.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: 'ordens-montagem',
        loadComponent: () => import('./features/ordens-montagem/ordens-montagem.component').then(m => m.OrdensMontagemComponent)
      },
      {
        path: 'ordens-montagem/nova-avulsa',
        loadComponent: () => import('./features/ordens-montagem-avulsa/ordens-montagem-avulsa.component').then(m => m.OrdensMontagemAvulsaComponent)
      },
      {
        path: 'montadores',
        loadComponent: () => import('./features/montadores/montadores.component').then(m => m.MontadoresComponent)
      },
      {
        path: 'agenda',
        loadComponent: () => import('./features/agenda/agenda.component').then(m => m.AgendaComponent)
      },
      {
        path: 'dashboard',
        canActivate: [relatorioGuard],
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'usuarios',
        canActivate: [adminGuard],
        loadComponent: () => import('./features/usuarios/usuarios.component').then(m => m.UsuariosComponent)
      },
      { path: '', pathMatch: 'full', redirectTo: 'ordens-montagem' }
    ]
  },
  { path: '**', redirectTo: 'ordens-montagem' }
];
