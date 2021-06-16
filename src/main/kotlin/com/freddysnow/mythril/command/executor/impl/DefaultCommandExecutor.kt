package com.freddysnow.mythril.command.executor.impl

import com.freddysnow.mythril.Locale
import com.freddysnow.mythril.Mythril
import com.freddysnow.mythril.command.Command
import com.freddysnow.mythril.command.CommandContext
import com.freddysnow.mythril.command.annotation.*
import com.freddysnow.mythril.command.executor.CommandExecutor
import com.freddysnow.mythril.command.provider.CommandProvider
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class DefaultCommandExecutor (val mythril: Mythril): CommandExecutor {

    override fun execute(sender: CommandSender, label: String, command: Command, arguments: List<String>): Boolean {

        if (command.getRequiredPermission() != "" && !sender.hasPermission(command.getRequiredPermission())) {
            sender.sendMessage(_root_ide_package_.com.freddysnow.mythril.Locale.NO_PERMISSION.message)
            return true
        }

        if (arguments.isNotEmpty() && command.getSubCommands(arguments.first()).isNotEmpty()) {
            var sub = arguments.first()
            var args = arguments.toMutableList()
            args.removeAt(0)

            return execute(sender, command, label, sub, args)
        }

        var primary: Method? = null

        for (method in command.getDefaultCommands()) {
            if (primary == null) {
                primary = method
                continue
            }

            if (command.getRealArgumentCount(method) == arguments.size) primary = method
        }

        if (primary == null) return false

        if (sender !is Player && (primary.isAnnotationPresent(PlayerOnly::class.java) || command.isPlayerOnly())) {
            sender.sendMessage(_root_ide_package_.com.freddysnow.mythril.Locale.PLAYER_ONLY.message)
            return true
        }

        if (sender is Player && (primary.isAnnotationPresent(ConsoleOnly::class.java) || command.isConsoleOnly())) {
            sender.sendMessage(_root_ide_package_.com.freddysnow.mythril.Locale.CONSOLE_ONLY.message)
            return true
        }

        var argumentsCopy = arguments.toMutableList()
        var suppressed = mutableListOf<String>()
        argumentsCopy.filter { it.startsWith("-") }.forEach { suppressed.add(it) }
        argumentsCopy.removeAll { it.startsWith("-") }

        var context = CommandContext(sender, suppressed, label)

        var parameters = primary.parameters
        var supplied = mutableListOf<Any?>()

        for (param in parameters) {

            when (param.type.name) {

                "org.bukkit.command.CommandSender" -> {
                    supplied.add(sender)
                }

                "com.niteah.mythril.command.CommandContext" -> {
                    supplied.add(context)
                }

                else -> {
                    val provider = mythril.getProvider(param.type.name) ?: throw RuntimeException("Class '" + param.type + "' does not have a valid CommandProvider registered!")
                    var index = parameters.indexOf(param) - (parameters.size - command.getRealArgumentCount(primary))

                    if (index > argumentsCopy.lastIndex) return false
                    supplied.add(provider.provide(argumentsCopy[index]))
                }
            }

        }

        if (argumentsCopy.size >= command.getRealArgumentCount(primary) && supplied.isNotEmpty() && supplied.last() is String && !parameters.last().isAnnotationPresent(Singular::class.java)) {
            val builder = StringBuilder(supplied.last() as String)

            for (index in supplied.lastIndex until argumentsCopy.lastIndex) {
                builder.append(" ").append(argumentsCopy[index])
            }

            supplied[supplied.lastIndex] = builder.toString()
        }

        try {
            if (command.info.async || command.instance::class.java.isAnnotationPresent(Async::class.java)) {
                Bukkit.getScheduler().runTaskAsynchronously(mythril.plugin, Runnable {
                    primary.invoke(command.instance, *supplied.toTypedArray())
                })
            } else {
                primary.invoke(command.instance, *supplied.toTypedArray())
            }
        } catch (ex: IllegalArgumentException) {
            return false
        } catch (ex: InvocationTargetException) {
            return false
        }

        return true
    }

    fun execute(sender: CommandSender, command: Command, label: String, sub: String, arguments: List<String>): Boolean {
        var primary: Method? = null

        for (method in command.getSubCommands(sub)) {
            if (primary == null) {
                primary = method
                continue
            }

            if (command.getRealArgumentCount(method) == arguments.size) primary = method
        }

        if (primary == null) return false

        if (command.getRequiredPermission(primary) != "" && !sender.hasPermission(command.getRequiredPermission(primary))) {
            sender.sendMessage(_root_ide_package_.com.freddysnow.mythril.Locale.NO_PERMISSION.message)
            return true
        }

        if (sender !is Player && (primary.isAnnotationPresent(PlayerOnly::class.java) || command.isPlayerOnly())) {
            sender.sendMessage(_root_ide_package_.com.freddysnow.mythril.Locale.PLAYER_ONLY.message)
            return true
        }

        if (sender is Player && primary.isAnnotationPresent(ConsoleOnly::class.java) || command.isConsoleOnly()) {
            sender.sendMessage(_root_ide_package_.com.freddysnow.mythril.Locale.CONSOLE_ONLY.message)
            return true
        }

        var argumentsCopy = arguments.toMutableList()
        var suppressed = mutableListOf<String>()
        argumentsCopy.filter { it.startsWith("-") }.forEach { suppressed.add(it) }
        argumentsCopy.removeAll { it.startsWith("-") }

        var context = CommandContext(sender, suppressed, label)

        var parameters = primary.parameters
        var supplied = mutableListOf<Any?>()

        for (param in parameters) {

            when (param.type.name) {

                "org.bukkit.command.CommandSender" -> {
                    supplied.add(sender)
                }

                "com.niteah.mythril.command.CommandContext" -> {
                    supplied.add(context)
                }

                else -> {
                    val provider = mythril.getProvider(param.type.name) ?: throw RuntimeException("Class '" + param.type + "' does not have a valid CommandProvider registered!")
                    var index = parameters.indexOf(param) - (parameters.size - command.getRealArgumentCount(primary))

                    if (index > argumentsCopy.lastIndex) return false
                    supplied.add(provider.provide(argumentsCopy[index]))
                }
            }

        }

        if (argumentsCopy.size >= command.getRealArgumentCount(primary) && supplied.isNotEmpty() && supplied.last() is String && !parameters.last().isAnnotationPresent(Singular::class.java)) {
            val builder = StringBuilder(supplied.last() as String)

            for (index in supplied.lastIndex until argumentsCopy.lastIndex + 1) {
                builder.append(" ").append(argumentsCopy[index])
            }

            supplied[supplied.lastIndex] = builder.toString()
        }

        try {
            if (command.info.async || primary.getAnnotation(Sub::class.java).async || command.instance::class.java.isAnnotationPresent(Async::class.java) || primary.isAnnotationPresent(Async::class.java)) {
                Bukkit.getScheduler().runTaskAsynchronously(mythril.plugin, Runnable {
                    primary.invoke(command.instance, *supplied.toTypedArray())
                })
            } else {
                primary.invoke(command.instance, *supplied.toTypedArray())
            }
        } catch (ex: IllegalArgumentException) {
            return false
        } catch (ex: InvocationTargetException) {
            return false
        }

        return true
    }

    override fun tabComplete(sender: CommandSender, command: Command, arguments: List<String>): List<String> {
        if (command.getRequiredPermission() != "" && !sender.hasPermission(command.getRequiredPermission())) {
            return emptyList()
        }

        var argumentsCopy = arguments.toMutableList()
        argumentsCopy.removeAll { it.startsWith("-") }

        if (argumentsCopy.isNotEmpty() && command.getSubCommands(arguments.first()).isNotEmpty()) {
            var sub = arguments.first()
            var args = arguments.toMutableList()
            args.removeAt(0)

            return tabComplete(sender, command, sub, args)
        }

        var primary: Method? = null

        for (method in command.getDefaultCommands()) {
            if (primary == null) {
                primary = method
                continue
            }

            if (command.getRealArgumentCount(method) == argumentsCopy.size) primary = method
        }

        if (primary == null) return emptyList()

        var non = 0
        for (type in 0 until primary.parameters.lastIndex) {
            when (primary.parameters[type].type.name) {
                "org.bukkit.command.CommandSender" -> non++
                "com.niteah.mythril.command.CommandContext" -> non++
            }
        }

        var index = non + argumentsCopy.lastIndex
        if (index > primary.parameters.lastIndex) return emptyList()

        if (argumentsCopy.lastIndex - non == 0) return command.getSubCommandsLabels().filter { it.startsWith(argumentsCopy.last(), true) }
        var type = primary.parameters[index]

        if (type.isAnnotationPresent(Completer::class.java)) {
            var value = type.getAnnotation(Completer::class.java).completion

            return if (value.startsWith("@")) {
                mythril.getCustomCompletion(value.substring(1))?.call(argumentsCopy)?: throw RuntimeException("Missing custom completion callable for '" + value.substring(1) + "'")
            } else {
                value.split("|")
            }

        }


        // TODO("Custom tab complete")
        var provider: CommandProvider<*> = mythril.getProvider(type.type.name) ?: return emptyList()

        var all = provider.tabComplete()
        var complete = mutableListOf<String>()
        for (a in all) if (a.startsWith(argumentsCopy.last(), true)) complete.add(a)

        return complete

    }

    fun tabComplete(sender: CommandSender, command: Command, sub: String, argumentsCopy: List<String>): List<String> {
        var primary: Method? = null

        for (method in command.getSubCommands(sub)) {
            if (primary == null) {
                primary = method
                continue
            }

            if (command.getRealArgumentCount(method) == argumentsCopy.size) primary = method
        }

        if (primary == null) return emptyList()

        if (command.getRequiredPermission(primary) != "" && !sender.hasPermission(command.getRequiredPermission(primary))) {
            return emptyList()
        }

        var non = 0
        for (type in 0 until primary.parameters.lastIndex) {
            when (primary.parameters[type].type.name) {
                "org.bukkit.command.CommandSender" -> non++
                "com.niteah.mythril.command.CommandContext" -> non++
            }
        }

        var index = non + argumentsCopy.lastIndex
        if (index > primary.parameters.lastIndex) return emptyList()

        if (argumentsCopy.lastIndex - non == 0) return command.getSubCommandsLabels().filter { it.startsWith(argumentsCopy.last(), true) }

        if (index == -1) return emptyList()
        var type = primary.parameters[index]

        if (type.isAnnotationPresent(Completer::class.java)) {
            var value = type.getAnnotation(Completer::class.java).completion

            return if (value.startsWith("@")) {
                mythril.getCustomCompletion(value.substring(1))?.call(argumentsCopy)?.filter { it.startsWith(argumentsCopy.last()) }?: throw RuntimeException("Missing custom completion callable for '" + value.substring(1) + "'")
            } else {
                value.split("|").filter { it.startsWith(argumentsCopy.last()) }
            }

        }

        // TODO("Custom tab complete")
        var provider: CommandProvider<*> = mythril.getProvider(type.type.name) ?: return emptyList()

        var all = provider.tabComplete()
        var complete = mutableListOf<String>()
        for (a in all) if (a.startsWith(argumentsCopy.last(), true)) complete.add(a)

        return complete

    }

}