curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"email": "test@example.com", "password": "password"}'curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"email": "test@example.com", "password": "password"}'