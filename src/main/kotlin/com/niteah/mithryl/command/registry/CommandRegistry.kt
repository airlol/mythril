package com.niteah.mithryl.command.registry

import com.niteah.mithryl.command.Command

interface CommandRegistry {

    fun register(command: Command)
    fun get(label: String): Command?

}