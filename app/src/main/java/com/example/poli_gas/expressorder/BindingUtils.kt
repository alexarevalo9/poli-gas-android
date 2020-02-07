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

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.poli_gas.R
import com.example.poli_gas.database.PoliGas

@BindingAdapter("poligasImage")
fun ImageView.setGasImage(item: PoliGas?) {
    item?.let {
        setImageResource(when (item.typeCylinder) {
            1 -> R.drawable.gasindustrial
            2 -> R.drawable.gasazul
            3 -> R.drawable.gasamarillo
            else -> R.drawable.gasindustrial
        })
    }
}

@BindingAdapter("poligasString")
fun TextView.setGasTextString(item: PoliGas?) {
    item?.let {
        text = item.date +" - "+ item.hour
    }
}
