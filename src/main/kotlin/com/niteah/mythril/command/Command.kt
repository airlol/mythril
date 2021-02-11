package com.niteah.mythril.command

import com.niteah.mythril.command.annotation.*
import com.niteah.mythril.command.annotation.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.reflect.Method
import java.util.*

class Command (val instance: MythrilCommand) {

    val lastExecuted = mutableMapOf<UUID, Long>()

    fun getCooldown(): Long {
        if (!instance::class.java.isAnnotationPresent(Cooldown::class.java)) return 0
        return instance::class.java.getAnnotation(Cooldown::class.java).length
    }

    fun canExecute(uuid: UUID): Boolean {
        if (!lastExecuted.containsKey(uuid)) return true
        if (lastExecuted[uuid]!! + getCooldown() > System.currentTimeMillis()) return false
        return true
    }

    val info = instance::class.java.getAnnotation(Command::class.java)

    /* INFO */
    fun isPlayerOnly(): Boolean {
        return instance::class.java.isAnnotationPresent(PlayerOnly::class.java)
    }
    fun isConsoleOnly(): Boolean {
        return instance::class.java.isAnnotationPresent(ConsoleOnly::class.java)
    }

    fun getRealArgumentCount(method: Method): Int {
        var count = 0
        for (param in method.parameters) if (!(param.type.name.contains("CommandSender") || param.type.name.contains("CommandContext"))) count++

        return count
    }
    fun getDefaultCommands(): List<Method> {
        return instance::class.java.declaredMethods.filter { it.isAnnotationPresent(Default::class.java) }
    }
    fun getSubCommands(): List<Method> {
        return instance::class.java.declaredMethods.filter { it.isAnnotationPresent(Sub::class.java) }
    }
    fun getSubCommandsLabels(): List<String> {
        val subCommands = mutableListOf<String>()
        instance::class.java.declaredMethods.filter { it.isAnnotationPresent(Sub::class.java) }
            .forEach { subCommands.addAll(it.getAnnotation(Sub::class.java).labels) }

        return subCommands
    }
    fun getSubCommands(label: String): List<Method> {
        return instance::class.java.declaredMethods.filter { it.isAnnotationPresent(Sub::class.java) }.filter { it.getAnnotation(Sub::class.java).labels.contains(label) }
    }
    fun getRequiredPermission(): String {
        return instance::class.java.getAnnotation(Command::class.java).permission
    }
    fun getRequiredPermission(sub: Method): String {
        return sub.getAnnotation(Sub::class.java).permission
    }

}
