package com.iamomidk.helperlib.helper.abstracts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.iamomidk.helper.logger.Logger

abstract class AbstractFragment : Fragment(), AbstractFrgInterface {

	override lateinit var circularProgressDrawable: CircularProgressDrawable
	override lateinit var rootLayout: View

	override fun onResume() {
		super.onResume()
		trackByFirebase()
		Logger.w(TAG, javaClass.simpleName)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? = super.onCreateView(inflater, container, savedInstanceState)?.apply {
		Logger.w(TAG, javaClass.simpleName)
		circularProgressDrawable = CircularProgressDrawable(context).apply {
			strokeWidth = 5f
			centerRadius = 30f
			start()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		onDispose()
	}

}