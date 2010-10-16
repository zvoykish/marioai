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

public class Recorder
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