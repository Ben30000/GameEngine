
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
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;







// package org.joml.lwjgl; 
 
 
 import org.joml.Matrix4f; 
 import org.joml.Matrix4d;
 import org.joml.Vector3f;
 import org.joml.Vector3d;
 import org.joml.Quaternionf;
import org.joml.Vector2f; 

public class Water {

	int width, height;
	int prevHeightID, prevVelocityID, curHeightID, curVelocityID;
	int prevHeightFieldFBO, curHeightFieldFBO;
	int environmentCubeMapFBO;
	int environmentCubeMapID;
	double x, y, z;
	float heightScale, scale;
	Background b;
	float[] pixels;
    float[] vCoords;
    float[] tCoords;
    float[] nVecs;
    float[] screenQuad;
    float[] screenTCoords;
    
    float[] cubeVertices;
    
    float[] waterPlane, waterNormals, waterTCs;
    
    
    
    
    
    
    Wrap testWrap;
    
    float heightScaleSize;
    
    
    FloatBuffer verticesBuffer, verticesBuffer2;
    ByteBuffer buffer;
    FloatBuffer fBuffer;
    
    FloatBuffer theHeightField;
    IntBuffer theHeightFieldIndexBuffer;
    
    int theHeightFieldIndexBufferID;
    
    int heightFieldProgramID, heightFieldVShaderID, heightFieldFShaderID;
    int heightFieldVAOID, heightFieldVBOIDV, heightFieldVBOIDT;
    int heightFieldInHeightLocation, heightFieldInVelocityLocation, heightFieldShaderDXLocation, heightFieldShaderDYLocation;
    
    int dropProgramID, dropVShaderID, dropFShaderID;
    int dropVAOID, dropVBOIDV, dropVBOIDT;
    int dropInHeightLocation, dropInVelocityLocation, dropCenterLocation, dropStrengthLocation, dropRadiusLocation;
    
    int normalProgramID, normalVShaderID, normalFShaderID;
    int normalVAOID, normalVBOIDV, normalVBOIDT;
    int normalInHeightLocation, normalDXLocation, normalDYLocation, normalXScaleLocation, normalYScaleLocation, normalZScaleLocation;
    
    int waterProgramID, waterVShaderID, waterFShaderID;
    int waterVAOID, waterVBOIDV, waterVBOIDT, waterVBOIDN;
    int waterUMVPLocation, waterInHeightLocation, waterInNormalXLocation, waterInNormalZLocation, waterWidthLocation, waterHeightLocation, waterScreenBrightnessLocation;
    
    int copyProgramID, copyVShaderID, copyFShaderID;
    int copyVAOID, copyVBOIDV, copyVBOIDT;
    int copyInHeightLocation, copyInVelocityLocation;
    
    
    
    int programID, vShaderID, fShaderID;
    int vAOID, vBOIDV;
    
    int testTarget,testFBO;
    
    
    
    int[] drawBuffersOrder;
    IntBuffer drawBuffersList;
	
    private CubeMap reflectionsCubeMap;
    private int waterUWorldLocation;
    
	
    
    
	public Water(int width, int height, double x, double y, double z, float heightScale, float scale, Background b, Wrap w, float heightScaleSize) throws Exception {
		
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.z = z;
		this.heightScale = heightScale;
		this.scale = scale;
		this.pixels = new float[4*this.width*this.height];
		screenQuad = new float[]{-1.0f,1.0f,0.0f, -1.0f,-1.0f,0.0f, 1.0f,-1.0f,0.0f, -1.0f,1.0f,0.0f, 1.0f,-1.0f,0.0f, 1.0f,1.0f,0.0f};
        screenTCoords = new float[]{0.0f,1.0f, 0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f, 1.0f,0.0f, 1.0f,1.0f};
        
       this.heightScaleSize = heightScaleSize;

		
		cubeVertices = new float[] {-0.5f,0.5f,-0.5f, 
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
        
        
        drawBuffersOrder = new int[]{GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1};
        drawBuffersList = MemoryUtil.memAllocInt(2);
        drawBuffersList.put(drawBuffersOrder);
        drawBuffersList.flip();
        
       // glDrawBuffers(drawBuffersList);
        
        vCoords = new float[3*width*height];
        nVecs = new float[2*width*height];
        
		buffer = ByteBuffer.allocateDirect(4*pixels.length).order(ByteOrder.nativeOrder());
        fBuffer = buffer.asFloatBuffer();
        
        
        
        
        
        
        
        
        
        this.testWrap = w;
        
        
        
        
        this.b = b;
   
   for (int a = 0; a < pixels.length; a++) {
	
	   pixels[a] = 0.0f;
	   
   }
   
   fBuffer.put(pixels);
   
   
   theHeightFieldIndexBuffer = (ByteBuffer.allocateDirect(24*(width - 1)*(height - 1)).order(ByteOrder.nativeOrder())).asIntBuffer();

   theHeightField = (ByteBuffer.allocateDirect(16*width*height).order(ByteOrder.nativeOrder())).asFloatBuffer();
   int d = 0;
int[] ind = new int[6*(width - 1)*(height - 1)];
   for (int a = 0; a < width - 1; a++) {
	   for (int c = 0; c < height - 1; c++) {
		   
		   ind[d] = (a*height + c);
		   ind[d + 1] = (a*height + c + 1);
		   ind[d + 2] = ((a + 1)*height + c + 1);
		   ind[d + 3] = (a*height + c);
		   ind[d + 4] = ((a + 1)*height + c + 1);
		   ind[d + 5] = ((a + 1)*height + c);

		   d = d + 6;
		   
	   }
   }
   
   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(0));
   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(1));

   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(2));
   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(3));
   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(4));
   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(5));
   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(6));
   System.out.println("Ind1 = " + theHeightFieldIndexBuffer.get(7));

   
   
   
   ByteBuffer buf = ByteBuffer.allocateDirect(16*width*height);
System.out.println("WABAM: " + buf.getFloat(0));

System.out.println("WABAM: " + buf.getFloat(1));

System.out.println("WABAM: " + buf.getFloat(2));


System.out.println("WABAM: " + buf.getFloat(3));
System.out.println("WABAM: " + buf.getFloat(4));

System.out.println("WABAM: " + buf.getFloat(5));

System.out.println("WABAM: " + buf.getFloat(6));

System.out.println("WABAM: " + buf.getFloat(7));

   
   
   for (int a = 0; a < 16*width*height; a++) {
	   buf.put(a,(byte)0);
   }
   
   
   
   /*
   
   prevHeightFieldID = glGenTextures();
   glActiveTexture(GL_TEXTURE0 + 10);
   //System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
   glBindTexture(GL_TEXTURE_2D,prevHeightFieldID);
   //glPixelStorei(GL_UNPACK_ALIGNMENT,1);

   
glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, this.width,
    this.height, 0, GL_RGBA32F, GL_FLOAT, fBuffer);






  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
  
  
       prevHeightFieldFBO = glGenFramebuffers();

       glBindFramebuffer(GL_FRAMEBUFFER,prevHeightFieldFBO);
       glFramebufferTexture(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,prevHeightFieldID,0);
       
       
       */
   
   
   

   prevHeightID = glGenTextures();
   //glActiveTexture(GL_TEXTURE0 + 11);
   //System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
   glBindTexture(GL_TEXTURE_2D,prevHeightID);
   //glPixelStorei(GL_UNPACK_ALIGNMENT,1);


   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
   
   
glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, this.width,
    this.height, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);



