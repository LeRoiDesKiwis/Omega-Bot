package fr.leroideskiwis.omegabot.command.fun;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class GrosPuantCommand implements Command {

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("grospuant", "Pose le badge @Gros Puant sur une personne, le retirant de la précédente personne à l'avoir.")
                .addOption(OptionType.USER, "user", "La personne à qui poser le badge", true);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OptionMapping option = event.getOption("user");
        if(option == null){
            event.reply("Vous devez mentionner un utilisateur.").setEphemeral(true).queue();
            return;
        }
        if(!user.buy(event, price())) return;

        Role grosPuant = event.getGuild().getRoleById(System.getenv("GROS_PUANT_ID"));
        event.getGuild().findMembersWithRoles(grosPuant).onSuccess(list -> list.forEach(member -> event.getGuild().removeRoleFromMember(member, grosPuant).queue()));
        event.getGuild().addRoleToMember(event.getOption("user").getAsMember(), grosPuant).queue();
        event.reply("Badge @Gros Puant posé !").setEphemeral(true).queue();
    }

    @Override
    public int price() {
        return 200;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

    @Override
    public Category category() {
        return Category.BOUTIQUE_FUN;
    }

    @Override
    public boolean isBlacklisted() {
        return false;
    }
}
