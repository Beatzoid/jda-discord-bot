package me.beatzoid.jdabot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {

    private Bot() throws LoginException {
        JDABuilder.createDefault(
                "Lol you thought",
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES
        )
                .disableCache(CacheFlag.EMOTE, CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("Beatzoid develop me!"))
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
