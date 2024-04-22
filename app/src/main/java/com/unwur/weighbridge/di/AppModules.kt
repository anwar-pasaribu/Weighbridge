package com.unwur.weighbridge.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unwur.weighbridge.data.source.network.RealtimeDataSource
import com.unwur.weighbridge.data.source.network.RealtimeDataSourceImpl
import com.unwur.weighbridge.data.repo.TicketRepo
import com.unwur.weighbridge.data.repo.TicketRepoImpl
import com.unwur.weighbridge.data.source.local.TicketDao
import com.unwur.weighbridge.data.source.local.TicketDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindListRepo(repo: TicketRepoImpl): TicketRepo
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindRealtimeDataSource(dataSource: RealtimeDataSourceImpl): RealtimeDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealtimeDatabase(): DatabaseReference {
//        Firebase.database.setPersistenceEnabled(true)
        return Firebase.database.reference.child("wbs")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): TicketDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TicketDatabase::class.java,
            "Tickets.db"
        ).build()
    }

    @Provides
    fun provideTicketsDao(database: TicketDatabase): TicketDao = database.ticketDao()
}