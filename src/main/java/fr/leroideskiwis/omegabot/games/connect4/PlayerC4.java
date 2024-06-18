package fr.leroideskiwis.omegabot.games.connect4;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.Member;

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
}
