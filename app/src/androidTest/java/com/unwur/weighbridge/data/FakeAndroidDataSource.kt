package com.unwur.weighbridge.data

import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.source.network.RealtimeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAndroidDataSource : RealtimeDataSource {

    private val fakeList = mutableListOf<Ticket>()

    override fun getTicketList(): Flow<List<Ticket>> {
        return flowOf(fakeList)
    }

    override fun getTicket(key: String): Flow<Ticket> {
        return flowOf(fakeList.find { it.id == key } ?: Ticket())
    }

    override suspend fun addNewTicket(newTicket: Ticket): String {
        fakeList.add(newTicket)
        return newTicket.id.orEmpty()
    }

    override fun updateTicket(updatedTicket: Ticket) {
        fakeList.find { it.id == updatedTicket.id }?.apply {
            driverName = updatedTicket.driverName
            dateTime = updatedTicket.dateTime
            licenseNumber = updatedTicket.licenseNumber
            inboundWeight = updatedTicket.inboundWeight
            outboundWeight = updatedTicket.outboundWeight
        }
    }
}