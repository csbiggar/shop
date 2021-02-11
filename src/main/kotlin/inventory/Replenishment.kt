package inventory

import clients.BiscuitRepository
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto

class Replenishment(private val biscuitRepository: BiscuitRepository) {

    fun getBiscuitReplenishment(): Response {

        val replenishmentOrder = biscuitRepository
            .getAllBiscuits()
            .map {
                BiscuitReplenishment(
                    name = it.name,
                    toOrder = 10
                )
            }

        val responseShape = Body.auto<List<BiscuitReplenishment>>().toLens()
        return Response(Status.OK).with(
            responseShape of replenishmentOrder
        )
    }
}

data class BiscuitReplenishment(
    val name: String,
    val toOrder: Int
)
