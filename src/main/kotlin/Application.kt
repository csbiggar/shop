import clients.BiscuitRepository
import clients.DefaultBiscuitRepository
import clients.LocalBiscuitClient
import inventory.Inventory
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
    val port = 7000
    createApp(DefaultBiscuitRepository(LocalBiscuitClient))
        .asServer(Netty(port))
        .start()

    println("App running on http://localhost:$port")
}

fun createApp(biscuitRepository: BiscuitRepository) = routes(
    "/" bind Method.GET to { Response(OK).body("Pact example consumer application (shop consuming biscuits api)") },
    "inventory/biscuits" bind Method.GET to { Inventory(biscuitRepository).getBiscuitInventory() },
)

