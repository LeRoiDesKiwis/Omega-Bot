package fr.leroideskiwis.omegabot.command.fun;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassementCommand implements Command {

    private UserManager userManager;

    public ClassementCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("classement", "Affiche le classement des membres")
                .addOption(OptionType.USER, "user", "Utilisateur", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {

        Member member = event.getOption("user") == null ? event.getMember() : event.getOption("user").getAsMember();

        StringBuilder builder = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(1);
        userManager.stream()
                .sorted((o1, o2) -> o2.getPoints() - o1.getPoints())
                .forEach(omegaUser -> {
                    int i = counter.getAndIncrement();
                    if(i <= 10 || omegaUser.isMember(member)){
                        builder.append(i)
                                .append(" - ")
                                .append(omegaUser.getName())
                                .append(" (**")
                                .append(omegaUser.getRealName())
                                .append("**) : ")
                                .append(omegaUser.getPoints())
                                .append("\n");
                    }
                });
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Classement")
                .setColor(Color.ORANGE)
                .setDescription(builder.toString())
                .build()).queue();
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

    @Override
    public boolean isBlacklisted() {
        return true;
    }
}
