package gui_prog1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import org.jfree.data.time.*;

/**
 *
 * @author 7047629
 */
public class Day {
    public String [] date;
    public Minute [] time;
    public double [] temperature;
    public double [] humidity;
    public double [] barometer;
    public double [] windspeed;
    public String [] winddirection; 
    public double [] windgust;
    public double [] windchill;
    public double [] heatindex;
    public double [] uvindex;
    public double [] rainfall;
    public boolean [] valid;
    
    public MinMax mtemp = new MinMax();
    public MinMax mwindSpeed = new MinMax();
    public double avTemp = 0;
    public double avWindSpeed = 0;
    public double totalRain = 0;
    public boolean validDay = false;
    public int dayOfWeek = 0;
    
    public Map<String, Integer> preWind = new HashMap<String, Integer>();
     
   
    public Day()
    {
        date = new String[144];
        time  = new  Minute[144];
        temperature = new double[144];
        humidity = new double[144];
        barometer = new double[144];
        windspeed  = new double[144];
        winddirection  = new String[144]; 
        windgust = new double[144];
        windchill = new double[144];
        heatindex = new double[144];
        uvindex = new double[144];
        rainfall = new double[144];
        valid = new boolean[144];
        

        preWind.put(" N ", 0);
        preWind.put(" NE ", 0);
        preWind.put(" NW ", 0);
        preWind.put(" NNE ", 0);
        preWind.put(" NNW ", 0);
        
        preWind.put(" S ", 0);
        preWind.put(" SE ", 0);
        preWind.put(" SW ", 0);
        preWind.put(" SSE ", 0);
        preWind.put(" SSW ", 0);
        
        preWind.put(" E ", 0);
        preWind.put(" W ", 0);
        preWind.put(" ENE ", 0);
        preWind.put(" ESE ", 0);
        preWind.put(" WNW ", 0);
        preWind.put(" WSW ", 0);

    }

    
}
