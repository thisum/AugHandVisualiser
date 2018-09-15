package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application implements DataListener
{

    private RandomForest rf;
    private SMO smo;
    private ObjectInputStream ois;
    private FileInputStream fis;
    private ArrayList<Attribute> attributes;
    private Instances dataInstances;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try
        {
            fis = new FileInputStream("/Users/thisum/Documents/Personel_Docs/NUS_MSc/Research/NUS_Research/Application/AugHandVisualizer/classifier.model");
            ois = new ObjectInputStream(fis);
            rf = ( RandomForest ) ois.readObject();
//            smo = ( SMO ) ois.readObject();
            BufferedReader input = new BufferedReader(new FileReader("/Users/thisum/Documents/Personel_Docs/NUS_MSc/Research/NUS_Research/Application/AugHandVisualizer/haimo_static_test.arff"));
            dataInstances = new Instances(input);
            dataInstances.setClassIndex(dataInstances.numAttributes() - 1);
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            fis.close();
            ois.close();
        }

        setDataListener();
//        initiClassifier();

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

    }

    private void setDataListener()
    {
        SerialClass serialClass = new SerialClass();
        serialClass.setDataListener(this);
    }

    private void initiClassifier()
    {
        MyClassifier myClassifier = new MyClassifier();
        myClassifier.testModel("/Users/thisum/Documents/Personel_Docs/NUS_MSc/Research/NUS_Research/Application/AugHandVisualizer/classifier.model");
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private double predication = 0.0;
    private String predictedClass = "";
    private int lastPrediction = 0;
    private int tempPrediction = 0;

    @Override
    public void onDataAvailable(double[] doubleValues)
    {
        try
        {
            DenseInstance denseInstance = new DenseInstance(1.0, doubleValues);
            dataInstances.add(denseInstance);

            Instance current = dataInstances.instance(0);
            predication = classifyInstance(current);
            tempPrediction = ( int ) predication;
            if(tempPrediction != lastPrediction)
            {
                predictedClass = predication == -1 ? "Not Detected" : dataInstances.classAttribute().value(tempPrediction);

                System.out.println("prediction: " + predictedClass + "   score: " + predication + "\n");
                lastPrediction = tempPrediction;
            }

            dataInstances.remove(0);
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    public double classifyInstance(Instance instance) throws Exception {
        double[] dist = rf.distributionForInstance(instance);
//        double[] dist = smo.distributionForInstance(instance);
        if(dist == null) {
            throw new Exception("Null distribution predicted");
        } else {
            switch(instance.classAttribute().type()) {
                case 0:
                case 3:
                    return dist[0];
                case 1:
                    double max = 0.0D;
                    int maxIndex = 0;

                    for(int i = 0; i < dist.length; ++i) {
                        if(dist[i] > max) {
                            maxIndex = i;
                            max = dist[i];
                        }
                    }

//                    if(max < 0.7D)
//                    {
//                        return -1;
//                    }
//                    else
//                    {
//                        return (double)maxIndex;
//                    }
                    if(max > 0.0D) {
                        return (double)maxIndex;
                    }

                case 2:
                default:
                    return Utils.missingValue();
            }
        }
    }
}
