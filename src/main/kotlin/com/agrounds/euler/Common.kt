package com.agrounds.euler

import org.apache.commons.lang3.time.StopWatch

fun timed(block: () -> Unit) {
    val watch = StopWatch()
    watch.start()

    block()

    watch.stop()
    println("Took ${watch.time}ms")
}
