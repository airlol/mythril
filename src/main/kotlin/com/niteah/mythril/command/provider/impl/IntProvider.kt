package com.niteah.mythril.command.provider.impl

import com.niteah.mythril.command.provider.CommandProvider
import java.lang.NumberFormatException

class IntProvider: CommandProvider<Int?>("INT") {

    override fun provide(string: String): Int? {
        return try {
            string.toInt()
        } catch (ex: NumberFormatException) {
            null
        }
    }

    override fun tabComplete(): List<String> {
        return mutableListOf()
    }

}