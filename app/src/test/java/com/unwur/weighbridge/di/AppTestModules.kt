package com.unwur.weighbridge.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [DatabaseModule::class]
//)
//object DatabaseTestModule {
//
//    @Singleton
//    @Provides
//    fun provideRealtimeDatabase(): DatabaseReference {
//        return Firebase.database.reference.child("wbs")
//    }
//}