package fr.leroideskiwis.omegabot;

import fr.leroideskiwis.omegabot.command.*;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.listeners.CommandListener;
import fr.leroideskiwis.omegabot.listeners.MessageListener;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class Main {

    public static String version = "1.2.0";
    private CommandManager commandManager;
    private EventManager eventManager;
    private UserManager userManager;

    private JDA jda;

    private void launch(String token) {
        this.jda = JDABuilder.createLight(token).build();
        this.eventManager = new EventManager();
        this.userManager = new UserManager();
        this.commandManager = new CommandManager(jda, userManager,
                new TimeoutBombCommand(eventManager, userManager),
                new SoldeCommand(userManager),
                new GivePointsCommand(userManager),
                new TransferCommand(userManager),
                new LotteryCommand(),
                new RussianRouletteCommand(userManager),
                new ShameGoulagCommand(userManager),
                new RemoveAutoUserCommand(userManager, eventManager),
                new AnonymousCommand(),
                new AboutCommand(),
                new SlotMachineCommand()
        );

        jda.addEventListener(new MessageListener(eventManager, userManager));
        jda.addEventListener(new CommandListener(commandManager));

    }

    public static void main(String[] args) {
        new Main().launch(System.getenv("BOT_TOKEN"));
    }

}
