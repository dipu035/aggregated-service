### Running the application

**With docker installed**:
- You can build the image using the script: build-image.sh
- Go to the project's root directory and type: ```docker-compose up```

**Without docker**:
Either run this in maven using `mvn spring-boot:run` or using the run features from your IDE of choice.

For both cases the default port is 8081.

## Log into the application

### How to log in:

```curl -k -d '{"username":"admin", "password":"secret"}' -H "Content-Type: application/json" -X POST https://localhost:8081/rabobank/api/generate-token```

The response will provide the token for header authorization, which can be used for other requests.

### Users:
| user          | password      |
|:------------- |:------------- |
| admin         | secret        |

# APIs

## Power of attorney(aggregated) API

The power of attorney api contains aggregated information from 4 services.
- Provide aggregated information from different services
- (assumption) External services might be slow and for some services data doesn't change often, that's why implemented cache for some services.
- No database was required as the information of the service is derived from other services.
- Handled server error gracefully(retrieving debit-card information with id 3333 throws server error).

# Exercise
Create an api that 
  - aggregates 4 api's
      - ```/power-of-attorneys/{id}```
      - ```/debit-cards/{id}```
      - ```/credit-cards/{id}```
      - ```/accounts/{id}```

# assumptions:
All 4 api's are up and running. As default the api's are running in port 8080 (if not, the correct url for api's can be given through configuration)

## Remark
As this is a demo, tried to keep things simple.