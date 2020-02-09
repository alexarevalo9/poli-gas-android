/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.poli_gas.expressorder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.poli_gas.database.PoligasDatabaseDao


class ExpressOrderViewModel() : ViewModel() {

    private val _navigateToOrderDetailFragment = MutableLiveData<String>()
    val navigateToOrderDetailFragment
        get() = _navigateToOrderDetailFragment

    fun onPoliGasClicked(id: String) {
        _navigateToOrderDetailFragment.value = id
    }

    fun onOrderDetaielFragmentNavigated() {
        _navigateToOrderDetailFragment.value = null
    }
}
