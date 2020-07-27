import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL42.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.BufferUtils;


public class EngineMain {
		
		private Player player;
		private WorldUpdater zone;
		private Camera mainCamera;
		private Wrap testWrap1;
		private StageItemPOM  testStageItem1Nice;
	    
		private String sequence;
		private Menu activeMenu;
		private String currentMoveToType;
		private Menu mainMenu;
		private float screenBrightness;
		private CubeMap testCubeMap;
		private Water lake1;
		// The window handle
		private long window;
		private int shadowMap1, shadowMap2, shadowMap3, shadowMap4, shadowMap1FBO, shadowMap2FBO, shadowMap3FBO, shadowMap4FBO;
		
		private double landingPrecision = 0.00001;
		
		private int sceneDepthFBO;
		private int sceneDepthRBO;
		
		private float maxZ, minZ, maxX, minX, maxY, minY;
		
		private Vector3f sunDirection;
		private int sceneFBO;
		private int sceneShadows;
		private int sceneAmbientShadows;
		
		private int screenWidth = 1920; 
		private int screenHeight = 1080;
		private int shadowMap1Size,shadowMap2Size,shadowMap3Size;
		private float[] screenQuad;
		private float[] screenTCoords;
		private int renderToScreenQuadVAOID;
		private int renderToScreenQuadVBOIDV;
		private int renderToScreenQuadVBOIDT;
		private int renderToScreenQuadSceneSamplerLocation;
		private int renderToScreenQuadProgramID;
		private int renderToScreenQuadVShaderID;
		private int renderToScreenQuadFShaderID;
		private int blurGaussianProgramID;
		private int blurGaussianFShaderID;
		private int blurGaussianVShaderID;
		private int blurGaussianSceneSamplerLocation;
		private int blurredSceneShadows;
		private int blurGaussianHorizontalLocation;
		private int horizontallyBlurredSceneShadows;
		private int shadowMap1RBO;
		private int shadowMap2RBO;
		private int shadowMap3RBO;
		private int temporalFilteringFShaderID;
		private int temporalFilteringProgram;
		private int temporalFilteringVShaderID;
		private float maxZ2;
		private float minZ2;
		private int sceneWidth;
		private int sceneHeight;
	
		private int scene;

		private int sceneRBO;
		
		private int shouldTestSearchRadius = 1;
		
		private IntBuffer drawBuffersList;
		private IntBuffer drawBuffersDepthList;
		
		private int[] drawBuffersOrder;
		private int[] drawBuffersDepthOrder;
		
		private int sceneProgramID;
		private int sceneVShaderID;
		private int sceneFShaderID;
		private int sceneDirectionalLightTypeLocation;
		private int sceneDirectionalLightColorLocation;
		private int sceneDirectionalLightDirectionLocation;
		private int sceneScreenBrightnessLocation;
		private int sceneNormals;
		private int sceneNormalsLocation;
		private int sceneLocation;
		private int shadowMap4Size;
		private int shadowMap4RBO;
		private float prevMRange2;
		private boolean startRange = true;
		private int rotations;
		private float[] rotationValues;
		private int rotationsDimension = 128;
		private int brightPassFilterProgramID;
		private int brightPassFilterVShaderID;
		private int brightPassFilterFShaderID;
		private int sceneBright;
		// For Bloom Lighting
		private int downSampledSceneA;
		private int downSampledSceneAFBO;
		private int blurredDownSampledSceneA;
		private int blurredDownSampledSceneAFBO;
		private int downSampledSceneB;
		private int downSampledSceneBFBO;
		private int blurredDownSampledSceneB;
		private int blurredDownSampledSceneBFBO;
		private int downSampledSceneCFBO;
		private int blurredDownSampledSceneCFBO;
		private int downSampledSceneC;
		private int blurredDownSampledSceneC;
		private int horizontallyBlurredDownSampledSceneAFBO;
		private int horizontallyBlurredDownSampledSceneBFBO;
		private int horizontallyBlurredDownSampledSceneCFBO;
		private int horizontallyBlurredDownSampledSceneA;
		private int horizontallyBlurredDownSampledSceneB;
		private int horizontallyBlurredDownSampledSceneC;
		private int bloom1Location;
		private int bloom2Location;
		private int bloom3Location;
		private int scenePositions;
		private int scenePositions2;
				
		private int shadowMap5Size;
		private int shadowMap5;
		private int shadowMap5FBO;
		private int shadowMap5RBO;

		private int blurGaussianShadowMapsProgramID;
		private int blurGaussianShadowMapsFShaderID;
		private int blurGaussianShadowMapsVShaderID;
		private int blurGaussianShadowMapsSceneSamplerLocation;
		private int horizontallyBlurredScene;
		private int blurredScene;
		private int sceneShadowsLocation;
		private int blurGaussianSceneShadowsHorizontalLocation;
		private int blurGaussianSceneShadowsProgramID;
		private int blurGaussianSceneShadowsFShaderID;
		private int blurGaussianSceneShadowsVShaderID;
		private int blurGaussianSceneShadowsLocation;
		private int blurGaussianScenePositionsLocation;
		private int materialM = 1;
		private int prevFrame;
		private int renderToMemoryFBO;
		private boolean vSync = true;
		private int sceneRefractiveNormals;
		private int sceneRefractivePositions;
		private int sceneRefractive;
		private int refractiveObjects;
		private int sceneCompleteProgramID;
		private int sceneCompleteVShaderID;
		private int sceneCompleteFShaderID;
		private int scenePreRefractionLocation;
		private int refractiveObjectsLocation;
		private int waterNormalsLocation;
		private int scenePreRefraction;
		private int scenePreRefractionFBO;
		
		private int sceneShadowRadius;
		private int blurGaussianSceneShadowRadiusLocation;
		private int sceneWaterYPos;
		private int waterYPosWorldLocation;
		private int scenePosPreRefractionLocation;
		private int sceneCompleteDirectionalLightTypeLocation;
		private int sceneCompleteDirectionalLightDirectionLocation;
		private int sceneCompleteDirectionalLightColorLocation;
		private int sceneCompleteProjMLocation;
		private int sceneCompleteWVMLocation;
		private int reflectionsCubeMapLocation;
		private Pather testCreature1;
		private int testSceneProgramID;
		private int testSceneFShaderID;
		private int testSceneVShaderID;
		private int testSceneSceneLocation;
		private int sceneShadowsFBO;
		private int shadowsProgramID;
		private int shadowsFShaderID;
		private int shadowsVShaderID;
		private int shadowsSceneNormalsSamplerLocation;
		private int shadowsScenePositionsSamplerLocation;
		private int blurGaussianInvProjMLocation;
		private int blurGaussianInvCamMLocation;
		private int shadowInvProjMatrixLocation;
		private int sceneAmbientShadowsNoise;
		private int shadowNormalTransformMatrixLocation;
		private int shadowsNoiseSamplerLocation;
		private int shadowSphereSamplesLocation;
		private Vector3f[] testSphere;
		private int sSAOSamples = 16;
		private int shadowProjMatrixLocation;
		private int blurGaussianSceneAmbientShadowsLocation;
		private int sceneAmbientShadowsLocation;
		private int blurGaussianShadowsVerticalFBO;
		private int blurGaussianShadowsHorizontalFBO;
		private int horizontallyBlurredSceneAmbientShadows;
		private int blurredSceneAmbientShadows;
		private int renderToScreenQuadVBOIDFarPlaneCorners;
		private int lightingProgramID;
		private int lightingShaderID;
		private int pointLightsColor;
		private int pointLightsColorTUnit = 0;
		private int lightsSSBO;
		private int lightsSSBOIndex;
		private int lightingProgramLightsSizeLocation;
		private int lightingProgramProjectionMLocation;
		private int lightingProgramDepthLocation;
		private int lightingProgramNormalsLocation;
		private int lightingProgramCameraMLocation;
		private int lightingProgramFrustumCornerPositionsLocation;
		private double farPlaneHeight;
		private double farPlaneWidth;
		private double farPlaneZ;
		private double nearPlaneZ;
		
		private int lightingProgramScreenWidthLocation;
		private int lightingProgramScreenHeightLocation;
		
		
		// Global Transformation Matrices
		
		private Matrix4f globalMainCameraMatrix;
		private Matrix4f globalProjectionMatrix;
		private Matrix4f globalInvProjectionMatrix;
		private int pointLight1CubeMapShadowsSize;
		private int pointLight1CubeMapShadowsFBO;
		private int pointLight1CubeMapShadows;
		private int pointLight1CubeMapShadowsRBO;
		private int prevScenePreRefractionLocation;
		private int prevScenePreRefraction;
		
		private int tessellationEnabled;
		
		private Slider testSlider1;
		private boolean shouldUseBlurredSceneShadows = true;
		
		
		
		public void run() throws Exception {
			
			System.out.println("Using LWJGL " + Version.getVersion() + "!");
			
			init();
			loop();

			// Free the window callbacks and destroy the window
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);

			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}
		
		

		private void init() throws Exception {
			// Setup an error callback. The default implementation
			// will print the error message in System.err.
			GLFWErrorCallback.createPrint(System.err).set();

			// Initialize GLFW. Most GLFW functions will not work before doing this.
			if ( !glfwInit() )
				throw new IllegalStateException("Unable to initialize GLFW");

			// Configure GLFW
			glfwDefaultWindowHints(); // optional, the current window hints are already the default
			
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
             glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4); 
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0); 
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); 

            glfwWindowHint(GLFW_SAMPLES,1);
			// Create the window
			window = glfwCreateWindow(1920, 1080, "Space Cat", glfwGetPrimaryMonitor(), NULL);
			if ( window == NULL )
				throw new RuntimeException("Failed to create the GLFW window");
			
			
			screenQuad = new float[]{-1.0f,1.0f,0.0f, -1.0f,-1.0f,0.0f, 1.0f,-1.0f,0.0f, -1.0f,1.0f,0.0f, 1.0f,-1.0f,0.0f, 1.0f,1.0f,0.0f};
			screenTCoords = new float[]{0.0f,1.0f, 0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f, 1.0f,0.0f, 1.0f,1.0f};
      
        
		
        		/* ***************************************************************
        		 * Keyboard Input Handling *    
        		 ************************************************************** */
        
				// Setup a key callback. It will be called every time a key is pressed, repeated or released.
        		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
				
				if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ){
					glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
				}
				
				if ( key == GLFW_KEY_LEFT && action == GLFW_PRESS ) {
					
				  if (sequence == "Level") {
					zone.setDX(1);
				    
					zone.setGlide(false);
					zone.setLeftKeyR(false);
					zone.setRightKeyR(false);
                    //player.setGoingLeft(true);
                    //player.setGoingRight(false);
                    
				  }
					
				  else if (sequence == "Main Menu") {
					  
				  }
				}
				if ( key == GLFW_KEY_RIGHT && action == GLFW_PRESS ) {
				  
				  if (sequence == "Level") {
					zone.setDX(-1);
					
					zone.setGlide(false);
				    zone.setLeftKeyR(false);
				    zone.setRightKeyR(false);
				   
				   // player.setGoingRight(true);
				    //player.setGoingLeft(false);
				    
				  }
				  
				  else if (sequence == "Main Menu") {
					  
				  }
				}
				
				
				if ( key == GLFW_KEY_LEFT && action == GLFW_RELEASE ) {
				
			     //if (p.getX() > cameraRegion.getX1() || Math.abs())
				
				  if (sequence == "Level") {
				
					  if (zone.getDX() == -1 || zone.getDX() == 0) {
						  
						  
					  }
					  
					  else {
					  zone.setDX(0);
					zone.setGlide(true);
					zone.setGlideCounter(10);
					zone.setLeftKeyR(true);
					zone.setRightKeyR(false);
					  }
				  }
				  else if (sequence == "Main Menu") {
					  
				  }
				}
				if ( key == GLFW_KEY_RIGHT && action == GLFW_RELEASE ) {
					
				  if (sequence == "Level") {
					  
					  if (zone.getDX() == 1 || zone.getDX() == 0) {
						  
						  
					  }
					  
					  else {
					zone.setDX(0);
					zone.setGlide(true);
					zone.setGlideCounter(10);
					zone.setRightKeyR(true);
					zone.setLeftKeyR(false);
					  }
				  }
				  else if (sequence == "Main Menu") {
					  
				  }
				}
				 if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS ) {
					
				  if (sequence == "Level") {
					if (!player.getJumping() && !player.getFalling()) {
						player.setJumping(true);
					}
					else if (player.getFalling()) {
						//System.out.println("Trying to jump while in falling state");
						//System.exit(0);
					}
					
				  }
				  else if (sequence == "Main Menu") {
					  
				  }
				}
				 if ( key == GLFW_KEY_UP && action == GLFW_PRESS) {
					
					if (sequence == "Level") {
						
					}
					
					else if (sequence == "Main Menu") {
						 
						int currentHoveredButtonIndex = 0;
						
						for (int k = 0; k < activeMenu.getMenuBoxItems().size(); k++){
							if (activeMenu.getMenuBoxItems().get(k).isHoveredOver()) {
								currentHoveredButtonIndex = k;
							}
						}
						  
						if (currentHoveredButtonIndex >= 1) {
							for (int k = 0; k < activeMenu.getMenuBoxItems().size(); k++){
								
								activeMenu.getMenuBoxItems().get(k).setHoveredOver(false);
								
							}
							activeMenu.getMenuBoxItems().get(currentHoveredButtonIndex - 1).setHoveredOver(true);

						}
						
						
					}
				}
				
				 	if ( key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
					
					if (sequence == "Level") {
						
					}
					
					else if (sequence == "Main Menu") {
						

						int currentHoveredButtonIndex = 0;
						
						for (int k = 0; k < activeMenu.getMenuBoxItems().size(); k++){
							if (activeMenu.getMenuBoxItems().get(k).isHoveredOver()) {
								currentHoveredButtonIndex = k;
							}
						}
						
						if (currentHoveredButtonIndex < activeMenu.getMenuBoxItems().size() - 1) {
							for (int k = 0; k < activeMenu.getMenuBoxItems().size(); k++){
								
								activeMenu.getMenuBoxItems().get(k).setHoveredOver(false);
								
							}
							activeMenu.getMenuBoxItems().get(currentHoveredButtonIndex + 1).setHoveredOver(true);

						}
						
						
					}
				}
				
				 	if ( key == GLFW_KEY_ENTER && action == GLFW_PRESS) {
					
					if (sequence == "Level") {
						
					}
					
					else if (sequence == "Main Menu") {
						
						for (int k = 0; k < activeMenu.getMenuBoxItems().size(); k++){
							if (activeMenu.getMenuBoxItems().get(k).isHoveredOver()) {
								sequence = "Move To";
								System.out.println("Made it " + activeMenu.getMenuBoxItems().size() );
	
								currentMoveToType = activeMenu.getMenuBoxItems().get(k).getMoveTo().getType();
								
								System.out.println(currentMoveToType);
							}
						}
					}
				}
				
				 	

					 if ( key == GLFW_KEY_W && action == GLFW_PRESS ) {
						
					  if (sequence == "Level") {
						  
						  if (materialM != 1) {
						  materialM = 1;
						  }
						  else {
							  materialM = 0;
						  }
						
					  }
					  else if (sequence == "Main Menu") {
						  
					  }
					}
					

					 

					 if ( key == GLFW_KEY_V && action == GLFW_PRESS ) {
						
					  if (sequence == "Level") {
						  
						  if (vSync == true) {
						  vSync = false;
						  }
						  else {
							  vSync = true;
						  }
						
					  }
					  else if (sequence == "Main Menu") {
						  
					  }
					}
					

					 
					 if ( key == GLFW_KEY_P && action == GLFW_PRESS ) {
							
						  if (sequence == "Level") {
							  
							  if (shouldUseBlurredSceneShadows == true) {
								  shouldUseBlurredSceneShadows = false;
							  }
							  else {
								  shouldUseBlurredSceneShadows = true;
							  }
							
						  }
						  else if (sequence == "Main Menu") {
							  
						  }
						}
						
					 

					 if ( key == GLFW_KEY_S && action == GLFW_PRESS ) {
						
					  if (sequence == "Level") {
						  
						  if (shouldTestSearchRadius == 1) {
						  shouldTestSearchRadius = 0;
						  }
						  else {
							  shouldTestSearchRadius = 1;
						  }
						
					  }
					  else if (sequence == "Main Menu") {
						  
					  }
					}
					 

					 if ( key == GLFW_KEY_R && action == GLFW_PRESS ) {
						
					  if (sequence == "Level") {
						  player.setX(10.0);
						  player.setY(-15.0);
						  
					  }
					  else if (sequence == "Main Menu") {
						  
					  }
					}


					 if ( key == GLFW_KEY_J ) {
							
						  if (sequence == "Level") {
							  
							  mainCamera.setEyeDX(0.0);
							  mainCamera.setEyeDY(0.1);
							  mainCamera.setEyeDZ(0.0);
							  mainCamera.moveEye();
							
						  }
						  else if (sequence == "Main Menu") {
							  
						  }
						}
						
					 
					 if ( key == GLFW_KEY_B ) {
							
						  if (sequence == "Level") {
							  mainCamera.setEyeDX(-0.12);
							  mainCamera.setEyeDY(0.0);
							  mainCamera.setEyeDZ(0.0);
							  mainCamera.moveEye();
							
							
						  }
						  else if (sequence == "Main Menu") {
							  
						  }
						}
						
					 
					 if ( key == GLFW_KEY_N ) {
							
						  if (sequence == "Level") {
							  mainCamera.setEyeDX(0.0);
							  mainCamera.setEyeDY(-0.1);
							  mainCamera.setEyeDZ(0.0);
							  mainCamera.moveEye();
							  
							
						  }
						  else if (sequence == "Main Menu") {
							  
						  }
						}
						
					 
					 if ( key == GLFW_KEY_M ) {
							
						  if (sequence == "Level") {
							  mainCamera.setEyeDX(0.12);
							  mainCamera.setEyeDY(0.0);
							  mainCamera.setEyeDZ(0.0);
							  mainCamera.moveEye();
							
							  
							
						  }
						  else if (sequence == "Main Menu") {
							  
						  }
						}
						
				
			});

			
			
			
			
			
			
			

			
			
			
			
			screenBrightness = 1.0f;
			
			
			tessellationEnabled = 1;
			//tessellationEnabled = 0;
			
			// Get the thread stack and push a new frame
			try ( MemoryStack stack = stackPush() ) {
				IntBuffer pWidth = stack.mallocInt(1); // int*
				IntBuffer pHeight = stack.mallocInt(1); // int*

				// Get the window size passed to glfwCreateWindow
				glfwGetWindowSize(window, pWidth, pHeight);

				// Get the resolution of the primary monitor
				GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
System.out.println("Resolution is " + vidmode.width() + " by " + vidmode.height());
				// Center the window
				glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
				);
			} // the stack frame is popped automatically

			// Make the OpenGL context current
			glfwMakeContextCurrent(window);
			// V sync
			if (vSync) {
			glfwSwapInterval(1);
			}
			else {
				glfwSwapInterval(0);
			}
			// Make the window visible
			glfwShowWindow(window);
			
			GL.createCapabilities();
		/*
			 player = new Player();
			 
			 testWrap1 = new Wrap("C:\\TestWrap1.png",0);
			 
			 ArrayList<Wrap> testMaterials1 = new ArrayList<Wrap>();
			 testMaterials1.add(testWrap1);
			 float[] vCoords1 = new float[]{-2.0f,-1.0f,-10.0f, 0.1f,2.0f,-22.0f, 2.0f,-1.0f,-10.0f};
			 float[] nVecs1 = new float[]{-1.1f,0.2f,1.0f, 1.4f,1.2f,1.0f, 0.1f,1.0f,0.01f};
			 float[] tCoords1 = new float[]{0.0f,1.0f, 0.4f,0.0f, 1.0f,1.0f};
			 ArrayList<float[]> testTCoordsMaterials1 = new ArrayList<float[]>();
			 testTCoordsMaterials1.add(tCoords1);
			 
			 
				Background testBG1 = new Background();
             
			 testStageItem1 = new StageItem(0.2,0.1,0.1,vCoords1,nVecs1,testMaterials1,testTCoordsMaterials1,testBG1);
		
			 ArrayList<StageItem> stageItemsBG1 = new ArrayList<StageItem>();
			 stageItemsBG1.add(testStageItem1);
			 
			 testBG1.setStageItems(stageItemsBG1);   
				Interval interval1 = new Interval(-20.0,100.0,-20.0,20.0,-6.01,26.01,true,1,0,testBG1,player,4,2);
			ArrayList<Interval> intervalsBG1 = new ArrayList<Interval>();
			intervalsBG1.add(interval1);
			testBG1.addInterval(interval1);
				
				ArrayList<DependentScrollingBackground> followingZones = new ArrayList<DependentScrollingBackground>();
				
				
				zone = new ScrollingBackground(2,player,followingZones);
			
				ArrayList<Background> zoneBGs = new ArrayList<Background>();
				zoneBGs.add(testBG1);
				zone.setBackgrounds(zoneBGs);
		*/
			

				Wrap mainMenuButton1Wrap = new Wrap("C:\\MMenuButton1.png",0,1,1);
				Wrap mainMenuButton2Wrap = new Wrap("C:\\MMenuButton2.png",1,1,1);
				MoveTo mainMenuButton1MoveTo = new MoveTo("BOutAndLoadLevel",null,null);
				MoveTo mainMenuButton2MoveTo = new MoveTo("BOutAndLoadLevel",null,null);
				MenuBoxItem mainMenuButton1 = new MenuBoxItem(0.01,0.01,mainMenuButton1Wrap,mainMenuButton1MoveTo,2.0f,0.2f,true,false);
				MenuBoxItem mainMenuButton2 = new MenuBoxItem(0.01,-0.28,mainMenuButton2Wrap,mainMenuButton2MoveTo, 2.0f,0.2f,false,false);
				
			    ArrayList<MenuBoxItem> mainMenuButtons = new ArrayList<MenuBoxItem>();
			    
			    mainMenuButtons.add(mainMenuButton1);
			    mainMenuButtons.add(mainMenuButton2);
				Wrap mainMenuWrap = new Wrap("C:\\SpaceCatsMainMenu.png",2,1,1);




			
				String vShaderRenderToScreenQuad = "#version 430\n" +
		" layout (location = 0) in vec3 vPosition;\n" +
		" layout (location = 1) in vec2 tCoord;\n" +
		
		" out vec2 sampleCoord;\n" +
		
		" void main() {\n" +
		"  sampleCoord = tCoord;\n" +
		" gl_Position = vec4(vPosition,1.0);\n" +
		" } ";
		
		String fShaderRenderToScreenQuad = "#version 430\n" +
		" in vec2 sampleCoord;\n" +
		" out vec4 fragColor;"+
		" uniform sampler2D scene;\n"+
			
		
		" void main() {\n" +
		" vec3 color = texture(scene,sampleCoord).rgb;"+
		" fragColor = vec4(color,1.0);"+
		"   } ";
		
		
			
		
		
		
        
        renderToScreenQuadProgramID = glCreateProgram();
        
        glUseProgram(renderToScreenQuadProgramID);
        
        if (renderToScreenQuadProgramID == 0) {
            throw new Exception("Could not create Shader");
        }
        
        
        renderToScreenQuadVShaderID = glCreateShader(GL_VERTEX_SHADER);
        if (renderToScreenQuadVShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
        }

        glShaderSource(renderToScreenQuadVShaderID, vShaderRenderToScreenQuad);
        glCompileShader(renderToScreenQuadVShaderID );

        if (glGetShaderi(renderToScreenQuadVShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(renderToScreenQuadVShaderID, 1024));
        }

        glAttachShader(renderToScreenQuadProgramID, renderToScreenQuadVShaderID);

        
        
        
        

        renderToScreenQuadFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        if (renderToScreenQuadFShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
        }

        glShaderSource(renderToScreenQuadFShaderID, fShaderRenderToScreenQuad);
        glCompileShader(renderToScreenQuadFShaderID);

        if (glGetShaderi(renderToScreenQuadFShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(renderToScreenQuadFShaderID, 1024));
        }

        glAttachShader(renderToScreenQuadProgramID, renderToScreenQuadFShaderID);
        
        
        
        
        
        
        
        
        glLinkProgram(renderToScreenQuadProgramID);
        if (glGetProgrami(renderToScreenQuadProgramID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(renderToScreenQuadProgramID, 1024));
        }

        if (renderToScreenQuadVShaderID != 0) {
            glDetachShader(renderToScreenQuadProgramID, renderToScreenQuadVShaderID);
        }
        if (renderToScreenQuadFShaderID != 0) {
            glDetachShader(renderToScreenQuadProgramID, renderToScreenQuadFShaderID);
        }

        glValidateProgram(renderToScreenQuadProgramID);
        if (glGetProgrami(renderToScreenQuadProgramID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(renderToScreenQuadProgramID, 1024));
        }
    
	
        glUseProgram(renderToScreenQuadProgramID);
        
        
        
        renderToScreenQuadSceneSamplerLocation = glGetUniformLocation(renderToScreenQuadProgramID,"scene");
        
        glUniform1i(renderToScreenQuadSceneSamplerLocation,0);
        
        
       
       // glUniform1i(heightFieldShaderSamplerLocation,11);
        
        

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
		    verticesBuffer = null;
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(screenTCoords.length);
	            System.out.println("Length of vertices is " + screenQuad.length);
	            (verticesBuffer.put(screenTCoords)).flip();
	            glBindVertexArray(renderToScreenQuadVAOID);
	            renderToScreenQuadVBOIDT = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, renderToScreenQuadVBOIDT);
	            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
	            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	            glBindVertexArray(0);
	            glBindBuffer(GL_ARRAY_BUFFER, 0);
	        } finally {
	            if (verticesBuffer  != null) {
	                MemoryUtil.memFree(verticesBuffer);
	            }
	        }
	        
        

		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    // Far Plane Frustum Positions
		    // VBO For Frustum Far Plane Corner Positions, in View Space
		    
		    

			Matrix4f testProjectionM = new Matrix4f();
	 		testProjectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
	 		//testProjectionM.invert(testProjectionM);
	 		
	 		Vector3f TLeft = new Vector3f();
	 		
	 		testProjectionM.frustumCorner(Matrix4fc.CORNER_NXNYPZ, TLeft);
	 		
	 		Matrix4f invPM = new Matrix4f();
	 		testProjectionM.invert(invPM);
	 		
	 		Vector4f TLV = new Vector4f(-1.0f,1.0f,1.0f,1.0f);
	 		
	 		invPM.transform(TLV,TLV);
	 		
	 		double fOV = (double)Math.PI/3.0;
	 		double aR = 1920.0/1080.0;
	 		
	 		farPlaneHeight = 2.0*Math.tan(fOV/2.0)*1000.0;
	 		farPlaneWidth = aR*farPlaneHeight;
	 		nearPlaneZ = 0.001;
	 		farPlaneZ = 1000.0;
	 		System.out.println("Far plane position x via method is " + TLeft);
	 		System.out.println("Far plane position x via transforming NDC point is " + TLV.x/TLV.w);
	 		
	 		System.out.println("Far plane height and width are " + farPlaneHeight + ", " + farPlaneWidth);
	 		System.out.println("Far plane position x via trigonometry is " + -0.5*farPlaneWidth);

	 		float[] farPlaneCorners = new float[] {-0.5f*(float)farPlaneWidth, 0.5f*(float)farPlaneHeight, -(float)farPlaneZ,
	 				-0.5f*(float)farPlaneWidth, -0.5f*(float)farPlaneHeight, -(float)farPlaneZ,        0.5f*(float)farPlaneWidth, -0.5f*(float)farPlaneHeight, -(float)farPlaneZ,
	 				-0.5f*(float)farPlaneWidth, 0.5f*(float)farPlaneHeight, -(float)farPlaneZ,           0.5f*(float)farPlaneWidth, -0.5f*(float)farPlaneHeight, -(float)farPlaneZ,
	 				0.5f*(float)farPlaneWidth, 0.5f*(float)farPlaneHeight, -(float)farPlaneZ
	 				};
	 		
		    
	 		for (int y = 0; y < farPlaneCorners.length; y++) {
	 		
	 			if (y % 3 == 0) {
	 				System.out.println("\n");
	 			}
	 			System.out.println(farPlaneCorners[y]);
	 		}

	 		//System.exit(0);

			 FloatBuffer farPlaneCornersBuffer = null;
			    try {
			    	farPlaneCornersBuffer = MemoryUtil.memAllocFloat(farPlaneCorners.length);
		            System.out.println("Length of vertices is " + farPlaneCorners.length);
		            (farPlaneCornersBuffer.put(farPlaneCorners)).flip();
		            glBindVertexArray(renderToScreenQuadVAOID);

		            renderToScreenQuadVBOIDFarPlaneCorners = glGenBuffers();
		            glBindBuffer(GL_ARRAY_BUFFER, renderToScreenQuadVBOIDFarPlaneCorners );
		            glBufferData(GL_ARRAY_BUFFER, farPlaneCornersBuffer, GL_STATIC_DRAW);            
		            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		            glBindVertexArray(0);
		            glBindBuffer(GL_ARRAY_BUFFER, 0);

		        } finally {
		            if (farPlaneCornersBuffer != null) {
		                MemoryUtil.memFree(farPlaneCornersBuffer);
		            }
		        }
			    
	        
	

		    
		    
		    
		    // Gaussian Blur Shader Program
		    
		    
		    


			
			String vShaderGaussianBlur = "#version 430\n" +
	" layout (location = 0) in vec3 vPosition;\n" +
	" layout (location = 1) in vec2 tCoord;\n" +
	
	" out vec2 sampleCoord;\n" +
	
	" void main() {\n" +
	"  sampleCoord = tCoord;\n" +
	" gl_Position = vec4(vPosition,1.0);\n" +
	" } ";
	
	String fShaderGaussianBlur = "#version 430\n" +
	" in vec2 sampleCoord;\n" +
	" out vec4 fragColor;\n"+
	" uniform sampler2D scene;\n"+
	
	"uniform int horizontal;"+ 
    //"uniform float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);"+
	
  //  "uniform float weight[5] = float[] (0.427027, 0.1645946, 0.106216, 0.044054, 0.016216);"+
//"uniform float weight[3] = float[] (0.55,0.2,0.075);"+
//"uniform float weight[4] = float[] (0.48,0.19,0.1,0.041);"+
//"uniform float weight[2] = float[] (0.35,0.325);"+
//" uniform float weight[3] = float[] (0.2, 0.3,0.1);"+
" uniform float weight[3] = float[] (0.38774, 0.24477,0.06136);"+
	
	" void main() {\n" +
	
  "vec2 tex_oset = 1.0 / textureSize(scene, 0);\n"+ 
   "vec3 result = ( texture(scene, sampleCoord).rgb * weight[0] );\n"+ 
  

