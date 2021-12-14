package com.iamomidk.helperlib.helper.extentions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.core.text.HtmlCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.iamomidk.helper.PersianCalendar
import com.iamomidk.helperlib.helper.abstracts.AbstractSwipeHelper
import com.iamomidk.helperlib.helper.constants.Constants.SHARE_VIA
import com.iamomidk.helperlib.helper.logger.Logger
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

val TAG = "Extentions_TAG"

private val persianNumbers = arrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

val Number.toLocalLang: String
	get() = buildString {
		for (element in this@toLocalLang.toString()) when (element) {
			in '0'..'9' -> (element.toString().toInt()).apply { append(persianNumbers[this]) }
			else -> append(element)
		}
	}

val String.toLocaleLang: String
	get() = if (isEmpty()) "" else buildString {
		for (element in this@toLocaleLang) when (element) {
			in '0'..'9' -> (element.toString().toInt()).apply { append(persianNumbers[this]) }
			else -> append(element)
		}
	}

val String.toEnglish: String
	get() = try {
		when {
			isEmpty() -> "0"
			else -> BigDecimal(
				replace('−', '-')
					.replace('٫', '.')
					.replace("٬", "")
					.replace(',', '.')
					.replace(".", ".").trim()
			).toString()
		}
	} catch (e: Exception) {
		"0"
	}

fun share(context: Context, content: String, subject: String) = try {
	Logger.i(TAG, "sharing this $content from this $subject")
	Intent(Intent.ACTION_SEND).apply {
		type = "text/plain"
		putExtra(Intent.EXTRA_SUBJECT, subject)
		putExtra(Intent.EXTRA_TEXT, content)
		context.startActivity(Intent.createChooser(this, SHARE_VIA))
	}
} catch (e: Exception) {
	Logger.e(
		tag = TAG,
		msg = "errorMessage : ${e.message}",
		throwable = e
	)
}

fun openUrl(context: Context, url: String) = try {
	Logger.i(TAG, "opening this $url")
	val openUrlIntent = Intent(Intent.ACTION_VIEW).apply {
		flags = Intent.FLAG_ACTIVITY_NEW_TASK
		data = Uri.parse(url)
	}
	context.startActivity(openUrlIntent)
} catch (e: Exception) {
	Logger.e(
		tag = TAG,
		msg = "errorMessage : ${e.message}",
		throwable = e
	)
}

val String.convertArabicToPersianAlphabets: String
	get() = StringBuilder().apply {
		for (c in this@convertArabicToPersianAlphabets) append(
			when (c.toString()) {
				"ئ" -> "ی"
				"ؤ" -> "و"
				"إ" -> "ا"
				"أ" -> "ا"
				"ك" -> "ک"
				"دِ" -> "د"
				"بِ" -> "ب"
				"زِ" -> "ز"
				"ذِ" -> "ذ"
				"شِ" -> "ش"
				"سِ" -> "س"
				"ى" -> "ی"
				"ي" -> "ی"
				else -> c.toString()
			}
		)
	}.toString()

fun processHtml(s: String): Spanned = HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_LEGACY)

fun RecyclerView.onEndReached(sendReq: () -> Unit) = addOnScrollListener(
	object : RecyclerView.OnScrollListener() {
		private var invoked = false
		override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
			super.onScrollStateChanged(recyclerView, newState)
			if (!recyclerView.canScrollVertically(1) and !invoked) {
				invoked = true
				sendReq()
				Logger.i(TAG, "onEndReached")
			}
		}
	}
)

val onItemTouchListener: RecyclerView.OnItemTouchListener =
	object : RecyclerView.OnItemTouchListener {
		override fun onInterceptTouchEvent(rv: RecyclerView, me: MotionEvent): Boolean {
			if (me.action == MotionEvent.ACTION_DOWN) rv.parent?.requestDisallowInterceptTouchEvent(true)
			return false
		}

		override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) = Unit
		override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit
	}

