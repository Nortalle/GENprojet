/*
    Author : Adrien Allemand
 */

package Utils;

public class OTrainProtocol {

    public static final int PORT = 4444;

    /* ---------{ Client side commands }--------- */

    // get the list of stations
    public static final String GET_GARES = "GET_GARES";
    // get the player's resources
    public static final String GET_RESSOURCES = "GET_RESSOURCES";
    // get the player's objects
    public static final String GET_OBJECTS = "GET_OBJECTS";


    /* Train related commands */
    // gets the train information
    public static final String GET_TRAIN_STATUS = "GET_TRAIN_STATUS";
    // gets all trains at station
    public static final String GET_TRAINS_AT = "GET_TRAINS_AT";
    // sends train to station
    public static final String GO_TO = "GO_TO";
    // get mining wagon info
    public static final String MINE_INFO = "MINE_INFO";
    // start a mining session
    public static final String MINE = "MINE";
    // end mining session
    public static final String STOP_MINE = "STOP_MINE";

    /* Buy/Sell related commands*/
    // place an offer
    public static final String SET_OFFRE = "SET_OFFRE";
    // get the offer lists
    public static final String GET_OFFRES = "GET_OFFRES";
    // acquire an offer
    public static final String BUY_OFFRE = "BUY_OFFRE";

    /* Production related commands */
    // get the list of production
    public static final String GET_PROD_QUEUE = "GET_PROD_QUEUE";
    // adds the object to the prod queue
    public static final String CRAFT = "CRAFT";
    // removes the craft from prod queue
    public static final String CANCEL_CRAFT = "CANCEL_CRAFT";

    /* Upgrade related commands */
    // get the list of production
    public static final String GET_UPGRADE_QUEUE = "GET_UPGRADE_QUEUE";
    // adds the object to the prod queue
    public static final String UPGRADE = "UPGRADE";

    /* Connection related commands */
    // connects to the server
    public static final String CONNECT = "CONNECT";
    // create a new account
    public static final String SIGN_UP = "SIGN_UP";


    /* ---------{ Server side commands }--------- */

    /* General commands */
    // confirm
    public static final String SUCCESS = "SUCCESS";
    // failure
    public static final String FAILURE = "FAILURE";

    /* Train related commands*/
    // the train possible status
    public static final String AT_STATION = "AT_STATION";
    public static final String ON_THE_WAY = "ON_THE_WAY";
    public static final String ARRIVED = "ARRIVED";

}
