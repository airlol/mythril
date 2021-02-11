package com.niteah.mythril.command

interface CommandCompletionResolver {

    fun call(): List<String>

}