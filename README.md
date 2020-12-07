### PACT  - Example Provider application

Simple shop inventory api which consumes a "biscuits" apo, to demonstrate the Consumer
side of pact contract testing


### Prerequisites

* jvm 11+ (recommend installing via [sdkman](https://sdkman.io/))

### Run the application

    ./gradlew build run

then visit http://localhost:7000/inventory/biscuits

Requires the "biscuits" service to be running, expected on localhost/9000


### Publish pacts

    ./gradlew clean build pactPublish

Expects to find a pact broker at http://localhost:2020
