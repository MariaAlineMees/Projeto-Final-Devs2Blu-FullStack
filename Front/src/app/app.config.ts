// front/src/app/app.config.ts

import { ApplicationConfig, importProvidersFrom, LOCALE_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideClientHydration } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';

import { routes } from './app.routes';

// Registra a localidade pt-BR
registerLocaleData(localePt);

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(),

    // Configuração para usar Formulários
    importProvidersFrom(FormsModule),

    // Define o LOCALE_ID da aplicação para pt-BR
    { provide: LOCALE_ID, useValue: 'pt-BR' }
  ]
};
