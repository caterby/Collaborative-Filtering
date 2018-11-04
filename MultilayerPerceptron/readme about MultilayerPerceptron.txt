
*************1. CONVERT THE TXT FILES INTO THE ARFF FILES *****************
The execution process is:

First part:convert the files
put the Converter.java file under the same file of dataset file;
find the path of the *java file under the cmd;
using javac Converter.java and java Converter commands to convert the file into ARFF file;

Second part: use the MultilayerPerceptron to classify the files

Run the following command and change the parameters, the result is as follows: 
java -Xmx2g -cp "C:\Program Files\Weka-3-8\weka.jar" weka.classifiers.functions.MultilayerPerceptron -L 0.01 -M 0.2 -N 10 -V 0 -S 0 -E 20 -H 10 -B -C -I -t hw2_train.arff -T hw2_test.arff

We can change the paramaters of the MultilayerPerceptron:
L: learning rate M: momentum N: iterate times H: indicate the hidden layers and the number of nodes



************************2. THE BEGINING OF THE FIRST TEST****************************

===============Parameters for the multilayers perceptrons===============

The nodes of the one hidden layers: 15
The learning rate: 0.04
N 50 

Time taken to build model: 81.65 seconds
Time taken to test model on training data: 0.75 seconds

=== Error on training data ===

Correctly Classified Instances         462               99.784  %
Incorrectly Classified Instances         1                0.216  %
Kappa statistic                          0.9944
Mean absolute error                      0.0238
Root mean squared error                  0.0533
Relative absolute error                  6.0945 %
Root relative squared error             12.0645 %
Total Number of Instances              463


=== Detailed Accuracy By Class ===

                 TP Rate  FP Rate  Precision  Recall   F-Measure  MCC      ROC Area  PRC Area  Class
                 1.000    0.008    0.997      1.000    0.999      0.994    1.000     1.000     ham
                 0.992    0.000    1.000      0.992    0.996      0.994    1.000     1.000     spam
Weighted Avg.    0.998    0.006    0.998      0.998    0.998      0.994    1.000     1.000


=== Confusion Matrix ===

   a   b   <-- classified as
 340   0 |   a = ham
   1 122 |   b = spam


=== Error on test data ===

Correctly Classified Instances         442               92.4686 %
Incorrectly Classified Instances        36                7.5314 %
Kappa statistic                          0.8107
Mean absolute error                      0.1092
Root mean squared error                  0.2526
Relative absolute error                 27.7509 %
Root relative squared error             56.7673 %
Total Number of Instances              478


=== Detailed Accuracy By Class ===

                 TP Rate  FP Rate  Precision  Recall   F-Measure  MCC      ROC Area  PRC Area  Class
                 0.945    0.131    0.951      0.945    0.948      0.811    0.958     0.985     ham
                 0.869    0.055    0.856      0.869    0.863      0.811    0.958     0.853     spam
Weighted Avg.    0.925    0.110    0.925      0.925    0.925      0.811    0.958     0.949


=== Confusion Matrix ===

   a   b   <-- classified as
 329  19 |   a = ham
  17 113 |   b = spam



C:\Users\jxl16\Desktop\machine learning3\MultilayerPerceptron>

************************************END********************************************


************************3. THE BEGINING OF THE SECOND TEST*************************

===============Parameters for the multilayers perceptrons===============

The nodes of the one hidden layers: 10
The learning rate: 0.3
N:75
Class ham
    Input
    Node 0
Class spam
    Input
    Node 1


Time taken to build model: 83.83 seconds
Time taken to test model on training data: 0.56 seconds

=== Error on training data ===

Correctly Classified Instances         461               99.568  %
Incorrectly Classified Instances         2                0.432  %
Kappa statistic                          0.9889
Mean absolute error                      0.0183
Root mean squared error                  0.0718
Relative absolute error                  4.6808 %
Root relative squared error             16.255  %
Total Number of Instances              463


