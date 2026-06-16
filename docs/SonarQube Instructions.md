-------------------------
SonarQube Configurations:
-------------------------

- Login: http://localhost:9000
- Default Name: admin
- Default Password: admin

- Create a Manually Project:
    - Name: vprofile
    - Project Settings -> Webhook:
        - Name: jenkins
        - URL: http://host.docker.internal:8080/sonarqube-webhook/

- Add Jenkins Token:
    - My Account -> Security -> Generate Token
    - Copy the Generated Token into Jenkins Credencials


-----------------------
Jenkins Configurations:
-----------------------

- Manage Jenkins -> Credentials -> Secret Text:
    - Secret: Generated Token from SonarQube
    - ID: sonarqube-token

- Manage Jenkins -> Tools:
    - Add SonarQube Scanner: 
        - Name: SonarScanner
        - Check Install automatically

- System:
    - SonarQube Servers -> Add SonarQube:
        - Name: sonarqube
        - URL: http://host.docker.internal:9000
        - Server authentication token: sonarqube-token