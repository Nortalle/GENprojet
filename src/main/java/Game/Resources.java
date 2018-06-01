package Game;


import Utils.JsonUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Class which represents all the resources of a player
 *
 * @author Vincent Guidoux
 */
public class Resources {
    // use enum instead
    private static final int SCRUM_ID = 0;
    private static final int EAU_ID = 1;
    private static final int BOIS_ID = 2;
    private static final int CHARBON_ID = 3;
    private static final int PETROL_ID = 4;
    private static final int FER_ID = 5;
    private static final int CUIVRE_ID = 6;
    private static final int OR_ID = 7;

    private int scrum;//unité monétaire du jeu
    private int eau;
    private int bois;
    private int chardon;
    private int petrol;
    private int fer;
    private int cuivre;
    private int or;

    /**
     * constructor which init all the resources with the given value
     *
     * @param value : value to initialize all the resources
     */
    public Resources(int value){
        setAll(value);
    }

    /**
     * constructor which init all the resources with the given tab
     *
     * @param resources tab of resources (Scrum, Eau, Bois, Charbon, Petrol, Fer, Cuivre, Acier, Or); -1 means unknown
     */
    public Resources(int [] resources){
        scrum = resources[0];
        eau = resources[1];
        bois = resources[2];
        chardon = resources[3];
        petrol = resources[4];
        fer = resources[5];
        cuivre = resources[6];
        or = resources[7];
        /*
        scrum_gain_rate = resources[8];
        eau_gain_rate = resources[9];
        bois_gain_rate = resources[10];
        chardon_gain_rate = resources[11];
        petrol_gain_rate = resources[12];
        fer_gain_rate = resources[13];
        cuivre_gain_rate = resources[14];
        or_gain_rate = resources[15];
        */
    }

    public Resources(JsonObject json){
        fromJson(json);
    }

    /**
     * constructor which init all the resources with the given json
     *
     * @param json : json of the resources
     */
    public Resources(String json){

        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    /**
     *
     * @return the resources Jsonified
     */
    public JsonObject toJson(){
        JsonObject resources = new JsonObject();
        resources.add("scrum", new JsonPrimitive(scrum));
        resources.add("eau", new JsonPrimitive(eau));
        resources.add("bois", new JsonPrimitive(bois));
        resources.add("chardon", new JsonPrimitive(chardon));
        resources.add("petrol", new JsonPrimitive(petrol));
        resources.add("fer", new JsonPrimitive(fer));
        resources.add("cuivre", new JsonPrimitive(cuivre));
        resources.add("or", new JsonPrimitive(or));

        return resources;
    }

    /**
     * Sets all the resources to zero
     */
    public void init(){
        setAll(0);
    }

    /**
     * Sets the object with the given Json
     *
     * @param from : Json to sets the object
     */
    public void fromJson(JsonObject from){
        scrum = from.get("scrum").getAsInt();
        eau = from.get("eau").getAsInt();
        bois = from.get("bois").getAsInt();
        chardon = from.get("chardon").getAsInt();
        petrol = from.get("petrol").getAsInt();
        fer = from.get("fer").getAsInt();
        cuivre = from.get("cuivre").getAsInt();
        or = from.get("or").getAsInt();
        /*
        scrum = from.get("scrum_gain_rate").getAsInt();
        eau = from.get("eau_gain_rate").getAsInt();
        bois = from.get("bois_gain_rate").getAsInt();
        chardon = from.get("chardon_gain_rate").getAsInt();
        petrol = from.get("petrol_gain_rate").getAsInt();
        fer = from.get("fer_gain_rate").getAsInt();
        cuivre = from.get("_gain_rate").getAsInt();
        or = from.get("or_gain_rate").getAsInt();
        */
    }


    public int getScrum() {
        return scrum;
    }

    public void setScrum(int scrum) {
        this.scrum = scrum;
    }

    public int getEau() {
        return eau;
    }

    public void setEau(int eau) {
        this.eau = eau;
    }

    public int getBois() {
        return bois;
    }

    public void setBois(int bois) {
        this.bois = bois;
    }

    public int getCharbon() {
        return chardon;
    }

    public void setChardon(int chardon) {
        this.chardon = chardon;
    }

    public int getPetrol() {
        return petrol;
    }

    public void setPetrol(int petrol) {
        this.petrol = petrol;
    }

    public int getFer() {
        return fer;
    }

    public void setFer(int fer) {
        this.fer = fer;
    }

    public int getCuivre() {
        return cuivre;
    }

    public void setCuivre(int cuivre) {
        this.cuivre = cuivre;
    }

    public int getOr() {
        return or;
    }

    public void setOr(int or) {
        this.or = or;
    }

    /*
    public int getScrumRate() {
        return scrum_gain_rate;
    }

    public void setScrumRate(int scrum_gain_rate) {
        this.scrum_gain_rate = scrum_gain_rate;
    }

    public int getEauRate() {
        return eau_gain_rate;
    }

    public void setEauRate(int eau_gain_rate) {
        this.eau_gain_rate = eau_gain_rate;
    }

    public int getBoisRate() {
        return bois_gain_rate;
    }

    public void setBoisRate(int bois_gain_rate) {
        this.bois_gain_rate = bois_gain_rate;
    }

    public int getCharbonRate() {
        return chardon_gain_rate;
    }

    public void setChardonRate(int charbon_gain_rate) {
        this.chardon_gain_rate = charbon_gain_rate;
    }

    public int getPetrolRate() {
        return petrol_gain_rate;
    }

    public void setPetrolRate(int petrol_gain_rate) {
        this.petrol_gain_rate = petrol_gain_rate;
    }

    public int getFerRate() {
        return fer_gain_rate;
    }

    public void setFerRate(int fer_gain_rate) {
        this.fer_gain_rate = fer_gain_rate;
    }

    public int getCuivreRate() {
        return cuivre_gain_rate;
    }

    public void setCuivreRate(int cuivre_gain_rate) {
        this.cuivre_gain_rate = cuivre_gain_rate;
    }

    public int getOrRate() {
        return or_gain_rate;
    }

    public void setOrRate(int or_gain_rate) {
        this.or_gain_rate = or_gain_rate;
    }
    */
    /**
     * Sets all the resources to the given value
     * @param all : value to set all the ressources
     */
    public void setAll(int all){
        scrum = all;
        eau = all;
        bois = all;
        chardon = all;
        petrol = all;
        fer = all;
        cuivre = all;
        or = all;
    }

    public int[] toArray() {
        int array[] = {scrum, eau, bois, chardon, petrol, fer, cuivre, or};
        return array;
    }
}
