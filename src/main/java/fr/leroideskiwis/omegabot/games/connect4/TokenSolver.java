package fr.leroideskiwis.omegabot.games.connect4;

import java.util.Optional;

public class TokenSolver {

    private BoardC4 board;
    private int x;
    private int y;

    public TokenSolver(BoardC4 board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public Optional<PlayerC4> solve(){
        if(board.getPiece(x, y).isEmpty()) return Optional.empty();
        for(int i = 0; i < 9; i++){
            int offsetX = (i % 3)-1;
            int offsetY = (i / 3)-1;
            if(offsetX == 0 && offsetY == 0) continue;
            Optional<PlayerC4> player = solve(x+offsetX, y+offsetY, offsetX, offsetY, 1);
            if(player.isPresent()) return player;
        }
        return Optional.empty();
    }

    private Optional<PlayerC4> solve(int x, int y, int offsetX, int offsetY, int counter) {
        if(x < 0 || x >= 7 || y < 0 || y >= 7) return Optional.empty();

        Optional<PieceC4> piece = board.getPiece(this.x, this.y);
        if(counter == 4) return Optional.of(piece.get().getPlayer());

        Optional<PieceC4> piece1 = board.getPiece(x, y);

        if(piece1.isPresent() && piece.get().isSamePlayer(piece1.get())){
            return solve(x+offsetX, y+offsetY, offsetX, offsetY, counter+1);
        }
        return Optional.empty();
    }
}
