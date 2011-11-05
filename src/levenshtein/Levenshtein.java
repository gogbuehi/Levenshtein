/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levenshtein;

/**
 *
 * @author goodwin
 */
public class Levenshtein {
    public static final int DEGREES_OF_SEPARATION=1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //TODO: Get word list
        System.out.println(levenshtienFriend("lamps","camp"));
    }
    public static boolean levenshtienFriend(String primary, String potentialFriend) {
        int m = primary.length();
        int n = potentialFriend.length();
        
        if (Math.abs(m-n) > DEGREES_OF_SEPARATION) {
            return false;
        }
        int levenshteinCount = 0;
        if (m == n) {
            //Substitution
            for(int i = 0; i < primary.length(); i++) {
                if (primary.charAt(i) != potentialFriend.charAt(i)) {
                    if(++levenshteinCount > DEGREES_OF_SEPARATION) {
                        return false;
                    }
                }
            }
            return true;
        } 
        if (m < n) {
            //Addition
            int offset = 0;
            for(int i = 0; i < primary.length(); i++) {
                if (primary.charAt(offset) != potentialFriend.charAt(i)) {
                    if(++levenshteinCount > DEGREES_OF_SEPARATION) {
                        return false;
                    }
                } else {
                    offset++;
                }
            }
            return (n-m)==DEGREES_OF_SEPARATION;
        } 
        if (m > n) {
            //Deletion
            int offset = 0;
            for(int i = 0; i < potentialFriend.length(); i++) {
                if (primary.charAt(i) != potentialFriend.charAt(offset)) {
                    if(++levenshteinCount > DEGREES_OF_SEPARATION) {
                        return false;
                    }
                } else {
                    offset++;
                }
            }
            return (m-n)==DEGREES_OF_SEPARATION;
        } 
        return true;
    }
    public static int levenshteinDistance(String s, String t) {
        int m = s.length();
        int n = t.length();
        
        int[][] d = new int[m+1][n+1];
        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }
        int i2,j2;
        for (int j = 1; j <= n; j++) {
            j2 = j-1;
            for(int i = 1; i <= m; i++) {
                i2 = i-1;
                if (s.charAt(i2) == t.charAt(j2)) {
                    d[i][j] = d[i2][j2];
                } else {
                    d[i][j] = minimum(d,i,j);
                }
            }
        }
        return d[m][n];
    }
    
    public static int minimum(int[][] d, int i, int j) {
        return Math.min(
                Math.min(
                    d[i-1][j] + 1, //Delection
                    d[i][j-1] + 1 //Insertion
                ),
                d[i-1][j-1] + 1 //Substitution
                );
    }
}
