package com.niteah.mythril

import org.bukkit.ChatColor

enum class Locale (val message: String) {

    NO_PERMISSION(ChatColor.RED.toString() + "You don't have permission to execute this command."),
    CONSOLE_ONLY(ChatColor.RED.toString() + "This action can only be performed by the console."),
    PLAYER_ONLY(ChatColor.RED.toString() + "This action can only be performed by a player.")

}