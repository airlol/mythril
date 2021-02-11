package com.niteah.mythril.command.executor

import com.niteah.mythril.command.Command
import org.bukkit.command.CommandSender

interface CommandExecutor {

    fun execute(sender: CommandSender, label: String, command: Command, arguments: List<String>): Boolean
    fun tabComplete(sender: CommandSender, command: Command, arguments: List<String>): List<String>

}