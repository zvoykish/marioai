package ch.idsia.benchmark.mario.engine;

import ch.idsia.benchmark.mario.environments.Environment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */
public class Replayer
{
private ZipFile zf;
private ZipEntry ze;
private BufferedInputStream fis;
Queue replayFiles = new LinkedList();

public Replayer(String fileName) throws IOException
{
    if (!fileName.endsWith(".zip"))
        fileName += ".zip";

    zf = new ZipFile(fileName);
    Enumeration en = zf.entries();
    while (en.hasMoreElements())
    {
        String s = ((ZipEntry) en.nextElement()).getName();
        if (s.endsWith(".mario") || s.endsWith(".act"))
        {
            s = s.substring(0, s.lastIndexOf("."));
            if (!replayFiles.contains(s))
                replayFiles.add(s);
        }
    }
}

public void openFile(String filename) throws Exception
{
    ze = zf.getEntry(filename);

    if (ze == null)
        throw new Exception("[Mario AI EXCEPTION] : File " + filename + " not found in the archive");
}

private void openBufferedInputStream() throws IOException
{
    fis = new BufferedInputStream(zf.getInputStream(ze));
}

public boolean[] readAction() throws IOException
{
    if (fis == null)
        openBufferedInputStream();

    boolean[] buffer = new boolean[Environment.numberOfButtons];
//    int count = fis.read(buffer, 0, size);
    byte actions = (byte) fis.read();
    for (int i = 0; i < Environment.numberOfButtons; i++)
    {
        if ((actions & ((1 << i))) > 0)
            buffer[i] = true;
        else
            buffer[i] = false;
    }

    if (actions == -1)
        buffer = null;

    return buffer;
}

public byte[] readGameInformation(int length) throws IOException
{
    if (fis == null)
        openBufferedInputStream();

    byte[] buffer = new byte[length];
    fis.read(buffer, 0, length);

    return buffer;
}

public Object readObject() throws IOException, ClassNotFoundException
{
    ObjectInputStream ois = new ObjectInputStream(zf.getInputStream(ze));
    Object res = ois.readObject();
//    ois.close();

    return res;
}

public void closeFile() throws IOException
{
    fis.close();
}

public void closeZip() throws IOException
{
    zf.close();
}

public String getNextReplayFileName()
{
    String s = (String) replayFiles.poll();

    return s;
}
}