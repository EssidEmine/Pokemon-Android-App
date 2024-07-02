package com.bforbank.pokemon.utils

inline fun <T> List<T>.filterByText(text: String, predicate: (T) -> String): List<T> {
    return this.filter { item ->
        predicate(item).startsWith(text, ignoreCase = true)
    }
}
