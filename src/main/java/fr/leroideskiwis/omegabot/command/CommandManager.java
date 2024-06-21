package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.command.other.HelpCommand;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import fr.leroideskiwis.omegabot.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.HashMap;
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

    public void register(Command command, Command... subcommands){
        SlashCommandData data = command.commandData().setDescription(formatCommandDescription(command, command.commandData().getDescription()));
        if(subcommands.length == 0) commands.put(data.getName(), command);
        for(Command subcommand : subcommands) {
            SlashCommandData slashCommandData = subcommand.commandData();
            commands.put(data.getName()+" "+slashCommandData.getName(), subcommand);

            SubcommandData subcommandData = new SubcommandData(slashCommandData.getName(), formatCommandDescription(subcommand, slashCommandData.getDescription()));
            subcommandData.addOptions(slashCommandData.getOptions());
            data.addSubcommands(subcommandData);
        }
        jda.upsertCommand(data).queue();
    }

    private String formatCommandDescription(Command command, String description){
        if(command.price() == 0) return description;
        String newDescription = String.format("%s - %s", command.price()+"pts", description);
        if(newDescription.length() > 100) return description;
        else return newDescription;
    }

    /**
     * Execute the command
     * @param event the event
     */
    public void execute(SlashCommandInteraction event) {
        OmegaUser user = userManager.from(event.getMember());

        if(event.getOptions().stream()
                .filter(option -> option.getType() == OptionType.USER)
                .map(OptionMapping::getAsUser)
                .anyMatch(User::isBot)){
            event.reply("Vous ne pouvez pas mentionner de bot.").setEphemeral(true).queue();
            return;
        }
        commands.entrySet().stream()
                .filter(command -> command.getKey().equalsIgnoreCase(event.getFullCommandName()))
                .findFirst()
                .ifPresent(entrySet -> {
                    Command command = entrySet.getValue();
                    if(command.isBlacklisted() && !event.getChannelId().equals(System.getenv("BOT_CHANNEL_ID"))){
                        event.reply("Cette commande est désactivée dans ce salon.").setEphemeral(true).queue();
                        return;
                    }
                    int pointsBefore = 0;
                    try {
                        pointsBefore = user.getPoints();
                        command.execute(user, event);
                    }catch(Exception e){
                        e.printStackTrace();
                        user.givePoints(pointsBefore- user.getPoints());
                        event.getChannel().sendMessageEmbeds(MessageUtil.error(e)).queue();
                    }
                    if(command.isLoggable()) event.getGuild().getChannelById(TextChannel.class, System.getenv("LOG_CHANNEL_ID")).sendMessageEmbeds(createLogEmbed(event)).queue();
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

    public Map<String, Command> getByCategory(Category category){
        return commands.entrySet().stream().filter(command -> command.getValue().category() == category).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
