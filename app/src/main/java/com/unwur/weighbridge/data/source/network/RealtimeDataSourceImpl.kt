package com.unwur.weighbridge.data.source.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.unwur.weighbridge.data.model.Ticket
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class RealtimeDataSourceImpl @Inject constructor(
    private val database: DatabaseReference
) : RealtimeDataSource {

    override fun getTicketList(): Flow<List<Ticket>> = callbackFlow {
        val listener = database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newTicketList = mutableListOf<Ticket>()
                for (ticketSnapshot in dataSnapshot.children) {
                    ticketSnapshot.getValue(Ticket::class.java)?.let {
                        newTicketList.add(it.apply { id = ticketSnapshot.key })
                    }
                }
                trySend(newTicketList).isSuccess
            }

            override fun onCancelled(databaseError: DatabaseError) {
                close(databaseError.toException())
            }
        })

        awaitClose { database.removeEventListener(listener) }
    }

    override fun getTicket(key: String): Flow<Ticket> = callbackFlow {

        database.child(key).get()
            .addOnSuccessListener { dataSnapshot ->
                dataSnapshot.getValue(Ticket::class.java)?.let {
                    val ticketItem = it.apply {
                        id = dataSnapshot.key
                    }
                    trySend(ticketItem)
                }
            }
        awaitClose {  }
    }

    override suspend fun addNewTicket(newTicket: Ticket): String {
        val key = database.push().key ?: return ""
        val newTicketWithDate = newTicket.copy()
        database.child(key).setValue(newTicketWithDate)

        return key
    }

    override fun updateTicket(updatedTicket: Ticket) {
        val ticketKey = updatedTicket.id ?: return
        database.child(ticketKey).setValue(updatedTicket)
    }
}