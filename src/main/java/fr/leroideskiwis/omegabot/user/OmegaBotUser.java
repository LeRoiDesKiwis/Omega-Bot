package fr.leroideskiwis.omegabot.user;

import fr.leroideskiwis.omegabot.BuyType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OmegaBotUser extends OmegaUser {
    public OmegaBotUser(Member member) {
        super(member);
    }

    @Override
    public void immune(BuyType type, Date date) {
    }

    @Override
    public boolean isImmune(BuyType type) {
        return true;
    }

    @Override
    public void goulag(int time, String reason) {
    }

    @Override
    public void goulag(int time, TimeUnit unit, String reason) {
    }

    @Override
    public void takePoints(int points) {
    }

    @Override
    public void givePoints(int points) {
    }

    @Override
    public boolean hasEnoughPoints(int points) {
        return false;
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public void save() {
    }

    @Override
    public void load() throws SQLException {
    }

    @Override
    public void createBomb(TextChannel channel) {
    }

    @Override
    public void transferPoints(OmegaUser user, int points) {

    }
}
