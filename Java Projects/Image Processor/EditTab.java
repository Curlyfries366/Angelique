import javax.swing.JFrame;
import java.io.File;
public abstract class EditTab extends JFrame
{
public abstract void parse();
public abstract void write(File filePath);
public abstract void draw();
/**
* <h1>bytesToString</h1>
* @param bytes An array of byte containing the data we wish to convert
* @param offset The index of the first byte in the array to be converted
* @param length The number of bytes from the array to include in the
coversion
* @return Returns the completed String
*/
protected static String bytesToString(byte[] bytes, int offset, int length)
{
try {
String result = "";
for (int i = 0; i < length; i++) {
result += (char)bytes[offset + i];
}
return result;
} catch (ArrayIndexOutOfBoundsException oobEx) {
return null;
}
}
/**
* <h1>bytesToInt</h1>
* @param bytes An array of byte containing the data we wish to convert
* @param offset The index of the first byte in the array to be converted
* @param length The number of bytes from the array to include in the
coversion
* @param littleEndian The endianness (byte order) of the bytes
* @return
*/
protected static int bytesToInt(byte[] bytes, int offset, int length, boolean
littleEndian) {
return (littleEndian ? bytesToIntLE(bytes, offset, length) :
bytesToIntBE(bytes, offset, length));
}
/**
* Converts a little-endian sequence of bytes to an {@code int}.
*
* @param bytes the byte sequence that is to be converted.
* @param offset the offset to {@code bytes} at which the sequence starts.
* @param length the length of the byte sequence.
* @return the {@code int} that results from the conversion of the byte
sequence.
* @since 1.0
* @see #bytesToIntBE(byte[], int, int)
*/
protected static int bytesToIntLE(byte[] bytes, int offset, int length) {
int i = offset + length;
int value = bytes[--i];
while (--i >= offset) {
value <<= 8;
value |= bytes[i] & 0xFF;
}
return value;
}
/**
* Converts a big-endian sequence of bytes to an {@code int}.
*
* @param bytes the byte sequence that is to be converted.
* @param offset the offset to {@code bytes} at which the sequence starts.
* @param length the length of the byte sequence.
* @return the {@code int} that results from the conversion of the byte
sequence.
* @since 1.0
* @see #bytesToIntLE(byte[], int, int)
*/
protected static int bytesToIntBE(byte[] bytes, int offset, int length) {
int endOffset = offset + length;
int value = bytes[offset];
while (++offset < endOffset) {
value <<= 8;
value |= bytes[offset] & 0xFF;
}
return value;
}
}

