package fr.leroideskiwis.omegabot.command.goulag.bomb;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class GiveBomb implements Command {
    @Override
    public SlashCommandData commandData() {
        return Commands.slash("give", "Donne une bombe à un utilisateur")
                .addOption(OptionType.USER, "user", "Utilisateur à qui donner la bombe", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        event.reply("give").queue();
    }

    @Override
    public int price() {
        return 50;
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
