import static org.lwjgl.glfw.GLFW.*;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Slider {

	private long window;
	private double pointerPosX, pointerPosY;
	private float leftValue, rightValue, baseWidth, baseHeight;
	private float[] screenQuad;
	private float[] screenTCoords;
	private int renderToScreenQuadVAOID;
	private int renderToScreenQuadVBOIDV;
	private int windowWidth, windowHeight;
	private float sliderWidth;
	private float sliderHeight;
	private float sliderMaxPositionXLeft, sliderMaxPositionXRight;
	private float sliderRange;
	
	private int sliderTextureLocation;
	private int sliderHasTextureLocation;
	private int sliderScaleLocation;
	private int sliderTranslationLocation;
	private int sliderColorLocation;
	private int sliderProgramID;
	private int sliderFShaderID;
	private int sliderVShaderID;
	
	private boolean beingDragged;
	
	private float basePosX, basePosY;
	private float sliderPosX, sliderPosY;
	
	private float sliderDX;
	//At
	GLFWMouseButtonCallbackI mouseCallback;
	GLFWCursorPosCallbackI mousePosCallback;
	
	public Slider(int windowWidth, int windowHeight, float basePosX, float basePosY, float leftValue, float rightValue, float baseWidth, float baseHeight, float sliderWidth, float sliderHeight, long window) throws Exception {
		
		this.window = window;                                                                                                                                             
		this.leftValue = leftValue;
		this.rightValue = rightValue;
		this.baseWidth = baseWidth;
		this.baseHeight = baseHeight;
		this.sliderWidth = sliderWidth;
		this.sliderHeight = sliderHeight;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.basePosX = basePosX;
		this.basePosY = basePosY;
		this.sliderPosX = basePosX;
		this.sliderPosY = basePosY;
		this.sliderMaxPositionXLeft  =  basePosX - baseWidth + 0.5f*sliderWidth;
		this.sliderMaxPositionXRight =  basePosX + baseWidth - 0.5f*sliderWidth;
		this.sliderRange = sliderMaxPositionXRight - sliderMaxPositionXLeft;
		this.beingDragged = false;
		this.sliderDX = 0.0f;
		
		
		glfwSetCursorPosCallback(this.window, mousePosCallback = GLFWCursorPosCallback.create((aWindow, xpos, ypos) -> {
			
			
			double prevPointerPosX = this.pointerPosX;
			double prevPointerPosY = this.pointerPosY;
			
			// Convert position to NDC
			
			this.pointerPosX = 2.0*(xpos / (double)windowWidth) - 1.0;
			this.pointerPosY = -1.0*(2.0*(ypos / (double)windowHeight) - 1.0);
			
			this.sliderDX = (float)(pointerPosX - prevPointerPosX);
			
			if (this.beingDragged) {
				if (this.sliderPosX + this.sliderDX >= sliderMaxPositionXRight) {
					//this.beingDragged = false;
					this.sliderDX = sliderMaxPositionXRight - this.sliderPosX;
				}
				else if (this.sliderPosX + this.sliderDX <= sliderMaxPositionXLeft) {
					//this.beingDragged = false;
					this.sliderDX = sliderMaxPositionXLeft - this.sliderPosX;
				}
				this.sliderPosX += this.sliderDX;
			}
			
			this.sliderDX = 0.0f;

		}));
		
		
		glfwSetMouseButtonCallback(this.window, mouseCallback = GLFWMouseButtonCallback.create((aWindow, button, action, mods) -> {

//			glfwSetMouseButtonCallback(window, mouseCallback = GLFWMouseButtonCallback.create((aWindow, button, action, mods) -> {

			if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
		
			if (this.isPointInsideRectangle((double)pointerPosX,(double)pointerPosY, sliderPosX - 0.5f*sliderWidth, sliderPosX + 0.5f*sliderWidth, sliderPosY + 0.5f*sliderHeight, sliderPosY - 0.5f*sliderHeight) ) {
					System.out.println("MOUSEX : " + pointerPosX + " and MOUSEY : "+ pointerPosY);
					System.out.println("L SIDE OF SLIDER BUTTON IS AT X = "+(sliderPosX - 0.5f*sliderWidth));
					System.out.println("R SIDE OF SLIDER BUTTON IS AT X = "+(sliderPosX + 0.5f*sliderWidth));
					
					//System.exit(0);
					this.beingDragged = true;
			}
				
				
			}
			
			else if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
				
				if (this.beingDragged) { 
					this.beingDragged = false;
				}
					
				}
		}));
	
		
		screenQuad = new float[]{-1.0f,1.0f,0.0f, -1.0f,-1.0f,0.0f, 1.0f,-1.0f,0.0f, -1.0f,1.0f,0.0f, 1.0f,-1.0f,0.0f, 1.0f,1.0f,0.0f};
        screenTCoords = new float[]{0.0f,1.0f, 0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f, 1.0f,0.0f, 1.0f,1.0f};
      
      
      //  screenQuad = new float[]{-1.0f,1.0f,0.0f, -1.0f,-1.0f,0.0f, 1.0f,-1.0f,0.0f, -1.0f,1.0f,0.0f, 1.0f,-1.0f,0.0f, 1.0f,1.0f,0.0f};
      //  screenTCoords = new float[]{0.0f,1.0f, 0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f, 1.0f,0.0f, 1.0f,1.0f};
      
        
		 FloatBuffer verticesBuffer = null;
		 
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(screenQuad.length);
	            System.out.println("Length of vertices is " + screenQuad.length);
	            
	            (verticesBuffer.put(screenQuad)).flip();
	            
	           renderToScreenQuadVAOID = glGenVertexArrays();
	           
	           System.out.println("Vao is " + renderToScreenQuadVAOID);
	           
	           
	            glBindVertexArray(renderToScreenQuadVAOID);

	            renderToScreenQuadVBOIDV = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, renderToScreenQuadVBOIDV);
	            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
	            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
	            
	            glBindVertexArray(0);
	            glBindBuffer(GL_ARRAY_BUFFER, 0);

	        } finally {
	            if (verticesBuffer  != null) {
	                MemoryUtil.memFree(verticesBuffer);
	            }
	        }
		
		String sliderVShader = "#version 430\n"+
		"  "+
		" layout (location = 0) in vec3 vPosition;\n" +
