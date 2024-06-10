package fr.leroideskiwis.omegabot.command;

public enum Category {
    BOUTIQUE_CANAUX(":speaker:"), BOUTIQUE_SANCTIONS(":bomb:"), BOUTIQUE_FUN(":tada:"), BANQUE(":moneybag:"), ADMIN(":no_entry_sign:"), DIVERS(":flushed:");

    String emote;

    Category(String emote){
        this.emote = emote;
    }
}
