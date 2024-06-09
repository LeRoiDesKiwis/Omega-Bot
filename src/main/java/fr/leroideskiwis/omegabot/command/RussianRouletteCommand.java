package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.concurrent.TimeUnit;

public class RussianRouletteCommand implements Command{

    private UserManager userManager;
    private final int PRICE = 100;

    public RussianRouletteCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("russianroulette", "Play a russian roulette")
                .addOption(OptionType.USER, "user", "The user to play with", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser toPlayUser = userManager.from(event.getOption("user").getAsMember());
        if(!user.hasEnoughPoints(PRICE)){
            event.reply("You don't have enough points to play russian roulette.").queue();
            return;
        }
        if(toPlayUser.isImmune(BuyType.RUSSIAN_ROULETTE)){
            event.reply(toPlayUser.getName()+" est immunise").setEphemeral(true).queue();
            return;
        }

        user.takePoints(PRICE);
        if(Math.random() < 0.1666666666f){
            event.reply(String.format("%s a joue a la roulette russe et a perdu ! Au goulag !", toPlayUser.getAsMention())).queue();
            toPlayUser.goulag(7, TimeUnit.SECONDS); //10min pour la version finale
        }else{
            event.reply(String.format("%s a gagne a la roulette russe ! +100pts !", toPlayUser.getAsMention())).queue();
            toPlayUser.givePoints(100);
        }
    }

    @Override
    public int price() {
        return PRICE;
    }
}
