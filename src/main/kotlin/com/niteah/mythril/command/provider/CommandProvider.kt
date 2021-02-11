package com.niteah.mythril.command.provider

abstract class CommandProvider<T>(val id: String) {

    abstract fun provide(string: String): T
    open fun tabComplete(): List<String> { return emptyList() }

}