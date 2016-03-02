package gui_prog1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * ChartContainer
 * @author Murray LaHood-Burns
 * 
 * @description
 *   A JScrollPane that contains six charts (temp, humidity,
 * barometer, windspeed, UV index, and rainfall). The charts must be initialized
 * with the init( Record record ) function for the charts to be filled with data.
 * Also provides functionality for chart clicks, changing (yearly,monthly,weekly,
 * daily) views, and toggling between next and previous datasets.
 * 
 * @bugs
 * -Weeks do not properly step into the next month when using the next and
 * previous data set buttons
 * -The dataset for the year 2013 is not properly created
 * -February does not display all days of the month, because there is no data
 * for the 1st through the 23rd, and we have not handled that
 * -Chart clicks do not always find data to display, even if the click is on
 * a valid data value
 */
public final class ChartContainer extends JScrollPane implements ChartMouseListener {
    
    /**
     * ChartContainer
     * @author Murray LaHood-Burns
     * 
     * Initializes the chartpanels, and the panel that will contain them, as
     * well as the header for the scrollpane.
     */
    public ChartContainer()
    {
        
        // panel initialization
        panel = new JPanel();
        panel.setLayout( new BoxLayout(panel, BoxLayout.Y_AXIS) );
        
        // panel size
        panel.setPreferredSize( new Dimension(900,1800));
        
        // chartpanel initialization
        tempPanel = new ChartPanel(tempChart);
        humPanel = new ChartPanel(humChart);
        baroPanel = new ChartPanel(baroChart);
        windPanel = new ChartPanel(windChart);
        uvPanel = new ChartPanel(uvChart);
        rainPanel = new ChartPanel(rainChart);
        
        // add chartpanels to panel
        panel.add(tempPanel);
        panel.add(humPanel);
        panel.add(baroPanel);
        panel.add(windPanel);
        panel.add(uvPanel);
        panel.add(rainPanel);
        
        // scrollpane settings
        setViewportView(panel);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize( new Dimension(930,630));
    }
    
    /**
     * init
     * @author Murray LaHood-Burns
     * 
     * Initializes record for class, indexes, and loads new charts into the 
     * chart panels.
     * 
     * @param input_record contains all data for the ChartContainer
     */
    public void init( Records input_record )
    {
    
        // record initialization
        record = input_record;
        
        // index initialization
        year_i = 0;
        month_i = 0;
        day_i = 0;
        
        // view initialization
        currView = ViewEnum.YEAR;
        
        // chart initialization
        tempChart = createChart( createYearDataset("Temp", ChartEnum.TEMP),
                "Temperature", "Degrees Fahrenheit");
        humChart = createChart( createYearDataset("Hum", ChartEnum.HUM),
                "Humidity", "Percent");
        baroChart = createChart( createYearDataset("Baro", ChartEnum.BARO),
                "Barometric Pressure", "Inches of Mercury");
        windChart = createChart( createYearDataset("Wind", ChartEnum.WIND),
                "Wind Speed", "Miles Per Hour");
        uvChart = createChart( createYearDataset("UV", ChartEnum.UV),
                "UV Index", "UV Radiation Dose");
        rainChart = createChart( createYearDataset("Rain", ChartEnum.RAIN),
                "Rainfall", "Inches");
        
        setChartPanels();
        
        // add chartmouselisteners
        tempPanel.addChartMouseListener(this);
        humPanel.addChartMouseListener(this);
        baroPanel.addChartMouseListener(this);
        windPanel.addChartMouseListener(this);
        uvPanel.addChartMouseListener(this);
        rainPanel.addChartMouseListener(this);
        
        // header settings
        headerView = new JViewport();
        headerLabel = new JLabel(Integer.toString(record.leastYear+year_i), JLabel.CENTER);
        headerLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        headerView.setBackground(Color.WHITE);
        headerView.setView(headerLabel);
        setColumnHeader( headerView );
    }
    
    /**
     * getYear_i
     * @author Murray LaHood-Burns
     * 
     * get for year index
     * 
     * @return year_i
     */
    public int getYear_i(){
        return year_i;
    }
    
    /**
     * getMonth_i
     * @author Murray LaHood-Burns
     * 
     * get for month index
     * 
     * @return month_i
     */
    public int getMonth_i(){
        return month_i;
    }
    
    /**
     * getDay_i
     * @author Murray LaHood-Burns
     * 
     * get for day index
     * 
     * @return day_i
     */
    public int getDay_i(){
        return day_i;
    }
    
