package com.niteah.mithryl

import com.niteah.mithryl.command.registry.CommandRegistry
import com.niteah.mithryl.command.registry.impl.DefaultCommandRegistry
import org.bukkit.plugin.java.JavaPlugin

class Mithryl (val plugin: JavaPlugin) {

    val registry: CommandRegistry = DefaultCommandRegistry()

    fun register() {
        TODO("Register command logic.")
    }

}