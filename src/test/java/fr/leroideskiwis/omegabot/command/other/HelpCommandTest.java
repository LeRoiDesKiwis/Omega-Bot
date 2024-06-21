package fr.leroideskiwis.omegabot.command.other;

import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.command.CommandManager;
import fr.leroideskiwis.omegabot.TestUtil;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HelpCommandTest {


    @Test
    void execute() {

        CommandManager commandManager = mock(CommandManager.class);
        Command command = mock(Command.class);
        SlashCommandData data = Commands.slash("test", "damn");
        when(command.commandData()).thenReturn(data);
        when(commandManager.getByCategory(any())).thenReturn(Map.of("test", command));

        OmegaUser user = mock(OmegaUser.class);
        SlashCommandInteraction event = TestUtil.mockSlashCommandInteraction();

        HelpCommand helpCommand = new HelpCommand(commandManager);
        helpCommand.execute(user, event);
        verify(event, times(1)).replyEmbeds(any(MessageEmbed.class));
        verify(event, never()).reply(anyString());

    }
}