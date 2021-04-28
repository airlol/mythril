package com.niteah.mythril

import com.niteah.mythril.command.Command
import com.niteah.mythril.command.MythrilCommand
import com.niteah.mythril.command.bukkit.MythrilBukkitCommand
import com.niteah.mythril.command.bukkit.MythrilListener
import com.niteah.mythril.command.executor.CommandExecutor
import com.niteah.mythril.command.executor.impl.DefaultCommandExecutor
import com.niteah.mythril.command.provider.CommandProvider
import com.niteah.mythril.command.provider.impl.*
import com.niteah.mythril.command.registry.CommandRegistry
import com.niteah.mythril.command.registry.impl.DefaultCommandRegistry
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import com.niteah.mythril.command.CommandCompletionResolver as CommandCompletionResolver

class Mythril (val plugin: JavaPlugin) {

    private val registry: CommandRegistry = DefaultCommandRegistry()
    val executor: CommandExecutor = DefaultCommandExecutor(this)

    private val providers = mutableMapOf<String, CommandProvider<*>>()
    private val customCompletions = mutableMapOf<String, CommandCompletionResolver>()

    init {
        Bukkit.getPluginManager().registerEvents(MythrilListener(this), plugin)

        register("nothing", object : CommandCompletionResolver {
            override fun call(): List<String> {
                return emptyList()
            }
        })

        register(Boolean::class.java, BooleanProvider())
        register(Int::class.java, IntProvider())
        register(Long::class.java, LongProvider())
        register(OfflinePlayer::class.java, OfflinePlayerProvider())
        register(Player::class.java, PlayerProvider())
        register(String::class.java, StringProvider())
    }

    fun getProvider(label: String): CommandProvider<*>? {
        return providers[label]
    }

    fun getProvider(clazz: Any): CommandProvider<*>? {
        return getProvider(clazz::class.java.name)
    }

    fun getCustomCompletion(string: String): CommandCompletionResolver? {
        return customCompletions[string.toUpperCase()]
    }

    fun getCommand(label: String): Command? {
        return registry.get(label)
    }

    fun register(clazz: MythrilCommand) {
        register(plugin.name, clazz)
    }

    fun register(string: String, clazz: MythrilCommand) {
        var command = Command(clazz)

        var valid = true
        for (label in command.info.labels) if (plugin.server.commandMap.getCommand(label) != null) {
            valid = false
            continue
        }

        if (!valid) return

        registry.register(command)

        command.info.labels.forEach { plugin.server.commandMap.register(string, MythrilBukkitCommand(this, it)) }
    }

    fun register(type: Class<*>, clazz: CommandProvider<*>) {
        providers[clazz.id] = clazz
        providers[type.name] = clazz

        register(clazz.id, object : CommandCompletionResolver {
            override fun call(): List<String> {
                return clazz.tabComplete()
            }
        })

    }

    fun register(string: String, callable: CommandCompletionResolver) {
        customCompletions[string.toUpperCase()] = callable
    }


}