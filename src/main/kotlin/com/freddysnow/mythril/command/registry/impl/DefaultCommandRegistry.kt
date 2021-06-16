package com.freddysnow.mythril.command.registry.impl

import com.freddysnow.mythril.command.Command
import com.freddysnow.mythril.command.registry.CommandRegistry

class DefaultCommandRegistry: CommandRegistry {

    private val commands = mutableSetOf<Command>()

    override fun register(command: Command) {
        commands.add(command)
    }

    override fun get(label: String): Command? {
        return commands.firstOrNull { it.info.labels.contains(label) }
    }

}