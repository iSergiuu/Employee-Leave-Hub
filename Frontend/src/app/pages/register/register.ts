import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  
  registerForm = new FormGroup({
    fullname: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^a-zA-Z0-9]).{8,}/) 
    ])
  });

  showPassword = false;
  toastMessage = '';
  isSuccessToast = true;

  constructor(private authService: AuthService, private router: Router) {}

  get pwd() { return this.registerForm.get('password')?.value || ''; }
  get hasLength() { return this.pwd.length >= 8; }
  get hasUpper() { return /[A-Z]/.test(this.pwd); }
  get hasLower() { return /[a-z]/.test(this.pwd); }
  get hasNumber() { return /\d/.test(this.pwd); }
  get hasSpecial() { return /[^a-zA-Z0-9]/.test(this.pwd); }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  showToast(msg: string, success: boolean) {
    this.toastMessage = msg;
    this.isSuccessToast = success;
    setTimeout(() => this.toastMessage = '', 3000);
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        next: (response) => {
          this.showToast('Cont creat cu succes! Te redirecționăm...', true);
          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: (err) => {
          this.showToast('A apărut o eroare la creare.', false);
        }
      });
    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}