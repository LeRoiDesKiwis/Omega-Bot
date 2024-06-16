package fr.leroideskiwis.omegabot.command.goulag.bomb;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class AddTimeBomb implements Command {
    @Override
    public SlashCommandData commandData() {
        return Commands.slash("addtime", "ajoute du temps de goulag")
                .addOption(OptionType.USER, "user", "utilisateur qui possède la bombe", true)
                .addOption(OptionType.INTEGER, "time", "temps à ajouter", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        event.reply("add time").queue();
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