System.out.println("WAFFEL FRIES");


  
       
       
       
       

		   

		   prevVelocityID = glGenTextures();
		   //glActiveTexture(GL_TEXTURE0 + 11);
		   //System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
		   glBindTexture(GL_TEXTURE_2D,prevVelocityID);
		   //glPixelStorei(GL_UNPACK_ALIGNMENT,1);


		   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
		   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
		   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
		   glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		   
		   
		glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, this.width,
		    this.height, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);



		System.out.println("WAFFEL FRIES");


		  
	       prevHeightFieldFBO = glGenFramebuffers();

	       glBindFramebuffer(GL_FRAMEBUFFER,prevHeightFieldFBO);
	       
	       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,prevHeightID,0);
	       
		       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,prevVelocityID,0);
		       
		       
		       
		       
		       

				glBindTexture(GL_TEXTURE_2D,0);
				glBindFramebuffer(GL_FRAMEBUFFER,0);
				
				
		       
		       
		       
		       
		   
		
       
       
       
       
   
   
   

       /*
       
       
       

       curHeightFieldID = glGenTextures();
      // glActiveTexture(GL_TEXTURE0 + 11);
       //System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
       glBindTexture(GL_TEXTURE_2D,curHeightFieldID);
       //glPixelStorei(GL_UNPACK_ALIGNMENT,1);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, this.width,
        this.height, 0, GL_RGBA32F, GL_FLOAT, fBuffer);

      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
      
      
           curHeightFieldFBO = glGenFramebuffers();

           glBindFramebuffer(GL_FRAMEBUFFER,curHeightFieldFBO);
           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,curHeightFieldID,0);
       
		
		glBindFramebuffer(GL_FRAMEBUFFER,0);
		
		*/
       
       
       
       
       
       
       

       
       
       
       

       curHeightID = glGenTextures();
      // glActiveTexture(GL_TEXTURE0 + 11);
       //System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
       glBindTexture(GL_TEXTURE_2D,curHeightID);
       //glPixelStorei(GL_UNPACK_ALIGNMENT,1);
       

       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
       
       

    glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, this.width,
        this.height, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
    
    
    


    curVelocityID = glGenTextures();
   // glActiveTexture(GL_TEXTURE0 + 11);
    //System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
    glBindTexture(GL_TEXTURE_2D,curVelocityID);
    //glPixelStorei(GL_UNPACK_ALIGNMENT,1);
    

    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    
    

 glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, this.width,
     this.height, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);


           curHeightFieldFBO = glGenFramebuffers();

           glBindFramebuffer(GL_FRAMEBUFFER,curHeightFieldFBO);
           glDrawBuffer(GL_COLOR_ATTACHMENT0);
           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,curHeightID,0);
           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,curVelocityID,0);

       
		glBindTexture(GL_TEXTURE_2D,0);
		glBindFramebuffer(GL_FRAMEBUFFER,0);
		
		
		
		
		
		
		
		String vShaderHeightField = "#version 400\n" +
		" layout (location = 0) in vec3 vPosition;\n" +
		" layout (location = 1) in vec2 tCoord;\n" +
		
		" out vec2 sampleCoord;\n" +
		
		" void main() {\n" +
		"  sampleCoord = tCoord;\n" +
		" gl_Position = vec4(vPosition,1.0);\n" +
		" } ";
		
		String fShaderHeightField = "#version 400\n" +
		" in vec2 sampleCoord;\n" +
		" out vec4 outHeight;\n" +
		" out vec4 outVelocity;\n" +
		" uniform sampler2D inHeight;\n"+
		" uniform sampler2D inVelocity;\n"+
		" uniform float dx;\n" +
		" uniform float dy;\n" +
		
		
		" void main() {\n" +
		"   float h = texture(inHeight, sampleCoord).r;\n" +
		"   float v = texture(inVelocity, sampleCoord).r;\n" +
		"   float avg = (texture(inHeight, sampleCoord - vec2(1.0*dx, 0.0)).r + texture(inHeight, sampleCoord - vec2(0.0, 1.0*dy)).r + texture(inHeight, sampleCoord + vec2(1.0*dx, 0.0)).r + texture(inHeight, sampleCoord + vec2(0.0, 1.0*dy)).r)*0.25;\n" +
		"v = v + (avg - h)*2.0;\n" +
		"   v = 1.00 * v;\n" + 
		"   h = h + v;\n" +
		" float t = 1.0;\n" +
	
		
		" outHeight = vec4(h, 0.0, 0.0, 1.0);\n" +
		" outVelocity = vec4(v, 0.0, 0.0, 1.0);\n" + 
		"   } ";
		
		String fShaderDrop = "#version 400\n" +
		" const float Pi = 3.141592653589793;\n" +
		" uniform sampler2D inHeight;\n" +
		" uniform sampler2D inVelocity;\n" +
		" uniform vec2 center;\n" +
		" uniform float radius;\n" +
		" uniform float strength;\n " +
		" in vec2 sampleCoord;\n" +
		" out vec4 outHeight;\n" +
		" out vec4 outVelocity;\n" +
		
		" void main() {\n" +
		" float h = texture(inHeight,sampleCoord).r;\n" +
		" float v = texture(inVelocity,sampleCoord).r;\n" +
		" float drop = max(1.0 - length(center*0.5 + 0.5 - sampleCoord)/radius, 0.0);\n"+
		" drop = 0.5 - 0.5*cos(Pi*drop);\n" +
		" h = h + strength*drop;\n" +
		" outHeight = vec4(h, 0.0, 0.0, 1.0);" +
		" outVelocity = vec4(v, 0.0, 0.0, 1.0);\n" +
		" } ";
		
		String fShaderNormals = "#version 400\n" +
		" uniform sampler2D inHeight;\n" +
		" uniform float dx;\n"+
		" uniform float dy;\n" +
		"uniform float xScale;\n"+
		"uniform float yScale;\n"+
		"uniform float zScale;\n"+
		" in vec2 sampleCoord;\n" +
		" out vec4 outNormalX;\n" +
		" out vec4 outNormalZ;\n" + 
		
		
		"void main() {\n"+
		"    float h = yScale*texture(inHeight,sampleCoord).r;\n" +
		" vec3 dxVec = vec3(20.0*dx,(yScale*texture(inHeight,vec2(sampleCoord.x + dx, sampleCoord.y)).r - h), 0.0);\n" +
		" vec3 dyVec = vec3(0.0, (yScale*texture(inHeight, vec2(sampleCoord.x, sampleCoord.y + dy)).r - h), 20.0*dy);\n" +
		
		" vec2 v = normalize(cross(dyVec,dxVec)).xz;\n"+
		
		"    outNormalX = vec4(v.x, 0.0, 0.0, 1.0);\n" +
		"    outNormalZ = vec4(v.y, 0.0, 0.0, 1.0);\n" +
		" }";
		
		
		String fShaderCopy = "#version 400\n" +
		" in vec2 sampleCoord;\n" +
		" uniform sampler2D inHeight;\n" +
		"uniform sampler2D inVelocity;\n" +
		" out vec4 outHeight;\n" +
		" out vec4 outVelocity;\n" +
		" void main() {\n" +
		" outHeight = vec4(texture(inHeight,sampleCoord).r, 0.0, 0.0, 1.0);\n" +
		" outVelocity = vec4(texture(inVelocity,sampleCoord).r, 0.0, 0.0, 1.0);\n" +
		" }";
		
		String vShaderWater = "#version 400\n" +
		" layout (location = 0) in vec3 vPosition;\n" +
		" layout (location = 1) in vec2 tCoords;\n" +
		       
		" uniform sampler2D inHeight;\n" +
		"uniform sampler2D inNormalX;\n" +
		" uniform sampler2D inNormalZ;\n" +
		" uniform mat4 uMVP;\n"+
		" uniform mat4 uWorld;\n"+
	    
		" uniform float width;\n" +
		" uniform float height;\n" +
	    " out vec3 fPosition;\n" +
	    " out vec3 fPosMVP;\n" +
		
	    " out vec2 sampleCoord;\n" +
	    "out vec3 fNormal;\n" +
		
		" out vec3 fPosWorld;"+
		" void main() {\n" +
		"   fPosition = vPosition;\n" +
		"   sampleCoord = vec2(vPosition.x,(-1.0*vPosition.z));\n" +
		"   float nX = texture(inNormalX, vec2(vPosition.x,(-1.0*vPosition.z))).r;\n" +
		"   float nZ = texture(inNormalZ, vec2(vPosition.x,(-1.0*vPosition.z))).r;\n" +
		"   vec3 normal = vec3(nX, sqrt(1.0 - dot(vec2(nX,nZ), vec2(nX,nZ))), nZ);\n" +
		"   normal = normalize(normal);\n" +
		
		" fNormal = normal;\n" +
		
		"   vec4 waterPos = vec4(vPosition.x,texture(inHeight,vec2( vPosition.x ,( -1.0*vPosition.z) )).r,vPosition.z,1.0);"+
		"   fPosWorld = (uWorld*waterPos).xyz;"+
		"   fPosMVP = (uMVP*waterPos).xyz;"+
		"   gl_Position = uMVP*waterPos;\n"+
		" } ";
		
		String fShaderWater = "#version 400\n"+
		" in vec3 fPosition;\n" +
         " in vec3 fPosWorld;"+
         " in vec3 fPosMVP;\n" +
 		
		"layout (location = 0) out vec4 albedoColor;\n"+
		"layout (location = 1) out vec4 colorBright;\n"+
		"layout (location = 2) out vec4 normal;\n"+
		"layout (location = 3) out vec4 position;\n"+
		"layout (location = 4) out vec4 sceneShadowAmount;\n"+
		"layout (location = 5) out vec4 refractiveObject;\n"+
		"layout (location = 6) out vec4 shadowBlurRadius;\n"+
		"layout (location = 7) out vec4 waterPosWorld;\n"+
		
		" uniform sampler2D inHeight;\n" +
		" uniform sampler2D inNormalX;\n" +
		" uniform sampler2D inNormalZ;\n" +
		" uniform float width;\n" +
		"uniform float height;\n" +
		" in vec2 sampleCoord;\n" +
		" in vec3 fNormal;\n" +
		"uniform float screenBrightness;\n" +
		
		" void main() {\n" +
		
		" vec3 light = normalize(1.0*vec3(0.001,0.039,0.932));\n" +
		
		" float theNormalX = texture(inNormalX, sampleCoord).r;"+
		" float theNormalZ = texture(inNormalZ, sampleCoord).r;"+
		
		" vec2 samplerVec = sampleCoord;"+
		
		" float normalPeakScalar = 0.005;"+
		" if (fPosMVP.z <= 4.1) {"+
		"  normalPeakScalar = mix(0.0026, 0.0011, clamp( fPosMVP.z /4.1, 0.0, 1.0));"+
	    " }"+
		" else if (fPosMVP.z <= 10.1) {"+
	    "  normalPeakScalar = mix(0.0011, 0.0011, clamp( (fPosMVP.z - 4.1)/6.0, 0.0, 1.0));"+
		" }"+
	    " else {"+
		"  normalPeakScalar = mix(0.0011, 0.0011, clamp( (fPosMVP.z - 10.1) / 19.1, 0.0, 1.0));"+ 
	    " }"+
		
	    
		
		//"     normalPeakScalar = 0.0011; "+
		
		
		" for (int k = 0; k < 3; k = k + 1) {"+
		"   samplerVec = samplerVec + normalPeakScalar*vec2(theNormalX,1.0*theNormalZ);    "+
		"  theNormalX =  texture(inNormalX, samplerVec).r;"+
		"  theNormalZ =  texture(inNormalZ, samplerVec).r;"+
		
		" }"+
		"   vec3 theNormal = vec3(theNormalX, sqrt(1.0 - dot(vec2(theNormalX,theNormalZ), vec2(theNormalX,theNormalZ))), theNormalZ);\n" +
		
		
		" albedoColor = vec4(0.02, 0.39, 0.43, 1.0);"+
		//" colorBright = vec4(0.0, 0.0, 0.0, 1.0);"+
		" normal = vec4(normalize(theNormal), 1.0);"+
		" refractiveObject = vec4(1.0, 1.0, 1.0, 1.0);"+
		//"   fragColor = vec4(screenBrightness*(0.93*vec3(0.02,0.39,0.43) + 0.07*dot(light,normalize(fNormal))*vec3(0.02,0.39,0.43)),0.99);\n"+
		" shadowBlurRadius = vec4(0.0,0.0,0.0,1.0);"+
		" waterPosWorld = vec4(fPosWorld.x,fPosWorld.y,fPosWorld.z,1.0);"+
		" } ";
		// Color was 0.02, 0.814,0.9
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//Test VShader and FShader
		
		String vShader = "#version 400\n" +
		" layout (location = 0) in vec3 vPos;\n"+
	    
		" void main() {\n"+
		"  gl_Position = vec4(vPos,1.0);\n " +
		" }";
		
		String fShader = "#version 400\n" +
		" out float outHeight;\n" +
		" out float outVelocity;\n" +
		" void main() {\n"+
		" outHeight = 0.0;\n" +
		" outVelocity = 0.0;\n" +
		" }";
		
		// Height Field Shader Program
		
		

		
		
		
		
		
		
		
        
        heightFieldProgramID = glCreateProgram();
        glUseProgram(heightFieldProgramID);
        
        if (heightFieldProgramID == 0) {
            throw new Exception("Could not create Shader");
        }
        
        
        heightFieldVShaderID = glCreateShader(GL_VERTEX_SHADER);
        if (heightFieldVShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
        }

        glShaderSource(heightFieldVShaderID, vShaderHeightField);
        glCompileShader(heightFieldVShaderID);

        if (glGetShaderi(heightFieldVShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(heightFieldVShaderID, 1024));
        }

        glAttachShader(heightFieldProgramID, heightFieldVShaderID);

        
        
        
        

        heightFieldFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        if (heightFieldFShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
        }

        glShaderSource(heightFieldFShaderID, fShaderHeightField);
        glCompileShader(heightFieldFShaderID);

        if (glGetShaderi(heightFieldFShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(heightFieldFShaderID, 1024));
        }

        glAttachShader(heightFieldProgramID, heightFieldFShaderID);
        
        
        
        
        
        
        
        
        glLinkProgram(heightFieldProgramID);
        if (glGetProgrami(heightFieldProgramID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(heightFieldProgramID, 1024));
        }

        if (heightFieldVShaderID != 0) {
            glDetachShader(heightFieldProgramID, heightFieldVShaderID);
        }
        if (heightFieldFShaderID != 0) {
            glDetachShader(heightFieldProgramID, heightFieldFShaderID);
        }

        glValidateProgram(heightFieldProgramID);
        if (glGetProgrami(heightFieldProgramID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(heightFieldProgramID, 1024));
        }
    
	
        glUseProgram(heightFieldProgramID);
        
        
        
        heightFieldInHeightLocation = glGetUniformLocation(heightFieldProgramID,"inHeight");
        heightFieldInVelocityLocation = glGetUniformLocation(heightFieldProgramID,"inVelocity");
        
        glUniform1i(heightFieldInHeightLocation,0);
        glUniform1i(heightFieldInVelocityLocation,1);
        
        
        heightFieldShaderDXLocation = glGetUniformLocation(heightFieldProgramID,"dx");
        heightFieldShaderDYLocation = glGetUniformLocation(heightFieldProgramID,"dy");
        
        
       // glUniform1i(heightFieldShaderSamplerLocation,11);
        
        

		 verticesBuffer = null;
	//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(screenQuad.length);
	            System.out.println("Length of vertices is " + screenQuad.length);
	//   verticesBuffer = 
	//	vertexCount = vCoords.length / 3;
	            (verticesBuffer.put(screenQuad)).flip();
