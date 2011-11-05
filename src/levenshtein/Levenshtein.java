/*
 * A simple program to play with Levenshtein words
 */
package levenshtein;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author goodwin
 */
public class Levenshtein {
    public static final String FILE_SHORTLIST="/home/goodwin/NetBeansProjects/Levenshtein/src/levenshtein/shortlist.dat";
    public static final String FILE_FULLLIST= "/home/goodwin/NetBeansProjects/Levenshtein/src/levenshtein/causes.dat";
    public static final int DEGREES_OF_SEPARATION = 1;
    public static final String CENTER_OF_ATTENTION = "causes";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //TODO: Get word list
        getWordList();
        //System.out.println(levenshteinFriend("lamps", "camp"));
    }

    public static void getWordList() {
        int count = 0;
        try {
            FileInputStream wordFileStream = new FileInputStream(FILE_FULLLIST);

            DataInputStream dis = new DataInputStream(wordFileStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));

            String word;
            while ((word = br.readLine()) != null) {
                if (levenshteinFriend(CENTER_OF_ATTENTION, word)) {
                    //write a 1
                    System.out.println(word);
                    count++;
                } else {
                    //write a 0
                }
            }
            dis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Levenshtein.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Levenshtein.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Friend count: " + count);
    }

    public static boolean levenshteinFriend(String primary, String potentialFriend) {
        int m = primary.length();
        int n = potentialFriend.length();

        if (Math.abs(m - n) > DEGREES_OF_SEPARATION) {
            return false;
        }
        int levenshteinCount = 0;
        if (m == n) {
            //Substitution
            for (int i = 0; i < primary.length(); i++) {
                if (primary.charAt(i) != potentialFriend.charAt(i)) {
                    if (++levenshteinCount > DEGREES_OF_SEPARATION) {
                        return false;
                    }
                }
            }
            return true;
        }
        if (m < n) {
            //Addition
            int offset = 0;
            for (int i = 0; i < primary.length(); i++) {
                if (primary.charAt(offset) != potentialFriend.charAt(i)) {
                    if (++levenshteinCount > DEGREES_OF_SEPARATION) {
                        return false;
                    }
                } else {
                    offset++;
                }
            }
            return (n - m) == DEGREES_OF_SEPARATION;
        }
        if (m > n) {
            //Deletion
            int offset = 0;
            for (int i = 0; i < potentialFriend.length(); i++) {
                if (primary.charAt(i) != potentialFriend.charAt(offset)) {
                    if (++levenshteinCount > DEGREES_OF_SEPARATION) {
                        return false;
                    }
                } else {
                    offset++;
                }
            }
            return (m - n) == DEGREES_OF_SEPARATION;
        }
        return true;
    }

    public static int levenshteinDistance(String s, String t) {
        int m = s.length();
        int n = t.length();

        int[][] d = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }
        int i2, j2;
        for (int j = 1; j <= n; j++) {
            j2 = j - 1;
            for (int i = 1; i <= m; i++) {
                i2 = i - 1;
                if (s.charAt(i2) == t.charAt(j2)) {
                    d[i][j] = d[i2][j2];
                } else {
                    d[i][j] = minimum(d, i, j);
                }
            }
        }
        return d[m][n];
    }

    public static int minimum(int[][] d, int i, int j) {
        return Math.min(
                Math.min(
                d[i - 1][j] + 1, //Delection
                d[i][j - 1] + 1 //Insertion
                ),
                d[i - 1][j - 1] + 1 //Substitution
                );
    }
}
