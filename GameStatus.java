package game1024;

/**
 * Created by Hans Dulimarta on Feb 08, 2016.
 * Used by Ryan Beam on 2/16/2017.
 */
public enum GameStatus {
    IN_PROGRESS, /* game is still in progress */
    USER_WON,    /* the player is able to add the numbers to the goal value */
    USER_LOST    /* no more move possible and no tiles with the goal value on the board */
}
