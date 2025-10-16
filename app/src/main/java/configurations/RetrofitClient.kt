package configurations

import endpoints.FootballApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.football-data.org/v4/"
    private const val API_KEY = "6634b56860b449bd8fd1446d9678b33f"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Interceptor para la API key
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("X-Auth-Token", API_KEY)
            .build()
        chain.proceed(requestWithAuth)
    }

    // Client con el interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // Función para obtener el servicio
    fun getFootballApiService(): FootballApiService {
        return retrofit.create(FootballApiService::class.java)
    }
}