    /**
     * createChart
     * @author Murray LaHood-Burns
     * 
     * create JFreeChart with supplied dataset
     * 
     * @param dataset data to plot
     * @param title title for chart
     * @param unitLabel y axis label
     * @return created chart
     */
    private JFreeChart createChart( XYDataset dataset, String title, String unitLabel )
    {
        return ChartFactory.createTimeSeriesChart(
                title,
                "Date/Time",
                unitLabel,
                dataset,
                false,
                true,
                false);
    }
    
    /**
     * createYearDataset
     * @author Murray LaHood-Burns
     * 
     * Creates a timeseries dataset of all data in the record indexed by the
     * current value of year_i.
     * 
     * @param title title of dataset ( shows up in tooltips )
     * @param chartNum enum value indicating the chart is temp, hum, etc...
     * @return XYDataset for current year
     */
    private XYDataset createYearDataset( String title, ChartEnum chartNum )
    {
        TimeSeries series = new TimeSeries(title);
        boolean validDay;
        int month, day;
        Years year = record.year[year_i];
        
        // run through all months of year
        for(month = 0; month < 12; month++){
            // run through all days of each month
            validDay = true;
            for( day = 0; day < 31 && validDay == true; day++ ){
                if( year.months[month].days[day].validDay)
                {
                    // add valid days to the timeseries
                    addDayToSeries(series,year.months[month].days[day], chartNum);
                }
                else
                    validDay = false;
            }
        }
        
        return new TimeSeriesCollection(series);
    }
    
    /**
     * createMonthDataset
     * @author Murray LaHood-Burns
     * 
     * Create a timeseries dataset of all data in the record indexed by the
     * current value of month_i.
     * 
     * @param title title of dataset
     * @param chartNum indicates type of chart
     * @return XYDataset of month
     */
    private XYDataset createMonthDataset( String title, ChartEnum chartNum )
    {
        TimeSeries series = new TimeSeries(title);
        boolean validDay = true;
        int day;
        Month month = record.year[year_i].months[month_i];
        
        // run through all days in month
        for( day = 0; day < 31 && validDay == true; day++ ){
            if( month.days[day].validDay)
            {
                // add valid days to dataset
                addDayToSeries(series, month.days[day], chartNum);
            }
            else
                validDay = false;
        }
        
        return new TimeSeriesCollection(series);
    }
    
    /**
     * createWeekDataset
     * @author Murray LaHood-Burns
     * 
     * Create timeseries data set of 7 values from record starting with the
     * day indexed by day_i;
     * 
     * @param title title of timeseries
     * @param chartNum indicates chart type
     * @return week dataset
     */
    private XYDataset createWeekDataset( String title, ChartEnum chartNum )
    {
        TimeSeries series = new TimeSeries(title);
        boolean inNextMonth = false;
        int day_offset, i;
        Month currMonth = record.year[year_i].months[month_i];
        Month nextMonth = null;
        Day day = currMonth.days[day_i];
        
        // check if next month is within the record
        if(year_i==record.size-1 && month_i == 11)
        {
            // if not, ignore it
        }
        // if it is, set its value
        else
        {
            // if current month is december, set next to jan of next year
            if( month_i == 11 )
                nextMonth = record.year[year_i+1].months[0];
            // else set it to the next month of current year
            else
                nextMonth = record.year[year_i].months[month_i+1];
        }
        
        day_offset = 0;
        for( i = 0; i < 7; i++ )
        {
            // check if we went over the current month
            if( (day_i+day_offset == 31 || 
                    !day.validDay) && inNextMonth == false)
            {
                System.out.println("Week extending to next month");
                // reset day offest
                day_offset = 0;
                // go to next month if valid
                if(nextMonth!=null){
                    inNextMonth = true;
                }
                // else return the current dataset
                else
                    return new TimeSeriesCollection(series);
            }
            
            // get next day of week
            if(inNextMonth){
                if( nextMonth!=null)
                    day = nextMonth.days[day_offset];
            }
            else
                day = currMonth.days[day_i+day_offset];
            
            addDayToSeries(series,day,chartNum);
            
            // increment day
            day_offset++;
        }
                
        return new TimeSeriesCollection(series);
        
    }
    
