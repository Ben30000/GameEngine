

import java.awt.Graphics2D;



import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import java.nio.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;







// package org.joml.lwjgl; 
 
 
 import org.joml.Matrix4f; 
 import org.joml.Matrix4d;
 import org.joml.Vector3f;
 import org.joml.Vector3d;
 import org.joml.Quaternionf;
import org.joml.Vector2f; 



public class CubeMap {

	private int cubeMapID;
	private double size;
	InputStream in;
	
	private float[] vertices;
	
	private int programId, vShaderId, fShaderId;
	private int uMVPLocation, sampler1Location;
	private int vaoId, vboIdV;
	
	public CubeMap(double size, String x1, String x2, String y1, String y2, String z1, String z2) throws Exception {


		
		vertices = new float[] {-0.5f,0.5f,-0.5f, 
								-0.5f,-0.5f,-0.5f,
								-0.5f,-0.5f,0.5f,
								-0.5f,0.5f,-0.5f,
								-0.5f,-0.5f,0.5f,
								-0.5f,0.5f,0.5f,
								
								0.5f,0.5f,-0.5f, 
								0.5f,-0.5f,-0.5f,
								0.5f,-0.5f,0.5f,
								0.5f,0.5f,-0.5f,
								0.5f,-0.5f,0.5f,
								0.5f,0.5f,0.5f,
								
								-0.5f,-0.5f,-0.5f, 
								-0.5f,-0.5f,0.5f,
								0.5f,-0.5f,0.5f,
								-0.5f,-0.5f,-0.5f,
								0.5f,-0.5f,0.5f,
								0.5f,-0.5f,-0.5f,
								

								-0.5f,0.5f,-0.5f, 
								-0.5f,0.5f,0.5f,
								0.5f,0.5f,0.5f,
								-0.5f,0.5f,-0.5f,
								0.5f,0.5f,0.5f,
								0.5f,0.5f,-0.5f,
								

								-0.5f,0.5f,-0.5f, 
								-0.5f,-0.5f,-0.5f,
								0.5f,-0.5f,-0.5f,
								-0.5f,0.5f,-0.5f,
								0.5f,-0.5f,-0.5f,
								0.5f,0.5f,-0.5f,
								

								-0.5f,0.5f,0.5f, 
								-0.5f,-0.5f,0.5f,
								0.5f,-0.5f,0.5f,
								-0.5f,0.5f,0.5f,
								0.5f,-0.5f,0.5f,
								0.5f,0.5f,0.5f
								
		};
		
		   //glPixelStorei(GL_UNPACK_ALIGNMENT,1);

		   String vShaderCode = "#version 400\n" +
		   " layout (location = 0) in vec3 vPosition;\n" +
		   " out vec3 sampleVec;\n" +
		   " uniform mat4 uMVP;\n" +
		   
		   " void main() {\n" +
		   "  sampleVec = vPosition;\n" +
		   "  gl_Position = uMVP*vec4(vPosition,1.0);\n" +
		   " }";
		   
		   String fShaderCode = "#version 400\n" +
		   " in vec3 sampleVec;\n" +
		   " uniform samplerCube cubeMap;\n" +
		   

 	"layout (location = 0) out vec4 color;\n"+
 	"layout (location = 1) out vec4 colorBright;\n"+
	"layout (location = 2) out vec4 normal;\n"+
	"layout (location = 3) out vec4 position;\n"+
	"layout (location = 4) out vec4 sceneShadowAmount;\n"+
	"layout (location = 5) out vec4 refractiveObject;\n"+

		   "   void main() {\n" +
		   "   color = vec4(1.0*texture(cubeMap,sampleVec).rgb, 1.0);\n  " +
		   //" fragColor = vec4(1.0,0.0,0.0,1.0);"+
		   " float brightness = dot(color.rgb,vec3(0.2126,0.7152,0.0722));"+

		   " if (brightness > 0.1) { "+
		   //" fragColorBright = vec4((1.7*brightness + 0.2)*fragColor.rgb, 1.0);"+
		   " colorBright = vec4(0.0,0.0,0.0,1.0);"+
		   
		   " }"+
		   " else {"+
		   " 	colorBright = vec4(0.0,0.0,0.0,1.0);"+
		   " }"+
		   
		   
		   " normal = vec4(100.0,0.0,0.0,1.0);"+
		   " refractiveObject = vec4(0.0, 0.0, 0.0, 1.0);"+
		   " position = vec4(1.0,0.0,0.0,1.0);"+
		   " }";
		   
		   

			
			
	        
	        programId = glCreateProgram();
	        glUseProgram(programId);
	        
	        if (programId == 0) {
	            throw new Exception("Could not create Shader");
	        }
	        
	        
	        vShaderId = glCreateShader(GL_VERTEX_SHADER);
	        if (vShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
	        }

	        glShaderSource(vShaderId, vShaderCode);
	        glCompileShader(vShaderId);

	        if (glGetShaderi(vShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(vShaderId, 1024));
	        }

	        glAttachShader(programId, vShaderId);

	        
	        
	        
	        

	        fShaderId = glCreateShader(GL_FRAGMENT_SHADER);
	        if (fShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
	        }

	        glShaderSource(fShaderId, fShaderCode);
	        glCompileShader(fShaderId);

	        if (glGetShaderi(fShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(fShaderId, 1024));
	        }

	        glAttachShader(programId, fShaderId);
	        
	        
	        
	        
	        
	        
	        
	        
	        glLinkProgram(programId);
	        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
	        }

