package com.niteah.mythril.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandContext (val sender: CommandSender, val suppressed: List<String>, val label: String) {

    fun getSenderAsPlayer(): Player {
        return sender as Player
    }

}