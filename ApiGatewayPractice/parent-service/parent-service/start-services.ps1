Write-Host "Starting Microservices..." -ForegroundColor Green

Start-Process powershell -ArgumentList "cd eureka-server; mvn spring-boot:run"
Start-Process powershell -ArgumentList "cd api-gateway; mvn spring-boot:run"
Start-Process powershell -ArgumentList "cd user-service; mvn spring-boot:run"
Start-Process powershell -ArgumentList "cd order-service; mvn spring-boot:run"

Write-Host "All services started 🚀"


# .\start-services.ps1