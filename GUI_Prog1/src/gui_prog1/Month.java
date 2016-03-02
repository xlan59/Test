/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_prog1;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 7047629
 */
public class Month {
    
    public Day [] days;
    
    public MinMax mtemp = new MinMax();
    public MinMax mwindSpeed = new MinMax();
    public double avTemp = 0;
    public double avWindSpeed = 0;
    public double totalRain = 0;
    public Map<String, Integer> preWind = new HashMap<String, Integer>();
    
    public Month()
    {
        days = new Day [31];
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
        preWind.put(" ENE ", 0);
        preWind.put(" ESE ", 0);
        preWind.put(" WNW ", 0);
        preWind.put(" WSW ", 0);
        
    }
}
