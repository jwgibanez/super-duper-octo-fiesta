package io.github.jwgibanez.bitexplorer

import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate

fun <T> check(consumer: Consumer<T>): Predicate<T> {
    return Predicate<T> { t ->
        consumer.accept(t)
        true
    }
}