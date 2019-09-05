package com.xyt.resource.utill;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 * @author 梁昊
 * @date 2019/7/5
 * @function 循环冗余算法
 * @editLog
 */
public class LHCRC32 {
    private final int STREAM_BUFFER_LENGTH = 1024;

    public long encrypt(Object data) {
        return encrypt(data.toString().getBytes());
    }

    public long encrypt(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

//    InputStrem is = new ByteArrayInputStream(str.getBytes());
//    或者
//    ByteArrayInputStream stream= new ByteArrayInputStream(str.getBytes());
    public long encrypt(InputStream data) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        CRC32 crc32 = new CRC32();
        while (read > -1) {
            crc32.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }
        return crc32.getValue();
    }
}
