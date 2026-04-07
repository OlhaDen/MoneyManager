package com.example.myapplication.ui.components

import androidx.annotation.DrawableRes
import com.example.myapplication.R

@DrawableRes
fun categoryImageRes(category: String): Int {
    return when (category.lowercase()) {
        "food" -> R.drawable.cat_food
        "transport" -> R.drawable.cat_transport
        "shopping" -> R.drawable.cat_shopping
        "bills" -> R.drawable.cat_bills
        "entertainment" -> R.drawable.cat_entertainment
        "health" -> R.drawable.cat_health
        "salary" -> R.drawable.cat_salary
        "freelance" -> R.drawable.cat_freelance
        "investment" -> R.drawable.cat_investment
        "gift" -> R.drawable.cat_gift
        "refund" -> R.drawable.cat_refund
        "other" -> R.drawable.cat_other
        else -> R.drawable.cat_other
    }
}