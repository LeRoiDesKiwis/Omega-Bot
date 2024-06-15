package fr.leroideskiwis.omegabot.command.fun;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Random;

public class SlotMachineCommand implements Command {
    private static final Random random = new Random();

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("slotmachine", "Vous pouvez gagner la moitié de votre mise ou la perdre complètement.")
                .addOption(OptionType.INTEGER, "mise", "La mise", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        int mise = event.getOption("mise").getAsInt();
        if(mise < 1){
            event.reply("La mise doit être supérieure à 0.").queue();
            return;
        }
        if(!user.hasEnoughPoints(mise)){
            event.reply("Vous n'avez pas assez de points.").setEphemeral(true).queue();
            return;
        }
        if(random.nextBoolean()){
            int won = random.nextInt((int)(mise*0.5))+1;
            user.givePoints(won);
            event.reply(String.format("Vous avez gagné %d points !", won)).queue();
        } else {
            int lost = random.nextInt(mise)+1;
            user.takePoints(lost);
            event.reply(String.format("Vous avez perdu %d points.", lost)).queue();
        }

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
        return Category.BOUTIQUE_FUN;
    }
}
