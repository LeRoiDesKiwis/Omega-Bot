package fr.leroideskiwis.omegabot.command.bank;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SoldeCommand implements Command {

    private UserManager userManager;

    public SoldeCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("solde", "Voir votre solde")
                .addOption(OptionType.USER, "user", "L'utilisateur dont vous voulez voir le solde", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = event.getOption("user") == null ? user : userManager.from(event.getOption("user").getAsMember());
        event.reply(target.getAsMention()+" a " + target.getPoints() + " points.").setEphemeral(true).queue();
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