" if (horizontal == 1) {\n"+
	"for(int i = 1; i < 3; ++i)\n"+
    "{\n"+
	
            "result += ( texture(scene, sampleCoord + vec2(tex_oset.x * i, 0.0)).rgb * weight[i] );"+
    "result += ( texture(scene, sampleCoord - vec2(tex_oset.x * i, 0.0)).rgb * weight[i] );"+
    "}\n"+
" }\n"+
"else"+
"{"+
   " for(int i = 1; i < 3; ++i)"+
    "{"+
        "result += ( texture(scene, sampleCoord + vec2(0.0, tex_oset.y * i)).rgb * weight[i] );"+
        "result += ( texture(scene, sampleCoord - vec2(0.0, tex_oset.y * i)).rgb * weight[i] );"+
    "}"+
"}"+

   " fragColor =  vec4(result,1.0);"+
   "   } ";
	
	
		
	
	
	
    
    blurGaussianProgramID = glCreateProgram();
    
    glUseProgram(blurGaussianProgramID);
    
    if (blurGaussianProgramID == 0) {
        throw new Exception("Could not create Shader");
    }
    
    
    blurGaussianVShaderID = glCreateShader(GL_VERTEX_SHADER);
    if (blurGaussianVShaderID == 0) {
        throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
    }

    glShaderSource(blurGaussianVShaderID, vShaderGaussianBlur);
    glCompileShader(blurGaussianVShaderID );

    if (glGetShaderi(blurGaussianVShaderID, GL_COMPILE_STATUS) == 0) {
        throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(blurGaussianVShaderID, 1024));
    }

    glAttachShader(blurGaussianProgramID, blurGaussianVShaderID);

    
    
    
    

    blurGaussianFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
    if (blurGaussianFShaderID == 0) {
        throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
    }

    glShaderSource(blurGaussianFShaderID, fShaderGaussianBlur);
    glCompileShader(blurGaussianFShaderID);

    if (glGetShaderi(blurGaussianFShaderID, GL_COMPILE_STATUS) == 0) {
        throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(blurGaussianFShaderID, 1024));
    }

    glAttachShader(blurGaussianProgramID, blurGaussianFShaderID);
    
    
    
    
    
    
    
    
    glLinkProgram(blurGaussianProgramID);
    if (glGetProgrami(blurGaussianProgramID, GL_LINK_STATUS) == 0) {
        throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(blurGaussianProgramID, 1024));
    }

    if (blurGaussianVShaderID != 0) {
        glDetachShader(blurGaussianProgramID, blurGaussianVShaderID);
    }
    if (blurGaussianFShaderID != 0) {
        glDetachShader(blurGaussianProgramID, blurGaussianFShaderID);
    }

    glValidateProgram(blurGaussianProgramID);
    if (glGetProgrami(blurGaussianProgramID, GL_VALIDATE_STATUS) == 0) {
        System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(blurGaussianProgramID, 1024));
    }


    glUseProgram(blurGaussianProgramID);
    
    
    
    blurGaussianSceneSamplerLocation = glGetUniformLocation(blurGaussianProgramID,"scene");
    
    glUniform1i(blurGaussianSceneSamplerLocation,0);
    
    blurGaussianHorizontalLocation = glGetUniformLocation(blurGaussianProgramID,"horizontal");
    
    

			

    
    

    
    // Gaussian Blur for Shadow Maps
    
    
    


	
	String vShaderGaussianBlurShadowMaps = "#version 430\n" +
" layout (location = 0) in vec3 vPosition;\n" +
" layout (location = 1) in vec2 tCoord;\n" +

" out vec2 sampleCoord;\n" +

" void main() {\n" +
"  sampleCoord = tCoord;\n" +
" gl_Position = vec4(vPosition,1.0);\n" +
" } ";

String fShaderGaussianBlurShadowMaps = "#version 430\n" +
" in vec2 sampleCoord;\n" +
" out vec4 fragColor;\n"+
" uniform sampler2D scene;\n"+

"uniform int horizontal;"+ 
//"uniform float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);"+
" uniform float weight[6] = float[] (0.17,	0.15,	0.12,	0.07,	0.05,	0.025);"+
//  "uniform float weight[5] = float[] (0.427027, 0.1645946, 0.106216, 0.044054, 0.016216);"+
//"uniform float weight[3] = float[] (0.55,0.2,0.075);"+
//"uniform float weight[4] = float[] (0.48,0.19,0.1,0.041);"+
//"uniform float weight[2] = float[] (0.35,0.325);"+
//" uniform float weight[3] = float[] (0.2, 0.3,0.1);"+
//" uniform float weight[3] = float[] (0.38774, 0.24477,0.06136);"+

" void main() {\n" +

"vec2 tex_oset = 1.0 / textureSize(scene, 0);\n"+ 
"float result = ( texture(scene, sampleCoord).r * weight[0] );\n"+ 


" if (horizontal == 1) {\n"+
"for(int i = 1; i < 6; ++i)\n"+
"{\n"+

    "result += ( texture(scene, sampleCoord + vec2(tex_oset.x * i, 0.0)).r * weight[i] );"+
"result += ( texture(scene, sampleCoord - vec2(tex_oset.x * i, 0.0)).r * weight[i] );"+
"}\n"+
" }\n"+
"else"+
"{"+
" for(int i = 1; i < 6; ++i)"+
"{"+
"result += ( texture(scene, sampleCoord + vec2(0.0, tex_oset.y * i)).r * weight[i] );"+
"result += ( texture(scene, sampleCoord - vec2(0.0, tex_oset.y * i)).r * weight[i] );"+
"}"+
"}"+

" fragColor =  vec4(result, 0.0, 0.0 , 1.0);"+
"   } ";







blurGaussianShadowMapsProgramID = glCreateProgram();

glUseProgram(blurGaussianShadowMapsProgramID);

if (blurGaussianShadowMapsProgramID == 0) {
throw new Exception("Could not create Shader");
}


blurGaussianShadowMapsVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (blurGaussianShadowMapsVShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}

glShaderSource(blurGaussianShadowMapsVShaderID, vShaderGaussianBlurShadowMaps);
glCompileShader(blurGaussianShadowMapsVShaderID );

if (glGetShaderi(blurGaussianShadowMapsVShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(blurGaussianShadowMapsVShaderID, 1024));
}

glAttachShader(blurGaussianShadowMapsProgramID, blurGaussianShadowMapsVShaderID);






blurGaussianShadowMapsFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (blurGaussianShadowMapsFShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(blurGaussianShadowMapsFShaderID, fShaderGaussianBlurShadowMaps);
glCompileShader(blurGaussianShadowMapsFShaderID);

if (glGetShaderi(blurGaussianShadowMapsFShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(blurGaussianShadowMapsFShaderID, 1024));
}

glAttachShader(blurGaussianShadowMapsProgramID, blurGaussianShadowMapsFShaderID);








glLinkProgram(blurGaussianShadowMapsProgramID);
if (glGetProgrami(blurGaussianShadowMapsProgramID, GL_LINK_STATUS) == 0) {
throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(blurGaussianShadowMapsProgramID, 1024));
}

if (blurGaussianShadowMapsVShaderID != 0) {
glDetachShader(blurGaussianShadowMapsProgramID, blurGaussianShadowMapsVShaderID);
}
if (blurGaussianShadowMapsFShaderID != 0) {
glDetachShader(blurGaussianShadowMapsProgramID, blurGaussianShadowMapsFShaderID);
}

glValidateProgram(blurGaussianShadowMapsProgramID);
if (glGetProgrami(blurGaussianShadowMapsProgramID, GL_VALIDATE_STATUS) == 0) {
System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(blurGaussianShadowMapsProgramID, 1024));
}


glUseProgram(blurGaussianShadowMapsProgramID);



blurGaussianShadowMapsSceneSamplerLocation = glGetUniformLocation(blurGaussianShadowMapsProgramID,"scene");

glUniform1i(blurGaussianShadowMapsSceneSamplerLocation,0);



	



































// Gaussian Blur Scene Shadows Shader Program






String vShaderGaussianBlurSceneShadows = "#version 430\n" +
" layout (location = 0) in vec3 vPosition;\n" +
" layout (location = 1) in vec2 tCoord;\n" +
" layout (location = 2) in vec3 farPlaneCorner;\n" +

" out vec2 sampleCoord;\n" +
" out vec3 vRay;"+

" uniform mat4 invProjM;"+
" uniform mat4 invCamM;"+

" void main() {\n" +
"  sampleCoord = tCoord;\n" +
" vRay = farPlaneCorner;"+

" gl_Position = vec4(vPosition,1.0);\n" +
" } ";

String fShaderGaussianBlurSceneShadows = "#version 430\n" +
" in vec2 sampleCoord;\n" +
" in vec3 vRay;"+		
" layout (location = 0) out vec4 shadow;\n"+
" layout (location = 1) out vec4 ambient;\n"+

" uniform sampler2D sceneShadows;\n"+
" uniform sampler2D sceneAmbientShadows;\n"+

" uniform sampler2D scenePositions;\n"+

" uniform sampler2D sceneShadowRadius;\n"+
"uniform int horizontal;"+
" uniform mat4 invProjM;"+
" uniform mat4 invCamM;"+
 //"uniform float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);"+
//"uniform float weight[5] = float[] (0.116923,	0.116014,	0.11333,	0.108994,	0.103201);"+
//"uniform float weight[5] = float[] (0.121471,	0.119799,	0.114921,	0.107228,	0.097316);"+
// " uniform float weight[5] = float[] (0.152781,	0.144599,	0.122589,	0.093095,	0.063327);"+
//"uniform float weight[5] = float[] (0.143569,	0.137535,	0.120911,	0.097548,	0.072222);"+
//"uniform float weight[5] = float[] (0.382928,	0.241732,	0.060598,	0.005977,	0.000229);"+
//"uniform float weight[5] = float[] (0.246551,	0.204083,	0.115735,	0.044953,	0.011954);"+
//"uniform float weight[5] = float[] (0.27938,	0.21879,	0.105053,	0.030904,	0.005563);"+
//" uniform float weight[6] = float[] (0.1,	0.17,	0.12,	0.08,	0.05,	0.03);"+
 //" uniform float weight[6] = float[] (0.10082,	0.099738,	0.09656,	0.091487,	0.084829,	0.076976);"+
//" uniform float weight[6] = float[] ( 0.09681,	0.096188,	0.094345,	0.091351,	0.087318,	0.082394);"+
//" uniform float weight[9] = float[] ( 0.158626,	0.146586,	0.115676,	0.077951,	0.044857,	0.022042,	0.009248,	0.003314,	0.001014);" + 
//  "uniform float weight[5] = float[] (0.1, 0.17, 0.14, 0.1, 0.04);"+
//"uniform float weight[3] = float[] (0.55,0.2,0.075);"+
//"uniform float weight[4] = float[] (0.1,0.19,0.14,0.12);"+
//"uniform float weight[2] = float[] (0.2,0.4);"+
//"uniform float weight[2] = float[] (0.1,0.45);"+
//"uniform float weight[2] = float[] (0.01,0.495);"+
//"uniform float weight[2] = float[] (0.3,0.35);"+
//"uniform float weight[2] = float[] (0.9,0.05);"+
//" uniform float weight[3] = float[] (0.1, 0.25,0.2);"+
//" uniform float weight[3] = float[] (0.3, 0.2,0.15);"+
//" uniform float weight[3] = float[] (0.38, 0.2,0.11);"+
//"  uniform float weight[3] = float[] (0.234942,	0.215677,	0.166852);"+
//" uniform float weight[3] = float[] (0.222338,	0.210431,	0.1784);"+
//" uniform float weight[3] = float[] (0.212543,	0.206038,	0.187691);"+
//" uniform float weight[3] = float[] (0.05, 0.25,0.225);"+
//" uniform float weight[3] = float[] (0.38774,	0.24477,	0.06136);"+
//" uniform float weight[3] = float[] (0.202001,	0.200995,	0.198005);"+
//" uniform float weight[3] = float[] (0.525136,	0.221542,	0.01589);"+
//" uniform float weight[3] = float[] (0.423805,	0.242796,	0.045302);"+
//" uniform float weight[3] = float[] (0.288713,	0.233062,	0.122581);"+
//" uniform float weight[3] = float[] (0.30136,	0.236003,	0.113318);"+
//" uniform float weight[3] = float[] (0.313405,	0.238403,	0.104894);"+
//" uniform float weight[3] = float[] (0.335568,	0.241812,	0.090404);"+
//" uniform float weight[4] = float[] (	0.25259,	0.209081,	0.118569,	0.046054);"+
//" uniform float weight[4] = float[] (	0.227159,	0.196677,	0.127647,	0.062096);"+
 //" uniform float weight[4] = float[] (	0.192962,	0.17714,	0.137039,	0.089341);"+
//" uniform float weight[4] = float[] (		0.167808,	0.160755,	0.141324,	0.114017);"+
//" uniform float weight[4] = float[] (0.144717,	0.144248,	0.142848,	0.140546);"+

" vec3 getVRay() {"+
" return ( vRay );"+
" }"+

" void main() {\n" +

" float weight[2];"+

//" float blurRadius = texture(sceneShadowRadius, sampleCoord).r;"+
"float blurRadius = 1.0f;"+
//"     weight = float[] (1.0,	0.0,	0.0);"+
" weight = float[] (1.0, 0.0);"+
" if (blurRadius < 0.0016) {"+
//
//"     weight = float[] (0.9,	0.03,	0.02);"+              

//"     weight = float[] (0.8,	0.075,	0.025);"+
//" weight = float[] (0.744196,	0.127576,	0.000326);"+
//" weight = float[] (0.38774,	0.24477,	0.06136);"+
//"weight = float[] (0.55,0.2,0.075);"+
//"     weight = float[] (0.202001,	0.200995,	0.198005);"+
//"        weight = float[] (0.525136,	0.221542,	0.01589);"+
//" weight = float[] (0.595362,	0.196125,	0.006194);"+
" }"+


" else if (blurRadius < 0.0038) {"+
//" weight = float[] (0.38774,	0.24477,	0.06136);"+
//"        weight = float[] (0.525136,	0.221542,	0.01589);"+
//"     weight = float[] (0.8,	0.075,	0.025);"+
//" weight = float[] (0.744196,	0.127576,	0.000326);"+
//" weight = float[] (0.595362,	0.196125,	0.006194);"+
//"     weight = float[] (0.202001,	0.200995,	0.198005);"+

" }"+

" else {"+


//" weight = float[] (0.38774,	0.24477,	0.06136);"+
//"        weight = float[] (0.525136,	0.221542,	0.01589);"+
//"     weight = float[] (0.8,	0.075,	0.025);"+
//" weight = float[] (0.744196,	0.127576,	0.000326);"+
//"     weight = float[] (0.202001,	0.200995,	0.198005);"+
//" weight = float[] (0.1, 0.45);"+
//" weight = float[] (0.25,0.75/2.0);"+
" }"+

/*
" float weight[5];"+

" float blurRadius = texture(sceneShadowRadius, sampleCoord).r;"+

" if (blurRadius > 0.0024) {"+
"     weight = float[] (0.121471,	0.119799,	0.114921,	0.107228,	0.097316);"+
" }"+
" else {"+
"     weight = float[] (0.8,	0.075,	0.025, 0.0, 0.0);"+
" }"+
*/
" int blurSize = 2;"+


//"     float[] weightAmbient = float[] (0.202001,	0.200995,	0.198005);"+
" float[] weightAmbient = float[] (0.5,0.25);"+
//" float[] weightAmbient = float[] (0.38774,	0.24477,	0.06136);"+

" float d = 0.01;"+
"vec2 tex_oset = 1.0 / vec2(1920.0,1080.0);\n"+ 
"float resultOriginalShadow = ( texture(sceneShadows, sampleCoord).r  );\n"+ 
"float resultShadow = weight[0]*resultOriginalShadow;\n"+ 

"float resultOriginalAmbient = ( texture(sceneAmbientShadows, sampleCoord).r  );\n"+ 
"float resultAmbient = weightAmbient[0]*resultOriginalAmbient;\n"+ 


"vec3 pos =  (invCamM*vec4(getVRay()*texture(scenePositions, sampleCoord).r,1.0)).rgb;"+

" if (horizontal == 1) {\n"+
"for(int i = 1; i < blurSize; ++i)\n"+
"{\n"+


"vec3 pos1 =  (invCamM*vec4(getVRay()*texture(scenePositions, sampleCoord + 1.0*vec2(tex_oset.x * i, 0.0) ).r,1.0)).rgb;"+

"float potentialResultOriginalShadow1 = ( texture(sceneShadows, sampleCoord + 1.0*vec2(tex_oset.x * i, 0.0)).r );"+
"float potentialResultOriginalAmbient1 = ( texture(sceneAmbientShadows, sampleCoord + 1.0*vec2(tex_oset.x * i, 0.0)).r );"+

" if ( abs(length(pos1 - pos)) <= d) {"+
"       resultShadow = resultShadow + weight[i]*potentialResultOriginalShadow1;"+
"       resultAmbient = resultAmbient + weightAmbient[i]*potentialResultOriginalAmbient1;"+

" }"+
" else {"+
"       resultShadow = resultShadow + weight[i]*resultOriginalShadow;"+
"       resultAmbient = resultAmbient + weightAmbient[i]*resultOriginalAmbient;"+

" }"+

"vec3 pos2 =  (invCamM*vec4(getVRay()*texture(scenePositions, sampleCoord - 1.0*vec2(tex_oset.x * i, 0.0) ).r,1.0)).rgb;"+


"float potentialResultOriginalShadow2 = ( texture(sceneShadows, sampleCoord - 1.0*vec2(tex_oset.x * i, 0.0)).r );"+
"float potentialResultOriginalAmbient2 = ( texture(sceneAmbientShadows, sampleCoord - 1.0*vec2(tex_oset.x * i, 0.0)).r );"+

" if ( abs(length(pos2 - pos)) <= d) {"+
"       resultShadow = resultShadow + weight[i]*potentialResultOriginalShadow2;"+
"       resultAmbient = resultAmbient + weightAmbient[i]*potentialResultOriginalAmbient2;"+

" }"+
" else {"+
"       resultShadow = resultShadow + weight[i]*resultOriginalShadow;"+
"       resultAmbient= resultAmbient+ weightAmbient[i]*resultOriginalAmbient;"+

" }"+

"}\n"+
" }\n"+
"else"+
"{"+
" for(int i = 1; i < blurSize; ++i)"+
"{"+

"vec3 pos1 =  (invCamM*vec4(getVRay()*texture(scenePositions, sampleCoord + 1.0*vec2(0.0,tex_oset.y * i) ).r,1.0)).rgb;"+


"float potentialResultOriginalShadow1 = ( texture(sceneShadows, sampleCoord + 1.0*vec2(0.0,tex_oset.y * i)).r );"+
"float potentialResultOriginalAmbient1 = ( texture(sceneAmbientShadows, sampleCoord + 1.0*vec2(0.0,tex_oset.y * i)).r );"+

" if ( abs(length(pos1 - pos)) <= d) {"+
"       resultShadow = resultShadow + weight[i]*potentialResultOriginalShadow1;"+
"       resultAmbient = resultAmbient + weightAmbient[i]*potentialResultOriginalAmbient1;"+

" }"+
" else {"+
"       resultShadow = resultShadow + weight[i]*resultOriginalShadow;"+
"       resultAmbient = resultAmbient + weightAmbient[i]*resultOriginalAmbient;"+

" }"+

"vec3 pos2 =  (invCamM*vec4(getVRay()*texture(scenePositions, sampleCoord - 1.0*vec2(0.0,tex_oset.y * i) ).r,1.0)).rgb;"+

"float potentialResultOriginalShadow2 = ( texture(sceneShadows, sampleCoord - 1.0*vec2(0.0,tex_oset.y * i)).r );"+
"float potentialResultOriginalAmbient2 = ( texture(sceneAmbientShadows, sampleCoord - 1.0*vec2(0.0,tex_oset.y * i)).r );"+

" if ( abs(length(pos2 - pos)) <= d) {"+
"       resultShadow = resultShadow + weight[i]*potentialResultOriginalShadow2;"+
"       resultAmbient = resultAmbient + weightAmbient[i]*potentialResultOriginalAmbient2;"+


" }"+
" else {"+
"       resultShadow = resultShadow + weight[i]*resultOriginalShadow;"+
"       resultAmbient = resultAmbient + weightAmbient[i]*resultOriginalAmbient;"+

" }"+

"}"+
"}"+

" resultShadow = clamp(resultShadow, 0.0, 1.0);"+
" resultAmbient = clamp(resultAmbient, 0.0, 1.0);"+

" shadow =  vec4(resultShadow, 0.0, 0.0, 1.0);"+
" ambient = vec4(resultAmbient,0.0,0.0,1.0);"+
"   } ";







blurGaussianSceneShadowsProgramID = glCreateProgram();

glUseProgram(blurGaussianSceneShadowsProgramID);

if (blurGaussianSceneShadowsProgramID == 0) {
throw new Exception("Could not create Shader");
}


blurGaussianSceneShadowsVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (blurGaussianSceneShadowsVShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}

glShaderSource(blurGaussianSceneShadowsVShaderID, vShaderGaussianBlurSceneShadows);
glCompileShader(blurGaussianSceneShadowsVShaderID );

if (glGetShaderi(blurGaussianSceneShadowsVShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(blurGaussianSceneShadowsVShaderID, 1024));
}

glAttachShader(blurGaussianSceneShadowsProgramID, blurGaussianSceneShadowsVShaderID);






blurGaussianSceneShadowsFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (blurGaussianSceneShadowsFShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(blurGaussianSceneShadowsFShaderID, fShaderGaussianBlurSceneShadows);
glCompileShader(blurGaussianSceneShadowsFShaderID);

if (glGetShaderi(blurGaussianSceneShadowsFShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(blurGaussianSceneShadowsFShaderID, 1024));
}

glAttachShader(blurGaussianSceneShadowsProgramID, blurGaussianSceneShadowsFShaderID);








glLinkProgram(blurGaussianSceneShadowsProgramID);
if (glGetProgrami(blurGaussianSceneShadowsProgramID, GL_LINK_STATUS) == 0) {
throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(blurGaussianSceneShadowsProgramID, 1024));
}

if (blurGaussianSceneShadowsVShaderID != 0) {
glDetachShader(blurGaussianSceneShadowsProgramID, blurGaussianSceneShadowsVShaderID);
}
if (blurGaussianSceneShadowsFShaderID != 0) {
glDetachShader(blurGaussianSceneShadowsProgramID, blurGaussianSceneShadowsFShaderID);
}

glValidateProgram(blurGaussianSceneShadowsProgramID);
if (glGetProgrami(blurGaussianSceneShadowsProgramID, GL_VALIDATE_STATUS) == 0) {
System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(blurGaussianSceneShadowsProgramID, 1024));
}


glUseProgram(blurGaussianSceneShadowsProgramID);



blurGaussianSceneShadowsLocation = glGetUniformLocation(blurGaussianSceneShadowsProgramID,"sceneShadows");
blurGaussianSceneAmbientShadowsLocation = glGetUniformLocation(blurGaussianSceneShadowsProgramID,"sceneAmbientShadows");

blurGaussianScenePositionsLocation = glGetUniformLocation(blurGaussianSceneShadowsProgramID,"scenePositions");
blurGaussianSceneShadowRadiusLocation = glGetUniformLocation(blurGaussianSceneShadowsProgramID,"sceneShadowRadius");

blurGaussianSceneShadowsHorizontalLocation = glGetUniformLocation(blurGaussianSceneShadowsProgramID,"horizontal");

blurGaussianInvProjMLocation = glGetUniformLocation(blurGaussianSceneShadowsProgramID,"invProjM");
blurGaussianInvCamMLocation = glGetUniformLocation(blurGaussianSceneShadowsProgramID,"invCamM");






























    
    
    
    
    
    
    
    
     
    // Temporal Filtering Program
    

	
	String vShaderTemporalFiltering = "#version 430\n" +
" layout (location = 0) in vec3 vPosition;\n" +
" layout (location = 1) in vec2 tCoord;\n" +

" out vec2 sampleCoord;\n" +

" void main() {\n" +
"  sampleCoord = tCoord;\n" +
" gl_Position = vec4(vPosition,1.0);\n" +
" } ";

String fShaderTemporalFiltering = "#version 430\n" +
" in vec2 sampleCoord;\n" +
" out float fragColor;"+
" uniform sampler2D previousScene;\n"+
" uniform sampler2D presentScene;\n"+
" uniform int start;"+

" void main() {\n" +

" float previousAmount;"+
" float presentAmount;"+

" if (start == 1) {"+
"   previousAmount = 0.0;"+
"   presentAmount = 1.0;"+
" }"+
" else {"+
"   previousAmount = 0.1;"+
"   presentAmount = 0.9;"+
" }"+

" float previousDepth =  texture(previousScene,sampleCoord).r;"+
" float presentDepth =  texture(presentScene,sampleCoord).r;"+


" fragColor = presentAmount*presentDepth + previousAmount*previousDepth ;"+


"   } ";







temporalFilteringProgram = glCreateProgram();

glUseProgram(temporalFilteringProgram);

if (temporalFilteringProgram == 0) {
throw new Exception("Could not create Shader");
}


temporalFilteringVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (temporalFilteringVShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}

glShaderSource(temporalFilteringVShaderID, vShaderTemporalFiltering);
glCompileShader(temporalFilteringVShaderID );

if (glGetShaderi(temporalFilteringVShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(temporalFilteringVShaderID, 1024));
}

glAttachShader(temporalFilteringProgram, temporalFilteringVShaderID);






temporalFilteringFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (temporalFilteringFShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(temporalFilteringFShaderID, fShaderTemporalFiltering);
glCompileShader(temporalFilteringFShaderID);

if (glGetShaderi(temporalFilteringFShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(temporalFilteringFShaderID, 1024));
}

glAttachShader(temporalFilteringProgram, temporalFilteringFShaderID);








glLinkProgram(temporalFilteringProgram);
if (glGetProgrami(temporalFilteringProgram, GL_LINK_STATUS) == 0) {
throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(temporalFilteringProgram, 1024));
}

if (temporalFilteringVShaderID != 0) {
glDetachShader(temporalFilteringProgram, temporalFilteringVShaderID);
}
if (temporalFilteringFShaderID != 0) {
glDetachShader(temporalFilteringProgram, temporalFilteringFShaderID);
}

glValidateProgram(temporalFilteringProgram);
if (glGetProgrami(temporalFilteringProgram, GL_VALIDATE_STATUS) == 0) {
System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(temporalFilteringProgram, 1024));
}


glUseProgram(temporalFilteringProgram);



				
				mainMenu = new Menu(0.0,0.0,mainMenuWrap,mainMenuButtons);
				activeMenu = mainMenu;
				sequence = "Main Menu";
				


				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				// Render complete scene shader before water
				
				
				String vShaderScene = "#version 430\n" +
		" layout (location = 0) in vec3 vPosition;\n" +
		" layout (location = 1) in vec2 tCoord;\n" +
		
		" out vec2 sampleCoord;\n" +
		
		" void main() {\n" +
		"  sampleCoord = tCoord;\n" +
		" gl_Position = vec4(vPosition,1.0);\n" +
		" } ";
		
		String fShaderScene = "#version 430\n" +
		
		" in vec2 sampleCoord;\n" +
		" out vec4 fragColor;"+
		
		" uniform sampler2D scene;"+
		" uniform sampler2D bloom1;"+
		" uniform sampler2D bloom2;"+
		" uniform sampler2D bloom3;"+
		
		/*
		" uniform sampler2D sceneAlbedoColors;"+
		" uniform sampler2D sceneNormals;\n"+
		" uniform sampler2D sceneStaticShadows;\n"+
		
		*/
		
		" uniform sampler2D sceneNormals;\n"+

		" uniform sampler2D sceneShadows;\n"+
		" uniform sampler2D sceneAmbientShadows;\n"+
		
		" uniform float screenBrightness;"+
		
		
		"struct LightSource {" +
		 "int type;"+
		 "vec3 directionOrLocation;"+
		 "vec3 color;"+
		 "float amount;"+
		 "};"+
		" uniform LightSource directionalLight;"+
		
		
		" void main() {\n" +
		