	        if (vShaderId != 0) {
	            glDetachShader(programId, vShaderId);
	        }
	        if (fShaderId != 0) {
	            glDetachShader(programId, fShaderId);
	        }

	        glValidateProgram(programId);
	        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
	        }
	    
		
	        glUseProgram(programId);
	        
	        
	        
	        uMVPLocation = glGetUniformLocation(programId,"uMVP");
	      sampler1Location = glGetUniformLocation(programId,"cubeMap");       
	        
	        

	        
	        
			 FloatBuffer verticesBuffer = null;
		//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
			    try {
		            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
		            System.out.println("Length of vertices is " + vertices.length);
		//   verticesBuffer = 
			
		            (verticesBuffer.put(vertices)).flip();
	//glGenVertexArrays();
		           vaoId = glGenVertexArrays();
		           System.out.println("Vao is " + vaoId);
		            glBindVertexArray(vaoId);

		            vboIdV = glGenBuffers();
		            glBindBuffer(GL_ARRAY_BUFFER, vboIdV);
		            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
		            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		            
		            glBindVertexArray(0);
		            glBindBuffer(GL_ARRAY_BUFFER, 0);

		            //glBindVertexArray(0);         
		        } finally {
		            if (verticesBuffer  != null) {
		         //   	System.exit(0);
		                MemoryUtil.memFree(verticesBuffer);
		            }
		        }
		        
	      


				cubeMapID = glGenTextures();
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_CUBE_MAP,cubeMapID);
	        
		
 FileInputStream in1 = new FileInputStream(x1);
try {
   PNGDecoder decoder = new PNGDecoder(in1);
 
   System.out.println("  cube map width="+decoder.getWidth());
   System.out.println(" cube map height="+decoder.getHeight());
 
   ByteBuffer buf1 = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
   decoder.decode(buf1, decoder.getWidth()*4, Format.RGBA);
   
   
   
   buf1.flip();
   

glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_SRGB_ALPHA, decoder.getWidth(),
    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf1);

glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);  

} finally {
   in1.close();
}





 FileInputStream in2 = new FileInputStream(x2);
try {
   PNGDecoder decoder = new PNGDecoder(in2);
 
   System.out.println("width="+decoder.getWidth());
   System.out.println("height="+decoder.getHeight());
 
   ByteBuffer buf2 = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
   decoder.decode(buf2, decoder.getWidth()*4, Format.RGBA);
   buf2.flip();
   

glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_SRGB_ALPHA, decoder.getWidth(),
    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf2);

glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);  

} finally {
   in2.close();
}






FileInputStream in3 = new FileInputStream(y1);
try {
   PNGDecoder decoder = new PNGDecoder(in3);
 
   System.out.println("width="+decoder.getWidth());
   System.out.println("height="+decoder.getHeight());
 
   ByteBuffer buf3 = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
   decoder.decode(buf3, decoder.getWidth()*4, Format.RGBA);
   buf3.flip();
   

glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_SRGB_ALPHA, decoder.getWidth(),
    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf3);

glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);  

} finally {
   in3.close();
}






 FileInputStream in4 = new FileInputStream(y2);
