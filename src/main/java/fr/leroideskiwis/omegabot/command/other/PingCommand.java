package fr.leroideskiwis.omegabot.command.other;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class PingCommand implements Command {

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("ping", "Teste la connexion au bot");
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        event.reply("Pong!").setEphemeral(true).queue();
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
        return Category.BANQUE;
    }

    @Override
    public boolean isBlacklisted() {
        return false;
    }
}