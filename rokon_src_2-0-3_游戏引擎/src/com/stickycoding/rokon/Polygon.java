package com.stickycoding.rokon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.stickycoding.rokon.vbo.ArrayVBO;
import com.stickycoding.rokon.vbo.VBO;


/**
 * Polygon.java
 * A polygon shape, vertices should be from 0 to 1
 * 
 * @author Richard
 */

/**
 * @author Richard
 *
 */
public class Polygon {
	
	/**
	 * The maximum number of vertices allowed in the array of vertices
	 */
	public static int MAX_VERTEX_COUNT = 128;

	protected boolean building;
	protected Point[] vertex;
	protected int vertexCount = 0;
	
	protected Vector2[] edge;
	protected Vector2[] normal;
	
	protected BufferObject buffer;
	protected ArrayVBO vbo;
	
	/**
	 * Creates a PolygonShape (for Box2D) from this Polygon
	 * 
	 * @return valid PolygonShape
	 */
	public PolygonShape getPolygonShape() {
		PolygonShape shape = new PolygonShape();
		shape.set(vertex);
		return shape;
	}
	
	/**
	 * Returns the VBO associated with this Polygon, if one isn't ready, calculates a new one.
	 * The ArrayVBO is stored in memory, so only one copy is produced.
	 * 
	 * @return ArrayVBO for this Polygon shape
	 */
	public ArrayVBO getVBO() {
		if(vbo == null) {
			vbo = new ArrayVBO(buffer, VBO.STATIC);
		}
		return vbo;
	}
	
	/**
	 * Calculates the edges for this Polygon. There should be no need 
	 * to call this unless you directly modified/added vertices after 
	 * creating Polygon object.
	 */
	public void findEdges() {
		edge = new Vector2[vertexCount];
		normal = new Vector2[vertexCount];
		for(int i = 0; i < vertexCount; i++) {
			int nextEdge = i + 1;
			if(nextEdge == vertexCount) nextEdge = 0;
			edge[i] = new Vector2(vertex[nextEdge].getX() - vertex[i].getX(), vertex[nextEdge].getY() - vertex[i].getY());
			normal[i] = MathHelper.findNormal(edge[i]);
		}
	}
	
	/**
	 * Fetches the BufferObject for this Polygon. If one is not already created, produces another.
	 * Stored in the memory, so only one ever created per Polygon.
	 * 
	 * @return BufferObject
	 */
	public BufferObject getBufferObject() {
		if(buffer == null) {
			buffer = new BufferObject(vertexCount * 2);
			float[] vertices = new float[vertexCount * 2];
			int c = 0;
			for(int i = 0; i < vertex.length; i++) {
				vertices[c++] = vertex[i].getX();
				vertices[c++] = vertex[i].getY();
			}
			buffer.updateRaw(vertices);
		}
		return buffer;
	}
	
	/**
	 * Creates a 2D Polygon based on an array of Point objects
	 * 
	 * @param vertices array of Point
	 */
	public Polygon(Point[] vertices) {
		this.vertex = vertices;
		vertexCount = vertices.length;
		findEdges();
	}
	
	/**
	 * Creates a 2D Polygon based on an array of floating points
	 * 
	 * @param vertices array of floats
	 */
	public Polygon(float[] vertices) {
		if(vertices.length % 2 != 0) {
			Debug.error("Tried creating Polygon with odd number of vertices");
			Debug.forceExit();
			return;
		}
		vertex = new Point[vertices.length / 2];
		int c = 0;
		for(int i = 0; i < vertices.length / 2; i++) {
			vertex[i] = new Point(vertices[c++], vertices[c++]);
		}
		vertexCount = vertices.length / 2;
		findEdges();
	}
	
	/**
	 * Creates a 2D Polygon, with no vertices 
	 */
	public Polygon() {
		vertex = new Point[MAX_VERTEX_COUNT];
		vertexCount = 0;
	}
	
	/**
	 * Adds a vertex to the Polygon by passing a Point
	 * 
	 * @param location a valid Point
	 */
	public void addVertex(Point location) {
		vertex[vertexCount++] = location;
	}
	
	/**
	 * Adds a vertex to the Polygon by passing floats
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void addVertex(float x, float y) {
		addVertex(new Point(x, y));
	}
	
	/**
	 * Fetches the Point of a specific vertex
	 * 
	 * @param index index of the vertex
	 * 
	 * @return Point object, NULL if invalid index
	 */
	public Point getVertex(int index) {
		return vertex[index];
	}
	
	/**
	 * Completes the Polygon, this should be called after all vertices are added through addVertex.
	 * No need to call this if defining the vertices through Polygon()
	 */
	public void complete() {
		findEdges();
	}
	
	/**
	 * Returns the number of active vertices in this Polygon
	 * 
	 * @return number of vertices, >= 0
	 */
	public int getVertexCount() {
		return vertexCount;
	}
	
	
}
