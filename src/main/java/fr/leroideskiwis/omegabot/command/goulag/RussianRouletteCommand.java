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
        OmegaUser toPlayUser = userManager.from(event.getOption("user").getAsMember());

        if(toPlayUser.isImmune(BuyType.RUSSIAN_ROULETTE)){
            event.reply(toPlayUser.getName()+" est immunisé").setEphemeral(true).queue();
            return;
        }

        if(!user.buy(event, PRICE)) return;
        if(Math.random() < 1f/CHANCE){
            event.reply(String.format("%s a joué à la roulette russe et a perdu ! Au goulag !", toPlayUser.getAsMention())).queue();
            toPlayUser.goulag(10, "roulette russe"); //10min pour la version finale
        }else{
            event.reply(String.format("%s a gagné à la roulette russe ! +%dpts !", toPlayUser.getAsMention(), (int)(PRICE*MULTIPLICATOR))).queue();
            toPlayUser.givePoints((int)(PRICE*MULTIPLICATOR));
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
}
