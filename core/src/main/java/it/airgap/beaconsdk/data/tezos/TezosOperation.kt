package it.airgap.beaconsdk.data.tezos

import it.airgap.beaconsdk.data.tezos.MichelineMichelsonV1Expression
import it.airgap.beaconsdk.internal.utils.failWithExpectedJsonDecoder
import it.airgap.beaconsdk.internal.utils.failWithMissingField
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Base for Tezos operations supported in Beacon.
 */
@Serializable(with = TezosOperation.Serializer::class)
public sealed class TezosOperation {
    internal abstract val kind: Kind

    public companion object {}

    @Serializable
    internal enum class Kind {
        @SerialName("endorsement")
        Endorsement,

        @SerialName("seed_nonce_revelation")
        SeedNonceRevelation,

        @SerialName("double_endorsement_evidence")
        DoubleEndorsementEvidence,

        @SerialName("double_baking_evidence")
        DoubleBakingEvidence,

        @SerialName("activate_account")
        ActivateAccount,

        @SerialName("proposals")
        Proposals,

        @SerialName("ballot")
        Ballot,

        @SerialName("reveal")
        Reveal,

        @SerialName("transaction")
        Transaction,

        @SerialName("origination")
        Origination,

        @SerialName("delegation")
        Delegation,
    }

    internal object Field {
        const val KIND = "kind"
    }

    internal class Serializer : KSerializer<TezosOperation> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("TezosOperation", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): TezosOperation {
            val jsonDecoder = decoder as? JsonDecoder ?: failWithExpectedJsonDecoder(decoder::class)
            val jsonElement = jsonDecoder.decodeJsonElement()

            val kindSerialized =
                jsonElement.jsonObject[Field.KIND]?.jsonPrimitive ?: failWithMissingField(Field.KIND)
            val kind = jsonDecoder.json.decodeFromJsonElement(Kind.serializer(), kindSerialized)

            return when (kind) {
                Kind.ActivateAccount -> jsonDecoder.json.decodeFromJsonElement(TezosActivateAccountOperation.serializer(), jsonElement)
                Kind.Ballot -> jsonDecoder.json.decodeFromJsonElement(TezosBallotOperation.serializer(), jsonElement)
                Kind.Delegation -> jsonDecoder.json.decodeFromJsonElement(TezosDelegationOperation.serializer(), jsonElement)
                Kind.DoubleBakingEvidence -> jsonDecoder.json.decodeFromJsonElement(TezosDoubleBakingEvidenceOperation.serializer(), jsonElement)
                Kind.Endorsement -> jsonDecoder.json.decodeFromJsonElement(TezosEndorsementOperation.serializer(), jsonElement)
                Kind.DoubleEndorsementEvidence -> jsonDecoder.json.decodeFromJsonElement(TezosDoubleEndorsementEvidenceOperation.serializer(), jsonElement)
                Kind.Origination -> jsonDecoder.json.decodeFromJsonElement(TezosOriginationOperation.serializer(), jsonElement)
                Kind.Proposals -> jsonDecoder.json.decodeFromJsonElement(TezosProposalsOperation.serializer(), jsonElement)
                Kind.Reveal -> jsonDecoder.json.decodeFromJsonElement(TezosRevealOperation.serializer(), jsonElement)
                Kind.SeedNonceRevelation -> jsonDecoder.json.decodeFromJsonElement(TezosSeedNonceRevelationOperation.serializer(), jsonElement)
                Kind.Transaction -> jsonDecoder.json.decodeFromJsonElement(TezosTransactionOperation.serializer(), jsonElement)
            }
        }

