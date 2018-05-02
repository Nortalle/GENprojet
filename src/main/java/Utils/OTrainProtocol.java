/*
    Author : Adrien Allemand
 */

package Utils;

public class OTrainProtocol {

    /* ---------{ Client side commands }--------- */

    // get the list of stations
    static String GET_GARES = "GET_GARES";
    // get the player's ressources
    static String GET_RESSOURCES = "GET_RESSOURCES";


    /* Train related commands */
    // gets the train informations
    static String GET_TRAIN_STATUS = "GET_TRAIN_STATUS";
    // sends train to station
    static String GO_TO = "GO_TO";
    // start a mining session
    static String MINE = "MINE";
    // end mining session
    static String STOP_MINE = "STOP_MINE";

    /* Buy/Sell related commands*/
    // place an offer
    static String SET_OFFRE = "SET_OFFRE";
    // get the offer lists
    static String GET_OFFRES = "GET_OFFRES";
    // acquire an offer
    static String BUY_OFFRE = "BUY_OFFRE";

    /* Production related commands */
    // get the list of production
    static String GET_PROD_QUEUE = "GET_PROD_QUEUE";
    // adds the object to the prod queue
    static String CRAFT = "CRAFT";
    // removes the craft from prod queue
    static String CANCEL_CRAFT = "CANCEL_CRAFT";

    /* Connection related commands */
    // connects to the server
    static String CONNECT = "CONNECT";


    /* ---------{ Server side commands }--------- */

    /* General commands */
    // confirm
    static String SUCCESS = "SUCCESS";
    // failure
    static String FAILURE = "FAILURE";

    /* Train related commands*/
    // the train possible status
    static String AT_STATION = "AT_STATION";
    static String ON_THE_WAY = "ON_THE_WAY";
    static String ARRIVED = "ARRIVED";

}
