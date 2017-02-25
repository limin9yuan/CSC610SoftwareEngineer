/* file: Board.java
 *
 * rule book:
 *   http://veryspecial.us/free-downloads/AncientChess.com-DouShouQi.pdf
 * video of game play & nice picture of initial board setup:
 *   http://ancientchess.com/page/play-doushouqi.htm
 * play online:
 *   http://liacs.leidenuniv.nl/~visjk/doushouqi/
 *
 * Copyright George J. Grevera, 2016. All rights reserved.
 */
public class Board {

    //the low order 5 bits is one of the following playing/moveable pieces
    // (or none):
    public static final byte  rbNone    =  0;
    //constants for red pieces
    public static final byte  rRat      =  1;
    public static final byte  rCat      =  2;
    public static final byte  rDog      =  3;
    public static final byte  rWolf     =  4;
    public static final byte  rLeopard  =  5;
    public static final byte  rTiger    =  6;
    public static final byte  rLion     =  7;
    public static final byte  rElephant =  8;
    //constants for black (or blue, if you prefer) pieces
    public static final byte  bRat      =  9;
    public static final byte  bCat      = 10;
    public static final byte  bDog      = 11;
    public static final byte  bWolf     = 12;
    public static final byte  bLeopard  = 13;
    public static final byte  bTiger    = 14;
    public static final byte  bLion     = 15;
    public static final byte  bElephant = 16;

    public static final byte  fPieceMask = (byte)0x1f;  //to isolate piece bits

    //the high order 3 bits indicates one of these board position type values.
    // note: these never move but are part of the board itself.
    public static final byte  cNone   = 0;               //not used/out of bounds
    public static final byte  cWater  = (byte)(1 << 5);  //water
    public static final byte  cGround = (byte)(2 << 5);  //ordinary ground
    public static final byte  cRTrap  = (byte)(3 << 5);  //red (side of board) trap
    public static final byte  cBTrap  = (byte)(4 << 5);  //black (side of board) trap
    public static final byte  cRDen   = (byte)(5 << 5);  //red (side of board) den
    public static final byte  cBDen   = (byte)(6 << 5);  //black (side of board) den

    public static final byte  fBoardMask = (byte)0xe0;   //to isolate type bits

    public static final int   fRows = 9;  //# of board rows
    public static final int   fCols = 7;  //# of board cols