//glGenVertexArrays();
	           heightFieldVAOID = glGenVertexArrays();
	           System.out.println("Vao is " + heightFieldVAOID);
	            glBindVertexArray(heightFieldVAOID);

	            heightFieldVBOIDV = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, heightFieldVBOIDV);
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
	        
        verticesBuffer = null;
		 
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(screenTCoords.length);
	            System.out.println("Length of vertices is " + screenQuad.length);
	//   verticesBuffer = 
	//	vertexCount = vCoords.length / 3;
	            (verticesBuffer.put(screenTCoords)).flip();
//glGenVertexArrays();
	           //heightFieldVAOID = glGenVertexArrays();
	           //System.out.println("Vao is " + heightFieldVAOID);
	            glBindVertexArray(heightFieldVAOID);

	            heightFieldVBOIDT = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, heightFieldVBOIDT);
	            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
	            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	            
	            glBindVertexArray(0);
	            glBindBuffer(GL_ARRAY_BUFFER, 0);

	            //glBindVertexArray(0);         
	        } finally {
	            if (verticesBuffer  != null) {
	         //   	System.exit(0);
	                MemoryUtil.memFree(verticesBuffer);
	            }
	        }
	        
        
        
        
        
        
        
        
        
        
        
        
        
        // Drop Shader Program
        
        

		
		
		
		
		
		
		
        
        dropProgramID = glCreateProgram();
        glUseProgram(dropProgramID);
        
        if (dropProgramID == 0) {
            throw new Exception("Could not create Shader");
        }
        
        
        dropVShaderID = glCreateShader(GL_VERTEX_SHADER);
        if (dropVShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
        }

        glShaderSource(dropVShaderID, vShaderHeightField);
        glCompileShader(dropVShaderID);

        if (glGetShaderi(dropVShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(dropVShaderID, 1024));
        }

        glAttachShader(dropProgramID, dropVShaderID);

        
        
        
        

        dropFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        if (dropFShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
        }

        glShaderSource(dropFShaderID, fShaderDrop);
        glCompileShader(dropFShaderID);

        if (glGetShaderi(dropFShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(dropFShaderID, 1024));
        }

        glAttachShader(dropProgramID, dropFShaderID);
        
        
        
        
        
        
        
        
        glLinkProgram(dropProgramID);
        if (glGetProgrami(dropProgramID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(dropProgramID, 1024));
        }

        if (dropVShaderID != 0) {
            glDetachShader(dropProgramID, dropVShaderID);
        }
        if (dropFShaderID != 0) {
            glDetachShader(dropProgramID, dropFShaderID);
        }

        glValidateProgram(dropProgramID);
        if (glGetProgrami(dropProgramID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(dropProgramID, 1024));
        }
    
	
        glUseProgram(dropProgramID);
        
        
        
        

        

		 verticesBuffer = null;
	//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(screenQuad.length);
	            System.out.println("Length of vertices is " + screenQuad.length);
	//   verticesBuffer = 
	//	vertexCount = vCoords.length / 3;
	            (verticesBuffer.put(screenQuad)).flip();
