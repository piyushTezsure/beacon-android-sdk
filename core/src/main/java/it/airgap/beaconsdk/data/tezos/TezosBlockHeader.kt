package it.airgap.beaconsdk.data.tezos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TezosBlockHeader(
    public val level: Int,
    public val proto: Int,
    public val predecessor: String,
    public val timestamp: String,
    @SerialName("validation_pass") public val validationPass: Int,
    @SerialName("operations_hash") public val operationsHash: String,
    public val fitness: List<String>,
    public val context: String,
    public val priority: Int,
    @SerialName("proof_of_work_nonce") public val proofOfWorkNonce: String,
    public val signature: String,
) {
    public companion object {}
}