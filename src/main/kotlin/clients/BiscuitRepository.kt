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
    fun getAllBiscuits(): List<Biscuit>
}

class DefaultBiscuitRepository(
    private val client: BiscuitClient
) : BiscuitRepository {


    override fun getBiscuit(id: String): Biscuit? {
        val biscuitLens = Body.auto<Biscuit>().toLens()

        val response = client.getBiscuit(id)

        return when (response.status) {
            Status.OK -> biscuitLens(response)
            Status.NOT_FOUND -> null
            else -> throw Exception("Problem finding biscuit details for id $id")
        }
    }

    override fun getAllBiscuits(): List<Biscuit> {
        val biscuitsLens = Body.auto<List<Biscuit>>().toLens()

        val response = client.getAllBiscuits()

        return when (response.status) {
            Status.OK -> biscuitsLens(response)
            else -> throw Exception("Problem finding all biscuits")
        }
    }
}

interface BiscuitClient {
    fun getBiscuit(id: String): Response
    fun getAllBiscuits(): Response
}

class DefaultBiscuitClient(private val baseUrl: String) : BiscuitClient {
    private val client = ApacheClient()
    override fun getBiscuit(id: String): Response {
        val request = Request(Method.GET, "$baseUrl/biscuits/$id")
        return client(request)
    }
    override fun getAllBiscuits(): Response {
        val request = Request(Method.GET, "$baseUrl/biscuits")
        return client(request)
    }
}
