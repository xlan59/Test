    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_prog1;


/**
 *
 * @author 7047629
 */
public class Records 
{
   
    public Years []year;
    public int leastYear;
    public int size;
    
    public Records(int lowYear, int highYear)
    {
        leastYear= lowYear;
        size = highYear - lowYear+1;
        year = new Years [size];
        System.out.print( "Least Year is: " + leastYear );
        System.out.println();
        System.out.print( "Year amount: " + size );
        System.out.println();
    }
    
    public int yearIndex(int selectYear)
    {
        return (selectYear - leastYear);
    }
    
    public Day[] getYear(int year)
    {
        int dayIndex = 0;
        Day [] rdays = new Day [12*31];
        //rdays[0] = this.year[0].months[0].days[0]; 
      
        for(int j = 0; j < 12; j++)
        {
            for(int k = 0; k < 31; k++)
            {
                rdays[dayIndex] = this.year[this.yearIndex(year)].months[j].days[k];
                dayIndex++;
            }
            //rdays[i] = years[];
        }
        
        return rdays;     
    }
    
    public  Day[] getMonth(int year, int month)
    {
        int dayIndex = 0;
        Day [] rdays = new Day [31];
     
        for(int k = 0; k < 31; k++)
        {
                rdays[dayIndex] = this.year[this.yearIndex(year)].months[month].days[k];
                dayIndex++;
        }
        return rdays;
    }
    
    
    
    
    public int getWeekDay(int year, int month, int day)
    {  
       return this.year[year].months[month].days[day].dayOfWeek;
    }
        
    public Day getDay(int year, int month, int day)
    {
        return this.year[year].months[month].days[day];
    }
    
    public double getYearAvTemp(int year)
    {
        return this.year[year].avTemp;
    }
    
    public double getMonthAvTemp(int year, int month)
    {
        return this.year[year].months[month].avTemp;
    }
    
    public double getWeekAvTemp(int year_i, int month_i, int day_i)
    {
        double avTemp = 0;
        
        avTemp += year[year_i].months[month_i].days[day_i].avTemp;
        for( int i = 0; i < 6; i++ )
        {
            // try to get the next day
            day_i++;
            // if day is past current month
            if( day_i == 31 || 
                    !year[year_i].months[month_i].days[day_i].validDay)
            {
                // if last month of year
                if(month_i == 11)
                {
                    // go to next year
                    // if end of record, return
                    if(year_i == size-1)
                    {
                        System.out.println("Record End");
                        // return false
                        return avTemp;
                    }
                    // else view next year
                    year_i++;
                    // reset lower indexes
                    month_i = 0;
                    day_i = 0;
                }
                // else go to next month
                else
                {
                    month_i++;
                    // reset lower indexes
                    day_i = 0;
                }
            }
            
            avTemp += year[year_i].months[month_i].days[day_i].avTemp;
        }
        return avTemp;
    }
    
    public double getDayAvTemp(int year, int month, int day)
    {
        return this.year[year].months[month].days[day].avTemp;
    }
    
    public double getYearAvWind(int year)
    {
        return this.year[year].avWindSpeed;
    }
    
    public double getMonthAvWind(int year, int month)
    {
        return this.year[year].months[month].avWindSpeed;
    }
    
    public double getWeekAvWind(int year_i, int month_i, int day_i)
    {
        double avWind = 0;
        
        avWind += year[year_i].months[month_i].days[day_i].avWindSpeed;
        for( int i = 0; i < 6; i++ )
        {
            // try to get the next day
            day_i++;
            // if day is past current month
            if( day_i == 31 || 
                    !year[year_i].months[month_i].days[day_i].validDay)
            {
                // if last month of year
                if(month_i == 11)
                {
                    // go to next year
                    // if end of record, return
                    if(year_i == size-1)
                    {
                        System.out.println("Record End");
                        // return false
                        return avWind/(i+1);
                    }
                    // else view next year
                    year_i++;
                    // reset lower indexes
                    month_i = 0;
                    day_i = 0;
                }
                // else go to next month
                else
                {
                    month_i++;
                    // reset lower indexes
                    day_i = 0;
                }
            }
            
            avWind += year[year_i].months[month_i].days[day_i].avWindSpeed;
        }
        return avWind/7;
    }
    
    public double getDayAvWind(int year, int month, int day)
    {
        return this.year[year].months[month].days[day].avWindSpeed;
    }
    
    public MinMax getYearMinMaxTemp(int year)
    {
        return this.year[year].mtemp;
    }
    
    public MinMax getMonthMinMaxTemp(int year, int month)
    {
        return this.year[year].months[month].mtemp;
    }
    
    public MinMax getWeekMinMaxTemp(int year_i, int month_i, int day_i)
    {
        MinMax mTemp = new MinMax();
        
        mTemp.setMinMax(year[year_i].months[month_i].days[day_i].mtemp.max,
                year[year_i].months[month_i].days[day_i].mtemp.maxDay);
        mTemp.setMinMax(year[year_i].months[month_i].days[day_i].mtemp.min,
                year[year_i].months[month_i].days[day_i].mtemp.minDay);
        for( int i = 0; i < 6; i++ )
        {
            // try to get the next day
            day_i++;
            // if day is past current month
            if( day_i == 31 || 
                    !year[year_i].months[month_i].days[day_i].validDay)
            {
                // if last month of year
                if(month_i == 11)
                {
                    // go to next year
                    // if end of record, return
                    if(year_i == size-1)
                    {
                        System.out.println("Record End");
                        // return false
                        return mTemp;
                    }
                    // else view next year
                    year_i++;
                    // reset lower indexes
                    month_i = 0;
                    day_i = 0;
                }
                // else go to next month
                else
                {
                    month_i++;
                    // reset lower indexes
                    day_i = 0;
                }
            }
            
            mTemp.setMinMax(year[year_i].months[month_i].days[day_i].mtemp.max,
                year[year_i].months[month_i].days[day_i].mtemp.maxDay);
            mTemp.setMinMax(year[year_i].months[month_i].days[day_i].mtemp.min,
                year[year_i].months[month_i].days[day_i].mtemp.minDay);    
        }
        return mTemp;
    }
    
