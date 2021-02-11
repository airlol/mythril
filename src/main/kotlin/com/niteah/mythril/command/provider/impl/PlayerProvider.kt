package com.niteah.mythril.command.provider.impl

import com.niteah.mythril.command.provider.CommandProvider
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerProvider: CommandProvider<Player?>("PLAYER") {

    override fun provide(string: String): Player? {
        Bukkit.getOnlinePlayers().forEach { if(it.name.equals(string, true)) return it }
        return null
    }

    override fun tabComplete(): List<String> {
        var names = mutableListOf<String>()
        Bukkit.getOnlinePlayers().forEach { names.add(it.name) }

        return names
    }

}