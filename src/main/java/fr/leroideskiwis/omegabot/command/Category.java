package fr.leroideskiwis.omegabot.command;

public enum Category {
    BOUTIQUE_CANAUX(":speaker:"),
    BOUTIQUE_SANCTIONS(":smiling_imp:"),
    BOUTIQUE_SANCTIONS_BOMB(":bomb:"),
    BOUTIQUE_FUN(":tada:"),
    BANQUE(":moneybag:"),
    GAME(":video_game:"),
    ADMIN(":no_entry_sign:"),
    DIVERS(":flushed:");

    public String emote;

    Category(String emote){
        this.emote = emote;
    }
}
