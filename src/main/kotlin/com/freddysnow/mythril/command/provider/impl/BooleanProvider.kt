package com.freddysnow.mythril.command.provider.impl

import com.freddysnow.mythril.command.provider.CommandProvider
import java.lang.NumberFormatException

class BooleanProvider: CommandProvider<Boolean?>("BOOLEAN") {

    override fun provide(string: String): Boolean? {
        return when (string.toUpperCase()) {
            "TRUE" -> true
            "FALSE" -> false
            else -> null
        }
    }

    override fun tabComplete(): List<String> {
        return mutableListOf("True", "False")
    }

}