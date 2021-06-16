package com.freddysnow.mythril.command

interface CommandCompletionResolver {

    fun call(arguments: List<String>): List<String>

}