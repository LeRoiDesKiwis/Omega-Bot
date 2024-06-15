package fr.leroideskiwis.omegabot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MessageUtil {

    public static MessageEmbed error(Exception e){
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("An error occurred : "+e.getClass().getSimpleName())
                .setDescription(e.getMessage()).build();
    }

}
