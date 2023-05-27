package com.example.listed.di

import android.app.Application
import com.example.listed.data.local.AuthManager
import com.example.listed.data.remote.ApiInterface
import com.example.listed.data.repository.ListedRepoImpl
import com.example.listed.domain.repository.ListedRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApi(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://api.inopenapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRepo(api: ApiInterface):ListedRepo{
        return ListedRepoImpl(api)
    }

    @Provides
    @Singleton
    fun getAuthManager(app:Application):AuthManager{
        return AuthManager(app)
    }
}