package com.si.sipanicalarm.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel<N> : ViewModel() {

    private var navigator: N? = null


    fun getNavigator() = navigator!!

    fun setNavigator(navigator: N) {
        this.navigator = navigator
    }

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    val viewModelJob = Job()

    /**
     * This is the scope for all coroutines.
     *
     * Since we pass [viewModelJob], you can cancel all coroutines launched by [viewModelScope] by calling
     * viewModelJob.cancel().  This is called in [onCleared].
     */
    val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)


}