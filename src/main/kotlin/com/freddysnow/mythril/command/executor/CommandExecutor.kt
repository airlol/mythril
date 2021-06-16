package com.freddysnow.mythril.command.executor

import com.freddysnow.mythril.command.Command
import org.bukkit.command.CommandSender

interface CommandExecutor {

    fun execute(sender: CommandSender, label: String, command: Command, arguments: List<String>): Boolean
    fun tabComplete(sender: CommandSender, command: Command, arguments: List<String>): List<String>

}