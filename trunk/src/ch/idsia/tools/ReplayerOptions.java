package ch.idsia.tools;

import ch.idsia.benchmark.mario.engine.GlobalOptions;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Oct 15, 2010
 * Time: 1:13:13 AM
 * Package: ch.idsia.tools
 */
public class ReplayerOptions
{
public static class Interval
{
    public int from;
    public int to;

    public Interval(String interval)
    {
        String[] nums = interval.split(":");
        from = Integer.valueOf(nums[0]);
        to = Integer.valueOf(nums[1]);
    }

    public Interval(final int i, final int i1)
    {
        from = i;
        to = i1;
    }
}

private Queue<Interval> chunks = new LinkedList<Interval>();
private Queue<String> replays = new LinkedList<String>();
private String regex = "[a-zA-Z_0-9.-]+(;\\d+:\\d+)*";

public ReplayerOptions(String options)
{
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(options);

    while (matcher.find())
    {
        String group = matcher.group();
        replays.add(group);
    }
}

public String getNextReplayFile()
{
    String s = replays.poll();
    if (s == null)
        return null;
    
    String[] subgroups = s.split(";");
    if (subgroups.length == 0)
        return null;
    
    String fileName = subgroups[0];
    chunks.clear();

    if (subgroups.length > 1)
        for (int i = 1; i < subgroups.length; i++)
            chunks.add(new Interval(subgroups[i]));

    return fileName;
}

public Interval getNextIntervalInMarioseconds()
{
    return chunks.poll();
}

public Interval getNextIntervalInTicks()
{
    Interval i = chunks.poll();
    Interval res = null;

    if (i != null)
        res = new Interval(i.from * GlobalOptions.mariosecondMultiplier, i.to * GlobalOptions.mariosecondMultiplier);

    return res;
}

public boolean hasMoreChunks()
{
    return !chunks.isEmpty();
}
}