" vec3 aColor = texture(scene,sampleCoord).rgb;"+
 //" color = vec3(1.0,1.0,0.01);"+
 " vec3 bloomColor1 = texture(bloom1,sampleCoord).rgb;"+
 " vec3 bloomColor2 = texture(bloom2,sampleCoord).rgb;"+
 " vec3 bloomColor3 = texture(bloom3,sampleCoord).rgb;"+
		
		 " float shadowValue= texture(sceneShadows,sampleCoord).r;"+
		 " shadowValue = clamp(shadowValue, 0.0, 1.0);"+
		 " float ambientShadowValue = texture(sceneAmbientShadows,sampleCoord).r;"+
		 
		 
		 " vec3 aNormal = texture(sceneNormals,sampleCoord).rgb;"+
		 " vec3 sceneColor;"+
		 
		 "if ( aNormal.x == 0.0 && aNormal.y == 0.0 && aNormal.z == 0.0) {"+
		 "      sceneColor = (1.0 - shadowValue )*aColor;"+
		 "}"+
		 "else if ( aNormal.x == 1000.0) {"+
		 "      sceneColor = (1.0 - shadowValue)*0.9*aColor + 0.1*aColor;"+ 
		 "}"+
		 "else {"+
		 "	 float diffuseValue = (1.0 - 0.79*shadowValue)*0.8 *max(dot(normalize(-1.0*directionalLight.directionOrLocation), normalize(aNormal)),0.0);"+
		 //"diffuseValue = (1.0 - shadowValue) * 0.8;"+
		 " 	 float ambientValue = 1.0 ;"+
		 " 	 sceneColor = 1.0*(1.40*0.6*diffuseValue*(directionalLight.color)*aColor + 0.25*1.00*ambientShadowValue*normalize(directionalLight.color)*aColor);"+
		 //"sceneColor = vec3(aNormal.x,aNormal.y,aNormal.z);"+
		 //" sceneColor = aColor;"+
		 "}"+
		 
		 " vec3 x = (1.0)*((sceneColor) + 0.01*(0.4*bloomColor1 + 0.8*bloomColor2 + 1.2*bloomColor3));"+
		//" vec3 x = 1.0*(aNormal);"+
		 
		"  float a = 2.5f;"+
		// a originally 2.5, higher values means bright regions more saturated
		"float b = 0.03f;"+
		"float c = 2.5f;"+
		// d = 0.59 originally
		// d = 0.059 higher contrast
		"float d = 0.022f;"+
		"float e = 0.34f;"+
		// e originally 0.14
		" vec3 mapped = (x*(a*x+b))/(x*(c*x+d)+e);"+
		
   		" mapped = pow(mapped, vec3(1.0 / 2.2));"+

		"fragColor = vec4(1.0*x,1.0);"+
		//"fragColor = vec4(shadowAnimated,shadowAnimated,shadowAnimated,1.0);"+
 		
		//" fragColor = vec4(1.0,0.0,0.0,1.0);"+
 	//	" fragColor = vec4(diffuseLightAmount,diffuseLightAmount,diffuseLightAmount,1.0);"+
		//" fragColor = vec4(normalize(normal),1.0);"+
 		"   } ";
		
		
			
		            
		     
		
        
        sceneProgramID = glCreateProgram();
        
        glUseProgram(sceneProgramID);
        
        if (sceneProgramID == 0) {
            throw new Exception("Could not create Shader");
        }
        
        
        sceneVShaderID = glCreateShader(GL_VERTEX_SHADER);
        if (sceneVShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
        }

        glShaderSource(sceneVShaderID, vShaderScene);
        glCompileShader(sceneVShaderID );

        if (glGetShaderi(sceneVShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(sceneVShaderID, 1024));
        }

        glAttachShader(sceneProgramID, sceneVShaderID);

        
        
        
        

        sceneFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        if (sceneFShaderID == 0) {
            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
        }

        glShaderSource(sceneFShaderID, fShaderScene);
        glCompileShader(sceneFShaderID);

        if (glGetShaderi(sceneFShaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(sceneFShaderID, 1024));
        }

        glAttachShader(sceneProgramID, sceneFShaderID);
        
        
        
        
        
        
        
        
        glLinkProgram(sceneProgramID);
        if (glGetProgrami(sceneProgramID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(sceneProgramID, 1024));
        }

        if (sceneVShaderID != 0) {
            glDetachShader(sceneProgramID, sceneVShaderID);
        }
        if (sceneFShaderID != 0) {
            glDetachShader(sceneProgramID, sceneFShaderID);
        }

        glValidateProgram(sceneProgramID);
        if (glGetProgrami(sceneProgramID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(sceneProgramID, 1024));
        }
    
	
        glUseProgram(sceneProgramID);
        
        sceneLocation = glGetUniformLocation(sceneProgramID,"scene");
        bloom1Location = glGetUniformLocation(sceneProgramID,"bloom1");
        bloom2Location = glGetUniformLocation(sceneProgramID,"bloom2");
        bloom3Location = glGetUniformLocation(sceneProgramID,"bloom3");
        
    /*    
        sceneAlbedoColorsLocation = glGetUniformLocation(sceneProgramID,"sceneAlbedoColors");
        sceneNormalsLocation = glGetUniformLocation(sceneProgramID,"sceneNormals");
        sceneStaticShadowsLocation = glGetUniformLocation(sceneProgramID,"sceneStaticShadows");
      */
        
        sceneNormalsLocation = glGetUniformLocation(sceneProgramID,"sceneNormals");
        
        
        sceneShadowsLocation = glGetUniformLocation(sceneProgramID,"sceneShadows");
        sceneAmbientShadowsLocation = glGetUniformLocation(sceneProgramID,"sceneAmbientShadows");
        
        sceneDirectionalLightTypeLocation = glGetUniformLocation(sceneProgramID,"directionalLight.type");
        sceneDirectionalLightDirectionLocation = glGetUniformLocation(sceneProgramID,"directionalLight.directionOrLocation");
        sceneDirectionalLightColorLocation = glGetUniformLocation(sceneProgramID,"directionalLight.color");
        sceneScreenBrightnessLocation = glGetUniformLocation(sceneProgramID,"screenBrightness");
       
        


        
        
        
        
        
        
        
        
        
        
        
        
        

		
		
		
		// Render final scene with refractive meshes shader
		
		
		String vShaderSceneComplete = "#version 430\n" +
" layout (location = 0) in vec3 vPosition;\n" +
" layout (location = 1) in vec2 tCoord;\n" +

" out vec2 sampleCoord;\n" +

" void main() {\n" +
"  sampleCoord = tCoord;\n" +
" gl_Position = vec4(vPosition,1.0);\n" +
" } ";

String fShaderSceneComplete = "#version 430\n" +

" in vec2 sampleCoord;\n" +
" out vec4 fragColor;"+

" uniform sampler2D scenePreRefractiveObjects;"+
" uniform sampler2D prevScenePreRefractiveObjects;"+

" uniform sampler2D scenePosPreRefractiveObjects;"+

" uniform sampler2D refractiveObjects;"+
" uniform sampler2D waterNormals;\n"+
" uniform sampler2D waterYPosWorld;\n"+

" uniform samplerCube reflectionsCubeMap;"+

" uniform mat4 projM;"+
" uniform mat4 wVM;"+


" uniform float screenBrightness;"+


"struct WaterBody{"
+ "vec3 pos;"
+ "vec2 dimensions;"
+ "vec2 scale;"
+ "};"+
"uniform sampler2D waterBody1HeightField;"+
"uniform WaterBody waterBody1;"+


"struct LightSource {" +
 "int type;"+
 "vec3 directionOrLocation;"+
 "vec3 color;"+
 "float amount;"+
 "};"+
" uniform LightSource directionalLight;"+


" void main() {\n" +



" vec3 baselineColor = texture(scenePreRefractiveObjects,sampleCoord).rgb;"+
" vec3 baselineColor2 = texture(prevScenePreRefractiveObjects,sampleCoord).rgb;"+

" vec3 baselineWaterPos = texture(waterYPosWorld,sampleCoord).rgb;"+
" vec3 baselineScenePos = texture(scenePosPreRefractiveObjects,sampleCoord).rgb;"+

" float isRefractive = texture(refractiveObjects,sampleCoord).r;"+


" vec3 fragColorHDR= vec3(1.0,1.0,1.0);"+

" float highestDepthV = 22.0;"+

" if (isRefractive >= 0.9) {"+



"  float refractionScalar = 0.0045;"+
"   vec3 waterNormal = normalize(texture(waterNormals,sampleCoord).rgb);"+
//"    fragColorHDR = vec3(0.01,0.2,1.0);"+

//vec3(0.1,0.4,0.2)

"    float waterShadingBase = 0.9 + 0.1*max(dot(normalize(waterNormal), normalize(vec3(0.2,1.0,1.1))), 0.0);"+
//"   waterShadingBase = 1.0;"+
"  vec3 waterShadingColor = 0.4*vec3(0.1,0.4,0.2);"+
"    vec2 refractionSampleVec = vec2(sampleCoord.x + refractionScalar*waterNormal.x, sampleCoord.y + refractionScalar*waterNormal.z);"+

"    vec3 refractedColor = vec3(1.0,1.0,2.0);"+

"    vec3 scenePosValue = baselineScenePos;"+
"    vec3 waterPosValue = baselineWaterPos;"+

"    if (texture(refractiveObjects,refractionSampleVec).r < 0.9) {"+
"        refractedColor = baselineColor;"+
//		 "waterPosValue = baselineWaterPos;"+
//         "scenePosValue = baselineScenePos;"+
         //" 		 waterPosValue = texture(waterYPosWorld,refractionSampleVec).rgb;"+
         //" 		scenePosValue = texture(scenePosPreRefractiveObjects,refractionSampleVec).rgb;"+

         "    }"+
"    else {"+
"        refractedColor = texture(scenePreRefractiveObjects,vec2(sampleCoord.x + refractionScalar*waterNormal.x, sampleCoord.y + refractionScalar*waterNormal.z)).rgb;"+
" 		 waterPosValue = baselineWaterPos;"+
" 		scenePosValue = texture(scenePosPreRefractiveObjects,refractionSampleVec).rgb;"+
//"waterPosValue = baselineWaterPos;"+
//"scenePosValue = baselineScenePos;"+

"    }"+

" vec3 vVec = -1.0*(wVM*vec4(waterPosValue,1.0)).xyz;"+
" vec3 mVVec = (2.0 * dot(normalize(vVec),normalize(vec3(waterNormal.x,5877*pow(waterNormal.y,6.0),waterNormal.z)) )*normalize(vec3(waterNormal.x,5877*pow(waterNormal.y,6.0),waterNormal.z))  - normalize(vVec));"+
" float dotSpec = clamp(dot(normalize(mVVec).xyz, -1.0*normalize(directionalLight.directionOrLocation))*0.5 + 0.5, 0.0, 1.0);"+
" float spec = pow(dotSpec, 512.0)* (0.6*1.8 + 0.2);"+
" spec = spec + spec* 25.0*clamp(0.6 - 0.05,0.0,1.0);"+

" float accumulatedWater = length(waterPosValue - scenePosValue) ;"+
" float waterDepth = abs(waterPosValue.y - scenePosValue.y);"+
" vec3 waterColor = mix(refractedColor, 0.2*vec3(0.85,0.96,0.1), clamp(accumulatedWater/10.0, 0.0 , 1.0 ));"+
" vec3 actualWaterColor = mix(waterColor, 0.2141*vec3(0.047,0.16404,0.604), clamp(waterDepth/vec3(0.48,4.0,12.0), vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0)));"+

" vec3 waterColor2 = mix(refractedColor, 0.22*vec3(0.037,0.324,0.204), clamp(waterDepth/vec3(0.1,4.4,4.8), vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0)));"+
" vec3 actualWaterColor2 = mix(waterColor2, 0.5*vec3(0.02,0.142,0.4), clamp(accumulatedWater/vec3(120.0,120.0,120.0), vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0)));"+

" vec4 waterPosPreNDC = (projM*wVM*vec4(baselineWaterPos,1.0));"+
" vec3 waterPosNDC = waterPosPreNDC.xyz/waterPosPreNDC.w;"+


"  if ( waterPosNDC.y <= 0.0) {"+
//"    fragColorHDR =  actualWaterColor + 1.0*vec3(1.0*spec, 0.9*spec, 0.5*spec);"+
//"    fragColorHDR =  reflectionValue;"+

//    Fresnel 

" vec3 waterVSpace = (wVM*vec4(baselineWaterPos,1.0)).xyz;"+
"  vec3 vVec = -1.0*waterVSpace;"+
" vec3 reflectionValue = texture(reflectionsCubeMap,reflect(-1.0*vVec,normalize(vec3(waterNormal.x, 38.0070470600*(abs(waterNormal.y)/waterNormal.y)*pow(abs(waterNormal.y),1.6), waterNormal.z)) )).rgb + 0.03*vec3(1.0,1.0,1.0);"+
//" reflectionValue = vec3(1.0,0.0,0.0);"+
" float vVecXToUse = 1.0;"+
" if (vVec.x >= -0.2 && vVec.x <= 0.2) {"+
"      vVecXToUse = -1.2;"+
" }"+
" else {"+
"     vVecXToUse = -1.2;"+
" }"+
// Was 377.0502 and 4.0
// Was 5877.0502 and 6.0
// 2225877.0502 and 10.0 is good
"  float cosVVecAndWaterNormalAngle = clamp(dot(normalize(normalize(vec3(1.0*waterNormal.x,15877.0502*abs(pow(abs(waterNormal.y),6.0)),1.0*waterNormal.z))),normalize(vec3(vVecXToUse,0.99*vVec.y,1.0*vVec.z))),  0.0,   1.0);"+

" float amountUpright = dot(normalize(vec3(1.0*waterNormal.x,877.0502*abs(pow(abs(waterNormal.y),6.0)),1.0*waterNormal.z)), vec3(0.0,1.0,0.0));"+
"if ( cosVVecAndWaterNormalAngle <= 0.2) { "+
//"cosVVecAndWaterNormalAngle = clamp(dot(normalize(normalize(vec3(1.0*waterNormal.x,8000.0*abs(pow(abs(waterNormal.y),6.0)),1.0*waterNormal.z))),normalize(vec3(1.0*vVec.x,0.99*vVec.y,1.0*vVec.z))),  0.0,   1.0);"+
//" reflectionValue = (0.5*reflectionValue + 0.25*vec3(1.0,1.0,1.0)) * ( 1.0 - 10.0*cosVVecAndWaterNormalAngle);"+
"}"+


//" float fresnel = mix(0.01, 1.0, pow(1.0 - cosVVecAndWaterNormalAngle,3.0));"+
" float fresnelMixer = (1.0)/(pow(1.0 + cosVVecAndWaterNormalAngle,8.0));"+
" float fresnel = mix(0.0, 1.0,fresnelMixer);"+
//"    fragColorHDR =  actualWaterColor + (1.0 - cosVVecAndWaterNormalAngle)*reflectionValue + 0.0*0.5*vec3(0.7*spec, 0.62*spec, 0.3*spec);"+
"       fragColorHDR = mix(refractedColor, mix(1.0*actualWaterColor, 0.5*reflectionValue + 0.0*vec3(0.7*spec,0.5*spec,0.22*spec), fresnel) + 0.0*vec3(0.7*spec,0.5*spec,0.22*spec), clamp(accumulatedWater/0.2, 0.0, 1.0));"+
//"    fragColorHDR = 1.0*(1.0 - fresnel)*actualWaterColor  + 1.0*(1.0*fresnel)*reflectionValue + 0.0*0.5*vec3(0.7*spec, 0.62*spec, 0.3*spec);"+
//"    fragColorHDR = actualWaterColor;"+
//"    fragColorHDR = reflectionValue;"+

" }"+
"  else {"+
"    fragColorHDR =  vec3(0.08,0.01,0.2)*refractedColor;"+

" }"+


" }" +
" else if (isRefractive != 0.0) {"+

" vec3 scenePosValue = texture(scenePosPreRefractiveObjects,sampleCoord).rgb;"+
" vec3 waterPosValue = texture(waterYPosWorld,sampleCoord).rgb;"+
" float waterDepth = abs(waterPosValue.y - scenePosValue.y);"+
" float accumulatedWater = length(vec3(0.0,0.0,0.0) - (wVM*vec4(scenePosValue,1.0) ).xyz ) ;"+
" vec3 waterColor = mix(baselineColor, 0.4*vec3(0.1,0.56,0.4), clamp(accumulatedWater/20.0, 0.0 , 1.0 ));"+


"    vec2 screenPixelSize = vec2(1.0/1920.0,1.0/1080.0);"+
"    float isRefractive1 = texture(refractiveObjects, sampleCoord - vec2(0.0, 1.0*screenPixelSize.y) ).r;"+

"    float isRefractive2 = texture(refractiveObjects, sampleCoord - vec2(0.0, 2.0*screenPixelSize.y) ).r;"+
"    float isRefractive3 = texture(refractiveObjects, sampleCoord - vec2(0.0, 4.0*screenPixelSize.y) ).r;"+


"    if (isRefractive1 == 1.0 || isRefractive2 == 1.0 || isRefractive3 == 1.0) {"+
        " fragColorHDR = baselineColor;"+
"    }"+
"    else {"+
"       fragColorHDR = waterColor;"+
"    }"+

" }"+
" else {"+

"    fragColorHDR = vec3(baselineColor) ;"+
//"    fragColorHDR = 0.5*(vec3(baselineColor) + vec3(baselineColor2));"+




					//
					// For testing cube mapping
					//
//"     fragColorHDR = (texture( reflectionsCubeMap,vec3(1.0,0.1,1.0) )).rgb;"+





" }"+

"  float a = 2.5f;"+
// a originally 2.5, higher values means bright regions more saturated
"float b = 0.03f;"+
"float c = 2.5f;"+
// d = 0.59 originally
// d = 0.059 higher contrast
"float d = 0.022f;"+
"float e = 0.44f;"+
// e originally 0.14
" vec3 mapped = (fragColorHDR*(a*fragColorHDR+b))/(fragColorHDR*(c*fragColorHDR+d)+e);"+

	" mapped = pow(mapped, vec3(1.0 / 2.2));"+

//"fragColor = vec4(1.0*isRefractive, 1.0*isRefractive,1.0*isRefractive,1.0);"+

"fragColor = vec4(1.0*mapped,1.0);"+
//"fragColor = vec4(shadowAnimated,shadowAnimated,shadowAnimated,1.0);"+
	
//" fragColor = vec4(1.0,0.0,0.0,1.0);"+
//	" fragColor = vec4(diffuseLightAmount,diffuseLightAmount,diffuseLightAmount,1.0);"+
//" fragColor = vec4(normalize(normal),1.0);"+
	"   } ";


	

     


sceneCompleteProgramID = glCreateProgram();

glUseProgram(sceneCompleteProgramID);

if (sceneCompleteProgramID == 0) {
    throw new Exception("Could not create Shader");
}


sceneCompleteVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (sceneCompleteVShaderID == 0) {
    throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}

glShaderSource(sceneCompleteVShaderID, vShaderSceneComplete);
glCompileShader(sceneCompleteVShaderID );

if (glGetShaderi(sceneCompleteVShaderID, GL_COMPILE_STATUS) == 0) {
    throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(sceneCompleteVShaderID, 1024));
}

glAttachShader(sceneCompleteProgramID, sceneCompleteVShaderID);






sceneCompleteFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (sceneCompleteFShaderID == 0) {
    throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(sceneCompleteFShaderID, fShaderSceneComplete);
glCompileShader(sceneCompleteFShaderID);

if (glGetShaderi(sceneCompleteFShaderID, GL_COMPILE_STATUS) == 0) {
    throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(sceneCompleteFShaderID, 1024));
}

glAttachShader(sceneCompleteProgramID, sceneCompleteFShaderID);








glLinkProgram(sceneCompleteProgramID);
if (glGetProgrami(sceneCompleteProgramID, GL_LINK_STATUS) == 0) {
    throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(sceneCompleteProgramID, 1024));
}

if (sceneCompleteVShaderID != 0) {
    glDetachShader(sceneCompleteProgramID, sceneCompleteVShaderID);
}
if (sceneCompleteFShaderID != 0) {
    glDetachShader(sceneCompleteProgramID, sceneCompleteFShaderID);
}

glValidateProgram(sceneCompleteProgramID);
if (glGetProgrami(sceneCompleteProgramID, GL_VALIDATE_STATUS) == 0) {
    System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(sceneCompleteProgramID, 1024));
}


glUseProgram(sceneCompleteProgramID);

scenePreRefractionLocation = glGetUniformLocation(sceneCompleteProgramID,"scenePreRefractiveObjects");
prevScenePreRefractionLocation = glGetUniformLocation(sceneCompleteProgramID,"prevScenePreRefractiveObjects");

scenePosPreRefractionLocation = glGetUniformLocation(sceneCompleteProgramID,"scenePosPreRefractiveObjects");

refractiveObjectsLocation = glGetUniformLocation(sceneCompleteProgramID,"refractiveObjects");
waterNormalsLocation = glGetUniformLocation(sceneCompleteProgramID,"waterNormals");
waterYPosWorldLocation = glGetUniformLocation(sceneCompleteProgramID,"waterYPosWorld");
reflectionsCubeMapLocation = glGetUniformLocation(sceneCompleteProgramID,"reflectionsCubeMap");


sceneCompleteDirectionalLightTypeLocation = glGetUniformLocation(sceneCompleteProgramID,"directionalLight.type");
sceneCompleteDirectionalLightDirectionLocation = glGetUniformLocation(sceneCompleteProgramID,"directionalLight.directionOrLocation");
sceneCompleteDirectionalLightColorLocation = glGetUniformLocation(sceneCompleteProgramID,"directionalLight.color");

sceneCompleteProjMLocation = glGetUniformLocation(sceneCompleteProgramID, "projM");
sceneCompleteWVMLocation = glGetUniformLocation(sceneCompleteProgramID, "wVM");

























// Shader Program for computing Ambient Occlusion value
// SSAO value


String vShaderShadows = "#version 430\n" +
" layout (location = 0) in vec3 vPosition;\n" +
" layout (location = 1) in vec2 tCoord;\n" +
" layout (location = 2) in vec3 farPlaneCorner;"+
" out vec2 sampleC;\n" +
" out vec3 vRay;"+

" uniform mat4 projMatrix;"+

" uniform mat4 invProjMatrix;"+


" void main() {\n" +
"  sampleC = tCoord;\n" +
//" vec4 a = (invProjMatrix*vec4(vec3(vPosition.xy,1.0),1.0));"+
" vRay = farPlaneCorner;"+
" gl_Position = vec4(vPosition,1.0);\n" +
" } ";

String fShaderShadows = "#version 430\n" +
" in vec2 sampleC;\n" +
" in vec3 vRay;"+		
" layout (location = 0) out vec4 shadowAmount;"+

" uniform sampler2D sceneNormals;\n"+
" uniform sampler2D scenePositions;\n"+
" uniform sampler2D noise;"+

//" uniform mat4 projVMatrix;"+
" uniform mat4 projMatrix;"+

" uniform mat4 invProjMatrix;"+
" uniform mat3 normalTransformMatrix;"+

"uniform vec3 sphereSamples[16];"+

" float pi = 3.1415926535897932384626433832795;"+

" vec3 getVRay() {"+
"   return normalize(vRay);"+
" }"+


" void main() {\n" +



  "vec3 sphereSamples2[16] = vec3[]("+
	      "vec3( 0.5381, 0.1856,-0.4319), vec3( 0.1379, 0.2486, 0.4430),"+
	      "vec3( 0.3371, 0.5679,-0.0057), vec3(-0.6999,-0.0451,-0.0019),"+
	      "vec3( 0.0689,-0.1598,-0.8547), vec3( 0.0560, 0.0069,-0.1843),"+
	      "vec3(-0.0146, 0.1402, 0.0762), vec3( 0.0100,-0.1924,-0.0344),"+
	      "vec3(-0.3577,-0.5301,-0.4358), vec3(-0.3169, 0.1063, 0.0158),"+
	      "vec3( 0.0103,-0.5869, 0.0046), vec3(-0.0897,-0.4940, 0.3287),"+
	      "vec3( 0.7119,-0.0154,-0.0918), vec3(-0.0533, 0.0596,-0.5411),"+
	      "vec3( 0.0352,-0.0631, 0.5460), vec3(-0.4776, 0.2847,-0.0271)"+
	  ");"+


"vec3 hemisphereSamples[16] = vec3[]("+
"vec3(+0.000000, -0.433013, +0.901388 ) * 1.000000 ,"+
"vec3(+0.000000, +0.353553, +0.935414 ) * 0.707107 ,"+
"vec3(-0.250000, +0.000000, +0.968246 ) * 0.577350 ,"+
"vec3(+0.433013, -0.000000, +0.901388 ) * 0.500000 ,"+
"vec3(+0.433013, +0.433013, +0.790569 ) * 0.447214 ,"+
"vec3(-0.467707, +0.467707, +0.750000 ) * 0.408248 ,"+
"vec3(-0.353553, -0.353553, +0.866025 ) * 0.377964 ,"+
"vec3(+0.395285, -0.395285, +0.829156 ) * 0.353553 ,"+
"vec3(+0.653281, +0.270598, +0.707107 ) * 0.333333 ,"+
"vec3(-0.864210, +0.357968, +0.353553 ) * 0.316228 ,"+
"vec3(-0.894543, -0.370532, +0.350000 ) * 0.301511 ,"+
"vec3(+0.692910, -0.287013, +0.661438 ) * 0.288675 ,"+
"vec3(+0.331414, +0.800103, +0.500000 ) * 0.267350 ,"+
"vec3(-0.302538, +0.730391, +0.612372 ) * 0.257261 ,"+
"vec3(-0.344946, -0.832774, +0.433013 ) * 0.228199 ,"+
"vec3(+0.317304, -0.766040, +0.559017 ) * 0.125374"+ 
");"+

" vec3 accelHemisphere[16] = vec3[]("+
"vec3(-8.1992004E-4, -0.009717422, 0.009688608),"+
"vec3(-0.042971052, -0.06851137, 0.005827546),"+
"vec3(-0.03565987, -0.06629154, 0.06898691),"+
"vec3(-0.0070870486, -0.059869073, 0.053567607),"+
"vec3(0.0034478558, -0.056447484, 0.028165549),"+
"vec3(-0.0827804, 0.0645576, 0.100367226),"+
"vec3(0.010138296, -0.0027681806, 0.003814322),"+
"vec3(-0.1396061, 0.115028, 0.011840784),"+
"vec3(0.0058024586, -0.025698185, 0.017740997),"+
"vec3(0.042246014, 0.21076058, 0.18665563),"+
"vec3(-0.10744259, -0.048058167, 0.10266774),"+
"vec3(-0.023046503, 0.11854526, 0.10097304),"+
"vec3(0.54443914, -0.09234401, 0.015788728),"+
"vec3(-0.070081554, 0.044382483, 0.041525442),"+
"vec3(-0.13810638, 0.08653843, 0.42867056),"+
"vec3(0.20787776, -0.16582203, 0.35640347)"+
");"+

" int samples = 16;"+
" float samplesTaken = 16.0;"+

"   vec2 noiseScale = vec2(1920.0/3.0, 1080.0/3.0);"+
//"   noiseScale = vec2(1.0,1.0);"+

" 	vec3 n = normalize(normalTransformMatrix*normalize(texture(sceneNormals, sampleC).rgb));"+

" if (n.x >= 99.0) {"+
" shadowAmount = vec4(0.0,0.0,0.0,1.0);"+
" }"+
" else {"+





"   vec3 noiseVec = (texture(noise, noiseScale*sampleC).rgb);"+
//"   vec3 noiseVec = 2.0*(texture(noise, noiseScale*sampleC).rgb) - vec3(1.0,1.0,1.0);"+

"vec3 tangent   = normalize(noiseVec - n * dot(noiseVec, n));"+
"vec3 bitangent = cross(n, tangent);"+
"mat3 TBN       = mat3(tangent, bitangent, n);"+



" vec3 cameraSpacePosition = vRay*texture(scenePositions, sampleC).r;"+

" float radius = 1.0;"+


" radius = 0.9494024045025050;"+
" float bias = 0.0010952;"+
" float ambient = 0.0;"+







" for (int u = 0; u < samples; u++) {"+


//"   vec3 aSample = TBN * accelHemisphere[u];"+
"  vec3 aSample = reflect((sphereSamples[u]), normalize(noiseVec) );"+                //

"  float sampleVecSign = 1.0;"+
" if (dot(n,normalize(aSample)) >= -0.10) {"+
"       sampleVecSign = 1.0;"+
" }"+
" else {"+
"    sampleVecSign = -1.0;"+
" }"+

"   aSample = cameraSpacePosition + sampleVecSign*radius*aSample;"+
//"   aSample = cameraSpacePosition + radius*aSample;"+

"   vec4 sampleV = vec4(aSample, 1.0);"+
"   sampleV = projMatrix*sampleV;"+
"   vec3 sampleVec = sampleV.xyz/sampleV.w;"+
"   sampleVec = 0.5*sampleVec + vec3(0.5,0.5,0.5);"+

//" 	float sampleDepth =  (getVRay()*texture(scenePositions, sampleVec.xy).r).z;"+
"vec3 samplePosition = vRay*texture(scenePositions, sampleVec.xy).r;"+
" float sampleDepth = (samplePosition).z;"+

//"	ambient = ambient + ( sampleDepth >= aSample.z + bias ? 1.0 : 0.0);"+  

"float rangeCheck = smoothstep(0.0, 1.0, 1.0*radius / pow(abs(cameraSpacePosition.z - sampleDepth),1.0)  );"+

" if (cameraSpacePosition.z - sampleDepth > 0.0) {"+
//"   rangeCheck = 1.0;"+
" }"+


" float rangeCheckTHold = 0.001;"+
" if (rangeCheck < rangeCheckTHold) {"+
//"   samplesTaken = samplesTaken - (1.0);"+
//" ambient += ( 1.0 - rangeCheck);"+
" }"+

" if ( sampleVecSign*dot(n,normalize(reflect(normalize(sphereSamples[u]), normalize(noiseVec))) ) >= -0.100) {"+               //
//" if (abs( dot( normalize(TBN* accelHemisphere[u]), normalize(n) ) ) >= 0.001) {"+                        //

" 		ambient = ambient  +  ( (sampleDepth >= aSample.z + bias ) ? 1.0 : 0.0) * rangeCheck;  "+

" }"+
" else {"+
" samplesTaken = samplesTaken - 1.0;"+
//" ambient = ambient + 1.0;"+
" }"+

//" ambient = ambient + (aSample.z - sampleDepth < 0.0 ? 1.0 : 0.0);"+
" }"+

" ambient = 1.0 - (ambient/samplesTaken);"+
//" ambient = 1.0;"+
"   shadowAmount = vec4(       clamp(0.0 + 1.0*pow(ambient,1.4520),  0.0,1.0),               0.0, 0.0, 1.0);"+
" }"+

"   } ";


shadowsProgramID = glCreateProgram();
glUseProgram(shadowsProgramID);
if (shadowsProgramID == 0) {
throw new Exception("Could not create Shader");
}

shadowsVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (shadowsVShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}
glShaderSource(shadowsVShaderID, vShaderShadows);
glCompileShader(shadowsVShaderID );
if (glGetShaderi(shadowsVShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shadowsVShaderID, 1024));
}
glAttachShader(shadowsProgramID, shadowsVShaderID);

shadowsFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (shadowsFShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(shadowsFShaderID, fShaderShadows);
glCompileShader(shadowsFShaderID);

if (glGetShaderi(shadowsFShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shadowsFShaderID, 1024));
}
glAttachShader(shadowsProgramID, shadowsFShaderID);

glLinkProgram(shadowsProgramID);
if (glGetProgrami(shadowsProgramID, GL_LINK_STATUS) == 0) {
throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(shadowsProgramID, 1024));
}
if (shadowsVShaderID != 0) {
glDetachShader(shadowsProgramID, shadowsVShaderID);
}
if (shadowsFShaderID != 0) {
glDetachShader(shadowsProgramID, shadowsFShaderID);
}

glValidateProgram(shadowsProgramID);
if (glGetProgrami(shadowsProgramID, GL_VALIDATE_STATUS) == 0) {
System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(shadowsProgramID, 1024));
}

glUseProgram(shadowsProgramID);



shadowsSceneNormalsSamplerLocation = glGetUniformLocation(shadowsProgramID,"sceneNormals");
shadowsScenePositionsSamplerLocation = glGetUniformLocation(shadowsProgramID,"scenePositions");
shadowsNoiseSamplerLocation = glGetUniformLocation(shadowsProgramID,"noise");

//shadowProjVMatrixLocation = glGetUniformLocation(shadowsProgramID,"projVMatrix");
shadowProjMatrixLocation = glGetUniformLocation(shadowsProgramID,"projMatrix");

shadowInvProjMatrixLocation = glGetUniformLocation(shadowsProgramID, "invProjMatrix");
shadowNormalTransformMatrixLocation = glGetUniformLocation(shadowsProgramID,"normalTransformMatrix");

shadowSphereSamplesLocation = glGetUniformLocation(shadowsProgramID,"sphereSamples");















int testN = 2;
int testV = 26;