    /**
     * createDayDataset
     * @author Murray LaHood-Burns
     * 
     * create timeseries data set from record of the day indexed by day_i
     * 
     * @param title title of dataset
     * @param chartNum type of data
     * @return day dataset
     */
    private XYDataset createDayDataset( String title, ChartEnum chartNum )
    {        
        TimeSeries series = new TimeSeries(title);
        Day day = record.year[year_i].months[month_i].days[day_i];
        
        addDayToSeries(series, day, chartNum);
        
        return new TimeSeriesCollection(series);
    }
    
    /**
     * addDayToSeries
     * @author Murray LaHood-Burns
     * 
     * adds all value times of day to series, as well as the data indicated
     * by chartNum
     * 
     * @param series dataset
     * @param day day containing times and data
     * @param chartNum indicates data type
     */
    private void addDayToSeries( TimeSeries series, Day day, ChartEnum chartNum)
    {
        int time;
        Minute currMinute;
        boolean validTime = true;
        
        // add all valid times to series
        for( time = 0; time < 144 && validTime; time++ ){
            if( day.valid[time] )
            {
                currMinute = day.time[time];
                
                // add data indicated by chartNum to series
                if( null != chartNum)
                switch (chartNum) {
                    case TEMP:
                        series.add( currMinute,
                                day.temperature[time] );
                        break;
                    case HUM:
                        series.add( currMinute,
                                day.humidity[time]);
                        break;
                    case BARO:
                        series.add( currMinute,
                                day.barometer[time]);
                        break;
                    case WIND:
                        series.add( currMinute,
                                day.windspeed[time]);
                        break;
                    case UV:
                        series.add( currMinute,
                                day.uvindex[time]);
                        break;
                    case RAIN:
                        series.add( currMinute,
                                day.rainfall[time]);
                        break;
                    default:
                        System.out.println("Invalid ChartNum(enum)");
                        break;
                }
            }
            else
                validTime = false;
        }
    }
    
    /**
     * viewYear
     * @author Murray LaHood-Burns
     * 
     * Calls createDataset for each type of graph and sets the chartPanels
     * to the created datasets
     */
    public void viewYear()
    {
        // update current view
        currView = ViewEnum.YEAR;
        
        // create charts
        tempChart = createChart( createYearDataset("Temp", ChartEnum.TEMP),
                "Temperature", "Degrees Fahrenheit");
        humChart = createChart( createYearDataset("Hum", ChartEnum.HUM),
                "Humidity", "Percent");
        baroChart = createChart( createYearDataset("Baro", ChartEnum.BARO),
                "Barometric Pressure", "Inches of Mercury");
        windChart = createChart( createYearDataset("Wind", ChartEnum.WIND),
                "Wind Speed", "Miles Per Hour");
        uvChart = createChart( createYearDataset("UV", ChartEnum.UV),
                "UV Index", "UV Radiation Dose");
        rainChart = createChart( createYearDataset("Rain", ChartEnum.RAIN),
                "Rainfall", "Inches");
        
        // set chart panels to created charts
        setChartPanels();
        
        // update header
        headerLabel.setText(Integer.toString(record.leastYear + year_i));
    }
    
    /**
     * viewMonth
     * @author Murray LaHood-Burns
     * 
     * Calls createDataset for each type of graph and sets the chartPanels
     * to the created datasets
     */
    public void viewMonth()
    {
        // update current view
        currView = ViewEnum.MONTH;
        
        // create charts
        tempChart = createChart( createMonthDataset("Temp", ChartEnum.TEMP),
                "Temperature", "Degrees Fahrenheit");
        humChart = createChart( createMonthDataset("Hum", ChartEnum.HUM),
                "Humidity", "Percent");
        baroChart = createChart( createMonthDataset("Baro", ChartEnum.BARO),
                "Barometric Pressure", "Inches of Mercury");
        windChart = createChart( createMonthDataset("Wind", ChartEnum.WIND),
                "Wind Speed", "Miles Per Hour");
        uvChart = createChart( createMonthDataset("UV", ChartEnum.UV),
                "UV Index", "UV Radiation Dose");
        rainChart = createChart( createMonthDataset("Rain", ChartEnum.RAIN),
                "Rainfall", "Inches");
        
        // put charts in panels
        setChartPanels();
        
        // update header
        headerLabel.setText( monthName[month_i] + " " +
                Integer.toString(record.leastYear + year_i));
    }
    
