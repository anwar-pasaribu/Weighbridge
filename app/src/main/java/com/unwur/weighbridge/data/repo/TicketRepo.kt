package com.unwur.weighbridge.data.repo

import com.unwur.weighbridge.data.model.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketRepo {
    fun getList(): Flow<List<Ticket>>
    fun getTicket(key: String): Flow<Ticket>
    suspend fun addNewTicket(newTicket: Ticket)
    suspend fun updateTicket(updatedTicket: Ticket)
}