package ch.idsia.benchmark.mario.engine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */
//TODO: buffered output
public class Recorder //TODO: auto add .zip extension
{
private ZipOutputStream zos;

public Recorder(String filename) throws FileNotFoundException
{
    zos = new ZipOutputStream(new FileOutputStream(filename));
}

public void createFile(String filename) throws IOException
{
   zos.putNextEntry(new ZipEntry(filename));
}

public void writeBytes(byte[] data) throws IOException
{
    zos.write(data, 0, data.length);
}

public void writeObject(Object object) throws IOException
{
    ObjectOutputStream oos = new ObjectOutputStream(zos);
    oos.writeObject(object);
    oos.flush();
}

public void closeFile() throws IOException
{
    zos.closeEntry();
}

public void closeZip() throws IOException
{
    zos.close();
}

public void writeBytes(final boolean[] bo) throws IOException
{
    //TODO: translate boolean array to 1 byte using bits
    byte[] b = new byte[bo.length];
    for (int i = 0; i < bo.length; i++)
        b[i] = (byte) (bo[i] ? 1 : 0);
    writeBytes(b);
}
}