package com.anymailer.core.match;

import com.anymailer.core.Logger;
import org.junit.Assert;
import org.junit.Test;
import sun.rmi.runtime.Log;

/**
 * Created by noel on 16/12/15.
 */
public class LevenshteinTest
{

    Levenshtein levenshtein = new Levenshtein();

    @Test
    public void exactMatch()
    {
        Levenshtein.MatchResult result = levenshtein.match("El viejo esta en el banno y ya no puede mas", "en el banno");
        Logger.console.info("Exact match: " + result.getHitRate() + " Text: " + result.getText());
    }

    @Test
    public void caseMatch()
    {
        Levenshtein.MatchResult result = levenshtein.match("El viejo esta en el banno y ya no puede mas", "VIEJO");
        Logger.console.info("Case match: " + result.getHitRate() + " Text: " + result.getText());
    }

    @Test
    public void partialMatch()
    {
        Levenshtein.MatchResult result = levenshtein.match("El viejo esta en el banno y ya no puede mas", "biejo ezta");
        Logger.console.info("Partial match: " + result.getHitRate() + " Text: " + result.getText());
    }

    @Test
    public void noMatch()
    {
        Levenshtein.MatchResult result = levenshtein.match("El viejo esta en el banno y ya no puede mas", "almuerzo");
        Logger.console.info("No match: " + result.getHitRate() + " Text: " + result.getText());
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(Levenshtein.contains("Quiero algo de revolico", "revolico"));
        Assert.assertTrue(Levenshtein.contains("Buscar en reboliko una laptop", "revolico"));
        Assert.assertFalse(Levenshtein.contains("Nada", "revolico"));
        Logger.console.info("Contains: Ok");
    }

}
