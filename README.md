# Employee-Leave-Hub

A modern employee leave and absence management system designed to streamline leave requests, approvals, tracking, and reporting within an organization.

## Overview

Employee-Leave-Hub helps companies manage employee time-off requests in a structured and transparent way. It allows employees to submit leave requests, managers to review and approve them, and administrators to monitor team availability and analytics.

## Key Features

- Submit leave requests with start and end dates
- Track leave status: pending, approved, rejected
- Support different leave types such as vacation, sick leave, or personal time
- View leave history and employee attendance records
- Manager approval workflow
- Department-based filtering and reporting
- Dashboard with leave statistics and summaries
- Role-based access control
- Responsive and easy-to-use interface

## Tech Stack

| Layer | Technology |
|------|------------|
| Frontend | Angular |
| Language | TypeScript |
| Styling | SCSS / CSS |
| State / HTTP | RxJS + Angular Services |
| Build Tool | Angular CLI |
| Runtime | Node.js |

## Project Structure

```text
Employee-Leave-Hub/
├── Frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   ├── services/
│   │   │   ├── models/
│   │   │   ├── guards/
│   │   │   └── app.module.ts
│   │   ├── assets/
│   │   ├── styles/
│   │   ├── index.html
│   │   └── main.ts
│   ├── angular.json
│   ├── package.json
│   ├── tsconfig.json
│   └── README.md
├── Backend/
│   ├── src/
│   ├── package.json
│   └── README.md
├── README.md
└── .gitignore
```

## Prerequisites

Before running the application, ensure you have the following installed:

- Node.js 18 or newer
- npm
- Angular CLI
- A working backend API for leave management data
- Modern browser (Chrome, Edge, Firefox, Safari)

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/iSergiuu/Employee-Leave-Hub.git
cd Employee-Leave-Hub
```

### 2. Install frontend dependencies

```bash
cd Frontend
npm install
```

### 3. Install Angular CLI globally (optional)

```bash
npm install -g @angular/cli
```

## Running the App

### Development mode

```bash
cd Frontend
npm start
```

Or:

```bash
cd Frontend
ng serve
```

Then open the application in your browser at:

```text
http://localhost:4200
```

### Production build

```bash
cd Frontend
ng build --configuration production
```

The build output will be generated in the `dist/` folder.

## Environment Configuration

Create environment files in the frontend project if needed.

Example for `src/environments/environment.ts`:

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  appName: 'Employee-Leave-Hub',
  appVersion: '1.0.0'
};
```

Example for production:

```ts
export const environment = {
  production: true,
  apiUrl: 'https://api.example.com/api',
  appName: 'Employee-Leave-Hub',
  appVersion: '1.0.0'
};
```

## API Integration

This frontend expects a backend API that exposes endpoints such as:

### Authentication

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "employee@example.com",
  "password": "password123"
}
```

### Leave requests

```http
GET /api/leave-requests
Authorization: Bearer {token}

POST /api/leave-requests
Authorization: Bearer {token}
Content-Type: application/json

{
  "startDate": "2026-10-10",
  "endDate": "2026-10-14",
  "leaveType": "annual",
  "reason": "Planned vacation"
}
```

### Approve or reject request

```http
PATCH /api/leave-requests/{id}/approve
Authorization: Bearer {token}

{
  "status": "approved",
  "comment": "Approved by manager"
}
```

## User Roles

| Role | Permissions |
|------|------------|
| Employee | Submit leave requests, view personal requests |
| Manager | Review requests in their department, approve/reject |
| Admin | Full access and system management |

## Testing

### Run unit tests

```bash
cd Frontend
ng test
```

### Lint project

```bash
cd Frontend
ng lint
```

## Troubleshooting

### Port 4200 already in use

```bash
# Linux/macOS
lsof -i :4200
kill -9 <PID>

# Windows
netstat -ano | findstr :4200
taskkill /PID <PID> /F
```

Or run Angular on another port:

```bash
ng serve --port 4201
```

### Module not found or dependencies missing

```bash
rm -rf node_modules package-lock.json
npm install
```

### API connection issues

- Check whether the backend server is running
- Verify the API URL in the Angular environment file
- Make sure CORS is configured correctly on the backend

### Angular CLI not found

```bash
npm install -g @angular/cli
```

## Contribution

We welcome contributions.

```bash
git checkout -b feature/new-feature
git commit -m "Add new feature"
git push origin feature/new-feature
```

Then open a pull request on GitHub.

## License

This project is intended for educational and internal business use.

## Support

If you encounter any issues, open a GitHub issue or contact the project maintainer.

---

Last updated: October 2026
Version: 1.0.0
