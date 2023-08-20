import { InjectionToken } from '@angular/core';
import { JWTGenerator } from './jwt-generator.interface';

export const JWT_GENERATOR = new InjectionToken<JWTGenerator>('JWTGenerator');
