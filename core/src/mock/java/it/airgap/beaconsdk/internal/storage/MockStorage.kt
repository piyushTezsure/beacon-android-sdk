package it.airgap.beaconsdk.internal.storage

import it.airgap.beaconsdk.data.beacon.AppMetadata
import it.airgap.beaconsdk.data.beacon.P2pPeerInfo
import it.airgap.beaconsdk.data.beacon.PermissionInfo
import it.airgap.beaconsdk.internal.transport.p2p.matrix.data.MatrixRoom

internal class MockStorage : Storage {
    private var p2pPeers: List<P2pPeerInfo> = emptyList()
    private var appsMetadata: List<AppMetadata> = emptyList()
    private var permissions: List<PermissionInfo> = emptyList()
    private var matrixSyncToken: String? = null
    private var matrixRooms: List<MatrixRoom> = emptyList()
    private var sdkSecretSeed: String? = null
    private var sdkVersion: String? = null

    override suspend fun getP2pPeers(): List<P2pPeerInfo> = p2pPeers
    override suspend fun setP2pPeers(p2pPeers: List<P2pPeerInfo>) {
        this.p2pPeers = p2pPeers
    }

    override suspend fun getAppMetadata(): List<AppMetadata> = appsMetadata
    override suspend fun setAppMetadata(appMetadata: List<AppMetadata>) {
        this.appsMetadata = appMetadata
    }

    override suspend fun getPermissions(): List<PermissionInfo> = permissions
    override suspend fun setPermissions(permissions: List<PermissionInfo>) {
        this.permissions = permissions
    }

    override suspend fun getMatrixSyncToken(): String? = matrixSyncToken
    override suspend fun setMatrixSyncToken(syncToken: String) {
        this.matrixSyncToken = syncToken
    }

    override suspend fun getMatrixRooms(): List<MatrixRoom> = matrixRooms
    override suspend fun setMatrixRooms(rooms: List<MatrixRoom>) {
        this.matrixRooms = rooms
    }

    override suspend fun getSdkSecretSeed(): String? = sdkSecretSeed
    override suspend fun setSdkSecretSeed(sdkSecretSeed: String) {
        this.sdkSecretSeed = sdkSecretSeed
    }

    override suspend fun getSdkVersion(): String? = sdkVersion
    override suspend fun setSdkVersion(sdkVersion: String) {
        this.sdkVersion = sdkVersion
    }
}