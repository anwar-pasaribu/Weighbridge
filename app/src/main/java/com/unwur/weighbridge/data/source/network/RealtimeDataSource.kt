package com.unwur.weighbridge.data.source.network

import com.unwur.weighbridge.data.model.Ticket
import kotlinx.coroutines.flow.Flow

interface RealtimeDataSource {
    fun getTicketList(): Flow<List<Ticket>>
    fun getTicket(key: String): Flow<Ticket>
    suspend fun addNewTicket(newTicket: Ticket): String
    fun updateTicket(updatedTicket: Ticket)
}