import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Bitmap extends EditTab {
	private Editor _parent = null;
	private File _srcFile = null;
	private Canvas _canvas = null;
	private Color[][] _colorPixelArray = null;
	private int _imgWidth = 0;
	private int _imgHeight = 0;
	private int _numPixels = 0;
// May not actually want these vars to be long
// Java doesn't like making arrays that big, so if you start finding yourself
//casting a bunch of these to int then I'd just change them
// Just have to hope that we don't encounter any bmp files big enough to need
//longs
// If push came to shove, you could get away with using a HashMap<Point,
//Color> to key pixel data to an x,y coordinate
	private long _imgFileTotalSize = 0;
	private long _imgDataStartAddr = 0;
	private long _imgDataPostAddr = 0;

	public Bitmap(Editor parent, File bmpFile) {
		_parent = parent;
		_srcFile = bmpFile;
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Bitmap");
		JMenuItem writeMenuItem = new JMenuItem("Save BMP File");
		writeMenuItem.addActionListener(event -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setAcceptAllFileFilterUsed(true);
			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().trim().toLowerCase().endsWith(".bmp");
				}

				@Override
				public String getDescription() {
					return "Bitmap Image File";
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
		JMenuItem f2MenuItem = new JMenuItem("Blur");
		f2MenuItem.addActionListener(event -> {
			filter2();
		});
		editMenu.add(f2MenuItem);
		JMenuItem f3MenuItem = new JMenuItem("Gaussian Blur");
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
// The BMP format header is 14 bytes
// The RAF.read() method fills a buffer of byte[] with
//buffer.length
// of bytes, starting at the current file pointer
// Once the buffer is filled, the file pointer is
//incremented to match
byte[] bmpHeader = new byte[14];
raf.read(bmpHeader);
int index = 0;
// Every file format I'm familiar with has a 2-8-byte ASCII
//string
// starting at file pointer value 0x00, which identifies
//the type of data in the file
// In the case of a windows-format bitmap, that identifier
//is "BM"
String magic = bytesToString(bmpHeader, index, 2);
if (_parent._debugMode)
{
System.out.println(magic); // Should be "BM"
}
index += 2;
// Pretty common to see a file size or header size int
//value near the top of the file
// This one is the full file size in bytes
int _imgFileTotalSize = bytesToIntLE(bmpHeader, index, 4);
if (_parent._debugMode)
{
System.out.println(_imgFileTotalSize);
}
index += 4;
// The bmp format has two 2-byte reserved blocks here (file
//ptr 0x06)
// These will generally be unused space (i.e. left as
//0x0000)
// No matter what, a general-purpose parser can ignore
//these values entirely
int reserved = bytesToIntLE(bmpHeader, index, 4);
if (_parent._debugMode)
{
System.out.println(reserved);
}
index += 4;
// The first field of data we're actually interested in,
//this is the address (file ptr)
// of the first byte (pixel color data) where the image
// begins
int _imgDataStartIndex = bytesToIntLE(bmpHeader, index, 4);
if (_parent._debugMode)
{
//System.out.printf("File offset of first pixel data:
//%d%n", _imgDataStartIndex); 
}
index += 4;
// There is a second block in the header from which we need
//a couple of things
// The bmp format allows this header to vary in size, so
//they put the size of the
// header in the first 4 bytes of the header, allowing us
//to grab them,
// parse them, and then make a buffer to hold the rest of
//this header data
byte[] dibHeaderSize = new byte[4];
raf.read(dibHeaderSize);
int dibHdrSize = bytesToIntLE(dibHeaderSize, 0, 4);
// The size int includes its own 4 bytes, so when we create
//our buffer for the
// remainder of the header, we need to subtract those
//from the buffer size
byte[] dibHeader = new byte[dibHdrSize-4];
raf.read(dibHeader);
index = 0;
// The next 4 bytes are the image width in pixels
// This and the following 4 bytes - the height in pixels -
//are the last two pieces
// of information we need to successfully render the
//image raster
_imgWidth = bytesToIntLE(dibHeader, index, 4);
if (_parent._debugMode)
{
System.out.printf("Image width: %d%n", _imgWidth);
}
index += 4;
_imgHeight = bytesToIntLE(dibHeader, index, 4);
if (_parent._debugMode)
{
System.out.printf("Image height: %d%n", _imgHeight);
}
index += 4;
_numPixels = _imgWidth * _imgHeight;
// Not even sure what this is used for
int colorPlanes = bytesToIntLE(dibHeader, index, 2);
index += 2;
// In old, low-quality applications bit depths of less than
//24 bits per pixel
// were used, primarily due to hardware limitations
// These days, it's safe to assume that any bitmap will be
//using 24-bit "color depth"
// That is to say, one byte per color in RGB mode: 3 colors
//* 8 bits per color = 24 bits
// The file data actually gives us bits per pixel, so we
//divide it by 8 to get bytes
int bytesPerPx = bytesToIntLE(dibHeader, index, 2) / 8;
if (_parent._debugMode)
{
System.out.printf("Bytes per pixel: %d%n",
bytesPerPx);
}
index += 2;
_colorPixelArray = new Color[_imgHeight][_imgWidth];
// Iterate through the pixel data
for (int y = 0; y < _imgHeight; y++)
{
for (int x = 0; x < _imgWidth; x++)
{
// We use our bytesPerPx value here to set the
//size of our buffer
// Then we use that buffer to get the data for
//a single pixel at a time
byte[] pixel = new byte[bytesPerPx];
raf.read(pixel);
// The pixel data is a single byte each for
//red, green, and blue color values,
// in that order
// Note that since Java does not allow unsigned
//values, we have to correct the
// negative values to be correct: we do this
//by adding 256 if the number is negative
int R = bytesToIntLE(pixel, 2, 1);
R = R < 0 ? R + 256 : R;
int G = bytesToIntLE(pixel, 1, 1);
G = G < 0 ? G + 256 : G;
int B = bytesToIntLE(pixel, 0, 1);
B = B < 0 ? B + 256 : B;
// We create our new color, get the current
//graphics object from the canvas,
// and fill a rectangle on the canvas with
//our color
// The rectangle's top left corner is defined
//by the current x and y coordinates
// While its width and height are simply 1
//pixel each (times our scale factor)
Color c = new Color(R, G, B);
_colorPixelArray[y][x] = c;
}
}
_imgDataPostAddr = raf.getFilePointer();
draw();
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
            // Ensure the file has a .bmp extension
            final File finalFilePath;
            if (!filePath.getName().toLowerCase().endsWith(".bmp")) {
                finalFilePath = new File(filePath.getAbsolutePath() + ".bmp");
            } else {
                finalFilePath = filePath;
            }

            RandomAccessFile originalFile = new RandomAccessFile(_srcFile, "r");
            RandomAccessFile outputFile = new RandomAccessFile(finalFilePath, "rw");

            // Copy BMP header
            byte[] bmpHeader = new byte[14];
            originalFile.read(bmpHeader);
            outputFile.write(bmpHeader);

            // Read and copy DIB header
            byte[] dibHeaderSize = new byte[4];
            originalFile.read(dibHeaderSize);
            int dibHdrSize = bytesToIntLE(dibHeaderSize, 0, 4);
            byte[] dibHeader = new byte[dibHdrSize - 4];
            originalFile.read(dibHeader);
            outputFile.write(dibHeader);

            // Copy pixel data
            originalFile.seek(_imgDataStartAddr);
            byte[] pixelData = new byte[(int) (_imgFileTotalSize - _imgDataStartAddr)];
            originalFile.read(pixelData);
            outputFile.write(pixelData);

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
			Graphics2D g2d = (Graphics2D) _canvas.getGraphics();
			g2d.setColor(new Color(0xa0, 0xa0, 0xa0));
			g2d.fillRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
// Iterate through the pixel data
			for (int y = 0; y < _imgHeight; y++) {
				for (int x = 0; x < _imgWidth; x++) {
// We want to fill a rectangle on the canvas with the
//color value of the current pixel
// The rectangle's top left corner is defined by the
//current x and y coordinates
// While its width and height are simply 1 pixel each
					g2d.setColor(_colorPixelArray[y][x]);
// Note that, for some reason, the bitmap format
//stores pixels upside-down relative
// to how every other standard handles things
// To compensate, instead of drawing the rect at x, y
//we have to draw it at
// x, (image height - y) to have it be right side
//up
					g2d.fillRect(x, (_imgHeight - y), 1, 1);
				}
			}
		}).start();
	}
	private void applyKernel(int[][] kernel, double mult) {
		for (int imgY = 0; imgY < _imgHeight; imgY++)
		{
		for (int imgX = 0; imgX < _imgWidth; imgX++)
		{
		double redSum = 0.0;
		double grnSum = 0.0;
		double bluSum = 0.0;
		for (int subY = imgY-1, krnY = 0; subY <= imgY+1; subY++,
		krnY++) {
		for (int subX = imgX-1, krnX = 0; subX <= imgX+1;
		subX++, krnX++) {
		if (subY < 0 || subY >= _imgHeight || subX < 0
		|| subX >= _imgWidth) {
		continue; }
		redSum += _colorPixelArray[subY][subX].getRed()
				* kernel[krnY][krnX];
				grnSum += _colorPixelArray[subY]
				[subX].getGreen() * kernel[krnY][krnX];
				bluSum += _colorPixelArray[subY]
				[subX].getBlue() * kernel[krnY][krnX];
				}
				}
				_colorPixelArray[imgY][imgX] = new Color((int)(redSum *
				mult), (int)(grnSum * mult), (int)(bluSum * mult), _colorPixelArray[imgY]
				[imgX].getAlpha()); }
		}
		draw();

	}

	public void filter1() {
		new Thread(() -> {
  //Identity kernel
			int[][] kernel = {
					{ 0, 0, 0 }, 
					{ 0, 1, 0 },
					{ 0, 0, 0 } };
			double mult = 1.0;
			applyKernel(kernel, mult);
		}).start();
	}

	public void filter2() {
		new Thread(() -> {
			//blur kernel
			int[][] kernel = {
	                {1, 1, 1},
	                {1, 1, 1},
	                {1, 1, 1}
	        };
			double mult = 1.0/9.0;
			applyKernel(kernel, mult);
		}).start();
	}

	public void filter3() {
		new Thread(() -> {
			//gaussian blur
	        int[][] kernel = {
	                {1, 2, 1},
	                {2, 4, 2},
	                {1, 2, 1}
	        };
	        double mult = 1.0/16.0;
			applyKernel(kernel, mult);
		}).start();
	}
}
