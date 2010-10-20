package ch.idsia.benchmark.mario.engine;

import ch.idsia.tools.ReplayerOptions;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */

public class Recorder
{
private ZipOutputStream zos;
boolean lastRecordingState = false;
private Queue<ReplayerOptions.Interval> chunks = new LinkedList<ReplayerOptions.Interval>();
private ReplayerOptions.Interval chunk;

public Recorder(String fileName) throws FileNotFoundException
{
    if (!fileName.endsWith(".zip"))
        fileName += ".zip";

    zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
}

public void createFile(String filename) throws IOException
{
   zos.putNextEntry(new ZipEntry(filename));
}

public void writeObject(Object object) throws IOException
{
    ObjectOutputStream oos = new ObjectOutputStream(zos);
    oos.writeObject(object);
    oos.flush();
}

public void closeFile() throws IOException
{
    zos.flush();
    zos.closeEntry();
}

public void closeRecorder(boolean recordingState, int time) throws IOException
{
    changeRecordingState(recordingState, time);
    if (!chunks.isEmpty())
    {
        createFile("chunks");
        writeObject(chunks);
        closeFile();
    }
    zos.flush();
    zos.close();
}

public void writeAction(final boolean[] bo) throws IOException
{
    byte action = 0;

    for (int i = 0; i < bo.length; i++)
        if (bo[i])
            action |= (1 << i);

    zos.write(action);
}

public void changeRecordingState(boolean state, int time)
{
    if (state && !lastRecordingState)
    {
        chunk = new ReplayerOptions.Interval();
        chunk.from = time;
        lastRecordingState = state;
    } else if (!state && lastRecordingState)
    {
        chunk.to = time;
        chunks.add(chunk);
        lastRecordingState = state;
    }
}
}