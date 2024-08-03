package fr.leroideskiwis.omegabot.command.goulag;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class ShameGoulagCommand implements Command {

    private UserManager userManager;
    private final int PRICE = 120;

    public ShameGoulagCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("votetimeout", "Goulag de la honte. Si il fonctionne, la personne ciblée se prend 5min, sinon tu te prends 5min.")
                .addOption(OptionType.USER, "user", "La personne ciblée", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser target = userManager.from(event.getOption("user").getAsMember());
        if (target.equals(user)) {
            event.reply("Tu ne peux pas te goulag toi même.").setEphemeral(true).queue();
            return;
        }
        if (!user.buy(event, PRICE)) return;

        event.reply(String.format("Un goulag de la honte a ete émis par %s sur %s.\n" +
                        "Si il y a plus de :white_check_mark: que de :x: sur ce message dans les 30 prochaines secondes, %s se prendra 5min de goulag, sinon %s se prendra 5min.\n" +
                        "Ce vote nécessite la majorité absolue. En cas d'égalité, %s se prendra le goulag.",
                user.getAsMention(), target.getAsMention(), target.getAsMention(), user.getAsMention(), user.getAsMention())).queue(hook -> hook.retrieveOriginal().queue(message -> {
            message.addReaction(Emoji.fromUnicode("\u2705")).queue();
            message.addReaction(Emoji.fromUnicode("\u274C")).queue();

            Timer timer = new Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    int check = message.retrieveReactionUsers(Emoji.fromUnicode("\u2705")).complete()
                            .stream()
                            .filter(user -> !user.isBot())
                            .mapToInt(t -> 1).sum();
                    int cross = message.retrieveReactionUsers(Emoji.fromUnicode("\u274C")).complete().stream()
                            .filter(user -> !user.isBot())
                            .mapToInt(t -> 1).sum();;
                    OmegaUser goulaged = check > cross ? target : user;
                    goulaged.goulag(10, "shame goulag");
                    event.getChannel().sendMessage(String.format("%d :white_check_mark: vs %d :x: : le %s l'emporte donc et %s se prend 5min de goulag.",
                            check, cross, (check > cross ? ":white_check_mark:" : ":x:"), goulaged.getAsMention())).queue();
                }
            }, 30000);
        }));
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
        return false;
    }
}
