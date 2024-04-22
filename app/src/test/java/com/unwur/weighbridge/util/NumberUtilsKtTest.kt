package com.unwur.weighbridge.util


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NumberUtilsKtTest {

    @Test
    fun formatDoubleAsNeeded() {

        val num1 = 10.0
        val expect1 = "10"

        val num2 = 10.25
        val expect2 = "10.25"

        val formattedNum1 = num1.formatDoubleAsNeeded()
        val formattedNum2 = num2.formatDoubleAsNeeded()

        assertThat(formattedNum1).isEqualTo(expect1)
        assertThat(formattedNum2).isEqualTo(expect2)
    }
}