package com.iamomidk.helperlib.helper.clickListener

interface OnItemSelectedListener<T> {

	var onItemSelected: OnItemSelected<T>?

	/**
	 * an interface for passing data between fragments
	 * @param T is the type of passing items
	 * */
	interface OnItemSelected<T> {
		fun onItemSelected(item: T, id: Int = 0)
	}

	fun onItemSelected(onItemSelected: (item: T, id: Int) -> Unit) {
		object : OnItemSelected<T> {
			override fun onItemSelected(item: T, id: Int) {
				onItemSelected(item, id)
			}
		}.also { this.onItemSelected = it }
	}
}