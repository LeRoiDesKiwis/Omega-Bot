package fr.leroideskiwis.omegabot.command;

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
    private final int PRICE = 100;

    public ShameGoulagCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("shamegoulag", "Goulag de la honte. Si il fonctionne, la personne ciblée se prend 5min, sinon tu te prends 5min.")
                .addOption(OptionType.USER, "user", "La personne ciblée", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OmegaUser toGoulagUser = userManager.from(event.getOption("user").getAsMember());
        if (toGoulagUser.equals(user)) {
            event.reply("Tu ne peux pas te goulag toi meme.").setEphemeral(true).queue();
            return;
        }
        if (!user.hasEnoughPoints(PRICE)) {
            event.reply("Tu n'as pas assez de points pour goulager quelqu'un.").queue();
            return;
        }
        user.takePoints(PRICE);

        event.reply(String.format("Un goulag de la honte a ete emis par %s sur %s.\n" +
                        "Si il y a plus de :white_check_mark: que de :x: sur ce message dans les 30 prochaines secondes, %s se prendra 5min de goulag, sinon %s se prendra 5min.\n" +
                        "Ce vote necessite la majorite absolue. En cas d'egalite, %s se prendre le goulag.",
                user.getAsMention(), toGoulagUser.getAsMention(), toGoulagUser.getAsMention(), user.getAsMention(), user.getAsMention())).queue(hook -> hook.retrieveOriginal().queue(message -> {
            message.addReaction(Emoji.fromUnicode("\u2705")).queue();
            message.addReaction(Emoji.fromUnicode("\u274C")).queue();

            Timer timer = new Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    int check = message.retrieveReactionUsers(Emoji.fromUnicode("\u2705")).complete().size();
                    int cross = message.retrieveReactionUsers(Emoji.fromUnicode("\u274C")).complete().size();
                    OmegaUser goulaged = check > cross ? toGoulagUser : user;
                    goulaged.goulag(10, TimeUnit.SECONDS);
                    event.getChannel().sendMessage(String.format("%d :white_check_mark: vs %d :x: : le %s l'emporte donc et %s se prend 5min de goulag.",
                            check, cross, (check >= cross ? ":white_check_mark:" : ":x:"), goulaged.getAsMention())).queue();
                }
            }, 5000);
        }));
    }
}
