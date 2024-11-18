package com.online_shop.kotlin_online_shop

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler
/**
 * Constructor
 *
 */
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    /**
     * Create orders table
     */
    override fun onCreate(db: SQLiteDatabase) {
        // Set column names and schema in new table
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SIZE_COL + " TEXT,"
                + FLAVOR_COL + " TEXT,"
                + FUDGE_COL + " TEXT,"
                + CREATED_AT_COL + " int,"
                + PRICE_COL + " TEXT)")
        db.execSQL(query)
    }

    /**
     * Add new order to DB
     */
    fun addNewOrder(
        orderSize: String?,
        orderFlavor: String?,
        orderFudge: String?,
        orderPrice: String?
    ) {
        // Call DB handler writable method

        val db = this.writableDatabase

        val values = ContentValues()
        values.put(SIZE_COL, orderSize)
        values.put(FLAVOR_COL, orderFlavor)
        values.put(FUDGE_COL, orderFudge)
        values.put(CREATED_AT_COL, System.currentTimeMillis())
        values.put(PRICE_COL, orderPrice)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    /**
     * Read orders from DB
     */
    fun readOrders(): ArrayList<OrderModel> {
        // Get readable instance of DB
        val db = this.readableDatabase

        val cursorOrders = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        val orderModelArrayList = ArrayList<OrderModel>()

        if (cursorOrders.moveToFirst()) {
            do {
                // Add cursor data to list
                orderModelArrayList.add(
                    OrderModel(
                        cursorOrders.getInt(0),
                        cursorOrders.getString(1),
                        cursorOrders.getString(2),
                        cursorOrders.getString(3),
                        cursorOrders.getString(4),
                        cursorOrders.getString(5)
                    )
                )
            } while (cursorOrders.moveToNext())
            // Add next order to list
        }
        cursorOrders.close()
        return orderModelArrayList
    }

    /**
     * Upgrade DB
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop table if it exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "franksSundaeKotlinDB"

        private const val DB_VERSION = 1

        const val TABLE_NAME: String = "orders"

        private const val ID_COL = "id"

        private const val SIZE_COL = "size"

        private const val FLAVOR_COL = "flavor"

        private const val FUDGE_COL = "fudge"

        private const val CREATED_AT_COL = "created_at"

        private const val PRICE_COL = "price"
    }
}