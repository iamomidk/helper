package com.iamomidk.helperlib.helper.abstracts

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamomidk.helperlib.helper.clickListener.OnDismissListener
import com.iamomidk.helperlib.helper.clickListener.OnItemSelectedListener
import com.iamomidk.helperlib.R

@Suppress("DEPRECATION")
abstract class AbstractBottomSheetDialogFragment<T> : BottomSheetDialogFragment(),
	AbstractFrgInterface, OnItemSelectedListener<T>, OnDismissListener {

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
		super.onCreateDialog(savedInstanceState).apply {
			setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
			setOnShowListener {
				STATE_EXPANDED.also {
					from(findViewById(com.google.android.material.R.id.design_bottom_sheet)).state = it
				}
			}
			circularProgressDrawable = CircularProgressDrawable(context).apply {
				strokeWidth = 5f
				centerRadius = 30f
				start()
			}
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BottomSheetDialog)
	}

	override fun onDismiss(dialog: DialogInterface) {
		onDismiss?.onDismiss()
		super<BottomSheetDialogFragment>.onDismiss(dialog)
	}
}