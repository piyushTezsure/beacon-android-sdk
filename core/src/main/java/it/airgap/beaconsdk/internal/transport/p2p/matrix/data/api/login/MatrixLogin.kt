package it.airgap.beaconsdk.internal.transport.p2p.matrix.data.api.login

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class MatrixLoginRequest(@Required private val type: Type) {
    abstract val identifier: UserIdentifier
    @SerialName("device_id") abstract val deviceId: String?

    @Serializable
    data class Password(
        override val identifier: UserIdentifier,
        val password: String,
        override val deviceId: String? = null,
    ) : MatrixLoginRequest(Type.Password)

    @Serializable
    private enum class Type {
        @SerialName("m.login.password") Password,
        @SerialName("m.login.token") Token,
    }

    @Serializable
    sealed class UserIdentifier(@Required private val type: Type) {

        @Serializable
        data class User(val user: String) : UserIdentifier(Type.User)

        @Serializable
        private enum class Type {
            @SerialName("m.id.user") User,
            @SerialName("m.id.thirdparty") ThirdParty,
            @SerialName("m.id.phone") Phone,
        }
    }
}

@Serializable
internal data class MatrixLoginResponse(
    @SerialName("user_id") val userId: String?,
    @SerialName("device_id") val deviceId: String?,
    @SerialName("access_token") val accessToken: String?,
)