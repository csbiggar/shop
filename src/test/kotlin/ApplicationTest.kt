import clients.Biscuit
import clients.BiscuitRepository
import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert

class ApplicationTest {

    @Test
    fun `should indicate status for use with readiness and liveness probe`() {
        // When
        val result = createApp(MockBiscuitRepository)(Request(Method.GET, "/"))

        // Then
        assertThat(result.status).isEqualTo(Status.OK)
        assertThat(result.bodyString()).isEqualTo("Pact example consumer application (shop consuming biscuits api)")
    }

    @Test
    fun `should return biscuit inventory`() {
        // When
        val result = createApp(MockBiscuitRepository)(Request(Method.GET, "/inventory/biscuits"))

        assertThat(result.status).isEqualTo(Status.OK)
        assertThat(result.header("Content-Type")).isEqualTo("application/json; charset=utf-8")

        JSONAssert.assertEquals(
            """ 
            [
              {
                "id": "1",
                "name": "My Biscuit",
                "quantity": 42
              },
              {
                "id": "3",
                "name": "Unknown",
                "quantity": 12
              }
            ]
            """.trimIndent(),
            result.body.toString(),
            false
        )

    }

    @Test
    fun `should return biscuit replenishment list`() {
        // When
        val result = createApp(MockBiscuitRepository)(Request(Method.GET, "/replenishment/biscuits"))

        assertThat(result.status).isEqualTo(Status.OK)
        assertThat(result.header("Content-Type")).isEqualTo("application/json; charset=utf-8")

        JSONAssert.assertEquals(
            """ 
            [
              {
                "name": "My Biscuit",
                "toOrder": 10
              },
              {
                "name": "Another biscuit",
                "toOrder": 10
              }
            ]
            """.trimIndent(),
            result.body.toString(),
            true
        )

    }

}

object MockBiscuitRepository : BiscuitRepository {
    override fun getBiscuit(id: String): Biscuit? {
        return when (id) {
            "1" -> Biscuit("My Biscuit")
            else -> null
        }
    }

    override fun getAllBiscuits(): List<Biscuit> {
        return listOf(
            Biscuit("My Biscuit"),
            Biscuit("Another biscuit")
        )
    }

}