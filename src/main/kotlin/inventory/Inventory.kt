package inventory

import clients.BiscuitRepository
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto

private fun biscuitInventory(): Map<String, Int> {
    return mapOf(
        "1" to 42,
        "2" to 0,
        "3" to 12
    )
}

class Inventory(private val biscuitRepository: BiscuitRepository) {

    fun getBiscuitInventory(): Response {

        val inventory = biscuitInventory()
            .filter { (_, quantity) -> quantity > 0 }
            .map { (biscuitId, quantity) ->

                val biscuit = biscuitRepository.getBiscuit(biscuitId)

                BiscuitQuantity(
                    id = biscuitId,
                    name = biscuit?.name ?: "Unknown",
                    quantity = quantity
                )
            }

        val responseShape = Body.auto<List<BiscuitQuantity>>().toLens()
        return Response(Status.OK).with(
            responseShape of inventory
        )
    }
}

data class BiscuitQuantity(
    val id: String,
    val name: String,
    val quantity: Int
)