    /**
     * viewWeek
     * @author Murray LaHood-Burns
     * 
     * Calls createDataset for each type of graph and sets the chartPanels
     * to the created datasets
     */
    public void viewWeek()
    {
        // update view
        currView = ViewEnum.WEEK;
        
        // make dem charts
        tempChart = createChart( createWeekDataset("Temp", ChartEnum.TEMP),
                "Temperature", "Degrees Fahrenheit");
        humChart = createChart( createWeekDataset("Hum", ChartEnum.HUM),
                "Humidity", "Percent");
        baroChart = createChart( createWeekDataset("Baro", ChartEnum.BARO),
                "Barometric Pressure", "Inches of Mercury");
        windChart = createChart( createWeekDataset("Wind", ChartEnum.WIND),
                "Wind Speed", "Miles Per Hour");
        uvChart = createChart( createWeekDataset("UV", ChartEnum.UV),
                "UV Index", "UV Radiation Dose");
        rainChart = createChart( createWeekDataset("Rain", ChartEnum.RAIN),
                "Rainfall", "Inches");
        
        // put dem charts in dem panels
        setChartPanels();
        
        // update header
        String weekStart = monthName[month_i] + " " +
                Integer.toString(day_i+1) + ", " +
                Integer.toString(record.leastYear + year_i);
        
        headerLabel.setText( "Week starting on " + weekStart);
    }
    
    /**
     * viewDay
     * @author Murray LaHood-Burns
     * 
     * Calls createDataset for each type of graph and sets the chartPanels
     * to the created datasets
     */
    public void viewDay()
    {
        // update view
        currView = ViewEnum.DAY;
        
        // create charts
        tempChart = createChart( createDayDataset("Temp", ChartEnum.TEMP),
                "Temperature", "Degrees Fahrenheit");
        humChart = createChart( createDayDataset("Hum", ChartEnum.HUM),
                "Humidity", "Percent");
        baroChart = createChart( createDayDataset("Baro", ChartEnum.BARO),
                "Barometric Pressure", "Inches of Mercury");
        windChart = createChart( createDayDataset("Wind", ChartEnum.WIND),
                "Wind Speed", "Miles Per Hour");
        uvChart = createChart( createDayDataset("UV", ChartEnum.UV),
                "UV Index", "UV Radiation Dose");
        rainChart = createChart( createDayDataset("Rain", ChartEnum.RAIN),
                "Rainfall", "Inches");
        
        // charts in panels
        setChartPanels();
        
        // update header
        headerLabel.setText( 
                record.year[year_i].months[month_i].days[day_i].time[0].getDay().toString()
        );
    }
    
    /**
     * setChartPanels
     * @author Murray LaHood-Burns
     * 
     * add charts to their respective panels
     */
    private void setChartPanels()
    {
        tempPanel.setChart(tempChart);
        humPanel.setChart(humChart);
        baroPanel.setChart(baroChart);
        windPanel.setChart(windChart);
        uvPanel.setChart(uvChart);
        rainPanel.setChart(rainChart);
    }
    
    /**
     * nextDataset
     * @author Murray LaHood-Burns
     * 
     * Depending on the current view, creates a new dataset of the next view
     * and sets the view to that dataset.
     */
    public void nextDataset()
    {
        if(null != currView)
        switch (currView) {
            case YEAR:
                // attempt to go to the next year
                if(!incrementYear_i())
                    return;
                // make a new dataset of next year and set view
                viewYear();
                break;
            case MONTH:
                // attempt to go to the next month
                if(!incrementMonth_i())
                    return;
                // make a new dataset of next month and set view
                viewMonth();
                break;
            case WEEK:
                // go forward a day
                if(!incrementDay_i())
                    return;
                // attempt to increment day_i by 7
                for( int i = 0; i < 7; i++ ){
                    if(!incrementDay_i())
                        return;
                }
                // make a new dataset of next week and set view
                viewWeek();
                break;
            case DAY:
                // attempt to go forward a day
                if(!incrementDay_i())
                    return;
                // make a new dataset of next day and set view
                viewDay();
                break;
            default:
                System.out.println("currView corrupted");
                break;
        }
    }
    
