package fr.leroideskiwis.omegabot.command.other;

import fr.leroideskiwis.omegabot.Main;
import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChangelogCommand implements Command {

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("changelog", "Dernières nouveautées du bot.");
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Nouveautées de la version v" + Main.version).setColor(new Color(109, 50, 109));

        String changelogFilePath = "changelog/" + Main.version + ".md";

        try {
            List<String> changelog = Files.readAllLines(Path.of(changelogFilePath));
            changelog.forEach(line -> builder.appendDescription(line + "\n"));

        } catch (Exception e) {
            builder.setColor(Color.red);
            builder.addField("Pas de changelog pour la v" + Main.version + " trouvé:", "", false);
        }

        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public boolean isLoggable() {
        return false;
    }

    @Override
    public Category category() {
        return Category.DIVERS;
    }

}
