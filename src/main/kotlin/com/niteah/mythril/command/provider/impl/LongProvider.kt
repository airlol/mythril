package com.niteah.mythril.command.provider.impl

import com.niteah.mythril.command.provider.CommandProvider
import java.lang.NumberFormatException

class LongProvider: CommandProvider<Long?>("LONG") {

    override fun provide(string: String): Long? {
        return try {
            string.toLong()
        } catch (ex: NumberFormatException) {
            null
        }
    }

    override fun tabComplete(): List<String> {
        return mutableListOf()
    }

}