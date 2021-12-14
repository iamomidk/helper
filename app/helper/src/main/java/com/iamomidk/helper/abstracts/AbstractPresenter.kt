package com.iamomidk.helperlib.helper.abstracts

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.iamomidk.helper.api.APIClient
import com.iamomidk.helper.constants.Constants.BASE_URL
import com.iamomidk.helper.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.io.StringReader
import java.util.*

abstract class AbstractPresenter<T, R> {

	val TAG = javaClass.simpleName + "_TAG"

	var disposable: Disposable? = null
	var listener: T? = null

	open fun onAttach(listener: T) {
		Logger.i(TAG, "attached")
		listener.also { this.listener = it }
	}

	open fun onDetach() {
		Logger.i(TAG, "detached")
		onDispose()
	}

	open fun onDispose() {
		Logger.i(TAG, "disposed")
		disposable?.dispose()
	}

	val headers: HashMap<String, String>
		get() = HashMap<String, String>()

	val Response<R>.errorBody: String
		get() = errorBody()?.charStream()?.readText()?.replace("\"", "") ?: ""

	val Response<R>.body: R?
		get() = body()

	val Response<R>.responseCode: Int
		get() = code()

	val Response<R>.headers: String
		get() = "${headers()}"

	val Response<R>.message: String?
		get() = message()

	var contents: JsonObject = JsonObject()

	fun content(content: (JsonObject) -> JsonObject) {
		contents = content(JsonObject())
	}

	val String.reader: JsonReader
		get() = JsonReader(StringReader(this))
	private val gsonBuilder =
		GsonBuilder().setLenient().serializeNulls().serializeSpecialFloatingPointValues()
			.setPrettyPrinting().create()

	interface OnRequest<R> {
		fun onSuccessful(response: R?)
		fun onFailure(response: R?)
		fun onError(e: Throwable)
	}

	var onResponse: OnResponse<R>? = null
	var onFailure: OnFailure<R>? = null
	var onError: OnError? = null

	interface OnResponse<R> {
		fun onResponse(response: Response<R>?)
	}

	interface OnFailure<R> {
		fun onFailure(response: String?)
	}

	interface OnError {
		fun onError(e: Throwable)
	}

	fun onResponse(onResponse: (responseBody: Response<R>?) -> Unit) = object : OnResponse<R> {
		override fun onResponse(response: Response<R>?) = onResponse(response)
	}.also { this.onResponse = it }

	fun onFailure(onFailure: (responseBody: String?) -> Unit) = object : OnFailure<R> {
		override fun onFailure(response: String?) = onFailure(response)
	}.also { this.onFailure = it }

	fun onError(onError: (e: Throwable) -> Unit) = object : OnError {
		override fun onError(e: Throwable) = onError(e)
	}.also { this.onError = it }

	private fun Retrofit.sendReq(
		onRequestListener: OnRequest<Response<R>>,
		observable: (Retrofit) -> Observable<Response<R>?>?,
	): Job = CoroutineScope(Dispatchers.IO).launch {
		kotlin.runCatching {
			observable(this@sendReq)?.observeOn(Schedulers.newThread())
				?.observeOn(AndroidSchedulers.mainThread())?.subscribeWith(
					object : DisposableObserver<Response<R>?>() {
						override fun onNext(response: Response<R>) = with(onRequestListener) {
							if (response.isSuccessful) onSuccessful(response) else onFailure(response)
						}

						override fun onError(e: Throwable) = onRequestListener.onError(e)
						override fun onComplete() = onDispose()
					}
				).also { disposable = it }
		}.onFailure { onRequestListener.onError(it) }
	}

	fun safeRequest(apiInterface: (Retrofit) -> Observable<Response<R>?>?) {
		sendReq(apiInterface, BASE_URL).start()
	}

	private fun sendReq(
		apiInterface: (Retrofit) -> Observable<Response<R>?>?,
		ip: String,
	): Job = APIClient.defaultClient(ip).sendReq(
		object : OnRequest<Response<R>> {
			override fun onSuccessful(response: Response<R>?) {
				response?.also { res ->
					onResponse?.onResponse(res)
					Logger.prettyPrinter(TAG, "request was Successful -> ", "${res.body}")
				}
			}

			override fun onFailure(response: Response<R>?) {
				response?.errorBody?.also { res ->
					onFailure?.onFailure(res)
					Logger.e(TAG, "request was Unsuccessful -> $res")
				}
			}

			override fun onError(e: Throwable) {
				Logger.e(TAG, e)
				onError?.onError(e)
			}
		}
	) { apiInterface(APIClient.defaultClient(ip)) }
}