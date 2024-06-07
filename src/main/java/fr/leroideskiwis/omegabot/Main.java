package fr.leroideskiwis.omegabot;

import fr.leroideskiwis.omegabot.command.CommandManager;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.listeners.CommandListener;
import fr.leroideskiwis.omegabot.listeners.MessageListener;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.EnumSet;

public class Main {

    private CommandManager commandManager;
    private EventManager eventManager;
    private UserManager userManager;

    private JDA jda;

    private void launch(String token) {
        this.jda = JDABuilder.createLight(token).build();
        this.eventManager = new EventManager();
        this.userManager = new UserManager();
        this.commandManager = new CommandManager(jda, eventManager, userManager);

        jda.addEventListener(new MessageListener(eventManager));
        jda.addEventListener(new CommandListener(commandManager));

    }



    public static void main(String[] args) {
        new Main().launch(args[0]);
    }

}
