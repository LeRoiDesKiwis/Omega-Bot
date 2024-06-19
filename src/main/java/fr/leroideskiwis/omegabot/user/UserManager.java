package fr.leroideskiwis.omegabot.user;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Manage the users
 */
public class UserManager {

    private final List<OmegaUser> users = new ArrayList<>();

    public UserManager(OmegaUser... users){
        this.users.addAll(Arrays.asList(users));
    }

    /**
     * Convert a discord member to an OmegaUser (if the user is not in the list, it will be added)
     * @param member the discord member
     * @return the OmegaUser
     */
    public OmegaUser from(Member member){
        return users.stream().filter(omegaUser -> omegaUser.isMember(member)).findFirst().orElseGet(() -> {
            OmegaUser user = new OmegaUser(member);
            try {
                user.load();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            users.add(user);
            return user;
        });
    }

    public Stream<OmegaUser> stream() {
        return users.stream();
    }

    public void loadGuild(Guild guild) {
        guild.loadMembers().onSuccess(members -> members.forEach(this::from));
    }
}
