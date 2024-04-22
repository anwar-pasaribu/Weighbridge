package com.unwur.weighbridge.data.mapper

import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.source.local.LocalTicket

fun Ticket.toLocal() = LocalTicket (
    id = id.orEmpty(),
    ticketKey = "",
    dateTime = dateTime.orEmpty(),
    licenseNumber = licenseNumber.orEmpty(),
    driverName = driverName.orEmpty(),
    inboundWeight = inboundWeight,
    outboundWeight = outboundWeight
)

fun LocalTicket.toExternal() = Ticket (
    id = ticketKey,
    dateTime = dateTime.orEmpty(),
    licenseNumber = licenseNumber.orEmpty(),
    driverName = driverName.orEmpty(),
    inboundWeight = inboundWeight,
    outboundWeight = outboundWeight
)

@JvmName("localToExternal")
fun List<LocalTicket>.toExternal() = map(LocalTicket::toExternal)