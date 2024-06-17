package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.command.other.HelpCommand;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.RestAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommandManagerTest {

    private CommandManager commandManager;
    private Command command;
    private SlashCommandInteraction slashCommandInteraction;
    private OmegaUser user;
    private UserManager userManager;
    private JDA jda;

    @BeforeEach
    void setUp() {
        userManager = mock(UserManager.class);
        user = mock(OmegaUser.class);
        when(userManager.from(any())).thenReturn(user);

        jda = mock(JDA.class);
        RestAction restAction = mock(RestAction.class);
        when(jda.upsertCommand(any())).thenReturn(restAction);
        doNothing().when(restAction).queue();

        command = mock(Command.class);
        SlashCommandData commandData = mock(SlashCommandData.class);
        when(command.commandData()).thenReturn(commandData);
        when(commandData.getName()).thenReturn("test");
        when(commandData.getDescription()).thenReturn("test");
        when(commandData.setDescription(anyString())).thenReturn(commandData);
        when(command.category()).thenReturn(Category.BOUTIQUE_FUN);
        when(command.price()).thenReturn(100);

        slashCommandInteraction = mock(SlashCommandInteraction.class);
        commandManager = new CommandManager(jda, userManager, command);
    }

    @Test
    void execute() {
        when(slashCommandInteraction.getName()).thenReturn("test");
        when(slashCommandInteraction.getFullCommandName()).thenReturn("test");
        commandManager.execute(slashCommandInteraction);
        verify(command).execute(user, slashCommandInteraction);
    }

    @Test
    void dontexecute() {
        when(slashCommandInteraction.getName()).thenReturn("test1");
        commandManager.execute(slashCommandInteraction);
        verify(command, never()).execute(user, slashCommandInteraction);
    }

    @Test
    void forEach() {
        Map<String, Class<? extends Command>> expected = Map.of("help", HelpCommand.class, "test", command.getClass());
        commandManager.forEach((s, command) -> assertEquals(expected.get(s), command.getClass()));
    }

    @Test
    void getByCategory() {
        assertEquals(0, commandManager.getByCategory(Category.BANQUE).size());
        assertEquals(1, commandManager.getByCategory(Category.DIVERS).size());
        assertEquals(0, commandManager.getByCategory(Category.ADMIN).size());
        assertEquals(0, commandManager.getByCategory(Category.BOUTIQUE_CANAUX).size());
        assertEquals(0, commandManager.getByCategory(Category.BOUTIQUE_SANCTIONS).size());
        assertEquals(1, commandManager.getByCategory(Category.BOUTIQUE_FUN).size());
    }
}