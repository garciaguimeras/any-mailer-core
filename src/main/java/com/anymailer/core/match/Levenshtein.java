package com.anymailer.core.match;

/**
 * Created by noel on 16/12/15.
 */
public class Levenshtein
{

    class MatchResult
    {

        float minDistance;
        float hitRate;
        String text;

        public MatchResult(float minDistance, float hitRate, String text)
        {
            this.minDistance = minDistance;
            this.hitRate = hitRate;
            this.text = text;
        }

        public String getText()
        {
            return text;
        }

        public float getMinDistance()
        {
            return minDistance;
        }

        public float getHitRate()
        {
            return hitRate;
        }
    }

    private float minimum(float a, float b, float c)
    {
        return Math.min(a, Math.min(b, c));
    }

    public MatchResult match(String sentence, String word)
    {
        char[] str1 = word.toLowerCase().toCharArray();
        char[] str2 = sentence.toLowerCase().toCharArray();
        float[][] matrix = new float[str1.length + 1][str2.length + 1];

        for (int i = 0; i <= str1.length; i++)
            matrix[i][0] = i;
        for (int j = 0; j <= str2.length; j++)
            matrix[0][j] = 0;

        float deleteCost;
        float insertCost;
        float subCost;

        for (int i = 1; i <= str1.length; i++)
        {
            for (int j = 1; j <= str2.length; j++)
            {
                if (str1[i - 1] == str2[j - 1])
                    matrix[i][j] = matrix[i - 1][j - 1] ;
                else
                {
                    // Levenshtein method
                    deleteCost = matrix[i - 1][j] + 1; // delete
                    insertCost = matrix[i][j - 1] + 1; // insertion
                    subCost = matrix[i - 1][j - 1] + ((str1[i - 1] == str2[j - 1]) ? 0 : 1); // substitution
                    matrix[i][j] = minimum (deleteCost, insertCost, subCost);

                    // Damerou improvement
                    if (i > 1 && j > 1 && str1[i - 1] == str2[j - 2]&& str1[i - 2] == str2[j - 1])
                        matrix[i][j] = Math.min(matrix[i][j], matrix[i - 2][j - 2] + 1); // transposition
                }
            }
        }

        // Get minimum
        float min = Float.MAX_VALUE;
        int pos = -1 ;
        for(int i = 1; i <= sentence.length(); i++)
        {
            if(matrix[word.length()][i] < min)
            {
                min = matrix[word.length()][i];
                pos = i ;
            }
        }

        // Get matching substring. POP
        int i = word.length();
        int j = pos;
        boolean loop = true;
        while (loop)
        {
            float value = matrix[i][j];
            int newi = i;
            int newj = j;
            if (i > 1)
            {
                if (j > 1 && matrix[i][j - 1] <= value)
                {
                    value = matrix[i][j - 1];
                    newi = i;
                    newj = j - 1;
                }
                if (j > 1 && matrix[i -1 ][j - 1] <= value)
                {
                    value = matrix[i - 1][j - 1];
                    newi = i - 1;
                    newj = j - 1;
                }
                if (matrix[i - 1][j] <= value)
                {
                    value = matrix[i - 1][j];
                    newi = i - 1;
                    newj = j;
                }
            }
            if (i == 1)
            {
                if (j > 1 && matrix[i][j - 1] <= value)
                {
                    value = matrix[i][j - 1];
                    newi = i;
                    newj = j - 1;
                }
                else
                    loop = false;
            }
            if (i == newi && j == newj)
                loop = false;
            i = newi;
            j = newj;
        }

        return new MatchResult(min, 1 - (min / word.length()), sentence.substring(j - 1, pos));
    }

    public static boolean contains(String sentence, String word)
    {
        Levenshtein levenshtein = new Levenshtein();
        MatchResult result = levenshtein.match(sentence, word);
        if (result.getHitRate() >= 0.75)
            return true;
        return false;
    }

}
