package fr.leroideskiwis.omegabot.command.goulag.bomb;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class AddTimeBomb implements Command {
    private UserManager userManager;

    public AddTimeBomb(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("addtime", "ajoute du temps de goulag ("+price()+"pt/min de plus)")
                .addOption(OptionType.USER, "user", "utilisateur qui possède la bombe", true)
                .addOption(OptionType.INTEGER, "time", "temps à ajouter", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = userManager.from(event.getOption("user").getAsMember());
        int time = event.getOption("time").getAsInt();
        int cost = price() * time;
        target.ifBombPresentOrElse(bomb -> {
            if(!user.buy(event, cost)) return;
            bomb.addGoulagTime(time);
            event.reply("Vous avez ajouté " + time + " minutes de goulag à la bombe de " + target.getAsMention() + " pour " + cost + " points !").queue();
        }, () -> event.reply("Cet utilisateur n'a pas de bombe !").setEphemeral(true).queue());
    }

    @Override
    public int price() {
        return 30;
    }

    @Override
    public boolean isLoggable() {
        return false;
    }

    @Override
    public Category category() {
        return Category.BOUTIQUE_SANCTIONS_BOMB;
    }

    @Override
    public boolean isBlacklisted() {
        return true;
    }

    /*
     * [X] donner une raison aux to
     * XXX supprimer les perms au lieu de juste les deny
     * XXX timestamp dynamic pour les bombes
     * indiquer les prix dans description
     */
}