        override fun serialize(encoder: Encoder, value: TezosOperation) {
            when (value) {
                is TezosActivateAccountOperation -> encoder.encodeSerializableValue(TezosActivateAccountOperation.serializer(), value)
                is TezosBallotOperation -> encoder.encodeSerializableValue(TezosBallotOperation.serializer(), value)
                is TezosDelegationOperation -> encoder.encodeSerializableValue(TezosDelegationOperation.serializer(), value)
                is TezosDoubleBakingEvidenceOperation -> encoder.encodeSerializableValue(TezosDoubleBakingEvidenceOperation.serializer(), value)
                is TezosEndorsementOperation -> encoder.encodeSerializableValue(TezosEndorsementOperation.serializer(), value)
                is TezosDoubleEndorsementEvidenceOperation -> encoder.encodeSerializableValue(TezosDoubleEndorsementEvidenceOperation.serializer(), value)
                is TezosOriginationOperation -> encoder.encodeSerializableValue(TezosOriginationOperation.serializer(), value)
                is TezosProposalsOperation -> encoder.encodeSerializableValue(TezosProposalsOperation.serializer(), value)
                is TezosRevealOperation -> encoder.encodeSerializableValue(TezosRevealOperation.serializer(), value)
                is TezosSeedNonceRevelationOperation -> encoder.encodeSerializableValue(TezosSeedNonceRevelationOperation.serializer(), value)
                is TezosTransactionOperation -> encoder.encodeSerializableValue(TezosTransactionOperation.serializer(), value)
            }
        }

    }
}

@Serializable
public data class TezosActivateAccountOperation(
    public val pkh: String,
    public val secret: String,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.ActivateAccount

    public companion object {}
}

@Serializable
public data class TezosBallotOperation(
    public val source: String,
    public val period: String,
    public val proposal: String,
    public val ballot: Type,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.Ballot

    @Serializable
    public enum class Type {
        @SerialName("nay")
        Nay,

        @SerialName("yay")
        Yay,

        @SerialName("pass")
        Pass,
    }

    public companion object {}
}

@Serializable
public data class TezosDelegationOperation(
    public val source: String? = null,
    public val fee: String? = null,
    public val counter: String? = null,
    @SerialName("gas_limit") public val gasLimit: String? = null,
    @SerialName("storage_limit") public val storageLimit: String? = null,
    public val delegate: String? = null,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.Delegation

    public companion object {}
}

@Serializable
public data class TezosDoubleBakingEvidenceOperation(
    public val bh1: TezosBlockHeader,
    public val bh2: TezosBlockHeader,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.DoubleBakingEvidence

    public companion object {}
}

@Serializable
public data class TezosEndorsementOperation(public val level: String) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.Endorsement

    public companion object {}
}

@Serializable
public data class TezosDoubleEndorsementEvidenceOperation(
    public val op1: TezosInlinedEndorsement,
    public val op2: TezosInlinedEndorsement,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.DoubleEndorsementEvidence

    public companion object {}
}

@Serializable
public data class TezosOriginationOperation(
    public val source: String? = null,
    public val fee: String? = null,
    public val counter: String? = null,
    @SerialName("gas_limit") public val gasLimit: String? = null,
    @SerialName("storage_limit") public val storageLimit: String? = null,
    public val balance: String,
    public val delegate: String? = null,
    public val script: String,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.Origination

    public companion object {}
}

@Serializable
public data class TezosProposalsOperation(
    public val period: String,
    public val proposals: List<String>,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.Proposals

    public companion object {}
}

@Serializable
public data class TezosRevealOperation(
    public val source: String? = null,
    public val fee: String? = null,
    public val counter: String? = null,
    @SerialName("gas_limit") public val gasLimit: String? = null,
    @SerialName("storage_limit") public val storageLimit: String? = null,
    @SerialName("public_key") public val publicKey: String,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.Reveal

    public companion object {}
}

@Serializable
public data class TezosSeedNonceRevelationOperation(public val level: String, public val nonce: String) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.SeedNonceRevelation

    public companion object {}
}

@Serializable
public data class TezosTransactionOperation(
    public val source: String? = null,
    public val fee: String? = null,
    public val counter: String? = null,
    @SerialName("gas_limit") public val gasLimit: String? = null,
    @SerialName("storage_limit") public val storageLimit: String? = null,
    public val amount: String,
    public val destination: String,
    public val parameters: Parameters? = null,
) : TezosOperation() {
    @Required
    override val kind: Kind = Kind.Transaction

    @Serializable
    public data class Parameters(
        public val entrypoint: String,
        public val value: MichelineMichelsonV1Expression,
    ) {
        public companion object {}
    }

    public companion object {}
}