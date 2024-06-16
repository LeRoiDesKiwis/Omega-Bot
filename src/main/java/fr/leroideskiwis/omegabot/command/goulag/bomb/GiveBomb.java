package fr.leroideskiwis.omegabot.command.goulag.bomb;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class GiveBomb implements Command {

    private final UserManager userManager;

    public GiveBomb(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("give", "Donne une bombe à un utilisateur (coûte 50pts de plus si c'est une création")
                .addOption(OptionType.USER, "user", "Utilisateur à qui donner la bombe", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = userManager.from(event.getOption("user").getAsMember());
        if(target.hasBomb()) {
            event.reply("Cet utilisateur a déjà une bombe !").queue();
            return;
        }

        if(user.hasBomb()) {
            if(!user.buy(event, price())) return;
            user.ifBombPresentOrElse(bomb -> bomb.giveBomb(event, target), () -> {});
        }
        else {
            if(!user.buy(event, price()+50)) return;
            target.createBomb(event.getGuildChannel().asTextChannel());
            event.reply("Bombe créée et donnée à "+target.getAsMention()).queue();
        }

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
