# mythril
lightweight, simple, and reflection-based command framework for spigot.

## example command

```
@Command(labels = ["mythril"], usage = "$")
class MythrilCommand {

    @Default
    fun onDefault(context: CommandContext) {
        context.sendMessage("Mythril")
    }
    
    @Sub(labels = ["test"], usage = "$ test <player> <message..>")
    @Async
    fun onTest(context: CommandContext, player: Player, @Completions("Hello|Have a nice day|Okay") message: String)
        player.sendMessage(string)
    }    

}

var mythril = Mythril()
mythril.register(MythrilCommand())

```

## example provider

```
// id specified (here is "PLAYER"), can then be used as a valid string in a Completions annotation on a paramater in a command.
// Here, you can auto complete player names to strings by using @Completions("@player")

class PlayerProvider: CommandProvider<Player?>("PLAYER") {

    override fun provide(string: String): Player? {
        Bukkit.getOnlinePlayers().forEach { if(it.name.equals(string, true)) return it }
        return null
    }

    override fun tabComplete(): List<String> {
        var names = mutableListOf<String>()
        Bukkit.getOnlinePlayers().forEach { names.add(it.name) }

        return names
    }

}

var mythril = Mythril()
mythril.register(Player::class.java, PlayerProvider())

```

## example standalone auto-completer

```
var mythril = Mythril(this)
mythrilregister("day", object : CommandCompletionResolver {

    override fun call(): List<String> {
        return mutableListOf(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        )
    }
})
