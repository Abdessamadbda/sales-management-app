
export interface JWTGenerator {
    validateToken(token: string): boolean;
}
  