//glGenVertexArrays();
	           dropVAOID = glGenVertexArrays();
	           System.out.println("Vao is " + heightFieldVAOID);
	            glBindVertexArray(dropVAOID);

	            dropVBOIDV = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, dropVBOIDV);
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
		    
		    
		    
		    

			 
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(screenTCoords.length);
	            System.out.println("Length of vertices is " + screenQuad.length);
	//   verticesBuffer = 
	//	vertexCount = vCoords.length / 3;
	            (verticesBuffer.put(screenTCoords)).flip();
//glGenVertexArrays();
	           //heightFieldVAOID = glGenVertexArrays();
	           //System.out.println("Vao is " + heightFieldVAOID);
	            glBindVertexArray(dropVAOID);

	            dropVBOIDT = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, dropVBOIDT);
	            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
	            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	            
	            glBindVertexArray(0);
	            glBindBuffer(GL_ARRAY_BUFFER, 0);

	            //glBindVertexArray(0);         
	        } finally {
	            if (verticesBuffer  != null) {
	         //   	System.exit(0);
	                MemoryUtil.memFree(verticesBuffer);
	            }
	        }
	        
        
		    
		    dropInHeightLocation = glGetUniformLocation(dropProgramID,"inHeight");
		    dropInVelocityLocation = glGetUniformLocation(dropProgramID,"inVelocity");
		    dropCenterLocation = glGetUniformLocation(dropProgramID,"center");
		    dropStrengthLocation = glGetUniformLocation(dropProgramID,"strength");
		    dropRadiusLocation = glGetUniformLocation(dropProgramID,"radius");
		    
		    glUniform1i(dropInHeightLocation,0);
		    glUniform1i(dropInVelocityLocation,1);
	        
       
		   // glUniform1i(dropSamplerLocation, 10);
       
        
		    

		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    // Normal Shader Program

			
			
			
	        
	        normalProgramID = glCreateProgram();
	        glUseProgram(normalProgramID);
	        
	        if (normalProgramID == 0) {
	            throw new Exception("Could not create Shader");
	        }
	        
	        
	        normalVShaderID = glCreateShader(GL_VERTEX_SHADER);
	        if (dropVShaderID == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
	        }

	        glShaderSource(normalVShaderID, vShaderHeightField);
	        glCompileShader(normalVShaderID);

	        if (glGetShaderi(normalVShaderID, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(normalVShaderID, 1024));
	        }

	        glAttachShader(normalProgramID, normalVShaderID);

	        
	        
	        
	        

	        normalFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
	        if (normalFShaderID == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
	        }

	        glShaderSource(normalFShaderID, fShaderNormals);
	        glCompileShader(normalFShaderID);

	        if (glGetShaderi(normalFShaderID, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(normalFShaderID, 1024));
	        }

	        glAttachShader(normalProgramID, normalFShaderID);
	        
	        
	        
	        
	        
	        
	        
	        
	        glLinkProgram(normalProgramID);
	        if (glGetProgrami(normalProgramID, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(normalProgramID, 1024));
	        }

	        if (normalVShaderID != 0) {
	            glDetachShader(normalProgramID, normalVShaderID);
	        }
	        if (normalFShaderID != 0) {
	            glDetachShader(normalProgramID, normalFShaderID);
	        }

	        glValidateProgram(normalProgramID);
	        if (glGetProgrami(normalProgramID, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(normalProgramID, 1024));
	        }
	    
		
	        glUseProgram(normalProgramID);
	        
	        
	        
	        

	        

			 verticesBuffer = null;
		//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
			    try {
		            verticesBuffer = MemoryUtil.memAllocFloat(screenQuad.length);
		            System.out.println("Length of vertices is " + screenQuad.length);
		//   verticesBuffer = 
		//	vertexCount = vCoords.length / 3;
		            (verticesBuffer.put(screenQuad)).flip();
	//glGenVertexArrays();
		           normalVAOID = glGenVertexArrays();
		           System.out.println("Vao is " + heightFieldVAOID);
		            glBindVertexArray(normalVAOID);

		           // glEnableVertexAttribArray(0);
		            normalVBOIDV = glGenBuffers();
		            glBindBuffer(GL_ARRAY_BUFFER, normalVBOIDV);
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
			    
			    
			    
			    

				 
			    try {
		            verticesBuffer = MemoryUtil.memAllocFloat(screenTCoords.length);
		            System.out.println("Length of vertices is " + screenQuad.length);
		//   verticesBuffer = 
		//	vertexCount = vCoords.length / 3;
		            (verticesBuffer.put(screenTCoords)).flip();
	//glGenVertexArrays();
		           //heightFieldVAOID = glGenVertexArrays();
		           //System.out.println("Vao is " + heightFieldVAOID);
		            glBindVertexArray(normalVAOID);
		            //glEnableVertexAttribArray(1);

		            normalVBOIDT = glGenBuffers();
		            glBindBuffer(GL_ARRAY_BUFFER, normalVBOIDT);
		            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
		            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		            
		            glBindVertexArray(0);
		            glBindBuffer(GL_ARRAY_BUFFER, 0);

		            //glBindVertexArray(0);         
		        } finally {
		            if (verticesBuffer  != null) {
		         //   	System.exit(0);
		                MemoryUtil.memFree(verticesBuffer);
		            }
		        }
		        
	        
			    
			    normalInHeightLocation = glGetUniformLocation(normalProgramID,"inHeight");
			    //dropCenterLocation = glGetUniformLocation(dropProgramID,"center");
			    //dropStrengthLocation = glGetUniformLocation(dropProgramID,"strength");
			    //dropRadiusLocation = glGetUniformLocation(dropProgramID,"radius");
		        normalDXLocation = glGetUniformLocation(normalProgramID,"dx");
		        normalDYLocation = glGetUniformLocation(normalProgramID,"dy");
	            normalXScaleLocation = glGetUniformLocation(normalProgramID,"xScale");
	            normalYScaleLocation = glGetUniformLocation(normalProgramID,"yScale");
	            normalZScaleLocation = glGetUniformLocation(normalProgramID,"zScale");
		        
		        glUniform1i(normalInHeightLocation,0);
			   // glUniform1i(dropSamplerLocation, 10);
	       
	        
        
        
        // Water Shader Program
        
        

		
		
		
		
		
		
		
        
        waterProgramID = glCreateProgram();
        glUseProgram(waterProgramID);
        
        if (waterProgramID == 0) {
            throw new Exception("Could not create Shader");
        }
        
        
        waterVShaderID = glCreateShader(GL_VERTEX_SHADER);
        if (waterVShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
        }

        glShaderSource(waterVShaderID, vShaderWater);
        glCompileShader(waterVShaderID);

        if (glGetShaderi(waterVShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(waterVShaderID, 1024));
        }

        glAttachShader(waterProgramID, waterVShaderID);

        
        
        
        

        waterFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        if (waterFShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
        }

        glShaderSource(waterFShaderID, fShaderWater);
        glCompileShader(waterFShaderID);

        if (glGetShaderi(waterFShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(waterFShaderID, 1024));
        }

        glAttachShader(waterProgramID, waterFShaderID);
        
        
        
        
        
        
        
        
        glLinkProgram(waterProgramID);
        if (glGetProgrami(waterProgramID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(waterProgramID, 1024));
        }

        if (waterVShaderID != 0) {
            glDetachShader(waterProgramID, waterVShaderID);
        }
        if (waterFShaderID != 0) {
            glDetachShader(waterProgramID, waterFShaderID);
        }

        glValidateProgram(waterProgramID);
        if (glGetProgrami(waterProgramID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(waterProgramID, 1024));
        }
    
	
        glUseProgram(waterProgramID);
        
        waterVAOID = glGenVertexArrays();
        glBindVertexArray(waterVAOID);
        
        waterVBOIDV = glGenBuffers();
       // waterVBOIDT = glGenBuffers();
       // waterVBOIDN = glGenBuffers();

        theHeightFieldIndexBufferID = glGenBuffers();

        
        theHeightFieldIndexBuffer = MemoryUtil.memAllocInt(6*(width - 1)*(height - 1));
        theHeightFieldIndexBuffer.put(ind).flip();
        

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,theHeightFieldIndexBufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,theHeightFieldIndexBuffer,GL_STATIC_DRAW);
        
        
        this.constructMesh();

        
		  verticesBuffer = null;
	//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(vCoords.length);
	            System.out.println("Length of vertices is " + vCoords.length);
	//   verticesBuffer = 
		 //vertexCount = vertices.length / 3;
	            (verticesBuffer.put(vCoords)).flip();
