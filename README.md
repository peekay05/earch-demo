Setting up the API

The Search API uses lucene ( V6 ) for indexing, searching and autocomplete features.  It uses Spring Boot for creating the service layer.
Lucene was used to keep the application simple and make a single executable jar for the application.  


Requirements: 

Gradle should be installed in the system to build the JAR


1. Go inside the API folder
2. Execute:  gradle build
     This will create a executable jar in the folder build/lib with name api.jar
     
3. The jar can be run using the command

    java -jar  build/libs/api.jar

    The jar reads the properties from the application.properties file present inside the config folder. 
    Edit the properties in the application.properties file before executing the jar
    
    The above command will work only when run from the API root dir as it requires the config folder to be located  in the same folder where the command is executed. 
    If running from any other location, please copy the config folder located inside the API root folder to the directory where the command is to be executed. 
    
   The application has the following settings:
   //The following 3 settings need to be changed to point to appropriate files
   searchapp.keyfile=dataDir/keyfile.txt   // Path to the key file. File Format:  key, allowed_requests_per_second 
   searchapp.hotelDb1FilePath=dataDir/hotels.csv  //Path to the first hotel database
   searchapp.hotelDb2FilePath=dataDir/hoteldb.csv //Path to the second hotel database
   
   //The settings below need not be changed. This will create the indexes in the directory from where the jar was executed
   searchapp.hotelDb1IndexDir=indexDir/hoteldb1
   searchapp.hotelDb2IndexDir=indexDir/hoteldb2
   searchapp.autoCompleteIndexDir=indexDir/autoComplete
    
    
    
    Setting up the UI 