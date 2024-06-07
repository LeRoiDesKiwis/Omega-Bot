package fr.leroideskiwis.omegabot.commands.user;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage the users
 */
public class UserManager {

    private final List<OmegaUser> users = new ArrayList<>();

    /**
     * Convert a discord member to an OmegaUser (if the user is not in the list, it will be added)
     * @param member the discord member
     * @return the OmegaUser
     */
    public OmegaUser from(Member member){
        return users.stream().filter(omegaUser -> omegaUser.isUser(member)).findFirst().orElseGet(() -> {
            OmegaUser user = new OmegaUser(member);
            users.add(user);
            return user;
        });
    }
}
