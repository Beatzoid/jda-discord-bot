package me.beatzoid.jdabot;

import me.beatzoid.jdabot.command.CommandContext;
import me.beatzoid.jdabot.command.ICommand;
import me.beatzoid.jdabot.command.commands.admin.SetPrefixCommand;
import me.beatzoid.jdabot.command.commands.fun.JokeCommand;
import me.beatzoid.jdabot.command.commands.fun.KickCommand;
import me.beatzoid.jdabot.command.commands.fun.MemeCommand;
import me.beatzoid.jdabot.command.commands.info.HelpCommand;
import me.beatzoid.jdabot.command.commands.info.PingCommand;
import me.beatzoid.jdabot.command.commands.misc.PasteCommand;
import me.beatzoid.jdabot.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager () {
        // Info
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));

        // Fun
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());

        // Admin
        addCommand(new SetPrefixCommand());
        addCommand(new KickCommand());

        // Music
        addCommand(new JoinCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new QueueCommand());

        // Misc
        addCommand(new PasteCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already in use.");
        }

        commands.add(cmd);
        LOGGER.info("Loaded command {}", cmd.getName());
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public  ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            // All this complicated looking line does is capitalize the first letter of the command name. Ex: "ping" to "Ping"
            LOGGER.info("{} command has been run by {}", cmd.getName().substring(0, 1).toUpperCase() + cmd.getName().substring(1), event.getAuthor().getAsTag());
            cmd.handle(ctx);
        }
    }

}
