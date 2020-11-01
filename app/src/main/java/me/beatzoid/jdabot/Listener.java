package me.beatzoid.jdabot;

import me.beatzoid.jdabot.command.commands.music.JoinCommand;
import me.beatzoid.jdabot.database.DatabaseManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable isAloneVC = () -> {
            event.getJDA().getGuilds().forEach(guild -> {
                AudioManager audioManager = guild.getAudioManager();
                if (audioManager.getConnectedChannel() != null) {
                    if (audioManager.getConnectedChannel().getMembers().size() == 1) {
                        audioManager.closeAudioConnection();
                        JoinCommand.channel.sendMessage("I have automatically left the voice channel because no one was in it").queue();
                    }
                }
            });
        };

        executor.scheduleWithFixedDelay(isAloneVC, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if(user.isBot() || event.isWebhookMessage()) return;


        final long guildId = event.getGuild().getIdLong();
        String prefix = VeryBadDesign.PREFIXES.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(Config.get("owner_id"))) {
            LOGGER.info("Shutting down bot...");
            event.getChannel().sendMessage("Goodbye").queue();
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }
}
