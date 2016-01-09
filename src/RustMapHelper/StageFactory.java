/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

/**
 *
 * @author joeca_000
 */
public final class StageFactory {

    private static AddMapPieceStage ADD_MAP_PIECE_STAGE;

    public static AddMapPieceStage getAddMapPieceStage() {
        if (ADD_MAP_PIECE_STAGE == null) {
            ADD_MAP_PIECE_STAGE = new AddMapPieceStage();
        }
        return ADD_MAP_PIECE_STAGE;
    }

    public static void destroyAddMapPieceStage() {
        if (ADD_MAP_PIECE_STAGE != null) {
            ADD_MAP_PIECE_STAGE.close();
            ADD_MAP_PIECE_STAGE = null;
        }
    }
}
