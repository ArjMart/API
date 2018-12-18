# API
This is the REST Api for ArjMart. It uses Jersey (JAX-RS) as its framework. The backend is a JDBC Database (currently MySQL but abstracted away behind HikariCP).

## Running this yourself
First and foremost, add your database info to 'src/jdbc.properties'. The application will not start without that. Second, compile a war file and deploy it to your application server of choice. Third, sit back and enjoy a fully working E-Commerce API system.

## Technologies
- JAX-RS
- Jackson JSON
- JDBC (MySQL)
- HikariCP
- HK2 Dependency Injection
- Slf4j (logging framework)
- JMS (ActiveMQ backend)

## Best Practices
- All database logic is abstracted away into DAOs
- HK2 Dependency Injection encapsulates logic into interfaces for DAOs and implementations
- Connections to database are pooled for maximum performance
- Authentication and authorization logic are abstracted into a filter and used with simple annotations
- Exception handling is delegated to dedicated ExceptionMappers so resource methods only handle best-case scenarios

## Unit Testing
- Everything is very testable. DAOs can be replaced with mocks to test resource methods, and Jersey can handle everything else. DAOs themselves can be tested via giving them a mocked ConnectionFactory with connections to a fake in-memory database.

## Questions?
Email me at arjvik@gmail.com