    /**
     * prevDataset
     * @author Murray LaHood-Burns
     * 
     * Depending on the current view, creates a new dataset of the previous view
     * and sets the view to that dataset.
     */
    public void prevDataset()
    {
        if(null != currView)
        switch (currView) {
            case YEAR:
                // attempt to go back a year
                if(!decrementYear_i())
                    return;
                // make a new data set of previous year and set view
                viewYear();
                break;
            case MONTH:
                // attempt to go back a month
                if(!decrementMonth_i())
                    return;
                // make a new dataset of previous month and set view
                viewMonth();
                break;
            case WEEK:
                // go back a day
                if(!decrementDay_i())
                    return;
                // attempt to decrement day 7 times
                for( int i = 0; i < 7; i++){
                    if(!decrementDay_i())
                        return;
                }
                // make a new dataset of previous week and set view
                viewWeek();
                break;
            case DAY:
                // attempt to go back a day
                if(!decrementDay_i())
                    return;
                // make a new dataset of previous day and set view
                viewDay();
                break;
            default:
                System.out.println("currView corrupted");
                break;
        }
    }
    
    /**
     * incrementYear_i
     * @author Murray LaHood-Burns
     * 
     * checks if the next year is in the record, goes to next year so
     * 
     * @return false if end of record
     */
    private boolean incrementYear_i(){
        // if end of record, return
        if(year_i == record.size-1)
        {
            System.out.println("Record End");
            return false;
        }
        // else view next year
        year_i++;
        // reset lower indexes
        month_i = 0;
        day_i = 0;
        return true;
    }
    
    /**
     * incrementMonth_i
     * @author Murray LaHood-Burns
     * 
     * checks if next month is in record, increments month if it is
     * 
     * @return false if end of record
     */
    private boolean incrementMonth_i(){
        // if last month of year
        if(month_i == 11)
        {
            // go to next year
            if(!incrementYear_i())
                return false;
        }
        // else go to next month
        else
        {
            month_i++;
            // reset lower indexes
            day_i = 0;
        }
        return true;
    }
    
    /**
     * incrementDay_i
     * @author Murray LaHood-Burns
     * 
     * check if next day is in record, increments day if it is
     * 
     * @return false if end of record
     */
    private boolean incrementDay_i(){
        // increment day index
        day_i++;
        // if day is past current month
        if( day_i == 31 || 
                !record.year[year_i].months[month_i].days[day_i].validDay)
        {
            
            if(!incrementMonth_i())
                return false;
        }
        return true;
    }
    
    /**
     * decrementYear_i
     * @author Murray LaHood-Burns
     * 
     * check if previous year is in record, decrements year_i if it is
     * 
     * @return false if end of record
     */
    private boolean decrementYear_i(){
        // if beginning of record, return
        if(year_i == 0)
        {
            System.out.println("Record Start");
            return false;
        }
        // else view start of prev year
        year_i--;
        month_i = 0;
        day_i = 0;
        return true;
    }
    
    /**
     * decrementMonth_i
     * @author Murray LaHood-Burns
     * 
     * check if previous month is in record, decrements month_i if it is
     * 
     * @return false if end of record
     */
    private boolean decrementMonth_i(){
        // if first month of year
        if(month_i == 0)
        {
            // attempt to go to previous year
            if(!decrementYear_i())
                return false;
        }
        // else go to prev month
        else
        {
            month_i--;
            // reset lower indexes
            day_i = 0;
        }
        return true;
    }
    
    /**
     * decrementDay_i
     * @author Murray LaHood-Burns
     * 
     * check if previous day is in record, decrements day_i if it is
     * 
     * @return false if end of record
     */
    private boolean decrementDay_i(){
        // if first day of month
        if(day_i == 0)
        {
           if(!decrementMonth_i())
               return false;

            // go to last valid day of month
            day_i = 30;
            while(!record.year[year_i].months[month_i].days[day_i].validDay)
                day_i--;
        }
        // else go to previous day
        day_i--;
        return true;
    }
    
    /**
     * selectedDataset
     * @author Murray LaHood-Burns
     * 
     * sets record indexes to selected year month and day
     * 
     * @param year
     * @param month
     * @param day
     * @return true if day exists
     */
    public boolean selectedDataset( int year, int month, int day )
    {
        // set input to array indexes
        year = year - record.leastYear;
        month--;
        day--;
        
        if(!record.year[year].months[month].days[day].validDay)
            return false;
        
        // if it is a valid day, change internal indexes
        year_i = year;
        month_i = month;
        day_i = day;
        
        return true;
    }
    
