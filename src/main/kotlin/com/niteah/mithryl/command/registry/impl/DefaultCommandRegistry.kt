package com.niteah.mithryl.command.registry.impl

import com.niteah.mithryl.command.Command
import com.niteah.mithryl.command.registry.CommandRegistry

class DefaultCommandRegistry: CommandRegistry {

    private val commands = mutableSetOf<Command>()

    override fun register(command: Command) {
        commands.add(command)
    }

    override fun get(label: String): Command? {
        return commands.firstOrNull { it.info.labels.contains(label) }
    }

}