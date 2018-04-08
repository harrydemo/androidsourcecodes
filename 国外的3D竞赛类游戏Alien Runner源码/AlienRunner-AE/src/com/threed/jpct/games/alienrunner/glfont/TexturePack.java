package com.threed.jpct.games.alienrunner.glfont;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;

/**
 * packs several arbitrary sized images into a jPCT texture. 
 * the created texture is 2^n x 2^m as  required by jPCT. useful for
 * blitting purposes.
 * 
 * <p>see <a href='http://www.blackpawn.com/texts/lightmaps/default.html'
 * target='_blank'>this page</a> for an explanation of packing algorithm</p>
 * 
 * @author hakan eryargi (r a f t)
 */
public class TexturePack {
	/** use alpha when creating texture */
	public static boolean ALPHA_USE = true;
	/** do not use alpha when creating texture */
	public static boolean ALPHA_DONT_USE = false;

	private static final boolean DEBUG = false;

	private final Bitmap.Config bitmapConfig;
	private boolean packed = false;
	private final Point blittedSize = new Point();

	private final List<Entry> entries = new ArrayList<Entry>();
	private Texture texture = null;

	/**
	 * creates an TexturePack using Bitmap.Config.ARGB_8888 as bitmap config
	 * 
	 * @see Bitmap.Config#ARGB_8888
	 */
	public TexturePack() {
		this(Bitmap.Config.ARGB_8888);
	}

	/**
	 * creates an TexturePack
	 * 
	 * @param imageType must be one of the image types defined in
	 *            {@link BufferedImage} class
	 * @see BufferedImage
	 */
	public TexturePack(Bitmap.Config bitmapConfig) {
		this.bitmapConfig = bitmapConfig;
	}

	/**
	 * add image to be included in pack.
	 * 
	 * @return image id which can later be used for blit(..) methods
	 */
	public int addImage(Bitmap image) {
		checkPacked();

		if (image == null)
			throw new NullPointerException();
		if (image.getWidth() <= 0 || image.getHeight() <= 0)
			throw new IllegalArgumentException("width and height must be positive");

		this.entries.add(new Entry(image));
		return entries.size() - 1;
	}

	/**
	 * packs images into an 2^n x 2^m size d image. after packing clears all
	 * references to given images hence this method can only be called once
	 * 
	 * @return created image
	 */
	private Bitmap packImage() {
		checkPacked();

		if (entries.isEmpty())
			throw new IllegalStateException("nothing to pack");

		int maxWidth = 0;
		int maxHeight = 0;
		int totalArea = 0;

		for (Entry entry : entries) {
			int width = entry.image.getWidth();
			int height = entry.image.getHeight();

			if (width > maxWidth)
				maxWidth = width;
			if (height > maxHeight)
				maxHeight = height;

			totalArea += width * height;
		}

		Point size = new Point(closestTwoPower(maxWidth), closestTwoPower(maxHeight));
		boolean fitAll = false;
		if (DEBUG)
			System.out.println("initial size " + size);

		loop: while (!fitAll) {
			int area = size.x * size.y;
			if (area < totalArea) {
				nextSize(size);
				if (DEBUG)
					System.out.println("enlarging to " + size);
				continue;
			}

			Node root = new Node(size.x, size.y);
			for (Entry entry : entries) {
				Node inserted = root.insert(entry);
				if (inserted == null) {
					nextSize(size);
					if (DEBUG)
						System.out.println("couldnt fit, enlarging to " + size);
					continue loop;
				}
			}
			fitAll = true;
			if (DEBUG)
				printTree(root, "", 0);
		}

		Bitmap image = Bitmap.createBitmap(size.x, size.y, bitmapConfig);
		Canvas g2d = new Canvas(image);
		for (Entry entry : entries) {
			g2d.drawBitmap(entry.image, entry.bounds.left, entry.bounds.top, null);
			entry.image = null;
		}
		//g2d.dispose();
		packed = true;
		// if (DEBUG) raft.karga.gui.Swing.showImage("texture pack", image);

		return image;
	}

	/**
	 * packs image, creates a Texture out of it and adds texture to
	 * TextureManager.
	 * 
	 * @param useAlpha will created texture use alpha ?
	 * @return created texture
	 * 
	 * @see #ALPHA_USE
	 * @see #ALPHA_DONT_USE
	 */
	public Texture pack(boolean useAlpha) {
		if (texture != null)
			return texture;

		Bitmap image = packImage();
		texture = new Texture(image, useAlpha);
		texture.keepPixelData(true);
		texture.setMipmap(false);

		return texture;
	}

	/**
	 * blits one of packed images completely without scaling and with 
	 * transparency or additional color. 
	 * see {@link FrameBuffer#blit(Texture, int, int, int, int, int, int, int, int, int, boolean, Color)
	 * FrameBuffer.blit(..)} for description of other parameters 
	 *
	 * @param buffer the FrameBuffer
	 * @param imageId id of packed image as returned by addImage(..)
	 * 
	 * @return size of blitted image. note this method always returns the same
	 *         array instance
	 * @see FrameBuffer#blit(Texture, int, int, int, int, int, int, int, int, int, boolean, Color)
	 *      FrameBuffer.blit(..)
	 */
	public Point blit(FrameBuffer buffer, int imageId, int destX, int destY, 
			int transparency, boolean additive, RGBColor color) {
		
		if (texture == null)
			throw new IllegalStateException("not packed yet");

		Entry entry = entries.get(imageId);
		return blit(buffer, entry, destX, destY, entry.bounds.width(), 
				entry.bounds.height(), transparency, additive, color);
	}