    public MinMax getDayMinMaxTemp(int year, int month, int day)
    {
        return this.year[year].months[month].days[day].mtemp;
    }
    
    public MinMax getYearMinMaxWind(int year)
    {
        return this.year[year].mwindSpeed;
    }
    
    public MinMax getMonthMinMaxWind(int year, int month)
    {
        return this.year[year].months[month].mwindSpeed;
    }
    
    public MinMax getWeekMinMaxWind(int year_i, int month_i, int day_i)
    {
        MinMax mWind = new MinMax();
        
        mWind.setMinMax(year[year_i].months[month_i].days[day_i].mwindSpeed.max,
                year[year_i].months[month_i].days[day_i].mwindSpeed.maxDay);
        mWind.setMinMax(year[year_i].months[month_i].days[day_i].mwindSpeed.min,
                year[year_i].months[month_i].days[day_i].mwindSpeed.minDay);
        for( int i = 0; i < 6; i++ )
        {
            // try to get the next day
            day_i++;
            // if day is past current month
            if( day_i == 31 || 
                    !year[year_i].months[month_i].days[day_i].validDay)
            {
                // if last month of year
                if(month_i == 11)
                {
                    // go to next year
                    // if end of record, return
                    if(year_i == size-1)
                    {
                        System.out.println("Record End");
                        // return false
                        return mWind;
                    }
                    // else view next year
                    year_i++;
                    // reset lower indexes
                    month_i = 0;
                    day_i = 0;
                }
                // else go to next month
                else
                {
                    month_i++;
                    // reset lower indexes
                    day_i = 0;
                }
            }
            
            mWind.setMinMax(year[year_i].months[month_i].days[day_i].mwindSpeed.max,
                year[year_i].months[month_i].days[day_i].mwindSpeed.maxDay);
            mWind.setMinMax(year[year_i].months[month_i].days[day_i].mwindSpeed.min,
                year[year_i].months[month_i].days[day_i].mwindSpeed.minDay);    
        }
        return mWind;
    }
    
    public MinMax getDayMinMaxWind(int year, int month, int day)
    {
        return this.year[year].months[month].days[day].mwindSpeed;
    }
    
    public double getYearRainfall(int year)
    {
        return this.year[year].totalRain;
    }
    
    public double getMonthRainfall(int year, int month)
    {
        return this.year[year].months[month].totalRain;
    }
    
    public double getWeekRainfall(int year_i, int month_i, int day_i)
    {
        double Rainfall = 0;
        
        Rainfall += year[year_i].months[month_i].days[day_i].totalRain;
        for( int i = 0; i < 6; i++ )
        {
            // try to get the next day
            day_i++;
            // if day is past current month
            if( day_i == 31 || 
                    !year[year_i].months[month_i].days[day_i].validDay)
            {
                // if last month of year
                if(month_i == 11)
                {
                    // go to next year
                    // if end of record, return
                    if(year_i == size-1)
                    {
                        System.out.println("Record End");
                        // return false
                        return Rainfall;
                    }
                    // else view next year
                    year_i++;
                    // reset lower indexes
                    month_i = 0;
                    day_i = 0;
                }
                // else go to next month
                else
                {
                    month_i++;
                    // reset lower indexes
                    day_i = 0;
                }
            }
            
            Rainfall += year[year_i].months[month_i].days[day_i].totalRain;
        }
        return Rainfall;
    }
    
    public double getDayRainfall(int year, int month, int day)
    {
        return this.year[year].months[month].days[day].totalRain;
    }
    
    public String getYearPreWind(int year)
    {
        return this.year[year].bestWind;
    }
    
    public String getMonthPreWind(int year, int month)
    {
        return this.year[year].months[month].bestWind;
    }
    
    public String getWeekPreWind(int year_i, int month_i, int day_i)
    {
        String preWind = " N ";
        
        preWind += year[year_i].months[month_i].days[day_i].bestWind;
        for( int i = 0; i < 6; i++ )
        {
            // try to get the next day
            day_i++;
            // if day is past current month
            if( day_i == 31 || 
                    !year[year_i].months[month_i].days[day_i].validDay)
            {
                // if last month of year
                if(month_i == 11)
                {
                    // go to next year
                    // if end of record, return
                    if(year_i == size-1)
                    {
                        System.out.println("Record End");
                        // return false
                        return preWind;
                    }
                    // else view next year
                    year_i++;
                    // reset lower indexes
                    month_i = 0;
                    day_i = 0;
                }
                // else go to next month
                else
                {
                    month_i++;
                    // reset lower indexes
                    day_i = 0;
                }
            }
            
            preWind = year[year_i].months[month_i].days[day_i].bestWind;
        }
        return preWind;
    }
    
    public String getDayPreWind(int year, int month, int day)
    {
        return this.year[year].months[month].days[day].bestWind;
    }
      
}
