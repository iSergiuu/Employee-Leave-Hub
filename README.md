# Employee Leave Hub

Employee Leave Hub is a full-stack leave-management application built with Angular and Spring Boot. It provides a centralized workflow for employees to submit leave requests and for managers and administrators to review, monitor, and manage organizational leave data.

## Key Features

- Employee registration and login
- Role-based dashboards for employees, managers, and administrators
- Submission and tracking of leave requests
- Employee request history
- Manager review and approval workflow
- Administrative management tools
- Department and leave-type reporting
- Calendar view for leave planning
- JWT-based backend authentication
- PDF report generation support
- Responsive Angular interface

## Technology Stack

| Area | Technology |
|---|---|
| Frontend | Angular 21 |
| Frontend language | TypeScript |
| Styling | SCSS |
| Calendar | FullCalendar |
| Frontend testing | Vitest, jsdom |
| Backend | Spring Boot 4.1 |
| Backend language | Java 25 |
| Security | Spring Security and JWT |
| Data access | Spring Data JPA |
| Database | Oracle Database |
| Documentation | SpringDoc OpenAPI |
| PDF generation | OpenPDF |
| Build tools | Angular CLI, Maven |

## Project Structure

```text
Employee-Leave-Hub/
├── Frontend/
│   ├── public/
│   ├── src/
│   │   └── app/
│   │       ├── layout/
│   │       ├── pages/
│   │       │   ├── login/
│   │       │   ├── register/
│   │       │   ├── employee-dashboard/
│   │       │   ├── manager-dashboard/
│   │       │   ├── admin-dashboard/
│   │       │   ├── my-requests/
│   │       │   └── calendar/
│   │       ├── services/
│   │       └── app.routes.ts
│   ├── angular.json
│   └── package.json
├── Backend/
│   ├── src/
│   │   ├── main/java/
│   │   ├── main/resources/
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
├── CREATE TABLE departament (.sql
├── CREATE USER leave_hub_user IDENTIFIED BY.sql
└── README.md
```

## Prerequisites

Install the following tools before running the project:

- Node.js 20 or newer
- npm
- Angular CLI
- Java JDK 25
- Maven or the included Maven Wrapper
- Oracle Database

## Database Setup

The repository includes SQL scripts for initializing the database:

- `CREATE TABLE departament (.sql`
- `CREATE USER leave_hub_user IDENTIFIED BY.sql`

1. Create an Oracle database or connect to an existing instance.
2. Run the provided SQL scripts.
3. Verify that the `leave_hub_user` account can access the database.
4. Update `Backend/src/main/resources/application.properties` if your database configuration differs:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/FREEPDB1
spring.datasource.username=leave_hub_user
spring.datasource.password=your-password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

> Avoid storing real passwords in source control. Use environment variables or an external configuration file for production deployments.

## Backend Setup

```bash
cd Backend
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

The backend starts through Spring Boot. The available API documentation may be exposed through SpringDoc/Swagger after the application starts.

## Frontend Setup

```bash
cd Frontend
npm install
npm start
```

Alternatively:

```bash
ng serve
```

Open the application at:

```text
http://localhost:4200
```

## Frontend Routes

| Route | Purpose |
|---|---|
| `/login` | User authentication |
| `/register` | Account registration |
| `/employee` | Employee dashboard |
| `/manager` | Manager dashboard |
| `/admin` | Administration dashboard |
| `/my-requests` | Employee leave requests |
| `/calendar` | Leave calendar |

## Available Scripts

### Frontend

```bash
npm start      # Start the development server
npm run build  # Create a production build
npm test       # Run unit tests
```

### Backend

```bash
./mvnw test
./mvnw package
./mvnw spring-boot:run
```

## API and Backend Structure

The backend uses a Spring Boot architecture with:

- REST controllers
- JPA entities and repositories
- Spring Security authentication
- JWT token generation and validation
- Oracle database integration
- OpenPDF report support
- Swagger/OpenAPI documentation

## Security Considerations

Before deploying the application:

- Replace the default database credentials
- Use strong JWT signing secrets
- Restrict CORS origins
- Enable HTTPS
- Configure environment-specific secrets
- Add route guards and role validation consistently
- Review SQL scripts before executing them in a production database

## Production Build

### Frontend

```bash
cd Frontend
npm run build
```

The generated build is placed in the Angular `dist` directory.

### Backend

```bash
cd Backend
./mvnw clean package
```

The packaged JAR is generated in `Backend/target`.

## Troubleshooting

### Oracle connection fails

- Confirm that Oracle Listener is running.
- Verify the hostname, port, service name, username, and password.
- Ensure the database user has been granted the required permissions.

### Frontend cannot connect to the backend

- Confirm that the Spring Boot server is running.
- Verify the API base URL in the frontend services.
- Check CORS configuration in the backend.

### Angular dependencies fail to install

```bash
cd Frontend
rm -rf node_modules package-lock.json
npm install
```

## License

No license has been specified for this project.

## Repository

[Employee-Leave-Hub](https://github.com/iSergiuu/Employee-Leave-Hub)
