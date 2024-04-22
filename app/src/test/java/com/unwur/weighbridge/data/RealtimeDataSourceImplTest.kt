package com.unwur.weighbridge.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.unwur.weighbridge.data.source.network.RealtimeDataSourceImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RealtimeDataSourceImplTest {

    private val databaseReference = mockk<DatabaseReference>()
    private val dataSnapshot: DataSnapshot = mockk()
    private val databaseError: DatabaseError = mockk()

    private lateinit var dataSourceImpl: RealtimeDataSourceImpl

    @Before
    fun setUp() {
        dataSourceImpl = RealtimeDataSourceImpl(databaseReference)
    }

    @Test
    fun `getTicketList should return 0 size at first`() = runTest {
        val valueEventListener = mockk<ValueEventListener>()

        every {
            databaseReference.addValueEventListener(valueEventListener)
        }.returns(valueEventListener)

    }
}