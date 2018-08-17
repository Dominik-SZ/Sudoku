package swingGUI.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

// This class is mostly copied from:
// http://stackoverflow.com/questions/17904518/fit-scale-jcomponent-to-page-being-printed
// I made the modification to use the actual size of the component instead of
// its  preferred one and removed some unnecessary code.

/**
 * This Class can print the inserted component. By doing so, the component is
 * scaled to the maximum possible size, which still fits the printable space of
 * the paper. Its height/width ratio is maintained by doing so.
 */
public class ComponentPrinter implements Printable {

	private Component comp;

	public ComponentPrinter(Component comp) {
		this.comp = comp;
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageNumber) throws PrinterException {
		if (pageNumber > 0) {
			return Printable.NO_SUCH_PAGE;
		}

		// Get the actual size of the component
		Dimension compSize = comp.getSize();

		// Get the the print size
		Dimension printSize = new Dimension();
		printSize.setSize(pageFormat.getImageableWidth(), pageFormat.getImageableHeight());

		// Calculate the scale factor
		double scaleFactor = getScaleFactorToFit(compSize, printSize);

		// Calculate the scaled size...
		double scaledWidth = compSize.width * scaleFactor;
		double scaledHeight = compSize.height * scaleFactor;

		// Create a clone of the graphics context. This allows us to manipulate
		// the graphics context without being worried about the effects
		// it might have once we're finished
		Graphics2D graphicsClone = (Graphics2D) graphics.create();

		// Calculate the x/y position of the component, this will center
		// the result on the page if it can
		double x = ((pageFormat.getImageableWidth() - scaledWidth) / 2d) + pageFormat.getImageableX();
		double y = ((pageFormat.getImageableHeight() - scaledHeight) / 2d) + pageFormat.getImageableY();
		// Create a new AffineTransformation
		AffineTransform affineTransformation = new AffineTransform();
		// Translate the offset to center the picture on the page
		affineTransformation.translate(x, y);

		// Set the scaling
		affineTransformation.scale(scaleFactor, scaleFactor);
		// Apply the transformation
		graphicsClone.transform(affineTransformation);
		// Print the component
		comp.printAll(graphicsClone);
		// Dispose of the graphics context, freeing up memory and discarding
		// our changes
		graphicsClone.dispose();

		comp.revalidate();
		return Printable.PAGE_EXISTS;
	}

	/**
	 * This method calculates the factor, which should be used to scale the
	 * original component so it fits the toFit size, while being as big as
	 * possible.
	 * 
	 * @param originalSize
	 *            The original size of the component, which should be scaled
	 *            afterwards
	 * @param toFit
	 *            The size of the area in which the component should be scaled
	 *            into
	 * @return The factor which should be multiplied by the component which
	 *         should be scaled
	 */
	private double getScaleFactorToFit(Dimension originalSize, Dimension toFit) {

		double dScale = 1d;

		if (originalSize != null && toFit != null) {

			double dScaleWidth = toFit.getWidth() / originalSize.getWidth();
			double dScaleHeight = toFit.getHeight() / originalSize.getHeight();

			dScale = Math.min(dScaleHeight, dScaleWidth);

		}
		return dScale;
	}

}