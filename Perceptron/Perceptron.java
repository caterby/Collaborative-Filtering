

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Perceptron {

    private static final String[] STOP_WORDS = {"a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"};
    private static final int HAM = 0;
    private static final int SPAM = 1;
    private static final String[] TRAIN_PATH = {"dataset/hw2/train/ham", "dataset/hw2/train/spam"};
    private static final String[] TEST_PATH = {"dataset/hw2/test/ham", "dataset/hw2/test/spam"};

    // create a List variable named stopWords to store all the stop words in the stopword.txt
    private static List<String> stop_words;
    // define a HashMap variable named dictionary to store all the distinct words in the training set
    private static Set<String> dictionary;
    // the variable is used to store the weight of the features for Perceptron
    private static double[] W;
    // the array variable is used to store the number of spam and ham files in the test data set
    private static int[] numberOfTestFiles = {0, 0};
    // the learning rate array
    private static double[] learningRates = new double[20];
    // the iteration array
    private static int[] iterations = new int[20];

   // the static structure defines the number of the test files
    static {
        // the number of the HAM test files
        numberOfTestFiles[HAM] = new File(TEST_PATH[HAM]).list().length;
        // the number of the SPAM test files
        numberOfTestFiles[SPAM] = new File(TEST_PATH[SPAM]).list().length;
        for (int i = 0; i < 20; i++) {
            learningRates[i] = 0.004 + 0.002 * i;
            iterations[i] = 60 - i;
        }
        stop_words = Arrays.asList(STOP_WORDS);
    }

    public static void main(String[] args) {

        System.out.println("****************THE RESULT WITH STOP WORDS***************");
        countWords(TRAIN_PATH, false);
        for (int i = 0; i < 20; i++) {
            output(learningRates[i], iterations[i], false);
        }

        System.out.println("\n\n***************THE RESULT WITHOUT STOP WORDS**************");
        countWords(TRAIN_PATH, true);
        for (int i = 0; i < 20; i++) {
            output(learningRates[i], iterations[i], true);
        }
    }

    private static void output(double learningRate, int iteration, boolean filter) {
        trainPerceptron(toVectors(TRAIN_PATH, filter), learningRate, iteration);
        System.out.println("");
        System.out.println("learningRate = " + learningRate + ", iteration = " + iteration + " The accuracy is: " + testAccuracy(toVectors(TEST_PATH, filter)));
    }

    /**
     * Record the frequency of each word in the given training text.
     */
    private static void countWords(String[] folders, boolean filter) {
        dictionary = new LinkedHashSet<>();
        for (String folder : folders) {
            try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
                paths.forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        String line;
                        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
                            while ((line = br.readLine()) != null) {
                                String[] words = line.split(" ");
                                for (String word : words) {
                                    if (filter && stop_words.contains(word)) continue;
                                    dictionary.add(word);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method and its sub method are used to convert the raw data to vectors
     * Can only be called after dictionary has been constructed
     */
    private static ArrayList<TextVector> toVectors(String[] folders, boolean filter) {
        ArrayList<TextVector> vectors = new ArrayList<>();
        List<String> word_list = new ArrayList<>(dictionary);
        fillVector(folders, HAM, word_list, vectors, filter);
        fillVector(folders, SPAM, word_list, vectors, filter);
        return vectors;
    }

    private static void fillVector(String[] folders, int type, List<String> word_list, ArrayList<TextVector> vectors, boolean filter) {
        try (Stream<Path> paths = Files.walk(Paths.get(folders[type]))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    String line;
                    int[] features = new int[word_list.size() + 1];
                    features[0] = 1;    // set x0=1 for all vectorNormalize X
                    try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
                        while ((line = br.readLine()) != null) {
                            String[] words = line.split(" ");
                            for (String word : words) {
                                if (filter && stop_words.contains(word)) continue;
                                int index = word_list.indexOf(word) + 1;
                                if (index != 0) features[index]++;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    vectors.add(new TextVector(features, type == HAM ? -1 : 1));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implement the perceptron algorithm by using the perceptron training rule
     *
     * @param learningRate the learning rate in gradient ascent
     * vector is the vector of the test file
     * repeat is the iteration times
     */
    private static void trainPerceptron(ArrayList<TextVector> vectors, double learningRate, int repeat) {
        int size = dictionary.size() + 1;
        W = new double[size];//initially set all w=0
        while (repeat-- > 0) {
            for (TextVector tv : vectors) {
                int error = tv.predictionError(W);
                if (error != 0) {
                    for (int i = 0; i < size; i++) {
                        //update the parameters according to prediction error with respect to this single training example only.
                        if (tv.features[i] != 0) W[i] += learningRate * tv.features[i] * error;
                    }
                }
            }
        }
    }

    /**
     * Test the accuracy of perceptron algorithm
     * compute the number of the files that be classified correctly
     */
    private static double testAccuracy(ArrayList<TextVector> test_vectors) {
        int correct = 0;
        for (TextVector tv : test_vectors) {
            if (tv.predictionError(W) == 0) correct++;
        }
        return (double) correct / (numberOfTestFiles[HAM] + numberOfTestFiles[SPAM]);
    }
}

/**
 * convert each text example to vector
 */
class TextVector {
    int[] features;
    private int type;

    TextVector(int[] fts, int tp) {
        features = fts;
        type = tp;
    }

    int predictionError(double[] W) {
        return type - estimateType(W);
    }

    private int estimateType(double[] W) {
        double sum = 0;
        for (int i = 0; i < W.length; i++) {
            if (features[i] != 0) sum += W[i] * features[i];
        }
        return sum > 0 ? 1 : -1;
    }
}