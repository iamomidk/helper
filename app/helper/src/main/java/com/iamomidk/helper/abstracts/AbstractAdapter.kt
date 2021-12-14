package com.iamomidk.helperlib.helper.abstracts

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.iamomidk.helper.clickListener.OnItemClickListener
import com.iamomidk.helper.logger.Logger

abstract class AbstractAdapter<VH : RecyclerView.ViewHolder?, T> : RecyclerView.Adapter<VH>,
	OnItemClickListener<T> {

	val TAG: String = this.javaClass.simpleName + "_TAG"

	companion object {
		const val NOT_SWEEPABLE = 100

	}

	constructor(context: Context, items: List<T>) : super() {
		inflater = LayoutInflater.from(context)
		ctxAdapter = context
		this.items = items
		circularProgressDrawable = CircularProgressDrawable(ctxAdapter).apply {
			strokeWidth = 5f
			centerRadius = 30f
			start()
		}
	}

	constructor(context: Context) : super() {
		inflater = LayoutInflater.from(context)
		ctxAdapter = context
		this.items = mutableListOf()
		circularProgressDrawable = CircularProgressDrawable(ctxAdapter).apply {
			strokeWidth = 5f
			centerRadius = 30f
			start()
		}
	}

	/**
	 * Get T typed item [List]
	 * @return T typed item [List]
	 */
	var items: List<T>
	var ctxAdapter: Context
	var inflater: LayoutInflater

	/**
	 * @param position Index of target item
	 * @return T type
	 */
	fun getItem(position: Int): T = items[position]

	/**
	 * a function to getColor from colorResource
	 * @return colorResInt type
	 * */
	fun getColor(@ColorRes resId: Int): Int = ctxAdapter.resources.getColor(resId, null)

	val Int.getColor: Int
		get() = ctxAdapter.resources.getColor(this, null)

	/**
	 * Get size of Items
	 * @return Item size
	 */
	override fun getItemCount(): Int = items.size

	var circularProgressDrawable: CircularProgressDrawable

	/**
	 * a function for setting the Image Resource using Glide
	 * @param resId is the image resourceId
	 */
	open fun ImageView.setImageResourceWithGlide(resId: Int) = try {
		Glide.with(this).load(resId).placeholder(circularProgressDrawable).into(this)
	} catch (e: Exception) {
		Logger.e(tag = TAG, throwable = e)
	}

	open fun AppCompatImageView.setImageResourceWithGlide(resId: Int) = try {
		Glide.with(this).load(resId).placeholder(circularProgressDrawable).into(this)
	} catch (e: Exception) {
		Logger.e(tag = TAG, throwable = e)
	}

	fun AppCompatImageView.setImageResourceWithGlide(url: String) = try {
		Glide.with(this).load(GlideUrl(url)).centerCrop().placeholder(circularProgressDrawable)
			.diskCacheStrategy(DiskCacheStrategy.ALL).into(this)
	} catch (e: Exception) {
		Logger.e(tag = TAG, throwable = e)
	}
}