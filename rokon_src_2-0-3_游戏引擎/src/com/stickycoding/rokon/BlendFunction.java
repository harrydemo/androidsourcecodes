package com.stickycoding.rokon;

/**
 * BlendFunction.java
 * Holds parameters for OpenGL blend function
 * See glBlendFunc parameters for details on each different setting
 * 
 * @author Richard
 */

public class BlendFunction {

    protected int blendFunctionSrc, blendFunctionDst;
    
    /**
     * Creates a BlendFunction object with given parameters
     * 
     * @param blendFunctionSrc blend function source parameter
     * @param blendFunctionDst blend function destination parameter
     */
    public BlendFunction(int blendFunctionSrc, int blendFunctionDst) {
            this.blendFunctionSrc = blendFunctionSrc;
            this.blendFunctionDst = blendFunctionDst;
    }

    /**
     * Returns the choice of source blend function
     * 
     * @return source parameter
     */
    public int getSrc() {
            return blendFunctionSrc;
    }
    
    /**
     * Sets the choice of source blend function
     * 
     * @param blendFunctionSrc source parameter
     */
    public void setSrc(int blendFunctionSrc) {
            this.blendFunctionSrc = blendFunctionSrc;
    }
    
    /**
     * Returns the choice of destination blend function
     * 
     * @return
     */
    public int getDst() {
            return blendFunctionDst;
    }
    
    /**
     * Sets the choice of destination blend function
     * 
     * @param blendFunctionDst destination parameter
     */
    public void setDst(int blendFunctionDst) {
            this.blendFunctionDst = blendFunctionDst;
    }
}
