package com.unwur.weighbridge.data.repo

import com.unwur.weighbridge.data.mapper.toExternal
import com.unwur.weighbridge.data.mapper.toLocal
import com.unwur.weighbridge.data.source.network.RealtimeDataSource
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.source.local.TicketDao
import com.unwur.weighbridge.di.ApplicationScope
import com.unwur.weighbridge.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class TicketRepoImpl @Inject constructor(
    private val networkDataSource: RealtimeDataSource,
    private val localDataSource: TicketDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : TicketRepo {

    override fun getList(): Flow<List<Ticket>> {
        scope.launch {
            networkDataSource.getTicketList().collect { networkTickets ->
                withContext(dispatcher) {
                    localDataSource.deleteAll()
                    networkTickets.forEach { networkTicket ->
                        val networkTicketKey = networkTicket.id.orEmpty()
                        localDataSource.upsert(
                            networkTicket.toLocal().apply { ticketKey = networkTicketKey }
                        )
                    }
                }
            }
        }

        return localDataSource.observeAll().map { tickets ->
            withContext(dispatcher) {
                tickets.toExternal()
            }
        }
    }

    override fun getTicket(key: String): Flow<Ticket> {
        return localDataSource.getByTicketKey(key).map { ticket ->
            withContext(dispatcher) {
                ticket.toExternal()
            }
        }
    }

    override suspend fun addNewTicket(newTicket: Ticket) {
        // ID creation might be a complex operation so it's executed using the supplied
        // coroutine dispatcher
        val localTicketId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }

        val newTicketRemoteKey = networkDataSource.addNewTicket(newTicket)

        newTicket.apply { id = localTicketId }

        val newLocalTicket = newTicket.toLocal().apply {
            ticketKey = newTicketRemoteKey
        }

        localDataSource.upsert(newLocalTicket)
    }

    override suspend fun updateTicket(updatedTicket: Ticket) {
        networkDataSource.updateTicket(updatedTicket)
    }
}