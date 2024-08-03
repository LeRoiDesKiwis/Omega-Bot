package fr.leroideskiwis.omegabot.command.goulag.bomb;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ImmuneBomb implements Command {

    private final int TIME = 30;

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("immune", "Become immune to the bomb");
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        if(!user.buy(event, price())) return;
        event.reply(String.format("Tu es maintenant immunisé contre les bombes pendant %d minutes.", TIME)).setEphemeral(true).queue();
        user.immune(BuyType.BOMB, new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(TIME)));
    }

    @Override
    public int price() {
        return 50;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

    @Override
    public Category category() {
        return Category.BOUTIQUE_SANCTIONS_BOMB;
    }

    @Override
    public boolean isBlacklisted() {
        return false;
    }
}
