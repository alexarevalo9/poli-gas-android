package com.example.poli_gas.database

data class PoliGas(
    val UUID : String? = null,
    val poligasUID: String? = null,
    val typeCylinder: Int? = null,
    val totalCylinder: String? = null,
    val date: String? = null,
    val hour: String? = null
){
    constructor() : this("", "",0, "", "", "")
}
