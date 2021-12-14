@file:Suppress("DEPRECATION")

package com.iamomidk.helperlib.helper.abstracts

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.iamomidk.helper.logger.Logger
import com.iamomidk.helper.sharedPrefrences.ShpHelper.darkMode

abstract class AbstractActivity : AppCompatActivity() {

	@Suppress("PropertyName")
	val TAG: String = this.javaClass.simpleName + "_TAG"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		when {
			applicationContext.darkMode -> AppCompatDelegate.MODE_NIGHT_YES
			else -> AppCompatDelegate.MODE_NIGHT_NO
		}.also { AppCompatDelegate.setDefaultNightMode(it) }
	}

	val Int.resultLauncher: ActivityResultLauncher<Intent>
		get() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
			Logger.d(TAG, "result $result")
		}

	fun removeFragment(@IdRes fragmentId: Int) = try {
		Logger.i(TAG, "removing fragment")
		supportFragmentManager.apply {
			beginTransaction().apply {
				setCustomAnimations(
					android.R.anim.fade_in,  // enter
					android.R.anim.fade_out, // exit
					android.R.anim.fade_in,  // popEnter
					android.R.anim.fade_out  // popExit
				)
				if (findFragmentById(fragmentId) != null) remove(findFragmentById(fragmentId)!!)
				commit()
				popBackStack()
			}
		}
	} catch (e: Exception) {
		Logger.e(TAG, "error message : ${e.message}", e)
	}

	fun addFragment(@IdRes fragmentId: Int, fragment: AbstractFragment) = try {
		supportFragmentManager.apply {
			beginTransaction().apply {
				if (findFragmentById(fragmentId) == null) {
					Logger.i(TAG, "adding fragment fragmentName : ${fragment::class.java.simpleName}")
					setCustomAnimations(
						android.R.anim.fade_in,   // enter
						android.R.anim.fade_out,  // exit
						android.R.anim.fade_in,   // popEnter
						android.R.anim.fade_out   // popExit
					)
					add(fragmentId, fragment, fragment::class.java.simpleName)
				} else {
					Logger.i(TAG, "replacing ${javaClass.simpleName} with ${fragment::class.java.simpleName}")
					setCustomAnimations(
						android.R.anim.fade_in,   // enter
						android.R.anim.fade_out,  // exit
						android.R.anim.fade_in,   // popEnter
						android.R.anim.fade_out   // popExit
					)
					replace(fragmentId, fragment, fragment::class.java.simpleName)
				}
				disallowAddToBackStack()
				commit()
			}
		}
	} catch (e: Exception) {
		Logger.e(TAG, "error message : ${e.message}", e)
	}

	override fun recreate() {
		finish()
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
		startActivity(intent)
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
	}
}