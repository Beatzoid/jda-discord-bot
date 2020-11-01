package me.beatzoid.jdabot.command.commands.music;

import me.beatzoid.jdabot.Listener;
import me.beatzoid.jdabot.command.CommandContext;
import me.beatzoid.jdabot.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I'm already in a voice channel").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if(!self.hasPermission(memberChannel, Permission.valueOf("VOICE_CONNECT"))) {
            channel.sendMessage("I am missing permissions to join your voice channel.").queue();
            return;
        }

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessageFormat("Connected to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();
        LOGGER.info("Joined voice channel \"{}\" in guild {}", memberChannel.getName(), ctx.getGuild().getName());
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Make the bot join your current voice channel";
    }
}
