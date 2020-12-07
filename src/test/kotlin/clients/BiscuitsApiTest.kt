package clients

import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test

internal class BiscuitsApiTest {

    private val biscuitsApi = DefaultBiscuitRepository(FakeBiscuitClient)

    @Test
    fun `should return null for not found`() {
        val missingBiscuit: Biscuit? = biscuitsApi.getBiscuit("missing")

        assertThat(missingBiscuit).isNull()
    }

    @Test
    fun `should return biscuit details when biscuit found`() {

        // When
        val missingBiscuit: Biscuit? = biscuitsApi.getBiscuit("123")

        // Then
        val expected = Biscuit("My Biscuit")
        assertThat(missingBiscuit).isEqualTo(expected)
    }

    @Test
    fun `should blow up when server error (not a real life reaction - usually deal with by retries etc)`() {
        // Then
        //TODO Grr something not compiling here!
//        assertThrows<Exception> { biscuitsApi.getBiscuit("123") }
    }
}

object FakeBiscuitClient : BiscuitClient {
    override fun getBiscuit(id: String): Response {
        return when (id) {
            "123" -> Response(OK).body("""{"name":"My Biscuit"}""")
            "errorId" -> Response(INTERNAL_SERVER_ERROR)
            else -> Response(NOT_FOUND)
        }
    }
}