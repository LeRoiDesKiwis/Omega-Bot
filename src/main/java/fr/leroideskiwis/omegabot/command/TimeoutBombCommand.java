package fr.leroideskiwis.omegabot.command;

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
        return Commands.slash("timeoutbomb", "Timeout for 5 minutes next 3 users who send a message in this channel.")
                .setGuildOnly(true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {

        if(!user.hasEnoughPoints(PRICE)){
            int missingPoints = PRICE - user.getPoints();
            event.reply(String.format("Vous n'avez pas assez de points pour utiliser cette commande. (il vous manque %d points).", missingPoints)).queue();
            return;
        }
        user.takePoints(PRICE);
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
}