//glGenVertexArrays();
	           
	           //System.out.println("Vao is " + vaoId);
	            glBindVertexArray(waterVAOID);
	           // glEnableVertexAttribArray(0);

	            //vboId = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, waterVBOIDV);
	            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
	            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
	            
	            

	            glBindVertexArray(0);
	            glBindBuffer(GL_ARRAY_BUFFER, 0);

	            //glBindVertexArray(0);
	            //glBindBuffer(GL_ARRAY_BUFFER, 0);

	            //glBindVertexArray(0);         
	        } finally {
	            if (verticesBuffer  != null) {
	         //   	System.exit(0);
	                MemoryUtil.memFree(verticesBuffer);
	            }
	        }
		    
		    

	        
			  verticesBuffer = null;
		//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
			    try {
		            verticesBuffer = MemoryUtil.memAllocFloat(screenTCoords.length);
		            System.out.println("Length of vertices is " + vCoords.length);
		//   verticesBuffer = 
			 //vertexCount = vertices.length / 3;
		            (verticesBuffer.put(screenTCoords)).flip();
	//glGenVertexArrays();
		           
		           //System.out.println("Vao is " + vaoId);
		            glBindVertexArray(waterVAOID);
		           // glEnableVertexAttribArray(1);

		            //vboId = glGenBuffers();
		            waterVBOIDT = glGenBuffers();
		            glBindBuffer(GL_ARRAY_BUFFER, waterVBOIDT);
		            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
		            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		            
		            

		            glBindVertexArray(0);
		            glBindBuffer(GL_ARRAY_BUFFER, 0);

		            //glBindVertexArray(0);
		            //glBindBuffer(GL_ARRAY_BUFFER, 0);

		            //glBindVertexArray(0);         
		        } finally {
		            if (verticesBuffer  != null) {
		         //   	System.exit(0);
		                MemoryUtil.memFree(verticesBuffer);
		            }
		        }
		        
	        
        
        waterUMVPLocation = glGetUniformLocation(waterProgramID,"uMVP");
        waterUWorldLocation = glGetUniformLocation(waterProgramID,"uWorld");
        
        waterInHeightLocation = glGetUniformLocation(waterProgramID,"inHeight");
        waterInNormalXLocation = glGetUniformLocation(waterProgramID,"inNormalX");
        waterInNormalZLocation = glGetUniformLocation(waterProgramID,"inNormalZ");
        
        waterWidthLocation = glGetUniformLocation(waterProgramID,"width");
        waterHeightLocation = glGetUniformLocation(waterProgramID,"height");
        
        waterScreenBrightnessLocation = glGetUniformLocation(waterProgramID,"screenBrightness");
        
        glUniform1i(waterInHeightLocation,0);
        glUniform1i(waterInNormalXLocation,1);
        glUniform1i(waterInNormalZLocation,2);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        // Test Shader
        
        

		
		
		
		
		
		
        
        programID = glCreateProgram();
        glUseProgram(programID);
        
        if (programID == 0) {
            throw new Exception("Could not create Shader");
        }
        
        
        vShaderID = glCreateShader(GL_VERTEX_SHADER);
        if (vShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
        }

        glShaderSource(vShaderID, vShader);
        glCompileShader(vShaderID);

        if (glGetShaderi(vShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(vShaderID, 1024));
        }

        glAttachShader(programID, vShaderID);

        
        
        
        

        fShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        if (fShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
        }

        glShaderSource(fShaderID, fShader);
        glCompileShader(fShaderID);

        if (glGetShaderi(fShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(fShaderID, 1024));
        }

        glAttachShader(programID, fShaderID);
        
        
        
        
        
        
        
        
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programID, 1024));
        }

        if (vShaderID != 0) {
            glDetachShader(programID, vShaderID);
        }
        if (fShaderID != 0) {
            glDetachShader(programID, fShaderID);
        }

        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
        }
    
	
        glUseProgram(programID);
        
        
        


		 verticesBuffer = null;
	//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(screenQuad.length);
	            System.out.println("Length of vertices is " + screenQuad.length);
	//   verticesBuffer = 
	//	vertexCount = vCoords.length / 3;
	            (verticesBuffer.put(screenQuad)).flip();
