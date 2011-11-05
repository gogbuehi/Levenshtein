/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levenshtein;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author goodwin
 */
public class LevenshteinNetwork {
    private BigBitMask mBigBitMask;
    private int mDegreesOfSeparation;
    private String mWordListFilename;
    
    public LevenshteinNetwork(String filename, int degreesOfSeparation) {
        mBigBitMask = new BigBitMask(fileLineCount(filename));
        mWordListFilename = filename;
        mDegreesOfSeparation = degreesOfSeparation;
    }
    
    public static int fileLineCount(String filename) {
        InputStream is;
        try {
            is = new BufferedInputStream(new FileInputStream(filename));
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }

            is.close();
            return (count > 0 ? ++count : 0); //Don't forget to add the first line
        } catch (IOException ex) {
            return 0;
        }
    }

    public int getWordList(String currentWord) {
        int count = 0;
        
        byte currentByte;
        int currentLine = 0;
        try {
            FileInputStream wordFileStream = new FileInputStream(mWordListFilename);

            DataInputStream dis = new DataInputStream(wordFileStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));

            String word;
            while ((word = br.readLine()) != null) {
                if (levenshteinFriend(currentWord, word)) {
                    //write a 1
                    System.out.println(currentLine + ": " + word);
                    count++;
                } else {
                    //write a 0
                }
                currentLine++;
            }
            dis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Levenshtein.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Levenshtein.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Friend count: " + count);
        return count;
    }
    /**
     * This checks for words that are only a distance of 1 Levenshtein from
     * the primary word.
     * @param primary
     * @param potentialFriend
     * @return 
     */
    public boolean levenshteinFriend(String primary, String potentialFriend) {
        int m = primary.length();
        int n = potentialFriend.length();

        if (Math.abs(m - n) > mDegreesOfSeparation) {
            return false;
        }
        int levenshteinCount = 0;
        if (m == n) {
            //Substitution
            for (int i = 0; i < primary.length(); i++) {
                if (primary.charAt(i) != potentialFriend.charAt(i)) {
                    if (++levenshteinCount > mDegreesOfSeparation) {
                        return false;
                    }
                }
            }
            return (levenshteinCount == 1); //Can't be your own friend
        }
        if (m < n) {
            //Addition
            int offset = 0;
            for (int i = 0; i < primary.length(); i++) {
                if (primary.charAt(offset) != potentialFriend.charAt(i)) {
                    if (++levenshteinCount > mDegreesOfSeparation) {
                        return false;
                    }
                } else {
                    offset++;
                }
            }
            return (n - m) == mDegreesOfSeparation;
        }
        if (m > n) {
            //Deletion
            int offset = 0;
            for (int i = 0; i < potentialFriend.length(); i++) {
                if (primary.charAt(i) != potentialFriend.charAt(offset)) {
                    if (++levenshteinCount > mDegreesOfSeparation) {
                        return false;
                    }
                } else {
                    offset++;
                }
            }
            return (m - n) == mDegreesOfSeparation;
        }
        return true;
    }
    
    public static boolean constrainedLevenshtein(String primary, String potential, int maxLevenshteinDistance) {
        int m  = primary.length();
        int n = potential.length();
        
        if (m == n) {
            switch(maxLevenshteinDistance) {
                case 1:
                    //Levenshtein Friend
                    break;
                case 2:
                    //2 Substitutions;
                    //1 Insertion + 1 Deletion
                    break;
                case 3:
                    //3 Substitutions
                    //1 Insertion + 1 Deletion + 1 Substitution
                    break;
                default:
                    //Not handled
            }
        } else if (m < n) {
            switch(maxLevenshteinDistance) {
                case 1:
                    //Levenshtein Friend
                    break;
                case 2:
                    //2 Insertions
                    //1 Insertion + 1 Substitution
                    break;
                case 3:
                    //3 Insertions
                    //2 Insertions + 1 Substitution
                    //2 Insertions + 1 Deletions
                    //1 Insertion + 2 Substitutions
                    break;
                default:
                    //Not Handled
            }
        } else if (m > n) {
            switch(maxLevenshteinDistance) {
                case 1:
                    //Levenshtein Friend
                    break;
                case 2:
                    //2 Deletions
                    //1 Deletion + 1 Substitution
                    break;
                case 3:
                    //3 Deletions
                    //2 Deletions + 1 Substitution
                    //2 Deletions + 1 Insertion
                    //1 Deletion + 2 Substitutions
                    break;
                default:
                    //Not handled
            }
        }
        
        return false;
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
