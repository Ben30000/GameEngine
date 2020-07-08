import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
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
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Menu {

	private double x,y;
	private float[] vCoords, tCoords;
	private Wrap wrap;
	private ArrayList<MenuBoxItem> menuBoxItems;
	private int programId, vShaderId, fShaderId, vaoId, vboIdV, vboIdT;
	private int sampler1Location, uMVPLocation, screenBrightnessLocation;
	
	
	
	public Menu(double x, double y, Wrap wrap,ArrayList<MenuBoxItem> menuBoxItems) throws Exception {
		
		
		
		
		
		this.x = x;
		this.y = y;
		//this.vCoords = vCoords;
		//this.tCoords = tCoords;
		this.vCoords = new float[]{-1.0f,1.0f,0.2f, -1.0f,-1.0f,0.2f, 1.0f,-1.0f,0.2f, -1.0f,1.0f,0.2f, 1.0f,-1.0f,0.2f, 1.0f,1.0f,0.2f};
	    this.tCoords = new float[]{0.0f,0.0f, 0.0f,1.0f, 1.0f,1.0f, 0.0f,0.0f, 1.0f,1.0f, 1.0f,0.0f};
		this.wrap = wrap;
		this.setMenuBoxItems(menuBoxItems);
		
		
		
		

GL.createCapabilities();
		String vShaderCode = "#version 400\n "+

"layout (location=0) in vec3 vCoord;\n"+

"layout (location=1) in vec2 tCoord;"+


"out vec2 fTxt;"+

"uniform mat4 uMVP;"+




"void main()\n"+
"{\n"+
" vec4 pos = uMVP*vec4(vCoord,1.0);"+


" fTxt = tCoord;"+
"   gl_Position = pos;\n" +
"}";
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		String fShaderCode = "#version 400\n "+



"in vec2 fTxt;"+

"out vec4 fragColor;\n"+


 
"uniform float screenBrightness;"+
"uniform sampler2D sampler1;" +


"void main()\n"+
"{\n"+
    

   " fragColor = vec4(screenBrightness*texture(sampler1,fTxt).xyz,1.0);"+
"}";
		
		
		
		
		
		
		
		
		
		
		
		
	        
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
	        screenBrightnessLocation = glGetUniformLocation(programId,"screenBrightness");
	        sampler1Location = glGetUniformLocation(programId,"sampler1");       
	       glUniform1i(sampler1Location,wrap.getTxtUnit());

	        
	        
		 FloatBuffer verticesBuffer = null;
	//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(vCoords.length);
	            System.out.println("Length of vertices is " + vCoords.length);
	//   verticesBuffer = 
	
	            (verticesBuffer.put(vCoords)).flip();
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
	        
		    
		    
		    
		    
		        
			    
			    
			    
			    
			    	

					 FloatBuffer tBuffer = null;
				//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
					    try {
				            tBuffer = MemoryUtil.memAllocFloat(tCoords.length);
				            System.out.println("Length of vertices is " + tCoords.length);
				//   verticesBuffer = 
				
				            (tBuffer.put(tCoords)).flip();
			//glGenVertexArrays();
				          glBindVertexArray(vaoId);

				            vboIdT = glGenBuffers();
				            glBindBuffer(GL_ARRAY_BUFFER, vboIdT);
				            glBufferData(GL_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);            
				            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
				            
				            glBindVertexArray(0);
				            glBindBuffer(GL_ARRAY_BUFFER, 0);

				            //glBindVertexArray(0);         
				        } finally {
				            if (tBuffer  != null) {
				         //   	System.exit(0);
				                MemoryUtil.memFree(tBuffer);
				            }
				        
				        
					    
					    
					    
					    
			    }
		    
		    
		    
		    
	}
	
	
	
	public double getX() {
		return x;
		
	}
	public void setX(double x) {
	    this.x = x;
	}
	
	public void render(float screenBrightness) {
	
		//System.out.println(programId + " and " + vaoId);
		
		glUseProgram(programId);
		Matrix4f tMVP = new Matrix4f();
		tMVP.ortho(-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f);
        
		   try (MemoryStack stack = MemoryStack.stackPush()) {
		        FloatBuffer tMVPM = stack.mallocFloat(16);
	        	        
	        	        tMVP.get(tMVPM);
	
		       // glUseProgram(programId);
		       glUniformMatrix4fv(uMVPLocation, false, tMVPM);
		       glUniform1f(screenBrightnessLocation,screenBrightness);
		   }
		 //  System.out.println("GL_TEXTURE  has value " + GL_TEXTURE1);
		        glActiveTexture(GL_TEXTURE0 + wrap.getTxtUnit());
		        glBindTexture(GL_TEXTURE_2D,wrap.getTxtId());
		        
		        

		     //   glUseProgram(programId);
		        glBindVertexArray(vaoId);
		        glEnableVertexAttribArray(0);
		        glEnableVertexAttribArray(1);
		        
		        
		        
	           //glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
	//System.out.println("Going there with vertex count of " + vertexCount);
	//System.out.println("Vao is " + vaoId + " and Vbo is " + vboId + " and programId is " + programId);
		        glDrawArrays(GL_TRIANGLES,0,6);
		        
		        
		        glDisableVertexAttribArray(0);
		        glDisableVertexAttribArray(1);
		       
		        
		       glUseProgram(0);
		       
			   
			   
		        
		        
		        
		        
		        
		
		
	}
	
	
	public float[] getvCoords() {
		return vCoords;
	}

	public void setvCoords(float[] vCoords) {
		this.vCoords = vCoords;
	}

	public float[] gettCoords() {
		return tCoords;
	}

	public void settCoords(float[] tCoords) {
		this.tCoords = tCoords;
	}

	public Wrap getWrap() {
		return wrap;
	}

	public void setWrap(Wrap wrap) {
		this.wrap = wrap;
	}

	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	public ArrayList<MenuBoxItem> getMenuBoxItems() {
		return menuBoxItems;
	}

	public void setMenuBoxItems(ArrayList<MenuBoxItem> menuBoxItems) {
		this.menuBoxItems = menuBoxItems;
	}
	
	
	
}
