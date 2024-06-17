package fr.leroideskiwis.omegabot.command.goulag.bomb;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class LockBomb implements Command {

    private UserManager userManager;

    public LockBomb(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("lock", "empêche l'utilisateur de redonner la bombe. (400 pts)")
                .addOption(OptionType.USER, "user", "utilisateur à lock", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = userManager.from(event.getOption("user").getAsMember());
        target.ifBombPresentOrElse(bomb -> {
            if(!user.buy(event, price())) return;
            bomb.lock();
            event.reply("Vous avez lock la bombe de "+target.getAsMention()).queue();
        }, () -> event.reply("Cet utilisateur n'a pas de bombe !").setEphemeral(true).queue());
    }

    @Override
    public int price() {
        return 400;
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
}
