package me.beatzoid.jdabot.command.commands.admin;

import me.beatzoid.jdabot.VeryBadDesign;
import me.beatzoid.jdabot.command.CommandContext;
import me.beatzoid.jdabot.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class PrefixCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You must have the `Manage Server` permission to use this command").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Missing args. Command syntax: `?setprefix <prefix>`").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        VeryBadDesign.PREFIXES.put(ctx.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("Successfully changed the prefix to `%s`", newPrefix).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for the current guild\n" +
                "Usage: `?setprefix <prefix>`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("prefix");
    }
}
