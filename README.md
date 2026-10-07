# 👤 Employee-Leave-Hub

Un sistem modern de gestionare a concediilor și a absenteismului angajaților, construit pentru a centraliza cererile de concediu, aprobări și urmărire istoricului.

---

## 📝 Descriere

**Employee-Leave-Hub** este o aplicație web care permite:
- ✅ Introducerea și gestionarea cererilor de concediu
- ✅ Urmărirea statusului fiecărei solicitări (în așteptare, aprobată, respingă)
- ✅ Aprobarea sau respingerea cererilor de concediu
- ✅ Vizualizarea unui calendar interactiv cu concediile aprobate
- ✅ Filtrare după departament, status și utilizator
- ✅ Dashboard statistic cu rezumate
- ✅ Exportul rapoartelor de concediu

Proiectul este ideal pentru departamentele HR și pentru organizații care doresc să automatizeze procesul de gestionare a concediilor.

---

## ✨ Funcționalități principale

### 📋 Gestionare cereri
- Creare cereri de concediu cu dată de început și sfârșit
- Selectare tip concediu (anual, boală, plată)
- Comentarii și observații
- Validare automată a cererilor

### 👁️ Urmărire status
- Vizualizare status în timp real: **în așteptare**, **aprobată**, **respingă**
- Notificări pentru schimbări de status
- Istoricul acțiunilor și comentariilor

### 📅 Calendar interactiv
- Vizualizare grafică a concediilor pe calendar
- Filtrare după departament sau utilizator
- Identificare perioadelor libere

### 📊 Dashboard statistic
- Total zile de concediu utilizate
- Zile rămase
- Distribuție pe departamente
- Grafice și statistici

### 🔐 Autentificare și roluri
- **Employee**: poate crea și urmări propriile cereri
- **Manager**: poate aproba/respinge cereri din departament
- **Admin**: control complet

---

## 🛠️ Tehnologii utilizate

| Componenta | Tehnologie | Versiune |
|-----------|-----------|----------|
| **Frontend** | Angular | 21.2.19 |
| **Limbaj** | TypeScript | Latest |
| **Styling** | SCSS / CSS | - |
| **HTTP Client** | RxJS | Latest |
| **Build Tool** | Angular CLI | 21.2.19 |
| **Runtime** | Node.js | 18+ |

---

## 📁 Structura proiectului

```
Employee-Leave-Hub/
├── Frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/          # Componente reutilizabile
│   │   │   ├── pages/               # Pagini principale
│   │   │   ├── services/            # Servicii API
│   │   │   ├── models/              # Modele de date
│   │   │   ├── guards/              # Route guards
│   │   │   └── app.module.ts
│   │   ├── assets/                  # Imagini, fonturi, etc.
│   │   ├── styles/                  # Stil global
│   │   ├── index.html
│   │   └── main.ts
│   ├── angular.json
│   ├── package.json
│   ├── tsconfig.json
│   ├── README.md
│   └── .gitignore
├── Backend/
│   ├── src/
│   ├── package.json
│   └── README.md
├── README.md
└── .gitignore
```

---

## 📋 Cerințe preliminare

### Frontend
- **Node.js** 18 sau mai nouă
  - Download: https://nodejs.org/
  - Verificare: `node --version`

- **npm** (vine cu Node.js)
  - Verificare: `npm --version`

- **Angular CLI** (se instalează automat)
  - Sau manual: `npm install -g @angular/cli`

### Backend (dacă este)
- Un API server care oferă date (Node.js, Spring Boot, etc.)

### Browser
- Chrome, Firefox, Safari, Edge (versiuni recente)

---

## 🚀 Instalare și Setup

### 1️⃣ Clonare repository

```bash
git clone https://github.com/iSergiuu/Employee-Leave-Hub.git
cd Employee-Leave-Hub
```

### 2️⃣ Setup Frontend

#### Navigare în directorul frontend
```bash
cd Frontend
```

#### Instalare dependențe
```bash
npm install
```

Aceasta va descărca toți pachetele necesare în folder-ul `node_modules/`.

#### (Opțional) Instalare Angular CLI global
```bash
npm install -g @angular/cli
```

---

## 🏃 Pornirea aplicației

### Development Mode (cu Hot Reload)

```bash
cd Frontend
npm start
```

Sau:
```bash
ng serve
```

**Output:**
```
✔ Compiled successfully.
✔ Build at: 2024-01-15T10:30:00.000Z - Hash: abc123def456

Application bundle generation complete. [2.345 seconds]

Initial Chunk Files | Names | Raw Size | Estimated Transfer Size
main.js              |       | 456 kB  | 120 kB
polyfills.js         |       | 128 kB  | 40 kB
styles.css           |       | 45 kB   | 12 kB

** Angular Live Development Server is listening on localhost:4200 **
```

