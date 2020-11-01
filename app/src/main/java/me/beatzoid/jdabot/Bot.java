package me.beatzoid.jdabot;

import me.beatzoid.jdabot.database.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import okhttp3.Cache;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Bot {

    private Bot() throws LoginException {
        WebUtils.setUserAgent("Beatzoid JDA Bot/Beatzoid#1642");

        JDA jda = JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES
        )
                .disableCache(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOTE
                )
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("Beatzoid develop me!"))
                .build();

        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(0x2c2f33)
                        .setFooter("Bot created by Beatzoid#1642", jda.getSelfUser().getEffectiveAvatarUrl())
        );
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
