package com.iamomidk.helper.sharedPrefrences

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.SharedPreferences
import com.iamomidk.helper.constants.Constants.SHP_DARK_MOOD
import com.iamomidk.helper.constants.Constants.SHP_INTRO_PLAYED
import com.iamomidk.helper.constants.Constants.SHP_KEY
import com.iamomidk.helper.constants.Constants.SHP_SERVER_ID

object ShpHelper {

	private val Context.shp: SharedPreferences
		get() = getSharedPreferences(SHP_KEY, Context.MODE_PRIVATE)

	var Context.darkMode: Boolean
		get() = shp.getBoolean(SHP_DARK_MOOD, false)
		set(value) = shp.edit().apply { putBoolean(SHP_DARK_MOOD, value) }.apply()

	var Context.introPlayed: Boolean
		get() = shp.getBoolean(SHP_INTRO_PLAYED, false)
		set(value) = shp.edit().apply { putBoolean(SHP_INTRO_PLAYED, value) }.apply()

	var Context.serverId: Long?
		get() = shp.getLong(SHP_SERVER_ID, 0)
		set(value) = shp.edit().apply { value?.let { putLong(SHP_SERVER_ID, it) } }.apply()

	fun Context.clearCache() {
		kotlin.runCatching {
			(shp.edit().clear().apply())
			cacheDir.deleteRecursively()
			dataDir.deleteRecursively()
			codeCacheDir.deleteRecursively()
			externalCacheDir?.deleteRecursively()
			filesDir.deleteRecursively()
			noBackupFilesDir.deleteRecursively()
			obbDir.deleteRecursively()
			(getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
		}.onFailure { it.printStackTrace() }
	}

}