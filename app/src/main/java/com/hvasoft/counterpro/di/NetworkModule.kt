package com.hvasoft.counterpro.di

import com.hvasoft.counterpro.BuildConfig
import com.hvasoft.counterpro.data.remote_db.service.CounterApi
import com.hvasoft.counterpro.data.util.DataConstants.CONNECT_TIMEOUT
import com.hvasoft.counterpro.data.util.DataConstants.READ_TIMEOUT
import com.hvasoft.counterpro.data.util.DataConstants.WRITE_TIMEOUT
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun okHttpClientProvider(): OkHttpClient =
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(loggingInterceptorProvider())
        }.build()

    private fun loggingInterceptorProvider(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesCounterApi(retrofit: Retrofit): CounterApi = retrofit.create()

}