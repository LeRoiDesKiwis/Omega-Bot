package fr.leroideskiwis.omegabot.command.other;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.command.CommandManager;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.Color;
import java.util.List;

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
            List<Command> commands = commandManager.getByCategory(category);
            if(commands.isEmpty()) continue;
            StringBuilder stringBuilder = new StringBuilder(">>> ");
            for(Command command : commands){
                SlashCommandData slashCommandData = command.commandData();
                //if(command.price() > 0) stringBuilder.append(":dollar: ").append(command.price());
                //stringBuilder.append("`/").append(slashCommandData.getName()).append(" - ").append(slashCommandData.getDescription()).append("`\n");
                stringBuilder.append("`/").append(slashCommandData.getName()).append(" - ");
                if(command.price() > 0) stringBuilder.append(command.price()).append("pts").append(" - ");
                stringBuilder.append(slashCommandData.getDescription()).append("`\n");
            }
            //stringBuilder.append("");
            embedBuilder.addField(category.emote+"  "+category.name(), stringBuilder.toString(), false);
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
