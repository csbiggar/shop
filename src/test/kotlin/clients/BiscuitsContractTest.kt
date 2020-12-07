package clients

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.PactTestExecutionContext
import au.com.dius.pact.consumer.PactTestRun
import au.com.dius.pact.consumer.PactVerificationResult
import au.com.dius.pact.consumer.model.MockProviderConfig
import au.com.dius.pact.consumer.runConsumerTest
import au.com.dius.pact.core.model.RequestResponsePact
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val PACT_PORT = 6060

class BiscuitsContractTest {
    private val pactMockServer = "http://localhost:$PACT_PORT"

    private val biscuitRepository = DefaultBiscuitRepository(DefaultBiscuitClient(pactMockServer))

    @Test
    fun `should read biscuit response`() {

        val biscuitId = "1"

        val primedResponse = ConsumerPactBuilder
            .consumer("shop")
            .hasPactWith("biscuits")

            .given("Biscuit exists")
            .uponReceiving("Details return for existing biscuit")

            .path("/biscuits/$biscuitId")
            .method("GET")
            .willRespondWith()

            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                //TODO Pattern matching response
                """
                    { "name" : "Club" }
                """
            )
            .toPact()

        val deferredResult = primedResponse.runTest {
            val biscuit = biscuitRepository.getBiscuit(biscuitId)
            assertThat(biscuit?.name).isNotNull()
        }

        // This is super important! If you don't include this, your test will
        // always pass
        assertThat(deferredResult).isEqualTo(PactVerificationResult.Ok())
    }

    @Test
    fun `no biscuit details found`() {
        val biscuitId = "DUMMY"

        val primedNotFoundResponse = ConsumerPactBuilder
            .consumer("shop")
            .hasPactWith("biscuits")

            .given("Biscuit does not exist")
            .uponReceiving("Null returned when biscuit is not found")

            .path("/biscuits/$biscuitId")
            .method("GET")
            .willRespondWith()

            .status(404)
            .headers(mapOf("Content-Type" to "application/json"))
            .toPact()

        val deferredResult = primedNotFoundResponse.runTest {
            val biscuit = biscuitRepository.getBiscuit(biscuitId)
            assertThat(biscuit).isNull()
        }

        assertThat(deferredResult).isEqualTo(PactVerificationResult.Ok())
    }

}


private fun RequestResponsePact.runTest(testAndAssertions: () -> Unit): PactVerificationResult {
    val config = MockProviderConfig.httpConfig(port = PACT_PORT)
    return runConsumerTest(this, config, object : PactTestRun<Nothing?> {
        override fun run(mockServer: MockServer, context: PactTestExecutionContext?): Nothing? {
            testAndAssertions()
            return null
        }
    })
}
