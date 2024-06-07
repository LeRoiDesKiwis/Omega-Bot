package fr.leroideskiwis.omegabot.user;

import fr.leroideskiwis.omegabot.BuyType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Represents a member of the Omega WTF server
 */
public class OmegaUser {

    private final Member member;
    private int points;
    private final Map<BuyType, Date> immunes = new HashMap<>();

    public OmegaUser(Member member, int points) {
        this.member = member;
        this.points = points;
    }

    public OmegaUser(Member member) {
        this(member, 0);
    }

    /**
     * immune the user to the {@code type} of buy until {@code date}
     * @param type the type of buy
     * @param date the date until the user is immune
     */
    public void immune(BuyType type, Date date){
        immunes.put(type, date);
    }

    /**
     * check if the user is immune to the {@code type} of buy
     * @param type the type of buy
     * @return if the user is immune
     */
    public boolean isImmune(BuyType type){
        return immunes.containsKey(type) && immunes.get(type).before(new Date());
    }

    /**
     * timeout the user for {@code time} minutes
     * @param time duration of the timeout
     * @see #goulag(int, TimeUnit)
     */
    public void goulag(int time){
        goulag(time, TimeUnit.MINUTES);
    }

    /**
     * timeout the user for {@code time} of {@code unit}
     * @param time duration of the timeout
     * @param unit the unit of the duration
     */
    public void goulag(int time, TimeUnit unit){
        member.timeoutFor(time, unit).queue();
    }

    /**
     * remove points from the user
     * @param points the amount of points to remove
     */
    public void takePoints(int points){
        this.points -= points;
    }

    /**
     * give points to the user
     * @param points the amount of points to give
     */
    public void givePoints(int points){
        this.points += points;
    }

    /**
     * check if the user has enough points
     * @param points the amount of points to check
     * @return if the user has enough points
     */
    public boolean hasEnoughPoints(int points) {
        return this.points >= points;
    }

    /**
     * check if the user is the same as the {@code member}
     * @param member the member to compare
     * @return if the user is the same as the {@code member}
     */
    public boolean isUser(Member member){
        return this.member.equals(member);
    }

    /**
     * Give a role to the user
     * @param id the id of the role
     */
    public void giveRole(int id){
        Guild guild = member.getGuild();
        if(guild.getRoleById(id) != null) guild.addRoleToMember(member, guild.getRoleById(id)).queue();
    }

    /**
     * Remove a role from the user
     * @param id the id of the role
     */
    public void removeRole(int id){
        Guild guild = member.getGuild();
        if(guild.getRoleById(id) != null) guild.removeRoleFromMember(member, guild.getRoleById(id)).queue();
    }

    public int getPoints() {
        return points;
    }

    public String getAsMention() {
        return member.getAsMention();
    }
}
