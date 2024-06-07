package fr.leroideskiwis.omegabot.commands.command;

import fr.leroideskiwis.omegabot.commands.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface Command {

    SlashCommandData register();
    void execute(OmegaUser user, SlashCommandInteraction event);
}
