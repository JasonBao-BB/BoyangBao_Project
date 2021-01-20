# All modified codes are done by Boyang Bao. Plagiarism is prohibited!
# Improved codes and features
- Implemented Excel service data layer with Spring Data JPA + Hibernate + SQL database
- Connected and stored Excel files into S3 bucket
- Modified Excel files downloads from local to AWS S3 bucket
- Implemented CRUD functions in PDF service with MongoDB and Spring Data MongoDB
- Reconstructed whole project to microservices with Eureka and Ribbon
- Implemented authentication and authorization with OAuth2 + JWT
- Fixed some bugs and irregular code writing

# Micro-services
Eureka-Server: http://localhost:2001

Oauth2-Authorization-Server: http://localhost:4001

Client-Service: http://localhost:8080

Excel-Service: http://localhost:8888

PDF-Service: http://localhost:9999

## How to Login with OAuth2?
Authorization Code 

http://localhost:4001/oauth/authorize?client_id=reporting_system&response_type=code&scope=all

Generate JWT Token

http://localhost:4001/oauth/token?client_id=reporting_system&client_secret=secret&grant_type=authorization_code&code=[YOUR_CODE]

Access client service with JWT Token

http://localhost:8080/?access_token=[YOUR_TOKEN]
