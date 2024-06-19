package fr.leroideskiwis.omegabot;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.awt.Color;
import java.util.Timer;

public class Bomb {

    private OmegaUser user;
    private int count;
    private int goulagTime;
    private boolean locked;
    private TextChannel channel;

    public Bomb(OmegaUser user, TextChannel channel){
        this.user = user;
        this.channel = channel;
        this.count = 300;
        this.goulagTime = 1;
        this.locked = false;
    }

    public void giveBomb(IReplyCallback callback, OmegaUser user){
        if(user.isImmune(BuyType.BOMB)) {
            callback.reply(String.format("%s est immunisé contre les bombes", user.getAsMention())).queue();
            return;
        }
        callback.reply(String.format("Vous avez donné une bombe à %s (pour rappel, elle explosera dans %d secondes)", user.getAsMention(), count)).queue();
        this.user.giveBomb(user);
        this.user = user;
    }

    public void explode(){
        channel.sendMessage(String.format("La bombe a explosé sur %s", user.getAsMention())).queue();
        user.goulag(goulagTime, "bomb");
        user.removeBomb();
    }

    public boolean tick(){
        if(count == 0) {
            explode();
            return true;
        }
        count--;
        return false;
    }

    public void lock(){
        locked = true;
    }

    public boolean isLocked(){
        return locked;
    }
    public void addGoulagTime(int time){
        goulagTime += Math.max(0, time);
    }

    public void run() {
        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                if(tick()) timer.cancel();
            }
        }, 1000, 1000);
    }

    public String toString(){
        return count+" "+goulagTime;
    }

    public MessageEmbed buildInfoEmbed() {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: VOUS POSSÉDEZ UNE BOMBE ! :warning:")
                .addField("Temps restant avant explosion", count+" secondes", false)
                .addField("Temps de goulag", goulagTime+" minutes", false)
                .addField("Bloquée", locked ? "Oui" : "Non", false)
                .build();
    }
}
