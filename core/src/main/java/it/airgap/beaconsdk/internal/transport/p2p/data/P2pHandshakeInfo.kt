package it.airgap.beaconsdk.internal.transport.p2p.data

import kotlinx.serialization.Serializable

@Serializable
internal data class P2pHandshakeInfo(
    val name: String,
    val version: String,
    val publicKey: String,
    val relayServer: String
)