# Welcome to data-harvester

Data Harvester Microservice (DHM) is a service that collects streaming data from  (ficticious) platforms: Sytflix, Sytazon, Sysney. It is implemented using Springboot and Java 17. DHM is also utilizing the Spring Webflux library in order to allow handling of large number of requests efficiently (ingress and egress). 
It consists of 3 major components interaction: `Controller Layer <-> Service Layer <-> 3 Streaming Sources`. 

See diagram below:
![DHM Service Diagram](src/main/resources/images/dhm_overview.png)

## Project Structure Explained
```
|- data-harvester                  // The project source codes
|- devcase-streaming-readme-main   // The assignment details & instructions
|- README.MD                       // ReadMe file with project overview & developer's guide
```

For data-harvester, the project structure is:

![DHM Project Structure](src/main/resources/images/dhm_projstructure.png)


## How to use DHM API for external users?
Refer to the OpenAPI 3.0 of this microservice to see the its features. Open this file `data-harvester/src/main/resources/dhm.yaml` after cloning the project locally.

Then visit the site https://editor.swagger.io/ or use VSCode with plugin OpenAPI (Swagger) Editor and paste its content. You should see:
![DHM Service Diagram](src/main/resources/images/dhm_yaml.png)

## How to run the microservice locally?
A. Pre-requisites
1. Docker installed locally
2. Run the docker images for the streams provided by Sytac
ie. In MacOS
`docker run -p 8080:8080 sytacdocker/video-stream-server-arm:latest`

B. Steps
1. Download an IDE, in my case I use IntelliJ
2. Download Java 17 from the IDEA
   `Project Structure > Project tab > SDK: click dropdown > Add SDK > Type 17 under version and choose your desired flavor`. I am using JetBrains Runtime.

   ![DHM Service Diagram](src/main/resources/images/idea_add_sdk.png)

3. Then, clone the project by clicking the menu
    - In Java-mavi-quarks (Github), click Code > In the HTTPS tab > Copy the URL: https://github.com/Sytac-DevCase/Java-mavi-quarks.git 
    - In your terminal, paste this and run: `git clone https://github.com/Sytac-DevCase/Java-mavi-quarks.git`. Please take note of the location of the project.
   
4. Launch IDEA Intelij, click Open and then locate the folder where you saved the project (step #3) & go to data-harvester directory.
5. Click the pom file to open and a pop-up will display, select "Open As Project" option.
6. Right click the pom.xml > Maven > click Reload project.
7. You can either, run all its UT or/and run the microservice
8. Test the endpoint using postman or the browser. Type in the address bar `http://localhost:9000/dhm/v1` and run.

![DHM Postman](src/main/resources/images/dhm_postman.png)


## Objects Schema

### StreamResponseStatistics

An example of `StreamResponseStatistics` object:
```
{
    "id": "8630c4ff-507f-415b-a56c-705336678b80",
    "totalShowsReleasedIn2020AndLater": 15,
    "totalStreamingDurationMillis": 20028,
    "percentageStartedStreamingOnSytflix": 69.05,
    "totalNumberOfEvents": 126,
    "streamingData": [
        {
            "userId": "28",
            "completeName": "Malinda Brasier",
            "age": 28,
            "successfulEvents": 1,
            "eventList": [
                {
                    "eventId": "5ee59347-c388-4f45-8056-0497061af030",
                    "eventName": "stream-started",
                    "showId": "s8",
                    "cast": "Qtvistine Saylor",
                    "releaseYear": 2020,
                    "title": "Gove Ilonlasting",
                    "platform": "Sytazon",
                    "eventDate": "09-09-2023 05:26:20.407"
                },
                {
                    "eventId": "76cd7ddc-d989-4fa7-8d21-252986610c83",
                    "eventName": "stream-finished",
                    "showId": "s8",
                    "cast": "Qtvistine Saylor",
                    "releaseYear": 2020,
                    "title": "Gove Ilonlasting",
                    "platform": "Sytazon",
                    "eventDate": "09-09-2023 05:26:25.225"
                },
                {
                    "eventId": "70130b35-bbc9-4a83-9e06-6bbc96c4c4aa",
                    "eventName": "stream-finished",
                    "showId": "s10",
                    "cast": "Lill Kormer",
                    "releaseYear": 2020,
                    "title": "Jorld's Lost Hifgerous Xkark?",
                    "platform": "Sysney",
                    "eventDate": "09-09-2023 05:26:25.649"
                },
                {
                    "eventId": "91dd782f-5c0d-4b85-b6b1-70b581a8e1ae",
                    "eventName": "stream-finished",
                    "showId": "s7",
                    "cast": "Wason Pomoa",
                    "releaseYear": 2020,
                    "title": "Vvhulz Taves Omerica",
                    "platform": "Sytflix",
                    "eventDate": "09-09-2023 05:26:31.841"
                }
            ]
        },
        ...
        <other streaming events>
    ]
}
```

### Definitions:

| Property Name                       |        Descriptions         | 
|-------------------------------------|------------------------|
| userId                              | User identification   | 
| completeName                        | The user's name and surname | 
| totalShowsReleasedIn2020AndLater    | the total number of shows released in 2020 or later (for any type of event) |
| totalStreamingDurationMillis        | For how long the 3 http streams were consumed by your harvester program, in milliseconds |
| percentageStartedStreamingOnSytflix | The percentage of started stream events out of all events occurred on the Sytflix platform |
| streamingData[].age                 | The user's age |
| streamingData[].successfulEvents    | The total number of successful streaming events per user
| eventList[]                         | All the events that the user has executed | 
| eventList[].platform                | Platform where each event has occurred (Sytflix, Sysney, Sytazon) | 
| eventList[].title                   | The show titles | 
| eventList[].cast                    | The first person in the cast for each show, if present |
| eventList[].showId                  | The show ids | 
| eventList[].eventDate               | The event time the streaming happens, it is converted to Amsterdam CET timezone |

