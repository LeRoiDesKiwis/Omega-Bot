package fr.leroideskiwis.omegabot;

import fr.leroideskiwis.omegabot.command.CommandManager;
import fr.leroideskiwis.omegabot.command.admin.GivePointsCommand;
import fr.leroideskiwis.omegabot.command.bank.SoldeCommand;
import fr.leroideskiwis.omegabot.command.bank.TransferCommand;
import fr.leroideskiwis.omegabot.command.channels.AnonymousCommand;
import fr.leroideskiwis.omegabot.command.channels.SpecialChannelCommand;
import fr.leroideskiwis.omegabot.command.fun.ClassementCommand;
import fr.leroideskiwis.omegabot.command.fun.GrosPuantCommand;
import fr.leroideskiwis.omegabot.command.fun.LotteryCommand;
import fr.leroideskiwis.omegabot.command.fun.SlotMachineCommand;
import fr.leroideskiwis.omegabot.command.goulag.*;
import fr.leroideskiwis.omegabot.command.goulag.bomb.AddTimeBomb;
import fr.leroideskiwis.omegabot.command.goulag.bomb.GiveBomb;
import fr.leroideskiwis.omegabot.command.goulag.bomb.LockBomb;
import fr.leroideskiwis.omegabot.command.other.AboutCommand;
import fr.leroideskiwis.omegabot.command.other.changelog.ChangelogCommand;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.listeners.CommandListener;
import fr.leroideskiwis.omegabot.listeners.MessageListener;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main extends ListenerAdapter {

    public static String version = "1.6.1";
    private CommandManager commandManager;
    private EventManager eventManager;
    private UserManager userManager;

    private JDA jda;

    private void launch(String token) {
        this.jda = JDABuilder.createLight(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL).build();
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
                new ChangelogCommand(),
                new ClassementCommand(userManager),
                new SpecialChannelCommand(eventManager),
                new GrosPuantCommand()
        );
        commandManager.register(new BombCommand(),
                new AddTimeBomb(userManager),
                new GiveBomb(userManager),
                new LockBomb(userManager),
                new BombCommand.InfoCommand());

        jda.addEventListener(new MessageListener(eventManager, userManager));
        jda.addEventListener(new CommandListener(commandManager));
        jda.addEventListener(this);

    }

    @Override
    public void onReady(ReadyEvent event) {
        jda.getGuilds().forEach(guild -> userManager.loadGuild(guild));
    }

    public static void main(String[] args) {
        new Main().launch(System.getenv("BOT_TOKEN"));
    }

}
