package com.niteah.mythril.command.provider.impl

import com.niteah.mythril.command.provider.CommandProvider

class StringProvider: CommandProvider<String>("STRING") {

    override fun provide(string: String): String {
        return string
    }

    override fun tabComplete(): List<String> {
        return mutableListOf()
    }

}