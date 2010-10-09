package ch.idsia.benchmark.mario.engine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */
public class Replayer //TODO: auto add .zip extension
{
private ZipFile zf;
private ZipEntry ze;
private BufferedInputStream fis;

public Replayer(String filename) throws IOException
{
    zf = new ZipFile(filename);
}

public void openFile(String filename) throws Exception
{
    Enumeration e = zf.entries();
    boolean f = false;

    while (e.hasMoreElements())
    {
        ze = (ZipEntry) e.nextElement();
        if (ze.getName().equals(filename))
        {
            f = true;
            break;
        }
    }
    
    if (!f)
        throw new Exception("[Mario AI EXCEPTION] : File " + filename + " not found in the archive");
}

private void openInputStream() throws IOException
{
    fis = new BufferedInputStream(zf.getInputStream(ze));
}

public byte[] readBytes(int size) throws IOException
{
    if (fis == null)
        openInputStream();

    byte[] buffer = new byte[size];
    int count = fis.read(buffer, 0, size);

    if (count == -1)
        buffer = null;

    return buffer;
}

public Object readObject() throws IOException, ClassNotFoundException
{
    ObjectInputStream ois = new ObjectInputStream(zf.getInputStream(ze));
    Object res = ois.readObject();
    ois.close();

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
}