System.out.println("There are " + glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS) + " image units available on this device");

// Light culling program



String lightingShader = 
"#version 430\n"+

//"#define WORK_GROUP_SIZE 16"+

"uniform int screenWidth;"+
"uniform int screenHeight;"+

"uniform float roll;"+
"uniform int lightsSize;"+
"uniform sampler2D depth;"+
"uniform sampler2D normals;"+

// Frustum position order
// 1...4
// .....
// 2...3

"uniform vec3 frustumCornerPositions[4];"+
"uniform mat4 projectionM;"+
"uniform mat4 cameraM;"+


"shared uint mnDepth;"+
"shared uint mxDepth;"+

// Light List Format: vec3 position, vec3 color, float strength, float radius, float castsShadows
 "layout (std430, binding=2) buffer theLights"+
 "{ "+
 "  float lights[];"+
 "};"+
 
"layout(local_size_x = 16, local_size_y = 16) in;"+
"layout(rgba32f, binding = 0) uniform image2D pointLightsColor;"+ 

 "void main() {"+
 
 " int workGroupSize = 16;"+
 " float lightCount = 0.0;"+
 
 " if (gl_LocalInvocationID.xy == vec2(0,0)) {"+
 	"mnDepth = 0xffffffffu;"+
 	"mxDepth = 0;"+
"}"+
  " barrier();"+
 "ivec2 storePos = ivec2(gl_GlobalInvocationID.xy);"+
 "    vec2 samplePos = gl_GlobalInvocationID.xy / vec2(float(screenWidth) - 1.0, float(screenHeight) - 1.0); "+
"     float localCoef = length(vec2(ivec2(gl_LocalInvocationID.xy)-8)/8.0);"+
"     float globalCoef = sin(float(gl_WorkGroupID.x+gl_WorkGroupID.y)*0.1 + roll)*0.5;"+

// screenPosN in [0.0, 1.0] for x and y
" float tN = 2.0*4.0;"+
"vec2 screenPosN = vec2(gl_GlobalInvocationID.xy)/vec2((float(screenWidth) - 1.0), (float(screenHeight) - 1.0));"+
" float floatDepth = texture(depth,samplePos).r;"+
" vec3 vRay = vec3( (1.0 - screenPosN.x)*frustumCornerPositions[0].x + screenPosN.x*frustumCornerPositions[3].x,"+
"                   (1.0 - screenPosN.y)*frustumCornerPositions[1].y + screenPosN.y*frustumCornerPositions[0].y,"+
"                   frustumCornerPositions[0].z );"+
" vec3 vSpacePosition = vRay*vec3( (2.0*screenPosN - vec2(1.0,1.0)).xy, -1.0*floatDepth );"+
//" vec3 

" uint intDepthZ = uint(floatDepth*0xffffffffu);"+
" atomicMax(mxDepth,intDepthZ);"+
" atomicMin(mnDepth,intDepthZ);"+

" barrier();"+
" float mxDepthF = float(float(mxDepth)/float(0xffffffffu));"+
" float mnDepthF = float(float(mnDepth)/float(0xffffffffu));"+

" barrier();"+
"vec2 tileScale = vec2(float(screenWidth),float(screenHeight)) * (1.0/(float(2*workGroupSize)));"+
"vec2 tileBias = tileScale - vec2(gl_WorkGroupID.xy);"+
" vec4 c1 = vec4(-projectionM[0][0]*tileScale.x, projectionM[0][1], tileBias.x, projectionM[0][3]);"+
"vec4 c2 = vec4(projectionM[1][0], -projectionM[1][1]*tileScale.y, tileBias.y, projectionM[1][3]);"+
"vec4 c4 = vec4(projectionM[3][0],projectionM[3][1],-1.0,projectionM[3][3]);"+

"vec4 frustumPlanes[6];"+

//Right plane
"frustumPlanes[0] = c4 - c1;"+
//Left plane
"frustumPlanes[1] = c4 + c1;"+
//Top plane
"frustumPlanes[2] = c4 - c2;"+
//Bottom plane
"frustumPlanes[3] = c4 + c2;"+
//Near plane
"frustumPlanes[4] = vec4(0.0,0.0,-1.0,-mnDepthF);"+
//Far plane
"frustumPlanes[5] = vec4(0.0,0.0,-1.0,mxDepthF);"+

"for (int k = 0; k < 4; k++) {"+
" frustumPlanes[k] = frustumPlanes[k] * (1.0) / length(frustumPlanes[k].xyz); "+
"}"+

"vec4 lightPosition =  cameraM*vec4(lights[0],lights[1],lights[2],1.0);"+ 
" float lightRadius = lights[7];"+

" barrier();"+
" bool inFrustum = true;"+

" for (int k = 3; k >= 0 && inFrustum; k--) {"+
"    float dist = dot(frustumPlanes[k], lightPosition);"+
" 	 inFrustum = (-lights[7] <= dist);"+
"}"+

" if (lightPosition.z + lightRadius < -1000.0*mxDepthF  ||  lightPosition.z - lightRadius > -1000.0*mnDepthF) {"+
"       inFrustum = false;"+
" }"+
//" barrier();"+

" if ( inFrustum) {"+
"         lightCount = lightCount + 1.0;" + 

" }"+
" if (abs( ( cameraM*vec4(lights[0],lights[1],lights[2],1.0) ).z - vSpacePosition.z) <= lights[7]) {"+
//"                  lightCount = lightCount + 1.0;"+
" }"+
//"     imageStore(destTex, storePos, vec4(1.0-globalCoef*localCoef, 0.0, 0.0, 0.0));"+
//"   imageStore(pointLightsColor, storePos, vec4(0.40,0.0,1.0,1.0));"+
//"   imageStore(pointLightsColor, storePos, vec4(float(gl_WorkGroupID.x)/120.0,0.0,0.0,1.0 ) );"+
//"   imageStore(pointLightsColor, storePos, vec4(lights[3], 0.0, 0.0, 1.0) );"+
//"	imageStore(pointLightsColor, storePos, vec4(texture(depth, storePos2).r, 0.0, 0.0, 1.0) );"+
//"	imageStore(pointLightsColor, storePos, vec4(texture(normals, storePos2).r, 0.0, 0.0, 1.0) );"+
//"	imageStore(pointLightsColor, storePos, vec4(mxDepthF, 0.0, 0.0, 1.0) );"+
//"	imageStore(pointLightsColor, storePos, vec4(0.5*(vSpacePosition.y/(frustumCornerPositions[3].y)) + 0.5, 0.0, 0.0, 1.0) );"+
"	imageStore(pointLightsColor, storePos, vec4(lightCount, 0.0, 0.0, 1.0) );"+

"}";

lightingProgramID = glCreateProgram();
glUseProgram(lightingProgramID);

if (lightingProgramID == 0) {
throw new Exception("Could not create Shader");
}

lightingShaderID = glCreateShader(GL_COMPUTE_SHADER);
if (lightingShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_COMPUTE_SHADER);
}
glShaderSource(lightingShaderID, lightingShader);
glCompileShader(lightingShaderID );
if (glGetShaderi(lightingShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(lightingShaderID, 1024));
}
glUseProgram(lightingProgramID);

glAttachShader(lightingProgramID, lightingShaderID);
glLinkProgram(lightingProgramID);

if (glGetProgrami(lightingProgramID, GL_LINK_STATUS) == 0) {
throw new Exception("Error linking Shader code: " + glGetShaderInfoLog(lightingProgramID, 1024));
}




// *****************************
// Create list of point lights 
// **************************

lightsSSBO = glGenBuffers();
glBindBuffer(GL_SHADER_STORAGE_BUFFER,lightsSSBO);

int pointLightCount = 1;
PointLight testLight1 = new PointLight(new Vector3f(4.90f,5.0f,-4.0f), new Vector3f(0.72f,0.2f,0.5f), 2.0f, 15.150f);
//float[] testLightsList = new float[] {0.20f,-17.0f,-17.0f,   0.7f,0.2f,0.5f,  1.0f,  2.150f,  1.0f};

float[] testLightsList = new float[9*pointLightCount];

for (int c = 0; c < 1; c++) {
	
	// Set position of light
	testLightsList[9*c + 0] = testLight1.getLocation().x;
	testLightsList[9*c + 1] = testLight1.getLocation().y;
	testLightsList[9*c + 2] = testLight1.getLocation().z;
	
	// Set color of light
	testLightsList[9*c + 3] = testLight1.getColor().x;
	testLightsList[9*c + 4] = testLight1.getColor().y;
	testLightsList[9*c + 5] = testLight1.getColor().z;
	
	// Set strength of light
	testLightsList[9*c + 6] = testLight1.getStrength();
	
	// Set radius of light
	testLightsList[9*c + 7] = testLight1.getRadius();
	
	// Set point light shadows status of light
	testLightsList[9*c + 8] = 1.0f;
	
}

FloatBuffer testLights = BufferUtils.createFloatBuffer(9);
testLights.put(testLightsList);
testLights.flip();

glBufferData(GL_SHADER_STORAGE_BUFFER,testLights,GL_DYNAMIC_COPY);
glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, lightsSSBO);
glBindBuffer(GL_SHADER_STORAGE_BUFFER,0);


lightsSSBOIndex = glGetProgramResourceIndex(lightingProgramID,GL_SHADER_STORAGE_BLOCK, "theLights");

lightingProgramLightsSizeLocation = glGetUniformLocation(lightingProgramID,"lightsSize");
lightingProgramProjectionMLocation = glGetUniformLocation(lightingProgramID,"projectionM");
lightingProgramCameraMLocation = glGetUniformLocation(lightingProgramID,"cameraM");

lightingProgramDepthLocation = glGetUniformLocation(lightingProgramID,"depth");
lightingProgramNormalsLocation = glGetUniformLocation(lightingProgramID,"normals");

lightingProgramFrustumCornerPositionsLocation = glGetUniformLocation(lightingProgramID,"frustumCornerPositions");



lightingProgramScreenWidthLocation = glGetUniformLocation(lightingProgramID,"screenWidth");
lightingProgramScreenHeightLocation = glGetUniformLocation(lightingProgramID,"screenHeight");











//        
// Render test scene, an image of variable size and position to the screen
//




String vShaderTestScene = "#version 430\n" +
" layout (location = 0) in vec3 vPosition;\n" +
" layout (location = 1) in vec2 tCoord;\n" +

" out vec2 sampleCoord;\n" +

" uniform mat4 positionAndSize;"+

" void main() {\n" +
"  sampleCoord = tCoord;\n" +
" gl_Position = vec4(1.4*vPosition + vec3(-0.2,0.6,0.0),1.0);\n" +
" } ";

String fShaderTestScene = "#version 430\n" +

" in vec2 sampleCoord;\n" +
" out vec4 fragColor;"+

" uniform sampler2D scene;"+


" void main() {\n" +

" fragColor = vec4(1.0*texture(scene,sampleCoord).r, 0.0, 0.0, 1.0);"+
//" fragColor = vec4(1.0,1.0,0.0,1.0);"+

"   } ";



    



testSceneProgramID = glCreateProgram();

glUseProgram(testSceneProgramID);

if (testSceneProgramID == 0) {
throw new Exception("Could not create Shader");
}


testSceneVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (testSceneVShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}

glShaderSource(testSceneVShaderID, vShaderTestScene);
glCompileShader(testSceneVShaderID );

if (glGetShaderi(testSceneVShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(testSceneVShaderID, 1024));
}

glAttachShader(testSceneProgramID, testSceneVShaderID);






testSceneFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (testSceneFShaderID == 0) {
throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(testSceneFShaderID, fShaderTestScene);
glCompileShader(testSceneFShaderID);

if (glGetShaderi(testSceneFShaderID, GL_COMPILE_STATUS) == 0) {
throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(testSceneFShaderID, 1024));
}

glAttachShader(testSceneProgramID, testSceneFShaderID);








glLinkProgram(testSceneProgramID);
if (glGetProgrami(testSceneProgramID, GL_LINK_STATUS) == 0) {
throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(testSceneProgramID, 1024));
}

if (testSceneVShaderID != 0) {
glDetachShader(testSceneProgramID, testSceneVShaderID);
}
if (testSceneFShaderID != 0) {
glDetachShader(testSceneProgramID, testSceneFShaderID);
}

glValidateProgram(testSceneProgramID);
if (glGetProgrami(testSceneProgramID, GL_VALIDATE_STATUS) == 0) {
System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(testSceneProgramID, 1024));
}


glUseProgram(testSceneProgramID);

testSceneSceneLocation = glGetUniformLocation(testSceneProgramID,"scene");



        
        
        
        
        
        
        
        
        
        // Brightpass Filtering Program
        
        

		
		String vShaderBrightPassFilter = "#version 430\n" +
" layout (location = 0) in vec3 vPosition;\n" +
" layout (location = 1) in vec2 tCoord;\n" +

" out vec2 sampleCoord;\n" +

" void main() {\n" +
"  sampleCoord = tCoord;\n" +
" gl_Position = vec4(vPosition,1.0);\n" +
" } ";

String fShaderBrightPassFilter = "#version 430\n" +
" in vec2 sampleCoord;\n" +
" out vec3 fragColor;"+
" uniform sampler2D scene;\n"+
	

" void main() {\n" +
" vec2 color = texture(scene,sampleCoord).rg;"+
" fragColor = vec3(color,1.0);"+
"   } ";


	




brightPassFilterProgramID = glCreateProgram();

glUseProgram(brightPassFilterProgramID);

if (brightPassFilterProgramID == 0) {
    throw new Exception("Could not create Shader");
}


brightPassFilterVShaderID = glCreateShader(GL_VERTEX_SHADER);
if (brightPassFilterVShaderID == 0) {
    throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
}

glShaderSource(brightPassFilterVShaderID, vShaderBrightPassFilter);
glCompileShader(brightPassFilterVShaderID );

if (glGetShaderi(brightPassFilterVShaderID, GL_COMPILE_STATUS) == 0) {
    throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(brightPassFilterVShaderID, 1024));
}

glAttachShader(brightPassFilterProgramID, brightPassFilterVShaderID);






brightPassFilterFShaderID = glCreateShader(GL_FRAGMENT_SHADER);
if (brightPassFilterFShaderID == 0) {
    throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
}

glShaderSource(brightPassFilterFShaderID, fShaderBrightPassFilter);
glCompileShader(brightPassFilterFShaderID);

if (glGetShaderi(brightPassFilterFShaderID, GL_COMPILE_STATUS) == 0) {
    throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(brightPassFilterFShaderID, 1024));
}

glAttachShader(brightPassFilterProgramID, brightPassFilterFShaderID);








glLinkProgram(brightPassFilterProgramID);
if (glGetProgrami(brightPassFilterProgramID, GL_LINK_STATUS) == 0) {
    throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(brightPassFilterProgramID, 1024));
}

if (brightPassFilterVShaderID != 0) {
    glDetachShader(brightPassFilterProgramID, brightPassFilterVShaderID);
}
if (brightPassFilterFShaderID != 0) {
    glDetachShader(brightPassFilterProgramID, brightPassFilterFShaderID);
}

glValidateProgram(brightPassFilterProgramID);
if (glGetProgrami(brightPassFilterProgramID, GL_VALIDATE_STATUS) == 0) {
    System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(brightPassFilterProgramID, 1024));
}


glUseProgram(brightPassFilterProgramID);



renderToScreenQuadSceneSamplerLocation = glGetUniformLocation(brightPassFilterProgramID,"scene");

glUniform1i(renderToScreenQuadSceneSamplerLocation,0);


// A
// Initialize Global Transformation Matricies

globalMainCameraMatrix = new Matrix4f();
globalProjectionMatrix = new Matrix4f();
globalProjectionMatrix.perspective((float)Math.PI/3.0f, (float)screenWidth/(float)screenHeight , (float)nearPlaneZ, (float)farPlaneZ);
globalInvProjectionMatrix = new Matrix4f();
globalProjectionMatrix.invert(globalInvProjectionMatrix);




				this.testSlider1 = new Slider(1920, 1080, 0.0f, 0.0f, -1.0f, 1.0f,  0.15f,0.03f,        0.02f, 0.03f,   window);
											 //windSize      basePos     endValues    bWidth&Height    sWidth&Height
												

		}

		
		
		
		
		/*
		 *   Menu System
		 *   
		 *      
		 */  
		             
		private void loop() throws Exception {
			// This line is critical for LWJGL's interoperation with GLFW's
			// OpenGL context, or any context that is managed externally.
			// LWJGL detects the context that is current in the current thread,
			// creates the GLCapabilities instance and makes the OpenGL
			// bindings available for use.
			//GL.createCapabilities();

		//	GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		//	glViewport(0,0,800,600);
			// Set the clear color
			
			
			glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
			glViewport(0,0,1920,1080);
			


/*
glMatrixMode(GL_PROJECTION);
glLoadIdentity(); // Resets any previous projection matrices
glOrtho(0, 800, 600, 0, 1, -1);
glMatrixMode(GL_MODELVIEW);
*/
 

	//player.animate();

		//	int programID = glCreateProgram();
			// Run the rendering loop until the user has attempted to close
			// the window or has pressed the ESCAPE key.
			while ( !glfwWindowShouldClose(window) ) {
			
				
				if (vSync) {
					glfwSwapInterval(1);
					}
					else {
						glfwSwapInterval(0);
					}
				
				
			if ( sequence == "Level" ) {
		
				double startTime = System.nanoTime();
				
				
				double taskBegin = System.nanoTime();
				
				glBindFramebuffer(GL_FRAMEBUFFER,shadowMap1FBO);
				glClearColor(1.0f,1.0f,1.0f,1.0f);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				
				glBindFramebuffer(GL_FRAMEBUFFER,0);
				glClearColor(screenBrightness*0.4f,0.0f,0.0f,0.0f);
				
				
				//glEnable(GL_MULTISAMPLE);
				glDisable(GL_MULTISAMPLE);
  
  
  
				
				
				/*******************
				 **** MECHANICS  ***
				 *******************/
				
  
  				lake1.update();
  				
				zone.updateWorld(mainCamera);
				

				globalMainCameraMatrix.lookAt((float)mainCamera.getEyeX(),(float)mainCamera.getEyeY(),(float)mainCamera.getEyeZ(), (float)mainCamera.getVPointX(), (float)mainCamera.getVPointY(), (float)mainCamera.getVPointZ(), 0.0f,1.0f,0.0f );
				
	
				
				/*******************
				 **** RENDERING  ***
				 *******************/
				
				
				double b4RenderTime = System.nanoTime();
				glEnable(GL_DEPTH_TEST);
				//glEnableDepthTest();

				//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
				// Determine  the appropriate light space transformation for shadowing


				sunDirection = new Vector3f(-0.8f,-0.885f,1.2f);
				sunDirection.normalize();
				
				
				Vector3f sunDirectionForShadowMaps = new Vector3f();
				if (shouldTestSearchRadius == 1) {
					sunDirectionForShadowMaps.set(sunDirection.x, 1.0f*sunDirection.y, sunDirection.z);					
				}
				else {
					sunDirectionForShadowMaps.set(sunDirection.x, 1.0f*sunDirection.y, sunDirection.z);					
				}
				
				
				
				sunDirectionForShadowMaps.normalize();
				
				
				
				
				/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
				 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
				 * CONSTRUCT FRUSTUM CASCADES FOR CASCADED SHADOW MAPPING  *
				 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
				 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
				
				// TODO
				// CLEAN UP THIS SECTION / CONSOLIDATE INTO A FOR LOOP
				
				ArrayList<double[]> frustums = new ArrayList<double[]>();
				frustums.add(new double[] {0.001, 4.0});
				frustums.add(new double[] {4.0, 8.0});
				
				ArrayList<Vector4f> corners = new ArrayList<Vector4f>();
				
				// Near Plane
				
				
				corners.add(new Vector4f(-1.0f,1.0f,-1.0f,1.0f)); // Top Left
				corners.add(new Vector4f(-1.0f,-1.0f,-1.0f,1.0f)); // Bottom Left
				corners.add(new Vector4f(1.0f,1.0f,-1.0f,1.0f)); // Top Right
				corners.add(new Vector4f(1.0f,-1.0f,-1.0f,1.0f)); // Bottom Right
				
				// Far Plane
				
				corners.add(new Vector4f(-1.0f,1.0f,1.0f,1.0f)); // Top Left
				corners.add(new Vector4f(-1.0f,-1.0f,1.0f,1.0f)); // Bottom Left
				corners.add(new Vector4f(1.0f,1.0f,1.0f,1.0f)); // Top Right
				corners.add(new Vector4f(1.0f,-1.0f,1.0f,1.0f)); // Bottom Right
				
				
				
				// Center of frustum
				
				Vector4f center = new Vector4f(0.0f,0.0f,0.0f,0.0f);
				
				
				center.div(8,center);
				

				// View Matrix 
				
				Matrix4f vMatrix1 = new Matrix4f();
				//Matrix4f vMatrix1 = (new Matrix4f()).translation(new Vector3f(-1.0f*(float)mainCamera.getEyeX(),-1.0f*(float)mainCamera.getEyeY(),-1.0f*(float)mainCamera.getEyeZ()));
				vMatrix1.lookAt((float)mainCamera.getEyeX(),(float)mainCamera.getEyeY(),(float)mainCamera.getEyeZ(), (float)mainCamera.getVPointX(), (float)mainCamera.getVPointY(), (float)mainCamera.getVPointZ(), 0.0f,1.0f,0.0f );
				
				// Projection Matrix 
				
				Matrix4f pMatrix1 = (new Matrix4f()).setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(28.0),(float)(54.0));
				
				// View Projection Matrix
				
				Matrix4f vPMatrix1 = new Matrix4f();
				pMatrix1.mul(vMatrix1,vPMatrix1);
				
				// Inverse View Projection Matrix
				
				vPMatrix1.invert(vPMatrix1);
				
				// Corners of perspective frustum in world space
				
				
				 maxZ = -Float.MAX_VALUE;
				 minZ = Float.MAX_VALUE;
				
				ArrayList<Vector4f> corners1 = new ArrayList<Vector4f>();
				for (int q = 0; q < corners.size(); q++) {
					
					Vector4f transformedVec = new Vector4f();
					vPMatrix1.transform(corners.get(q),transformedVec);
					corners1.add(transformedVec);
					corners1.get(q).set(corners1.get(q).x/corners1.get(q).w, corners1.get(q).y/corners1.get(q).w, corners1.get(q).z/corners1.get(q).w, corners1.get(q).w/corners1.get

(q).w);
					
					maxZ = Math.max(maxZ, -1.0f*corners1.get(q).z);
					minZ = Math.min(minZ, -1.0f*corners1.get(q).z);
					
				}
				
			  
				
				Vector4f center1 = new Vector4f(0.0f,0.0f,0.0f,0.0f);
				
					for (int q = 0; q < corners1.size(); q++) {
						center1.add(corners1.get(q),center1);
					}
					
					center1.div(8,center1);
			//	System.out.println("Cascade 3 has center of " + center1);
				
				
				
				
				
	
				// Light View Matrix 1
				
				Matrix4f lightView1 = new Matrix4f();
				
			
				Vector3f frustumCenter1 = new Vector3f(center1.x,center1.y,center1.z);
				//System.out.println(" Frustum Center 1 is " + frustumCenter1);
			//	Vector3f sunPosition1 = new Vector3f();
				//System.out.println(" Sun Direction is " + sunDirectionForShadowMaps);
				
			//	System.out.println(" Z Distance is " + (maxZ - minZ));
				
			//	sunDirectionForShadowMaps.mul(-1.0f*(maxZ - minZ),sunPosition1);
			//	System.out.println("1......Adjusted Sun Position 1 is " + sunPosition1);
				
			//	sunPosition1.add(frustumCenter1,sunPosition1);
		//		System.out.println("2.......Adjusted Sun Position 1 is " + sunPosition1);
				
				
				
			//	lightView1.lookAt(sunPosition1.x,sunPosition1.y,sunPosition1.z,frustumCenter1.x,frustumCenter1.y,frustumCenter1.z,0.0f,1.0f,0.0f);
				lightView1.setLookAt(0.0f, 0.0f, 0.0f, sunDirectionForShadowMaps.x, sunDirectionForShadowMaps.y, sunDirectionForShadowMaps.z, 0.0f, 1.0f, 0.0f);
	
				Matrix3f normalLightTransformMatrix = new Matrix3f();
				lightView1.get3x3(normalLightTransformMatrix);
	
				normalLightTransformMatrix.invert(normalLightTransformMatrix);
				normalLightTransformMatrix.transpose(normalLightTransformMatrix);
		
				Vector3f sunDirectionForShadowMaps1 = new Vector3f();
				sunDirectionForShadowMaps1.set(sunDirectionForShadowMaps);
				
				Vector3f sunPos1 = new Vector3f(0.0f,0.0f,0.0f);
				sunPos1.add(frustumCenter1,sunPos1);
				sunPos1.add(sunDirectionForShadowMaps1.mul(-1.0f*(maxZ - minZ  + 10.0f )),sunPos1);
			//	System.out.println("ZVals are  " + maxZ + " and " + minZ);
			//	System.out.println("SunPos1 is " + sunPos1);
		
				
			//	lightView1.setLookAt(sunPos1.x, sunPos1.y, sunPos1.z,frustumCenter1.x,frustumCenter1.y,frustumCenter1.z,0.0f,1.0f,0.0f);	

				
				Matrix4f lightMatrix1 = new Matrix4f();
				
				
				// Light Projection Matrix 1
				
				Matrix4f lightProjection1 = new Matrix4f();
				
				maxX = -Float.MAX_VALUE;
				minX = Float.MAX_VALUE;
				maxY = -Float.MAX_VALUE;
				minY = Float.MAX_VALUE;
				maxZ = -Float.MAX_VALUE;
				minZ = Float.MAX_VALUE;
				
				for (int q = 0; q < corners1.size(); q++) {
				
					Vector4f corner = corners1.get(q);
					//corner.mul(lightView1);
				//	Matrix4f invertedLightView = lightView1.invert();
			        lightView1.transform(corner,corner);
					
			        minX = Math.min(corner.x, minX);
			        maxX = Math.max(corner.x, maxX);
			        minY = Math.min(corner.y, minY);
			        maxY = Math.max(corner.y, maxY);
			        minZ = Math.min(-1.0f*corner.z, minZ);
			        maxZ = Math.max(-1.0f*corner.z, maxZ);
				}
				
				Vector3f lightFrustumCenter1 = new Vector3f((maxX + minX)/2.0f,(maxY + minY)/2.0f,(maxZ + minZ)/2.0f);
				float mRange = Math.max(maxX - minX, maxY - minY)*0.5f;
				Vector3f lightFrustumCenterOriginal1 = new Vector3f();
				lightFrustumCenterOriginal1.set(lightFrustumCenter1);
			
				
				float g = 1.6f;
				float padding = 0.0f;
		
				

				float sMap1SizeRatio = 1.0f;
				lightFrustumCenter1.set(this.snap(lightFrustumCenter1.x,(2.0f)*mRange/( ((float)shadowMap1Size)/sMap1SizeRatio) ), this.snap(lightFrustumCenter1.y,(2.0f)*mRange/(((float)

shadowMap1Size)/sMap1SizeRatio)) , lightFrustumCenter1.z);
				//		lightProjection2.setOrtho(this.snap(minX,(2.0f)*mRange/(2048.0f)),this.snap(maxX,(2.0f)*mRange/(2048.0f)),this.snap(minY,(2.0f)*mRange/(2048.0f)),this.snap

//(maxY,(2.0f)*mRange/(2048.0f)), minZ - 26.2f, maxZ );
					lightProjection1.setOrtho(lightFrustumCenter1.x - mRange,lightFrustumCenter1.x + mRange,lightFrustumCenter1.y - mRange,lightFrustumCenter1.y + mRange, minZ - 70.0f, 

maxZ );
					
	//		System.out.println("It would be " + minZ + " and " + maxZ);
				//lightProjection1.setOrtho((float)(Math.floor((minX )*g)/g) - padding,(float)(Math.floor((maxX )*g)/g) + padding,(float)(Math.floor((minY )*g)/g) - padding,(float)

//(Math.floor((maxY )*g)/g) + padding, 0.0f, (maxZ - minZ) + 8.0f);
				lightMatrix1.set(lightProjection1);
				lightMatrix1.mul(lightView1,lightMatrix1);
				


				float mRange1 = mRange;
				
				
				
				
				
				float mnZ1 = minZ;
				float mxZ1 = maxZ;
								
				
				
				
				
				
				
				
				
				
				
				

				// View Matrix 
				
				Matrix4f vMatrix2 = new Matrix4f();
				//Matrix4f vMatrix2 = (new Matrix4f()).translation(new Vector3f(-1.0f*(float)mainCamera.getEyeX(),-1.0f*(float)mainCamera.getEyeY(),-1.0f*(float)mainCamera.getEyeZ()));
				vMatrix2.lookAt((float)mainCamera.getEyeX(),(float)mainCamera.getEyeY(),(float)mainCamera.getEyeZ(), (float)mainCamera.getVPointX(), (float)mainCamera.getVPointY(), (float)mainCamera.getVPointZ(), 0.0f,1.0f,0.0f );

				// Projection Matrix 
				
				
				Matrix4f pMatrix2 = (new Matrix4f()).setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(18.0),(float)(28.0));
				
				// View Projection Matrix
				
				Matrix4f vPMatrix2 = new Matrix4f();
				pMatrix2.mul(vMatrix2,vPMatrix2);
				
				// Inverse View Projection Matrix
				
				vPMatrix2.invert(vPMatrix2);
				
				// Corners of perspective frustum in world space
				
				
				 maxZ = -Float.MAX_VALUE;
				 minZ = Float.MAX_VALUE;
				
				ArrayList<Vector4f> corners2 = new ArrayList<Vector4f>();
				for (int q = 0; q < corners.size(); q++) {
					
					Vector4f transformedVec = new Vector4f();
					vPMatrix2.transform(corners.get(q),transformedVec);
					corners2.add(transformedVec);
					corners2.get(q).set(corners2.get(q).x/corners2.get(q).w, corners2.get(q).y/corners2.get(q).w, corners2.get(q).z/corners2.get(q).w, corners2.get(q).w/corners2.get

(q).w);
					
					maxZ = Math.max(maxZ, -1.0f*corners2.get(q).z);
					minZ = Math.min(minZ, -1.0f*corners2.get(q).z);
					
				}
				
			  
				
				Vector4f center2 = new Vector4f(0.0f,0.0f,0.0f,0.0f);
				
					for (int q = 0; q < corners2.size(); q++) {
						center2.add(corners2.get(q),center2);
					}
					
					center2.div(8,center2);
			//	System.out.println("Cascade 3 has center of " + center2);
				
				
				
				
				
	
				// Light View Matrix 1
				
				Matrix4f lightView2 = new Matrix4f();
				
			
				Vector3f frustumCenter2 = new Vector3f(center2.x,center2.y,center2.z);
				Vector3f sunDirectionForShadowMaps2 = new Vector3f();
				sunDirectionForShadowMaps2.set(sunDirectionForShadowMaps);
				
				Vector3f sunPos2 = new Vector3f(0.0f,0.0f,0.0f);
				sunPos2.add(frustumCenter2,sunPos2);
				sunPos2.add(sunDirectionForShadowMaps2.mul(-1.0f*(maxZ - minZ  + 16.0f)),sunPos2);
		//		System.out.println("ZVals are  " + maxZ + " and " + minZ);
		//		System.out.println("SunPos2 is " + sunPos2);
		
				
				//lightView2.setLookAt(sunPos2.x, sunPos2.y, sunPos2.z,frustumCenter2.x,frustumCenter2.y,frustumCenter2.z,0.0f,1.0f,0.0f);	

				
			//	lightView2.lookAt(sunPosition1.x,sunPosition1.y,sunPosition1.z,frustumCenter2.x,frustumCenter2.y,frustumCenter2.z,0.0f,1.0f,0.0f);
			lightView2.setLookAt(0.0f, 0.0f, 0.0f, sunDirectionForShadowMaps.x, sunDirectionForShadowMaps.y, sunDirectionForShadowMaps.z, 0.0f, 1.0f, 0.0f);
				Matrix4f lightMatrix2 = new Matrix4f();
				
				
				// Light Projection Matrix 1
				
				Matrix4f lightProjection2 = new Matrix4f();
				
				maxX = -Float.MAX_VALUE;
				minX = Float.MAX_VALUE;
				maxY = -Float.MAX_VALUE;
				minY = Float.MAX_VALUE;
				maxZ = -Float.MAX_VALUE;
				minZ = Float.MAX_VALUE;
			
				for (int q = 0; q < corners2.size(); q++) {
				
					Vector4f corner = corners2.get(q);
					//corner.mul(lightView2);
				//	Matrix4f invertedLightView = lightView2.invert();
			        lightView2.transform(corner,corner);
					
			        minX = Math.min(corner.x, minX);
			        maxX = Math.max(corner.x, maxX);
			        minY = Math.min(corner.y, minY);
			        maxY = Math.max(corner.y, maxY);
			        minZ = Math.min(-1.0f*corner.z, minZ);
			        maxZ = Math.max(-1.0f*corner.z, maxZ);
				
			    }
				
					Vector3f lightFrustumCenter2 = new Vector3f((maxX + minX)/2.0f,(maxY + minY)/2.0f,(maxZ + minZ)/2.0f);
			
					Vector3f lightFrustumCenterOriginal2 = new Vector3f();
					lightFrustumCenterOriginal2.set(lightFrustumCenter2);
				
				mRange = Math.max(maxX - minX, maxY - minY)*0.5f;
				
				
				
				if (!startRange) {
					if ( Math.abs(mRange - prevMRange2) >= 0.00001) {
						System.out.println("MRANGE IS " + mRange);
						System.out.println("PREVMRANGE IS " + prevMRange2);
				//		System.exit(0);
					}
				}
				startRange = false;
				prevMRange2 = mRange;
				
				
				
				//Vector3f lightFrustumCenterAnimated = new Vector3f();
				//lightFrustumCenterAnimated.set(lightFrustumCenter2);
					
				lightFrustumCenter2.set(this.snap(lightFrustumCenter2.x,(2.0f)*mRange/(shadowMap2Size)), this.snap(lightFrustumCenter2.y,(2.0f)*mRange/(shadowMap2Size)) , 

lightFrustumCenter2.z);
		//		lightProjection2.setOrtho(this.snap(minX,(2.0f)*mRange/(2048.0f)),this.snap(maxX,(2.0f)*mRange/(2048.0f)),this.snap(minY,(2.0f)*mRange/(2048.0f)),this.snap(maxY,(2.0f)

//*mRange/(2048.0f)), minZ - 26.2f, maxZ );
		
	//			lightFrustumCenterAnimated.set(this.snap(lightFrustumCenterAnimated.x,(2.0f)*mRange/(shadowMapAnimated3Size)), this.snap(lightFrustumCenterAnimated.y,(2.0f)*mRange/

//(shadowMapAnimated3Size)) , lightFrustumCenterAnimated.z);
						
				lightProjection2.setOrtho(lightFrustumCenter2.x - mRange,lightFrustumCenter2.x + mRange,lightFrustumCenter2.y - mRange,lightFrustumCenter2.y + mRange, minZ - 74.0f, maxZ );
		
			//	lightProjection2.setOrtho((float)(Math.floor((minX )*g2)/g2) - padding2,(float)(Math.floor((maxX )*g2)/g2) + padding2,(float)(Math.floor((minY )*g2)/g2) - padding2,(float)

//(Math.floor((maxY )*g2)/g2) + padding2,minZ - 26.2f, maxZ - 0.0f);
				lightMatrix2.set(lightProjection2);
				lightMatrix2.mul(lightView2,lightMatrix2);
				
				float mRange2 = mRange;
				
				float mnZ2 = minZ;
				float mxZ2 = maxZ;
				
				
				
				
				
				
				

				// View Matrix 
				
				Matrix4f vMatrix3 = new Matrix4f();
				//Matrix4f vMatrix3 = (new Matrix4f()).translation(new Vector3f(-1.0f*(float)mainCamera.getEyeX(),-1.0f*(float)mainCamera.getEyeY(),-1.0f*(float)mainCamera.getEyeZ()));
				vMatrix3.lookAt((float)mainCamera.getEyeX(),(float)mainCamera.getEyeY(),(float)mainCamera.getEyeZ(), (float)mainCamera.getVPointX(), (float)mainCamera.getVPointY(), (float)mainCamera.getVPointZ(), 0.0f,1.0f,0.0f );

				// Projection Matrix 
				
				Matrix4f pMatrix3 = (new Matrix4f()).setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(12.0),(float)(18.0));
				
				// View Projection Matrix
				
				Matrix4f vPMatrix3 = new Matrix4f();
				pMatrix3.mul(vMatrix3,vPMatrix3);
				
				// Inverse View Projection Matrix
				
				vPMatrix3.invert(vPMatrix3);
	
				Matrix4f vPMatrixAnimated3 = new Matrix4f();
				vPMatrixAnimated3.set(pMatrix3);
				vPMatrixAnimated3.invert(vPMatrixAnimated3); 
				
				// Corners of perspective frustum in world space
				
				
				 maxZ = -Float.MAX_VALUE;
				 minZ = Float.MAX_VALUE;
				
				 maxZ2 = -Float.MAX_VALUE;
				 minZ2 = Float.MAX_VALUE;
				
				 
		//		 System.out.println("Determining frustum 3 corners in world space");
				 ArrayList<Vector4f> corners3 = new ArrayList<Vector4f>();
				 ArrayList<Vector4f> cornersAnimated3 = new ArrayList<Vector4f>();
					
				for (int q = 0; q < corners.size(); q++) {
					
					Vector4f transformedVec = new Vector4f();
					Vector4f transformedVec2 = new Vector4f();
					
					vPMatrix3.transform(corners.get(q),transformedVec);
					vPMatrixAnimated3.transform(corners.get(q),transformedVec2);
					corners3.add(transformedVec);
					cornersAnimated3.add(transformedVec2);
					corners3.get(q).set(corners3.get(q).x/corners3.get(q).w, corners3.get(q).y/corners3.get(q).w, corners3.get(q).z/corners3.get(q).w, corners3.get(q).w/corners3.get

(q).w);
					cornersAnimated3.get(q).set(cornersAnimated3.get(q).x/cornersAnimated3.get(q).w, cornersAnimated3.get(q).y/cornersAnimated3.get(q).w, cornersAnimated3.get

(q).z/cornersAnimated3.get(q).w, cornersAnimated3.get(q).w/cornersAnimated3.get(q).w);

					//		System.out.println("Frustum3 corner in world space: " + corners3.get(q));
			//				System.out.println("Frustum3 corner in USER VIEW space: " + cornersAnimated3.get(q));
					
					maxZ = Math.max(maxZ, -1.0f*corners3.get(q).z);
					minZ = Math.min(minZ, -1.0f*corners3.get(q).z);
		
					maxZ2 = Math.max(maxZ, -1.0f*cornersAnimated3.get(q).z);
					minZ2 = Math.min(minZ, -1.0f*cornersAnimated3.get(q).z);
		
				}
				
			  
				
				Vector4f center3 = new Vector4f(0.0f,0.0f,0.0f,0.0f);
				Vector4f centerAnimated3 = new Vector4f(0.0f,0.0f,0.0f,0.0f);
				
					for (int q = 0; q < corners3.size(); q++) {
						center3.add(corners3.get(q),center3);
					}
					
					center3.div(8,center3);
			//	System.out.println("Cascade 3 has center of " + center2);
				
					for (int q = 0; q < corners3.size(); q++) {
						centerAnimated3.add(cornersAnimated3.get(q),centerAnimated3);
					}
					
					centerAnimated3.div(8,centerAnimated3);
		//		System.out.println("Animated Cascade 3 has center of " + centerAnimated3);
			
				
				
				
	
				// Light View Matrix 3
				
				
				// Light View Transform for Static Geometry
				
				Matrix4f lightView3 = new Matrix4f();
				Vector3f frustumCenter3 = new Vector3f(center3.x,center3.y,center3.z);
				Vector3f frustumCenterAnimated3 = new Vector3f(centerAnimated3.x,centerAnimated3.y,centerAnimated3.z);
				
				lightView3.setLookAt(0.0f, 0.0f, 0.0f, sunDirectionForShadowMaps.x, sunDirectionForShadowMaps.y, sunDirectionForShadowMaps.z, 0.0f, 1.0f, 0.0f);
			
				
				// Light View Transform for Animated Geometry
				
				Vector3f sunPos3 = new Vector3f(0.0f,0.0f,0.0f);
				sunDirectionForShadowMaps.mul(-1.0f*(maxZ2 - minZ2 + 4.0f),sunPos3);
				sunPos3.add(frustumCenterAnimated3,sunPos3);
				
			//	System.out.println("SUNPOS3 IS " + sunPos3);
				
				Matrix4f lightViewAnimated3 = new Matrix4f();
				lightViewAnimated3.setLookAt(sunPos3.x, sunPos3.y, sunPos3.z, frustumCenterAnimated3.x, frustumCenterAnimated3.y, frustumCenterAnimated3.z, 0.0f, 1.0f, 0.0f);
				
				Vector3f aTest = new Vector3f();
				frustumCenterAnimated3.sub(sunPos3,aTest);
				aTest.normalize(aTest);
			//	System.out.println("ATEST " + aTest);
				
				
				
				
				
				
				
				
				
				Matrix4f lightMatrix3 = new Matrix4f();
				
				
				// Light Projection Matrix 3
				
				Matrix4f lightProjection3 = new Matrix4f();
				
				maxX = -Float.MAX_VALUE;
				minX = Float.MAX_VALUE;
				maxY = -Float.MAX_VALUE;
				minY = Float.MAX_VALUE;
				maxZ = -Float.MAX_VALUE;
				minZ = Float.MAX_VALUE;
			
			
				for (int q = 0; q < corners3.size(); q++) {
				
					Vector4f corner = new Vector4f(); 
					corner.set(corners3.get(q));
			    
					
					lightView3.transform(corner,corner);
			        minX = Math.min(corner.x, minX);
			        maxX = Math.max(corner.x, maxX);
			        minY = Math.min(corner.y, minY);
			        maxY = Math.max(corner.y, maxY);
			        minZ = Math.min(-1.0f*corner.z, minZ);
			        maxZ = Math.max(-1.0f*corner.z, maxZ);
			
			        
					
//					System.out.println("ORTHOGONAL BOX ANIMATED CORNER  PRE TRANSFORM OF " + cornerAnimated);
				
			    
				}
				
				mRange = Math.max(maxX - minX, maxY - minY)*0.5f;
				
			//	float mRangeAnimated = Math.max(maxX2 - minX2,  maxY2 - minY2)*0.5f;
				
		//		System.out.println("Ortho 3 X Range is " + ( maxX - minX));
				Vector3f lightFrustumCenter3 = new Vector3f((maxX + minX)/2.0f,(maxY + minY)/2.0f,(maxZ + minZ)/2.0f);
				Vector3f lightFrustumCenterOriginal3 = new Vector3f();
				lightFrustumCenterOriginal3.set(lightFrustumCenter3);
				
		//		System.out.println("Light frustum center 3 X is " + lightFrustumCenter3.x);
			
				
				
				//Vector3f lightFrustumCenterAnimated3 = new Vector3f((maxX2 + minX2)/2.0f,(maxY2 + minY2)/2.0f,(maxZ2 + minZ2)/2.0f);
				
				float sMap3SizeRatio = 2.0f;
				lightFrustumCenter3.set(this.snap(lightFrustumCenter3.x,(2.0f)*mRange/( ((float)shadowMap3Size)/sMap3SizeRatio) ), this.snap(lightFrustumCenter3.y,(2.0f)*mRange/(((float)

shadowMap3Size)/sMap3SizeRatio)) , lightFrustumCenter3.z);
				
				g = 0.1f;
				padding = 0.0f;
				
				lightProjection3.setOrtho(lightFrustumCenter3.x - mRange,lightFrustumCenter3.x + mRange,lightFrustumCenter3.y - mRange,lightFrustumCenter3.y + mRange, minZ - 79.0f, maxZ );
				//lightProjection3.setOrtho((float)(Math.floor((minX )*g)/g) - padding,(float)(Math.floor((maxX )*g)/g) + padding,(float)(Math.floor((minY )*g)/g) - padding,(float)

