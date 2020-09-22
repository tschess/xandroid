package io.tschess.tschess.model

class ExtendedDataHolder(
    val extras: MutableMap<String, Any> = HashMap()
) {

    fun getInstance(): ExtendedDataHolder {
        return instance
    }

    fun putExtra(name: String, any: Any) {
        extras[name] = any
    }

    fun getExtra(name: String): Any? {
        return extras[name]
    }

    fun hasExtra(name: String): Boolean {
        return extras.containsKey(name)
    }

    fun clear() {
        extras.clear()
    }

    companion object {
        val instance = ExtendedDataHolder()
    }
}