    /**
     * getTimeIndex
     * @author Murray LaHood-Burns
     * 
     * binary search of time values in day
     * 
     * @param day
     * @param key
     * @param imin
     * @param imax
     * @return 
     */
    private int getTimeIndex(Day day, Minute key, int imin, int imax)
    {
        // binary search algorithm
        if(imax<imin)
            return-1;
        else
        {
            int imid = (imin+imax)>>> 1;
            Minute currMinute = day.time[imid];
            
            if(currMinute == null || currMinute.compareTo(key) > 0)
                return getTimeIndex(day,key,imin,imid-1);
            else if(currMinute.compareTo(key) < 0)
                return getTimeIndex(day,key,imid+1,imax);
            else
                return imid;
        }
    }
    
    /**
     * chartMouseClicked
     * @author Murray LaHood-Burns
     * 
     * displays message dialog with data associated with time clicked on graph
     * 
     * @param cme 
     */
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {
        
        // get time values from tooltip
        String tooltip = cme.getEntity().getToolTipText();
        String date = tooltip.substring(tooltip.indexOf("(")+1, tooltip.indexOf("M")+1);
        int month = Integer.parseInt(date.substring(0, date.indexOf("/")));
        int day = Integer.parseInt(date.substring(date.indexOf("/")+1, date.lastIndexOf("/")));
        int year = Integer.parseInt(date.substring(date.lastIndexOf("/")+1, date.indexOf(" ")));
        int hour = Integer.parseInt(date.substring(date.indexOf(" ")+1,date.indexOf(":")));
        int min = Integer.parseInt(date.substring(date.indexOf(":")+1,date.lastIndexOf(" ")));
        
        char meridian = date.charAt(date.indexOf("M")-1);
        
        if( meridian == 'P' )
            hour+=12;
        
        // set minute equal to time values
        Minute minute = new Minute(min,hour,day,month,year+2000);
        
        // get indexes from time values
        year = year - (record.leastYear - 2000);
        day--;
        month--;
        
        // day to search minute for
        Day currDay = record.year[year].months[month].days[day];
        
        // search for minute in day
        int index = getTimeIndex( currDay, minute, 0, 143 );
        
        // return if we didn't find the time
        if(index == -1)
            return;
        
        // create message dialog with data associated with time
        JOptionPane.showMessageDialog(null, date + "\n" +
                "Temperature (F): " + Double.toString(currDay.temperature[index]) + "\n" +
                "Humidity (%): " + Double.toString(currDay.humidity[index]) + "\n" +
                "Barometric Pressure (in.): " + Double.toString(currDay.barometer[index]) + "\n" +
                "Wind Speed (mph): " + Double.toString(currDay.windspeed[index]) + "\n" +
                "Wind Direction: " + currDay.winddirection[index]+ "\n" +
                "Wind Gust (mph): " + Double.toString(currDay.windgust[index]) + "\n" +
                "Wind Chill (F): " + Double.toString(currDay.windchill[index]) + "\n" +
                "Heat Index: " + Double.toString(currDay.heatindex[index]) + "\n" +
                "UV Index " + Double.toString(currDay.uvindex[index]) + "\n" +
                "Rainfall: " + Double.toString(currDay.rainfall[index]) + "\n");
        
    }

    /**
     * chartMouseMoves
     * @author Murray LaHood-Burns
     * 
     * interface implementation
     * 
     * @param cme 
     */
    @Override
    public void chartMouseMoved(ChartMouseEvent cme) {
    }
   
    // Variable declarations
    // panel that chartPanels are stored in
    private final JPanel panel;
    
    // record holding data
    private Records record;

    // indicates data for current chat
    private enum ChartEnum{
        TEMP, HUM, BARO, WIND, UV, RAIN
    }
    
    // indicates current chart view
    private enum ViewEnum{
        YEAR, MONTH, WEEK, DAY
    }
    
    private ViewEnum currView;
    
    // indexes for accessing data from record
    private int year_i;
    private int month_i;
    private int day_i;
    
    // charts
    private JFreeChart tempChart;
    private JFreeChart humChart;
    private JFreeChart baroChart;
    private JFreeChart windChart;
    private JFreeChart uvChart;
    private JFreeChart rainChart;
    
    // chart panels
    private final ChartPanel tempPanel;
    private final ChartPanel humPanel;
    private final ChartPanel baroPanel;
    private final ChartPanel windPanel;
    private final ChartPanel uvPanel;
    private final ChartPanel rainPanel;
    
    // scrollpane header variables
    private JViewport headerView;
    private JLabel headerLabel;
    
    // month names
    private final String[] monthName = {
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December" };
}