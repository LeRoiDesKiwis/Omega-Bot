package fr.leroideskiwis.omegabot;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BombTest {

    private Bomb bomb;
    private TextChannel channel;
    private OmegaUser user;
    private IReplyCallback callback;

    @BeforeEach
    void setUp() {
        this.channel = mock(TextChannel.class);
        MessageCreateAction messageCreateAction = mock(MessageCreateAction.class);
        doNothing().when(messageCreateAction).queue();
        when(channel.sendMessage(anyString())).thenReturn(messageCreateAction);
        when(channel.sendMessageEmbeds(any())).thenReturn(messageCreateAction);

        this.user = mock(OmegaUser.class);
        doNothing().when(user).goulag(anyInt(), anyString());
        doNothing().when(user).giveBomb(any());
        when(user.getAsMention()).thenReturn("mention");

        this.callback = mock(IReplyCallback.class);
        ReplyCallbackAction replyCallbackAction = mock(ReplyCallbackAction.class);
        doNothing().when(replyCallbackAction).queue();
        when(callback.reply(anyString())).thenReturn(replyCallbackAction);

        bomb = new Bomb(user, channel);
    }

    @Test
    void giveBomb() {
        OmegaUser user = mock(OmegaUser.class);
        bomb.giveBomb(callback, user);
        verify(callback).reply(anyString());
        verify(this.user).giveBomb(user);
    }

    @Test
    void explode() {
        bomb.explode();
        verify(channel).sendMessage(anyString());
        verify(user).goulag(anyInt(), anyString());
    }

    @Test
    void tick() {
        int countBefore = Integer.parseInt(bomb.toString().split(" ")[0]);
        bomb.tick();
        int countAfter = Integer.parseInt(bomb.toString().split(" ")[0]);
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    void lock() {
        bomb.lock();
        assertTrue(bomb.isLocked());
    }

    @Test
    void addGoulagTime() {
        int goulagTimeBefore = Integer.parseInt(bomb.toString().split(" ")[1]);
        bomb.addGoulagTime(1);
        int goulagTimeAfter = Integer.parseInt(bomb.toString().split(" ")[1]);
        assertEquals(goulagTimeBefore + 1, goulagTimeAfter);

    }

    @Test
    void addNegativeGoulagTime() {
        int goulagTimeBefore = Integer.parseInt(bomb.toString().split(" ")[1]);
        bomb.addGoulagTime(-1);
        int goulagTimeAfter = Integer.parseInt(bomb.toString().split(" ")[1]);
        assertEquals(goulagTimeBefore, goulagTimeAfter);
    }
}