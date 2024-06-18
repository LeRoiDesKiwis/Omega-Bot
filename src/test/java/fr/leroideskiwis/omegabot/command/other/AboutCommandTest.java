package fr.leroideskiwis.omegabot.command.other;

import fr.leroideskiwis.omegabot.TestUtil;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class AboutCommandTest {

    @Test
    void execute() {

        OmegaUser user = mock(OmegaUser.class);
        SlashCommandInteraction event = TestUtil.mockSlashCommandInteraction();

        AboutCommand helpCommand = new AboutCommand();
        helpCommand.execute(user, event);
        verify(event, times(1)).replyEmbeds(any(MessageEmbed.class));
        verify(event, never()).reply(anyString());

    }

}