    public static enum Color { None, Red, Black };  //color of piece (or none)
    //-----------------------------------------------------------------------
    //the playing board.  mBoard[0][0] is the upper left corner.
    protected byte     mBoard[][]  = new byte[ fRows ][ fCols ];
    protected boolean  mBlacksTurn = true;  //by convention, black goes first
    //-----------------------------------------------------------------------
    // init the board.  by convention, red will initially be in the top half
    // (0,0) of the board, and black will start in the bottom half.
    // be careful.  the opposite sides do not mirror each other!
    public Board ( ) {
        // \todo v1
        //set ground
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 7; j++)
                mBoard[i][j] = cGround;
        }
        int mCol = fCols / 2;
        //set red den
        mBoard [0][mCol] = cRDen;
        //set black den
        mBoard [8][mCol] = cBDen;
        //set water
        for (int i = 3; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                if (j != 3) {
                    mBoard[i][j] = cWater;
                }
            }
        }
        //set red trap
        for (int i = 0; i < 2; i++){
            for (int j = 2; j < 5; j++){
                if (i == 0 && j != 3){
                    mBoard[i][j] = cRTrap;
                }
                if (i == 1 && j != 2 && j != 4){
                    mBoard[i][j] = cRTrap;
                }
            }
        }
        //set black trap
        for (int i = 7; i < 9; i++){
            for (int j = 2; j < 5; j++){
                if (i == 8 && j != 3){
                    mBoard[i][j] = cBTrap;
                }
                if (i == 7 && j != 2 && j != 4){
                    mBoard[i][j] = cBTrap;
                }
            }
        }
        //set red pieces
        mBoard[0][0] |= rLion;
        mBoard[0][6] |= rTiger;
        mBoard[1][1] |= rDog;
        mBoard[1][5] |= rCat;
        mBoard[2][0] |= rRat;
        mBoard[2][2] |= rLeopard;
        mBoard[2][4] |= rWolf;
        mBoard[2][6] |= rElephant;
        //set black pieces
        mBoard[6][0] |= bElephant;
        mBoard[6][2] |= bWolf;
        mBoard[6][4] |= bLeopard;
        mBoard[6][6] |= bRat;
        mBoard[7][1] |= bCat;
        mBoard[7][5] |= bDog;
        mBoard[8][0] |= bTiger;
        mBoard[8][6] |= bLion;
    }
    //-----------------------------------------------------------------------
    // return the specific (moveable) piece (e.g., bWolf or rbNone) at the
    // indicated position.
    public int getPiece ( int r, int c ) {
        // \todo v1
        if (r >= fRows || c >= fCols || r < 0 || c < 0)
            return rbNone;
        else
            return fPieceMask & mBoard[r][c];
    }
    //-----------------------------------------------------------------------
    // given a piece, return its rank (or 0 for an unknown piece).
    // rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6, lion is 7, elephant is 8.
    public int getRank ( int p ) {
        // \todo v1
        if (p == rbNone) return rbNone;
        if (p % rElephant == 0)
            return rElephant;
        else
            return p % rElephant;
    }
    //-----------------------------------------------------------------------
    // return the rank of the piece at the specified position (or 0 for none).
    // rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6, lion is 7, elephant is 8.
    public int getRank ( int r, int c ) {
        // \todo v1
        if (r >= fRows || c >= fCols || r < 0 || c < 0)
            return rbNone;
        int p = fPieceMask & mBoard[r][c];
        if (p == rbNone) return rbNone;
        if (p % rElephant == 0)
            return rElephant;
        else
            return p % rElephant;
    }
    //-----------------------------------------------------------------------
    // returns what appears on the underlying board at the specified position
    // (e.g., cWater), or cNone if out of bounds.
    public int getBoard ( int r, int c ) {
        // \todo v1
        if (r >= fRows || c >= fCols || r < 0 || c < 0)
            return rbNone;
        else
            return fBoardMask & mBoard[r][c];
    }
    //-----------------------------------------------------------------------
    // returns the color of the piece (or Color.None) at the specified location.
    public Color getColor( int r, int c ) {
        // \todo v1
        if (r >= fRows || c >= fCols || r < 0 || c < 0)
            return Color.None;
        int p = fPieceMask & mBoard[r][c];
        if (p == rbNone)
            return Color.None;
        else if (p - rElephant > 0)
            return Color.Black;
        else
            return Color.Red;
    }
    //-----------------------------------------------------------------------
    // returns t if this spot does not have any (moveable) piece on it;
    // f otherwise or if out of bounds.
    public boolean isEmpty ( int r, int c ) {
        // \todo v1
        int p = fPieceMask & mBoard[r][c];
        if (r >= fRows || c >= fCols || r < 0 || c < 0)
            return false;
        if (r < fRows && c < fCols && r >= 0 && c >= 0){
            if (p != rbNone){
                return true;
            }
        }
        return false;
    }
    //=======================================================================
    // version 2
    //   use these rules for game play (no variations except for the one noted
    //   below):
    //     http://veryspecial.us/free-downloads/AncientChess.com-DouShouQi.pdf
    //
    //   required variation (from https://en.wikipedia.org/wiki/Jungle_(board_game)):
    //     "All traps are universal. If an animal goes into a trap in its own
    //     region, an opponent animal is able to capture it regardless of rank
    //     difference if it is beside the trapped animal."  this avoids the
    //     known draw described in http://www.chessvariants.com/other.dir/shoudouqi2.html.
    //
    //   clarification: do not allow moves/captures where the attacker "loses."
    //     for example, do not allow the mouse to attack the lion and "lose"
    //     to the lion on purpose and be removed.
    //   as much as possible, use the functions that you have already defined.
    //-----------------------------------------------------------------------
    // returns true if the proposed move is valid (regardless of whose turn it is).
    // false otherwise.
    // (this is the most challenging part of the assignment.)
    protected boolean isValidMove ( int fromRow, int fromCol, int toRow, int toCol ) {
        // \todo v2
        int fromPiece = getPiece(fromRow, fromCol);
        int toPiece = getPiece(toRow, toCol);
        int board = getBoard(toRow, toCol);
        if (fromRow >= fRows || fromCol >= fCols || toRow >= fRows || toCol >= fCols || fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0)
            return false;
        //check elephant
        if (fromPiece == rElephant || fromPiece == bElephant) {
            //check move
            if (toRow == fromRow && toCol == fromCol)
                return false;
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rElephant) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant || toPiece == bRat)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bElephant) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == rRat)
                            return false;
                    }
                    //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;
                }
                return true;
            }
        }
        //check lion
        if (fromPiece == rLion || fromPiece == bLion){
            if (toRow == fromRow && toCol == fromCol)
                return false;
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rLion) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rElephant || toPiece == bElephant)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bLion) {
                    //check board
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bElephant || toPiece == rElephant)
                            return false;
                    }
                    //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;
                }
                return true;
            }
            //jump over water
            if (getBoard(fromRow + 1, fromCol) == cWater || getBoard(fromRow - 1, fromCol) == cWater
                    || getBoard(fromRow, fromCol + 1) == cWater || getBoard(fromRow, fromCol - 1) == cWater) {
                System.out.println("water 1");
                if (((toRow == fromRow) && (toCol == fromCol + 3)) || ((toRow == fromRow + 4) && (toCol == fromCol))
                        || ((toRow == fromRow) && (toCol == fromCol - 3)) || ((toRow == fromRow - 4) && (toCol == fromCol))) {
                    System.out.println("jump1");
                    //check red piece
                    if (fromPiece == rLion) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rElephant || toPiece == bElephant)
                            return false;
                        if (board == cWater)
                            return false;
                        if ((fromRow - toRow > 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow - 1, fromCol) == rRat || getPiece(fromRow - 2, fromCol) == rRat
                                    || getPiece(fromRow - 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow - 1, fromCol) == bRat || getPiece(fromRow - 2, fromCol) == bRat
                                    || getPiece(fromRow - 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow - toRow < 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow + 1, fromCol) == rRat || getPiece(fromRow + 2, fromCol) == rRat
                                    || getPiece(fromRow + 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow + 1, fromCol) == bRat || getPiece(fromRow + 2, fromCol) == bRat
                                    || getPiece(fromRow + 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol > 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol - 1) == rRat || getPiece(fromRow, fromCol - 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol - 1) == bRat || getPiece(fromRow, fromCol - 2) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol < 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol + 1) == rRat || getPiece(fromRow, fromCol + 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol + 1) == bRat || getPiece(fromRow, fromCol + 2) == bRat)
                                return false;
                        }
                    }
                    //check black piece
                    if (fromPiece == bLion) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bElephant || toPiece == rElephant)
                            return false;
                        if (board == cWater)
                            return false;
                        //check Rat
                        if ((fromRow - toRow > 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow - 1, fromCol) == rRat || getPiece(fromRow - 2, fromCol) == rRat
                                    || getPiece(fromRow - 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow - 1, fromCol) == bRat || getPiece(fromRow - 2, fromCol) == bRat
                                    || getPiece(fromRow - 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow - toRow < 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow + 1, fromCol) == rRat || getPiece(fromRow + 2, fromCol) == rRat
                                    || getPiece(fromRow + 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow + 1, fromCol) == bRat || getPiece(fromRow + 2, fromCol) == bRat
                                    || getPiece(fromRow + 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol > 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol - 1) == rRat || getPiece(fromRow, fromCol - 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol - 1) == bRat || getPiece(fromRow, fromCol - 2) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol < 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol + 1) == rRat || getPiece(fromRow, fromCol + 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol + 1) == bRat || getPiece(fromRow, fromCol + 2) == bRat)
                                return false;
                        }
                    }
                    return true;
                }
            }
        }
        //check tiger
        if (fromPiece == rTiger || fromPiece == bTiger) {
            if (toRow == fromRow && toCol == fromCol)
                return false;
            //check move
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rTiger) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rLion || toPiece == rElephant || toPiece == bLion || toPiece == bElephant)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bTiger) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bLion || toPiece == bElephant || toPiece == rLion || toPiece == rElephant)
                            return false;
                    }
                        //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;
                }
                return true;
            }
            //jump over water
            if (getBoard(fromRow + 1, fromCol) == cWater || getBoard(fromRow - 1, fromCol) == cWater
                    || getBoard(fromRow, fromCol + 1) == cWater || getBoard(fromRow, fromCol - 1) == cWater) {
                System.out.println("water 1");
                if (((toRow == fromRow) && (toCol == fromCol + 3)) || ((toRow == fromRow + 4) && (toCol == fromCol))
                        || ((toRow == fromRow) && (toCol == fromCol - 3)) || ((toRow == fromRow - 4) && (toCol == fromCol))) {
                    System.out.println("jump1");
                    //check red piece
                    if (fromPiece == rTiger) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rLion || toPiece == rElephant || toPiece == bLion || toPiece == bElephant)
                            return false;
                        if (board == cWater)
                            return false;
                        if ((fromRow - toRow > 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow - 1, fromCol) == rRat || getPiece(fromRow - 2, fromCol) == rRat
                                    || getPiece(fromRow - 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow - 1, fromCol) == bRat || getPiece(fromRow - 2, fromCol) == bRat
                                    || getPiece(fromRow - 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow - toRow < 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow + 1, fromCol) == rRat || getPiece(fromRow + 2, fromCol) == rRat
                                    || getPiece(fromRow + 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow + 1, fromCol) == bRat || getPiece(fromRow + 2, fromCol) == bRat
                                    || getPiece(fromRow + 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol > 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol - 1) == rRat || getPiece(fromRow, fromCol - 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol - 1) == bRat || getPiece(fromRow, fromCol - 2) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol < 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol + 1) == rRat || getPiece(fromRow, fromCol + 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol + 1) == bRat || getPiece(fromRow, fromCol + 2) == bRat)
                                return false;
                        }
                    }
                    //check black piece
                    if (fromPiece == bTiger) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bLion || toPiece == bElephant || toPiece == rLion ||toPiece == rElephant)
                            return false;
                        if (board == cWater)
                            return false;
                        //check Rat
                        if ((fromRow - toRow > 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow - 1, fromCol) == rRat || getPiece(fromRow - 2, fromCol) == rRat
                                    || getPiece(fromRow - 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow - 1, fromCol) == bRat || getPiece(fromRow - 2, fromCol) == bRat
                                    || getPiece(fromRow - 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow - toRow < 0) && (fromCol == toCol)){
                            //check rRat
                            if (getPiece(fromRow + 1, fromCol) == rRat || getPiece(fromRow + 2, fromCol) == rRat
                                    || getPiece(fromRow + 3, fromCol) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow + 1, fromCol) == bRat || getPiece(fromRow + 2, fromCol) == bRat
                                    || getPiece(fromRow + 3, fromCol) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol > 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol - 1) == rRat || getPiece(fromRow, fromCol - 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol - 1) == bRat || getPiece(fromRow, fromCol - 2) == bRat)
                                return false;
                        }
                        if ((fromRow == toRow) && (fromCol - toCol < 0)){
                            //check rRat
                            if (getPiece(fromRow, fromCol + 1) == rRat || getPiece(fromRow, fromCol + 2) == rRat)
                                return false;
                            //check bRat
                            if (getPiece(fromRow, fromCol + 1) == bRat || getPiece(fromRow, fromCol + 2) == bRat)
                                return false;
                        }
                    }
                    return true;
                }
            }
        }
        //check leopard
        if (fromPiece == rLeopard || fromPiece == bLeopard) {
            if (toRow == fromRow && toCol == fromCol)
                return false;
            //check move
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rLeopard) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant || toPiece == bTiger || toPiece == bLion
                                || toPiece == bElephant)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bLeopard) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant || toPiece == rTiger || toPiece == rLion
                                || toPiece == rElephant)
                            return false;
                    }
                    //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;
                }
                return true;
            }
        }
        //check wolf
        if (fromPiece == rWolf || fromPiece == bWolf) {
            if (toRow == fromRow && toCol == fromCol)
                return false;
            //check move
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rWolf) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rLeopard || toPiece == rTiger
                                || toPiece == rLion || toPiece == rElephant || toPiece == bLeopard || toPiece == bTiger || toPiece == bLion
                                || toPiece == bElephant)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bWolf) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bLeopard || toPiece == bTiger
                                || toPiece == bLion || toPiece == bElephant || toPiece == rLeopard || toPiece == rTiger || toPiece == rLion
                                || toPiece == rElephant)
                            return false;
                    }
                    //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;

                }
                return true;
            }
        }
        //check dog
        if (fromPiece == rDog || fromPiece == bDog) {
            if (toRow == fromRow && toCol == fromCol)
                return false;
            //check move
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rDog) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rRat || toPiece == rCat || toPiece == rWolf || toPiece == rLeopard || toPiece == rTiger
                                || toPiece == rLion || toPiece == rElephant || toPiece == bWolf || toPiece == bLeopard || toPiece == bTiger
                                || toPiece == bLion || toPiece == bElephant)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bDog) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bRat || toPiece == bCat || toPiece == bWolf || toPiece == bLeopard || toPiece == bTiger
                                || toPiece == bLion || toPiece == bElephant || toPiece == rWolf || toPiece == rLeopard || toPiece == rTiger
                                || toPiece == rLion || toPiece == rElephant)
                            return false;
                    }
                    //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;
                }
                return true;
            }
        }
        //check cat
        if (fromPiece == rCat || fromPiece == bCat) {
            if (toRow == fromRow && toCol == fromCol)
                return false;
            //check move
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rCat) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rRat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard || toPiece == rTiger
                                || toPiece == rLion || toPiece == rElephant || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bCat) {
                    //check water
                    if (board == cWater)
                        return false;
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bRat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard || toPiece == bTiger
                                || toPiece == bLion || toPiece == bElephant || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                    }
                    //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;
                }
                return true;
            }
        }
        //check rat
        if (fromPiece == rRat || fromPiece == bRat) {
            if (toRow == fromRow && toCol == fromCol)
                return false;
            //check move
            if ((((toRow == fromRow + 1) || (toRow == fromRow - 1)) && (toCol == fromCol))
                    || (((toCol == fromCol + 1) || (toCol == fromCol - 1)) && (toRow == fromRow))) {
                //check red piece
                if (fromPiece == rRat) {
                    //check attack from water
                    if (getBoard(fromRow, fromCol) == cWater) {
                        if (toPiece == bElephant)
                            return false;
                    }
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return true;
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard || toPiece == rTiger
                                || toPiece == rLion || toPiece == rElephant || toPiece == bCat || toPiece == bDog || toPiece == bWolf
                                || toPiece == bLeopard || toPiece == bTiger || toPiece == bLion)
                            return false;
                    }
                    //check bDen
                    if (board == cBDen)
                        return true;
                    if (board == cRDen)
                        return false;
                }
                //check black piece
                if (fromPiece == bRat) {
                    //check attack from water
                    if (getBoard(fromRow, fromCol) == cWater) {
                        if (toPiece == rElephant)
                            return false;
                    }
                    //check trap
                    if (board == cRTrap || board == cBTrap) {
                        if (toPiece == rRat || toPiece == rCat || toPiece == rDog || toPiece == rWolf || toPiece == rLeopard
                                || toPiece == rTiger || toPiece == rLion || toPiece == rElephant)
                            return true;
                        if (toPiece == bRat || toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard
                                || toPiece == bTiger || toPiece == bLion || toPiece == bElephant)
                            return false;
                        return true;
                    }
                    if (board != cRTrap || board != cBTrap){
                        if (toPiece == bCat || toPiece == bDog || toPiece == bWolf || toPiece == bLeopard || toPiece == bTiger
                                || toPiece == bLion || toPiece == bElephant || toPiece == rCat || toPiece == rDog || toPiece == rWolf
                                || toPiece == rLeopard || toPiece == rTiger || toPiece == rLion)
                            return false;
                    }
                    //check rDen
                    if (board == cRDen)
                        return true;
                    if (board == cBDen)
                        return false;
                }
                return true;
            }
        }
        return false;
    }
    //-----------------------------------------------------------------------
    // perform the specified move but only if it's valid.
    // returns true if the proposed move is valid (regardless of whose turn it is).
    // false otherwise.
    public boolean doMove ( int fromRow, int fromCol, int toRow, int toCol ) {
        // \todo v2
        if (isValidMove(fromRow,fromCol,toRow,toCol) == true){
            int piece = getPiece(fromRow,fromCol);
            mBoard[fromRow][fromCol] &= fBoardMask;
            mBoard[toRow][toCol] &= fBoardMask;
            mBoard[toRow][toCol] |=(byte) piece;
            return true;
        }
        else
            return false;
    }
    //=======================================================================
    // version 3
    //=======================================================================
    // returns the number of black pieces remaining on the board.
    // (accomplish this by scanning the entire board while counting the black pieces.)
    public int countBlack ( ) {
        // \todo v3
        int count = 0;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 7; j++){
                if (getColor(i, j) == Color.Black)
                    count++;
            }
        }
        return -1;
    }
    //-----------------------------------------------------------------------
    // returns the number of red pieces remaining on the board.
    // (accomplish this by scanning the entire board while counting the red pieces.)
    public int countRed ( ) {
        // \todo v3
        int count = 0;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 7; j++){
                if (getColor(i, j) == Color.Black)
                    count++;
            }
        }
        return -1;
    }
    //-----------------------------------------------------------------------
    // returns true if red is a winner (regardless of whose turn it is).
    // false otherwise.
    // (accomplish this by checking if a red piece is in the black den.)
    public boolean isRedWinner ( ) {
        // \todo v3
        if (countBlack() == -1)
            return true;
        if (getColor(8, 3) == Color.Red)
            return true;
        return false;
    }
    //-----------------------------------------------------------------------
    // returns true if black is a winner (regardless of whose turn it is).
    // false otherwise.
    // (accomplish this by checking if a black piece is in the red den.)
    public boolean isBlackWinner ( ) {
        // \todo v3
        if (countRed() == -1)
            return true;
        if (getColor(0, 3) == Color.Red)
            return true;
        return false;
    }
    //-----------------------------------------------------------------------
    // copy ctor. make a separate, independent copy.
    public Board ( final Board other ) {
        // \todo v3
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 7; j++){
                mBoard[i][j] = other.mBoard[i][j];
            }
        }

    }
    //-----------------------------------------------------------------------
    // this is a "proper" equals method.
    // return true if the board _objects_ are equal; false otherwise.
    @Override
    public boolean equals ( Object other ) {
        // \todo v3
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        else {
            Board newBoard = (Board)other;
            return (rbNone == newBoard.rbNone && rRat == newBoard.rRat && rCat == newBoard.rCat && rDog == newBoard.rDog
            && rWolf == newBoard.rWolf && rLeopard == newBoard.rLeopard && rTiger == newBoard.rTiger && rLion == newBoard.rLion
            && rElephant == newBoard.rElephant && bRat == newBoard.bRat && bCat == newBoard.bCat && bDog == newBoard.bDog
            && bWolf == newBoard.bWolf && bLeopard == newBoard.bLeopard && bTiger == newBoard.bTiger && bLion == newBoard.bLion
            && bElephant == newBoard.bElephant && fPieceMask == newBoard.fPieceMask && cNone == newBoard.cNone && cWater == newBoard.cWater
            && cGround == newBoard.cGround && cRTrap == newBoard.cRTrap && cBTrap == newBoard.cBTrap && cRDen == newBoard.cRDen
            && cBDen == newBoard.cBDen && fBoardMask == newBoard.fBoardMask && fRows == newBoard.fRows && fCols == newBoard.fCols
            && mBoard == newBoard.mBoard && mBlacksTurn == newBoard.mBlacksTurn);
        }
    }
    //-----------------------------------------------------------------------
    // return true if the board _contents_ are equal; false otherwise.
    // do not consider mBlacksTurn for equality; only consider the board contents.
    // this is NOT a "proper" equals method but can be used by a "proper" one.
    public boolean equalsBoard ( Board other ) {
        // \todo v3
        if (other == null)
            return false;
        else{
            for (int i = 0; i < 9; i++){
                for (int j = 0; j < 7; j++){
                    if (mBoard[i][j] != other.mBoard[i][j])
                        return false;
                }
            }
        }
        return true;
    }
    //-----------------------------------------------------------------------
    //from "Effective Java" by J. Bloch:
    // "Item 9: Always override hashCode when you override equals"
    //
    //use the string algorithm from
    // https://en.wikipedia.org/wiki/Java_hashCode%28%29#The_java.lang.String_hash_function.
    // simply treat the individual board values (i.e., mBoard[r][c]) as the
    // individual character values.  (DO NOT use charAt on the string returned
    // from toString for this function.)  also include the value of mBlacksTurn
    // as described below as the last (i.e., nth) character.
    @Override
    public int hashCode ( ) {
        //include individual board values
        int hash = 0, i = 0;
        for (int r = 0; r < Board.fRows; r++) {
            for (int c = 0; c < Board.fCols; c++) {
                int power = 1;
                if (i > 0) {
                    for (int p = 1; p <= fRows * fCols - 1 - i; p++)
                        power *= 31;
                }
                hash += power * mBoard[r][c];
                i++;
            }
        }
        //include mBlacksTurn as the last character value by using a value of 1 if it
        // is true, and a value of 2 if it is false.
        return hash + ((mBlacksTurn) ? 1 : 2);

    }
    //-----------------------------------------------------------------------
    // returns a string representing the board that can be pretty-printed.
    // it should look something like the following:
    //
    //     --...-        --...-     \n
    //    |      |      |      |    \n
    //    .      .      .      .     .
    //    .      .      .      .     .
    //    .      .      .      .     .
    //    |      |      |      |    \n
    //     --...-        --...-     \n
    //
    // the left side of the string should be the underlying board.
    // the right side should be the pieces at their specific locations.
    // put the first 3 characters of the name at each location
    // (e.g., rLi for the red lion, and bEl for the black elephant).
    //
    // if you have a better idea, please let me know!
    public String toString ( ) {
        // \todo v1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fRows; i++){
            for (int j = 0; j < fCols; j++){
                sb.append(getBoardType(i,j));
                sb.append(" ");
                sb.append(getPieceType(i,j));
                if (j == 6){
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
    private String getBoardType(int r, int c){
        switch (getBoard(r, c)) {
            case cWater:
                return "Water";
            case cGround:
                return "Ground";
            case cBDen:
                return "BDen";
            case cRDen:
                return "RDen";
            case cRTrap:
                return "RTrap";
            case cBTrap:
                return "BTrap";
            default:
                return "None";
        }
    }
    private String getPieceType(int r, int c){
        switch (getPiece(r, c)) {
            case rCat:
                return "rCa";
            case rDog:
                return "rDo";
            case rElephant:
                return "rEl";
            case rLeopard:
                return "rLe";
            case rLion:
                return "rLi";
            case rRat:
                return "rRa";
            case rTiger:
                return "rTi";
            case rWolf:
                return "rWo";
            case bCat:
                return "bCa";
            case bDog:
                return "bDo";
            case bElephant:
                return "bEl";
            case bLeopard:
                return "bLe";
            case bLion:
                return "bLi";
            case bRat:
                return "bRa";
            case bTiger:
                return "bTi";
            case bWolf:
                return "bWo";
            default:
                return "";
        }
    }

}  //end class Board