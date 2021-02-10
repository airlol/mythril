package com.niteah.mithryl.command.provider

interface CommandProvider<T> {

    fun provide(string: String): T

}