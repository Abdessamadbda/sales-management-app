import { Injectable } from '@angular/core';
import { JWTGenerator } from './jwt-generator.interface';

@Injectable({
  providedIn: 'root'
})
export class JWTGeneratorService implements JWTGenerator {
  validateToken(token: string): boolean {
    return !!token;
  }

}
