package fr.leroideskiwis.omegabot.games.connect4;

public class PieceC4 {

    private PlayerC4 player;

    public PieceC4(PlayerC4 player){
        this.player = player;
    }

    public String toString(){
        return player.toString();
    }

}
