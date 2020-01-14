package com.example.poli_gas.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*

class HomeViewModel : ViewModel(){

    // The current quantity
    private val _quantity_cilynders = MutableLiveData<Int>()

    val quantity_cilynders = Transformations.map(_quantity_cilynders) { quantity ->
        quantity.toString()
    }

    private var _showToastEvent = MutableLiveData<Int>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingToast()`.
     */
    val showToastEvent: LiveData<Int>
        get() = _showToastEvent

    init {
        _quantity_cilynders.value = 1
    }


    fun increaseQuantity(){

        if(_quantity_cilynders.value != 3){
            _quantity_cilynders.value = _quantity_cilynders.value?.plus(1)
        }else{
            _showToastEvent.value = _quantity_cilynders.value
        }
    }

    fun decreaseQuantity(){
        if (_quantity_cilynders.value != 1){
            _quantity_cilynders.value = _quantity_cilynders.value?.minus(1)
        }else{
            _showToastEvent.value = _quantity_cilynders.value
        }

    }

    fun doneShowingToast() {
        _showToastEvent.value = null
    }

}