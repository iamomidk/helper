package com.iamomidk.helper.clickListener

interface OnItemClickListener<T> {

	var onItemClickListener: OnItemClickListener<T>?

	/**
	 * Item Click Listener callback Interface
	 * @param T Type of item Extends [Object]
	 * */
	interface OnItemClickListener<T> {
		fun onClicked(actionId: Int, position: Int, item: T)
	}

	fun onItemClickListener(onItemClickListener: (actionId: Int, position: Int, item: T) -> Unit) {
		object : OnItemClickListener<T> {
			override fun onClicked(actionId: Int, position: Int, item: T) {
				onItemClickListener(actionId, position, item)
			}
		}.also { this.onItemClickListener = it }
	}

}