//glGenVertexArrays();
	           vAOID = glGenVertexArrays();
	           System.out.println("Vao is " + heightFieldVAOID);
	            glBindVertexArray(vAOID);

	            vBOIDV = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, vBOIDV);
	            glEnableVertexAttribArray(0);
	            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
	            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
	            
	            glBindVertexArray(0);
	            glBindBuffer(GL_ARRAY_BUFFER, 0);
	            
	            glUseProgram(0);

	            //glBindVertexArray(0);         
	        } finally {
	            if (verticesBuffer  != null) {
	         //   	System.exit(0);
	                MemoryUtil.memFree(verticesBuffer);
	            }
	        }
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    

	        
	        copyProgramID = glCreateProgram();
	        glUseProgram(copyProgramID);
	        
	        if (copyProgramID == 0) {
	            throw new Exception("Could not create Shader");
	        }
	        
	        
	        copyVShaderID = glCreateShader(GL_VERTEX_SHADER);
	        if (vShaderID == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
	        }

	        glShaderSource(copyVShaderID, vShaderHeightField);
	        glCompileShader(copyVShaderID);

	        if (glGetShaderi(copyVShaderID, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(copyVShaderID, 1024));
	        }

	        glAttachShader(copyProgramID, copyVShaderID);

	        
	        
	        
	        

	        copyFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
	        if (copyFShaderID == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
	        }

	        glShaderSource(copyFShaderID, fShaderCopy);
	        glCompileShader(copyFShaderID);

	        if (glGetShaderi(fShaderID, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(copyFShaderID, 1024));
	        }

	        glAttachShader(copyProgramID, copyFShaderID);
	        
	        
	        
	        
	        
	        
	        
	        
	        glLinkProgram(copyProgramID);
	        if (glGetProgrami(copyProgramID, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(copyProgramID, 1024));
	        }

	        if (copyVShaderID != 0) {
	            glDetachShader(copyProgramID, copyVShaderID);
	        }
	        if (copyFShaderID != 0) {
	            glDetachShader(copyProgramID, copyFShaderID);
	        }

	        glValidateProgram(copyProgramID);
	        if (glGetProgrami(copyProgramID, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(copyProgramID, 1024));
	        }
	    
		
	        glUseProgram(copyProgramID);
	        
	        
	        


			 verticesBuffer = null;
		//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
			    try {
		            verticesBuffer = MemoryUtil.memAllocFloat(screenQuad.length);
		            System.out.println("Length of vertices is " + screenQuad.length);
		//   verticesBuffer = 
		//	vertexCount = vCoords.length / 3;
		            (verticesBuffer.put(screenQuad)).flip();
	//glGenVertexArrays();
		           copyVAOID = glGenVertexArrays();
		           System.out.println("Vao is " + heightFieldVAOID);
		            glBindVertexArray(copyVAOID);

		            copyVBOIDV = glGenBuffers();
		            glBindBuffer(GL_ARRAY_BUFFER, copyVBOIDV);
		         //   glEnableVertexAttribArray(0);
		            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
		            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		            
		            glBindVertexArray(0);
		            glBindBuffer(GL_ARRAY_BUFFER, 0);
		            
		            glUseProgram(0);

		            //glBindVertexArray(0);         
		        } finally {
		            if (verticesBuffer  != null) {
		         //   	System.exit(0);
		                MemoryUtil.memFree(verticesBuffer);
		            }
		        }
			    
			    
			    
			    
			    
			    
			    
			    
			    



				 verticesBuffer = null;
			//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
				    try {
			            verticesBuffer = MemoryUtil.memAllocFloat(screenTCoords.length);
			            System.out.println("Length of vertices is " + screenQuad.length);
			//   verticesBuffer = 
			//	vertexCount = vCoords.length / 3;
			            (verticesBuffer.put(screenTCoords)).flip();
		//glGenVertexArrays();
			           
			           System.out.println("Vao is " + heightFieldVAOID);
			            glBindVertexArray(copyVAOID);

			            copyVBOIDT = glGenBuffers();
			            glBindBuffer(GL_ARRAY_BUFFER, copyVBOIDT);
			        //    glEnableVertexAttribArray(1);
			            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
			            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			            
			            glBindVertexArray(0);
			            glBindBuffer(GL_ARRAY_BUFFER, 0);
			            
			            glUseProgram(0);

			            //glBindVertexArray(0);         
			        } finally {
			            if (verticesBuffer  != null) {
			         //   	System.exit(0);
			                MemoryUtil.memFree(verticesBuffer);
			            }
			        }
			    
				    
				    
				    copyInHeightLocation = glGetUniformLocation(copyProgramID,"inHeight");
		            copyInVelocityLocation = glGetUniformLocation(copyProgramID,"inVelocity");
		            
		            glUniform1i(copyInHeightLocation,0);
		            glUniform1i(copyInVelocityLocation,1);
		    
       // waterVBOIDT = glGenBuffers();
       // waterVBOIDN = glGenBuffers();
        
        //waterUMVPLocation = glGetUniformLocation(waterProgramID,"uMVP");
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        vCoords = new float[3*width*height];
        
        glUseProgram(programID);
        glBindFramebuffer(GL_FRAMEBUFFER,prevHeightFieldFBO);
        glViewport(0,0,width,height);
        glBindTexture(GL_TEXTURE_2D,0);
        //glUseProgram(programID);
        glClearColor(0.0f,0.0f,0.0f,1.0f);
        glDrawBuffers(drawBuffersList);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glBindVertexArray(vAOID);
        glEnableVertexAttribArray(0);
        
       // glDrawBuffers(GL_COLOR_ATTACHMENT0);
        glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
        
        glDisableVertexAttribArray(0);
        
        glBindVertexArray(0);
        
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        glViewport(0,0,1920,1080);
        
       // glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      //  update();
       // constructMesh();
        
        
        
        
       environmentCubeMapID = glGenTextures();
       glActiveTexture(GL_TEXTURE0);
       glBindTexture(GL_TEXTURE_CUBE_MAP,environmentCubeMapID);
       

glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGB, 1080,
    1080, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);


glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGB, 1080,
    1080, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);


glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGB, 1080,
    1080, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);


glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGB, 1080,
    1080, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);


glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGB, 1080,
    1080, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);


glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGB, 1080,
    1080, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);

               

glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);  



environmentCubeMapFBO = glGenFramebuffers();
glBindFramebuffer(GL_FRAMEBUFFER,environmentCubeMapFBO);
glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_POSITIVE_X,environmentCubeMapID,0);
glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_CUBE_MAP_NEGATIVE_X,environmentCubeMapID,0);
glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT2,GL_TEXTURE_CUBE_MAP_POSITIVE_Y,environmentCubeMapID,0);
glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT3,GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,environmentCubeMapID,0);
glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT4,GL_TEXTURE_CUBE_MAP_POSITIVE_Z,environmentCubeMapID,0);
glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT5,GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,environmentCubeMapID,0);



int renderBuffer = glGenRenderbuffers();
glBindRenderbuffer(GL_RENDERBUFFER,renderBuffer);
glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH_COMPONENT,1024,1024);

glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER,renderBuffer);

