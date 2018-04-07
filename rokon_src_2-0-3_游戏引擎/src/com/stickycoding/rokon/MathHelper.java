package com.stickycoding.rokon;

import com.badlogic.gdx.math.Vector2;

/**
 * MathHelper.java
 * Some basic functions for mathematics
 * 
 * @author Richard
 */

public class MathHelper {
	
	/**
	 * Conversion factor from degrees to radians
	 */
	public static final float DEG_TO_RAD = 0.0174532925f;
	
	/**
	 * Conversion factor from radians to degrees
	 */
	public static final float RAD_TO_DEG = 57.2957795f;
	
	/**
	 * Determines whether a given point is inside a given rectangular area
	 * 
	 * @param pointX x-coordinate of point 
	 * @param pointY y-coordinate of point
	 * @param x x-coordinate of rectangular area
	 * @param y y-coordinate of rectangular area
	 * @param width width of the rectangular area
	 * @param height height of the rectangular area
	 * 
	 * @return TRUE if point is inside the rectangular area, FALSE otherwise
	 */
	public static boolean pointInRect(float pointX, float pointY, float x, float y, float width, float height) {
		if(pointX < x || pointY < y || pointX > x + width || pointY > y + height) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Determines whether two rectangles overlap
	 * 
	 * @param rect1X1 top-left x-coordinate of rectangle A
	 * @param rect1Y1 top-left y-coordinate of rectangle A
	 * @param rect1X2 bottom-right x-coordinate of rectangle A
	 * @param rect1Y2 bottom-right y-coordinate of rectangle A
	 * @param rect2X1 top-left x-coordinate of rectangle B
	 * @param rect2Y1 top-left y-coordinate of rectangle B
	 * @param rect2X2 bottom-right x-coordinate of rectangle A
	 * @param rect2Y2 bottom-right y-coordinate of rectangle B
	 * @return
	 */
	public static boolean rectOverlap(float rect1X1, float rect1Y1, float rect1X2, float rect1Y2, float rect2X1, float rect2Y1, float rect2X2, float rect2Y2) {
		return rect1X1 < rect2X2 && rect1X2 > rect2X1 && rect1Y1 < rect2Y2 && rect1Y2 > rect2Y1;
	}
	
	/**
	 * Finds the normal vector
	 * 
	 * @param vector valid Vector2 object
	 * 
	 * @return a normalized Vector2 object, normal to the input vector
	 */
	public static Vector2 findNormal(Vector2 vector) {
		return new Vector2(vector.y, -vector.x).nor();
	}
	
	/**
	 * Determines whether two Sprites intersect
	 * 
	 * @param sprite1 valid Sprite object
	 * @param sprite2 valid Sprite object
	 * 
	 * @return TRUE if overlapping, FALSE otherwise
	 */
	public static boolean intersects(Sprite sprite1, Sprite sprite2) {
		if(sprite1.polygon == Rokon.rectangle && sprite2.polygon == Rokon.rectangle && sprite1.rotation == 0 && sprite2.rotation == 0) {
			return rectOverlap(sprite1.getX(), sprite1.getY(), sprite1.getX() + sprite1.getWidth(), sprite1.getX() + sprite1.getHeight(), sprite2.getX(), sprite2.getY(), sprite2.getX() + sprite2.getWidth(), sprite2.getY() + sprite2.getHeight());
		}
		
		float dotProduct = 0;
		float minA = 0;
		float maxA = 0;
		float minB = 0;
		float maxB = 0;
		
		for(int i = 0; i < sprite1.polygon.vertexCount; i++) {
			int startIndex = i;
			int endIndex = (i < sprite1.polygon.edge.length - 1 ? i + 1 : 0);
			float[] startVertex = sprite1.getVertex(startIndex);
			float[] endVertex = sprite1.getVertex(endIndex);
			float edgeX = endVertex[0] - startVertex[0];
			float edgeY = endVertex[1] - startVertex[1];
			
			float axisX = edgeY;
			float axisY = -edgeX;
			
			float[] vertex = sprite2.getVertex(0);
			dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
			minA = dotProduct;
			maxA = dotProduct;			
			for(int j = 1; j < sprite2.polygon.vertexCount; j++) {
				vertex = sprite2.getVertex(j);
				dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
				if(dotProduct < minA) {
					minA = dotProduct;
				} else if(dotProduct > maxA) {
					maxA = dotProduct;
				}
			}

			vertex = sprite1.getVertex(0);
			dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
			minB = dotProduct;
			maxB = dotProduct;			
			for(int j = 1; j < sprite1.polygon.vertexCount; j++) {
				vertex = sprite1.getVertex(j);
				dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
				if(dotProduct < minB) {
					minB = dotProduct;
				} else if(dotProduct > maxB) {
					maxB = dotProduct;
				}
			}
			
			if(intervalDistance(minB, maxB, minA, maxA) > 0) {
				return false;
			}
		}
		
		for(int i = 0; i < sprite2.polygon.vertexCount; i++) {
			int startIndex = i;
			int endIndex = (i < sprite2.polygon.edge.length - 1 ? i + 1 : 0);
			float[] startVertex = sprite2.getVertex(startIndex);
			float[] endVertex = sprite2.getVertex(endIndex);
			float edgeX = endVertex[0] - startVertex[0];
			float edgeY = endVertex[1] - startVertex[1];
			
			float axisX = edgeY;
			float axisY = -edgeX;

			float vertex[] = sprite2.getVertex(0);
			dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
			minA = dotProduct;
			maxA = dotProduct;			
			for(int j = 1; j < sprite2.polygon.vertexCount; j++) {
				vertex = sprite2.getVertex(j);
				dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
				if(dotProduct < minA) {
					minA = dotProduct;
				} else if(dotProduct > maxA) {
					maxA = dotProduct;
				}
			}

			vertex = sprite1.getVertex(0);
			dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
			minB = dotProduct;
			maxB = dotProduct;			
			for(int j = 1; j < sprite1.polygon.vertexCount; j++) {
				vertex = sprite1.getVertex(j);
				dotProduct = dot(axisX, axisY, vertex[0], vertex[1]);
				if(dotProduct < minB) {
					minB = dotProduct;
				} else if(dotProduct > maxB) {
					maxB = dotProduct;
				}
			}
			
			if(intervalDistance(minB, maxB, minA, maxA) > 0) {
				return false;
			}
		}
		return true;
	}
	
	private static float intervalDistance(float minA, float maxA, float minB, float maxB) {
	    if (minA < minB) {
	        return minB - maxA;
	    } else {
	        return minA - maxB;
	    }
	}
	
	/**
	 * Calculates the minimum distance between a given point, and an edge/vertex on a Sprite
	 * 
	 * @param x point X
	 * @param y point Y 
	 * @param sprite valid Sprite
	 * @return 9999 if unable to calculate, +ve float otherwise
	 */
	public static float distanceToShape(float x, float y, Sprite sprite) {
		if(sprite.polygon != Rokon.circle) {
			float minDistance = 9999;
			for(int i = 0; i < sprite.getPolygon().vertexCount; i++) {
				int startIndex = i;
				int endIndex = i < sprite.polygon.edge.length - 1 ? i + 1 : 0;
				float[] startVertex = sprite.getVertex(startIndex);
				float[] endVertex = sprite.getVertex(endIndex);
				float edgeX = endVertex[0] - startVertex[0];
				float edgeY = endVertex[1] - startVertex[1];
				float axisX = edgeY;
				float axisY = -edgeX;
				
				float edgeStartPoint = dot(edgeX, edgeY, startVertex[0], startVertex[1]);
				float edgeEndPoint = dot(edgeX, edgeY, endVertex[0], endVertex[1]);
				float edgePointPos = dot(edgeX, edgeY, x, y);
				
				if((edgePointPos > edgeStartPoint && edgePointPos < edgeEndPoint) || (edgePointPos > edgeEndPoint && edgePointPos < edgeStartPoint)) {
					
					float normalDotEdge = dot(axisX, axisY, startVertex[0], startVertex[1]);
					float normalDotPoint = dot(axisX, axisY, x, y);
					float normalDotDiff = Math.abs(normalDotEdge - normalDotPoint);
					
					if(normalDotDiff < minDistance) minDistance = normalDotDiff;
				}
				
				//Also check against this vertex
				float dx = x - startVertex[0];
				float dy = y - startVertex[1];
				float vertexDiff = (float)Math.sqrt(dx * dx + dy * dy);
				if(vertexDiff < minDistance) minDistance = vertexDiff;
			}
			return minDistance;
		} else {
			float centreX = sprite.getX() + (sprite.getWidth() / 2);
			float centreY = sprite.getY() + (sprite.getHeight() / 2);
			float maxDistance = (sprite.getWidth() / 2) * (sprite.getWidth() / 2);
			float dx = x - centreX;
			float dy = y - centreY;
			return (float)Math.sqrt(dx * dx + dy * dy) - (sprite.getWidth() / 2);
		}
	}
	
	public static boolean pointInShape(float x, float y, Sprite sprite) {
		if(sprite.polygon != Rokon.circle) { 
			for(int i = 0; i < sprite.getPolygon().vertexCount; i++) {
				int startIndex = i;
				int endIndex = i < sprite.polygon.edge.length - 1 ? i + 1 : 0;
				float[] startVertex = sprite.getVertex(startIndex);
				float[] endVertex = sprite.getVertex(endIndex);
				float edgeX = endVertex[0] - startVertex[0];
				float edgeY = endVertex[1] - startVertex[1];
				float axisX = edgeY;
				float axisY = -edgeX;
	
				int nextIndex = (endIndex < sprite.polygon.edge.length - 1 ? endIndex + 1 : 0);
				float[] nextVertex = sprite.getVertex(nextIndex);
				
				float axisDot = dot(axisX, axisY, startVertex[0], startVertex[1]);
				float nextDot = dot(axisX, axisY, nextVertex[0], nextVertex[1]);
				float testDot = dot(axisX, axisY, x, y);
				
				if((nextDot >= axisDot && testDot < axisDot) || (nextDot <= axisDot && testDot > axisDot)) {
					return false;
				}
			}
			return true;
		} else {
			float centreX = sprite.getX() + (sprite.getWidth() / 2);
			float centreY = sprite.getY() + (sprite.getHeight() / 2);
			float maxDistance = (sprite.getWidth() / 2) * (sprite.getWidth() / 2);
			float dx = x - centreX;
			float dy = y - centreY;
			float distance = dx * dx + dy * dy;
			return distance <= maxDistance;
		}
	}
	
	/**
	 * Returns the dot product between two Vector2 objects
	 * 
	 * @param vector1 valid Vector2
	 * @param vector2 valid Vector2
	 * 
	 * @return dot product
	 */
	public static float dot(Vector2 vector1, Vector2 vector2) {
		return vector1.dot(vector2);
	}
	
	/**
	 * Returns the dot product between a Vector2 object and given points
	 * 
	 * @param vector valid Vector2
	 * @param x x-value
	 * @param y y-value
	 * 
	 * @return dot product
	 */
	public static float dot(Vector2 vector, float x, float y) {
		return vector.x * x + vector.y * y;
	}
	
	/**
	 * Returns the dot product between two sets of xy values
	 * 
	 * @param x1 x-value A
	 * @param y1 y-value A
	 * @param x2 x-value B
	 * @param y2 y-value B
	 * 
	 * @return
	 */
	public static float dot(float x1, float y1, float x2, float y2) {
		return x1 * x2 + y1 * y2;
	}
	
	/**
	 * Calculates the rotated position of a set of points
	 * 
	 * @param angle angle to rotate, in degrees
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param pivotX x-coordinate of the rotation pivot
	 * @param pivotY y-coordinate of the rotation pivot
	 * 
	 * @return a float array with two items, 0=x, 1=y 
	 */
	public static float[] rotate(float angle, float x, float y, float pivotX, float pivotY) {
		angle *= DEG_TO_RAD;
		float[] ret = new float[2];
		ret[0] = pivotX + ( (float)Math.cos(angle) * (x - pivotX) - (float)Math.sin(angle) * (y - pivotY));
		ret[1] = pivotY + ( (float)Math.sin(angle) * (x - pivotX) + (float)Math.cos(angle) * (y - pivotY));
		return ret;
	}
	

}
