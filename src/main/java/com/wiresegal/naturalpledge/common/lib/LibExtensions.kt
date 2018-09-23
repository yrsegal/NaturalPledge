package com.wiresegal.naturalpledge.common.lib

/**
 * @author WireSegal
 * Created at 10:16 PM on 1/1/17.
 */

fun String.capitalizeFirst(): String {
    if (this.isEmpty()) return this
    return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
}

fun String.lowercaseFirst(): String {
    if (this.isEmpty()) return this
    return this.slice(0..0).toLowerCase() + this.slice(1..this.length - 1)
}

