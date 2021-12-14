package com.iamomidk.helper.logger

import android.util.Log
import com.iamomidk.helperlib.BuildConfig.BUILD_TYPE
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Logger {

	fun d(tag: String, msg: String) {
		when {
			BUILD_TYPE.contains("debug") -> Log.d(tag, msg)
			else -> Log.d(tag, msg)
		}
	}

	fun d(tag: String, msg: String, throwable: Throwable) {
		when {
			BUILD_TYPE.contains("debug") -> Log.d(
				tag,
				buildString {
					append("fileName -> ${Thread.currentThread().stackTrace[4].fileName}")
					appendLine()
					append("className -> ${Thread.currentThread().stackTrace[4].className}")
					appendLine()
					append("lineNumber -> ${Thread.currentThread().stackTrace[4].lineNumber}")
					appendLine()
					append("methodName -> ${Thread.currentThread().stackTrace[4].methodName}")
					appendLine()
					append("message -> $msg")
				},
				throwable
			)
			else -> return
		}
	}

	private val gsonBuilder: Gson
		get() = GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().setPrettyPrinting()
			.create()

	fun prettyPrinter(tag: String, src: Any, isError: Boolean = false) {
		when {
			BUILD_TYPE.contains("debug") -> when (isError) {
				true -> Log.e(tag, gsonBuilder.toJson(src))
				false -> Log.i(tag, gsonBuilder.toJson(src))
			}
			else -> return
		}
	}

	fun prettyPrinter(tag: String, msg: String, src: Any, isError: Boolean = false) {
		when {
			BUILD_TYPE.contains("debug") -> when (isError) {
				true -> Log.e(tag, "$msg -> ${gsonBuilder.toJson(src)}")
				false -> Log.i(tag, "$msg -> ${gsonBuilder.toJson(src)}")
			}
			else -> return
		}
	}

	fun i(tag: String, msg: String?) {
		when {
			BUILD_TYPE.contains("debug") -> Log.i(
				tag, "${Thread.currentThread().stackTrace[4].methodName} -> $msg"
			)
			else -> return
		}
	}

	fun i(tag: String, msg: String, throwable: Throwable) {
		when {
			BUILD_TYPE.contains("debug") -> Log.i(
				tag, "${Thread.currentThread().stackTrace[4].methodName} -> $msg", throwable
			)
			else -> return
		}
	}

	fun w(tag: String, msg: String) {
		when {
			BUILD_TYPE.contains("debug") -> Log.w(
				tag, "${Thread.currentThread().stackTrace[4].methodName} -> $msg"
			)
			else -> return
		}
	}

	fun e(tag: String, msg: String, throwable: Throwable) {
		when {
			BUILD_TYPE.contains("debug") -> Log.e(
				tag, "${Thread.currentThread().stackTrace[4].methodName} -> $msg", throwable
			)
			else -> return
		}
	}

	fun e(tag: String, throwable: Throwable) {
		when {
			BUILD_TYPE.contains("debug") -> Log.e(
				tag,
				buildString {
					append("fileName -> ${Thread.currentThread().stackTrace[4].fileName}")
					appendLine()
					append("className -> ${Thread.currentThread().stackTrace[4].className}")
					appendLine()
					append("lineNumber -> ${Thread.currentThread().stackTrace[4].lineNumber}")
					appendLine()
					append("methodName -> ${Thread.currentThread().stackTrace[4].methodName}")
					appendLine()
					append("errorMessage -> ${throwable.message}")
					appendLine()
					append("errorCause -> ${throwable.cause}")
				},
				throwable
			)
			else -> return
		}
	}

	fun e(tag: String, msg: String) {
		when {
			BUILD_TYPE.contains("debug") -> Log.e(
				tag, "${Thread.currentThread().stackTrace[4].methodName} -> $msg"
			)
			else -> return
		}
	}
}