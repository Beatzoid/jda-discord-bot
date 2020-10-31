package me.beatzoid.jdabot.command.commands;

import me.beatzoid.jdabot.command.CommandContext;
import me.beatzoid.jdabot.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessageFormat("Rest ping: %sms\nWS Ping: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getHelp() {
        return "Shows the current ping of the bot";
    }

    @Override
    public String getName() {
        return "ping";
    }
}
