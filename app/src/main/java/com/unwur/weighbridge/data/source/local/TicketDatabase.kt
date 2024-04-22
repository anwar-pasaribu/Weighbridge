package com.unwur.weighbridge.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [LocalTicket::class], version = 1, exportSchema = false)
abstract class TicketDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao
}
