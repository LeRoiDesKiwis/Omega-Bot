package fr.leroideskiwis.omegabot.user;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.database.Database;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        this(member, 100);
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
        if(member.getGuild().getSelfMember().hasPermission(Permission.MODERATE_MEMBERS)) member.timeoutFor(time, unit).queue();
        else member.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage("ERREUR IMPORTANTE: il me manque la permission de timeout !").queue();
    }

    /**
     * remove points from the user
     * @param points the amount of points to remove
     */
    public void takePoints(int points){
        this.points -= points;
        save(); //pas opti mais comme y'a pas bcp de membres ça va
    }

    /**
     * give points to the user
     * @param points the amount of points to give
     */
    public void givePoints(int points){
        this.points += points;
        save(); //pas opti mais comme y'a pas bcp de membres ça va
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
    public boolean isMember(Member member){
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

    public String getName() {
        return member.getEffectiveName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OmegaUser omegaUser = (OmegaUser) o;
        return Objects.equals(member, omegaUser.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member);
    }

    public boolean canSendAt(TextChannel channel) {
        return channel.canTalk(member);
    }

    public void save() {
        try {
            Database.getDatabase().execute("INSERT OR REPLACE INTO users (id, points) VALUES (?, ?)", member.getId(), points);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void load() throws SQLException {
        this.points = Database.getDatabase().getFirst("SELECT * FROM users WHERE id = ?", "points", Integer.class, member.getId())
                .orElse(points);
    }
}