//(Math.floor((maxY )*g)/g) + padding, minZ - 26.0f, maxZ );
					
				//lightMatrix3.set(lightProjection3);
				lightMatrix3.set(lightProjection3);
				
				lightMatrix3.mul(lightView3,lightMatrix3);
				
				
			//	lightFrustumCenterAnimated3.set(this.snap(lightFrustumCenterAnimated3.x,(2.0f)*mRange/( ((float)shadowMap3Size)/sMap3SizeRatio) ), this.snap(lightFrustumCenterAnimated3.y,

//(2.0f)*mRange/(((float)shadowMap3Size)/sMap3SizeRatio)) , lightFrustumCenterAnimated3.z);
				
			//	System.out.println("MINZ2 " + minZ2 + " and MAXZ2 " + maxZ2);
				
				
				
				
				float mRange3 = mRange;
			//	float mRangeAnimated3 = mRangeAnimated;
				float mnZ3 = minZ;
				float mxZ3 = maxZ;
				
				
				

		
				
				
				

				// View Matrix 
				
				Matrix4f vMatrix4 = new Matrix4f();
				//Matrix4f vMatrix4 = (new Matrix4f()).translation(new Vector3f(-1.0f*(float)mainCamera.getEyeX(),-1.0f*(float)mainCamera.getEyeY(),-1.0f*(float)mainCamera.getEyeZ()));
				vMatrix4.lookAt((float)mainCamera.getEyeX(),(float)mainCamera.getEyeY(),(float)mainCamera.getEyeZ(), (float)mainCamera.getVPointX(), (float)mainCamera.getVPointY(), (float)mainCamera.getVPointZ(), 0.0f,1.0f,0.0f );

				// Projection Matrix 
				
				Matrix4f pMatrix4 = (new Matrix4f()).setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(6.0),(float)(12.0));
				
				// View Projection Matrix
				
				Matrix4f vPMatrix4 = new Matrix4f();
				pMatrix4.mul(vMatrix4,vPMatrix4);
				
				// Inverse View Projection Matrix
				
				vPMatrix4.invert(vPMatrix4);
				
				// Corners of perspective frustum in world space
				maxZ = -Float.MAX_VALUE;
				minZ = Float.MAX_VALUE;
				
				ArrayList<Vector4f> corners4 = new ArrayList<Vector4f>();
				for (int q = 0; q < corners.size(); q++) {
					Vector4f transformedVec = new Vector4f();
					vPMatrix4.transform(corners.get(q),transformedVec);
					corners4.add(transformedVec);
					corners4.get(q).set(corners4.get(q).x/corners4.get(q).w, corners4.get(q).y/corners4.get(q).w, corners4.get(q).z/corners4.get(q).w, corners4.get(q).w/corners4.get(q).w);
					maxZ = Math.max(maxZ, -1.0f*corners4.get(q).z);
					minZ = Math.min(minZ, -1.0f*corners4.get(q).z);
				}
				
			  Vector4f center4 = new Vector4f(0.0f,0.0f,0.0f,0.0f);
				for (int q = 0; q < corners4.size(); q++) {
					center4.add(corners4.get(q),center4);
				}
					
				center4.div(8,center4);
			
				// Light View Matrix 4
				
				Matrix4f lightView4 = new Matrix4f();
				Vector3f frustumCenter4 = new Vector3f(center4.x,center4.y,center4.z);
				Vector3f sunDirectionForShadowMaps4 = new Vector3f();
				sunDirectionForShadowMaps4.set(sunDirectionForShadowMaps);
				Vector3f sunPos4 = new Vector3f(0.0f,0.0f,0.0f);
				sunPos4.add(frustumCenter4,sunPos4);
				sunPos4.add(sunDirectionForShadowMaps4.mul(-1.0f*(maxZ - minZ  + 16.0f)),sunPos4);
				lightView4.setLookAt(0.0f, 0.0f, 0.0f, sunDirectionForShadowMaps.x, sunDirectionForShadowMaps.y, sunDirectionForShadowMaps.z, 0.0f, 1.0f, 0.0f);
				Matrix4f lightMatrix4 = new Matrix4f();
				
				// Light Projection Matrix 1
				
				Matrix4f lightProjection4 = new Matrix4f();
				maxX = -Float.MAX_VALUE;
				minX = Float.MAX_VALUE;
				maxY = -Float.MAX_VALUE;
				minY = Float.MAX_VALUE;
				maxZ = -Float.MAX_VALUE;
				minZ = Float.MAX_VALUE;
				for (int q = 0; q < corners4.size(); q++) {
					Vector4f corner = corners4.get(q);
			        lightView4.transform(corner,corner);
			        minX = Math.min(corner.x, minX);
			        maxX = Math.max(corner.x, maxX);
			        minY = Math.min(corner.y, minY);
			        maxY = Math.max(corner.y, maxY);
			        minZ = Math.min(-1.0f*corner.z, minZ);
			        maxZ = Math.max(-1.0f*corner.z, maxZ);
			    }
				Vector3f lightFrustumCenter4 = new Vector3f((maxX + minX)/2.0f,(maxY + minY)/2.0f,(maxZ + minZ)/2.0f);
				Vector3f lightFrustumCenterOriginal4 = new Vector3f();
				lightFrustumCenterOriginal4.set(lightFrustumCenter4);
				mRange = Math.max(maxX - minX, maxY - minY)*0.5f;
				int sMap4Ratio = 1;
				lightFrustumCenter4.set(this.snap(lightFrustumCenter4.x,(2.0f)*mRange/(shadowMap4Size/sMap4Ratio )), this.snap(lightFrustumCenter4.y,(2.0f)*mRange/(shadowMap4Size/sMap4Ratio )) , lightFrustumCenter4.z);
				lightProjection4.setOrtho(lightFrustumCenter4.x - mRange,lightFrustumCenter4.x + mRange,lightFrustumCenter4.y - mRange,lightFrustumCenter4.y + mRange, minZ - 84.0f, maxZ );
				lightMatrix4.set(lightProjection4);
				lightMatrix4.mul(lightView4,lightMatrix4);
				
				
				
				
				
				
				

				// View Matrix 
				
				Matrix4f vMatrix5 = new Matrix4f();
				//Matrix4f vMatrix5 = (new Matrix4f()).translation(new Vector3f(-1.0f*(float)mainCamera.getEyeX(),-1.0f*(float)mainCamera.getEyeY(),-1.0f*(float)mainCamera.getEyeZ()));
				vMatrix5.lookAt((float)mainCamera.getEyeX(),(float)mainCamera.getEyeY(),(float)mainCamera.getEyeZ(), (float)mainCamera.getVPointX(), (float)mainCamera.getVPointY(), (float)mainCamera.getVPointZ(), 0.0f,1.0f,0.0f );

				// Projection Matrix 
				
				Matrix4f pMatrix5 = (new Matrix4f()).setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(6.0));
				
				// View Projection Matrix
				
				Matrix4f vPMatrix5 = new Matrix4f();
				pMatrix5.mul(vMatrix5,vPMatrix5);
				
				// Inverse View Projection Matrix
				
				vPMatrix5.invert(vPMatrix5);
				
				// Corners of perspective frustum in world space
				
				 maxZ = -Float.MAX_VALUE;
				 minZ = Float.MAX_VALUE;
				
				ArrayList<Vector4f> corners5 = new ArrayList<Vector4f>();
				for (int q = 0; q < corners.size(); q++) {
					Vector4f transformedVec = new Vector4f();
					vPMatrix5.transform(corners.get(q),transformedVec);
					corners5.add(transformedVec);
					corners5.get(q).set(corners5.get(q).x/corners5.get(q).w, corners5.get(q).y/corners5.get(q).w, corners5.get(q).z/corners5.get(q).w, corners5.get(q).w/corners5.get(q).w);
					maxZ = Math.max(maxZ, -1.0f*corners5.get(q).z);
					minZ = Math.min(minZ, -1.0f*corners5.get(q).z);
				}
				Vector4f center5 = new Vector4f(0.0f,0.0f,0.0f,0.0f);
				
				for (int q = 0; q < corners5.size(); q++) {
					center5.add(corners5.get(q),center5);
				}
					center5.div(8,center5);
					
				// Light View Matrix 5
				
				Matrix4f lightView5 = new Matrix4f();
				Vector3f frustumCenter5 = new Vector3f(center5.x,center5.y,center5.z);
				Vector3f sunDirectionForShadowMaps5 = new Vector3f();
				sunDirectionForShadowMaps5.set(sunDirectionForShadowMaps);
				Vector3f sunPos5 = new Vector3f(0.0f,0.0f,0.0f);
				sunPos5.add(frustumCenter5,sunPos5);
				sunPos5.add(sunDirectionForShadowMaps5.mul(-1.0f*(maxZ - minZ  + 16.0f)),sunPos5);
				lightView5.setLookAt(0.0f, 0.0f, 0.0f, sunDirectionForShadowMaps.x, sunDirectionForShadowMaps.y, sunDirectionForShadowMaps.z, 0.0f, 1.0f, 0.0f);
				Matrix4f lightMatrix5 = new Matrix4f();
				Matrix4f lightMatrix5V2 = new Matrix4f();
				
				// Light Projection Matrix 1
				
				Matrix4f lightProjection5 = new Matrix4f();
				Matrix4f lightProjection5V2 = new Matrix4f();
				
				maxX = -Float.MAX_VALUE;
				minX = Float.MAX_VALUE;
				maxY = -Float.MAX_VALUE;
				minY = Float.MAX_VALUE;
				maxZ = -Float.MAX_VALUE;
				minZ = Float.MAX_VALUE;
			
				for (int q = 0; q < corners5.size(); q++) {
					Vector4f corner = corners5.get(q);
				    lightView5.transform(corner,corner);
				    minX = Math.min(corner.x, minX);
			        maxX = Math.max(corner.x, maxX);
			        minY = Math.min(corner.y, minY);
			        maxY = Math.max(corner.y, maxY);
			        minZ = Math.min(-1.0f*corner.z, minZ);
			        maxZ = Math.max(-1.0f*corner.z, maxZ);
				}
					Vector3f lightFrustumCenter5 = new Vector3f((maxX + minX)/2.0f,(maxY + minY)/2.0f,(maxZ + minZ)/2.0f);
			
					
					
				Vector3f lightFrustumCenterOriginal5 = new Vector3f();
				lightFrustumCenterOriginal5.set(lightFrustumCenter5);
				
				mRange = Math.max(maxX - minX, maxY - minY)*0.5f;
				//System.out.println("THAT MRANGE IS " + mRange);
				int sMap5Ratio = 1;
				lightFrustumCenter5.set(this.snap(lightFrustumCenter5.x,(2.0f)*mRange/(shadowMap5Size/sMap5Ratio )), this.snap(lightFrustumCenter5.y,(2.0f)*mRange/(shadowMap5Size/sMap5Ratio )) , lightFrustumCenter5.z);
				lightProjection5.setOrtho(lightFrustumCenter5.x - mRange,lightFrustumCenter5.x + mRange,lightFrustumCenter5.y - mRange,lightFrustumCenter5.y + mRange, minZ - 84.0f, maxZ );
				lightMatrix5.set(lightProjection5);
				lightMatrix5.mul(lightView5,lightMatrix5);
			
				Vector3f lightFrustumCenter5V2 = new Vector3f((maxX + minX)/2.0f,(maxY + minY)/2.0f,(maxZ + minZ)/2.0f);
				lightProjection5V2.setOrtho(lightFrustumCenter5V2.x - mRange,lightFrustumCenter5V2.x + mRange,lightFrustumCenter5V2.y - mRange,lightFrustumCenter5V2.y + mRange, minZ - 84.0f, maxZ );
				lightMatrix5V2.set(lightProjection5V2);
				lightMatrix5V2.mul(lightView5,lightMatrix5V2);
			
				
				
				
				// Get Rotational part of the light's camera transformation
				
				Vector3f rotationLight = new Vector3f();
				
				
			     	
				if (screenBrightness < 1.0f) {
					screenBrightness = screenBrightness + 0.0281f;
				}
				if (screenBrightness > 1.0f) {
					screenBrightness = 1.0f;
				}
				
			
				int testLakeID = zone.getBackgrounds().get(0).getBodiesOfWater().get(0).getCurHeightFieldID();
	         //   glEnable(GL_MULTISAMPLE);
			
	        

	    	
				
				/* * * * * * * * * * * *
	    	     * RENDER SHADOW MAPS  *
	    	     * * * * * * * * * * * */
	          	    	    
	    	// Static Geometry Shadow Map 1    
	    		glBindFramebuffer(GL_FRAMEBUFFER,shadowMap1FBO);
	    		glViewport(0,0,shadowMap1Size,shadowMap1Size);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	 //       	glClearColor((float)Math.exp(80.0),(float)Math.exp(80.0),(float)Math.exp(80.0),1.0f);

	    		glEnable(GL_DEPTH_TEST);
	    		glDisable(GL_MULTISAMPLE);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		
	    		player.renderShadowMap(lightMatrix1,shadowMap1FBO,lightView1,lightFrustumCenter1,lightFrustumCenterOriginal1, shadowMap1Size,mRange1,mnZ1, mxZ1,globalMainCameraMatrix,1);
	    		testCreature1.renderShadowMap(lightMatrix1,shadowMap1FBO,lightView1,lightFrustumCenter1,lightFrustumCenterOriginal1, shadowMap1Size,mRange1,mnZ1, mxZ1);
  	  	  
	    		
	    		for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
	    			if (!zone.getBackgrounds().get(0).getStageItems().get(z).isHasMotion()) {
	    			zone.getBackgrounds().get(0).getStageItems().get(z).renderShadowMap(lightMatrix1, shadowMap1FBO,shadowMap1, lightProjection1, lightView1, mRange1, shadowMap1Size);
	    			}
	            }
	       
	          
	    	 // Static Geometry Shadow Map 2
	    		glBindFramebuffer(GL_FRAMEBUFFER,shadowMap2FBO);
	    		glViewport(0,0,shadowMap2Size,shadowMap2Size);
	    		glClearColor(1.0f,1.0f,0.0f,1.0f);
	    //    	glClearColor((float)Math.exp(80.0),(float)Math.exp(80.0),(float)Math.exp(80.0),1.0f);

	    		glEnable(GL_DEPTH_TEST);
	    		glDisable(GL_MULTISAMPLE);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	            
	    		player.renderShadowMap(lightMatrix2,shadowMap2FBO,lightView2,lightFrustumCenter2,lightFrustumCenterOriginal2, shadowMap2Size,mRange2,mnZ2, mxZ2,globalMainCameraMatrix,2);
	    		testCreature1.renderShadowMap(lightMatrix2,shadowMap2FBO,lightView2,lightFrustumCenter2,lightFrustumCenterOriginal2, shadowMap2Size,mRange2,mnZ2, mxZ2);
    	        
	    		for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
	    			if (!zone.getBackgrounds().get(0).getStageItems().get(z).isHasMotion()) {
		    			
	    			zone.getBackgrounds().get(0).getStageItems().get(z).renderShadowMap(lightMatrix2, shadowMap2FBO,shadowMap2,lightProjection2, lightView2, mRange2, shadowMap2Size);
	    			}
	            }

	            
	         // Static Geometry Shadow Map 3	            
	        	glBindFramebuffer(GL_FRAMEBUFFER,shadowMap3FBO);
	        	glViewport(0,0,shadowMap3Size,shadowMap3Size);
	    		glClearColor(1.0f,1.0f,0.0f,1.0f);
	        	//glClearColor((float)Math.exp(80.0),(float)Math.exp(80.0),(float)Math.exp(80.0),1.0f);

	    		glEnable(GL_DEPTH_TEST);
	    		glDisable(GL_MULTISAMPLE);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		
	    		player.renderShadowMap(lightMatrix3,shadowMap3FBO,lightView3,lightFrustumCenter3,lightFrustumCenterOriginal3, shadowMap3Size,mRange3,mnZ2, mxZ3,globalMainCameraMatrix,3);
	    		testCreature1.renderShadowMap(lightMatrix3,shadowMap3FBO,lightView3,lightFrustumCenter3,lightFrustumCenterOriginal3, shadowMap3Size,mRange3,mnZ2, mxZ3);
	    	    
	    		for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
	    			if (!zone.getBackgrounds().get(0).getStageItems().get(z).isHasMotion()) {
		    			
	    			zone.getBackgrounds().get(0).getStageItems().get(z).renderShadowMap(lightMatrix3, shadowMap3FBO,shadowMap3,lightProjection3, lightView3, mRange3, shadowMap3Size);
	    			}
	            }
	        
	    		
  		
	    		
	    		
	         // Static Geometry Shadow Map 4	            
	        	glBindFramebuffer(GL_FRAMEBUFFER,shadowMap4FBO);
	        	glViewport(0,0,shadowMap4Size,shadowMap4Size);
	    		glClearColor(1.0f,1.0f,0.0f,1.0f);
	       //	glClearColor((float)Math.exp(80.0),(float)Math.exp(80.0),(float)Math.exp(80.0),1.0f);

	    		glEnable(GL_DEPTH_TEST);
	    		glDisable(GL_MULTISAMPLE);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		
	    		player.renderShadowMap(lightMatrix4,shadowMap4FBO,lightView4,lightFrustumCenter4,lightFrustumCenterOriginal4, shadowMap4Size,mRange3,mnZ2, mxZ3,globalMainCameraMatrix,4);
	    		testCreature1.renderShadowMap(lightMatrix4,shadowMap4FBO,lightView4,lightFrustumCenter4,lightFrustumCenterOriginal4, shadowMap4Size,mRange3,mnZ2, mxZ3);
	    	    
	    		for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
	    			if (!zone.getBackgrounds().get(0).getStageItems().get(z).isHasMotion()) {
		    			
	    			zone.getBackgrounds().get(0).getStageItems().get(z).renderShadowMap(lightMatrix4, shadowMap3FBO,shadowMap3,lightProjection3, lightView3, mRange3, shadowMap3Size);
	    			}
	            }

	    		
	    		
	    		
	    		
	         // Static Geometry Shadow Map 5	            
	        	glBindFramebuffer(GL_FRAMEBUFFER,shadowMap5FBO);
	        	glViewport(0,0,shadowMap5Size,shadowMap5Size);
	    		glClearColor(1.0f,1.0f,0.0f,1.0f);
	        //	glClearColor((float)Math.exp(80.0),(float)Math.exp(80.0),(float)Math.exp(80.0),1.0f);

	    		glEnable(GL_DEPTH_TEST);
	    		glDisable(GL_MULTISAMPLE);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		
	    		player.renderShadowMap(lightMatrix5,shadowMap5FBO,lightView5,lightFrustumCenter5,lightFrustumCenterOriginal5, shadowMap5Size,mRange3,mnZ2, mxZ3,globalMainCameraMatrix,5);
	    		testCreature1.renderShadowMap(lightMatrix5,shadowMap5FBO,lightView5,lightFrustumCenter5,lightFrustumCenterOriginal5, shadowMap5Size,mRange3,mnZ2, mxZ3);
	    	    
	    		for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
	    			if (!zone.getBackgrounds().get(0).getStageItems().get(z).isHasMotion()) {
		    			
	    			zone.getBackgrounds().get(0).getStageItems().get(z).renderShadowMap(lightMatrix5, shadowMap3FBO,shadowMap3,lightProjection3, lightView3, mRange3, shadowMap3Size);
	    			}
	            }
	        
	    		
				    		
				    		
				    		// Depth Pass
				    		
				    		
				    		/*
				    		glBindFramebuffer(GL_FRAMEBUFFER,sceneDepthFBO);
		    	    		//glUseProgram(testSceneProgramID);
				    		
				    		glViewport(0,0,sceneWidth,sceneHeight);
				    		glClearColor(0.0f,1.0f,0.0f,1.0f);
				    		glEnable(GL_DEPTH_TEST);
				    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				    
				    		
		        			

				    		
				    		glEnable(GL_CULL_FACE);
				    		glCullFace(GL_BACK);

				    		//glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scenePositions,0);
					           
				    		
				    	    for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
				    	    	//System.out.println("Rendering Depth");
				    	    	//System.exit(0);
				    	    	//zone.getBackgrounds().get(0).getStageItems().get(z).renderDepth(1.0f,mainCamera);
				    	    }
				    		
				    //		zone.getBackgrounds().get(0).getStageItems().get(0).renderDepth(1.0f,mainCamera);
				    	    
				          glDisable(GL_CULL_FACE);
				    		
				    		*/
				    		
				glBindFramebuffer(GL_FRAMEBUFFER,0);
				    		
				    		
				    		
				    		
	        	
	            // Render Pass 1

	    		glBindFramebuffer(GL_FRAMEBUFFER,sceneFBO);
	    		glViewport(0,0,sceneWidth,sceneHeight);
	    		glClearColor(0.0f,0.0f,0.0f,1.0f);
	    		glEnable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glDrawBuffers(drawBuffersList);
	    		glEnable(GL_CULL_FACE);
	    		glCullFace(GL_BACK);
     	        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scene,0);
	            glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT2,GL_TEXTURE_2D,sceneNormals,0);
				
	    		
	            for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
	            	zone.getBackgrounds().get(0).getStageItems().get(z).render(mainCamera,lightMatrix1, lightMatrix2, lightMatrix3,lightMatrix4, lightMatrix5, normalLightTransformMatrix, 
	              shadowMap1, shadowMap2, shadowMap3,shadowMap4, shadowMap5, rotations, screenBrightness,testLakeID,false,1,false, sunDirection, materialM, shouldTestSearchRadius, tessellationEnabled, globalProjectionMatrix);
	            }
  	     	
	            glDisable(GL_CULL_FACE);
	    		
	            
	            player.render(mainCamera, lightMatrix1,  lightMatrix2,lightMatrix3,lightMatrix4,lightMatrix5,shadowMap1, shadowMap2, shadowMap3,shadowMap4,shadowMap5, 1.0f,0,null,globalProjectionMatrix);
	            
	            
	            
  	     	
	            testCreature1.render(mainCamera, lightMatrix1,  lightMatrix2,lightMatrix3,lightMatrix4,lightMatrix5,shadowMap1, shadowMap2, shadowMap3,shadowMap4, screenBrightness,0,null,globalProjectionMatrix);
	            
	            testCubeMap.render(screenBrightness);
	    		
  	
	            glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,sceneRefractive,0);
	            glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT2,GL_TEXTURE_2D,sceneRefractiveNormals,0);
	            glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT7,GL_TEXTURE_2D,sceneWaterYPos,0);
				
    
	           
	           
	           
	           
	           
	           
	           
	           
	           
	           // TEST MOUSE INPUT
	           
				if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == 1) {
				
				}

	           
	           testSlider1.update();
	           
	           
	           
	           
	           
	           
	           
	           
	           
	           
	           
	           
	           /*
	            * * * * * * * * * *
	            * CALCULATE SSAO  *
	            * * * * * * * * * *
	            */
	           

				glBindFramebuffer(GL_FRAMEBUFFER,sceneShadowsFBO);
   	    //		glBindFramebuffer(GL_FRAMEBUFFER,0);
   	    		glViewport(0,0,1920,1080);
   	    		glClearColor(0.0f,0.0f,1.0f,1.0f);
   	    		glDisable(GL_DEPTH_TEST);
   	    		//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

   	    		glUseProgram(shadowsProgramID);
   
   	    		//glActiveTexture(GL_TEXTURE0 + 6);
           		//glBindTexture(GL_TEXTURE_2D,refractiveObjects);
           		//glUniform1i(testSceneSceneLocation,6);
           		
   	    	
   	    		Matrix4f projVM = new Matrix4f();
   	    		projVM.translation(new Vector3f((float)(-mainCamera.getEyeX() ),(float)(-mainCamera.getEyeY()),(float)(-mainCamera.getEyeZ() )));
   	    		Matrix4f projecM = new Matrix4f();
   	    		projecM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
   	    		projecM.mul(projVM,projVM);

   	    		Matrix4f invProjM = new Matrix4f();
   	    		projecM.invert(invProjM);
   	 		
   	    		Matrix4f mainCameraM = new Matrix4f();
   	    		mainCameraM.translation(new Vector3f((float)(-mainCamera.getEyeX()), (float)(-mainCamera.getEyeY()), (float)(-mainCamera.getEyeZ())));
   	    		Matrix3f nTransformMatrix = new Matrix3f();
   	    		mainCameraM.get3x3(nTransformMatrix);
   	    		nTransformMatrix.invert(nTransformMatrix);
   	    		nTransformMatrix.transpose(nTransformMatrix);
   	    		
   	    		Matrix4f projectionM = new Matrix4f();
   	    		projectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
   	 		
   	    		try (MemoryStack stack = MemoryStack.stackPush()) {
   			        FloatBuffer tPM = stack.mallocFloat(16);
   			        FloatBuffer tInvPM = stack.mallocFloat(16);
   			        FloatBuffer tNormalTransformMatrix = stack.mallocFloat(9);
			        FloatBuffer sphereSampleVectors = BufferUtils.createFloatBuffer(3*sSAOSamples);
			        FloatBuffer tProjM = BufferUtils.createFloatBuffer(16);
				    
			        //testSphere[15] = new Vector3f(0.50f,1.0f,1.0f);
			        
        	        projectionM.get(tPM);
        	        invProjM.get(tInvPM);
        	        nTransformMatrix.get(tNormalTransformMatrix);
        	        projectionM.get(tProjM);
        	        
        	        for (int l = 0; l < sSAOSamples; l++) {
        	        	
        	        	testSphere[l].get(l*3, sphereSampleVectors);
        	        	
        	        }
        	        
        	        
        	        
        	        glUniformMatrix4fv(shadowProjMatrixLocation, false, tPM);
        	        glUniformMatrix4fv(shadowInvProjMatrixLocation, false, tInvPM);
        	        glUniformMatrix3fv(shadowNormalTransformMatrixLocation, false, tNormalTransformMatrix);
        	     //glUniformMatrix4fv(shadowProjMatrixLocation, false, tProjM);
        	        
        	        glUniform3fv(shadowSphereSamplesLocation, sphereSampleVectors);
        	        
  	        	    
   		        	        
   		    		    
   			   }
   	    		glActiveTexture(GL_TEXTURE0 + 0);
   	       		glBindTexture(GL_TEXTURE_2D,scenePositions);
   	       		glUniform1i(shadowsScenePositionsSamplerLocation,0);
   	   			
   	       		glActiveTexture(GL_TEXTURE0 + 1);
	       		glBindTexture(GL_TEXTURE_2D,sceneNormals);
	       		glUniform1i(shadowsSceneNormalsSamplerLocation,1);
	   			
	       		glActiveTexture(GL_TEXTURE0 + 2);
		       	glBindTexture(GL_TEXTURE_2D,sceneAmbientShadowsNoise);
		       	glUniform1i(shadowsNoiseSamplerLocation,2);
		   			
   	   			

   	    		glBindVertexArray(renderToScreenQuadVAOID);
       			glEnableVertexAttribArray(0);
       			glEnableVertexAttribArray(1);
       			glEnableVertexAttribArray(2);
       			
       			glDrawArrays(GL_TRIANGLES,0,6);
       			
       			glDisableVertexAttribArray(0);
       			glDisableVertexAttribArray(1);
       			glDisableVertexAttribArray(2);
       			

	           
	           
	           
	           
	           
	           
	           
	           
       			
       			
       			
       			
       			
       			
       			

       			/*
       			 * * * * * * * * * *
       			 * TILED LIGHTING  *
       			 * * * * * * * * * *
       			 */
       			
       			
       			glUseProgram(lightingProgramID);
       			
       			
       			// Far plane position order in list
       			// 1      4
       			//
       			// 2      3
       			
       			float fCP[] = new float[] {(float)(-0.5*farPlaneWidth),(float)(0.5*farPlaneHeight),(float)(farPlaneZ),   
       					(float)(-0.5*farPlaneWidth),(float)(-0.5*farPlaneHeight),(float)(farPlaneZ),   
       					(float)(0.5*farPlaneWidth),(float)(-0.5*farPlaneHeight),(float)(farPlaneZ),   
       					(float)(0.5*farPlaneWidth),(float)(0.5*farPlaneHeight),(float)(farPlaneZ)    };
       			FloatBuffer fCPB = BufferUtils.createFloatBuffer(12);
       			fCPB.put(fCP);
       			fCPB.flip();
       			
       			glUniform3fv(lightingProgramFrustumCornerPositionsLocation, fCPB);
       			glUniform1i(lightingProgramScreenWidthLocation, screenWidth);
       			glUniform1i(lightingProgramScreenHeightLocation, screenHeight);
       			
     		   try (MemoryStack stack = MemoryStack.stackPush()) {
     			   
     			  FloatBuffer tVM = stack.mallocFloat(16);
     			  globalMainCameraMatrix.get(tVM);
     			  FloatBuffer tPM = stack.mallocFloat(16);
    			  globalProjectionMatrix.get(tPM);
    			  
    			  glUniformMatrix4fv(lightingProgramCameraMLocation, false, tVM);
    			  glUniformMatrix4fv(lightingProgramProjectionMLocation, false, tPM);
    			
     		   }
     		   
    			glActiveTexture(GL_TEXTURE0 + 3);
    			glBindTexture(GL_TEXTURE_2D, scenePositions);
    			glUniform1i(lightingProgramDepthLocation,3);
    			
    			glActiveTexture(GL_TEXTURE0 + 4);
    			glBindTexture(GL_TEXTURE_2D, sceneNormals);
    			glUniform1i(lightingProgramNormalsLocation,4);
    			
    			glDispatchCompute(120,68,1);
    			
    			
       			
	           
	           
	           //lake1.render(1.0f, mainCamera);
                

                
                
                
                
         

	       /*
	        * * * * * * * * * * * * * * * * * * * * * * * *
	        * Gaussian Blur Shadows and Ambient Occlusion *
	        * * * * * * * * * * * * * * * * * * * * * * * *
	        */

   			glBindFramebuffer(GL_FRAMEBUFFER,blurGaussianShadowsHorizontalFBO);
	    		//glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredSceneShadows,0);
			glViewport(0,0,sceneWidth,sceneHeight);
	    	glClearColor(1.0f,1.0f,1.0f,1.0f);
	    	glDisable(GL_DEPTH_TEST);
	    	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    	glUseProgram(blurGaussianSceneShadowsProgramID);
   	        glActiveTexture(GL_TEXTURE0 + 2);
       		glBindTexture(GL_TEXTURE_2D,sceneShadows);
       		glUniform1i(blurGaussianSceneShadowsLocation,2);
   			
   			glActiveTexture(GL_TEXTURE0 + 3);
       		glBindTexture(GL_TEXTURE_2D,scenePositions);
       		glUniform1i(blurGaussianScenePositionsLocation,3);
   			
       		glActiveTexture(GL_TEXTURE0 + 4);
       		glBindTexture(GL_TEXTURE_2D,sceneShadowRadius);
       		glUniform1i(blurGaussianSceneShadowRadiusLocation,4);
   			
       		glActiveTexture(GL_TEXTURE0 + 5);
       		glBindTexture(GL_TEXTURE_2D,sceneAmbientShadows);
       		glUniform1i(blurGaussianSceneAmbientShadowsLocation,5);
   			
       		glUniform1i(blurGaussianSceneShadowsHorizontalLocation,1);
       		
   			glBindVertexArray(renderToScreenQuadVAOID);
   			glEnableVertexAttribArray(0);
   			glEnableVertexAttribArray(1);
   			glEnableVertexAttribArray(2);
   			
   			
   			glDrawArrays(GL_TRIANGLES,0,6);
   			
   			glDisableVertexAttribArray(0);
   			glDisableVertexAttribArray(1);
   			glDisableVertexAttribArray(2);
   			
   			glUseProgram(0);

   			
   			glBindFramebuffer(GL_FRAMEBUFFER,blurGaussianShadowsVerticalFBO);
	    		//glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredSceneShadows,0);
	    	glViewport(0,0,sceneWidth,sceneHeight);
	    	glClearColor(1.0f,1.0f,1.0f,1.0f);
	    	glDisable(GL_DEPTH_TEST);
	    	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    	glUseProgram(blurGaussianSceneShadowsProgramID);
   			glActiveTexture(GL_TEXTURE0 + 2);
       		glBindTexture(GL_TEXTURE_2D,horizontallyBlurredSceneShadows);
       		glUniform1i(blurGaussianSceneShadowsLocation,2);
       		
       		glActiveTexture(GL_TEXTURE0 + 3);
       		glBindTexture(GL_TEXTURE_2D,scenePositions);
       		glUniform1i(blurGaussianScenePositionsLocation,3);
   			
       		glActiveTexture(GL_TEXTURE0 + 4);
       		glBindTexture(GL_TEXTURE_2D,sceneShadowRadius);
       		glUniform1i(blurGaussianSceneShadowRadiusLocation,4);
   			
       		glActiveTexture(GL_TEXTURE0 + 5);
       		glBindTexture(GL_TEXTURE_2D,horizontallyBlurredSceneAmbientShadows);
       		glUniform1i(blurGaussianSceneAmbientShadowsLocation,5);
   			
       		
       		glUniform1i(blurGaussianSceneShadowsHorizontalLocation,0);
           	

       		Matrix4f invProjectionM = new Matrix4f();
       		invProjectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
       		invProjectionM.invert();
       		
       		Matrix4f invCamM = new Matrix4f();
       		invCamM.translation(new Vector3f((float)(mainCamera.getEyeX()), (float)(-mainCamera.getEyeY()), (float)(-mainCamera.getEyeZ())));
       		invCamM.invert();
       		
			   try (MemoryStack stack = MemoryStack.stackPush()) {
			        FloatBuffer tInvProjM = stack.mallocFloat(16);
			        FloatBuffer tInvCamM = stack.mallocFloat(16);
			    
		        	        invProjectionM.get(tInvProjM);
		        	        invCamM.get(tInvCamM);
		                
		        	        glUniformMatrix4fv(blurGaussianInvProjMLocation, false, tInvProjM);
		        	        glUniformMatrix4fv(blurGaussianInvCamMLocation, false, tInvCamM);
		                
		    		    
			   }
	    	
			   
       		glBindVertexArray(renderToScreenQuadVAOID);
   			glEnableVertexAttribArray(0);
   			glEnableVertexAttribArray(1);
   			glDrawArrays(GL_TRIANGLES,0,6);
   			glDisableVertexAttribArray(0);
   			glDisableVertexAttribArray(1);
   			glUseProgram(0);

   			
 	    
	   
   			
   			
   	       
   	       /*
   	        **************************************
   	        ************* Bloom ******************        
   	        **************************************                                                         
   		   */
   		            

	           
	    		glBindFramebuffer(GL_FRAMEBUFFER,downSampledSceneAFBO);
	    	    		
	    		glViewport(0,0,sceneWidth/2,sceneHeight/2);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		
	    
				glUseProgram(renderToScreenQuadProgramID);
    			
		        glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,sceneBright);
        
        		glUniform1i(renderToScreenQuadSceneSamplerLocation,2);
    			
    			glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			
    			glDrawArrays(GL_TRIANGLES,0,6);
    			
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			
    			
    			glUseProgram(0);


    			glBindFramebuffer(GL_FRAMEBUFFER,horizontallyBlurredDownSampledSceneAFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneA,0);
				glViewport(0,0,sceneWidth/2,sceneHeight/2);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    	        glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,downSampledSceneA);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
    			glUniform1i(blurGaussianHorizontalLocation,1);
        		glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    			
    			glBindFramebuffer(GL_FRAMEBUFFER,blurredDownSampledSceneAFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredDownSampledSceneA,0);
	    		glViewport(0,0,sceneWidth/2,sceneHeight/2);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    			glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneA);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
        		glUniform1i(blurGaussianHorizontalLocation,0);
            	glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    			

    			
    			

	    		glBindFramebuffer(GL_FRAMEBUFFER,downSampledSceneBFBO);
	    		
	    		glViewport(0,0,sceneWidth/4,sceneHeight/4);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		
	    
				glUseProgram(renderToScreenQuadProgramID);
    			
		        glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,downSampledSceneA);
        
        		glUniform1i(renderToScreenQuadSceneSamplerLocation,2);
    			
    			glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			
    			glDrawArrays(GL_TRIANGLES,0,6);
    			
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			
    			
    			glUseProgram(0);


    			glBindFramebuffer(GL_FRAMEBUFFER,horizontallyBlurredDownSampledSceneBFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneB,0);
				glViewport(0,0,sceneWidth/4,sceneHeight/4);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    	        glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,downSampledSceneB);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
    			glUniform1i(blurGaussianHorizontalLocation,1);
        		glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    			
    			glBindFramebuffer(GL_FRAMEBUFFER,blurredDownSampledSceneBFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredDownSampledSceneB,0);
	    		glViewport(0,0,sceneWidth/4,sceneHeight/4);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    			glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneB);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
        		glUniform1i(blurGaussianHorizontalLocation,0);
            	glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    	

    			
    			
    			


    			glBindFramebuffer(GL_FRAMEBUFFER,horizontallyBlurredDownSampledSceneBFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneB,0);
				glViewport(0,0,sceneWidth/4,sceneHeight/4);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    	        glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,blurredDownSampledSceneB);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
    			glUniform1i(blurGaussianHorizontalLocation,1);
        		glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    			
    			glBindFramebuffer(GL_FRAMEBUFFER,blurredDownSampledSceneBFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredDownSampledSceneB,0);
	    		glViewport(0,0,sceneWidth/4,sceneHeight/4);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    			glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneB);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
        		glUniform1i(blurGaussianHorizontalLocation,0);
            	glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    	
    			
    			
    			

	    		glBindFramebuffer(GL_FRAMEBUFFER,downSampledSceneCFBO);
	    		
	    		glViewport(0,0,sceneWidth/8,sceneHeight/8);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		
	    
				glUseProgram(renderToScreenQuadProgramID);
    			
		        glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,downSampledSceneB);
        
        		glUniform1i(renderToScreenQuadSceneSamplerLocation,2);
    			
    			glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			
    			glDrawArrays(GL_TRIANGLES,0,6);
    			
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			
    			
    			glUseProgram(0);


    			


    			glBindFramebuffer(GL_FRAMEBUFFER,horizontallyBlurredDownSampledSceneCFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneC,0);
				glViewport(0,0,sceneWidth/8,sceneHeight/8);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    	        glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,downSampledSceneC);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
    			glUniform1i(blurGaussianHorizontalLocation,1);
        		glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    			
    			glBindFramebuffer(GL_FRAMEBUFFER,blurredDownSampledSceneCFBO);
	    		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredDownSampledSceneC,0);
	    		glViewport(0,0,sceneWidth/8,sceneHeight/8);
	    		glClearColor(1.0f,1.0f,1.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    		glUseProgram(blurGaussianProgramID);
    			glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneC);
        		glUniform1i(blurGaussianSceneSamplerLocation,2);
        		glUniform1i(blurGaussianHorizontalLocation,0);
            	glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    			glDrawArrays(GL_TRIANGLES,0,6);
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			glUseProgram(0);

    	
	        			
	        			
	        			

	        			
	        			
	        			
	        			
	    		
	    		
	    		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	    		 * RENDER COMPLETE SCENE BEFORE INCLUSION OF REFRACTIVE OBJECTS  *
	    		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	        			
    			glBindFramebuffer(GL_FRAMEBUFFER,scenePreRefractionFBO);
		
			    glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scenePreRefraction,0);

	    //		glBindFramebuffer(GL_FRAMEBUFFER,0);
	    		glViewport(0,0,1920,1080);
	    		glClearColor(0.0f,0.0f,0.0f,1.0f);
	    		glEnable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	    //		glEnable(GL_MULTISAMPLE);

	    	    glUseProgram(sceneProgramID);
	    	    //System.out.println("SB of " + screenBrightness);
		        glUniform1f(sceneScreenBrightnessLocation,screenBrightness);

		        Vector3f lightD = sunDirection;				
		        Vector3f lightC = new Vector3f(1.50f,1.50f,1.70f);
		        
		        try (MemoryStack stack = MemoryStack.stackPush()) {
			        FloatBuffer tMVPM = stack.mallocFloat(16);
			        FloatBuffer tLightMatrix1 = stack.mallocFloat(16);
			        FloatBuffer tLightMatrix2 = stack.mallocFloat(16);
 			        FloatBuffer tLightMatrix3 = stack.mallocFloat(16);
				     
			        FloatBuffer lightDV = stack.mallocFloat(3);
			        FloatBuffer lightCV = stack.mallocFloat(3);
			
		    		        
        	        lightD.get(lightDV);
        	        lightC.get(lightCV);

    	    	    glUniform1i(sceneDirectionalLightTypeLocation, 1);
    			    glUniform3fv(sceneDirectionalLightDirectionLocation,  lightDV);
    		        glUniform3fv(sceneDirectionalLightColorLocation, lightCV);
		        }
		        

		        //glUniform1f(sceneDirectionalLightAmountLocation,0.7f);
		            
		        //glUniform1f(sceneBrightnessLocation,screenBrightness);
		    
		        
		    	glActiveTexture(GL_TEXTURE0 + 2);
        		glBindTexture(GL_TEXTURE_2D,scene);
        		glUniform1i(sceneLocation,2);
        		glActiveTexture(GL_TEXTURE0 + 3);
        		glBindTexture(GL_TEXTURE_2D,blurredDownSampledSceneA);
        		glUniform1i(bloom1Location,3);
        		glActiveTexture(GL_TEXTURE0 + 4);
        		glBindTexture(GL_TEXTURE_2D,blurredDownSampledSceneB);
        		glUniform1i(bloom2Location,4);
        		glActiveTexture(GL_TEXTURE0 + 5);
        		glBindTexture(GL_TEXTURE_2D,blurredDownSampledSceneC);
        		glUniform1i(bloom3Location,5);
       
        		
            	glActiveTexture(GL_TEXTURE0 + 6);
            	if (shouldUseBlurredSceneShadows) {
            		glBindTexture(GL_TEXTURE_2D,blurredSceneShadows);
            	}
            	else {
            		glBindTexture(GL_TEXTURE_2D,sceneShadows);
            	}
      
        		glUniform1i(sceneShadowsLocation,6);
       
        		
        		glActiveTexture(GL_TEXTURE0 + 7);
        		glBindTexture(GL_TEXTURE_2D,sceneNormals);
        		glUniform1i(sceneNormalsLocation,7);
       
        		
         		glActiveTexture(GL_TEXTURE0 + 8);
        		glBindTexture(GL_TEXTURE_2D,blurredSceneAmbientShadows);
        		glUniform1i(sceneAmbientShadowsLocation,8);
       
        	
	    		glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    	
    			glDrawArrays(GL_TRIANGLES,0,6);
    			
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
    			
	            
	    		glDisable(GL_MULTISAMPLE);
   
    	//		glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,0,0);
				       
    			glUseProgram(0);
	
    			
    			
    			
    			
    			// TODO
    			// Point light shadows, rendering depth to cube map
       			glBindFramebuffer(GL_DRAW_FRAMEBUFFER,pointLight1CubeMapShadowsFBO);
    			zone.getBackgrounds().get(0).getStageItems().get(0).renderDepthCubeMap(screenBrightness,pointLight1CubeMapShadows,(Vector3f)null,1.0f,(Matrix4f)null,(Matrix4f)null,(Matrix4f)null,(Matrix4f)null,(Matrix4f)null,(Matrix4f)null);
    			
    			
    			
    			
    			
    			
    			/* * * * * * * * * * * * * * * * * *
    			 * RENDER COMPLETE SCENE TO THE SCREEN  *
    			 * * * * * * * * * * * * * * * * * */
    			

    			glBindFramebuffer(GL_FRAMEBUFFER,0);
    				
	    //		glBindFramebuffer(GL_FRAMEBUFFER,0);
	    		glViewport(0,0,1920,1080);
	    		glClearColor(0.0f,0.0f,0.0f,1.0f);
	    		glEnable(GL_DEPTH_TEST);
	    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    	    		
	    	    String drawingMode = "Solid";
	    	    
	    		if (drawingMode.compareTo("Solid") == 0)  {
	    			
    	    		glUseProgram(sceneCompleteProgramID);
    
    	    	    Matrix4f projM = new Matrix4f();
    	    	    projM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
    	    		Matrix4f vMatrix = new Matrix4f();
    				vMatrix.translation(new Vector3f((float)(-mainCamera.getEyeX() ),(float)(-mainCamera.getEyeY()),(float)(-mainCamera.getEyeZ() )));
    				Matrix4f wrldMatrix = new Matrix4f();
    				//wrldMatrix.translation((float)lake1.getX(),(float)lake1.getY(),(float)lake1.getZ());
    				Matrix4f worldVMatrix = new Matrix4f();
    				worldVMatrix.set(vMatrix);
    				
    		        try (MemoryStack stack = MemoryStack.stackPush()) {
    		        	
    			        FloatBuffer tWMT = stack.mallocFloat(16);
    			        FloatBuffer tPMT = stack.mallocFloat(16);
    			        FloatBuffer lightDV = stack.mallocFloat(3);
    			        FloatBuffer lightCV = stack.mallocFloat(3);
    			        
	        	        lightD.get(lightDV);
	        	        lightC.get(lightCV);
	        	        projM.get(tPMT);
	        	        vMatrix.get(tWMT);
	        	        
	    	    	    glUniform1i(sceneCompleteDirectionalLightTypeLocation, 1);
	    			    glUniform3fv(sceneCompleteDirectionalLightDirectionLocation,  lightDV);
	    		        glUniform3fv(sceneCompleteDirectionalLightColorLocation, lightCV);
	    		        glUniformMatrix4fv(sceneCompleteProjMLocation, false, tPMT);
	    		        glUniformMatrix4fv(sceneCompleteWVMLocation, false, tWMT);
	    		        
    		        }

    		        
    		        
    		    	glActiveTexture(GL_TEXTURE0 + 2);
            		glBindTexture(GL_TEXTURE_2D,scenePreRefraction);
            		glUniform1i(scenePreRefractionLocation,2);
            
        		
            		glActiveTexture(GL_TEXTURE0 + 11);
        			glBindTexture(GL_TEXTURE_2D,prevScenePreRefraction);
        			glUniform1i(prevScenePreRefractionLocation,11);
            

    		    	glActiveTexture(GL_TEXTURE0 + 3);
            		glBindTexture(GL_TEXTURE_2D,refractiveObjects);
            		glUniform1i(refractiveObjectsLocation,3);
            
            
                	glActiveTexture(GL_TEXTURE0 + 4);
            		glBindTexture(GL_TEXTURE_2D,sceneRefractiveNormals);
            		glUniform1i(waterNormalsLocation,4);
            
            		
                	glActiveTexture(GL_TEXTURE0 + 5);
            		glBindTexture(GL_TEXTURE_2D,scenePositions);
            		glUniform1i(scenePosPreRefractionLocation,5);
            		

            		glActiveTexture(GL_TEXTURE0 + 6);
            		glBindTexture(GL_TEXTURE_2D,sceneWaterYPos);
            		glUniform1i(waterYPosWorldLocation,6);
            		

            		glEnable(GL_TEXTURE_2D);
            		glDisable(GL_TEXTURE_2D);
            		
            		glEnable(GL_TEXTURE_CUBE_MAP);
            	
            		
            		
            		//
            		glActiveTexture(GL_TEXTURE0 + 8);
            		//glBindTexture(GL_TEXTURE_CUBE_MAP,testCubeMap.getCubeMapID());
            		glBindTexture(GL_TEXTURE_CUBE_MAP,pointLight1CubeMapShadows);
            		glUniform1i(reflectionsCubeMapLocation,8);
            		//
            		
            		
    	    		glBindVertexArray(renderToScreenQuadVAOID);
        			glEnableVertexAttribArray(0);
        			glEnableVertexAttribArray(1);
        	
        			glDrawArrays(GL_TRIANGLES,0,6);
        			
        			glDisableVertexAttribArray(0);
        			glDisableVertexAttribArray(1);
        			
        			glEnable(GL_TEXTURE_2D);
            		
	    		} 
	    		
	    		else if (drawingMode.compareTo("Wireframe") == 0) {
	    			
					 for (int z = 0; z < zone.getBackgrounds().get(0).getStageItems().size(); z++) {
				     	zone.getBackgrounds().get(0).getStageItems().get(z).renderWireFrame(mainCamera,normalLightTransformMatrix,1.0f,tessellationEnabled); 
				     }
					 
					 System.out.println("SLIDER CURRENT VALUE IS " + testSlider1.getValue());
					 testSlider1.draw();
					
	    		}
    			
	        			
	        			
    			
    		

	    		/* * * * * * * * * * *
	    		 * DRAW TEST IMAGES  *
	    		 * * * * * * * * * * */
	    
				glBindFramebuffer(GL_FRAMEBUFFER,0);
	    		glBindFramebuffer(GL_FRAMEBUFFER,0);
	    		glViewport(0,0,1920,1080);
	    		//glClearColor(0.0f,0.0f,0.0f,1.0f);
	    		glDisable(GL_DEPTH_TEST);
	    		//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	    		glUseProgram(testSceneProgramID);

	    		glActiveTexture(GL_TEXTURE0 + 6);
        		glBindTexture(GL_TEXTURE_2D,scene);
        		glUniform1i(testSceneSceneLocation,6);
        		

	    		glBindVertexArray(renderToScreenQuadVAOID);
    			glEnableVertexAttribArray(0);
    			glEnableVertexAttribArray(1);
    	
    		//  glDrawArrays(GL_TRIANGLES,0,6);
    			
    			glDisableVertexAttribArray(0);
    			glDisableVertexAttribArray(1);
        			
  	
	        
      
  	
	    	        			
	            // Render Standard

		        //glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

	       
	            if (!vSync) {        
	            	glFinish();
	            }
  				double taskComplete = System.nanoTime();  				

  				System.out.println(" Task Completion Time " + (taskComplete - taskBegin) + " ns");

  				//lake1.render();
  				//testStageItem1.render();
				glfwSwapBuffers(window); // swap the color buffers
				//glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
				
			}
			
			
			else if ( sequence == "Main Menu") {
				
				glEnable(GL_MULTISAMPLE);
		
				glEnable(GL_DEPTH_TEST);
		//		glDisable(GL_DEPTH_TEST);
				
				glEnable(GL_BLEND); 
				glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
				
				for (int k = 0; k < activeMenu.getMenuBoxItems().size(); k++) {
					
					if (activeMenu.getMenuBoxItems().get(k).isSelected()) {
						
						sequence = "Move To";
						currentMoveToType = activeMenu.getMenuBoxItems().get(k).getMoveTo().getType();
					    
						
					}
				}
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				
				mainMenu.render(screenBrightness);
				for(int k = 0; k < mainMenu.getMenuBoxItems().size(); k++){
					//System.exit(0);;
					mainMenu.getMenuBoxItems().get(k).render(screenBrightness);
				}
				glfwSwapBuffers(window);
				
				
				
			}
			
			else if ( sequence == "Move To") {
				   
				if (currentMoveToType == "BIn") {
					
					sequence = "Level";
					
				}
				else if (currentMoveToType == "BOutAndLoadLevel") {
					
			
					if (screenBrightness > 0.0f) {
						screenBrightness = screenBrightness - 0.0281f;
						
						glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
						
						mainMenu.render(screenBrightness);
						for(int k = 0; k < mainMenu.getMenuBoxItems().size(); k++){
			 				//System.exit(0);;
							mainMenu.getMenuBoxItems().get(k).render(screenBrightness);
						}
						glfwSwapBuffers(window);
						
					
						
					}
					else {
					 player = new Player(null);
					//testCreature1 = new Pather(-8.0 ,-19.509527086202826 + 0.5,-14.241);
					testCreature1 = new Pather(6.0,12.0,-10.241);
							
					mainCamera = new Camera(7.0,-18.990,0.0, 7.0,-18.990,-8.0);
					 ArrayList<Creature> testCreatures = new ArrayList<Creature>();
					 testCreatures.add(testCreature1);
					 Background testBG1 = new Background(player);

					 
					 testSphere = generateSphereOfPoints(sSAOSamples);
					 for (int j = 0; j < sSAOSamples; j++) {
					
						 //System.out.println("Sphere point " + j + " is " + testSphere[j]);
					 }
					 
					 //System.exit(0);
					 
					 // Water Initialize
					    
						lake1 = new Water(1100,1100,-20.1,-15.971,0.01,0.50f,20.0f,testBG1,testWrap1,16.0f);  // Scale was 0.0261 
			
					 testWrap1 = new Wrap("C:\\TestWrap2.png",0,1,1);
					 
					 
					 ArrayList<StageItemPOM> stageItemsBG1 = new ArrayList<StageItemPOM>();
		 
					 
			
					 
					 
					 
					 /*
					  * * * * * * * * * * * *
					  * * * * * * * * * * * *
					  * LOADING STAGE ITEMS *
					  * * * * * * * * * * * *
					  * * * * * * * * * * * *
					  */
							
					 
					 
					 
					 int stageItemCount = 1;
					 String levelOneStageItemsFilePath = "C:/StageItems/LevelOneStageItemFiles.txt";
					 FileReader fileReaderTerrain = 
							 new FileReader(levelOneStageItemsFilePath);
					 BufferedReader bufferedReaderTerrain = 
				                new BufferedReader(fileReaderTerrain);
			
					 String terrainFileLine;
		
					 
					 
					 
					 
				 
			try {
				
				while ( (terrainFileLine = bufferedReaderTerrain.readLine()) != null) {
						 
					
					 
				     String stageItemFileTitle = terrainFileLine;
				     System.out.println("Stage Item File Name of " + stageItemFileTitle);
					 
				     //	     System.exit(0);
					 String stageItemLine = null;
					
					 boolean readingVertices = false;
					 boolean readingTextureCoords = false;
					 boolean readingSmoothNormalVecs = false;
					 boolean readingSmoothTangentVecs = false;
					 boolean readingPlanarNormalVecs = false;
					 boolean readingIndicies = false;
					 boolean readingTessellationFactors = false;
					 

					 
					 
					 
					 // Diffuse Map, Normal Map, Specular Map of a stage item
					 
					 String stageItemDiffuseMapPath = "A";
					 boolean stageItemHasNormalMap = true;
					 String stageItemNormalMapPath = "A";
					 boolean stageItemHasDisplacementMap = true;
					 String stageItemDisplacementMapPath = "A";
					 
					   terrainFileLine = bufferedReaderTerrain.readLine();
					   terrainFileLine.trim();
			            stageItemDiffuseMapPath = bufferedReaderTerrain.readLine();
			            terrainFileLine = bufferedReaderTerrain.readLine();
			            stageItemNormalMapPath = bufferedReaderTerrain.readLine();
			            if (!stageItemNormalMapPath.equals("null")) {
			            	stageItemHasNormalMap = true;
			            }
			            else {
			            	stageItemHasNormalMap = false;
				        }
					terrainFileLine = bufferedReaderTerrain.readLine();

			            stageItemDisplacementMapPath = bufferedReaderTerrain.readLine();
			            if (!stageItemDisplacementMapPath.equals("null")) {
			            	stageItemHasDisplacementMap = true;
			            }
			            else {
			            	stageItemHasDisplacementMap = false;
				        }
			            
			         
			            
			            ArrayList<Vector3f> instanceTranslations = new ArrayList<Vector3f>();
			            ArrayList<Vector3f> instanceRotations= new ArrayList<Vector3f>();
			            ArrayList<Float> instanceSizes = new ArrayList<Float>();
			            
			            Vector3f instanceTranslation;
			            Vector3f instanceRotation;
			            float instanceSize;
			            boolean stageItemMoves;
			            
			        // Instances of a stage item
			          
			            terrainFileLine = bufferedReaderTerrain.readLine();
			            String[] translationTokens = terrainFileLine.split(" ",100);
			            instanceTranslation = new Vector3f(Float.parseFloat(translationTokens[0]), Float.parseFloat(translationTokens[1]), Float.parseFloat(translationTokens[2]));
			            terrainFileLine = bufferedReaderTerrain.readLine();
			            instanceSize = Float.parseFloat(terrainFileLine);
			            terrainFileLine = bufferedReaderTerrain.readLine();
			            String[] rotationTokens = terrainFileLine.split(" ",100);
			            instanceRotation = new Vector3f(Float.parseFloat(rotationTokens[0]), Float.parseFloat(rotationTokens[1]), Float.parseFloat(rotationTokens[2]) );
			  
			            terrainFileLine = bufferedReaderTerrain.readLine();
			            stageItemMoves =  Boolean.parseBoolean(terrainFileLine);
						  		  
			            
			            
					 float[] stageItemV = null;
					 float[] stageItemT = null;
					 float[] stageItemSmoothN = null;
					 float[] stageItemSmoothTan = null;
					 float[] stageItemPlanarN = null;
					 int[] stageItemIn = null;
					 int theCount = 0;	 
				        try { 
				        	System.out.println("About to open mesh file for " + stageItemFileTitle);
				            // FileReader reads text files in the default encoding.
				            FileReader fileReader = 
				                new FileReader(stageItemFileTitle);

				            // Always wrap FileReader in BufferedReader.
				            BufferedReader bufferedReader = 
				                new BufferedReader(fileReader);

				            // Get the vertex count, where each vertex is 3 floats
				            stageItemLine = bufferedReader.readLine();
				            int stageItemVCount = Integer.parseInt(stageItemLine.split("=",2)[1]);
				            theCount = stageItemVCount;
				            
				            // Get the number of indices, where each index points to a vertex, texture, and normal vector
				            stageItemLine = bufferedReader.readLine();
				            int stageItemIndexCount = Integer.parseInt(stageItemLine.split("=",2)[1]);
				            
				            
				            stageItemV = new float[3*stageItemVCount];
				            stageItemT = new float[2*stageItemVCount];
				            stageItemSmoothN = null;
				            stageItemSmoothTan = null;
				            stageItemPlanarN = null;
				            
				            if (stageItemIndexCount != 0) {
					            stageItemIn = new int[stageItemIndexCount];
				            } else {
				            	stageItemIn = null;
				            }
				            
				            
				            
				            
				            int r = 0;
				            
				            while((stageItemLine = bufferedReader.readLine()) != null) {

					            if (stageItemLine.equals("Vertex Coordinates")) {
					            	
									System.out.println("Vertex Line " );
	
					            	
					            	r = 0;
					            	
					            	readingVertices = true;
					            	readingTextureCoords = false;
					            	readingSmoothNormalVecs = false;
					            	readingPlanarNormalVecs = false;
					            	readingSmoothTangentVecs = false;
					            	readingIndicies = false;
					            	continue;
					            }
					            if (stageItemLine.equals("Texture Coordinates")) {
					            	
					            	r = 0;
					            	
					            	readingVertices = false;
					            	readingTextureCoords = true;
					            	readingSmoothNormalVecs = false;
					            	readingPlanarNormalVecs = false;
					            	readingSmoothTangentVecs = false;
					            	readingIndicies = false;
					            	continue;
					            }
					           if (stageItemLine.equals("Smooth Normal Vectors")) {
							
					        	   if (stageItemSmoothN == null) {
					        		   stageItemSmoothN = new float[3*stageItemVCount];
					        	   }
					        	   r = 0;
					        	   
					        	   readingVertices = false;
					        	   readingTextureCoords = false;
					        	   readingPlanarNormalVecs = false;
					        	   readingSmoothNormalVecs = true;
					        	   readingSmoothTangentVecs = false;
					        	   readingIndicies = false;
					        	   continue;
					           }
					           if (stageItemLine.equals("Smooth Tangent Vectors")) {
									
					        	   if (stageItemSmoothTan == null) {
					        		   stageItemSmoothTan = new float[3*stageItemVCount];
					        	   }
					        	   r = 0;
					        	   
					        	   readingVertices = false;
					        	   readingTextureCoords = false;
					        	   readingPlanarNormalVecs = false;
					        	   readingSmoothNormalVecs = false;
					        	   readingSmoothTangentVecs = true;
					        	   readingIndicies = false;
					        	   continue;
					           }
					           
					           if (stageItemLine.equals("Plane Normal Vectors")) {
									
					        	   if (stageItemPlanarN == null) {
					        		   stageItemPlanarN = new float[3*stageItemVCount];
					        	   }
					        	   
					        	   r = 0;
					        	   
					        	   readingVertices = false;
					        	   readingTextureCoords = false;
					        	   readingSmoothNormalVecs = false;
					        	   readingPlanarNormalVecs = true;
					        	   readingSmoothTangentVecs = false;
					        	   readingIndicies = false;
					        	   continue;
					           }

							if (stageItemLine.equals("Indicies")) {
								
								r = 0;
								
								readingVertices = false;
								readingTextureCoords = false;
								readingSmoothNormalVecs = false;
				            	readingPlanarNormalVecs = false;
				            	readingSmoothTangentVecs = false;
								readingIndicies = true;
								continue;
							}
							
							
							
							if (readingVertices) {
								
								String[] vertex = stageItemLine.split(" ");
								stageItemV[r] = (Float.parseFloat(vertex[0]));
								stageItemV[r + 1] = (Float.parseFloat(vertex[1]));
								stageItemV[r + 2] =(Float.parseFloat(vertex[2]));
								r = r + 3;
								
							}
							if (readingTextureCoords) {
								
								String[] vertex = stageItemLine.split(" ");
								stageItemT[r] = (Float.parseFloat(vertex[0]));
								stageItemT[r + 1] = (            Float.parseFloat(vertex[1]));
								r = r + 2;
								
							}
							if (readingSmoothNormalVecs) {
								
								String[] vertex = stageItemLine.split(" ");
								stageItemSmoothN[r] = (Float.parseFloat(vertex[0]));
								stageItemSmoothN[r + 1] = (Float.parseFloat(vertex[1]));
								stageItemSmoothN[r + 2] = (Float.parseFloat(vertex[2]));
								r = r + 3;
								
							}
							if (readingSmoothTangentVecs) {
								
								String[] vertex = stageItemLine.split(" ");
								stageItemSmoothTan[r] = (Float.parseFloat(vertex[0]));
								stageItemSmoothTan[r + 1] = (Float.parseFloat(vertex[1]));
								stageItemSmoothTan[r + 2] = (Float.parseFloat(vertex[2]));
								r = r + 3;
								
							}
							if (readingPlanarNormalVecs) {
								
								String[] vertex = stageItemLine.split(" ");
								stageItemPlanarN[r] = (Float.parseFloat(vertex[0]));
								stageItemPlanarN[r + 1] = (Float.parseFloat(vertex[1]));
								stageItemPlanarN[r + 2] = (Float.parseFloat(vertex[2]));
								r = r + 3;
								
							}
							if (readingIndicies) {
								
								String[] vertex = stageItemLine.split(" ");
								stageItemIn[r] = (Integer.parseInt(vertex[0]));
								stageItemIn[r + 1] = (Integer.parseInt(vertex[1]));
								stageItemIn[r + 2] = (Integer.parseInt(vertex[2]));
								r = r + 3;
								
							}
	     				            
				            
				            
				            
				            
				       } 
				            
				            // Always close files.
				            bufferedReader.close();         
				        }
				        catch(FileNotFoundException ex) {
				        	System.out.println("Dang 1");
				            System.out.println(
				                "Unable to open file '" + 
				                		terrainFileLine + "'");                
				        }
				        catch(IOException ex) {
				        	System.out.println("Dang 2");
				            System.out.println(
				                "Error reading file '" 
				                + terrainFileLine + "'");                  
				            // Or we could just do this: 
				            // ex.printStackTrace();
				        }
			
				   
				        Wrap stageItemWrap = new Wrap(stageItemDiffuseMapPath,1,1,1);
			
						 ArrayList<Wrap> stageItemMaterials = new ArrayList<Wrap>();
						 stageItemMaterials.add(stageItemWrap);
				
						 Wrap stageItemNormalMap;
						 if (stageItemHasNormalMap) {
							 stageItemNormalMap = new Wrap(stageItemNormalMapPath,1,2,1);
						 }
						 else {
							 stageItemNormalMap = null;
						 }
				
						 

						 Wrap stageItemDisplacementMap;
						 if (stageItemHasDisplacementMap) {
							 stageItemDisplacementMap = new Wrap(stageItemDisplacementMapPath,1,3,2);
						 }
						 else {
							 stageItemDisplacementMap = null;
						 }
				
						 ArrayList<float[]> testTCoordsMaterials4 = new ArrayList<float[]>();
				        testTCoordsMaterials4.add(stageItemT);
 				        
				        //testStageItem1Nice = new StageItem(instanceTranslation.x, instanceTranslation.y, instanceTranslation.z, stageItemV,stageItemSmoothN,stageItemPlanarN,stageItemIn,
				        		//stageItemMaterials,testTCoordsMaterials4,testBG1,instanceSize,instanceRotation.x, instanceRotation.y, instanceRotation.z, stageItemHasNormalMap,stageItemNormalMap,stageItemHasDisplacementMap,stageItemDisplacementMap, stageItemMoves, lake1.getCurHeightFieldID());
				        		testStageItem1Nice = new StageItemPOM(instanceTranslation.x, instanceTranslation.y, instanceTranslation.z, stageItemV,stageItemSmoothN, stageItemSmoothTan, stageItemIn,
				        		stageItemMaterials,testTCoordsMaterials4,testBG1,instanceSize,instanceRotation.x, instanceRotation.y, instanceRotation.z, stageItemHasNormalMap,stageItemNormalMap,stageItemHasDisplacementMap,stageItemDisplacementMap, stageItemMoves, lake1.getCurHeightFieldID());
						  
				       stageItemsBG1.add(testStageItem1Nice); 
				       
				       stageItemCount++;
				       
				       System.out.println("The size of stageItemV is  " + stageItemV.length);
			
			}	        
				
				
				bufferedReaderTerrain.close();
			}
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                		levelOneStageItemsFilePath  + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + levelOneStageItemsFilePath  + "'");                  
	            // Or we could just do this: 
	            // ex.printStackTrace();
	        }
			
			
			
			
			
			

			player.addWaterHeightField(lake1.getCurHeightFieldID());
			testCreature1.addWaterHeightField(lake1.getCurHeightFieldID());

						 ArrayList<Wrap> testMaterials1 = new ArrayList<Wrap>();
						 testMaterials1.add(testWrap1);
					
					
				     
					 float[] vCoords1 = new float[]{-2.0f,-0.40f,-10.0f, 0.1f,-1.0f,-22.0f, 2.0f,-1.0f,-10.0f};
					 float[] nVecs1 = new float[]{-1.1f,0.2f,1.0f, 1.4f,1.2f,1.0f, 0.1f,1.0f,0.01f};
					 float[] tCoords1 = new float[]{0.0f,1.0f, 0.4f,0.0f, 1.0f,1.0f};
					 ArrayList<float[]> testTCoordsMaterials1 = new ArrayList<float[]>();
					 
					 testTCoordsMaterials1.add(tCoords1);
					 
					 int[] indicies1 = new int[] {0,1,2};
					 
					 
					 
					 //testCubeMap = new CubeMap(1024.0,"C:\\TestClouds.png","C:\\TestClouds.png","C:\\TestClouds.png","C:\\TestClouds.png","C:\\TestClouds.png","C:\\TestClouds.png");
					 //testCubeMap = new CubeMap(2048.0,"C:\\StarsBG.png","C:\\StarsBG.png","C:\\StarsBG.png","C:\\StarsBG.png","C:\\StarsBG.png","C:\\StarsBG.png");
					 //testCubeMap = new CubeMap(2048.0,"C:\\StarsBG2.png","C:\\StarsBG2.png","C:\\StarsBG2.png","C:\\StarsBG2.png","C:\\StarsBG2.png","C:\\StarsBG2.png");
					 //testCubeMap = new CubeMap(1024.0,"C:\\TestClouds72.png","C:\\TestClouds72.png","C:\\TestClouds72.png","C:\\TestClouds72.png","C:\\TestClouds72.png","C:\\TestClouds72.png");
					 
					 //testCubeMap = new CubeMap(2048.0,"C:\\Clouds200.png","C:\\Clouds200.png","C:\\Clouds200.png","C:\\Clouds200.png","C:\\Clouds200.png","C:\\Clouds200.png");
					 testCubeMap = new CubeMap(2048.0,"C:\\posx.png","C:\\negx.png","C:\\posy.png","C:\\negy.png","C:\\posz.png","C:\\negz.png");
					 
				
					 lake1.addReflectionsCubeMap(testCubeMap);
					 
					// float[] vCoords2 = new float[]{-10.0f,-0.4f,-43.0f, -10.0f,-0.4f,1.0f, 80.0f,-0.4f,1.0f, 80.0f,-0.4f,-43.0f};
					 float[] vCoords2 = new float[]{-1.0f,1.0f,-0.2f, -1.0f,0.0f,-0.2f, 0.0f,0.0f,-0.2f, 0.0f,1.0f,-0.2f};
					 float[] nVecs2 = new float[]{-0.2f,1.0f,-0.1f, -0.1f,0.8f,0.1f, 0.01f,2.0f,0.01f,  0.01f,2.0f,0.01f};
					 float[] tCoords2 = new float[]{0.0f,1.0f,0.0f,0.0f,1.0f,0.0f, 1.0f,1.0f};
					
					 ArrayList<float[]> testTCoordsMaterials2 = new ArrayList<float[]>();
					 testTCoordsMaterials2.add(tCoords2);
					 
					 int[] indicies2 = new int[] {0,1,2, 0,2,3};

				     //  testStageItem1Nice2 = new StageItem(12.1,-4.7,-4.2,stageItemV,stageItemN,stageItemIn,testMaterials2,testTCoordsMaterials4,testBG1,4.06f,-0.94f);

					 //testStageItem1 = new StageItem(0.2,0.1,0.1,vCoords1,nVecs1,indicies1,testMaterials1,testTCoordsMaterials1,testBG1,1.0f,0.0f,false,null, false, null, false, lake1.getCurHeightFieldID());
				     //testStageItem2 = new StageItem(-0.4f,0.1f,-0.9f,vCoords2,nVecs2,indicies2,testMaterials1,testTCoordsMaterials2,testBG1,0.3f,0.0f,false,null, false, null, false, lake1.getCurHeightFieldID());
					// stageItemsBG1.add(testStageItem1);
					 //stageItemsBG1.add(testStageItem2);
				
				//	 stageItemsBG1.add(testStageItem1Nice);
					// stageItemsBG1.add(testStageItem1Nice2);
					 
					 testBG1.setStageItems(stageItemsBG1);   
					 
				
					 
					 
					 
					 
					   


					 
					 // Dimensions of render targets
					      
					 int lowRes = 256;
					 int highRes = 1024;
					 int sMapRes = highRes;
					 
					shadowMap1Size = sMapRes;
					shadowMap2Size = sMapRes;
					shadowMap3Size = sMapRes;
					shadowMap4Size = sMapRes;
					shadowMap5Size = sMapRes;
					
			
					pointLight1CubeMapShadowsSize = 512;
					
					sceneWidth = 1920/1;
					sceneHeight = 1080/1;
					
					
					
					 //
				     // FBOs  
				     // 
					
					 shadowMap1FBO = glGenFramebuffers();
					 shadowMap2FBO = glGenFramebuffers();
					 shadowMap3FBO = glGenFramebuffers();
					 shadowMap4FBO = glGenFramebuffers();
					 shadowMap5FBO = glGenFramebuffers();
					 pointLight1CubeMapShadowsFBO = glGenFramebuffers();
					 sceneDepthFBO = glGenFramebuffers();   
					sceneFBO = glGenFramebuffers();   
					scenePreRefractionFBO = glGenFramebuffers();   
					sceneShadowsFBO = glGenFramebuffers();
				    blurGaussianShadowsHorizontalFBO = glGenFramebuffers();
				    blurGaussianShadowsVerticalFBO = glGenFramebuffers();
				    downSampledSceneAFBO = glGenFramebuffers();
				    downSampledSceneBFBO = glGenFramebuffers();
				    downSampledSceneCFBO = glGenFramebuffers();
				    // For Bloom Lighting
				    horizontallyBlurredDownSampledSceneAFBO = glGenFramebuffers();
				    horizontallyBlurredDownSampledSceneBFBO = glGenFramebuffers();
				    horizontallyBlurredDownSampledSceneCFBO = glGenFramebuffers();
				    blurredDownSampledSceneAFBO = glGenFramebuffers();
				    blurredDownSampledSceneBFBO = glGenFramebuffers();
				    blurredDownSampledSceneCFBO = glGenFramebuffers();
				    renderToMemoryFBO = glGenFramebuffers();

				    
				    
				    //
					// Render Targets
				    //
				
					scenePreRefraction = glGenTextures();                    
					prevScenePreRefraction = glGenTextures();                    
					scene = glGenTextures();                    
					sceneBright = glGenTextures();                    
			    	sceneNormals= glGenTextures();   
			    	scenePositions = glGenTextures();
			    	scenePositions2 = glGenTextures();   
			    	sceneWaterYPos = glGenTextures();   
			    	sceneShadows = glGenTextures();   
			    	sceneAmbientShadows = glGenTextures();   
			    	sceneAmbientShadowsNoise = glGenTextures();   
			    	sceneShadowRadius = glGenTextures();   
			    	sceneRefractive = glGenTextures();                    
			    	sceneRefractiveNormals = glGenTextures();                    
			    	sceneRefractivePositions = glGenTextures();                    
			    	refractiveObjects = glGenTextures();                    
			    	pointLightsColor = glGenTextures();
			       shadowMap1 = glGenTextures();
			       shadowMap2 = glGenTextures();
			       shadowMap3 = glGenTextures();
			       shadowMap4 = glGenTextures();
			       shadowMap5 = glGenTextures();
			       rotations = glGenTextures();
			       pointLight1CubeMapShadows = glGenTextures();
			       // For Bloom Lighting
			       downSampledSceneA = glGenTextures();
			       downSampledSceneB = glGenTextures();
			       downSampledSceneC = glGenTextures();
			       horizontallyBlurredDownSampledSceneA = glGenTextures();
			       horizontallyBlurredDownSampledSceneB = glGenTextures();
			       horizontallyBlurredDownSampledSceneC = glGenTextures();
			       blurredDownSampledSceneA = glGenTextures();
			       blurredDownSampledSceneB = glGenTextures();
			       blurredDownSampledSceneC = glGenTextures();
			       horizontallyBlurredScene = glGenTextures();
			       blurredScene = glGenTextures();
			       horizontallyBlurredSceneShadows = glGenTextures();
			       blurredSceneShadows = glGenTextures();
			       horizontallyBlurredSceneAmbientShadows = glGenTextures();
			       blurredSceneAmbientShadows = glGenTextures();
				    prevFrame = glGenTextures();
				       

				    
				    
				    
				    
				       
			        drawBuffersOrder = new int[]{GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2, GL_COLOR_ATTACHMENT3, GL_COLOR_ATTACHMENT4, GL_COLOR_ATTACHMENT5, GL_COLOR_ATTACHMENT6};
			        drawBuffersList = MemoryUtil.memAllocInt(7);
			        drawBuffersList.put(drawBuffersOrder);
			        drawBuffersList.flip();
			       
			        drawBuffersDepthOrder = new int[]{GL_COLOR_ATTACHMENT0};
			        drawBuffersDepthList = MemoryUtil.memAllocInt(1);
			        drawBuffersDepthList.put(drawBuffersDepthOrder);
			        drawBuffersDepthList.flip();
			       
			       IntBuffer drawBuffersGaussianBlurShadowsList = MemoryUtil.memAllocInt(2);
			       drawBuffersGaussianBlurShadowsList.put(new int[] {GL_COLOR_ATTACHMENT0,GL_COLOR_ATTACHMENT1});
			       drawBuffersGaussianBlurShadowsList.flip();
	
			       
				       
				       
				       
				       
				       
				       
				       
				       
				       
				       
				       
				       //                                  //
				       // Create Shadow Map Render Targets //
				       //                                  //
				       
				       
				       glBindTexture(GL_TEXTURE_2D,shadowMap1);
				       glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, shadowMap1Size,shadowMap1Size, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
				       //glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, shadowMap1Size,shadowMap1Size, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
					    
				       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
				       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
				       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
				       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

				       
				       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap1FBO);
				       //glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,shadowMap1,0);
				       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,shadowMap1,0);
				       
				       shadowMap1RBO = glGenRenderbuffers();
				       glBindRenderbuffer(GL_RENDERBUFFER, shadowMap1RBO); 
				       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, shadowMap1Size,shadowMap1Size);  
				       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap1FBO);
				       glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, shadowMap1RBO);
				       glBindRenderbuffer(GL_RENDERBUFFER, 0);
				        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
				            throw new Exception("Could not create FrameBuffer");
				        }
				        glBindFramebuffer(GL_FRAMEBUFFER, 0);
					       
				        
				        
				        
				        
				        
				        glBindTexture(GL_TEXTURE_2D,shadowMap2);
					    glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, shadowMap2Size,shadowMap2Size, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
				       // glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, shadowMap2Size,shadowMap2Size, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
					      
				        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
				       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
				       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
				       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
		
			
				       
				       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap2FBO);
				       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,shadowMap2,0);
				       shadowMap2RBO = glGenRenderbuffers();
				       glBindRenderbuffer(GL_RENDERBUFFER, shadowMap2RBO); 
				       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, shadowMap2Size,shadowMap2Size);  
				       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap2FBO);
				       glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, shadowMap2RBO);
				       glBindRenderbuffer(GL_RENDERBUFFER, 0);
				        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
				            throw new Exception("Could not create FrameBuffer");
				        }
				        glBindFramebuffer(GL_FRAMEBUFFER, 0);

	       
					        
					        
					        
					        
					        glBindTexture(GL_TEXTURE_2D,shadowMap3);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, shadowMap3Size,shadowMap3Size, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
   
						       
					       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap3FBO);
					       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,shadowMap3,0);
							
					       shadowMap3RBO = glGenRenderbuffers();
					       glBindRenderbuffer(GL_RENDERBUFFER, shadowMap3RBO); 
					       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, shadowMap3Size,shadowMap3Size);  

					       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap3FBO);
					       glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, shadowMap3RBO);
					       glBindRenderbuffer(GL_RENDERBUFFER, 0);
					       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
					            throw new Exception("Could not create FrameBuffer");
					        }
					        glBindFramebuffer(GL_FRAMEBUFFER, 0);


						  
					
	
							        
							        
							        
							        
					        glBindTexture(GL_TEXTURE_2D,shadowMap4);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, shadowMap4Size,shadowMap4Size, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
  
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
					       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
					       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
					       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
					       
					       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap4FBO);
					       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,shadowMap4,0);
							
					       shadowMap4RBO = glGenRenderbuffers();
					       glBindRenderbuffer(GL_RENDERBUFFER, shadowMap4RBO); 
					       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, shadowMap4Size,shadowMap4Size);  

					       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap4FBO);
					       glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, shadowMap4RBO);
					       glBindRenderbuffer(GL_RENDERBUFFER, 0);
					       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
					            throw new Exception("Could not create FrameBuffer");
					        }
					        glBindFramebuffer(GL_FRAMEBUFFER, 0);
							        
		        
								        
								        
								        
					        
					        glBindTexture(GL_TEXTURE_2D,shadowMap5);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, shadowMap5Size,shadowMap5Size, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
  
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
					       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
					       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
					       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
					       
					       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap5FBO);
					       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,shadowMap5,0);
							
					       shadowMap5RBO = glGenRenderbuffers();
					       glBindRenderbuffer(GL_RENDERBUFFER, shadowMap5RBO); 
					       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, shadowMap5Size,shadowMap5Size);  

					       glBindFramebuffer(GL_FRAMEBUFFER,shadowMap5FBO);
					       glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, shadowMap5RBO);
					       glBindRenderbuffer(GL_RENDERBUFFER, 0);
					       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
					            throw new Exception("Could not create FrameBuffer");
					        }
					        glBindFramebuffer(GL_FRAMEBUFFER, 0);
								        
								        

						        
    
								       
								       
										
										
										
										
										
										
							//			
							// Point Lights Color
							//		
							glActiveTexture(GL_TEXTURE0 + pointLightsColorTUnit);
							glBindTexture(GL_TEXTURE_2D,pointLightsColor);

							glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
							glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
							glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
							glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
							
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, screenWidth,
								    screenHeight, 0, GL_RGBA, GL_FLOAT, (ByteBuffer)null);
					        
					        glBindImageTexture(pointLightsColorTUnit, pointLightsColor, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);
							
							
										
										
								       
			        
					        
							// Describes which objects in the scene are refractive
							
							glBindTexture(GL_TEXTURE_2D,refractiveObjects);
		  			        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
					          glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, sceneWidth, sceneHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
					        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);

									    
									    
									    
									    
									    
									    
