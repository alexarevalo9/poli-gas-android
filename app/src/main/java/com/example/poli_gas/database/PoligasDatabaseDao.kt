package com.example.poli_gas.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PoligasDatabaseDao  {

    /**
     * Insert a new row with a value
     *
     * @param purchase new value to write
     */
    @Insert
    fun insert(purchase: PoliGas)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param purchase new value to write
     */
    @Update
    fun update(purchase: PoliGas)

    /**
     * Selects and returns the row that matches the purchase id, which is our key.
     *
     * @param key purchase id to match
     */
    @Query("SELECT * from poli_gas_table WHERE purchaseId = :key")
    fun get(key: Long): PoliGas?


    /**
     * Selects and returns all rows in the table,
     *
     * sorted by purchase id in descending order.
     */
    @Query("SELECT * FROM poli_gas_table ORDER BY purchaseId DESC")
    fun getAllPurchases(): LiveData<List<PoliGas>>

    /**
     * Selects and returns the latest purchase.
     */
    @Query("SELECT * FROM poli_gas_table ORDER BY purchaseId DESC LIMIT 1")
    fun getPurchase(): PoliGas?

}
