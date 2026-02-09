package jhn;
import java.util.Calendar;
import java.util.Date;
public class CurrentTime {

    int currentHour;

    public CurrentTime(){

        Calendar calender =  Calendar.getInstance();

        currentHour = calender.get(Calendar.HOUR_OF_DAY);
        Date date = calender.getTime();

        System.out.println(currentHour);
        System.out.println(date.toString());
    }       


    public int getHour(){
        return currentHour;
    }

}
