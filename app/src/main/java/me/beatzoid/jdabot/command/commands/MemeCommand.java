package me.beatzoid.jdabot.command.commands;

import com.fasterxml.jackson.databind.JsonNode;
import me.beatzoid.jdabot.Listener;
import me.beatzoid.jdabot.command.CommandContext;
import me.beatzoid.jdabot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemeCommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json) -> {
            if (!json.get("success").asBoolean()) {
                channel.sendMessage("Something went wrong, please try again later!").queue();
                LOGGER.error("Meme command didn't return a success: true value, here is the JSON object:\n{}", json);
                return;
            }

            final JsonNode data = json.get("data");
            final String title = data.get("title").asText();
            final String url = data.get("url").asText();
            final String image = data.get("image").asText();

            final EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, url, image);

            channel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getHelp() {
        return "Shows a random meme";
    }
}
