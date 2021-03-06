
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CollaborativeFiltering {
    private static final String TRAININGDATA = "data/TrainingRatings.txt";
    private static final String TESTINGDATA = "data/TestingRatings.txt";
    static int[] uidArray;

    public static void main(String[] args) {
        long start = System.nanoTime();
        double MAE = 0, RMSE = 0;
        User[] allUsers = parseUsers(TRAININGDATA);
        Correlation core = new Correlation(allUsers);
        System.out.println("Matrix Calculation Finished.\nTime consumption(s): " + (System.nanoTime() - start) * 1.0e-9);
        User[] testUsers = parseUsers(TESTINGDATA);
        for (User actUser : testUsers) {
            for (int mid : actUser.ratings.keySet()) {
                double pscore = 0;
                int position;
                if ((position = Arrays.binarySearch(uidArray, actUser.userId)) != -1) {
                    pscore = allUsers[position].predictedVote(core, allUsers, mid);
                }
                int prating = (int) Math.round(pscore);
                int rating = actUser.ratings.get(mid);
                double error = pscore - rating;
                MAE += Math.abs(error);
                RMSE += error * error;
                System.out.println("User:" + actUser.userId + " Movie:" + mid + " " + prating + "(" + rating + ")");
            }
        }
        MAE = MAE / testUsers.length;
        RMSE = Math.sqrt(RMSE / testUsers.length);
        System.out.println("Mean Absolute Error: " + MAE + "; Root Mean Squared Error: " + RMSE);
        System.out.println("Time consumption(s): " + (System.nanoTime() - start) * 1.0e-9);
    }

    /**
     * Read the data set file convert them to user list
     */
    private static User[] parseUsers(String name) {
        Map<Integer, User> userMap = new HashMap<>(30000);
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            while ((line = br.readLine()) != null) {
                String[] s = line.split(",");
                int[] v = {Integer.parseInt(s[0]), Integer.parseInt(s[1]), (int) Float.parseFloat(s[2])};
                if (userMap.containsKey(v[1])) {
                    userMap.get(v[1]).ratings.put(v[0], v[2]);
                } else {
                    userMap.put(v[1], new User(v[0], v[1], v[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        User[] users = new User[userMap.size()];
        if (uidArray == null) {
            uidArray = new int[users.length];
            int i = 0, j = 0;
            for (int uid : userMap.keySet()) {
                uidArray[i++] = uid;
            }
            Arrays.sort(uidArray);//For binary search
            for (int uid : uidArray) {
                User user = userMap.get(uid);
                user.calMeanVote();
                users[j++] = user;
            }
        } else {
            users = userMap.values().toArray(users);
        }
        return users;
    }

}

/**
 * User class contains its ratings
 */
class User {

    double meanRating;

    //key-Movie Id; value-vote(1-5), the average size is 112 based on given information
    Map<Integer, Integer> ratings;

    int userId;

    //Construct a user by its first rating record
    public User(int mid, int uid, int vote) {
        userId = uid;
        ratings = new HashMap<>();
        ratings.put(mid, vote);
    }

    //Get the rating score user voted for a movie return 0 if had never rate this movie
    private double ratingMeanError(int mid) {
        if (ratings.containsKey(mid)) return ratings.get(mid) - meanRating;
        return -meanRating;
    }

    void calMeanVote() {
        meanRating = (double) ratings.values().stream().mapToInt(Integer::intValue).sum() / ratings.size();
    }

    //Method used to calculate the predicted rating for one movie
    double predictedVote(Correlation core, User[] users, int mid) {
        double result = 0;
        double k = 0;
        for (User user : users) {
            double w = core.getWeight(userId, user.userId);
            if (w != 0) {
                k += Math.abs(w);
                result += user.ratingMeanError(mid) * w;
            }
        }
        return meanRating + result / k;
    }
}

/**
 * Represent the Correlation Triangular Matrices W(i,j)
 * Calculate the correlation weight between user i and user j
 */
class Correlation {
    //weights[i][j] represents the correlation between user i and j
    private double[][] weights;

    public Correlation(User[] users) {
        int size = CollaborativeFiltering.uidArray.length;
        weights = new double[size][];
        for (int i = 0; i < size; i++) {
            weights[i] = new double[i + 1];
            User u1 = users[i];
            Set<Integer> set1 = u1.ratings.keySet();
            for (int j = 0; j < i; j++) {
                User u2 = users[j];
                Set<Integer> commons = new HashSet<>(set1);//fastest way to copy a set, performance crucial!!!-----------------------------
                commons.retainAll(u2.ratings.keySet());//way too slow!!!!!!!!
                double s1 = 0, s2 = 0, s3 = 0;
                for (int k : commons) {
                    double v1 = u1.ratings.get(k) - u1.meanRating;
                    double v2 = u2.ratings.get(k) - u2.meanRating;
                    s1 += v1 * v2;
                    s2 += v1 * v1;
                    s3 += v2 * v2;
                }
                if ((s3 *= s2) != 0) weights[i][j] = s1 / Math.sqrt(s3);
            }
            weights[i][i] = 1;
        }
    }

    //If user id has never appeared in training database just return 0;
    double getWeight(int id1, int id2) {
        int i = Arrays.binarySearch(CollaborativeFiltering.uidArray, id1);//-!performance critical
        int j = Arrays.binarySearch(CollaborativeFiltering.uidArray, id2);//-!performance critical
        if (i != -1 && j != -1) {
            if (i > j) return weights[i][j];
            return weights[j][i];
        }
        return 0;
    }
}