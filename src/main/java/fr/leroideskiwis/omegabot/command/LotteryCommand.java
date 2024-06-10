package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Random;

public class LotteryCommand implements Command{

    private Random random = new Random();
    private final int PRICE = 15;

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("lottery", "Une chance sur 100 de gagner 1000pts");
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        if(!user.hasEnoughPoints(PRICE)){
            event.reply("Tu n'as pas assez de points pour jouer à la lotterie !").queue();
            return;
        }
        user.takePoints(PRICE);
        int number = random.nextInt(100);
        if(number == 1){
            user.givePoints(1000);
            event.reply("Tu as gagné la lotterie ! +1000pts sur ton compte !").queue();
        }else{
            event.reply("Tu as perdu à la lotterie déso.").setEphemeral(true).queue();
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
}
