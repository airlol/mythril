package com.freddysnow.mythril.command.registry

import com.freddysnow.mythril.command.Command

interface CommandRegistry {

    fun register(command: Command)
    fun get(label: String): Command?

}