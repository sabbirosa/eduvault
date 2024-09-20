package com.sabbirosa.eduvault.backend.local

import com.sabbirosa.eduvault.backend.remote.Api
import com.sabbirosa.eduvault.backend.remote.EduVaultService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {
    @Provides
    @Singleton
    fun provideRealm(): Realm {
        return LocalServer.realm
    }

    @Provides
    @Singleton
    fun provideRemoteApi(): EduVaultService {
        return Api.retrofitService
    }
}