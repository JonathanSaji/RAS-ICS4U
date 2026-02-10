package jhn;
public class WeatherApp {
    public static Menu menu;
    public static JsonHandler json;
    public static void main(String[] args) throws Exception {
      
        menu = new Menu(new Weather(getLat(), getLong()));
        menu.setVisible(true);
        json = new JsonHandler("weatherapp1\\src\\main\\java\\jhn\\settings.json");
        

    }

    public WeatherApp(){}

    /*
    Global Helper Variables  
    */


    public static int getMiddleX(int sizeDiff){ return (1920 - sizeDiff) / 2; }
    public static int getMiddleY(int sizeDiff){ return (1080 - sizeDiff) / 2; }
    public static double getLat(){return 45.3042;}
    public static double getLong(){return -75.9341;}
    public static Menu getMenu(){return menu;}
    public static JsonHandler getJsonHandler(){return json;}











}


