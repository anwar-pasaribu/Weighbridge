package com.unwur.weighbridge.di

import com.unwur.weighbridge.data.FakeAndroidDataSource
import com.unwur.weighbridge.data.source.network.RealtimeDataSource
import com.unwur.weighbridge.data.repo.FakeAndroidRepoImpl
import com.unwur.weighbridge.data.repo.TicketRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class]
)
object RepoTestModule {

    @Singleton
    @Provides
    fun provideRepo(): TicketRepo {
        return FakeAndroidRepoImpl()
    }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModule::class]
)
object DataSourceTestModule {

    @Singleton
    @Provides
    fun provideRealtimeDataSource(): RealtimeDataSource {
        return FakeAndroidDataSource()
    }
}