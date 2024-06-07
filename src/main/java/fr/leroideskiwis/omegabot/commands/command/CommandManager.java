package fr.leroideskiwis.omegabot.commands.command;

import fr.leroideskiwis.omegabot.commands.user.OmegaUser;
import fr.leroideskiwis.omegabot.commands.user.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage the commands
 */
public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();
    private final UserManager userManager;
    private final JDA jda;

    public CommandManager(JDA jda, UserManager userManager) {
        this.userManager = userManager;
        this.jda = jda;
    }

    /**
     * Register a command
     * @param name the name of the command
     * @param command the command
     */
    public void register(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * Register the commands in discord
     */
    public void registerInDiscord(){
        jda.updateCommands().addCommands(commands.values().stream().map(Command::register).toList()).queue();
    }

    /**
     * Execute the command
     * @param event the event
     */
    public void execute(SlashCommandInteraction event) {
        OmegaUser user = userManager.from(event.getMember());
        commands.entrySet().stream()
                .filter(command -> command.getKey().equalsIgnoreCase(event.getName()))
                .findFirst()
                .ifPresent(command -> command.getValue().execute(user, event));
    }
}