glBindFramebuffer(GL_FRAMEBUFFER,0);
glBindRenderbuffer(GL_RENDERBUFFER,0);

       
		
	}
	
	
	
	
	
	public void constructMesh() {
		
		//glBindTexture(GL_TEXTURE_2D,curHeightFieldID);
		//glGetTexImage(GL_TEXTURE_2D,0,GL_RGBA,GL_FLOAT,theHeightField);
		
		//theHeightField.get(vCoords);
		 
	//	vCoords = new float[3*width*height];
		int index = 0;
		
		for (int a = 0; a < width; a++) {
		
			for (int b = 0; b < height; b++) {
				
			vCoords[index] = 1.0f*(float)a*(1.0f)/((float)width);
				//vCoords[index +1] = theHeightField.get(4*(b*width + a));
			vCoords[index + 1] = 0.0f;	
			vCoords[index + 2] = -1.0f*(float)b*(1.0f)/((float)height);
				/*
				// Upper Left Vertex
				
				vCoords[index] = (float)a;
				vCoords[index + 1] = theHeightField.get(4*(b*width + a)) ;
				vCoords[index + 2] = (float)b ;
			
			    // Bottom Left Vertex
				
				vCoords[index + 3] = (float)a;
				vCoords[index + 4] = theHeightField.get(4*((b + 1)*width + a)) ;
				vCoords[index + 5] = (float)(b + 1);
				
				// Bottom Right Vertex 
				
				vCoords[index + 6] = (float)(a + 1);
				vCoords[index + 7] = theHeightField.get(4*((b + 1)*width + (a + 1))) ;
				vCoords[index + 8] = (float)(b + 1);
				

				// Upper Left Vertex
				
				vCoords[index + 9] = (float)a;
				vCoords[index + 10] = theHeightField.get(4*(b*width + a)) ;
				vCoords[index + 11] = (float)b ;
				

				// Bottom Right Vertex 
				
				vCoords[index + 12] = (float)(a + 1);
				vCoords[index + 13] = theHeightField.get(4*((b + 1)*width + (a + 1))) ;
				vCoords[index + 14] = (float)(b + 1) ;
				
				// Top Right Vertex 
				
				vCoords[index + 15] = (float)(a + 1);
				vCoords[index + 16] = theHeightField.get(4*(b*width + (a + 1))) ;
				vCoords[index + 17] = (float)b  ;
				*/
				index = index + 3;
				
			}
		}
		
		
	}
	
	public void update() {
		
		
		
		 
		//glUseProgram(heightFieldProgramID);
		
		//glBindTexture(GL_TEXTURE_2D,0);

		glBindFramebuffer(GL_FRAMEBUFFER,curHeightFieldFBO);
		glUseProgram(heightFieldProgramID);
		glBindTexture(GL_TEXTURE_2D,0);
		glDrawBuffers(drawBuffersList);
		
	//	glBindTexture(GL_TEXTURE_2D,0);

		glClearColor(1.0f,0.0f,0.0f,1.0f);
		glViewport(0,0,width,height);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		
		float dx = (1.0f)/((float)width);
		float dy = (1.0f)/((float)height);
		glUniform1f(heightFieldShaderDXLocation,dx);
		glUniform1f(heightFieldShaderDYLocation,dy);
		

		
		
		glActiveTexture(GL_TEXTURE0);
		
		
		
		glBindTexture(GL_TEXTURE_2D,prevHeightID);
		glUniform1i(heightFieldInHeightLocation,0);
		
		glActiveTexture(GL_TEXTURE0 + 1);
		
		glBindTexture(GL_TEXTURE_2D,prevVelocityID);
		glUniform1i(heightFieldInVelocityLocation,1);
		
		glBindVertexArray(heightFieldVAOID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
//		glBindTexture(GL_TEXTURE_2D,0);


		//glActiveTexture(GL_TEXTURE0 + 11);
		//glBindTexture(GL_TEXTURE_2D,curHeightFieldID);
		

		glActiveTexture(GL_TEXTURE0 + 3);
		glBindTexture(GL_TEXTURE_2D,0);
		
		
		glDrawBuffers(drawBuffersList);
        glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
        
       
	    	    
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
        glBindVertexArray(0);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        
        
        
        
        glUseProgram(copyProgramID);
        
        
        glBindFramebuffer(GL_FRAMEBUFFER,prevHeightFieldFBO);
        glUseProgram(copyProgramID);
        
        glBindTexture(GL_TEXTURE_2D,0);
        glDrawBuffers(drawBuffersList);
        glClearColor(0.9f,0.0f,0.0f,1.0f);
        glViewport(0,0,width,height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,curHeightID);
        glUniform1i(copyInHeightLocation,0);
        
        
        glActiveTexture(GL_TEXTURE0 + 1);
        
        glBindTexture(GL_TEXTURE_2D,curVelocityID);
        glUniform1i(copyInVelocityLocation, 1);
        
        
        glBindVertexArray(copyVAOID);
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        

		glActiveTexture(GL_TEXTURE0 + 3);
		glBindTexture(GL_TEXTURE_2D,0);
		
        glDrawBuffers(drawBuffersList);
        glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
        glBindVertexArray(0);
       glBindFramebuffer(GL_FRAMEBUFFER,0);
        
       	glBindTexture(GL_TEXTURE_2D,0);
       	
        glViewport(0,0,1920,1080);
        
        calculateNormals();
		
		
		
		//glBindTexture(GL_TEXTURE_2D,0);
/*
		glUseProgram(heightFieldProgramID);
		glBindFramebuffer(GL_FRAMEBUFFER,curHeightFieldFBO);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		glUseProgram(programID);
		glViewport(0,0,width,height);
		glClearColor(0.0f,0.4f,0.0f,0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
      
		glBindVertexArray(heightFieldVAOID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		//glBindTexture(GL_TEXTURE_2D,0);
		
		//glActiveTexture(GL_TEXTURE0 + 11);
	//	glBindTexture(GL_TEXTURE_2D,0);
		glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
		
		glDisableVertexAttribArray(0);
		
		glBindTexture(GL_TEXTURE_2D,0);
		glBindVertexArray(0);
		glBindFramebuffer(GL_FRAMEBUFFER,0);
		glViewport(0,0,1920,1080);
		
		*/
		

        //constructMesh();
	}
	
	
	
	
	
	
	
	public void addDrop(float[] center, float strength, float radius) {
		
		
		
		
		
		

		
		
		
		glBindFramebuffer(GL_FRAMEBUFFER,curHeightFieldFBO);
		glUseProgram(dropProgramID);
		glBindTexture(GL_TEXTURE_2D,0);


		glViewport(0,0,width,height);

		
		glClearColor(1.0f,0.0f,0.0f,1.0f);

		
		glDrawBuffers(drawBuffersList);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		float dx = (1.0f)/((float)width);
		float dy = (1.0f)/((float)height);
		glUniform1f(dropRadiusLocation,radius);
		glUniform1f(dropStrengthLocation,strength);
		

		System.out.println("Center of " + center[0] + ", " + center[1]);
		Vector2f centerLocation = new Vector2f(center[0],center[1]);
		
		
		   try (MemoryStack stack = MemoryStack.stackPush()) {
		        
	        	        FloatBuffer centerLocationV = stack.mallocFloat(2);
		
	        	        
	        	        
		
	        	        centerLocation.get(centerLocationV);
		   
		
		glUniform2fv(dropCenterLocation,centerLocationV);
		   }
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D,prevHeightID);
		glUniform1i(dropInHeightLocation,0);
		
		glActiveTexture(GL_TEXTURE0 + 1);
		glBindTexture(GL_TEXTURE_2D,prevVelocityID);
		glUniform1i(dropInVelocityLocation,1);
		
		//glBindTexture(GL_TEXTURE_2D,0);
		glBindVertexArray(dropVAOID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawBuffers(drawBuffersList);

		glActiveTexture(GL_TEXTURE0 + 3);
		glBindTexture(GL_TEXTURE_2D,0);
		
		
        glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
        
       

		glBindTexture(GL_TEXTURE_2D,0);
	       //glPixelStorei(GL_UNPACK_ALIGNMENT,1);

        
	    	    
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
        glBindVertexArray(0);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        

        
        
        glUseProgram(copyProgramID);
        
        
        glBindFramebuffer(GL_FRAMEBUFFER,prevHeightFieldFBO);
        glUseProgram(copyProgramID);
        
        glBindTexture(GL_TEXTURE_2D,0);
        glDrawBuffers(drawBuffersList);
        glClearColor(0.9f,0.0f,0.0f,1.0f);
        glViewport(0,0,width,height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,curHeightID);
        glUniform1i(copyInHeightLocation,0);
        
        glActiveTexture(GL_TEXTURE0 + 1);
        glBindTexture(GL_TEXTURE_2D,curVelocityID);
        glUniform1i(copyInVelocityLocation,1);
        
        glBindVertexArray(copyVAOID);
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        

		glActiveTexture(GL_TEXTURE0 + 3);
		glBindTexture(GL_TEXTURE_2D,0);
		
		
        glDrawBuffers(drawBuffersList);
        glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
        glBindVertexArray(0);
       glBindFramebuffer(GL_FRAMEBUFFER,0);
       
       
        glViewport(0,0,1920,1080);
        
        
        
        
        
        
        
        
        
		
	}
	
	public void renderToCubeMap(Background b, Player p, CubeMap c) {
		
		glBindFramebuffer(GL_FRAMEBUFFER,environmentCubeMapFBO);
		
		Matrix4f leftVM = new Matrix4f();
		leftVM.lookAt((float)b.getX() + 0.7f, (float)b.getY() + 0.2f,-2.0f, (float)b.getX() + 0.2f, (float)b.getY() + 0.2f, -2.0f, 0.0f, 1.0f, 0.0f);
        
		//p.animate(1.0f, 1, leftVM);
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	public void render(float screenBrightness, Camera camera) {
		
		//calculateNormals();
		
		glUseProgram(waterProgramID);

		//this.constructMesh();
		
		//this.calculateNormals();
		
//glBindFramebuffer(GL_FRAMEBUFFER,0);
		
		Matrix4f worldM = new Matrix4f();
		worldM.setTranslation(new Vector3f((float)(x ),(float)(y ),(float)(z )));
		Matrix4f scaleM = new Matrix4f();
		scaleM.scaling(new Vector3f(scale,heightScale,scale));
		Matrix4f projectionM = new Matrix4f();
		projectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
		Matrix4f tMVP = new Matrix4f();
		Matrix4f tWorldT = new Matrix4f();
		tWorldT.translation(new Vector3f((float)(x ),(float)(y ),(float)(z )));
		Matrix4f tWorld = new Matrix4f();
		tWorldT.mul(scaleM,tWorld);
		worldM.mul(scaleM,tMVP);
		Matrix4f vM = new Matrix4f();
		vM.translation(new Vector3f((float)(-camera.getEyeX() ),(float)(-camera.getEyeY()),(float)(-camera.getEyeZ() )));
		vM.mul(tMVP,tMVP);
		
		projectionM.mul(tMVP,tMVP);
	//	tMVP = projectionM;
	//	tMVP = translationM;
		glEnable(GL_DEPTH_TEST);
		Vector3f lightD = new Vector3f(0.2f,1.0f,1.0f);
		Vector3f lightC = new Vector3f(0.7f,0.7f,0.7f);
		
		   try (MemoryStack stack = MemoryStack.stackPush()) {
		        FloatBuffer tMVPM = stack.mallocFloat(16);
		        FloatBuffer tWM = stack.mallocFloat(16);
    	        
	        	        FloatBuffer lightDV = stack.mallocFloat(3);
		FloatBuffer lightCV = stack.mallocFloat(3);
		
	        	        tMVP.get(tMVPM);
	        	        worldM.get(tWM);
	        	        
		        lightD.get(lightDV);
		lightC.get(lightCV);
		
		        glUseProgram(waterProgramID);
		        glUniformMatrix4fv(waterUMVPLocation, false, tMVPM);
		        glUniformMatrix4fv(waterUWorldLocation, false, tWM);
		        
		        //glUniform1i(sampler1Location,materials.get(0).getTxtUnit());
		        
		        //glActiveTexture(GL_TEXTURE0);
		        //glBindTexture(GL_TEXTURE_2D,materials.get(0).getTxtId());
		        
		        //glUniform1i(directionalLightTypeLocation, 1);
		        //glUniform3fv(directionalLightDirectionLocation,  lightDV);
	            //glUniform3fv(directionalLightColorLocation, lightCV);
	           // glUniform1f(directionalLightAmountLocation,1.0f);
	            
	           // glUniform1f(screenBrightnessLocation,screenBrightness);
		   }
		   
		   
		   
		   glUniform1f(waterScreenBrightnessLocation,screenBrightness);
		   glActiveTexture(GL_TEXTURE0);
		   glBindTexture(GL_TEXTURE_2D,prevHeightID);
		   glUniform1i(waterInHeightLocation,0);
		   

		   glActiveTexture(GL_TEXTURE0 + 1);
		   glBindTexture(GL_TEXTURE_2D,curHeightID);
		   glUniform1i(waterInNormalXLocation,1);
		   

		   glActiveTexture(GL_TEXTURE0 + 2);
		   glBindTexture(GL_TEXTURE_2D,curVelocityID);
		   glUniform1i(waterInNormalZLocation,2);
		   
		   glUniform1f(waterWidthLocation,(float)( width - 1));
		   glUniform1f(waterHeightLocation,(float)( height - 1));
		   
		   //System.out.println("For Stage Item, Program Id and Vao Id are " + programId + " and " + vaoId);

	        glUseProgram(waterProgramID);
	        glBindVertexArray(waterVAOID);
	        

			    glBindVertexArray(waterVAOID);
			    
	        glEnableVertexAttribArray(0);
	        glEnableVertexAttribArray(1);
	        //glEnableVertexAttribArray(1);
	       // glEnableVertexAttribArray(2);
	        

	     //   System.out.println("V1 IS " + vCoords[0] + ", " + vCoords[1] + ", " + vCoords[2]);
	      //  System.out.println("V2 IS " + vCoords[3] + ", " + vCoords[4] + ", " + vCoords[5]);

	        //System.out.println("V3 IS " + vCoords[6] + ", " + vCoords[7] + ", " + vCoords[8]);
	        
	        

	        //System.out.println("V1 IS " + vCoords[712809] + ", " + vCoords[712810] + ", " + vCoords[712811]);
	        //System.out.println("V2 IS " + vCoords[712812] + ", " + vCoords[712813] + ", " + vCoords[712814]);

	      //  System.out.println("V3 IS " + vCoords[712815] + ", " + vCoords[712816] + ", " + vCoords[712817]);
	        
	       // System.out.println("Water has " + vCoords.length + " vertices");
	        
	        
	        
	        
	        //glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//System.out.println("Going there with vertex count of " + vertexCount);
//System.out.println("Vao is " + vaoId + " and Vbo is " + vboId + " and programId is " + programId);
	        //glDrawArrays(GL_TRIANGLES,0,(vCoords.length)/3);
	        
	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,theHeightFieldIndexBufferID);

	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,theHeightFieldIndexBufferID);
	        glBufferData(GL_ELEMENT_ARRAY_BUFFER,theHeightFieldIndexBuffer,GL_STATIC_DRAW);
	        
	        

			glActiveTexture(GL_TEXTURE0 + 3);
			glBindTexture(GL_TEXTURE_2D,0);
	       // glEnable(GL_CULL_FACE);
	       // glCullFace(GL_FRONT);
	        glDrawElements(GL_TRIANGLES,6*(width - 1)*(height - 1),GL_UNSIGNED_INT,0);
	       // glCullFace(GL_BACK);
	       // glDrawElements(GL_TRIANGLES,6*(width - 1)*(height - 1),GL_UNSIGNED_INT,0);
	        
	      //  glDisable(GL_CULL_FACE);
	        
	        
	        glDisableVertexAttribArray(0);
	        glDisableVertexAttribArray(1);
	        //glDisableVertexAttribArray(2);
	        
	        //glUseProgram(programId);
		
	}
	
	public void calculateNormals() {
		

		

		
		
		glUseProgram(normalProgramID);
		glBindFramebuffer(GL_FRAMEBUFFER,curHeightFieldFBO);
		glDrawBuffers(drawBuffersList);
		glUseProgram(normalProgramID);

		
		glViewport(0,0,width,height);

		//glBindTexture(GL_TEXTURE_2D,curHeightFieldID);
		glClearColor(0.0f,0.0f,0.0f,1.0f);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		float dx = (1.0f)/((float)width);
		float dy = (1.0f)/((float)height);
		glUniform1f(normalDXLocation,dx);
		glUniform1f(normalDYLocation,dy);
		
		glUniform1f(normalXScaleLocation,scale);
		glUniform1f(normalYScaleLocation,heightScaleSize*heightScale);
		glUniform1f(normalZScaleLocation,scale);
		

		//System.out.println("Center of " + center[0] + ", " + center[1]);
		//Vector2f centerLocation = new Vector2f(center[0],center[1]);
		
		
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D,prevHeightID);
		glUniform1i(normalInHeightLocation,0);
		
		glBindVertexArray(normalVAOID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
	       glDrawBuffers(drawBuffersList);

	       glActiveTexture(GL_TEXTURE0 + 3);
	       glBindTexture(GL_TEXTURE_2D,0);
	       
        glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
        

		glBindTexture(GL_TEXTURE_2D,0);
	       //glPixelStorei(GL_UNPACK_ALIGNMENT,1);

        
	    	    
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
        glBindVertexArray(0);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        

        
        
       // glUseProgram(copyProgramID);
        
        
		
		
		/*
		
        glBindFramebuffer(GL_FRAMEBUFFER,prevHeightFieldFBO);
        glUseProgram(copyProgramID);
        
        glBindTexture(GL_TEXTURE_2D,0);
        glDrawBuffers(drawBuffersList);
        glClearColor(0.9f,0.0f,0.0f,1.0f);
        glViewport(0,0,width,height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,curHeightID);
        glUniform1i(copyInHeightLocation,0);
        

        glActiveTexture(GL_TEXTURE0 + 1);
        glBindTexture(GL_TEXTURE_2D,curHeightID);
        glUniform1i(copyInVelocityLocation,1);
        
        glBindVertexArray(copyVAOID);
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        glDrawBuffers(drawBuffersList);
        
        glActiveTexture(GL_TEXTURE0 + 3);
        glBindTexture(GL_TEXTURE_2D,0);
        
        
        glDrawArrays(GL_TRIANGLES,0,(screenQuad.length)/3);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
        glBindVertexArray(0);
       glBindFramebuffer(GL_FRAMEBUFFER,0);
       */
        
        glViewport(0,0,1920,1080);
        
        
		
		
		
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return  this.y;
	}
	
	public double getZ() {
		return  this.z;
	}
	
	public int getPrevHeightFieldID() {
		return this.prevHeightID;
	}
	
	public int getCurHeightFieldID() {
		
		return this.prevHeightID;
	}
	public void addReflectionsCubeMap(CubeMap rCubeMap) {
	
		this.reflectionsCubeMap = rCubeMap;
	}
}