try {
   PNGDecoder decoder = new PNGDecoder(in4);
 
   System.out.println("width="+decoder.getWidth());
   System.out.println("height="+decoder.getHeight());
 
   ByteBuffer buf4 = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
   decoder.decode(buf4, decoder.getWidth()*4, Format.RGBA);
   buf4.flip();
   

glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_SRGB_ALPHA, decoder.getWidth(),
    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf4);

glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);  

} finally {
   in4.close();
}




FileInputStream in5 = new FileInputStream(z1);
try {
   PNGDecoder decoder = new PNGDecoder(in5);
 
   System.out.println("width="+decoder.getWidth());
   System.out.println("height="+decoder.getHeight());
 
   ByteBuffer buf5 = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
   decoder.decode(buf5, decoder.getWidth()*4, Format.RGBA);
   buf5.flip();
   

glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_SRGB_ALPHA, decoder.getWidth(),
    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf5);

glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);  

} finally {
   in5.close();
}




 FileInputStream in6 = new FileInputStream(z2);
try {
   PNGDecoder decoder = new PNGDecoder(in6);
 
   System.out.println("width="+decoder.getWidth());
   System.out.println("height="+decoder.getHeight());
 
   ByteBuffer buf6 = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
   decoder.decode(buf6, decoder.getWidth()*4, Format.RGBA);
   buf6.flip();
   

glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_SRGB_ALPHA, decoder.getWidth(),
    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf6);

glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);  

} finally {
   in6.close();
}



glActiveTexture(GL_TEXTURE0);
glBindTexture(GL_TEXTURE_CUBE_MAP,cubeMapID);
glUniform1i(sampler1Location,0);
		//glGenerateMipmap(GL_TEXTURE_CUBE_MAP);

glUseProgram(0);
			
		
	}
	
	
public void render(float screenBrightness) {

	//glBindFramebuffer(GL_FRAMEBUFFER,0);
	
	glUseProgram(programId);
	
	//glDepthMask(false);
	
	glEnable(GL_TEXTURE_2D);
	glDisable(GL_TEXTURE_2D);
	
	glEnable(GL_TEXTURE_CUBE_MAP);
	Vector3f scalingVector = new Vector3f((float)size,(float)size,(float)size);
	Matrix4f scalingM = new Matrix4f();
	scalingM.scaling(310.0f,310.0f,310.0f);
	Matrix4f projectionM = new Matrix4f();
	projectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
	Matrix4f tMVP = new Matrix4f();
	//tMVP.identity();
	projectionM.mul(scalingM,tMVP);
	//tMVP.mul(scaling);
    //tMVP.mul(scaling,tMVP);
	//tMVP = projectionM;
	//tMVP = scalingM;
	Vector3f lightD = new Vector3f(0.2f,1.0f,1.0f);
	Vector3f lightC = new Vector3f(0.7f,0.7f,0.7f);
	
	   try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer tMVPM = stack.mallocFloat(16);
        	        FloatBuffer lightDV = stack.mallocFloat(3);
	FloatBuffer lightCV = stack.mallocFloat(3);
	
        	        tMVP.get(tMVPM);
	        lightD.get(lightDV);
	lightC.get(lightCV);
	
	        glUseProgram(programId);
	        
	        glUniformMatrix4fv(uMVPLocation, false, tMVPM);
	        glActiveTexture(GL_TEXTURE0);
	        glBindTexture(GL_TEXTURE_CUBE_MAP,cubeMapID);
	        glUniform1i(sampler1Location,0);
	        
	        //glActiveTexture(GL_TEXTURE0);
	        //glBindTexture(GL_TEXTURE_2D,testWater);
	        
	        
	   }
	   
	   
	   //System.out.println("For Stage Item, Program Id and Vao Id are " + programId + " and " + vaoId);

       // glUseProgram(programId);
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        
        
       //glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//System.out.println("Going there with vertex count of " + vertexCount);
//System.out.println("Vao is " + vaoId + " and Vbo is " + vboId + " and programId is " + programId);
       // System.out.println("About to draw cube map");
        glDrawArrays(GL_TRIANGLES,0,36);
        
        
        glDisable(GL_TEXTURE_CUBE_MAP);
        glEnable(GL_TEXTURE_2D);
        
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        
        glDepthMask(true);
        
        glUseProgram(0);
	   
	   
}

public int getCubeMapID() {
	
	return this.cubeMapID;
}
	
}
