package com.anymailer.core.task;

import com.anymailer.core.match.Levenshtein;

/**
 * Created by noel on 12/12/15.
 */
public class TaskFactory
{

    public static ITask buildTask(String name)
    {
        if (name == null || name.equals(""))
            return new NullTask();

        name = name.toLowerCase();

        if (Levenshtein.contains(name, "revolico"))
            return new RevolicoTask();

        if (Levenshtein.contains(name, "stackoverflow"))
            return new StackOverflowTask();

        if (Levenshtein.contains(name, "download"))
            return new DownloadTask();

        return new NullTask();
    }

}
