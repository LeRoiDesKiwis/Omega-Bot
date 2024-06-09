package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

public class TransferCommand implements Command{

    private UserManager userManager;

    public TransferCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("transfer", "Transfer points to another user")
                .addOption(OptionType.USER, "user", "The user to transfer points to", true)
                .addOption(OptionType.INTEGER, "points", "The amount of points to transfer", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser toGiveUser = userManager.from(event.getOption("user").getAsMember());
        int points = event.getOption("points").getAsInt();
        if(points <= 0){
            event.reply("You can't transfer negative points.").queue();
            return;
        }
        if(user.hasEnoughPoints(points)){
            event.reply("You don't have enough points to transfer.").queue();
            return;
        }
        user.takePoints(points);
        toGiveUser.givePoints(points);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("Transfert de points entre %s et %s", user.getName(), toGiveUser.getName()));
        embedBuilder.addField(createField(user.getName(), user.getPoints()+points, user.getPoints()));
        embedBuilder.addField(createField(toGiveUser.getName(), toGiveUser.getPoints()-points, toGiveUser.getPoints()));
        embedBuilder.setColor(Color.ORANGE);
        event.replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

    private MessageEmbed.Field createField(String name, int oldSolde, int newSolde){
        return new MessageEmbed.Field("Nouveau solde de "+name, String.format("%d (avant: %d)", newSolde, oldSolde), true);
    }
}
