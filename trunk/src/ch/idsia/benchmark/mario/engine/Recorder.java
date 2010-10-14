package ch.idsia.benchmark.mario.engine;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */

public class Recorder //TODO: auto add .zip extension
{
private ZipOutputStream zos;

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

public void writeMarioState(int[] data, float[] marioFloatPos) throws IOException
{
    byte[] res = new byte[data.length + marioFloatPos.length];
    int i = 0;
    for (; i < data.length; i++)
        res[i] = (byte)data[i];
    for (; i < data.length+marioFloatPos.length; i++)
        res[i] = (byte)(marioFloatPos[i-data.length]/16);

    zos.write(res, 0, res.length);
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

public void closeZip() throws IOException
{
    zos.flush();
    zos.close();
}

public void writeAction(final boolean[] bo) throws IOException
{
    byte action = 0;

    for (int i = 0; i < bo.length; i++)
        if (bo[i])
            action |= (1 << i);

    System.err.println(action);

    zos.write(action);
}
}