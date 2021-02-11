package com.niteah.mythril.command.provider.impl

import com.niteah.mythril.command.provider.CommandProvider
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class OfflinePlayerProvider: CommandProvider<OfflinePlayer?>("OFFLINEPLAYER") {

    override fun provide(string: String): OfflinePlayer? {
        Bukkit.getOfflinePlayers().forEach { if(it.name.equals(string, true)) return it }
        return null
    }

    override fun tabComplete(): List<String> {
        var names = mutableListOf<String>()
        Bukkit.getOfflinePlayers().filter { it.name != null }.forEach { names.add(it.name!!) }

        return names
    }

}