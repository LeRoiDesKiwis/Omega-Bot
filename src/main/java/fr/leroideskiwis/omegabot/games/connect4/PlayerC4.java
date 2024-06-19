package fr.leroideskiwis.omegabot.games.connect4;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class PlayerC4 {

    private OmegaUser user;
    private String emote;

    public PlayerC4(OmegaUser user, String emote){
        this.user = user;
        this.emote = emote;
    }

    @Override
    public String toString() {
        return emote;
    }

    public boolean isUser(OmegaUser user) {
        return user.equals(this.user);
    }

    public boolean isMember(Member member) {
        return user.isMember(member);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerC4 playerC4)) return false;

        return Objects.equals(user, playerC4.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }

    public String getAsMention() {
        return user.getAsMention();
    }
}
