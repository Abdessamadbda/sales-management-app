import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { sellerAuthGuard } from './seller-auth.guard';
import { AuthenticationService } from './login/authentication.service';

describe('SellerAuthGuard', () => {
  let guard: sellerAuthGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        sellerAuthGuard,
        AuthenticationService
        // Additional dependencies for the guard
      ]
    });
    guard = TestBed.inject(sellerAuthGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
