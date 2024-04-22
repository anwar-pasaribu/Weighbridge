package com.unwur.weighbridge.data.repo

import com.unwur.weighbridge.data.model.Ticket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAndroidRepoImpl : TicketRepo {

    private val fakeList = mutableListOf<Ticket>()

    override fun getList(): Flow<List<Ticket>> {
        return flowOf(fakeList)
    }

    override fun getTicket(key: String): Flow<Ticket> {
        return flowOf(fakeList.find { it.id == key } ?: Ticket())
    }

    override suspend fun addNewTicket(newTicket: Ticket) {
        fakeList.add(newTicket)
    }

    override suspend fun updateTicket(updatedTicket: Ticket) {
        fakeList.find { it.id == updatedTicket.id }?.apply {
            driverName = updatedTicket.driverName
            dateTime = updatedTicket.dateTime
            licenseNumber = updatedTicket.licenseNumber
            inboundWeight = updatedTicket.inboundWeight
            outboundWeight = updatedTicket.outboundWeight
        }
    }
}