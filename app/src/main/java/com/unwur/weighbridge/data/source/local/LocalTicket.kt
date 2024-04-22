package com.unwur.weighbridge.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "ticket"
)
data class LocalTicket(
    @PrimaryKey val id: String,
    var ticketKey: String? = null,
    var dateTime: String? = null,
    var licenseNumber: String? = null,
    var driverName: String? = null,
    var inboundWeight: Double? = 0.0,
    var outboundWeight: Double? = 0.0,
)
