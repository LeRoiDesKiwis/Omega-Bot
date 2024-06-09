package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class HelpCommand implements Command{

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
        StringBuilder builder = new StringBuilder("---- AIDE ---- (<option> = obligatoire, [option] = facultatif)\n");

        commandManager.forEach((name, command) -> {
            builder.append("/").append(name);
            command.commandData().getOptions().forEach(optionData -> {
                        if(optionData.isRequired()) builder.append(" <");
                        else builder.append(" [");
                        builder.append(optionData.getName());
                        if(optionData.isRequired()) builder.append(" >");
                        else builder.append("]");
            });

            builder.append(" : ").append(command.commandData().getDescription());

            if(command.price() > 0) builder.append(" (**Prix: ").append(command.price()).append("pts**)");
            builder.append("\n");
        });
        event.reply(builder.toString()).setEphemeral(true).queue();
    }

    @Override
    public int price() {
        return 0;
    }
}
