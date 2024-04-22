package com.unwur.weighbridge.di

import android.content.Context
import androidx.room.Room
import com.unwur.weighbridge.data.source.local.TicketDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalDatabaseModule::class]
)
object DatabaseUnitTestModule {

    @Singleton
    @Provides
    fun provideUnitTestDataBase(@ApplicationContext context: Context): TicketDatabase {
        return Room
            .inMemoryDatabaseBuilder(context.applicationContext, TicketDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}
