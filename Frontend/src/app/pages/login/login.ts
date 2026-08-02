import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  });

  showPassword = false;
  toastMessage = '';
  isSuccessToast = true;

  constructor(private authService: AuthService, private router: Router) {}

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  showToast(msg: string, success: boolean) {
    this.toastMessage = msg;
    this.isSuccessToast = success;
    setTimeout(() => this.toastMessage = '', 3000);
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          this.showToast('Logare reușită!', true);
          setTimeout(() => this.router.navigate(['/employee']), 1500);
        },
        error: (err) => {
          this.showToast('Eroare: Email sau parolă incorecte!', false);
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
    }
  }
}