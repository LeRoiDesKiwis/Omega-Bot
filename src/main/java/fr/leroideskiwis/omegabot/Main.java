package fr.leroideskiwis.omegabot;

import fr.leroideskiwis.omegabot.command.CommandManager;
import fr.leroideskiwis.omegabot.command.admin.GivePointsCommand;
import fr.leroideskiwis.omegabot.command.bank.SoldeCommand;
import fr.leroideskiwis.omegabot.command.bank.TransferCommand;
import fr.leroideskiwis.omegabot.command.channels.AnonymousCommand;
import fr.leroideskiwis.omegabot.command.channels.SpecialChannelCommand;
import fr.leroideskiwis.omegabot.command.fun.ClassementCommand;
import fr.leroideskiwis.omegabot.command.fun.LotteryCommand;
import fr.leroideskiwis.omegabot.command.fun.SlotMachineCommand;
import fr.leroideskiwis.omegabot.command.goulag.*;
import fr.leroideskiwis.omegabot.command.goulag.bomb.AddTimeBomb;
import fr.leroideskiwis.omegabot.command.goulag.bomb.GiveBomb;
import fr.leroideskiwis.omegabot.command.goulag.bomb.LockBomb;
import fr.leroideskiwis.omegabot.command.other.AboutCommand;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.listeners.CommandListener;
import fr.leroideskiwis.omegabot.listeners.MessageListener;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

    public static String version = "1.3.2";
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
                new SlotMachineCommand(),
                new ClassementCommand(userManager),
                new SpecialChannelCommand(eventManager)
        );
        commandManager.register(new BombCommand(),
                new AddTimeBomb(userManager),
                new GiveBomb(userManager),
                new LockBomb(userManager),
                new BombCommand.InfoCommand());

        jda.addEventListener(new MessageListener(eventManager, userManager));
        jda.addEventListener(new CommandListener(commandManager));

    }

    public static void main(String[] args) {
        new Main().launch(System.getenv("BOT_TOKEN"));
    }

}
