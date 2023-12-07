package com.agrounds.euler

import kotlin.math.max

// My probably naive attempt to generate a set of all prime numbers up to some limit.
// It uses the method of the Sieve of Eratosthenes, but does not optimize for the CPU cache,
// so the runtime bottleneck is likely the memory access speed.  It also runs entirely in a single thread.
// Regardless, it does eventually find all primes up to a limit of around 1 billion (my best guess), depending on
// your JVM max heap size and your computer's hardware.
object Primes {

    private var i = 2L
    // sieve of eratosthenes limit
    private const val STEP = 100_000L
    private var limit = STEP
    // known composites in the range (i, limit]
    // these are precisely the multiples of all primes <= i in the above range
    private val composites = mutableSetOf<Long>()
    // all primes <= i
    private val primes = mutableSetOf<Long>(2)

    init {
        addComposites(i, i * i)
    }

    private fun addComposites(p: Long, from: Long) {
        composites.addAll(from..limit step p)
    }

    private fun nextI() {
        i++

        if (i > limit) {
            val oldLimit = limit
            if (Long.MAX_VALUE - limit >= STEP) {
                limit += STEP
            } else {
                limit = Long.MAX_VALUE
            }
            // do not include primes whose square is greater than Long.MAX_VALUE
            // as we won't have any composites to add for them anyway
            primes
                .filter { it < Int.MAX_VALUE }
                .forEach { p ->
                    // greatest multiple of p that is <= oldLimit
                    val multP = (oldLimit / p) * p
                    val start = if (Long.MAX_VALUE - multP < p) {
                        // there is no multiple of p that is greater than the new limit
                        // punt by passing multP instead
                        multP
                    } else {
                        max(
                            // least multiple of p that is greater than l
                            multP + p,
                            // but also, we need not consider numbers less than p^2
                            p * p
                        )
                    }
                    addComposites(p, start)
                }
        }
    }

    fun isPrime(n: Long): Boolean {
        if (n == 0L) return false
        if (n < 0) return isPrime(-n)
        findPrimes(n)
        return n in primes
    }

    private fun findPrimes(upTo: Long) {
        while (i < upTo) {
            nextI()
            while (composites.remove(i)) {
                nextI()
            }
            // i is the next prime
            // add all multiples of i up to limit to the set of composites
            if (i <= Int.MAX_VALUE) {
                addComposites(i, i * i)
            }
            primes.add(i)
        }
    }
}

fun isPrime(n: Int) = isPrime(n.toLong())
fun isPrime(n: Long) = Primes.isPrime(n)

fun main() = timed {
    (1..10_000_000)
        .filter(::isPrime)
        .also { println("There are ${it.size} primes less than 10 million") }
}
