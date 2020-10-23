package it.airgap.beaconsdk.internal.utils

internal sealed class InternalResult<T> {
    data class Success<T>(val value: T) : InternalResult<T>()
    data class Error<T>(val error: Throwable? = null) : InternalResult<T>() {
        override fun toString(): String = error?.toString() ?: "Error: unknown"
    }

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    fun getOrNull(): T? =
        when (this) {
            is Success -> value
            is Error -> null
        }

    fun getErrorOrNull(): Throwable? =
        when (this) {
            is Success -> null
            is Error -> error
        }

    fun getOrLogError(tag: String): T? = tryLog(tag) { getOrThrow() }

    fun getOrThrow(): T =
        when (this) {
            is Success -> value
            is Error -> failWith(cause = error)
        }

    fun <S> map(transform: (T) -> S): InternalResult<S> =
        when (this) {
            is Success -> Success(transform(value))
            is Error -> Error(error)
        }

    suspend fun <S> mapSuspend(transform: suspend (T) -> S): InternalResult<S> =
        when (this) {
            is Success -> Success(transform(value))
            is Error -> Error(error)
        }

    fun mapError(transform: (Throwable?) -> Throwable): InternalResult<T> =
        when (this) {
            is Success -> this
            is Error -> Error(transform(error))
        }

    suspend fun mapErrorSuspend(transform: suspend (Throwable?) -> Throwable): InternalResult<T> =
        when (this) {
            is Success -> this
            is Error -> Error(transform(error))
        }

    fun <S> flatMap(transform: (T) -> InternalResult<S>): InternalResult<S> =
        when (this) {
            is Success -> transform(value)
            is Error -> Error(error)
        }

    suspend fun <S> flatMapSuspend(transform: suspend (T) -> InternalResult<S>): InternalResult<S> =
        when (this) {
            is Success -> transform(value)
            is Error -> Error(error)
        }

    inline fun alsoIfSuccess(block: (T) -> Unit): InternalResult<T> = also { it.getOrNull()?.let(block) }
}

internal fun <T> internalSuccess(value: T): InternalResult<T> = InternalResult.Success(value)
internal fun <T> internalError(error: Throwable? = null): InternalResult<T> = InternalResult.Error(error)