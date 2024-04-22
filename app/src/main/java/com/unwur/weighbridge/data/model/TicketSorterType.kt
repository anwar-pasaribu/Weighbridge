package com.unwur.weighbridge.data.model

enum class TicketSorterType(val value: Int) {
    NONE(0),
    NAME(1),
    DATE(2),
    LICENSE_NUMBER(3);

    companion object {
        fun fromValue(value: Int): TicketSorterType? {
            return values().find { it.value == value }
        }
    }
}
