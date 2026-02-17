package jhn.ui.uiComponents;

import java.time.LocalDate;

import jhn.API.Weather;

public  class WeatherIcons {
    Weather weather;
    public WeatherIcons(Weather weather){
        this.weather = weather;
    }


    public String getIconForCloud(LocalDate date,int start,int stop){
        String filePath;
        int avg = weather.getAverageVal(date, weather.getCloudCover(), start, stop);
        if(avg > 60){
            filePath = "weatherapp1\\src\\main\\java\\jhn\\resources\\clouds\\cloud.png";
        }
        else if(avg < 60 && avg > 30){
            filePath = "weatherapp1\\src\\main\\java\\jhn\\resources\\clouds\\mid-cloudy.png";
        }
        else{
           filePath =  "weatherapp1\\src\\main\\java\\jhn\\resources\\clouds\\sunrise.png";
        }
        return filePath;
    }       









}
