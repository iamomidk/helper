package com.iamomidk.helperlib.helper.clickListener

interface OnDismissListener {
	var onDismiss: OnDismiss?

	interface OnDismiss {
		fun onDismiss()
	}

	fun onDismiss(onDismiss: () -> Unit) {
		object : OnDismiss {
			override fun onDismiss() = onDismiss()
		}.also { this.onDismiss = it }
	}
}