package jhn.location;
import java.util.Calendar;
public class CurrentTime {

    int currentHour;

    public CurrentTime(){

        Calendar calender =  Calendar.getInstance();

        currentHour = calender.get(Calendar.HOUR_OF_DAY);
    }       


    public String getHour(){
        if(currentHour <= 11 && currentHour != 0){
            return currentHour + " AM";
        }
        else if(currentHour == 0){
            return "12 AM";
        }
        else if(currentHour == 12){
            return "12 PM";
        }
        else{
            return currentHour - 12 + " PM";
        }
    }



    public static void main(String[] args) {
        new CurrentTime();
    }
}
