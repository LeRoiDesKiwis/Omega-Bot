package fr.leroideskiwis.omegabot.command.goulag;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class BombCommand implements Command {

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("bomb", "Bombe un utilisateur");
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {

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
        return Category.BOUTIQUE_SANCTIONS;
    }
}