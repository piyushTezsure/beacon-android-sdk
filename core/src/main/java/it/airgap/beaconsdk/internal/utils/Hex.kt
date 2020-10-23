package it.airgap.beaconsdk.internal.utils

import java.math.BigInteger

private const val HEX_PREFIX = "0x"
private val hexRegex: Regex = Regex("^(${HEX_PREFIX})?([0-9a-fA-F]{2})+$")

internal class HexString private constructor(value: String) {
    private val value: String = if (value.startsWith(HEX_PREFIX)) value.substring(HEX_PREFIX.length) else value

    fun length(withPrefix: Boolean = false): Int = value(withPrefix).length

    fun value(withPrefix: Boolean = false): String {
        val startsWithPrefix = value.startsWith(HEX_PREFIX)

        return when {
            withPrefix && !startsWithPrefix -> "${HEX_PREFIX}${value}"
            !withPrefix && startsWithPrefix -> value.substring(HEX_PREFIX.length)
            else -> value
        }
    }

    fun slice(indices: IntRange): HexString = HexString(value.slice(indices))
    fun slice(startIndex: Int): HexString = HexString(value.substring(startIndex))

    fun asByteArray(): ByteArray = value(withPrefix = false).chunked(2).map { it.toInt(16).toByte() }.toByteArray()

    fun toBigInteger(): BigInteger = BigInteger(value(withPrefix = false), 16)

    override fun equals(other: Any?): Boolean =
        if (other == null || other !is HexString) false
        else value == other.value

    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = value(withPrefix = true)

    companion object {
        @Throws(IllegalArgumentException::class)
        fun fromString(string: String): HexString = fromStringOrNull(string) ?: failWithInvalidHexString(string)

        fun fromStringOrNull(string: String): HexString? = if (string.isHex()) HexString(string) else null
    }
}

internal fun String.isHex(): Boolean = this.matches(hexRegex)

internal fun Byte.asHexString(): HexString {
    val hex = "%02x".format(this)
    return HexString.fromString(hex)
}

internal fun ByteArray.asHexString(): HexString {
    val hex = joinToString("") { it.asHexString().value(withPrefix = false) }
    return HexString.fromString(hex)
}

internal fun List<Byte>.asHexString(): HexString {
    val hex = joinToString("") { it.asHexString().value(withPrefix = false) }
    return HexString.fromString(hex)
}

internal fun Int.asHexString(): HexString = asHexStringOrNull() ?: failWithNegativeNumber(this)
internal fun Int.asHexStringOrNull(): HexString? =
    if (this >= 0) HexString.fromString(toString(16).padStartEven('0'))
    else null

internal fun BigInteger.asHexString(): HexString = asHexStringOrNull() ?: failWithNegativeNumber(this)
internal fun BigInteger.asHexStringOrNull(): HexString? =
    if (this >= BigInteger.ZERO) HexString.fromString(toString(16).padStartEven('0'))
    else null

private fun failWithNegativeNumber(number: Int): Nothing = throw IllegalArgumentException("cannot create HexString from a negative number $number")
private fun failWithNegativeNumber(number: BigInteger): Nothing = throw IllegalArgumentException("cannot create HexString from a negative number $number")
private fun failWithInvalidHexString(string: String): Nothing = throw IllegalArgumentException("$string is not a valid hex string")