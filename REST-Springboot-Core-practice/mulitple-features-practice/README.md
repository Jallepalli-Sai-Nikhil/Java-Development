# Spring Boot Profiling Practice - User Service

A comprehensive Spring Boot application demonstrating **profiling** concepts including dynamic profiling, environment-specific configurations, terminal-based settings, and fallback mechanisms.

---

## Table of Contents

1. [What is Spring Boot Profiling?](#what-is-spring-boot-profiling)
2. [How Profiling Works](#how-profiling-works)
3. [Dynamic Profiling](#dynamic-profiling)
4. [Default Fallback Values](#default-fallback-values)
5. [Terminal-Based Settings](#terminal-based-settings)
6. [Switching Profiles](#switching-profiles)
7. [Profile Configuration in This Project](#profile-configuration-in-this-project)
8. [Running the Application](#running-the-application)
9. [Best Practices](#best-practices)
10. [Troubleshooting](#troubleshooting)

---

## What is Spring Boot Profiling?

**Spring Boot Profiling** is a mechanism to manage different configuration sets for different deployment environments. Instead of changing code or manually managing configuration files, you can define environment-specific configurations and Spring Boot automatically loads the appropriate one based on the active profile.

### Why Profiling?

- **Environment Separation**: Separate configs for Development, Testing, Staging, and Production
- **Security**: Different credentials and database URLs per environment
- **Performance**: Different logging levels and optimization settings per environment
- **Flexibility**: Quick environment switches without code changes
- **Consistency**: Predictable behavior across deployments

---

## How Profiling Works

### The Profiling Workflow

```
┌─────────────────────────────────────────────────────────────┐
│                 Spring Boot Application Start                │
└────────────────────────┬────────────────────────────────────┘
                         │
         ┌───────────────┼───────────────┐
         │               │               │
         ▼               ▼               ▼
    environment    command line    application.yml
     variables     arguments      (default profile)
         │               │               │
         └───────────────┼───────────────┘
                         │
                         ▼
        ┌──────────────────────────────┐
        │  Determine Active Profile(s) │
        └──────────────┬───────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
    application.yml  application-dev.yml  application-prod.yml
                         │              │
                         └──────────────┘
                                │
                                ▼
                   ┌────────────────────────┐
                   │  Load Merged Config    │
                   │  (Profile-specific    │
                   │   overrides defaults)  │
                   └────────────────────────┘
                                │
                                ▼
                   ┌────────────────────────┐
                   │  Application Ready     │
                   │  with Environment     │
                   │  Specific Settings     │
                   └────────────────────────┘
```

### Configuration Priority (Highest to Lowest)

1. **Command-line arguments** (highest priority)
2. **Environment variables**
3. **Profile-specific configuration files** (`application-{profile}.yml`)
4. **Default application.yml file** (lowest priority)

### File Loading Order

When you run Spring Boot with profile `dev`:

```
1. application.yml (loaded first - base configuration)
   └─ spring.profiles.active: dev
   └─ spring.application.name: user-service
   └─ spring.jpa.show-sql: true

2. application-dev.yml (loaded next - overrides matching properties)
   └─ spring.datasource.url (overrides if present)
   └─ spring.datasource.username (overrides if present)
   └─ spring.jpa.show-sql: true (overrides)

3. Final Configuration = application.yml + application-dev.yml merged
```

---

## Dynamic Profiling

Dynamic profiling allows you to change the active profile **at runtime** without modifying code or recompiling.

### Methods to Set Active Profile

#### 1. **Default Profile (in application.yml)**

```yaml
# application.yml
spring:
  profiles:
    active: dev  # Default active profile
```

When no active profile is specified, Spring Boot uses `dev`.

#### 2. **Environment Variables**

Set the environment variable before running:

```powershell
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE = "prod"
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar

# Or use set (older PowerShell)
set SPRING_PROFILES_ACTIVE=prod
```

#### 3. **Command-Line Arguments (Highest Priority)**

```powershell
# When running from JAR
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

# When running from Maven
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

#### 4. **System Properties**

```powershell
java -Dspring.profiles.active=test -jar profilingpractice-0.0.1-SNAPSHOT.jar
```

#### 5. **application.properties (Alternative to YAML)**

```properties
# application.properties
spring.profiles.active=dev
```

---

## Default Fallback Values

Fallback values are default values that Spring Boot uses when an environment variable or property isn't explicitly set.

### Syntax for Fallback Values

```yaml
spring:
  datasource:
    url: ${VARIABLE_NAME:default_value}
    username: ${USERNAME:root}
    password: ${PASSWORD:password123}
```

**How it works:**
- If environment variable `VARIABLE_NAME` exists → use it
- If environment variable doesn't exist → use `default_value`

### Example from This Project

#### Development Profile (application-dev.yml)

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/user_dev?createDatabaseIfNotExist=true}
    #     ^       │       ^
    #     │       │       └─ FALLBACK VALUE (used if ENV var not set)
    #     │       └─ Environment Variable Name
    #     └─ Placeholder Syntax
    
    username: ${DB_USERNAME:root}          # Falls back to 'root'
    password: ${DB_PASSWORD:Nikhil@420}    # Falls back to 'Nikhil@420'
  jpa:
    show-sql: true
```

#### Production Profile (application-prod.yml)

```yaml
spring:
  datasource:
    url: ${DB_URL}              # NO fallback - MUST be provided
    username: ${DB_USERNAME}    # NO fallback - MUST be provided
    password: ${DB_PASSWORD}    # NO fallback - MUST be provided
  jpa:
    show-sql: false             # Disabled for production
```

**Production Difference**: No fallback values! Security best practice - credentials must be explicitly provided.

#### Test Profile (application-test.yml)

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/user_test}  # Test DB
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root123}  # Test password
  jpa:
    show-sql: true  # Show SQL for debugging during tests
```

### When Fallback Values Are Used

```powershell
# Scenario 1: ENV variable NOT set
$env:DB_URL = ""  # or not set at all
java -jar app.jar --spring.profiles.active=dev
# Result: Connects to jdbc:mysql://localhost:3306/user_dev (fallback value)

# Scenario 2: ENV variable IS set
$env:DB_URL = "jdbc:mysql://production-db-server:3306/user_prod"
java -jar app.jar --spring.profiles.active=dev
# Result: Connects to jdbc:mysql://production-db-server:3306/user_prod (ENV variable)
```

---

## Terminal-Based Settings

### Setting Configuration via Terminal

You can override any property from the command line, which is useful for scripting and CI/CD pipelines.

#### Method 1: Using `--property=value`

```powershell
# Override spring.profiles.active
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar `
  --spring.profiles.active=test `
  --spring.datasource.url=jdbc:mysql://testdb:3306/test

# Override logging level
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar `
  --logging.level.root=DEBUG

# Override JPA settings
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar `
  --spring.jpa.show-sql=false `
  --spring.jpa.hibernate.ddl-auto=validate
```

#### Method 2: Using System Properties (with -D)

```powershell
java -Dspring.profiles.active=prod `
  -Dspring.datasource.url=jdbc:mysql://proddb:3306/prod `
  -jar target/profilingpractice-0.0.1-SNAPSHOT.jar
```

#### Method 3: Environment Variables

```powershell
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE = "test"
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/test_db"
$env:SPRING_DATASOURCE_USERNAME = "test_user"
$env:SPRING_DATASOURCE_PASSWORD = "test_pass"

java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar

# Or all in one line
$env:SPRING_PROFILES_ACTIVE = "test"; $env:DB_USERNAME = "testuser"; java -jar app.jar
```

#### Method 4: Maven with System Properties

```powershell
# Run with Maven and specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"

# Multiple arguments
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev --server.port=8090"
```

### Creating a Profile Script

Create a PowerShell script to easily switch profiles:

**File: `run-dev.ps1`**
```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "Nikhil@420"
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar
```

**File: `run-prod.ps1`**
```powershell
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DB_URL = "jdbc:mysql://prod-db-server:3306/user_prod"
$env:DB_USERNAME = $args[0]  # Passed as parameter
$env:DB_PASSWORD = $args[1]  # Passed as parameter
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar
```

**Usage:**
```powershell
# Run development
.\run-dev.ps1

# Run production (parameters required)
.\run-prod.ps1 "prod_user" "prod_password"
```

---

## Switching Profiles

### In This Project: Three Available Profiles

#### 1. **Development Profile (dev)**

```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_dev?createDatabaseIfNotExist=true
    username: root
    password: Nikhil@420
  jpa:
    show-sql: true  # Shows all SQL statements
```

**Use Case**: Local development
- Local MySQL database
- Full SQL logging for debugging
- Loose security (hardcoded credentials with fallback)
- Database auto-creation

#### 2. **Test Profile (test)**

```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_test
    username: root
    password: root123
  jpa:
    show-sql: true  # Shows SQL for test debugging
```

**Use Case**: Integration testing
- Separate test database
- SQL logging enabled for test debugging
- Test-specific credentials

#### 3. **Production Profile (prod)**

```yaml
# application-prod.yml
spring:
  datasource:
    url: ${DB_URL}          # Must be provided via ENV
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    show-sql: false  # Disabled for performance
```

**Use Case**: Production deployment
- External database server
- All credentials must be provided via environment variables (security)
- SQL logging disabled for performance
- No fallback credentials exposed

### Switching Profiles: Step-by-Step

#### Step 1: Build the Project

```powershell
cd C:\Users\nikhi\Desktop\CareerDevelopment\REST-Springboot-Core-practice\profilingpractice

# Clean and build
mvn clean package
```

#### Step 2: Run with Development Profile (Default)

```powershell
# Option A: Use default (dev is already set in application.yml)
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar

# Option B: Explicitly specify dev profile
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

**Output:**
```
INFO - Tomcat started on port(s): 8080 (http)
DEBUG - JDBC Connection to: jdbc:mysql://localhost:3306/user_dev (with show-sql: TRUE)
```

#### Step 3: Run with Test Profile

```powershell
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

# Or via environment variable
$env:SPRING_PROFILES_ACTIVE = "test"
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar
```

**Output:**
```
INFO - Tomcat started on port(s): 8080 (http)
DEBUG - JDBC Connection to: jdbc:mysql://localhost:3306/user_test (with show-sql: TRUE)
```

#### Step 4: Run with Production Profile

```powershell
# Must provide environment variables
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DB_URL = "jdbc:mysql://production-server:3306/user_prod"
$env:DB_USERNAME = "prod_user"
$env:DB_PASSWORD = "secure_password_123"

java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar
```

**Output:**
```
INFO - Tomcat started on port(s): 8080 (http)
WARN - JDBC Connection to: jdbc:mysql://production-server:3306/user_prod (show-sql: FALSE)
```

#### Step 5: Switch Between Profiles While IDE is Running

If using Maven from IDE:

```powershell
# Switch to test profile and run
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"

# Switch to prod profile and run
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

## Profile Configuration in This Project

### Project Structure

```
profilingpractice/
├── src/main/resources/
│   ├── application.yml              # Base config + default profile
│   ├── application-dev.yml          # Development profile
│   ├── application-prod.yml         # Production profile
│   ├── application-test.yml         # Test profile
│   └── ...
├── pom.xml                          # Maven config (Spring Boot 4.0.6)
└── ...
```

### Application.yml (Base Configuration)

```yaml
spring:
  application:
    name: user-service
  
  profiles:
    active: dev  # ← Default active profile
  
  jpa:
    hibernate:
      ddl-auto: update  # Apply to all profiles
    show-sql: true      # Override in prod profile
    properties:
      hibernate:
        format_sql: true
```

**Key Points:**
- `spring.profiles.active: dev` sets the default profile
- These settings apply to all profiles unless overridden
- Profile-specific files override base properties

### How Spring Boot Loads Configuration

1. **Load base** `application.yml` properties
2. **Identify active profile** from:
   - Default in `application.yml` (dev)
   - Environment variable `SPRING_PROFILES_ACTIVE`
   - Command-line argument `--spring.profiles.active=xxx`
3. **Load** `application-{profile}.yml` (e.g., `application-dev.yml`)
4. **Merge** with profile-specific values overriding base config
5. **Apply** environment variable substitution with fallback values

### Example: Merged Configuration for Dev Profile

```yaml
# Resulting in-memory configuration after merging:

spring:
  application:
    name: user-service
  profiles:
    active: dev
  datasource:
    url: jdbc:mysql://localhost:3306/user_dev?createDatabaseIfNotExist=true
    username: root
    password: Nikhil@420
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true          # ← From application-dev.yml
    properties:
      hibernate:
        format_sql: true
```

---

## Running the Application

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 5.7+ (for dev/test profiles)

### Quickstart: Using Maven

```powershell
# Navigate to project
cd C:\Users\nikhi\Desktop\CareerDevelopment\REST-Springboot-Core-practice\profilingpractice

# Run with dev profile (default)
mvn spring-boot:run

# Run with test profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"

# Run with prod profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Running as JAR

```powershell
# Build
mvn clean package

# Run with dev profile (default)
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar

# Run with custom port
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar --server.port=8090

# Run with test profile and custom settings
java -jar target/profilingpractice-0.0.1-SNAPSHOT.jar `
  --spring.profiles.active=test `
  --server.port=8091
```

### Verifying Active Profile

Check logs after startup:

```
2025-06-01 10:30:45.123  INFO 12345 --- [main] s.b.w.e.t.TomcatWebServer : Tomcat initialized with port(s): 8080
2025-06-01 10:30:45.456  INFO 12345 --- [main] c.e.p.ProfilingpracticeApplication : Started ProfilingpracticeApplication in 2.345 seconds
```

Check active profile via endpoint (if you add an actuator):

```powershell
curl http://localhost:8080/actuator/env | findstr "ACTIVE"
```

---

## API Endpoints

Once running, access the User Service:

```powershell
# Create user (POST)
curl -X POST http://localhost:8080/api/users `
  -H "Content-Type: application/json" `
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "city": "New York"
  }'

# Get all users
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/1

# Update user
curl -X PUT http://localhost:8080/api/users/1 `
  -H "Content-Type: application/json" `
  -d '{
    "name": "Jane Doe",
    "city": "Boston"
  }'

# Delete user
curl -X DELETE http://localhost:8080/api/users/1
```

---

## Best Practices

### 1. **Never Commit Sensitive Data to Version Control**

```yaml
# ✗ BAD - Credentials in file
spring:
  datasource:
    username: admin
    password: MySecurePassword123!

# ✓ GOOD - Use environment variables
spring:
  datasource:
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### 2. **Use Appropriate Defaults**

```yaml
# Dev: Safe defaults for local development
${DB_USERNAME:root}

# Prod: No defaults - must be explicitly provided
${DB_USERNAME}  # Will fail if ENV var not set
```

### 3. **Profile-Specific Security Settings**

```yaml
## development - Show full details for debugging
spring:
  jpa:
    show-sql: true

## production - Hide for security and performance
spring:
  jpa:
    show-sql: false
```

### 4. **Document Profile Behaviors**

Create profile documentation:

```
PROFILES IN THIS PROJECT:
- dev  : Local development with fallback credentials
- test : Integration testing with test database
- prod : Production with required environment variables
```

### 5. **Multiple Profiles Simultaneously**

Activate multiple profiles:

```powershell
# Comma-separated
java -jar app.jar --spring.profiles.active=prod,metrics,security

# Or via environment variable
$env:SPRING_PROFILES_ACTIVE = "prod,metrics"
java -jar app.jar
```

### 6. **Profile-Specific Beans**

```java
// Example: Different DataSource for different profiles
@Configuration
public class DataSourceConfig {
    
    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new LocalDataSource();  // Local H2
    }
    
    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return new RemoteDataSource();  // External DB
    }
}
```

---

## Common Issues & Troubleshooting

### Issue 1: Wrong Profile Active

**Problem**: Application connects to prod database when it should use dev

**Solution**: Check profile precedence
```powershell
# Verify which profile is active
java -jar app.jar --spring.profiles.active=dev

# Or check logs for: "The following profiles are active: dev"
```

### Issue 2: Environment Variable Not Recognized

**Problem**: `${DB_URL}` not being substituted

**Solution**: Ensure proper syntax and naming
```powershell
# Wrong
$env:DB-URL = "value"  # Hyphens not valid in PowerShell env vars

# Correct
$env:DB_URL = "value"

# Verify it's set
Write-Host $env:DB_URL
```

### Issue 3: Fallback Value Not Used

**Problem**: Getting connection error instead of fallback value

**Causes**:
- Environment variable is set (takes precedence over fallback)
- Typo in fallback syntax: should be `${VAR:fallback}` not `${VAR}:fallback`
- Profile not loaded correctly

**Solution**:
```yaml
# Verify syntax
spring.datasource.url: ${DB_URL:jdbc:mysql://localhost:3306/mydb}
#                                 ^ colon separates variable and fallback

# Check if ENV var is interfering
Get-ChildItem env: | Where-Object { $_.Name -eq 'DB_URL' }
```

### Issue 4: Properties Not Overriding

**Problem**: Command-line argument not taking effect

**Solution**: Use correct syntax
```powershell
# Wrong - property name missing
java -jar app.jar --active=dev

# Correct - full property name
java -jar app.jar --spring.profiles.active=dev
```

### Issue 5: Database Connection Fails

**Problem**: Can't connect to database in dev profile

**Solution**: Check configuration fallback chain
```powershell
# Manually verify fallback values work
$env:DB_URL = ""  # Clear if set
$env:DB_USERNAME = ""
$env:DB_PASSWORD = ""

# Should use fallback: localhost:3306/user_dev
java -jar app.jar --spring.profiles.active=dev

# Verify database exists
mysql -u root -p -e "SHOW DATABASES;"
```

---

## Environment Variable Reference

### Variable Names (Spring Boot converts to uppercase with underscores)

| Config Property | Environment Variable |
|-----------------|----------------------|
| `spring.profiles.active` | `SPRING_PROFILES_ACTIVE` |
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` |
| `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` |
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` |
| `server.port` | `SERVER_PORT` |
| `spring.jpa.show-sql` | `SPRING_JPA_SHOW_SQL` |

### Setting Multiple Variables (PowerShell)

```powershell
# Method 1: Individual lines
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DB_URL = "jdbc:mysql://proddb:3306/prod"
$env:DB_USERNAME = "admin"
$env:DB_PASSWORD = "secret123"

# Method 2: One-liner with semicolons
$env:SPRING_PROFILES_ACTIVE="prod"; $env:DB_URL="jdbc:mysql://proddb:3306/prod"; java -jar app.jar

# Method 3: Create script file and source it
# File: env-prod.ps1
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DB_URL = "jdbc:mysql://proddb:3306/prod"
$env:DB_USERNAME = "admin"
$env:DB_PASSWORD = "secret123"

# Usage:
. .\env-prod.ps1
java -jar app.jar
```

---

## Advanced: Creating Custom Profiles

To add a new profile (e.g., `staging`):

1. **Create configuration file**: `application-staging.yml`

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://staging-server:3306/user_staging}
    username: ${DB_USERNAME:staging_user}
    password: ${DB_PASSWORD:staging_pass}
  jpa:
    show-sql: false  # Production-like but some debug info
```

2. **Activate it**:

```powershell
java -jar app.jar --spring.profiles.active=staging
```

3. **Or set as default** in `application.yml`:

```yaml
spring:
  profiles:
    active: staging  # New default
```

---

## Summary

### Key Takeaways

1. **Profiling** separates configuration by environment (dev/test/prod)
2. **Default Profile**: Set in `application.yml` → currently `dev`
3. **Precedence**: Command-line > Environment Variables > Fallback Values > Base Config
4. **Fallback Syntax**: `${VAR_NAME:default_value}`
5. **Profile Files**: `application-{profile}.yml` automatically loaded
6. **Terminal Switching**: Use `--spring.profiles.active=xxx` or `SPRING_PROFILES_ACTIVE`
7. **Security**: Use environment variables for production credentials

### Quick Commands

```powershell
# Dev (default)
java -jar app.jar

# Test
java -jar app.jar --spring.profiles.active=test

# Production (with env vars)
$env:DB_URL="..."; $env:DB_USERNAME="..."; $env:DB_PASSWORD="..."
java -jar app.jar --spring.profiles.active=prod

# Check active profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

---

## Resources

- [Spring Boot Profiles Documentation](https://spring.io/blog/2011/02/15/spring-tip-externalized-configuration/)
- [Spring Boot Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [12-Factor App: Config](https://12factor.net/config)
- [Externalized Configuration Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)

---

**Created**: June 1, 2026  
**Project**: Profiling Practice - Spring Boot User Service  
**Author**: Nikhil

