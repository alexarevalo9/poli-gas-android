package com.example.poli_gas.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "poli_gas_table")
data class PoliGas(

    @PrimaryKey(autoGenerate = true)
    var purchaseId: Long = 0L,

    @ColumnInfo(name = "phone")
    var phone: String = "",

    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,

    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,

    @ColumnInfo(name = "cylinder_type")
    var cylinderType: Int = -1,

    @ColumnInfo(name = "cylinders_number")
    var cylindersNumber: Int = 0,

    @ColumnInfo(name = "shipping_type")
    var shippingType: String = "",

    @ColumnInfo(name = "delivery_date")
    var deliveryDate: String = "",

    @ColumnInfo(name = "delivery_hour")
    var deliveryHour: String = ""

)
