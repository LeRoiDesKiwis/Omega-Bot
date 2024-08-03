package fr.leroideskiwis.omegabot.command.goulag.bomb;

import fr.leroideskiwis.omegabot.BuyType;
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
    private final int CREATION_PRICE = 200;

    public GiveBomb(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("give", "Donne une bombe à un utilisateur (coûte " + CREATION_PRICE + "pts si c'est une création)")
                .addOption(OptionType.USER, "user", "Utilisateur à qui donner la bombe", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = userManager.from(event.getOption("user").getAsMember());
        if (target.hasBomb()) {
            event.reply("Cet utilisateur a déjà une bombe !").setEphemeral(true).queue();
            return;
        }

        user.ifBombPresentOrElse(bomb -> {
                    if (bomb.isLocked()) {
                        event.reply("La bombe ne peut plus être donnée").queue();
                        return;
                    }
                    if (!user.buy(event, price())) return;
                    if(checkImmunity(user, target, event)) return;
                    bomb.giveBomb(event, target);
                },
                () -> {
                    if (!user.buy(event, CREATION_PRICE)) return;
                    if(checkImmunity(user, target, event)) return;

                    target.createBomb(event.getGuildChannel().asTextChannel());
                    event.reply(String.format("%s a donné une bombe à %s !", user.getAsMention(), target.getAsMention())).queue();
                });

    }

    private boolean checkImmunity(OmegaUser user, OmegaUser target, SlashCommandInteraction event){
        if(target.isImmune(BuyType.BOMB)){
            event.reply("Oups ! La bombe t'a glissé des mains et t'as explosé dessus, tu pars donc 5min au goulag. :wave:").queue();
            user.goulag(5, "bomb");
            target.removeImmunity(BuyType.BOMB);
            return true;
        }
        return false;
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
}
