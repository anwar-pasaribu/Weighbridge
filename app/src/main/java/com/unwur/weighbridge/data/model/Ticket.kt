package com.unwur.weighbridge.data.model

data class Ticket(
    var id: String? = null,
    var dateTime: String? = null,
    var licenseNumber: String? = null,
    var driverName: String? = null,
    var inboundWeight: Double? = 0.0,
    var outboundWeight: Double? = 0.0,
)
