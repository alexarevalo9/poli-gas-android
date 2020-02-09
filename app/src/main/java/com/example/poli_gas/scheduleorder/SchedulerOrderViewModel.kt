package com.example.poli_gas.scheduleorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SchedulerOrderViewModel : ViewModel() {

    private val _navigateToOrderDetailScheFragment = MutableLiveData<String>()
    val navigateToOrderDetailScheFragment
        get() = _navigateToOrderDetailScheFragment

    fun onPoliGasClicked(id: String) {
        _navigateToOrderDetailScheFragment.value = id
    }

    fun onOrderDetailScheFragmentNavigated() {
        _navigateToOrderDetailScheFragment.value = null
    }
}