### Accesare în browser

Deschide în browser:
```
http://localhost:4200
```

### Production Build

```bash
ng build --configuration production
```

Aceasta generează folder-ul `dist/` pregătit pentru desfășurare.

---

## ⚙️ Configurare variabile de mediu

### Frontend

Creează fișierul `Frontend/src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',  // URL backend
  appName: 'Employee-Leave-Hub',
  appVersion: '1.0.0'
};
```

Pentru producție, editează `environment.prod.ts`:

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.example.com/api',  // URL producție
  appName: 'Employee-Leave-Hub',
  appVersion: '1.0.0'
};
```

---

## 📡 API Integration

Aplicația se conectează la un backend care oferă endpoints:

### Autentificare
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "123",
    "name": "John Doe",
    "role": "employee"
  }
}
```

### Cereri de concediu
```http
GET /api/leave-requests
Authorization: Bearer {token}

POST /api/leave-requests
Authorization: Bearer {token}
Content-Type: application/json

{
  "startDate": "2024-02-01",
  "endDate": "2024-02-05",
  "leaveType": "annual",
  "reason": "Vacanță planificată"
}
```

### Aprobare cereri
```http
PATCH /api/leave-requests/{id}/approve
Authorization: Bearer {token}

{
  "status": "approved",
  "comment": "Aprobat"
}
```

---

## 🧪 Testare

### Unit Tests
```bash
ng test
```

### E2E Tests
```bash
ng e2e
```

### Linting
```bash
ng lint
```

---

## 📚 Rol-uri și Permisiuni

| Rol | Permisiuni |
|-----|-----------|
| **Employee** | Creare cereri, vizualizare propriile cereri, ștergere propriile cereri (dacă sunt în așteptare) |
| **Manager** | Employee + aprobare/respingere cereri din departament, vizualizare statistici departament |
| **Admin** | Acces complet - management utilizatori, departamente, rapoarte globale |

---

## 🐛 Troubleshooting

### Problema: "Cannot find module"

**Cauza:** Dependențele nu sunt instalate

**Soluție:**
```bash
rm -rf node_modules package-lock.json
npm install
```

### Problema: "Port 4200 already in use"

**Cauza:** Alt proces folosește portul

**Soluție:**
```bash
# Linux/Mac - Kill process pe port 4200
lsof -i :4200
kill -9 <PID>

# Windows
netstat -ano | findstr :4200
taskkill /PID <PID> /F

# Sau pornește pe alt port
ng serve --port 4201
```

### Problema: "API connection failed"

**Cauza:** Backend-ul nu rulează sau URL-ul e greșit

**Soluție:**
1. Verifică URL-ul din `environment.ts`
2. Asigură-te că backend-ul rulează pe portul specificat
3. Verifică CORS headers în backend

### Problema: "Cannot resolve '@angular/core'"

**Cauza:** Versiune Angular incompatibilă

**Soluție:**
```bash
npm install @angular/core@latest --legacy-peer-deps
```

### Problema: "ng: command not found"

**Cauza:** Angular CLI nu este instalat global

**Soluție:**
```bash
npm install -g @angular/cli@latest
# Sau foloseste npm exec
npx ng serve
```

---

## 📝 Noturi importante

- ⚠️ Niciodată nu commit-ui `environment.prod.ts` cu credențiale reale
- ⚠️ Asigură-te că backend-ul are CORS configurat corect
- ⚠️ JWT tokens au durată limitată - implementează refresh logic
- ⚠️ Verifică browser console pentru erori (F12 → Console)

---

## 🤝 Contribuție

Dacă vrei să contribui:

1. Fork repository-ul
2. Creează o branch pentru feature-ul tău:
   ```bash
   git checkout -b feature/noua-functionalitate
   ```
3. Commit schimburile:
   ```bash
   git commit -m "Adaug funcție nouă X"
   ```
4. Push-ează branch-ul:
   ```bash
   git push origin feature/noua-functionalitate
   ```
5. Deschide un Pull Request pe GitHub

---

## 📄 Licență

Acest proiect este destinat pentru uz educațional și de dezvoltare internă.

---

## 📞 Support

Dacă ai întrebări sau probleme:
1. Verifică secțiunea **Troubleshooting**
2. Deschide o **Issue** pe GitHub
3. Contactează maintainer-ul proiectului

---

## ✅ Verificare Instalare

Rulează aceste comenzi pentru a verifica dacă totul e configurat:

```bash
# Verifică Node.js
node --version

# Verifică npm
npm --version

# Verifică Angular CLI
ng version

# Testează conexiunea la localhost
curl http://localhost:4200
```

---

**Ultima actualizare:** octombrie 2026  
**Versiune:** 1.0.0  
**Status:** ✅ Production Ready
