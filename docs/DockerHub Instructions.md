-------------------------
DockerHub Configurations:
-------------------------


- Account settings -> Personal access tokens -> Generate new token:

    - My Account -> Security -> Generate Token
        - Access token description: vprofile token
        - Access permissions: Read & Write
    - Copy the Generated Token into Jenkins Credencials


-----------------------
Jenkins Configurations:
-----------------------

- Manage Jenkins -> Credentials -> Username with password:
    - Username: DockerHub Username
    - Password: Generated Token from GitHub
    - ID: dockerhub-token
