package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.Constants;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage the commands
 */
public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();
    private final UserManager userManager;
    private final JDA jda;

    public CommandManager(JDA jda, EventManager eventManager, UserManager userManager) {
        this.userManager = userManager;
        this.jda = jda;

        register(new TimeoutBombCommand(eventManager, userManager));
        register(new SoldeCommand(userManager));
        register(new GivePointsCommand(userManager));
        register(new TransferCommand(userManager));
        register(new LotteryCommand());
        register(new RussianRouletteCommand(userManager));
        register(new ShameGoulagCommand(userManager));
        register(new RemoveAutoUserCommand(userManager, eventManager));
        register(new AnonymousCommand());
    }

    /**
     * Register a command
     * @param command the command
     */
    private void register(Command command) {
        SlashCommandData data = command.commandData();
        commands.put(data.getName(), command);
        jda.upsertCommand(data).queue();
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

        event.getGuild().getChannelById(TextChannel.class, System.getenv("LOG_CHANNEL_ID")).sendMessageEmbeds(createLogEmbed(event)).queue();
    }

    private MessageEmbed createLogEmbed(SlashCommandInteraction event){

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setAuthor("Commande executee par LeRoiDesKiwis", null, event.getMember().getAvatarUrl())
                .setDescription(event.getCommandString())
                .setTimestamp(event.getTimeCreated());
        return embedBuilder.build();
    }
}
