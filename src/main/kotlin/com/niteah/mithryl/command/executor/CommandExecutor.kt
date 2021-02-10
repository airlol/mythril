package com.niteah.mithryl.command.executor

import org.bukkit.command.CommandSender

interface CommandExecutor {

    fun execute(sender: CommandSender)

}