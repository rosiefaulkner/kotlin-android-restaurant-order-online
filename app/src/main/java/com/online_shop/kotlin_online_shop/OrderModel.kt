package com.online_shop.kotlin_online_shop

import androidx.appcompat.app.AppCompatActivity

class OrderModel
/**
 * Constructor for the order history model
 *
 * @param id the id of the order
 * @param size the size of the order
 * @param flavor the flavor of the order
 * @param fudge the fudge of the order
 * @param createdAt the date and time of the order
 */(
    val id: Int,
    /**
     * Getters for the order history model
     */
    @JvmField val size: String,
    @JvmField var flavor: String,
    @JvmField val fudge: String,
    val createdAt: String,
    @JvmField val price: String
) :
    AppCompatActivity()