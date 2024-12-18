package com.tutor.jettipapp.util

fun calculateTip(totalBill: Double, totalPercentage: Int): Double {
    return if(totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill *  totalPercentage) / 100 else 0.0
}

fun calculatePerPeople(totalBill: Double, splitBy: Int, totalPercentage: Int) : Double {
    val bill = calculateTip(totalBill, totalPercentage) + totalBill

    return (bill / splitBy)
}