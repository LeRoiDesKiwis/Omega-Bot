package fr.leroideskiwis.omegabot.command.admin;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class GivePointsCommand implements Command {

    private UserManager userManager;

    public GivePointsCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("givepoints", "Donner des points")
                .addOption(OptionType.INTEGER, "points", "Le nombre de points à donner", true)
                .addOption(OptionType.USER, "user", "L'utilisateur à qui donner les points", false)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        int money = event.getOption("points").getAsInt();
        if(money == 0){
            event.reply("Rien n'a changé.").setEphemeral(true).queue();
            return;
        }
        OptionMapping option = event.getOption("user");
        OmegaUser toGiveUser = option == null ? user : userManager.from(option.getAsMember());
        toGiveUser.givePoints(money);
        String message = money > 0 ? "donné" : "retiré";
        String emote = money > 0 ? ":chart_with_upwards_trend:" : ":chart_with_downwards_trend:";

        event.reply(String.format(":dollar: %s Vous avez %s %d points à %s (ancien solde: **%d**, nouveau solde: **%d**)", emote, message, Math.abs(money), toGiveUser.getAsMention(), toGiveUser.getPoints()-money, toGiveUser.getPoints())).queue();
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

    @Override
    public Category category() {
        return Category.ADMIN;
    }

    @Override
    public boolean isBlacklisted() {
        return false;
    }


}
