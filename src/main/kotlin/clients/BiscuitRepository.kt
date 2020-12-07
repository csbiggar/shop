package clients

import org.http4k.client.ApacheClient
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson.auto

data class Biscuit(val name: String)

interface BiscuitRepository {
    fun getBiscuit(id: String): Biscuit?
}

class DefaultBiscuitRepository(
    private val client: BiscuitClient = LocalBiscuitClient
) : BiscuitRepository {

    private val biscuitLens = Body.auto<Biscuit>().toLens()

    override fun getBiscuit(id: String): Biscuit? {
        val response = client.getBiscuit(id)

        return when (response.status) {
            Status.OK -> biscuitLens(response)
            Status.NOT_FOUND -> null
            else -> throw Exception("Problem finding biscuit details for id $id")
        }
    }
}

interface BiscuitClient {
    fun getBiscuit(id: String): Response
}

object LocalBiscuitClient : BiscuitClient {
    private const val baseUrl: String = "http://localhost:9000"

    private val client = ApacheClient()
    override fun getBiscuit(id: String): Response {
        val request = Request(Method.GET, "$baseUrl/biscuits/$id")
        return client(request)
    }
}
