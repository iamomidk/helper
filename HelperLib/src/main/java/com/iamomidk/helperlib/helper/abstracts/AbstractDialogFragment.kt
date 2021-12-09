@file:Suppress("DEPRECATION")

package com.iamomidk.helper.abstracts

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.iamomidk.helper.R
import com.iamomidk.helper.clickListener.OnDismissListener
import com.iamomidk.helper.clickListener.OnItemSelectedListener

abstract class AbstractDialogFragment<T> : DialogFragment(), AbstractFrgInterface,
	OnItemSelectedListener<T>, OnDismissListener {

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
		super.onCreateDialog(savedInstanceState).apply {
			setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
			circularProgressDrawable = CircularProgressDrawable(context).apply {
				strokeWidth = 5f
				centerRadius = 30f
				start()
			}
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
	}

	override fun show(manager: FragmentManager, tag: String?) {
		if (!isAdded or isDetached) super.show(manager, tag) else return
	}

	override fun onDismiss(dialog: DialogInterface) {
		onDismiss?.onDismiss()
		super<DialogFragment>.onDismiss(dialog)
	}
}