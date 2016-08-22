Setting up the API

Requirements: 

Gradle should be installed in the system

1. Go inside the api folder
2. Execute:  gradle build
     This will create a executable jar in the folder build/lib with name api.jar
     
3. The jar can be run using the command
    java -jar  build/libs/api.jar

    The jar reads the properties from the application.properties file present inside the config folder. 
    Edit the properties in the application.properties file before executing the jar