fun RecyclerView.addSwipeToAction(
	swipeDirs: Int,
	icon: Drawable?,
	backgroundColor: Int,
	onSwipeListener: (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit,
) {
	ItemTouchHelper(
		object : AbstractSwipeHelper(
			swipeDirs = swipeDirs,
			icon = icon,
			backgroundColor = backgroundColor
		) {
			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
				onSwipeListener(viewHolder, direction)
		}
	).attachToRecyclerView(this)
}

val Calendar.timeInDays: Long
	get() = Calendar.getInstance().apply {
		time = Date(this@timeInDays.timeInMillis)
		timeZone = TimeZone.getTimeZone("GMT")
		set(Calendar.HOUR_OF_DAY, 0)
		set(Calendar.MINUTE, 0)
		set(Calendar.SECOND, 0)
		set(Calendar.MILLISECOND, 0)
	}.timeInMillis / 1000

fun Long.setDate(date: (suffix: String, mainText: String) -> Unit) {
	TimeUnit.DAYS.convert(this, TimeUnit.SECONDS).also {
		val months = it / 30
		val years = it / 365
		when {
			months < 1 -> date(it.toLocalLang, "روز")
			years < 1 -> date(months.toLocalLang, "ماه")
			else -> date(years.toLocalLang, "سال")
		}
	}
}

val Long.toDateWithMonth: String
	@SuppressLint("SimpleDateFormat")
	get() {
		var jDate = ""
		var year: String
		var month: String
		var day: String
		var todayYear: String
		try {
			SimpleDateFormat("yyyy/MM/dd").apply {
				jDate = PersianCalendar(format(Date(this@toDateWithMonth * 1000))).persianDate
				year = jDate.split("/")[0]
				month = jDate.split("/")[1]
				day = jDate.split("/")[2]
				jDate = "$day/$month"
				SimpleDateFormat("yyyy/MM/dd").apply {
					todayYear =
						PersianCalendar(format(Date(System.currentTimeMillis()))).persianDate.split("/")[0]
				}
				if (year != todayYear) jDate += "/$year"
			}
		} catch (e: Exception) {
			e.toString()
		}
		return jDate.toLocaleLang
	}

val Long.toDateWithMonthName: String
	@SuppressLint("SimpleDateFormat")
	get() {
		var jDate = ""
		var year: String
		var month: String
		var day: String
		var todayYear: String
		try {
			SimpleDateFormat("yyyy/MM/dd").apply {
				jDate = PersianCalendar(format(Date(this@toDateWithMonthName * 1000))).persianDate
				year = jDate.split("/")[0]
				month = jDate.split("/")[1]
				day = jDate.split("/")[2]
				jDate = when (month) {
					"1" -> "$day فروردین"
					"2" -> "$day اردیبهشت"
					"3" -> "$day خرداد"
					"4" -> "$day تیر"
					"5" -> "$day مرداد"
					"6" -> "$day شهریور"
					"7" -> "$day مهر"
					"8" -> "$day آبان"
					"9" -> "$day آذر"
					"10" -> "$day دی"
					"11" -> "$day بهمن"
					"12" -> "$day اسفند"
					else -> ""
				}
				SimpleDateFormat("yyyy/MM/dd").apply {
					todayYear =
						PersianCalendar(format(Date(System.currentTimeMillis()))).persianDate.split("/")[0]
				}
				if (year != todayYear) jDate += " - $year"
			}
		} catch (e: Exception) {
			e.toString()
		}
		return jDate.toLocaleLang
	}

val Long.toDate: String
	@SuppressLint("SimpleDateFormat")
	get() {
		var jDate = ""
		try {
			SimpleDateFormat("yyyy/MM/dd").apply {
				jDate = PersianCalendar(format(Date(this@toDate.times(1000)))).persianDate
			}
		} catch (e: Exception) {
			e.toString()
		}
		return jDate.toLocaleLang
	}

@SuppressLint("SimpleDateFormat")
fun Long.getDate(date: (year: Int, month: Int, day: Int) -> Unit) {
	var jDate = ""
	try {
		SimpleDateFormat("yyyy/MM/dd").apply {
			jDate = PersianCalendar(
				format(
					Date(
						this@getDate.times(1000)
					)
				)
			).persianDate
		}
	} catch (e: Exception) {
		e.toString()
	}
	jDate.split('/').apply {
		date.invoke(get(0).toInt(), get(1).toInt(), get(2).toInt())
	}
}

val Long.toTime: String
	@SuppressLint("SimpleDateFormat")
	get() {
		var time = ""
		try {
			SimpleDateFormat("HH:mm").apply {
				timeZone = TimeZone.getDefault()
				time = format(Date(this@toTime.times(1000)))
			}
		} catch (e: Exception) {
			e.toString()
		}
		return time.toLocaleLang
	}

val Long.dayOfWeek: String
	get() {
		val today = Calendar.getInstance(TimeZone.getDefault())
			.apply { time = Date(System.currentTimeMillis()) }.get(Calendar.DAY_OF_YEAR)
		val tsDay = Calendar.getInstance(TimeZone.getDefault())
			.apply { time = Date(this@dayOfWeek * 1000) }.get(Calendar.DAY_OF_YEAR)
		return when {
			today - tsDay == 0 -> "امروز"
			today - tsDay == 1 -> "دیروز"
			else -> toDate
		}
	}

val View.makeGoneWithAnimation
	get() = apply {
		alpha = 1f
		translationX = 0f
		animate().apply {
			duration = 500
			translationX = width.toFloat()
			setListener(
				object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator?) {
						super.onAnimationEnd(animation)
						makeGone
						clearAnimation()
					}
				}
			)
			alpha(0f)
			start()
		}
	}

