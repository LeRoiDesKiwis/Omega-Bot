package fr.leroideskiwis.omegabot.command.other;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.command.CommandManager;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.Color;
import java.util.Map;

public class HelpCommand implements Command {

    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("help", "Get help");
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.ORANGE);
        embedBuilder.setTitle("Aide");
        for(Category category : Category.values()){
            Map<String, Command> commands = commandManager.getByCategory(category);
            if(commands.isEmpty()) continue;
            StringBuilder stringBuilder = new StringBuilder(">>> ");
            commandManager.getByCategory(category).entrySet().stream().sorted((o1, o2) -> o2.getValue().price() - o1.getValue().price()).forEach(entryset -> {
                SlashCommandData slashCommandData = entryset.getValue().commandData();
                //if(command.price() > 0) stringBuilder.append(":dollar: ").append(command.price());
                //stringBuilder.append("`/").append(slashCommandData.getName()).append(" - ").append(slashCommandData.getDescription()).append("`\n");
                stringBuilder.append("`/").append(entryset.getKey()).append(" - ");
                if(entryset.getValue().price() > 0) stringBuilder.append(entryset.getValue().price()).append("pts").append(" - ");
                stringBuilder.append(slashCommandData.getDescription()).append("`\n");
            });
            String categoryName = Util.capitalize(category.name().toLowerCase());
            if(categoryName.contains("_")) {
                String[] split = categoryName.split("_");
                for(int i = 0; i < split.length; i++) {
                    split[i] = Util.capitalize(split[i].toLowerCase());
                }
                categoryName = String.join(" - ", split);
            }


            embedBuilder.addField(category.emote+"  "+categoryName, stringBuilder.toString(), false);
        }
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public boolean isLoggable() {
        return false;
    }

    @Override
    public Category category() {
        return Category.DIVERS;
    }
}
