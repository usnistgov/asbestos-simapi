package gov.nist.asbestos.simapi.http

import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class gzip {

    static String zip(String s){
        def targetStream = new ByteArrayOutputStream()
        def zipStream = new GZIPOutputStream(targetStream)
        zipStream.write(s.getBytes('UTF-8'))
        zipStream.close()
        def zippedBytes = targetStream.toByteArray()
        targetStream.close()
        return zippedBytes.encodeBase64()
    }

    static String unzip(String compressed){
        def inflaterStream = new GZIPInputStream(new ByteArrayInputStream(compressed.decodeBase64()))
        def uncompressedStr = inflaterStream.getText('UTF-8')
        return uncompressedStr
    }
}
