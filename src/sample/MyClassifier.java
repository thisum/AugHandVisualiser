package sample;

import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by thisum_kankanamge on 11/9/18.
 */
public class MyClassifier
{
    double predication = 0.0;
    String predClass = "";
    private ArrayList<Attribute> attributes;
    Instances dataRaw;

    /**
     * Test the classifier for the input essay and output classified value
     *
     */
    public int testModel(String modelFilePath) {
        try {

            setAttributes();


            RandomForest randomForest = loadModel(modelFilePath);
            System.out.println("Loading trained model done.\nTesting...");

            double [][]values = {  {-33.34, -99.92, 181.28, 22.30, -125.75, 0.69, 73.00, 143.00, -233.00, 48.82, -103.78, -111.05, 20.50, 8.30, 59.43, -31.00, 333.00, -191.00, 108.87, -146.83, 68.48, 21.35, 16.35, 2.39, -147.00, 179.00, -197.00, 56.56, -143.00, 91.44, 31.01, 22.58, 38.91, -6.00, 60.00, -206.00, 80.71, -5.17, 15.20, 13.28, 20.10, 11.55, -17.00, 425.00, -203.00, 41.33, 54.70, 2.15, 12.88, -20.44, -7.89, 190.00, 342.00, -78.00},
                    {-74.63, -73.43, 113.10, 30.33, -96.43, -4.24, 72.00, 26.00, 212.00, -20.51, -106.22, -181.89, 5.33, -10.83, 44.21, 158.00, 138.00, -40.00, 38.90, -130.11, -22.25, 2.84, -0.82, 0.56, -25.00, -73.00, -41.00, -17.63, -122.64, -4.00, 14.25, 4.33, 30.09, 148.00, -274.00, -44.00, 19.32, 59.65, -87.09, -2.30, 0.05, 4.58, 259.00, 41.00, 101.00, -31.30, 13.27, 116.72, 0.80, 1.09, 0.20, 207.00, 135.00, -297.00}};

//            testInstance.setDataset(dataRaw);

            BufferedReader input = new BufferedReader(new FileReader("/Users/thisum/Documents/Personel_Docs/NUS_MSc/Research/NUS_Research/Application/AugHandVisualizer/haimo_static_test.arff"));
            Instances test = new Instances(input);
            test.setClassIndex(test.numAttributes() - 1);


            System.out.println("Random Forest Classifier now running...");
            for (int i = 0; i < 2; i++) {
                DenseInstance testInstance = new DenseInstance(1.0, values[i]);
                test.add(testInstance);
                Instance current = test.instance(0);
                 predication = randomForest.classifyInstance(current);
                 predClass = test.classAttribute().value(( int ) predication);
                System.out.println("Assigned a score of: " + predication + "  class: " + predClass);
                test.remove(0);
            }
//            System.out.println(testInstance);
//            randomForest.buildClassifier(dataRaw);
//            predication = randomForest.classifyInstance(testInstance);
//            predClass = testInstance.classAttribute().value(( int ) predication);
//            System.out.println("Assigned a score of: " + predication + "  class: " + predClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * This method loads the model to be used as classifier.
     *
     * @param fileName The name of the file that stores the text.
     */
    public static RandomForest loadModel(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
            RandomForest classifier = (RandomForest) tmp;
            in.close();
            System.out.println("===== Loaded model: " + fileName + " =====");
            return classifier;
        } catch (Exception e) { // Given the cast, a ClassNotFoundException must
            // be caught along with the IOException
            System.out.println("Problem found when reading: " + fileName);
        }
        return null;
    }


    private void setAttributes()
    {
        attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("T_ax", false));
        attributes.add(new Attribute("T_ay", false));
        attributes.add(new Attribute("T_az", false));
        attributes.add(new Attribute("T_gx", false));
        attributes.add(new Attribute("T_gy", false));
        attributes.add(new Attribute("T_gz", false));
        attributes.add(new Attribute("T_mx", false));
        attributes.add(new Attribute("T_my", false));
        attributes.add(new Attribute("T_mz", false));
        attributes.add(new Attribute("I_ax", false));
        attributes.add(new Attribute("I_ay", false));
        attributes.add(new Attribute("I_az", false));
        attributes.add(new Attribute("I_gx", false));
        attributes.add(new Attribute("I_gy", false));
        attributes.add(new Attribute("I_gz", false));
        attributes.add(new Attribute("I_mx", false));
        attributes.add(new Attribute("I_my", false));
        attributes.add(new Attribute("I_mz", false));
        attributes.add(new Attribute("M_ax", false));
        attributes.add(new Attribute("M_ay", false));
        attributes.add(new Attribute("M_az", false));
        attributes.add(new Attribute("M_gx", false));
        attributes.add(new Attribute("M_gy", false));
        attributes.add(new Attribute("M_gz", false));
        attributes.add(new Attribute("M_mx", false));
        attributes.add(new Attribute("M_my", false));
        attributes.add(new Attribute("M_mz", false));
        attributes.add(new Attribute("R_ax", false));
        attributes.add(new Attribute("R_ay", false));
        attributes.add(new Attribute("R_az", false));
        attributes.add(new Attribute("R_gx", false));
        attributes.add(new Attribute("R_gy", false));
        attributes.add(new Attribute("R_gz", false));
        attributes.add(new Attribute("R_mx", false));
        attributes.add(new Attribute("R_my", false));
        attributes.add(new Attribute("R_mz", false));
        attributes.add(new Attribute("P_ax", false));
        attributes.add(new Attribute("P_ay", false));
        attributes.add(new Attribute("P_az", false));
        attributes.add(new Attribute("P_gx", false));
        attributes.add(new Attribute("P_gy", false));
        attributes.add(new Attribute("P_gz", false));
        attributes.add(new Attribute("P_mx", false));
        attributes.add(new Attribute("P_my", false));
        attributes.add(new Attribute("P_mz", false));
        attributes.add(new Attribute("B_ax", false));
        attributes.add(new Attribute("B_ay", false));
        attributes.add(new Attribute("B_az", false));
        attributes.add(new Attribute("B_gx", false));
        attributes.add(new Attribute("B_gy", false));
        attributes.add(new Attribute("B_gz", false));
        attributes.add(new Attribute("B_mx", false));
        attributes.add(new Attribute("B_my", false));
        attributes.add(new Attribute("B_mz", false));
        attributes.add(new Attribute("label"));

        dataRaw = new Instances("TestInstances", attributes , 0);
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1); // Assuming z (z on lastindex) as classindex

    }

}
