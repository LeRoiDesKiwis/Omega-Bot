package fr.leroideskiwis.omegabot.command.goulag;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.events.RemoveMessageAutoEvent;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class RemoveAutoUserCommand implements Command {

    private final int PRICE = 1000;
    private UserManager userManager;
    private EventManager eventManager;

    public RemoveAutoUserCommand(UserManager userManager, EventManager eventManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("mutelight", "Supprime les messages d'un utilisateur pendant 10min")
                .addOption(OptionType.USER, "user", "L'utilisateur qui va recevoir le châtiment mouahaha", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        if(!user.buy(event, PRICE)) return;

        OmegaUser target = userManager.from(event.getOption("user").getAsMember());
        long end = System.currentTimeMillis() + 10*60000;
        eventManager.addEvent(new RemoveMessageAutoEvent(end, target));
        event.reply("L'utilisateur "+target.getAsMention()+" à été puni pendant 10min. :smiling_imp:").setEphemeral(true).queue();
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
