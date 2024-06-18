package fr.leroideskiwis.omegabot;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import static org.mockito.Mockito.*;

public class TestUtil {

    public static SlashCommandInteraction mockSlashCommandInteraction(){
        SlashCommandInteraction interaction = mock(SlashCommandInteraction.class);
        ReplyCallbackAction replyCallbackAction = mock(ReplyCallbackAction.class);
        when(interaction.reply(anyString())).thenReturn(replyCallbackAction);
        when(interaction.replyEmbeds(any(MessageEmbed.class))).thenReturn(replyCallbackAction);
        when(replyCallbackAction.setEphemeral(anyBoolean())).thenReturn(replyCallbackAction);
        doNothing().when(replyCallbackAction).queue();
        return interaction;
    }

}