	/**
	 * blits one of packed images completely with scaling or transparency or additional color 
	 * see {@link FrameBuffer#blit(Texture, int, int, int, int, int, int, int, int, int, boolean, Color)
	 * FrameBuffer.blit(..)} for description of other parameters
	 *  
	 * @param buffer the FrameBuffer
	 * @param imageId id of packed image as returned by addImage(..)
	 * 
	 * @return size of blitted image. note this method always returns the same
	 *         array instance
	 * @see FrameBuffer#blit(Texture, int, int, int, int, int, int, int, int, int, boolean, Color)
	 *      FrameBuffer.blit(..)
	 */
	public Point blit(FrameBuffer buffer, int imageId, int destX, int destY, 
			int destWidth, int destHeight, int transparency, boolean additive, RGBColor color) {
		
		if (texture == null)
			throw new IllegalStateException("not packed yet");

		Entry entry = entries.get(imageId);
		return blit(buffer, entry, destX, destY, destWidth, destHeight, 
				transparency, additive, color);
	}
	
	/**
	 * blits one of packed images completely without scaling
	 * 
	 * @param imageId
	 *            id of packed image as returned by addImage(..)
	 * @return size of blitted image. note this method always returns the same
	 *         array instance
	 * @see FrameBuffer#blit(Texture, int, int, int, int, int, int, boolean)
	 *      FrameBuffer.blit(..)
	 */
	public Point blit(FrameBuffer buffer, int imageId, int destX, int destY, boolean transparent) {
		if (texture == null)
			throw new IllegalStateException("not packed yet");

		Entry entry = entries.get(imageId);
		buffer.blit(texture, entry.bounds.left, entry.bounds.top, destX, destY, 
				entry.bounds.width(), entry.bounds.height(), transparent);

		blittedSize.set(entry.bounds.width(), entry.bounds.height());
		return blittedSize;
	}
	
	public Texture getTexture() {
		return texture;
	}

	private Point blit(FrameBuffer buffer, Entry entry, int destX, int destY, 
			int destWidth, int destHeight, int transparency, boolean additive, RGBColor color) {
		
		buffer.blit(texture, entry.bounds.left, entry.bounds.top, destX, destY, entry.bounds.width(), entry.bounds.height(), 
				destWidth, destHeight, transparency, additive, color);

		blittedSize.set(entry.bounds.width(), entry.bounds.height());
		return blittedSize;
	}
	
	public Point getImageSize(int imageId) {
		Entry entry = entries.get(imageId);
		return new Point(entry.bounds.width(), entry.bounds.height());
	}
	
	private void checkPacked() {
		if (packed)
			throw new IllegalStateException("already packed");
	}

	private void nextSize(Point size) {
		if (size.x > size.y)
			size.y <<= 1;
		else
			size.x <<= 1;
	}

	/** recursively prints placement tree starting from given node */
	private void printTree(Node node, String prefix, int depth) {
		if (node == null)
			return;

		System.out.println(depth + ":\t" + prefix + "--" + node);
		printTree(node.child[0], "  |" + prefix, depth + 1);
		printTree(node.child[1], "  |" + prefix, depth + 1);
	}

	/**
	 * returns the closest power of two that is equal or greater than given
	 * number
	 */
	private int closestTwoPower(int i) {
		int power = 1;
		while (power < i) {
			power <<= 1;
		}
		return power;
	}

	/** contents of a node if any */
	private static class Entry {
		private final Rect bounds = new Rect();
		private Bitmap image;

		private Entry(Bitmap image) {
			this.image = image;
		}

		@Override
		public String toString() {
			return "Entry: " + bounds;
		}
	}

	/** a node in our placement tree */
	private static class Node {
		private final Node[] child = new Node[2];
		private final Rect bounds = new Rect();
		private Entry entry;

		private Node() {
		}

		private Node(int width, int height) {
			bounds.set(0, 0, width, height);
		}

		private boolean isLeaf() {
			return (child[0] == null) && (child[1] == null);
		}

		private Node insert(Entry entry) {
			if (isLeaf()) {
				// if there's already a image here, return
				if (this.entry != null)
					return null;

				int width = entry.image.getWidth();
				int height = entry.image.getHeight();

				// (if we're too small, return)
				if ((width > bounds.width()) || (height > bounds.height()))
					return null;

				// (if we're just right, accept)
				if ((width == bounds.width()) && (height == bounds.height())) {
					this.entry = entry;
					this.entry.bounds.set(this.bounds);
					return this;
				}

				// otherwise, split this node
				child[0] = new Node();
				child[1] = new Node();

				// (decide which way to split)
				int dw = bounds.width() - width;
				int dh = bounds.height() - height;

				if (dw > dh) { // split horizontally
					child[0].bounds.set(bounds.left, bounds.top, bounds.left + width, bounds.bottom);
					child[1].bounds.set(bounds.left + width, bounds.top, bounds.right, bounds.bottom);
				} else { // split vertically
					child[0].bounds.set(bounds.left, bounds.top, bounds.right, bounds.top + height);
					child[1].bounds.set(bounds.left, bounds.top + height, bounds.right, bounds.bottom);
				}
				// insert into first child we created
				return child[0].insert(entry);
			} else {
				// try inserting into first child
				Node newNode = child[0].insert(entry);
				if (newNode != null)
					return newNode;

				// no room, insert into second
				return child[1].insert(entry);
			}
		}

		@Override
		public String toString() {
			return bounds + ((entry == null) ? " <no entry>" : " " + entry.toString());
		}
	}
}
