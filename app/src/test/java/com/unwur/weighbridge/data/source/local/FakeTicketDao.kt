package com.unwur.weighbridge.data.source.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTicketDao(initialTasks: List<LocalTicket>? = emptyList()) : TicketDao {

    private var _tickets: MutableMap<String, LocalTicket>? = null

    var tickets: List<LocalTicket>?
        get() = _tickets?.values?.toList()
        set(newTasks) {
            _tickets = newTasks?.associateBy { it.ticketKey.orEmpty() }?.toMutableMap()
        }

    init {
        tickets = initialTasks
    }

    override suspend fun getAll() = tickets ?: throw Exception("Ticket list is null")

    override fun getByTicketKey(ticketKey: String): Flow<LocalTicket> {
        return flowOf(
            _tickets?.get(ticketKey) ?: LocalTicket(
                id = "-",
                ticketKey = "-",
                dateTime = "-",
                licenseNumber = "-",
                driverName = "-",
                inboundWeight = 0.0,
                outboundWeight = 0.0
            )
        )
    }

    override suspend fun upsert(localTicket: LocalTicket) {
        _tickets?.put(localTicket.id, localTicket)
    }

    override suspend fun deleteAll() {
        _tickets?.clear()
    }

    override fun observeAll(): Flow<List<LocalTicket>> {
        return flowOf(tickets!!)
    }

}