//		" layout (location = 1) in vec2 tCoord;\n" +

		" uniform mat4 translationM;"+
		" uniform mat4 scaleM;"+
		
		" void main() {"+
		
		"  gl_Position = translationM*scaleM*vec4(vPosition,1.0);"+
		
		" }";
		
		String sliderFShader = "#version 430\n"+
		"  out vec4 color;"+
				
		"  uniform int hasTexture;"+
		"  uniform sampler2D background;"+
		
		"  uniform vec3 boxColor;"+
		
		" void main() {"+
				
		"  color = vec4(boxColor,1.0);"+
				
		" }";
		
		




sliderProgramID = glCreateProgram();
//sliderProgramID = glCreateProgram();

glUseProgram(sliderProgramID);

if (sliderProgramID == 0) {
throw new Exception("Could not create Shader");
}


sliderVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (sliderVShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}

glShaderSource(sliderVShaderID, sliderVShader);
glCompileShader(sliderVShaderID );

if (glGetShaderi(sliderVShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(sliderVShaderID, 1024));
}

glAttachShader(sliderProgramID, sliderVShaderID);






sliderFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (sliderFShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(sliderFShaderID, sliderFShader);
glCompileShader(sliderFShaderID);

if (glGetShaderi(sliderFShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(sliderFShaderID, 1024));
}

glAttachShader(sliderProgramID, sliderFShaderID);








glLinkProgram(sliderProgramID);
if (glGetProgrami(sliderProgramID, GL_LINK_STATUS) == 0) {
throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(sliderProgramID, 1024));
}

if (sliderVShaderID != 0) {
glDetachShader(sliderProgramID, sliderVShaderID);
}
if (sliderFShaderID != 0) {
glDetachShader(sliderProgramID, sliderFShaderID);
}

glValidateProgram(sliderProgramID);
if (glGetProgrami(sliderProgramID, GL_VALIDATE_STATUS) == 0) {
System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(sliderProgramID, 1024));
}


glUseProgram(sliderProgramID);

