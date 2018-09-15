package sample;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;


public class SerialClass implements SerialPortEventListener
{
    SerialPort serialPort;
    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {"/dev/tty.usbmodem4869681"};
    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;
    private DataListener dataListener;

    public SerialClass()
    {
        initialize();
    }

    public void initialize()
    {
        // the next line is for Raspberry Pi and
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while( portEnum.hasMoreElements() )
        {
            CommPortIdentifier currPortId = ( CommPortIdentifier ) portEnum.nextElement();
            for( String portName : PORT_NAMES )
            {
                if( currPortId.getName().equals(portName) )
                {
                    portId = currPortId;
                    break;
                }
            }
        }
        if( portId == null )
        {
            System.out.println("Could not find COM port.");
            return;
        }

        try
        {
            // open serial port, and use class name for the appName.
            serialPort = ( SerialPort ) portId.open(this.getClass().getName(),
                                                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                                           SerialPort.DATABITS_8,
                                           SerialPort.STOPBITS_1,
                                           SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch( Exception e )
        {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close()
    {
        if( serialPort != null )
        {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent)
    {
        if( oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE )
        {
            try
            {
                String inputLine = input.readLine();
//                inputLine = inputLine.replaceFirst(",", "");
                double[] doubleValues = Arrays.stream(inputLine.split(","))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                if(doubleValues.length == 20)
                {
                    dataListener.onDataAvailable(doubleValues);
                }

//                System.out.println(inputLine);
            }
            catch( Exception e )
            {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
//        mockData();
    }

    private void mockData()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    try
                    {
                        double [][]values = {  {-33.34, -99.92, 181.28, 22.30, -125.75, 0.69, 73.00, 143.00, -233.00, 48.82, -103.78, -111.05, 20.50, 8.30, 59.43, -31.00, 333.00, -191.00, 108.87, -146.83, 68.48, 21.35, 16.35, 2.39, -147.00, 179.00, -197.00, 56.56, -143.00, 91.44, 31.01, 22.58, 38.91, -6.00, 60.00, -206.00, 80.71, -5.17, 15.20, 13.28, 20.10, 11.55, -17.00, 425.00, -203.00, 41.33, 54.70, 2.15, 12.88, -20.44, -7.89, 190.00, 342.00, -78.00},
                                {-74.63, -73.43, 113.10, 30.33, -96.43, -4.24, 72.00, 26.00, 212.00, -20.51, -106.22, -181.89, 5.33, -10.83, 44.21, 158.00, 138.00, -40.00, 38.90, -130.11, -22.25, 2.84, -0.82, 0.56, -25.00, -73.00, -41.00, -17.63, -122.64, -4.00, 14.25, 4.33, 30.09, 148.00, -274.00, -44.00, 19.32, 59.65, -87.09, -2.30, 0.05, 4.58, 259.00, 41.00, 101.00, -31.30, 13.27, 116.72, 0.80, 1.09, 0.20, 207.00, 135.00, -297.00},
                                {-41.28, -34.22, 140.06, 30.08, -99.67, -3.58, 160.00, -104.00, -32.00, 17.05, -10.11, -63.81, 3.78, -11.06, 44.59, 363.00, 249.00, -271.00, 105.46, -80.83, 12.76, 1.60, 0.37, 0.35, 143.00, 185.00, 55.00, 62.71, -108.67, 68.54, 13.13, 3.28, 30.99, 196.00, 114.00, -38.00, 85.03, 7.70, 22.13, -3.06, -1.00, 4.93, 96.00, 524.00, -52.00, 44.15, 30.15, 37.32, 1.50, 1.85, -0.39, 65.00, 431.00, -371.00}
                                ,{-56.13, -49.32, 137.92, 29.68, -96.86, -4.33, 83.00, -139.00, -25.00, 34.53, -46.09, -40.78, 4.33, -10.85, 44.36, 255.00, 309.00, -369.00, 122.18, -83.62, 91.71, 2.12, -0.46, 0.66, 127.00, 279.00, -203.00, 65.37, -90.23, 112.65, 12.94, 4.63, 30.68, 271.00, 119.00, -269.00, 86.28, 17.94, 29.64, -2.99, 0.02, 4.23, 142.00, 490.00, -260.00, 42.51, 2.89, 1.08, 1.41, 0.44, -0.29, -41.00, 421.00, -239.00}
                        };

                        for(int i=0; i<values.length; i++)
                        {
                            dataListener.onDataAvailable(values[i]);
                            Thread.sleep(10);
                        }

                    }
                    catch( NullPointerException e )
                    {

                    }
                }
                catch( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}