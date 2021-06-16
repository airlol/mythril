package com.freddysnow.mythril.command.bukkit

import com.freddysnow.mythril.Mythril
import com.freddysnow.mythril.command.Command
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.TabCompleteEvent

class MythrilListener(val mythril: Mythril): Listener {

    @EventHandler
    fun onTabComplete(event: TabCompleteEvent) {
        var str = if (event.buffer.startsWith("/")) event.buffer.substring(1) else event.buffer
        var split = str.split(" ").toMutableList()

        var label = split.first()
        split.removeAt(0)

        var command: Command = mythril.getCommand(label) ?: return
        event.completions = mythril.executor.tabComplete(event.sender, command, split)
    }

}