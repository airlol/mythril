package com.niteah.mythril.command.bukkit

import com.niteah.mythril.Mythril
import com.niteah.mythril.command.annotation.Cooldown
import com.niteah.mythril.command.annotation.Sub
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class MythrilBukkitCommand (val mythril: Mythril, label: String): Command(label) {

    override fun execute(sender: CommandSender, label: String, arguments: Array<String>): Boolean {
        val command = mythril.getCommand(label)?: return false

        if (sender is Player && !command.canExecute(sender.uniqueId)) {
            sender.sendMessage(ChatColor.RED.toString() + "Slow down before doing that!")
            return true
        }

        if (sender is Player) command.lastExecuted[sender.uniqueId] = System.currentTimeMillis()

        var success = mythril.executor.execute(sender, label, command, arguments.toList())

        if (!success) {
            if (arguments.isNotEmpty() && command.getSubCommands(arguments[0]).isNotEmpty()) {
                sender.sendMessage(ChatColor.RED.toString() + "Invalid usage: /" + command.getSubCommands(arguments[0])[0].getAnnotation(Sub::class.java).usage.replace("$", label))
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "Invalid usage: /" + command.info.usage.replace("$", label))
            }
        }

        return true
    }

}