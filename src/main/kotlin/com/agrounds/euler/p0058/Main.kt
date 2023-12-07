package com.agrounds.euler.p0058

import com.agrounds.euler.isPrime
import com.agrounds.euler.timed

fun main() = timed {
    // start with the second square, with 3 primes on diagonals
    var squareNum = 2L
    var primeCount = 3

    // for square #s, there are 4 * s - 3 numbers on both diagonals
    while (primeCount.toDouble() / (4 * squareNum - 3) > 0.1) {
        if (squareNum % 100 == 0L) {
            println("Square number $squareNum has $primeCount primes on diagonals, with prime fraction " +
                "${primeCount.toDouble() / (4 * squareNum - 3)}")
        }

        // bottom right corner of previous square
        val prevCorner = (2 * squareNum - 1).let { it * it }
        // difference between each corner of this square
        val diff = 2 * (squareNum - 1)
        // new corners that are NOT squares are these:
        (1..3).map { prevCorner + it * diff }
            .filter(::isPrime)
            .also { primeCount += it.size }

        squareNum++
    }

    println("First square with diagonal prime ratio < 0.1 is number ${squareNum}, with side length ${2 * squareNum - 1}")
}
