package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface Command {

    SlashCommandData commandData();
    void execute(OmegaUser user, SlashCommandInteraction event);
    int price();
    boolean isLoggable();
    Category category();
}
