package fr.leroideskiwis.omegabot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
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

        JDA jda = mock(JDA.class);
        when(jda.getSelfUser()).thenReturn(mock(SelfUser.class));
        when(interaction.getJDA()).thenReturn(jda);
        return interaction;
    }

}
