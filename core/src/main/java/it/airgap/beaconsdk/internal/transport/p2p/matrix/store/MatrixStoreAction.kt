package it.airgap.beaconsdk.internal.transport.p2p.matrix.store

import it.airgap.beaconsdk.internal.transport.p2p.matrix.data.api.sync.MatrixSyncRooms

internal sealed class MatrixStoreAction

internal data class Init(
    val userId: String,
    val deviceId: String,
    val accessToken: String,
) : MatrixStoreAction()

internal data class OnSyncSuccess(
    val syncToken: String?,
    val pollingTimeout: Long,
    val syncRooms: MatrixSyncRooms?,
) : MatrixStoreAction()

internal object OnSyncError : MatrixStoreAction()

internal object OnTxnIdCreated : MatrixStoreAction()