val View.makeVisibleWithFadeAnimation
	get() = apply {
		alpha = 0f
		translationX = width.toFloat()
		makeVisible
		animate().apply {
			duration = 500
			translationX = 0f
			setListener(
				object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator?) {
						super.onAnimationEnd(animation)
						clearAnimation()
					}
				}
			)
			alpha(1f)
			start()
		}
	}

fun Number.withDigits(digits: Int = 0): String = DecimalFormat().apply {
	maximumFractionDigits = 8
	roundingMode = RoundingMode.UP
}.format(this)

inline fun View.showIf(crossinline condition: (View) -> Boolean): Int {
	if (condition(this)) makeVisible else makeGone
	return visibility
}

inline fun View.hideIf(crossinline condition: (View) -> Boolean): Int {
	if (condition(this)) makeGone else makeVisible
	return visibility
}

fun View.moveToBottomAnim(endAction: () -> Unit) {
	makeVisible
	alpha = 0f
	translationY = 50f
	animate().apply {
		alpha(1f)
		translationYBy(-50f)
		duration = 1500
		withEndAction {
			clearAnimation()
			endAction.invoke()
		}
	}
}

fun Context.getColor(@ColorRes resId: Int): Int = resources.getColor(resId, null)

val View.makeGone get() = true.also { isGone = it }
val View.makeInvisible get() = true.also { isInvisible = it }
val View.makeVisible get() = true.also { isVisible = it }

fun Number?.convertNumbersOnly(
	digits: Int = 8
): String {
	this ?: return "0".toLocaleLang
	return when (this) {
		is Double -> ensureDigits(digits).format("%,d", this).toLocaleLang
		else -> "%,d".format(this).toLocaleLang
	}
}

fun Double.ensureDigits(fractionDigits: Int): String {
	val minus = this < 0
	var intPart: String
	"%.${fractionDigits}f".format(abs(this)).also { formattedNum ->
		formattedNum.split('.').also {
			intPart = it[0]
			return when {
				intPart.length >= 8 || fractionDigits == 0 -> intPart
				else -> {
					var res = formattedNum.take(9)
					if (formattedNum.endsWith(".0") or formattedNum.endsWith(".00") or formattedNum.endsWith(".000"))
						res.trimEnd('0')
					if (res.endsWith(".")) res = res.take(res.length - 1)
					if (minus) "-$res" else res
				}
			}.toDouble().withDigits()
		}
	}
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) =
	addTextChangedListener(
		object : TextWatcher {
			override fun afterTextChanged(editable: Editable?) =
				afterTextChanged.invoke(editable.toString())

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
		}
	)