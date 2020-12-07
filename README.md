### PACT  - Example Consumer application

Simple shop inventory api which consumes a "biscuits" apo, to demonstrate the Consumer
side of pact contract testing

Part of the  [workshop steps here](https://github.com/csbiggar/pact-contract-testing-workshop)

### Prerequisites

* jvm 11+ (recommend installing via [sdkman](https://sdkman.io/))
* a pact broker (get a quick locally running broker set up by following [these instructions](https://github.com/pact-foundation/pact-broker-docker/blob/master/POSTGRESQL.md#running-postgresql-via-docker) )


### Publish pacts

    ./gradlew clean build pactPublish

Expects to find a pact broker at http://localhost:2020

### Run the application (optional)

    ./gradlew build run

then visit http://localhost:7000/inventory/biscuits

Requires the "biscuits" service to be running, expected on localhost/9000

