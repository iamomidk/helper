package com.iamomidk.helper.abstracts

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamomidk.helper.logger.Logger
import com.iamomidk.helper.router.OnNavigate

interface AbstractFrgInterface {
	val TAG: String
		get() = javaClass.simpleName + "_TAG"

	var circularProgressDrawable: CircularProgressDrawable

	fun Fragment.trackByFirebase(
		name: String = "",
		isAppOpen: Boolean = false,
		isSignUp: Boolean = false,
		isSignIn: Boolean = false,
	) {
		if (!isAdded or isDetached) return
		try {
			val bundle = Bundle().apply {
				when {
					isAppOpen -> putString(FirebaseAnalytics.Param.SCREEN_NAME, "App opened")
					isSignIn -> putString(FirebaseAnalytics.Param.SCREEN_NAME, "Signed In")
					isSignUp -> putString(FirebaseAnalytics.Param.SCREEN_NAME, "Signed Up")
				}
				if (name.isNotEmpty()) putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
			}
			FirebaseAnalytics.getInstance(requireContext()).apply {
				when {
					isAppOpen -> logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
					isSignIn -> logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
					isSignUp -> logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
				}
				logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
			}
		} catch (e: Exception) {
			Logger.e(
				tag = TAG,
				msg = "errorMessage : ${e.message}",
				throwable = e
			)
		}
	}

	var rootLayout: View

	fun onDispose() = Unit
	fun onErrorHandler() = Unit

	val Activity.hideKeyboard: Boolean
		get() = try {
			(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
				.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
		} catch (e: RuntimeException) {
			false
		}

	fun Fragment.getColor(i: Int): Int = ContextCompat.getColor(requireContext(), i)

	fun Activity.navigate(targetId: Int) =
		(this as? OnNavigate)?.apply { navigateTo(targetId = targetId) }

	fun AppCompatImageView.loadImageResourceWithGlide(resId: Int) =
		Glide.with(this).load(resId).placeholder(circularProgressDrawable).into(this)

	fun AppCompatImageView.loadImageResourceWithGlide(url: String) = try {
		Glide.with(this).load(GlideUrl(url)).centerCrop().placeholder(circularProgressDrawable)
			.diskCacheStrategy(DiskCacheStrategy.ALL).into(this)
	} catch (e: Exception) {
		Logger.e(tag = TAG, throwable = e)
	}

	fun View.snackBar(
		text: String, duration: Int = Snackbar.LENGTH_SHORT,
	): Snackbar = Snackbar.make(this, text, duration).apply { show() }

	fun View.snackBar(
		@StringRes text: Int,
		duration: Int = Snackbar.LENGTH_SHORT,
	): Snackbar = snackBar(text = context.getString(text), duration = duration)

}