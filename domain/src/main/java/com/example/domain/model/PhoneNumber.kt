package com.example.domain.model

private const val REGION_CODE = "+996"
const val PHONE_LENGTH = 13
private const val PHONE_LENGTH_WITHOUT_REGION_CODE = 9

@JvmInline
value class PhoneNumber(val value: String) {

    fun isValid(): Boolean = value.length == PHONE_LENGTH

    val valueWithoutRegionCode: String
        get(): String = value.replace(REGION_CODE, "")

    companion object {
        val EMPTY = PhoneNumber("")

        fun create(value: String): PhoneNumber {
            return PhoneNumber(REGION_CODE + value)
        }
    }

    override fun toString(): String {
        return value
    }
}