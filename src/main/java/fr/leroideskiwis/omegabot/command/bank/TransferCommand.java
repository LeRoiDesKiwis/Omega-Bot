package fr.leroideskiwis.omegabot.command.bank;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

public class TransferCommand implements Command {

    private UserManager userManager;

    public TransferCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("transfer", "Transfère des points a un autre utilisateur")
                .addOption(OptionType.USER, "user", "The user to transfer points to", true)
                .addOption(OptionType.INTEGER, "points", "The amount of points to transfer", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = userManager.from(event.getOption("user").getAsMember());
        int points = event.getOption("points").getAsInt();

        if(user.equals(target)){
            event.reply("Tu ne peux pas te donner des points à toi même.").setEphemeral(true).queue();
            return;
        }

        if(points <= 0){
            event.reply("Tu ne peux pas donner un nombre négatif de points.").setEphemeral(true).queue();
            return;
        }

        if(!user.hasEnoughPoints(points)){
            event.reply("Tu n'as pas assez de points pour donner autant de points.").setEphemeral(true).queue();
            return;
        }

        user.transferPoints(target, points);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("Transfert de points entre %s et %s", user.getName(), target.getName()));
        embedBuilder.addField(createField(user.getName(), user.getPoints()+points, user.getPoints()));
        embedBuilder.addField(createField(target.getName(), target.getPoints()-points, target.getPoints()));
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

    @Override
    public Category category() {
        return Category.BANQUE;
    }

    @Override
    public boolean isBlacklisted() {
        return true;
    }
}
