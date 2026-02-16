package jhn.handlers;

import java.time.LocalDate;

import jhn.API.Weather;

public class BackgroundHandler {

    String backgroundPath;

    public BackgroundHandler(Weather weather) {

        int averagetemp = weather.getAverageTempVal(LocalDate.now());
        System.out.println("Average Temp: " + averagetemp);
        if(averagetemp >= 10 ){
            backgroundPath = "weatherapp1\\src\\main\\java\\jhn\\resources\\summerBackground.gif";
        }
        else if(averagetemp >= 0){
            backgroundPath = "weatherapp1\\src\\main\\java\\jhn\\resources\\springBackground.gif";
        }
        else{
            backgroundPath = "weatherapp1\\src\\main\\java\\jhn\\resources\\winterBackground.gif";
        }
    }


    public String getBackgroundPath(){
        return backgroundPath;
    }

    

}
