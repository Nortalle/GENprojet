/*
    Author : Adrien Allemand
 */

public class OtrainProtocol {

    /* ---------{ Client side commands }--------- */

    static String CONNECT = "CONNECT";

    // get the list of stations
    static String GET_GARES = "GET_GARES";
    // get the player's ressources
    static String GET_RESSOURCES = "GET_RESSOURCES";


    /* Train related commands*/
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
