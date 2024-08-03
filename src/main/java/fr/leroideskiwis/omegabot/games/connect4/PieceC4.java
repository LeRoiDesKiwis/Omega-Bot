package fr.leroideskiwis.omegabot.games.connect4;

public class PieceC4 {

    private PlayerC4 player;

    public PieceC4(PlayerC4 player){
        this.player = player;
    }

    public String toString(){
        return player.toString();
    }

    public boolean isPlayer(PlayerC4 player){
        return this.player.equals(player);
    }

    public PlayerC4 getPlayer(){
        return player;
    }

    public boolean isSamePlayer(PieceC4 pieceC4) {
        return isPlayer(pieceC4.getPlayer());
    }
}
