package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.command.other.HelpCommand;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import fr.leroideskiwis.omegabot.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Manage the commands
 */
public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();
    private final UserManager userManager;
    private final JDA jda;

    public CommandManager(JDA jda, UserManager userManager, Command... commands) {
        this.userManager = userManager;
        this.jda = jda;
        register(new HelpCommand(this));
        for (Command command : commands) {
            register(command);
        }
        //register(new GrosPuantCommand()); (dont work)
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

    public void register(Command command, boolean registerBaseCommand, Command... subcommands){
        SlashCommandData data = command.commandData();
        if(registerBaseCommand) commands.put(data.getName(), command);
        for(Command subcommand : subcommands) {
            SlashCommandData slashCommandData = subcommand.commandData();
            commands.put(data.getName()+" "+slashCommandData.getName(), subcommand);

            SubcommandData subcommandData = new SubcommandData(slashCommandData.getName(), slashCommandData.getDescription());
            subcommandData.getOptions().forEach(subcommandData::addOptions);
            data.addSubcommands(subcommandData);
        }
        jda.upsertCommand(data).queue();
    }

    /**
     * Execute the command
     * @param event the event
     */
    public void execute(SlashCommandInteraction event) {
        OmegaUser user = userManager.from(event.getMember());
        commands.entrySet().stream()
                .filter(command -> command.getKey().equalsIgnoreCase(event.getFullCommandName()))
                .findFirst()
                .ifPresent(command -> {
                    int pointsBefore = 0;
                    try {
                        pointsBefore = user.getPoints();
                        command.getValue().execute(user, event);
                    }catch(Exception e){
                        e.printStackTrace();
                        user.givePoints(pointsBefore- user.getPoints());
                        event.getChannel().sendMessageEmbeds(MessageUtil.error(e)).queue();
                    }
                    if(command.getValue().isLoggable()) event.getGuild().getChannelById(TextChannel.class, System.getenv("LOG_CHANNEL_ID")).sendMessageEmbeds(createLogEmbed(event)).queue();
                });
    }

    private MessageEmbed createLogEmbed(SlashCommandInteraction event){

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setAuthor("Commande executee par "+event.getMember().getUser().getEffectiveName(), null, event.getMember().getUser().getAvatarUrl())
                .setDescription(event.getCommandString())
                .setTimestamp(event.getTimeCreated());
        return embedBuilder.build();
    }

    public void forEach(BiConsumer<String, Command> consumer) {
        commands.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().price() - o1.getValue().price())
                .forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
    }

    public List<Command> getByCategory(Category category){
        return commands.values().stream().sorted((o1, o2) -> o2.price() - o1.price()).filter(command -> command.category() == category).collect(Collectors.toList());
    }
}
