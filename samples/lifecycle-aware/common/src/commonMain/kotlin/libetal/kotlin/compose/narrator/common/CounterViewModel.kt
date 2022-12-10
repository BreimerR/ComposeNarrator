package libetal.kotlin.compose.narrator.common

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.log.info


class CounterViewModel : ViewModel(10000) {

    private val jobs = mutableListOf<Job>()
    val headerCounters = mutableStateListOf<Long>()

    val hI = mutableStateOf(-1)
    val fLi = mutableStateOf(-1)
    val fRi = mutableStateOf(-1)

    override fun onResume() {
        addHeaderCounter()
    }


    fun addHeaderCounter() {
        hI.value += 1
        val i = hI.value
        jobs += ioLaunch {
            delay(300)
            incrementIndex(i)
        }
    }

    fun removePrevHeaderCounter() {
        val i = hI.value
        if (i == -1) return
        hI.value -= 1
        jobs[i].cancel("Removing the counter for $i")
        jobs.removeAt(i)
        headerCounters.removeAt(i)
    }

    private fun incrementIndex(i: Int) = launch {
        val current = headerCounters.getOrNull(i) ?: -1
        if (current == -1L) headerCounters.add(i, -1)
        headerCounters[i] = current + 1
        TAG info "Adding $i = ${headerCounters[i]}"
    }

    companion object {
        const val TAG = "CounterViewModel"
    }

}