=== Detailed Accuracy By Class ===

                 TP Rate  FP Rate  Precision  Recall   F-Measure  MCC      ROC Area  PRC Area  Class
                 0.997    0.008    0.997      0.997    0.997      0.989    0.999     1.000     ham
                 0.992    0.003    0.992      0.992    0.992      0.989    0.999     0.998     spam
Weighted Avg.    0.996    0.007    0.996      0.996    0.996      0.989    0.999     0.999


=== Confusion Matrix ===

   a   b   <-- classified as
 339   1 |   a = ham
   1 122 |   b = spam


=== Error on test data ===

Correctly Classified Instances         442               92.4686 %
Incorrectly Classified Instances        36                7.5314 %
Kappa statistic                          0.8098
Mean absolute error                      0.0929
Root mean squared error                  0.2469
Relative absolute error                 23.6161 %
Root relative squared error             55.485  %
Total Number of Instances              478


=== Detailed Accuracy By Class ===

                 TP Rate  FP Rate  Precision  Recall   F-Measure  MCC      ROC Area  PRC Area  Class
                 0.948    0.138    0.948      0.948    0.948      0.810    0.971     0.990     ham
                 0.862    0.052    0.862      0.862    0.862      0.810    0.971     0.919     spam
Weighted Avg.    0.925    0.115    0.925      0.925    0.925      0.810    0.971     0.971


=== Confusion Matrix ===

   a   b   <-- classified as
 330  18 |   a = ham
  18 112 |   b = spam

************************************END********************************************

************************4. THE BEGINING OF THE THIRD TEST**************************

===============Parameters for the multilayers perceptrons===============

The nodes of the one hidden layers: 10
The learning rate: 0.01
N:10

Class ham
    Input
    Node 0
Class spam
    Input
    Node 1


Time taken to build model: 12.22 seconds
Time taken to test model on training data: 0.56 seconds

=== Error on training data ===

Correctly Classified Instances         405               87.473  %
Incorrectly Classified Instances        58               12.527  %
Kappa statistic                          0.6354
Mean absolute error                      0.2693
Root mean squared error                  0.3199
Relative absolute error                 68.9475 %
Root relative squared error             72.4233 %
Total Number of Instances              463


=== Detailed Accuracy By Class ===

                 TP Rate  FP Rate  Precision  Recall   F-Measure  MCC      ROC Area  PRC Area  Class
                 0.982    0.423    0.865      0.982    0.920      0.664    0.967     0.987     ham
                 0.577    0.018    0.922      0.577    0.710      0.664    0.967     0.910     spam
Weighted Avg.    0.875    0.315    0.880      0.875    0.864      0.664    0.967     0.967


=== Confusion Matrix ===

   a   b   <-- classified as
 334   6 |   a = ham
  52  71 |   b = spam


=== Error on test data ===

Correctly Classified Instances         385               80.5439 %
Incorrectly Classified Instances        93               19.4561 %
Kappa statistic                          0.4305
Mean absolute error                      0.303
Root mean squared error                  0.3615
Relative absolute error                 76.9908 %
Root relative squared error             81.2458 %
Total Number of Instances              478


=== Detailed Accuracy By Class ===

                 TP Rate  FP Rate  Precision  Recall   F-Measure  MCC      ROC Area  PRC Area  Class
                 0.948    0.577    0.815      0.948    0.876      0.459    0.913     0.970     ham
                 0.423    0.052    0.753      0.423    0.542      0.459    0.913     0.694     spam
Weighted Avg.    0.805    0.434    0.798      0.805    0.785      0.459    0.913     0.895


=== Confusion Matrix ===

   a   b   <-- classified as
 330  18 |   a = ham
  75  55 |   b = spam


************************************END********************************************


=========================== 5.CONCLUSSION =================================
The nueral network will use gradient decsent method to update the weight, 
so it will converge to the local minimum value. That's why different initial
parameters will converge to the different minimum point. In order to get the
best effectiveness of the NNs, when use the nueral network, we should try to
 use the different initial values to train the NNs.
