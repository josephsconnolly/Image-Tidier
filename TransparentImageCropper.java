import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

public class TransparentImageCropper {

	public static void main(String[] args) {
				
		Random r = new Random();
		Scanner s = new Scanner(System.in);
		
		System.out.println("Enter filepath for uncropped image:");
		String filepath = s.nextLine();
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Image could not be read.");
		}
		
		if (image == null) {
			System.exit(0);
		}
		
		int leftBound = findBound(image.getWidth()-1, image.getHeight()-1, true, true, image);
		int rightBound = findBound(image.getWidth()-1, image.getHeight()-1, true, false, image);
		int topBound = findBound(image.getHeight()-1, image.getWidth()-1, false, true, image);
		int bottomBound = findBound(image.getHeight()-1, image.getWidth()-1, false, false, image);

		BufferedImage croppedImage = image.getSubimage(leftBound, topBound, (rightBound-leftBound), (bottomBound-topBound));
	
		try {
		    File outputfile = new File(filepath.substring(0, filepath.length()-4) + 
		    					"_cropped" + filepath.substring(filepath.length()-4));
		    ImageIO.write(croppedImage, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
			System.out.println("Cropped image could not be saved.");
		}
	}
	
	/* Finds the bounds for a given direction (top, right, bottom, left) depending
	 * on the values of a, b, and c. 
	 * 
	 * @param 	a	Either takes on value image.getHeight()-1, image.getWidth()-1, or 0
	 * @param  	b   Either takes on value image.getHeight()-1, image.getWidth()-1, or 0
	 * @param	c	Specifies direction from which we are taking bounds (true -> left/right)
	 * @param	d	Specifies direction from which we should iterate (true -> left to right)
	 * @param	img	The passed in BufferedImage to read
	 */
	public static int findBound(int a, int b, boolean c, boolean d, BufferedImage img) {
		
		for(int j = 0; j < a; j++) {
					
			for(int k = 0; k < b; k++) {
					
				if(c) {
					if(d) {
						if(!isTransparent(img.getRGB(j,k))) {
							return (j);
						} 
					} else {
						if(!isTransparent(img.getRGB(a-j,k))) {
							return (b-j);
						}
					}
						
				}
				else {
					if(d) {
						if(!isTransparent(img.getRGB(k,j))) {
							return (j);
						} 
					} else {
						if(!isTransparent(img.getRGB(k,a-j))) {
							return (b-j);
						}
					}
				}

				
			}
					
		}
				
		return -1;
		
	}
		
	
	
	/* Takes in a pixel's color information in form TYPE_INT_ARGB,
	 * reads only the alpha channel, and returns true if the pixel 
	 * is transparent and false otherwise. 
	 * 
	 * @param 	pixelRGB	A pixel's color information
	 * @return	boolean		True if transparent, False otherwise
	 */
	public static boolean isTransparent(int pixelRGB) {
			 	
		if( (pixelRGB >> 24) == 0x00 ) {
			return true;
		}
		  
		return false;
	}

}
