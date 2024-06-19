package fr.leroideskiwis.omegabot.command.game;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.events.OmegaEvent;
import fr.leroideskiwis.omegabot.games.connect4.BoardC4;
import fr.leroideskiwis.omegabot.games.connect4.PlayerC4;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Timer;

public class Connect4Command implements Command {

    private UserManager userManager;
    private EventManager eventManager;

    public Connect4Command(UserManager userManager, EventManager eventManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("connect4", "Play a game of connect4")
                .addOption(OptionType.USER, "user", "The user you want to play with", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        Member member = event.getOption("user").getAsMember();
        if(member.getUser().isBot() || member.equals(event.getMember())) {
            event.reply("Tu ne peux pas jouer avec un bot ou toi même").setEphemeral(true).queue();
            return;
        }
        OmegaUser target = userManager.from(member);
        event.reply("Starting a game of connect4 with " + target.getAsMention())
                .flatMap(InteractionHook::retrieveOriginal)
                .queue(message -> {
                    BoardC4 board = new BoardC4(message, new PlayerC4(user, ":red_circle:"), new PlayerC4(target, ":blue_circle:"));
                    board.display();
                    eventManager.addEvent(new Connect4Event(board));
                });
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public boolean isLoggable() {
        return false;
    }

    @Override
    public Category category() {
        return Category.GAME;
    }

    @Override
    public boolean isBlacklisted() {
        return false;
    }

    public class Connect4Event implements OmegaEvent{

        private BoardC4 board;
        private int expirationCount;

        public Connect4Event(BoardC4 board) {
            Timer timer = new Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    expirationCount++;
                    if(expirationCount == 90) {
                        timer.cancel();
                        board.forceEnd();
                    }
                }
            }, 0, 1000);
            this.board = board;
        }

        @Override
        public boolean isFinished() {
            return board.isFinished();
        }

        @Override
        public boolean isApplicable(MessageReceivedEvent event) {
            return board.isApplicable(event);
        }

        @Override
        public void apply(MessageReceivedEvent event) {
            if(board.play(event.getMessage().getContentRaw())) {
                board.nextPlayer();
                event.getMessage().delete().queue();
                this.expirationCount = 0;
            }
            board.display();
        }

        @Override
        public void end() {

        }
    }
}
