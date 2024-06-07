package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class GivePointsCommand implements Command{

    private UserManager userManager;

    public GivePointsCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData register() {
        return Commands.slash("givepoints", "Donner des points")
                .addOption(OptionType.INTEGER, "points", "Le nombre de points à donner", true)
                .addOption(OptionType.USER, "user", "L'utilisateur à qui donner les points", false)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        int money = event.getOption("points").getAsInt();
        OptionMapping option = event.getOption("user");
        OmegaUser toGiveUser = option == null ? user : userManager.from(option.getAsMember());
        toGiveUser.givePoints(money);
        String message = money > 0 ? "donne" : "retire";

        event.reply(":dollar: Vous avez "+message+" "+ Math.abs(money) + " points a " + toGiveUser.getAsMention()).queue();
    }
}