sliderTranslationLocation = glGetUniformLocation(sliderProgramID,"translationM");
sliderScaleLocation = glGetUniformLocation(sliderProgramID,"scaleM");
sliderHasTextureLocation = glGetUniformLocation(sliderProgramID,"hasTexture");
sliderTextureLocation = glGetUniformLocation(sliderProgramID,"background");
sliderColorLocation = glGetUniformLocation(sliderProgramID,"boxColor");

		
		
		
		
		
		
	}
	
	
	public void update() {

		


	//	DoubleBuffer mousePosX = BufferUtils.createDoubleBuffer(1);
	//	DoubleBuffer mousePosY = BufferUtils.createDoubleBuffer(1);
		//glfwGetCursorPos(this.window,mousePosX,mousePosY);

	//	System.out.println("MOUSEX : " + mousePosX.get(0) + " and MOUSEY : "+ mousePosY.get(0));
		/*
		glfwGetMouseButton(this.window,GLFW_MOUSE_BUTTON_LEFT);
				
		if (glfwGetMouseButton(this.window, GLFW_MOUSE_BUTTON_LEFT) == 1 && this.isPointInsideRectangle((float)mousePosX.get(0),(float)mousePosY.get(0), sliderPosX - 0.5f*sliderWidth, sliderPosX + 0.5f*sliderWidth, sliderPosY + 0.5f*sliderHeight, sliderPosY - 0.5f*sliderHeight)) {
			
		
			
			//System.out.println("LEFT MOUSE BUTTON PRESSED.");
			this.beingDragged = true;
			System.exit(0);
			
		}
*/
	}
	
	public void draw() {
		
			glUseProgram(sliderProgramID);
			glEnable(GL_DEPTH_TEST);
			glDisable(GL_DEPTH_TEST);
			
		//	glViewport(0,0,this.windowWidth,this.windowHeight);
		
			// Draw the Slider base
			
			Matrix4f translationM = (new Matrix4f()).translation(basePosX,basePosY,0.0f);
			Matrix4f scaleM 	  = (new Matrix4f()).scaling(this.baseWidth,this.baseHeight,1.0f);
			Vector3f color 		  = (new Vector3f(1.0f,1.0f,1.0f));
			
			try (MemoryStack stack = MemoryStack.stackPush()) {
				
				FloatBuffer tTranslationM = stack.mallocFloat(16);
				FloatBuffer tScaleM = stack.mallocFloat(16);
				FloatBuffer tColorV = stack.mallocFloat(3);
				
				translationM.get(tTranslationM);
				scaleM.get(tScaleM);
				color.get(tColorV);
				
				glUniformMatrix4fv(sliderTranslationLocation, false, tTranslationM);
				glUniformMatrix4fv(sliderScaleLocation, false, tScaleM);
				glUniform3fv(sliderColorLocation, tColorV); 
			}
			
			glBindVertexArray(renderToScreenQuadVAOID);
			glEnableVertexAttribArray(0);
			
			glDrawArrays(GL_TRIANGLES,0,6);
			
			glDisableVertexAttribArray(0);
			
			// Draw the Slider button
			
			Matrix4f translationM2 = (new Matrix4f()).translation(sliderPosX,sliderPosY,0.0f);
			Matrix4f scaleM2 	  = (new Matrix4f()).scaling(this.sliderWidth,this.sliderHeight,1.0f);
			Vector3f color2 		  = (new Vector3f(0.0f,0.0f,0.50f));
			
			try (MemoryStack stack = MemoryStack.stackPush()) {
				
				FloatBuffer tTranslationM = stack.mallocFloat(16);
				FloatBuffer tScaleM = stack.mallocFloat(16);
				FloatBuffer tColorV = stack.mallocFloat(3);
				
				translationM2.get(tTranslationM);
				scaleM2.get(tScaleM);
				color2.get(tColorV);
				
				glUniformMatrix4fv(sliderTranslationLocation, false, tTranslationM);
				glUniformMatrix4fv(sliderScaleLocation, false, tScaleM);
				glUniform3fv(sliderColorLocation, tColorV); 
			}
			
			glBindVertexArray(renderToScreenQuadVAOID);
			glEnableVertexAttribArray(0);
			
			glDrawArrays(GL_TRIANGLES,0,6);
			
			glDisableVertexAttribArray(0);
			
			glUseProgram(0);
			
	}
	
	
	public float getValue() {
		float normalizedSliderPosX = (sliderPosX - sliderMaxPositionXLeft)/sliderRange;
		float value 		 =       (1.0f - normalizedSliderPosX)*leftValue + normalizedSliderPosX*rightValue;
		return value;
	}
	
	public boolean isPointInsideRectangle(double pointX, double pointY,   double rectLeft, double rectRight, double rectTop, double rectBottom) {
		
		if (pointX >= rectLeft && pointX <= rectRight  &&  pointY >= rectBottom && pointY <= rectTop) {
			return true;
		} else {
			return false;
		}
	}
	
}
