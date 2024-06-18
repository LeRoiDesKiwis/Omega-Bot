package fr.leroideskiwis.omegabot.command.goulag;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.concurrent.TimeUnit;

public class RussianRouletteCommand implements Command {

    private UserManager userManager;
    private final int PRICE = 100;
    private final float MULTIPLICATOR = 1f;
    private final int CHANCE = 5;

    public RussianRouletteCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("russianroulette", "Une chance sur "+CHANCE+" que la personne mentionnée prenne 10min de goulag, sinon elle gagne "+(int)(PRICE*MULTIPLICATOR)+"pts.")
                .addOption(OptionType.USER, "user", "Le joueur", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = userManager.from(event.getOption("user").getAsMember());

        if(target.isImmune(BuyType.RUSSIAN_ROULETTE)){
            event.reply(target.getName()+" est immunisé").setEphemeral(true).queue();
            return;
        }

        if(!user.buy(event, PRICE)) return;
        if(Math.random() < 1f/CHANCE){
            event.reply(String.format("%s a joué à la roulette russe et a perdu ! Au goulag !", target.getAsMention())).queue();
            target.goulag(7*60+20, TimeUnit.SECONDS, "roulette russe");
        }else{
            event.reply(String.format("%s a gagné à la roulette russe ! +%dpts !", target.getAsMention(), (int)(PRICE*MULTIPLICATOR))).queue();
            target.givePoints((int)(PRICE*MULTIPLICATOR));
        }
    }

    @Override
    public int price() {
        return PRICE;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

    @Override
    public Category category() {
        return Category.BOUTIQUE_SANCTIONS;
    }

    @Override
    public boolean isBlacklisted() {
        return true;
    }
}
