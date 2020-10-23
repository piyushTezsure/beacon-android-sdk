package it.airgap.beaconsdk.internal.transport.p2p.matrix.network

import it.airgap.beaconsdk.internal.network.HttpClient
import it.airgap.beaconsdk.internal.network.data.BearerHeader
import it.airgap.beaconsdk.internal.network.data.HttpParameter
import it.airgap.beaconsdk.internal.transport.p2p.matrix.data.api.event.MatrixEventResponse
import it.airgap.beaconsdk.internal.transport.p2p.matrix.data.api.sync.MatrixSyncResponse
import it.airgap.beaconsdk.internal.utils.InternalResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn

//@ExperimentalCoroutinesApi
internal class MatrixEventService(private val httpClient: HttpClient) {
    private val ongoingRequests: MutableMap<String, Flow<InternalResult<*>>> = mutableMapOf()

    suspend fun sync(accessToken: String, since: String? = null, timeout: Int? = null): InternalResult<MatrixSyncResponse> {
        val parameters = mutableListOf<HttpParameter>().apply {
            since?.let { add("since" to it) }
            timeout?.let { add("timeout" to it.toString()) }
        }.toList()

        return getOngoingOrRun<MatrixSyncResponse>("sync") {
            httpClient.get(
                "/sync",
                headers = listOf(BearerHeader(accessToken)),
                parameters = parameters,
            )
        }.single()
    }

    suspend inline fun <reified T : Any> sendEvent(
        accessToken: String,
        roomId: String,
        eventType: String,
        txnId: String,
        content: T
    ): InternalResult<MatrixEventResponse> =
        httpClient.put<T, MatrixEventResponse>(
            "/rooms/$roomId/send/$eventType/$txnId",
            body = content,
            headers = listOf(BearerHeader(accessToken))
        ).single()

    @Suppress("UNCHECKED_CAST")
    private suspend inline fun <T : Any> getOngoingOrRun(
        name: String,
        action: () -> Flow<InternalResult<T>>
    ): Flow<InternalResult<T>> = ongoingRequests.getOrPut(name) {
        action()
            .stateIn(CoroutineScope(Dispatchers.IO))
            .onCompletion { ongoingRequests.remove(name) }
    } as Flow<InternalResult<T>>
}