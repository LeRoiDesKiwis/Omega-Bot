package fr.leroideskiwis.omegabot.command.goulag;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.events.TimeoutEvent;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class TimeoutBombCommand implements Command {

    private final EventManager eventManager;
    private final UserManager userManager;
    private final int PRICE = 300;

    public TimeoutBombCommand(EventManager eventManager, UserManager userManager) {
        this.eventManager = eventManager;
        this.userManager = userManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("timeoutbomb", "Met au goulag pendant 5min les 3 prochaines personnes qui parleront (" + PRICE + " pts)")
                .setGuildOnly(true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        if(!user.buy(event, PRICE)) return;

        event.reply("Bombe planted hehe").setEphemeral(true).queue();
        event.getChannel().sendMessage("C'est l'heure de faire boom-boom ! :bomb:").queue(message -> eventManager.addEvent(new TimeoutEvent(message, event.getChannel(), userManager)));

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
