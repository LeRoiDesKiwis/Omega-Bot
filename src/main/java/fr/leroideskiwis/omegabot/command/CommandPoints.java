package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandPoints implements Command{

    private UserManager userManager;

    public CommandPoints(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData register() {
        return Commands.slash("solde", "Voir votre solde")
                .addOption(OptionType.USER, "user", "L'utilisateur dont vous voulez voir le solde", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser toCheck = event.getOption("user") == null ? user : userManager.from(event.getOption("user").getAsMember());
        event.reply(toCheck.getAsMention()+" a " + toCheck.getPoints() + " points.").queue();
    }
}
