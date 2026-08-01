import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common'; 
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterModule, CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  
  // Definim formularul și validările
  registerForm = new FormGroup({
    fullname: new FormControl('', [Validators.required, Validators.minLength(3)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^a-zA-Z0-9]).{8,}/) 
    ])
  });

  // Funcția apelată pe buton
  onSubmit() {
    if (this.registerForm.valid) {
      console.log('Date valide de trimis spre Java:', this.registerForm.value);
    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}