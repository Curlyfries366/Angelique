import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WaveRIFF extends EditTab
{
private Editor _parent = null;
private File _srcFile = null;
private Canvas _canvas = null;
private short[] _iChn1SampleOffsets = null;
private short[] _iChn2SampleOffsets = null;
private double[] _dChn1SampleOffsets = null;
private double[] _dChn2SampleOffsets = null;
private int _numChannels;
private int _sampleRate;
private int _sampleBytes;
private int _numSamples;
private Point[] _chn1Points;
private Point[] _chn2Points;
// May not actually want these vars to be long
// Java doesn't like making arrays that big, so if you start finding yourself
//casting a bunch of these to int then I'd just change them
// Just have to hope that we don't encounter any wav files big enough to need
//longs
// If push came to shove, you could get away with using a HashMap<Long,
//Double> to handle each channel
private long _wavFileTotalSize = 0;
private int _wavDataChunkSize = 0;
private long _wavDataStartAddr = 0;
private long _wavDataPostAddr = 0;
private final int _drawMult = 16;


public WaveRIFF(Editor parent, File wavFile) {
	_parent = parent;
_srcFile = wavFile;
JMenuBar menuBar = new JMenuBar();
JMenu fileMenu = new JMenu("Wave");
JMenuItem writeMenuItem = new JMenuItem("Save WAV File");
writeMenuItem.addActionListener(event -> {
JFileChooser chooser = new JFileChooser();
chooser.setCurrentDirectory(new
File(System.getProperty("user.dir")));
chooser.setAcceptAllFileFilterUsed(true);
FileFilter filter = new FileFilter()
{
	
@Override
public boolean accept(File file)
{
return
file.getName().trim().toLowerCase().endsWith(".wav");
}

@Override
public String getDescription()
{
return "Wave Audio File";
}
};

chooser.addChoosableFileFilter(filter);
chooser.setFileFilter(filter);
int option = chooser.showOpenDialog(this);
if (option != JFileChooser.APPROVE_OPTION) {
return;
}
final File filePath = chooser.getSelectedFile();
write(filePath);
});
fileMenu.add(writeMenuItem);
JMenuItem closeMenuItem = new JMenuItem("Close Editor");
closeMenuItem.addActionListener(event -> {
parent.closeTab(this);
});
fileMenu.add(closeMenuItem);
menuBar.add(fileMenu);
JMenu editMenu = new JMenu("Edit");
JMenuItem f1MenuItem = new JMenuItem("Identity");
f1MenuItem.addActionListener(event -> {
filter1();
});
editMenu.add(f1MenuItem);
JMenuItem f2MenuItem = new JMenuItem("Fade-In");
f2MenuItem.addActionListener(event -> {
filter2();
});
editMenu.add(f2MenuItem);
JMenuItem f3MenuItem = new JMenuItem("Fade-Out");
f3MenuItem.addActionListener(event -> {
filter3();
});
editMenu.add(f3MenuItem);
menuBar.add(editMenu);
setJMenuBar(menuBar);
_canvas = new Canvas();
add(_canvas);
}

public void parse() {
new Thread(() -> {
try {
// In Java, opening a file to read in byte mode is done via
	//the
// RandomAccessFile class
RandomAccessFile raf = new RandomAccessFile(_srcFile, "r");
// The WAV format header is 44 bytes
// The RAF.read() method fills a buffer of byte[] with
//buffer.length
// of bytes, starting at the current file pointer
// Once the buffer is filled, the file pointer is
//incremented to match
byte[] wavHeader = new byte[44];
raf.read(wavHeader);
int index = 0;
// Every file format I'm familiar with has a 2-8-byte ASCII
//string
// starting at file pointer value 0x00, which identifies
//the type of data in the file
// In the case of most audio files, that identifier is
//"RIF"
String riff = bytesToString(wavHeader, index, 4);
if (_parent._debugMode)
{
System.out.println(riff);
}
index += 4;
// Pretty common to see a file size or header size int
//value near the top of the file
// This one is the full file size in bytes
int fileSize = bytesToInt(wavHeader, index, 4, true);
if (_parent._debugMode)
{
System.out.printf("Total file size: %d%n", fileSize +
44);
}
index += 4;
// Because many audio formats have the RIFF identifier at
//position 0x00, they need a
// sub-format specifier as well, in this case "WAVEfmt "
String WAVEfmt = bytesToString(wavHeader, index, 8);
if (_parent._debugMode)
{
System.out.println(WAVEfmt);
}
index += 8;
// The format specification lists this 32-bit int value as
//a format value field
// It appears to always be exactly 0x00000010, aka 16
// I have not been able to determine what this signifies or
//why it is useful information
int fmtLen = bytesToIntLE(wavHeader, index, 4);
if (_parent._debugMode)
{
System.out.printf("Length of format data: %d%n",
fmtLen);
}
index += 4;
// Then there's these 2 bytes of some other format
//specifier
// I think this one is always exactly 0x0001
int format = bytesToIntLE(wavHeader, index, 2);
if (_parent._debugMode)
{
System.out.printf("Format type code: %d%n", format);
}
index += 2;
// Some actual useful information: the number of audio
//channels
// A 1 here means mono, a 2 means stereo
// I think more advanced formats than wav can have numbers
//above 2
_numChannels = bytesToIntLE(wavHeader, index, 2);
if (_parent._debugMode)
{
System.out.printf("Number of channels: %d%n",
_numChannels);
}
index += 2;
// The sample rate is the number of absolute wave amplitude
//readings per channel,
// per second
// Values here are generally 42000, 44100, 48000, etc.
_sampleRate = bytesToIntLE(wavHeader, index, 4);
if (_parent._debugMode)
{
System.out.printf("Sample rate: %dHz%n",
_sampleRate);
}
index += 4;
// This is the total number of bytes in the file per second
// It can be calculated from sampleRate * numChannels *
//bytesPerSample
// Because we have four+ numbers stored in the file but
//together they only
// represent three meaningful variables, we can use the
//stored values as a
// sanity check
int bitRate = bytesToIntLE(wavHeader, index, 4);
if (_parent._debugMode)
{
System.out.printf("Total byte rate: %dBps%n",
bitRate);
}
index += 4;
// Another value that can be calculated from other known
//values
// This one seems to just be intended as an easy way to
//generate the below
// String representation of the mode
int modeCode = bytesToIntLE(wavHeader, index, 2);
String mode = "";
switch (modeCode)
{
case 1:
mode = "8-bit mono";
break;
case 2:
mode = _numChannels == 1 ? "16-bit mono" : "8-bit mode" ;
break;
case 4:
mode = "16-bit stereo";
break;
default:
mode = "unknown";
break;
}
if (_parent._debugMode)
{
System.out.printf("Playback mode: %s%n", mode);
}
index += 2;
// The final variable we actually care about
// Real wav format audio should always have 0x0010 as the
//value here
// Indicating 16 bits (one Java short) per sample
// Keep in mind that these samples are stored as signed 2-
//byte int values, but
// are intended to be interpreted as a fraction of the
//maximum amplitude
// value (96 decibels for standard wav files) and thus
//must be normalized
// against the max possible (signed) value and the max
//amplitude to get the
// actual dB offset of the waveform relative to 0dB
int sampleBits = bytesToIntLE(wavHeader, index, 2);
_sampleBytes = sampleBits/8;
if (_parent._debugMode)
{
System.out.printf("Bytes per sample: %d%n",
_sampleBytes);
}
index += 2;
// A lot of file formats will have fields in the data that
//do nothing more than
// specify where a given field/region of the data begins
// This field identifier tells us where the data region
//begins
String dataChunk = bytesToString(wavHeader, index, 4);
index += 4;
// This 32-bit int tells us how many bytes of data to read
//out of the file
// The data starts on the next file pointer value after
//this int
_wavDataChunkSize = bytesToIntLE(wavHeader, index, 4);
if (_parent._debugMode)
{
System.out.printf("Data size: %d%n",
_wavDataChunkSize);
}
index += 4;
// Create a buffer of size dataSize and read all the data
//bytes in from the
// file reader
_wavDataStartAddr = raf.getFilePointer();
byte[] wavData = new byte[_wavDataChunkSize];
raf.read(wavData);
_wavDataPostAddr = raf.getFilePointer();
// The maxValue for a 2-byte signed short is 32767, which
//corresponds
// to the maximum allowed amplitude of our waveform of
//+/-96dB
// We generate these max and min values so that we can
//normalize the
// large-ish int values we're getting from each sample to
//a scaled
// vertical offset from a point on the screen, or to an
//absolute
// amplitude value in dB
double maxValue = Math.pow(2, (sampleBits-1))-1;
double minValue = -1*(maxValue+1);
// Do a bit of math here to make things easier later
// We want to know how long (in seconds) the file actually
//is
// We can find out the length, we can divide total data
//bytes / bytes per second
double seconds = (double)_wavDataChunkSize /
(double)bitRate;
if (_parent._debugMode)
{
System.out.printf("There are %.2f seconds of audiodata%n", seconds);
}
// We want to know the total number of samples (per single
//channel)
// That one is just total data byte / (number of channels *
//bytes per channel)
_numSamples = _wavDataChunkSize /
(_numChannels*_sampleBytes);
if (_parent._debugMode)
{
System.out.printf("There are %d total samples%n",
_numSamples);
}
_iChn1SampleOffsets = new short[_numSamples];
_iChn2SampleOffsets = new short[_numSamples];
_dChn1SampleOffsets = new double[_numSamples];
_dChn2SampleOffsets = new double[_numSamples];
// Regardless of draw mode, we actually need to get every
//sample
// Since we declared (and allocated) our arrays to store
//them earlier, we can
// use a simple counter to keep track of our array index
int smpCount = 0;
for(int idx = 0; idx < _wavDataChunkSize; idx +=
_sampleBytes*_numChannels)
{
// Get our samples from both channels and store them
// Note that if we swap to double arrays, we have to
//normalize them here
int chn1offset = bytesToIntLE(wavData, idx,
_sampleBytes);
_iChn1SampleOffsets[smpCount] = (short)chn1offset;
int chn2offset = bytesToIntLE(wavData,
idx+_sampleBytes, _sampleBytes);
_iChn2SampleOffsets[smpCount] = (short)chn2offset;
_dChn1SampleOffsets[smpCount] = chn1offset > 0 ?
(chn1offset / maxValue) : (chn1offset / minValue);
_dChn2SampleOffsets[smpCount] = chn2offset > 0 ?
(chn2offset / maxValue) : (chn2offset / minValue);
smpCount++;
}
buildWaveform();
} catch (FileNotFoundException fnfEx) {
fnfEx.printStackTrace();
} catch (IOException ioEx) {
ioEx.printStackTrace();
}
}).start();
}

public void write(File filePath) {
    new Thread(() -> {
        try {
            // Ensure the file has a .wav extension
            File finalFilePath = filePath;
            if (!finalFilePath.getName().toLowerCase().endsWith(".wav")) {
                finalFilePath = new File(finalFilePath.getAbsolutePath() + ".wav");
            }

            RandomAccessFile originalFile = new RandomAccessFile(_srcFile, "r");
            RandomAccessFile outputFile = new RandomAccessFile(finalFilePath, "rw");

            // Copy WAV header
            byte[] wavHeader = new byte[44];
            originalFile.read(wavHeader);
            outputFile.write(wavHeader);

            // Copy modified audio data
            originalFile.seek(_wavDataStartAddr);
            byte[] modifiedData = new byte[(int) (_wavDataPostAddr - _wavDataStartAddr)];
            // Assuming that the modified data is stored in _iChn1SampleOffsets and _iChn2SampleOffsets
            for (int idx = 0; idx < _numSamples; idx++) {
                modifiedData[idx * _sampleBytes * _numChannels] = (byte)(_iChn1SampleOffsets[idx] & 0xFF);
                modifiedData[idx * _sampleBytes * _numChannels + 1] = (byte)((_iChn1SampleOffsets[idx] >> 8) & 0xFF);
                modifiedData[idx * _sampleBytes * _numChannels + _sampleBytes] = (byte)(_iChn2SampleOffsets[idx] & 0xFF);
                modifiedData[idx * _sampleBytes * _numChannels + _sampleBytes + 1] = (byte)((_iChn2SampleOffsets[idx] >> 8) & 0xFF);
            }
            outputFile.write(modifiedData);

            // Close files
            originalFile.close();
            outputFile.close();
        } catch (FileNotFoundException fnfEx) {
            fnfEx.printStackTrace();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }).start();
}


public void draw() {
new Thread(() -> {
if (_chn1Points == null || _chn2Points == null) {
return;
}
Graphics2D g2d = (Graphics2D) _canvas.getGraphics();
g2d.setColor(new Color(0xa0, 0xa0, 0xa0));
g2d.fillRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
int cnvCenterLine = _canvas.getHeight() / 2;
int topCenterLine = cnvCenterLine / 2;
int botCenterLine = cnvCenterLine + topCenterLine;
g2d.setColor(new Color(0x00, 0x00, 0x00));
g2d.drawLine(0, topCenterLine, _canvas.getWidth(),
topCenterLine);
g2d.drawLine(0, botCenterLine, _canvas.getWidth(),
botCenterLine);
g2d.drawLine(0, cnvCenterLine, _canvas.getWidth(),
cnvCenterLine);
g2d.setColor(new Color(0x00, 0x00, 0xc0));

//Draw for _chn1Points and _chn2Points
for (int i = 0; i < Math.min(_chn1Points.length, _chn2Points.length) - 1; i++) {
    Point start1 = _chn1Points[i];
    Point end1 = _chn1Points[i + 1];

    Point start2 = _chn2Points[i];
    Point end2 = _chn2Points[i + 1];

    if (start1 != null && end1 != null) {
        g2d.drawLine(start1.x, start1.y, end1.x, end1.y);
    } else {
        System.err.println("Null points in _chn1Points at index: " + i);
    }

    if (start2 != null && end2 != null) {
        g2d.drawLine(start2.x, start2.y, end2.x, end2.y);
    } else {
        System.err.println("Null points in _chn2Points at index: " + i);
    }
}

}).start();
}

private void buildWaveform() {
if (_parent._debugMode) {
System.out.println("Beginning building waveform");
}
int cnvCenterLine = _canvas.getHeight() / 2;
int topCenterLine = cnvCenterLine / 2;
int botCenterLine = cnvCenterLine + topCenterLine;
int drawPitch = (_dChn1SampleOffsets.length / _canvas.getWidth()) /
_drawMult;
int numPoints = (_numSamples / drawPitch) + 1;
_chn1Points = new Point[numPoints];
_chn2Points = new Point[numPoints];
for (int smpIdx = 0, ptIdx = 0; smpIdx < _numSamples; smpIdx +=
drawPitch, ptIdx++) {
// The (smpIdx * width) calculation quickly overflows the int
//limit, so we have to cast it to long, do the division, and then cast back to int
int x = (int)(((long)smpIdx * _canvas.getWidth()) / _numSamples);
int y = _iChn1SampleOffsets[smpIdx] > 0.0 ? (int)(topCenterLine -
(topCenterLine * _dChn1SampleOffsets[smpIdx])) : (int)(topCenterLine +
(topCenterLine * _dChn1SampleOffsets[smpIdx]));
_chn1Points[ptIdx] = new Point(x, y);
y = _iChn2SampleOffsets[smpIdx] > 0.0 ? (int)(botCenterLine -
(topCenterLine * _dChn1SampleOffsets[smpIdx])) : (int)(botCenterLine +
(topCenterLine * _dChn1SampleOffsets[smpIdx]));
_chn2Points[ptIdx] = new Point(x, y);
}
if (_parent._debugMode) {
System.out.println("Finished building waveform");
}
draw();
}

public void applyFilter(Runnable filterOp) {
	// Accepts a Runnable lambda and executes it in a new thread
			new Thread(filterOp).start();
}

public void filter1() {
	// Identity filter
	applyFilter(() -> {
		for (int idx = 0; idx < _iChn1SampleOffsets.length; idx++) {
			_iChn1SampleOffsets[idx] *= 1;
			_iChn2SampleOffsets[idx] *= 1;
			_dChn1SampleOffsets[idx] *= 1.0;
			_dChn2SampleOffsets[idx] *= 1.0;
		}
		buildWaveform();
	});
}

public void filter2() {
	// fade in filter
	applyFilter(() -> {
		 for (int idx = 0; idx < _iChn1SampleOffsets.length; idx++) {
	            double scaleFactor = (double) idx / _iChn1SampleOffsets.length;
	            _iChn1SampleOffsets[idx] *= scaleFactor;
	            _iChn2SampleOffsets[idx] *= scaleFactor;
	            _dChn1SampleOffsets[idx] *= scaleFactor;
	            _dChn2SampleOffsets[idx] *= scaleFactor;
	        }
	            buildWaveform();
			});
}

public void filter3() {
	// fade out filter
			applyFilter(() -> {
				for (int idx = 0; idx < _iChn1SampleOffsets.length; idx++) {
		            double scaleFactor = 1.0 - ((double) idx / _iChn1SampleOffsets.length);
		            _iChn1SampleOffsets[idx] *= scaleFactor;
		            _iChn2SampleOffsets[idx] *= scaleFactor;
		            _dChn1SampleOffsets[idx] *= scaleFactor;
		            _dChn2SampleOffsets[idx] *= scaleFactor;
		        }
		        buildWaveform();
			});
}
}