// Frame 1 (Temporal Supersampling)								        
					        glBindTexture(GL_TEXTURE_2D,scenePreRefraction);
		  			        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
					          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
					        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);

									    
// Frame 2 (Temporal Supersampling)									    
					        glBindTexture(GL_TEXTURE_2D,prevScenePreRefraction);
		  			        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
					          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
					        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);


						    glBindFramebuffer(GL_FRAMEBUFFER,scenePreRefractionFBO);
						    glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scenePreRefraction,0);
						
									    
						        
						        
					        glBindTexture(GL_TEXTURE_2D,scene);
		  			        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
					          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
					        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);

						    
					        
					        
					        glBindTexture(GL_TEXTURE_2D,sceneBright);
					        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
					          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
					        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
 
								    

						    glBindTexture(GL_TEXTURE_2D,sceneShadows);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, sceneWidth, sceneHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);


								    
								    
						    glBindTexture(GL_TEXTURE_2D,sceneNormals);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		
						    
						    
						    glBindTexture(GL_TEXTURE_2D,scenePositions);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, sceneWidth, sceneHeight, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		
//						    glGenerateMipmap(GL_TEXTURE_2D);

//							    glTexParameterf(GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f); 



						    glBindTexture(GL_TEXTURE_2D,scenePositions2);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, sceneWidth, sceneHeight, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		
						    
						    glBindTexture(GL_TEXTURE_2D,sceneWaterYPos);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
				    
						    
					        
					        glBindTexture(GL_TEXTURE_2D,sceneRefractive);
		  			        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
					          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);
					        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);

							    
						    glBindTexture(GL_TEXTURE_2D,sceneRefractiveNormals);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth, sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		
						    
						    
						    glBindTexture(GL_TEXTURE_2D,sceneRefractivePositions);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R16F, sceneWidth, sceneHeight, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		

						    
						    
						    
						    
						    
		
						    glBindTexture(GL_TEXTURE_2D,sceneShadowRadius);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, sceneWidth, sceneHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		

				
							    glBindFramebuffer(GL_FRAMEBUFFER,sceneFBO);
							glDrawBuffers(drawBuffersList);
							    
							       
					       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scene,0);
				           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,sceneBright,0);
							
							       //	       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D_MULTISAMPLE,scene,0);
								   
							       //   glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,sceneAnimatedShadows,0);
	                                    
							       
					//		       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,sceneAlbedo,0);
					
				           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT2,GL_TEXTURE_2D,sceneNormals,0);
				           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT3,GL_TEXTURE_2D,scenePositions,0);
					glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT4,GL_TEXTURE_2D,sceneShadows,0);
				           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT5,GL_TEXTURE_2D,refractiveObjects,0);
					glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT6,GL_TEXTURE_2D,sceneShadowRadius,0);
			//	           glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT5,GL_TEXTURE_2D,scenePositions2,0);
							                           	
				           
				          // glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT2,GL_TEXTURE_2D,sceneStaticShadows,0);
				          // glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT3,GL_TEXTURE_2D,sceneAnimatedShadows,0);
                           
					
					       sceneRBO = glGenRenderbuffers();
					       glBindRenderbuffer(GL_RENDERBUFFER, sceneRBO); 
					       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, sceneWidth, sceneHeight);  
					   //    glRenderbufferStorageMultisample(GL_RENDERBUFFER, 8, GL_DEPTH_COMPONENT32F, sceneWidth,sceneHeight);  
					       //glBindRenderbuffer(GL_RENDERBUFFER, 0);
					       
					       glBindFramebuffer(GL_FRAMEBUFFER,sceneFBO);
					       
					       glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, sceneRBO);


							
							       
							       //glDrawBuffer(GL_NONE);
							       // glReadBuffer(GL_NONE);

							        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
							            throw new Exception("Could not create FrameBuffer");
							        }

							        // Unbind

								       glBindRenderbuffer(GL_RENDERBUFFER, 0); 
										
							        glBindFramebuffer(GL_FRAMEBUFFER, 0);


							        
				        
				        
				        // FBO for SSAO Value							        
				        
					       glBindFramebuffer(GL_FRAMEBUFFER,sceneShadowsFBO);

						    glBindTexture(GL_TEXTURE_2D,sceneAmbientShadows);
					        glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, 1920, 1080, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
					        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
						    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);

						    //glGenerateMipmap(GL_TEXTURE_2D);

						    glBindTexture(GL_TEXTURE_2D,sceneAmbientShadowsNoise);
					        
									    
						    //glPixelStorei(GL_UNPACK_ALIGNMENT,1);
					        //glTexImage3D(GL_TEXTURE_3D, 0, GL_R32F, 32,
							//	    32, 32, 0, GL_RED, GL_FLOAT, rotationsBuffer);
								       

					        // 4 x 4 noise texture
					        
						    int ambientShadowNoiseWidth = 3;
						    int ambientShadowNoiseHeight = 3;
						
						    
						    
					        Vector3f[] noiseValues = new Vector3f[ambientShadowNoiseWidth*ambientShadowNoiseHeight];
					        for (int u = 0; u < ambientShadowNoiseWidth*ambientShadowNoiseHeight; u++) {
					        	
					        	// Hemisphere
					        	//Vector3f noiseVec = new Vector3f( 2.0f*(float)Math.random() - 1.0f, 2.0f*(float)Math.random() - 1.0f, 0.0f);
					        	
					        	//Sphere
					        	Vector3f noiseVec = new Vector3f( 2.0f*(float)Math.random() - 1.0f, 2.0f*(float)Math.random() - 1.0f, 2.0f*(float)Math.random() - 1.0f);
					        	
					        	noiseValues[u] = noiseVec;
					        }
					        
					        
	/*							        
									    
									    
								        Vector3f[] noiseValues = new Vector3f[]{
								        	new	Vector3f(+0.766044f, +0.642788f, 0.0f), new Vector3f(+0.173648f, +0.984808f, 0.0f), new Vector3f(-0.5f, +0.866025f,0.0f),
								        	new	Vector3f(-0.939693f, +0.342020f, 0.0f), new Vector3f(-0.939693f, -0.342020f, 0.0f), new Vector3f(-0.5f, -0.866025f,0.0f),
								        	new	Vector3f(+0.173648f, -0.984808f, 0.0f), new Vector3f(+0.766044f, -0.642788f, 0.0f), new Vector3f(+1.0f, +0.000000f,0.0f)
					};

*/



								        FloatBuffer noiseBuffer = BufferUtils.createFloatBuffer(3*ambientShadowNoiseWidth*ambientShadowNoiseHeight);
									    for (int u = 0; u < ambientShadowNoiseWidth*ambientShadowNoiseHeight; u++) {
									    	noiseValues[u].get(3*u,noiseBuffer);
								        }
									    
									    
									    
									    float[] testSet = new float[] {0.25f,0.50f,0.75f,1.0f};
									    FloatBuffer testFBuffer = BufferUtils.createFloatBuffer(4);
									    testFBuffer.put(testSet);
									    testFBuffer.flip();
									    for (int l = 0; l < 4; l++ ) {
									    System.out.println("Element " + l + " is " + testFBuffer.get() );
									    };
									    
									    //System.exit(0);
									    
									    
									    
								        noiseBuffer.flip();
								        
									    
									    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, ambientShadowNoiseWidth, ambientShadowNoiseHeight, 0, GL_RGB, GL_FLOAT, noiseBuffer);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
									    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
									    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);


										//glDrawBuffers(drawBuffersShadowsList);
									    
									    glBindTexture(GL_TEXTURE_2D,sceneAmbientShadows);
								        
								        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,sceneAmbientShadows,0);
								        //glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,sceneShadowRadius,0);

								        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
								            throw new Exception("Could not create FrameBuffer");
								        }


								        glBindFramebuffer(GL_FRAMEBUFFER,0);
									    glBindRenderbuffer(GL_RENDERBUFFER, 0); 

								        
								        
								        
								        
								        
								        
								        
								        
								        
								        
								        
								        
								        // FBO For Depth Pass
								        
									       glBindFramebuffer(GL_FRAMEBUFFER,sceneDepthFBO);
									       //glDrawBuffers(drawBuffersShadowsList);
											//glDrawBuffers(drawBuffersDepthList);
										    
									        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scenePositions2,0);
									        //glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,sceneShadowRadius,0);

											
										       sceneDepthRBO = glGenRenderbuffers();
										       glBindRenderbuffer(GL_RENDERBUFFER, sceneDepthRBO); 
										       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, sceneWidth, sceneHeight);  
										   //    glRenderbufferStorageMultisample(GL_RENDERBUFFER, 8, GL_DEPTH_COMPONENT32F, sceneWidth,sceneHeight);  
										       //glBindRenderbuffer(GL_RENDERBUFFER, 0);
										       
										       glBindFramebuffer(GL_FRAMEBUFFER,sceneDepthFBO);
					//					        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scenePositions2,0);

										       glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, sceneDepthRBO);
										       //glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scenePositions,0);
												 
				//						        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,scenePositions2,0);

										        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
										            throw new Exception("Could not create FrameBuffer");
										        }

											    glBindRenderbuffer(GL_RENDERBUFFER, 0); 
										        glBindFramebuffer(GL_FRAMEBUFFER, 0);

										        	    
							        
							        
							        // TempFiltering
						      
							        
							        glBindTexture(GL_TEXTURE_2D, prevFrame);
				  			        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
							          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sceneWidth, sceneHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
							        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);
							        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
								    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
								    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
								    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

		
									
								    glBindFramebuffer(GL_FRAMEBUFFER,renderToMemoryFBO);
								//glDrawBuffers(drawBuffersList);
								    
								       
								       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,prevFrame,0);
						
							        
							        
							        
							        // Rotations in 3D space for Poisson Disc sampling
							        


							        
							        try (MemoryStack stack = MemoryStack.stackPush()) {
							    	
							        
							        	
							        	int rotationsSize = rotationsDimension*rotationsDimension*rotationsDimension;
							        	
							        	rotationValues = new float[1*rotationsSize];
							        	
							        	for (int t = 0; t < rotationsSize; t = t + 1) {
							        		float rnd = (float)(2.0*Math.PI*Math.random());
							        		rotationValues[t] = rnd;
							        	}
							        	
							        }
				         
							        ByteBuffer buffer32 = ByteBuffer.allocateDirect(1*4*rotationsDimension*rotationsDimension*rotationsDimension).order(ByteOrder.nativeOrder());
							      System.out.println("BUFFER32 CAPACITY IS " + buffer32.capacity());
							      
							      FloatBuffer rotationsBuffer = buffer32.asFloatBuffer();
							        rotationsBuffer.put(rotationValues);
							        rotationsBuffer.flip();
							         
							        
							        
							        
							        
							        
							         System.out.println("RotationValues has a length of " + rotationValues.length);
							         System.out.println("RotationsBuffer has a capacity of " + rotationsBuffer.capacity());
							         
			
							         glEnable(GL_TEXTURE_3D);
							        glBindTexture(GL_TEXTURE_3D,rotations);
			
							        System.out.println("Established");
									 			 
							        glTexParameteri(GL_TEXTURE_3D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
								       glTexParameteri(GL_TEXTURE_3D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
								       glTexParameteri(GL_TEXTURE_3D,GL_TEXTURE_WRAP_S,GL_REPEAT);
								       glTexParameteri(GL_TEXTURE_3D,GL_TEXTURE_WRAP_R,GL_REPEAT);
										glTexParameteri(GL_TEXTURE_3D,GL_TEXTURE_WRAP_T,GL_REPEAT);
								       //glTexParameterf(GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f); 
					
										glPixelStorei(GL_UNPACK_ALIGNMENT,1);
								        glTexImage3D(GL_TEXTURE_3D, 0, GL_R32F, 32,
											    32, 32, 0, GL_RED, GL_FLOAT, rotationsBuffer);
											       
								       
								       
							
								        
								        
					
								        
								        
								        
								        
								        
								        
								        
								        // Point light 1 cube map shadows
								        
								        glBindTexture(GL_TEXTURE_CUBE_MAP,pointLight1CubeMapShadows);
					  			        //glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,scene);
//								          glTexImage2D(GL_TEXTURE_CUBE_MAP, 0, GL_R32F, pointLight1CubeMapShadowsSize, pointLight1CubeMapShadowsSize, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
								        //glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 8, GL_RGB, sceneWidth, sceneHeight, true);

								        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_BASE_LEVEL, 0);
								        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAX_LEVEL, 0);
								        glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
									    glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
									    //glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
									    //glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
									    
									    for (int k = 0; k < 6; k++) {
								          glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + k, 0, GL_R32F, pointLight1CubeMapShadowsSize, pointLight1CubeMapShadowsSize, 0, GL_RED, GL_FLOAT, (ByteBuffer)null);
									    }
									       glBindFramebuffer(GL_DRAW_FRAMEBUFFER,pointLight1CubeMapShadowsFBO);
									    for (int k = 0; k < 6; k++) {
									       glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_POSITIVE_X + k,pointLight1CubeMapShadows,0);
									    }
									       pointLight1CubeMapShadowsRBO = glGenRenderbuffers();
									       glBindRenderbuffer(GL_RENDERBUFFER, pointLight1CubeMapShadowsRBO); 
									       glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, pointLight1CubeMapShadowsSize,pointLight1CubeMapShadowsSize);  

									       glBindFramebuffer(GL_DRAW_FRAMEBUFFER,pointLight1CubeMapShadowsFBO);
									       glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, pointLight1CubeMapShadowsRBO);
									       glBindRenderbuffer(GL_RENDERBUFFER, 0);
									       if (glCheckFramebufferStatus(GL_DRAW_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									        }
							
						
								        
								        
								        
								        
								        
								        
								        ////////////////////////////
									    //     FBOs for Bloom     //
									    ////////////////////////////
								        

										   glBindTexture(GL_TEXTURE_2D,horizontallyBlurredScene);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth,
								        		sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									
										   glBindTexture(GL_TEXTURE_2D,blurredScene);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth,
								        		sceneHeight, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       

									       
									       
										   glBindTexture(GL_TEXTURE_2D,downSampledSceneA);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/2,
								        		sceneHeight/2, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,downSampledSceneAFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,downSampledSceneA,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);
									       
										   glBindTexture(GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneA);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/2,
								        		sceneHeight/2, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,horizontallyBlurredDownSampledSceneAFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneA,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);

										   glBindTexture(GL_TEXTURE_2D,blurredDownSampledSceneA);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/2,
								        		sceneHeight/2, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,blurredDownSampledSceneAFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredDownSampledSceneA,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);
								       

									            

										   glBindTexture(GL_TEXTURE_2D,downSampledSceneB);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/4,
								        		sceneHeight/4, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,downSampledSceneBFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,downSampledSceneB,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);
											       
										   glBindTexture(GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneB);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/4,
								        		sceneHeight/4, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,horizontallyBlurredDownSampledSceneBFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneB,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);
											            
										   glBindTexture(GL_TEXTURE_2D,blurredDownSampledSceneB);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/4,
								        		sceneHeight/4, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,blurredDownSampledSceneBFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredDownSampledSceneB,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);

											       
											       

										   glBindTexture(GL_TEXTURE_2D,downSampledSceneC);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/8,
								        		sceneHeight/8, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,downSampledSceneCFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,downSampledSceneC,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);
											
										   glBindTexture(GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneC);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/8,
								        		sceneHeight/8, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,horizontallyBlurredDownSampledSceneCFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredDownSampledSceneC,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);
		            
											            
										   glBindTexture(GL_TEXTURE_2D,blurredDownSampledSceneC);
										   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, sceneWidth/8,
								        		sceneHeight/8, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
									       glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       glBindFramebuffer(GL_FRAMEBUFFER,blurredDownSampledSceneCFBO);
									       glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredDownSampledSceneC,0);
									       if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
									            throw new Exception("Could not create FrameBuffer");
									       }
									            glBindFramebuffer(GL_FRAMEBUFFER, 0);




		       
						       
						       

		       
										/////////////////////////////////////////////////////////////////
									    // FBO for Blurred Scene Shadows and Blurred Ambient Occlusion //
									    /////////////////////////////////////////////////////////////////
										       
										       
								        glBindTexture(GL_TEXTURE_2D,horizontallyBlurredSceneShadows);
								        glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, sceneWidth,
									    		sceneHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       
								        glBindTexture(GL_TEXTURE_2D,blurredSceneShadows);
								        glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, sceneWidth,
								        		sceneHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);


								        glBindTexture(GL_TEXTURE_2D,horizontallyBlurredSceneAmbientShadows);
								        glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, sceneWidth,
									    		sceneHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
									       
								        glBindTexture(GL_TEXTURE_2D,blurredSceneAmbientShadows);
								        glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, sceneWidth,
								        		sceneHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer)null);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
								        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

		       


							        
							        
							        // 
								    // FBO for Blurred Shadows and SSAO Values							        
 							        //
								        
							        glBindFramebuffer(GL_FRAMEBUFFER,blurGaussianShadowsHorizontalFBO);
							        glDrawBuffers(drawBuffersGaussianBlurShadowsList );
							        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,horizontallyBlurredSceneShadows,0);
							        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,horizontallyBlurredSceneAmbientShadows,0);

							        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
							        	System.exit(0);
							        }

							        // Unbind
							        glBindFramebuffer(GL_FRAMEBUFFER, 0);

							        
							        
							        glBindFramebuffer(GL_FRAMEBUFFER,blurGaussianShadowsVerticalFBO);
							        glDrawBuffers(drawBuffersGaussianBlurShadowsList );
							        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,blurredSceneShadows,0);
							        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT1,GL_TEXTURE_2D,blurredSceneAmbientShadows,0);

							        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
							        	System.exit(0);

							        }

							        // Unbind
							        glBindFramebuffer(GL_FRAMEBUFFER, 0);

							        
							        
							        

							        
							        
							        
							        
							        
							        
					/*
					 * * * * * * * * * * *
					 * * * * * * * * * * *
					 * LOADING INTERVALS *
					 * * * * * * * * * * *
					 * * * * * * * * * * *	        			        
					 */
					 
						Interval.landingPrecision = this.landingPrecision;

				        String worldOneTerrain = "C:/StageItems/WORLD_INTERVAL_DATA_1.txt";
				        
				        // These lists are used for the sake of loading an Interval's neighboring intervals
				        ArrayList<Interval> allIntervals = new ArrayList<Interval>();
				        ArrayList<String[]> intervalNeighborIndices = new ArrayList<String[]>();

				        
				        String line = null;

				        try {
				            
				        	
				        	
				        	// FileReader reads text files in the default encoding.
				            FileReader fileReader = 
				                new FileReader(worldOneTerrain);

				            // Always wrap FileReader in BufferedReader.
				            BufferedReader bufferedReader = 
				                new BufferedReader(fileReader);


				            int currentIntervalType = 0;
				            
				            bufferedReader.readLine();
				            bufferedReader.readLine();
				            
				            while((line = bufferedReader.readLine()) != null) {
					            
					            if (line.equals("TERRAIN")) {
					            	currentIntervalType = 1;
					            	continue;
					            }
					            else if (line.equals("WALLS")) {
					            	currentIntervalType = 2;
					            	continue;
					            }
					            else if (line.equals("CEILINGS")) {
					            	currentIntervalType = 3;
					            	continue;
					            }
					            
					            
					         // Terrain file line format :  x1, x2, sP, eP, z1, z2, leftCliff, rightCliff
					            String[] intervalLine = line.split(" ");
					            if (intervalLine.length !=  8) {
					            	System.out.println("Interval line does not have the proper number of inputs");
					            	System.exit(0);
					            }
					            
					            
					            
					            Interval interval = new Interval(Double.parseDouble(intervalLine[0]),Double.parseDouble(intervalLine[1]),
					            		Double.parseDouble(intervalLine[2]),Double.parseDouble(intervalLine[3]),
					            		Double.parseDouble(intervalLine[4]),Double.parseDouble(intervalLine[5]), 
					            		null, null, currentIntervalType
					            		);
					            if (currentIntervalType == 1) {
					            	testBG1.addInterval(interval);
					            }
					            else if (currentIntervalType == 2) {
					            	testBG1.addSlopedWall(interval);
					            }
					            else if (currentIntervalType == 3) {
					            	testBG1.addCeiling(interval);
					            }
					            
					            allIntervals.add(interval);
					            intervalNeighborIndices.add(new String[] {intervalLine[6], intervalLine[7]});
					            
					        
					           
				            }   

				            System.out.println("Terrain neighboring indices");
				            for (int g = 0; g < intervalNeighborIndices.size(); g++) {
				            	System.out.println("neighbors: "+intervalNeighborIndices.get(g)[0]+" and "+intervalNeighborIndices.get(g)[1]);
				            	char intervalLeftNeighborType = intervalNeighborIndices.get(g)[0].charAt(0);
				            	int intervalLeftNeighborIndex = Character.getNumericValue(intervalNeighborIndices.get(g)[0].charAt(1));
				            	char intervalRightNeighborType = intervalNeighborIndices.get(g)[1].charAt(0);
				            	int intervalRightNeighborIndex = Character.getNumericValue(intervalNeighborIndices.get(g)[1].charAt(1));
				            	
				            	if (Character.compare(intervalLeftNeighborType, 't') == 0) {
				            		allIntervals.get(g).setLeftInterval(testBG1.getIntervals().get(intervalLeftNeighborIndex));
				            	}
				            	else if (Character.compare(intervalLeftNeighborType, 'w') == 0) {
				            		allIntervals.get(g).setLeftInterval(testBG1.getSlopedWalls().get(intervalLeftNeighborIndex));
				            	}
				            	else if (Character.compare(intervalLeftNeighborType, 'c') == 0) {
				            		allIntervals.get(g).setLeftInterval(testBG1.getCeilings().get(intervalLeftNeighborIndex));
				            	}
				            	
				            	if (Character.compare(intervalRightNeighborType, 't') == 0) {
				            		allIntervals.get(g).setRightInterval(testBG1.getIntervals().get(intervalRightNeighborIndex));
				            	}
				            	else if (Character.compare(intervalRightNeighborType, 'w') == 0) {
				            		allIntervals.get(g).setRightInterval(testBG1.getSlopedWalls().get(intervalRightNeighborIndex));
				            	}
				            	else if (Character.compare(intervalRightNeighborType, 'c') == 0) {
				            		allIntervals.get(g).setRightInterval(testBG1.getCeilings().get(intervalRightNeighborIndex));
				            	}
				            	
				            }
				            
				            System.out.println("Finished loading intervals...");
				            for (int j = 0; j < testBG1.getIntervals().size(); j++) {
				            	System.out.println("interval left neighbor : "+testBG1.getIntervals().get(j).getLeftInterval());
				            	System.out.println("interval right neighbor : "+testBG1.getIntervals().get(j).getRightInterval());
				            }
				            
				            
				            // Always close files.
				            bufferedReader.close();    
				            
				            
				        }
				        catch(FileNotFoundException ex) {
				            System.out.println(
				                "Unable to open file '" + 
				                worldOneTerrain + "'");                
				        }
				        catch(IOException ex) {
				            System.out.println(
				                "Error reading file '" 
				                + worldOneTerrain + "'");                  
				            // Or we could just do this: 
				            // ex.printStackTrace();
				        }
					 
				       
				        
   
				
	 
				   DeadEndInterval lde1 = new DeadEndInterval(-20.0,100.0,-10.0,100.0,0.0,0.0,testBG1,1);
			//	    DeadEndInterval rde1 = new DeadEndInterval(-20.0,100.0,-10.0,100.0,0.0,0.0,testBG1,2);
				    
				    ArrayList<DeadEndInterval> bg1DEs = new ArrayList<DeadEndInterval>();
				    bg1DEs.add(lde1);
				//    bg1DEs.add(rde1);
				    
				    testBG1.setDeadEndIntervals(bg1DEs);
				    
				
					
			/*	lake1.addDrop(new float[]{0.0f,0.0f},01.24f, 0.021f);
					lake1.addDrop(new float[]{-0.4f,0.2f},0.9f, 0.012f);
					lake1.addDrop(new float[]{0.1f,0.7f},1.0f, 0.01f);
				lake1.addDrop(new float[]{-0.82f,-0.72f},0.7f, 0.011f);  // Drop Strength Was 3
		lake1.addDrop(new float[]{0.1f,0.7f},0.7f, 0.02f);
		lake1.addDrop(new float[]{0.28f,0.08259f},0.2f, 0.02f);
		lake1.addDrop(new float[]{0.27872f,0.0954f},0.40f, 0.02f);
		lake1.addDrop(new float[]{0.289872f,0.07954f},0.1f, 0.0162f);
		
		lake1.addDrop(new float[]{-0.6189872f,0.007954f},0.1f, 0.01962f);
		lake1.addDrop(new float[]{-0.789872f,0.77954f},0.2f, 0.0162f);
		lake1.addDrop(new float[]{-0.089872f,0.27954f},0.02f, 0.012f);
		lake1.addDrop(new float[]{-0.3489872f,-0.77954f},0.07f, 0.0062f);
		lake1.addDrop(new float[]{-0.289872f,-0.07954f},0.001f, 0.062f);
		lake1.addDrop(new float[]{-0.959872f,-0.47954f},0.01f, 0.0262f);
							
		
		for (int k = 0; k <= 10; k++ ) {
		
			double randX = 2.0*Math.random() - 1.0;
			double randmY = 2.0*Math.random() - 1.0;

			lake1.addDrop(new float[]{(float)randX,(float)randmY},0.058f, 0.000262f);

		}
		*/
		glfwSwapInterval(0);
		
							//		for (int a = 0; a < 10000; a++) {
				//	lake1.update();
					//}
			              
									glfwSwapInterval(1);
									
                  ArrayList<Water> testBG1BodiesOfWater = new ArrayList<Water>();
                   testBG1BodiesOfWater.add(lake1);
                   testBG1.setBodiesOfWater(testBG1BodiesOfWater);
                   
                   
					//	ArrayList<DependentScrollingBackground> followingZones = new ArrayList<DependentScrollingBackground>();
						
						 player.setBG(testBG1);
						 testCreature1.setBG(testBG1);
						 
						
						zone = new WorldUpdater(2,player,landingPrecision);
					
						ArrayList<Background> zoneBGs = new ArrayList<Background>();
						zoneBGs.add(testBG1);
						zone.setBackgrounds(zoneBGs);
						zone.setCreatures(testCreatures);
						
						sequence = "Move To";
						currentMoveToType = "BIn";
					}
				}
				else if (currentMoveToType == "Slide") {
					
				}
				
				
				
			}


			
			
			
			
			
			
			
			// Poll for window events. The key callback above will only be
			// invoked during this call.
				glfwPollEvents();                                                                        // Check for input


			}
		}
		
		private float snap(float f, float multiple) {
			return Math.round(f/multiple)*multiple;
		}
		
		public Vector3f[] generateSphereOfPoints(int numberOfSamples) throws IOException {
			
			Vector3f[] sphere = new Vector3f[numberOfSamples];
			
			
			for (int q = 0; q < numberOfSamples; q++) {
				
				// Hemisphere
				//Vector3f rndVec = new Vector3f(2.0f*(float)Math.random() - 1.0f, 2.0f*(float)Math.random() - 1.0f, (float)Math.random());
				
				// Sphere
				Vector3f rndVec = new Vector3f(2.0f*(float)Math.random() - 1.0f, 2.0f*(float)Math.random() - 1.0f, 2.0f*(float)Math.random() - 1.0f);
				
				rndVec.normalize(rndVec);
				rndVec.mul((float)Math.random(), rndVec);
				float scale = (float)q/(float)numberOfSamples;
				scale = 0.10f + ((float)Math.pow(scale, 2.0))*0.90f;
				rndVec.mul(scale,rndVec);
				sphere[q] = rndVec;
				
			}
			
			
			
			
			// Write Sphere Points to a file
			
			
			

			String spherePointsFileName = "C:/StageItems/SpherePoints";
	        File spherePoints = new File(spherePointsFileName + ".txt");
	        spherePoints.createNewFile();
	        BufferedWriter writer4 = new BufferedWriter(new FileWriter(spherePoints));
	        
	        for (int s = 0; s < numberOfSamples; s++) {
	        	
	        	writer4.write(Float.toString(sphere[s].x) + " " + Float.toString(sphere[s].y) + " " + Float.toString(sphere[s].z) ); ;
	        	writer4.write(System.lineSeparator());
	        }
	        
	        writer4.close();
	        			
			
			return sphere;
			
			
		}

		
		
		public static void main(String[] args) throws Exception {
			new EngineMain().run();
		}

}