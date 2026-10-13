# ShiftDesk
Spring Boot 3 + JPA + MySQL REST API (`shiftdesk-api`, port 8080) and React + Vite UI (`shiftdesk-ui`, port 5173).

API: `mvn spring-boot:run` (set MySQL user/password in application.properties; schema and sample data are created on first run)
UI:  `npm install && npm run dev`

Endpoints: GET /api/stores, GET /api/employees, GET /api/stores/{id}/shifts,
POST /api/shifts (manager only), POST /api/shifts/{id}/claim?employeeId=
