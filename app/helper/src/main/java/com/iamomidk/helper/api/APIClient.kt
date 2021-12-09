package com.iamomidk.helper.api

import com.google.gson.GsonBuilder
import com.iamomidk.helper.BuildConfig
import com.iamomidk.helper.constants.Constants.BASE_URL
import com.iamomidk.helper.constants.Constants.CONNECT_TIMEOUT
import com.iamomidk.helper.constants.Constants.READ_TIMEOUT
import com.iamomidk.helper.constants.Constants.WRITE_TIMEOUT
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit.MILLISECONDS

object APIClient {

	fun defaultClient(url: String = BASE_URL): Retrofit {
		val interceptor = Interceptor { chain ->
			val original = chain.request()
			val builder = original.newBuilder()
			val request = builder.method(original.method, original.body).build()
			chain.proceed(request)
		}

		val client = OkHttpClient.Builder().apply {
			connectTimeout(CONNECT_TIMEOUT, MILLISECONDS)
			readTimeout(READ_TIMEOUT, MILLISECONDS)
			writeTimeout(WRITE_TIMEOUT, MILLISECONDS)
			addNetworkInterceptor(interceptor)
			addInterceptor(interceptor)
			// uncomment the following line if you have ssl trust issues
			/*arrayOf<TrustManager>(
				object : X509TrustManager {
					@Throws(CertificateException::class)
					override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) = Unit

					@Throws(CertificateException::class)
					override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) = Unit
					override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
				}
			).also { trustAllCerts ->
				SSLContext.getInstance("SSL").apply {
					init(null, trustAllCerts, SecureRandom())
					socketFactory.also { sslSocketFactory ->
						sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
					}
				}
				// Create an ssl socket factory with our all-trusting manager
				hostnameVerifier { hostname, session -> true }
			}*/
			if (BuildConfig.BUILD_TYPE.contains("debug"))
				addInterceptor(
					HttpLoggingInterceptor().apply {
						level = HEADERS
						level = BODY
					}
				)
		}

		client.build().also { c ->
			return Retrofit.Builder().apply {
				baseUrl(url)
				addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
				addConverterFactory(ScalarsConverterFactory.create())
				addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				client(c)
			}.build()
		}
	}
}