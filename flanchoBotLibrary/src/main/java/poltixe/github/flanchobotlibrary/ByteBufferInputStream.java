package poltixe.github.flanchobotlibrary;

import java.io.*;
import java.nio.*;

public class ByteBufferInputStream extends InputStream {
    public int bbisInitPos;
    public int bbisLimit;
    public ByteBuffer bbisBuffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this(buffer, buffer.limit() - buffer.position());
    }

    public ByteBufferInputStream(ByteBuffer buffer, int limit) {
        bbisBuffer = buffer;
        bbisLimit = limit;
        bbisInitPos = bbisBuffer.position();
    }

    @Override
    public int read() throws IOException {
        if (bbisBuffer.position() - bbisInitPos > bbisLimit)
            return -1;
        return bbisBuffer.get();
    }
}
