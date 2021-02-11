package com.niteah.mythril.command.registry

import com.niteah.mythril.command.Command

interface CommandRegistry {

    fun register(command: Command)
    fun get(label: String): Command?

}