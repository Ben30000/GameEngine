
import java.util.ArrayList;


import org.joml.Matrix4f;
import org.joml.Matrix3f;
import org.joml.Matrix4d;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector3d;
import org.joml.Quaternionf;
import org.joml.Vector2d;
import org.joml.Vector2f;


import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;







// package org.joml.lwjgl; 
 
 





public class Player implements Entity {

	private double x;
	private double y;
	private double z;
	Vector2f[] feetPositions;
	Vector2d motionVector;
	private double previousX;
	private double previousY;
	private double currentX;
	private double currentY;
	private double dx;
	private double dy;
	private double height;
	private double width;
	private double feetWidth;
	private int feetHeight;
	private int feetOSet;
	private boolean freeToMove;
	private boolean alreadyMoved;
	private boolean jumping;
	private boolean falling;
	private boolean leftWPressed;
	private boolean rightWPressed;
	private int projLocation;
	private int translationMatrixLocation,directionalLightTypeLocation,directionalLightDirectionLocation,directionalLightColorLocation,directionalLightAmountLocation;
	private int uMVPLocation, screenBrightnessLocation;
	private boolean goingLeft, goingRight;
	private double targetHorizontalThresholdLeft, targetHorizontalThresholdRight;
	private double slopedWallAngle;
	private double initialFallVelocity, fallVelocity;
	
	private double velocityX, velocityY;
	
	private float prevLSpacePosX = 100.0f;
	
	private boolean pointingLeft, pointingRight;
	private double landingPrecision = 0.00001;
	private double t1, t2;  // t1 = b vertical camera thold, t2 = t vertical camera thold
	private double verticalPanDistance;
	private double vPanX,vPanY;
	
	private boolean withinCameraInterval;
	private boolean panningU, panningD, keepMovingBG;

	private String facingDirection;
	
	private Background b;
	private float[] vertices = new float[]{
			

		    -0.5f, -0.5f, -0.5f,  
		     0.5f, -0.5f, -0.5f,   
		     0.5f,  0.5f, -0.5f,   
		     0.5f,  0.5f, -0.5f,   
		    -0.5f,  0.5f, -0.5f,   
		    -0.5f, -0.5f, -0.5f,  

		    -0.5f, -0.5f,  0.5f,  
		     0.5f, -0.5f,  0.5f,  
		     0.5f,  0.5f,  0.5f,  
		     0.5f,  0.5f,  0.5f,  
		    -0.5f,  0.5f,  0.5f,  
		    -0.5f, -0.5f,  0.5f,  

		    -0.5f,  0.5f,  0.5f, 
		    -0.5f,  0.5f, -0.5f, 
		    -0.5f, -0.5f, -0.5f, 
		    -0.5f, -0.5f, -0.5f, 
		    -0.5f, -0.5f,  0.5f, 
		    -0.5f,  0.5f,  0.5f, 

		     0.5f,  0.5f,  0.5f,  
		     0.5f,  0.5f, -0.5f,  
		     0.5f, -0.5f, -0.5f,  
		     0.5f, -0.5f, -0.5f,  
		     0.5f, -0.5f,  0.5f,  
		     0.5f,  0.5f,  0.5f,  

		    -0.5f, -0.5f, -0.5f,  
		     0.5f, -0.5f, -0.5f,  
		     0.5f, -0.5f,  0.5f,  
		     0.5f, -0.5f,  0.5f,  
		    -0.5f, -0.5f,  0.5f,  
		    -0.5f, -0.5f, -0.5f,  

		    -0.5f,  0.5f, -0.5f,  
		     0.5f,  0.5f, -0.5f,  
		     0.5f,  0.5f,  0.5f,  
		     0.5f,  0.5f,  0.5f,  
		    -0.5f,  0.5f,  0.5f,  
		    -0.5f,  0.5f, -0.5f

    
	};
	
	private int programId;
	private int vShaderId;
	
	
	
	
	
	private int fShaderId;
	
	
	private int vaoId;
	private int vboId;
	private int vertexCount;
	private int shadowProgramId;
	private int shadowLightMatrixLocation;
	private int shadowFShaderId;
	private int shadowVShaderId;
	private int lightMatrix1Location;
	private int lightMatrix2Location;
	private int lightMatrix3Location;
	private int shadowMap1Location;
	private int shadowMap2Location;
	private int shadowMap3Location;
	private int temporalShadowProgramId;
	private int temporalShadowVShaderId;
	private int temporalShadowFShaderId;
	private int temporalShadowLightMatrixLocation;
	private int temporalShaderPreviousShadowMapSamplerLocation;
	private int shadowMapAnimated2Location;
	private int shadowMapAnimated1Location;
	private int shadowMapAnimated3Location;
	private int lightMatrix4Location;
	private int shadowMap4Location;
	private int waterBody1PosLocation;
	private int waterBody1DimensionsLocation;
	private int waterBody1ScaleLocation;
	private int waterBody1Location;
	private int[] waterHeightFields;
	private int uWorldLocation;
	private int lightMatrix5Location;
	private int shadowMap5Location;
	private double fallTime;
	private double jumpTime;
	private double depthShift;
	private int lightOrthoMatrixLocation;
	private int worldModelMatrixLocation;
	private float scale;
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	private int modelMatrixUniformLocation;
	private int modelWorldMatrixUniformLocation;
	private int modelWorldViewMatrixUniformLocation;
	private int modelWorldViewProjectionMatrixUniformLocation;
	private int viewProjectionMatrixUniformLocation;
	private int viewMatrixUniformLocation;
	private int normalMatrixUniformLocation;
	private int cameraPositionUniformLocation;
	private boolean slidingOnWall;
	
	
	
	
	
	public Player(Vector2f[] feetPositions) throws Exception {
		
		this.scale = 0.350f;
		this.rotationX = 0.0f;
		this.rotationY = 0.0f;
		this.rotationZ = 0.0f;
		this.facingDirection = "right";
		this.motionVector = new Vector2d(0.0,0.0);
		this.slopedWallAngle = 0.0;
		x = -7.0;
		y = -12.27;
		z = -5.0;
		this.height = 0.350;
		width = 0.50;
		feetWidth = 0.30;
		feetOSet = 10;
		feetHeight = 5;
		dx = 0;
		dy = 0;
		x = 10.765323597192765 + 0.5*this.feetWidth;
		this.initialFallVelocity = 1.66;
		this.initialFallVelocity = 0.0066;
		
		this.feetPositions = feetPositions;
		
		this.jumpTime = 0.48;
		this.fallTime = 0.0;
		this.goingRight = false;
		
		this.depthShift = 0.01;
		
		this.goingLeft = false;
		

		
		this.pointingLeft = true;
		this.pointingRight = true;
		this.pointingLeft = false;
		this.pointingRight = false;
		
		waterHeightFields = new int[10];
	//	this.b = b;
		
		targetHorizontalThresholdLeft = -1.2;
		targetHorizontalThresholdRight = 1.2;
		
		
		targetHorizontalThresholdLeft = 0.608;
		targetHorizontalThresholdRight = 0.608;
		
		rightWPressed = true;
		leftWPressed = false;
		System.out.println("OpenGL Version " + glGetString(GL_VERSION));
		
		
		
		this.t1 = -2.9;
		this.t2 = 3.7;
		
		
		
		keepMovingBG = false;
		
		
	
		
		
		
		
		
		
		
		String vShaderCode = "#version 430\n "+

"layout (location=0) in vec3 position;\n"+

"out vec4 fPosLight1;"+
"out vec4 fPosLight2;"+
"out vec4 fPosLight3;"+
"out vec4 fPosLight4;"+
"out vec4 fPosLight5;"+

"out vec4 fPosMVP;"+
"out vec3 fPosWorld;"+
"out vec3 fPosCamera;"+

// Main transformations/positions
"uniform mat4 modelMatrix;"+
"uniform mat4 modelWorldMatrix\n;"+
"uniform mat4 modelWorldViewMatrix;\n"+
"uniform mat4 modelWorldViewProjectionMatrix;\n"+
"uniform mat4 normalMatrix\n;"+
"uniform vec3 cameraPosition;"+

// For calculating shadows
"uniform mat4 lightMatrix1;"+
"uniform mat4 lightMatrix2;"+
"uniform mat4 lightMatrix3;"+
"uniform mat4 lightMatrix4;"+
"uniform mat4 lightMatrix5;"+

"void main()\n"+
"{\n"+
"	fPosMVP = (modelWorldViewProjectionMatrix*vec4(position,1.0));"+
"	fPosWorld = (modelWorldMatrix*vec4(position,1.0)).xyz;"+
"	fPosCamera = (modelWorldViewMatrix*vec4(position,1.0)).xyz;"+
"	fPosLight1 = lightMatrix1*vec4(position,1.0);\n"+
"	fPosLight2 = lightMatrix2*vec4(position,1.0);\n"+
"	fPosLight3 = lightMatrix3*vec4(position,1.0);\n"+
"	fPosLight4 = lightMatrix4*vec4(position,1.0);\n"+
"	fPosLight5 = lightMatrix5*vec4(position,1.0);\n"+

 "   gl_Position = fPosMVP;\n" +
"}";
		
		
		
		
		
		
		
		
		
		
		
		String fShaderCode = "#version 430\n "+


"in vec4 fPosMVP;"+
"in vec3 fPosWorld;"+
"in vec3 fPosCamera;"+
"in vec4 fPosLight1;"+
"in vec4 fPosLight2;"+
"in vec4 fPosLight3;"+
"in vec4 fPosLight4;"+
"in vec4 fPosLight5;"+

 " layout (location = 0) out vec4 albedoColor;\n"+
"layout (location = 1) out vec4 colorBright;\n"+
"layout (location = 2) out vec4 normal;\n"+
"layout (location = 3) out vec4 position;\n"+
"layout (location = 4) out vec4 sceneShadowAmount;\n"+
"layout (location = 5) out vec4 refractiveObject;\n"+
//"layout (location = 6) out vec4 shadowBlurRadius;\n"+

//"layout (location = 0) out vec4 diffuseMapColor;\n"+
//"layout (location = 1) out vec2 diffuseAmountAndShadowAmount;\n"+
//"layout (location = 2) out float shadowAnimatedAmount;\n"+


"struct WaterBody{"
+ "vec3 pos;"
+ "vec2 dimensions;"
+ "vec2 scale;"
+ "};"+
"uniform sampler2D waterBody1HeightField;"+
"uniform WaterBody waterBody1;"+


"struct LightSource{"
+ "int type;"
+ "vec3 directionOrLocation;"
+ "vec3 color;"
+ "float amount;"
+ "};"
+

"uniform sampler2D shadowMap1;"+
"uniform sampler2D shadowMap2;"+
"uniform sampler2D shadowMap3;"+
"uniform sampler2D shadowMap4;"+
"uniform sampler2D shadowMap5;"+

/*
"uniform sampler2D shadowMapAnimated1;"+
"uniform sampler2D shadowMapAnimated2;"+
"uniform sampler2D shadowMapAnimated3;"+
*/
"uniform LightSource directionalLight;"+
//"uniform float screenBrightness;"+

"void main()\n"+
"{\n"+




"float shadow = 0.0;"+
"float shadowAnimated = 0.0;"+

"								float bias = 0.0125;"+
//"float r = 80.0;"+



//" float biasDot = clamp(dot(1.0*normalize(-1.0*directionalLight.directionOrLocation), 1.0*normalize(1.0*fNVec)),0.0,1.0);"+

"float biasMagnitude = 0.048;"+
/*
" if (testSearchRadius == 1){"+
"   biasMagnitude = 0.04;"+
" }"+
"else {"+
"      biasMagnitude = 0.04;"+
"}"+
*/
//" float bias = max( biasMagnitude * (  1.0 - pow(biasDot,1.0)) , 0.005f);" +
//"float acosOfBiasDot = acos(biasDot);"+
//" float bias = 0.005*tan(acosOfBiasDot);"+
//" bias = clamp(bias,0.0,0.05);"+
//" bias = 0.038;"+
"float r = 80.0;"+



"vec2 poissonDisk[16] = vec2[]("+ 
"vec2( -0.94201624, -0.39906216 ), "+
"vec2( 0.94558609, -0.76890725 ), "+
"vec2( -0.094184101, -0.92938870 ),"+ 
"vec2( 0.34495938, 0.29387760 ), "+
"vec2( -0.91588581, 0.45771432 ), "+
"vec2( -0.81544232, -0.87912464 ), "+
"vec2( -0.38277543, 0.27676845 ), "+
"vec2( 0.97484398, 0.75648379 ), "+
"vec2( 0.44323325, -0.97511554 ), "+
"vec2( 0.53742981, -0.47373420 ),"+ 
"vec2( -0.26496911, -0.41893023 ),"+ 
"vec2( 0.79197514, 0.19090188 ), "+
"vec2( -0.24188840, 0.99706507 ), "+
"vec2( -0.81409955, 0.91437590 ), "+
"vec2( 0.19984126, 0.78641367 ), "+
"vec2( 0.14383161, -0.14100790 )"+ 
");"+



//For shadow 1, outer part
//Was 129.0
" float blurRadius1 = 127.0;"+
//For shadow 2, inner part
" float blurRadius2 = 136.0;"+
"float searchRadius = 1.0;"+

//" if (testSearchRadius == 1) {"+
//" searchRadius = 0.015;"+
//" }"+
//" else {"+
" searchRadius = 0.02;"+
//" }"+

//Really up close frequency for rSample1
//Frequency for  rSample3, used for         shadow1,              blur radius 1
//440 good
//Frequency 3 used for distant regions
" float frequency3 = 185;"+
//" if (testSearchRadius == 1) {"+
//"     frequency3 = 30;"+
//" }"+

"float dt3 = dot(floor(fPosWorld.xyz* frequency3), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle3 = 6.283285*fract(sin(dt3) * 2105.2354);"+

//Frequency for rSample4, used for          shadow2,              blur radius 2
//Frequency 4 used for closest region

" float frequency4 =  252;"+
//" if (testSearchRadius == 1) {"+
//"     frequency4 = 1100;"+
//" }"+

//Frequency 5 used for closest closest region

" float frequency5 =  590;"+
//" if (testSearchRadius == 1) {"+
//"     frequency4 = 1100;"+
//" }"+

"float dt4 = dot(floor(fPosWorld.xyz* frequency4), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle4 = 6.283285*fract(sin(dt4) * 2105.2354);"+

"float dt5 = dot(floor(fPosWorld.xyz* frequency5), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle5 = 6.283285*fract(sin(dt5) * 2105.2354);"+

" float shadow1Amnt = 1.0;"+
" float shadow2Amnt = 0.0;"+

" float shadow1THold = 0.99;"+
" float shadow2THold = 0.05;"+
" float shadow1Comp = 0.3;"+
" float shadow2Comp = 1.4;"+

" float shadow1THold2 = 0.99;"+

//" float rotationAngle = 1.0;"+	
//Frequency 1 used for 4th region
" float frequency1 = 146;"+
//" if (testSearchRadius == 1) {"+
//"     frequency1 = 40;"+
//" }"+

"float dt1 = dot(floor(fPosWorld.xyz* frequency1), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle1 = 6.283285*fract(sin(dt1) * 2105.2354);"+
//Up close frequency for rSample2
" float frequency = 164;"+
"float dt = dot(floor(fPosWorld.xyz* frequency), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle2 = 6.283285*fract(sin(dt) * 2105.2354);"+


//Search frequency
" float searchFrequency = 2.0;"+
//"if (testSearchRadius == 1) {"+
//"    searchFrequency = 2.0;"+
//" }"+
//"else {"+
//"     searchFrequency = 222222.0;"+
//"}"+
"float searchDt = dot(floor(fPosWorld.xyz* searchFrequency), vec3(53.1215, 21.1352, 9.1322));"+
"float searchRotationAngle = 6.283285*fract(sin(searchDt) * 2105.2354);"+


" vec2 rSample1 = vec2(cos(rotationAngle1),sin(rotationAngle1));"+
" vec2 rSample2 = vec2(cos(rotationAngle2),sin(rotationAngle2));"+
" vec2 rSample3 = vec2(cos(rotationAngle3),sin(rotationAngle3));"+
" vec2 rSample4 = vec2(cos(rotationAngle4),sin(rotationAngle4));"+
" vec2 rSample5 = vec2(cos(rotationAngle5),sin(rotationAngle5));"+

" vec2 rSampleSearch = vec2(cos(searchRotationAngle),sin(searchRotationAngle));"+
 



" int closestObjectSearchSize = 8;"+


  

" int outsideShadow = 0;"+
"float c = 1.0;"+
"float k = 1.0;"+ 


//shadow slope

//blurriest possible shadow blur radius
" float blurriestRadius = searchRadius;"+


//"if (testSearchRadius == 1) {"+

" k = 0.025;"+
" c = 0.0006;"+
"searchRadius = 0.0125;"+
" blurriestRadius = 0.0125;"+
"closestObjectSearchSize = 8;"+

//" if ( biasDot < 0.2 ) {"+
//"      searchRadius = 0.007;"+
//" }"+

//"}"+
//"else {"+



//" a = 0.02;"+
//" c = 0.0006;"+
//"searchRadius = 0.015;"+
//" blurriestRadius = 0.015;"+
//"closestObjectSearchSize = 16;"+

//" if ( biasDot < 0.2 ) {"+
//"      searchRadius = 0.007;"+
//" }"+
//"}"+

"float baselineClosestObjectDistance = 0.0;"+
	
"int objectFound = 0;"+

" float biasScale = 1.0;"+

" float distanceSofteningSampleCount = 8.0;"+
" float avgDistance = 0.0;"+
" float totalDistance = 0.0;"+



" if (fPosMVP.z <= 6.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap5, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight5.xyz/fPosLight5.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+


	" float shadow1 = 0.0;"+
	" float shadow2 = 0.0;"+
	" float samplesTaken1 = 0.0;"+
	" float samplesTaken2 = 0.0;"+
	// Poisson Sample

	"float closestObjectDistance = 100.0;"+
		
	"for (int t=0;t< closestObjectSearchSize ;t++){"+             
		" vec2 sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
		"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
		" float sampleDepth1 =  texture( shadowMap5, lightSpaceCoords.xy  + searchRadius*sampleC1).r;"+

		
	
	" if (sampleDepth1 == 1.0) {"+
   	//"          objectFound = 0;"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
   	
   " }"+
   " else {"+
   	
   		" if (currentDepth - bias  > sampleDepth1 ) {"+
   	"          objectFound = 1;"+  
   	"          totalDistance = totalDistance + abs(currentDepth - sampleDepth1 - bias);"+
    	" }"+
   	"     else {"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
    	" }"+
   	
  " }"+
	
	"}"+

	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 1.0 * min(c + k*abs(avgDistance), blurriestRadius);"+

	
	
	
	
	"for (int t=0;t<16;t++){"+
	"   vec2 sampleC1;"+
     " if (fPosMVP.z >= 1.6) {"+
		" sampleC1 = vec2(rSample4.x*(poissonDisk[t].x) - rSample4.y*(poissonDisk[t].y),  "+
		"   rSample4.y*(poissonDisk[t].x) + rSample4.x*(poissonDisk[t].y));"+
		" }"+
		" else {"+
		" sampleC1 = vec2(rSample5.x*(poissonDisk[t].x) - rSample5.y*(poissonDisk[t].y),  "+
		"   rSample5.y*(poissonDisk[t].x) + rSample5.x*(poissonDisk[t].y));"+
		"}"+
		" vec2 sampleC2 = vec2(rSample4.x*(poissonDisk[t].x) - rSample4.y*(poissonDisk[t].y),  "+
		"   rSample4.y*(poissonDisk[t].x) + rSample4.x*(poissonDisk[t].y));"+
		//" float sampleDepth1 =  texture( shadowMap5, lightSpaceCoords.xy  + sampleC1/(blurRadius1 + 7.0)).r;"+
		" float sampleDepth1 =  texture( shadowMap5, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+

		" samplesTaken1 = samplesTaken1 + 1.0;"+
		" shadow1 = shadow1 + ( currentDepth - biasScale*bias  > sampleDepth1 ? 1.0 : 0.0);"+    
	"}"+

	//" if (outsideShadow == 1) {"+
	
	//" shadow = 0.0;"+
	//"}"+
	//"else {"+
	"    shadow1 = shadow1 / samplesTaken1;"+
" shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+
//"}"+
//" shadow = 0.0;"+
" }\n"+





" else if (fPosMVP.z <= 12.0) {\n"+ 
	
	"	vec2 texelSize = 1.0 / textureSize(shadowMap4, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight4.xyz/fPosLight4.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z ;"+



	"float closestObjectDistance = 100.0;"+
	
	
//	" float baseDepth =   texture( shadowMap4, lightSpaceCoords.xy ).r;"+
//	"if (currentDepth - bias  > baseDepth && abs(currentDepth  - bias - baseDepth) < closestObjectDistance) {"+
//	"      closestObjectDistance = abs(currentDepth - baseDepth - bias);"+
//	"       objectFound = 1;"+
	
	
//	"}"+
	
	"for (int t=0;t< closestObjectSearchSize;t++){"+             
	
	" vec2 sampleC1 = vec2(1.0,1.0);"+
	"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
	
		" float sampleDepth1 =  texture( shadowMap4, lightSpaceCoords.xy  + 0.6*searchRadius*sampleC1).r;"+
		//" float sampleDepth2 =  texture( shadowMap4, lightSpaceCoords.xy  + 0.74*0.5*searchRadius*sampleC1).r;"+


	" if (sampleDepth1 == 1.0) {"+
   	//"          objectFound = 0;"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
   	
   " }"+
   " else {"+
   	
   		" if (currentDepth - bias  > sampleDepth1 ) {"+
   	"          objectFound = 1;"+  
   	"          totalDistance = totalDistance + abs(currentDepth - sampleDepth1 - bias);"+
    	" }"+
   	"     else {"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
    	" }"+
   	
  " }"+
	
	"}"+

	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.6 * min(c + k*abs(avgDistance), blurriestRadius);"+

	
	
		
		" float shadow1 = 0.0;"+
		" float shadow2 = 0.0;"+

	" float samplesTaken1 = 0.0;"+



	" float samplesTaken2 = 0.0;"+

	// Poisson Sample
	
	
	
		"for (int t=0;t<16;t++){"+ 

		" vec2 sampleC1 = vec2(rSample1.x*(poissonDisk[t].x) - rSample1.y*(poissonDisk[t].y),  "+
		"   rSample1.y*(poissonDisk[t].x) + rSample1.x*(poissonDisk[t].y));"+

		" vec2 sampleC2 = vec2(rSample4.x*(poissonDisk[t].x) - rSample4.y*(poissonDisk[t].y),  "+
		"   rSample4.y*(poissonDisk[t].x) + rSample4.x*(poissonDisk[t].y));"+
		
	//	" float sampleDepth1 =  texture( shadowMap4, lightSpaceCoords.xy  + sampleC1/(blurRadius1 + 78.0)).r;"+
		//" float sampleDepth2 =  texture( shadowMap4, lightSpaceCoords.xy  + sampleC2/(blurRadius2 + 90.0)).r;"+
		" float sampleDepth1 =  texture( shadowMap4, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+

		" samplesTaken1 = samplesTaken1 + 1.0;"+
		" shadow1 = shadow1 + ( currentDepth - biasScale*bias > sampleDepth1 ? 1.0 : 0.0);"+    
	//	" samplesTaken2 = samplesTaken2 + 1.0;"+
//" shadow2 = shadow2 + ( currentDepth - bias > sampleDepth2 ? 1.0 : 0.0);"+    
		
	"}"+
"    shadow1 = shadow1 / samplesTaken1;"+
//"    shadow2 = shadow2 / samplesTaken2;"+
" shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+
//" shadow = shadow2;"+
//" shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+
//"shadow = shadow2;"+
//" shadow = max(shadow1, shadow2);"+

" }\n"+





" else if (fPosMVP.z <= 18.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap3, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight3.xyz/fPosLight3.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	
 	//" vec3 lightSpaceCoords2 = fPosLightAnimated.xyz/fPosLightAnimated.w;"+
 	//" lightSpaceCoords2 = 0.5*lightSpaceCoords2 + 0.5;"+

		    	
		    	" float currentDepth = lightSpaceCoords.z;"+

		    	

	"float closestObjectDistance = 100.0;"+
		
	"for (int t=0;t< closestObjectSearchSize ;t++){"+             
		" vec2 sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
		"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
		" float sampleDepth1 =  texture( shadowMap3, lightSpaceCoords.xy  + 0.4*searchRadius*sampleC1).r;"+



	" if (sampleDepth1 == 1.0) {"+
   	//"          objectFound = 0;"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
   	
   " }"+
   " else {"+
   	
   		" if (currentDepth - bias  > sampleDepth1 ) {"+
   	"          objectFound = 1;"+  
   	"          totalDistance = totalDistance + abs(currentDepth - sampleDepth1 - bias);"+
    	" }"+
   	"     else {"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
    	" }"+
   	
  " }"+
	
	"}"+

	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.4 * min(c + k*abs(avgDistance), blurriestRadius);"+

	
			
		   
			" float shadow1 = 0.0;"+
			" float shadow2 = 0.0;"+

		" float samplesTaken1 = 0.0;"+

	  
	   
		" float samplesTaken2 = 0.0;"+

		// Poisson Sample
		
	   		"for (int t=0;t<16;t++){"+ 

	   		" vec2 sampleC1 = vec2(rSample3.x*(poissonDisk[t].x) - rSample3.y*(poissonDisk[t].y),  "+
	   		"   rSample3.y*(poissonDisk[t].x) + rSample3.x*(poissonDisk[t].y));"+
	   
			" vec2 sampleC2 = vec2(rSample4.x*(poissonDisk[t].x) - rSample4.y*(poissonDisk[t].y),  "+
	   		"   rSample4.y*(poissonDisk[t].x) + rSample4.x*(poissonDisk[t].y));"+
	   		
	   		//" float sampleDepth1 =  texture( shadowMap3, lightSpaceCoords.xy  + sampleC1/(blurRadius1 + 240.0)).r;"+
	 		" float sampleDepth1 =  texture( shadowMap3, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+

	   		//" float sampleDepth2 =  texture( shadowMap3, lightSpaceCoords.xy  + sampleC2/(blurRadius2 + 180.0)).r;"+

	   		" samplesTaken1 = samplesTaken1 + 1.0;"+
	 	" shadow1 = shadow1 + ( currentDepth - biasScale*bias > sampleDepth1 ? 1.0 : 0.0);"+    
	   	//	" samplesTaken2 = samplesTaken2 + 1.0;"+
	   		//" shadow2 = shadow2 + ( currentDepth - bias > sampleDepth2 ? 1.0 : 0.0);"+    
			
		"}"+
	"    shadow1 = shadow1 / samplesTaken1;"+
	//"    shadow2 = shadow2 / samplesTaken2;"+
	

" shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+
//"shadow = shadow2;"+
//" shadow = max(shadow1, shadow2);"+

" }\n"+





" else if (fPosMVP.z <= 22.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap2, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight2.xyz/fPosLight2.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	
 	//" vec3 lightSpaceCoords2 = fPosLightAnimated.xyz/fPosLightAnimated.w;"+
 	//" lightSpaceCoords2 = 0.5*lightSpaceCoords2 + 0.5;"+

		    	 
		    	" float currentDepth = lightSpaceCoords.z;"+
		    	
"float closestObjectDistance = 100.0;"+
	
	"for (int t=0;t< closestObjectSearchSize ;t++){"+             
		" vec2 sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
		"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
		" float sampleDepth1 =  texture( shadowMap2, lightSpaceCoords.xy  + 0.3*searchRadius*sampleC1).r;"+

	" if (sampleDepth1 == 1.0) {"+
   	//"          objectFound = 0;"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
   	
   " }"+
   " else {"+
   	
   		" if (currentDepth - bias  > sampleDepth1 ) {"+
   	"          objectFound = 1;"+  
   	"          totalDistance = totalDistance + abs(currentDepth - sampleDepth1 - bias);"+
    	" }"+
   	"     else {"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
    	" }"+
   	
  " }"+
	
	"}"+

	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.3 * min(c + k*abs(avgDistance), blurriestRadius);"+

	
	
		   
			" float shadow1 = 0.0;"+
			" float shadow2 = 0.0;"+

		" float samplesTaken1 = 0.0;"+

	  
	   
		" float samplesTaken2 = 0.0;"+

		// Poisson Sample
		
	   		"for (int t=0;t<16;t++){"+ 

	   		" vec2 sampleC1 = vec2(rSample3.x*(poissonDisk[t].x) - rSample3.y*(poissonDisk[t].y),  "+
	   		"   rSample3.y*(poissonDisk[t].x) + rSample3.x*(poissonDisk[t].y));"+
	   
			" vec2 sampleC2 = vec2(rSample4.x*(poissonDisk[t].x) - rSample4.y*(poissonDisk[t].y),  "+
	   		"   rSample4.y*(poissonDisk[t].x) + rSample4.x*(poissonDisk[t].y));"+
	   		
	//  	" float sampleDepth1 =  texture( shadowMap2, lightSpaceCoords.xy  + sampleC1/(blurRadius1 + 520.0)).r;"+
	   	//" float sampleDepth2 =  texture( shadowMap2, lightSpaceCoords.xy  + sampleC2/(blurRadius2 + 280.0)).r;"+

" float sampleDepth1 =  texture( shadowMap2, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+
	
	   		" samplesTaken1 = samplesTaken1 + 1.0;"+
	   	" shadow1 = shadow1 + ( currentDepth - biasScale*bias > sampleDepth1 ? 1.0 : 0.0);"+    
	   		//" samplesTaken2 = samplesTaken2 + 1.0;"+
	   		//" shadow2 = shadow2 + ( currentDepth - bias > sampleDepth2 ? 1.0 : 0.0);"+    
			
		"}"+
	"    shadow1 = shadow1 / samplesTaken1;"+
	//"    shadow2 = shadow2 / samplesTaken2;"+


" shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+
//"shadow = shadow2;"+
//	" shadow = max(shadow1, shadow2);"+

 " }\n"+



" else if (fPosMVP.z <= 100.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap1, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight1.xyz/fPosLight1.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z - 0.01;"+


"float closestObjectDistance = 1000.0;"+

"for (int t=0;t<16;t++){"+             
" vec2 sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
" float sampleDepth1 =  texture( shadowMap1, lightSpaceCoords.xy  + 0.141*searchRadius*sampleC1).r;"+



	" if (sampleDepth1 == 1.0) {"+
   	//"          objectFound = 0;"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
   	
   " }"+
   " else {"+
   	
   		" if (currentDepth - bias  > sampleDepth1 ) {"+
   	"          objectFound = 1;"+  
   	"          totalDistance = totalDistance + abs(currentDepth - sampleDepth1 - bias);"+
    	" }"+
   	"     else {"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
    	" }"+
   	
  " }"+
	
	"}"+

	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.141 * min(c + k*abs(avgDistance), blurriestRadius);"+

	
	
	
	" float samplesTaken = 0.0;"+
"for (int t=0;t<16;t++){"+

		" vec2 sampleC = vec2(rSample4.x*(poissonDisk[t].x) - rSample4.y*(poissonDisk[t].y),  "+
		"   rSample4.y*(poissonDisk[t].x) + rSample4.x*(poissonDisk[t].y));"+
		
	//	" float sampleDepth =  texture( shadowMap1, lightSpaceCoords.xy  + sampleC/1190.0).r;"+
		" float sampleDepth =  texture( shadowMap1, lightSpaceCoords.xy  + blurRadius1*sampleC).r;"+

	//" float sampleDepth =  texture( shadowMap1, lightSpaceCoords.xy  ).r;"+   

		" samplesTaken = samplesTaken + 1.0;"+
		
		" shadow = shadow + ( currentDepth - biasScale*bias > sampleDepth ? 1.0 : 0.0);"+    
	
	"}"+
"    shadow = shadow / samplesTaken;"+

	" }\n"+










		
" albedoColor = vec4(  vec3(0.0,1.0,0.0)  , 1.0);"+
//" color = vec4(1.0,1.0,1.0,1.0);"+
" float brightness = dot(albedoColor.rgb,vec3(0.2126,0.7152,0.0722));"+

   " if (brightness > 1.0) {"+
   "       colorBright = vec4(brightness*albedoColor.rgb, 1.0);"+
   " }"+
   " else {"+
   
   "       colorBright = vec4(0.0,0.0,0.0,1.0);"+
   " }"+
   
   "normal = vec4(1000.0,1.0,1.0,1.0);"+
   "position = vec4(-fPosCamera.z/1000.0,0.0,0.0,1.0);"+
  "sceneShadowAmount = vec4(shadow,0.0,0.0,1.0);"+
   
  


//Determine if fragment is within a water body bounds

" if ( fPosWorld.x >= (waterBody1.pos).x && fPosWorld.z <= (waterBody1.pos).z     &&    fPosWorld.x <= (waterBody1.pos).x + ((waterBody1.scale).x) && fPosWorld.z >= (waterBody1.pos).z - ((waterBody1.scale).x)   ) {"+

//" float waterHeight = (waterBody1.scale).y*texture(waterBody1HeightField, vec2( (fPosWorld.x - (waterBody1.pos).x)/( (waterBody1.dimensions).x   *   (waterBody1.scale).x ),   (  (waterBody1.pos).z - fPosWorld.z)/( (waterBody1.dimensions).y   *   (waterBody1.scale).x ) ) ).r;"+

" vec2 nGP = vec2( (fPosWorld.x - (waterBody1.pos).x) / (   (waterBody1.scale).x), ((waterBody1.pos).z - fPosWorld.z) / (   (waterBody1.scale).x));"+

" float x1 = (floor(nGP.x*(waterBody1.dimensions).x))/((waterBody1.dimensions).x);"+
" float x2 = (ceil(nGP.x*(waterBody1.dimensions).x))/((waterBody1.dimensions).x);"+
" float y1 = (floor(nGP.y*(waterBody1.dimensions).y))/((waterBody1.dimensions).y);"+
" float y2 = (ceil(nGP.y*(waterBody1.dimensions).y))/((waterBody1.dimensions).y);"+


" float distLToNGP = sqrt( pow(nGP.x - x1,2) + pow(nGP.y - y1,2));"+
" float distRToNGP = sqrt( pow(nGP.x - x2,2) + pow(nGP.y - y2,2));"+



" vec2 v1 = vec2(x1, y2);"+

" vec2 v2 = vec2(0.0,0.0);"+

" if ( abs(distLToNGP - distRToNGP) <= 0.0000000000001) {"+
"        v2 = vec2(x1,y1);"+
" }"+
" else if ( distLToNGP > distRToNGP) {"+
"    v2 = vec2(x2,y2);"+
"}"+
" else {"+
"    v2 = vec2(x1,y1);"+
" }"+

" vec2 v3 = vec2(x2,y1);"+
//      A               B
//
//                    
//                   P
//         P
//     B                C

" vec3 AB = vec3(v2.x - v1.x, 0.0, v2.y - v1.y);"+
" vec3 AP = vec3(nGP.x - v1.x, 0.0, nGP.y - v1.y);"+
" vec3 AC = vec3(v3.x - v1.x, 0.0, v3.y - v1.y);"+
" vec3 BC = vec3(v3.x - v2.x, 0.0, v3.y - v2.y);"+
" vec3 BP = vec3(nGP.x - v2.x, 0.0, nGP.y - v2.y);"+

" float areaABC = length(cross(AB,AC));"+
" float areaABP = length(cross(AB,AP));"+
" float areaACP = length(cross(AC,AP));"+
" float areaCBP = length(cross(BC,BP));"+

" float heightV1 = (waterBody1.scale).y*texture(waterBody1HeightField, v1).r;"+
" float heightV2 = (waterBody1.scale).y*texture(waterBody1HeightField, v2).r;"+
" float heightV3 = (waterBody1.scale).y*texture(waterBody1HeightField, v3).r;"+

" float waterHeight = (areaCBP/areaABC)*heightV1 + (areaACP/areaABC)*heightV2 + (areaABP/areaABC)*heightV3;"+ 

" if ( fPosWorld.y <= ((waterBody1.pos).y + waterHeight) + 0.008 ) {"+ 
" refractiveObject = vec4(0.5,0.5,0.5,1.0);"+
"}"+
" else {"+
" refractiveObject = vec4(0.0,0.0,0.0,1.0);"+
" }"+
"}"+
"else {"+
" refractiveObject = vec4(0.0, 0.0, 0.0, 1.0);"+
"}"+

//" shadowBlurRadius = vec4(0.01,0.0,0.0,1.0);"+
/*
   

"float shadow = 0.0;"+
"float bias = 0.002;"+


		" if (fPosMVP.z <= 12.0) {\n"+
		"	vec2 texelSize = 1.0 / textureSize(shadowMap3, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight3.xyz/fPosLight3.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+

		    	"vec2 sampleV = texture(shadowMap3, lightSpaceCoords.xy ).rg;"+
		    	"float z = sampleV.r;"+
		    	"float zSquared = sampleV.g;"+
		    	"float s = zSquared - pow(z,2.0);"+
		    	"float k = currentDepth - bias - z;"+
		    	
		    	" if ( k > 0.0 ) {"+
		    	" 		shadow = (s) / (s + pow((k),2.0));"+
		    	" }"+
		    	"else {"+
		    	"        shadow = 1.0;"+
		    	"}"+
		    	
" }\n"+





		" else if (fPosMVP.z <= 28.0) {\n"+
		"	vec2 texelSize = 1.0 / textureSize(shadowMap2, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight2.xyz/fPosLight2.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+

		    	"vec2 sampleV = texture(shadowMap2, lightSpaceCoords.xy ).rg;"+
		    	"float z = sampleV.r;"+
		    	"float zSquared = sampleV.g;"+
		    	"float s = zSquared - pow(z,2.0);"+
"float k = currentDepth - bias - z;"+
		    	
		    	" if ( k > 0.0 ) {"+
		    	" 		shadow = (s) / (s + pow((k),2.0));"+
		    	" }"+
		    	"else {"+
		    	"        shadow = 1.0;"+
		    	"}"+
		    		" }\n"+


		" else if (fPosMVP.z <= 50.0) {\n"+
		"	vec2 texelSize = 1.0 / textureSize(shadowMap1, 0);"+
		    	" vec3 lightSpaceCoords = fPosLight1.xyz/fPosLight1.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+

		    	"vec2 sampleV = texture(shadowMap1, lightSpaceCoords.xy ).rg;"+
		    	"float z = sampleV.r;"+
		    	"float zSquared = sampleV.g;"+
		    	"float s = zSquared - pow(z,2.0);"+
"float k = currentDepth - bias - z;"+
		    	
		    	" if ( k > 0.0 ) {"+
		    	" 		shadow = (s) / (s + pow((k),2.0));"+
		    	" }"+
		    	"else {"+
		    	"        shadow = 1.0;"+
		    	"}"+
		    	" }\n"+

		  " shadow = 1.0*max(shadow, 0.4);"+


   " shadow = 1.66667*(shadow - 0.4);"+
  */
  // " fragColor = vec4(screenBrightness *  ( 0.8*(1.0 - shadow)*vec3(0.2, 1.0, 0.9)   + 0.2*vec3(0.2, 1.0, 0.9) ),1.0);\n"+
  //" diffuseMapColor = vec4(0.2,1.0,0.9,1.0);"+
//" diffuseAmountAndShadowAmount = vec2(1.0,shadow);"+
//" shadowAnimatedAmount = shadowAnimated;"+
  
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
	        
	        
	        

	        // Uniforms
	        modelMatrixUniformLocation = glGetUniformLocation(programId,"modelMatrix");
	        modelWorldMatrixUniformLocation = glGetUniformLocation(programId,"modelWorldMatrix");
	        modelWorldViewMatrixUniformLocation = glGetUniformLocation(programId,"modelWorldViewMatrix");
	        modelWorldViewProjectionMatrixUniformLocation = glGetUniformLocation(programId,"modelWorldViewProjectionMatrix");
	        viewProjectionMatrixUniformLocation = glGetUniformLocation(programId,"viewProjectionMatrix");
	        viewMatrixUniformLocation = glGetUniformLocation(programId,"viewMatrix");
	        normalMatrixUniformLocation = glGetUniformLocation(programId,"normalMatrix");
	        cameraPositionUniformLocation = glGetUniformLocation(programId,"cameraPosition");
	        
	        lightMatrix1Location = glGetUniformLocation(programId,"lightMatrix1");
	        lightMatrix2Location = glGetUniformLocation(programId,"lightMatrix2");
	        lightMatrix3Location = glGetUniformLocation(programId,"lightMatrix3");
	        lightMatrix4Location = glGetUniformLocation(programId,"lightMatrix4");
	        lightMatrix5Location = glGetUniformLocation(programId,"lightMatrix5");
	        
	        directionalLightTypeLocation = glGetUniformLocation(programId,"directionalLight.type");
	        directionalLightDirectionLocation = glGetUniformLocation(programId,"directionalLight.directionOrLocation");
	        directionalLightColorLocation = glGetUniformLocation(programId,"directionalLight.color");
	        directionalLightAmountLocation = glGetUniformLocation(programId,"directionalLight.amount");
	        
	        //screenBrightnessLocation = glGetUniformLocation(programId,"screenBrightness");
	        
	        
	        shadowMap1Location = glGetUniformLocation(programId,"shadowMap1");
	        shadowMap2Location = glGetUniformLocation(programId,"shadowMap2");
	        shadowMap3Location = glGetUniformLocation(programId,"shadowMap3");
	        shadowMap4Location = glGetUniformLocation(programId,"shadowMap4");
	        shadowMap5Location = glGetUniformLocation(programId,"shadowMap5");
		        
	        
	        shadowMapAnimated1Location = glGetUniformLocation(programId,"shadowMapAnimated1");
	        shadowMapAnimated2Location = glGetUniformLocation(programId,"shadowMapAnimated2");
	        shadowMapAnimated3Location = glGetUniformLocation(programId,"shadowMapAnimated3");
	        
	        waterBody1PosLocation = glGetUniformLocation(programId,"waterBody1.pos");
	        waterBody1DimensionsLocation = glGetUniformLocation(programId,"waterBody1.dimensions");
	        waterBody1ScaleLocation = glGetUniformLocation(programId,"waterBody1.scale");
	      
	        waterBody1Location = glGetUniformLocation(programId,"waterBody1HeightField");
		        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        

	        
	        
	        
	        String shadowVShaderCode = 
	        		"#version 430 core\n" +
	        		"layout (location = 0) in vec3 aPos;" +
	        		
					"uniform mat4 worldModelMatrix;" +
					"uniform mat4 lightOrthoMatrix;"+
	        		"uniform mat4 shadowLightMatrix;" +
	        		" vec4 pos1;"+
	        		
	        		"float snap(float pos, float multiple) {"+
	        		"    return round(pos/multiple)*multiple; "+
	        		"}"+
	        		
	        		"void main()" +
	        		"{" +
	        		"    vec4 posWorldModel = worldModelMatrix*vec4(aPos, 1.0);"+
	        		"    vec4 snappedWorldPos = vec4(snap(posWorldModel.x, 2.0f*5.349103/1024.0f), snap(posWorldModel.y, 2.0f*5.349103/1024.0f), posWorldModel.z, 1.0f);"+
	        		// New snapped
	        		"    vec4 snappedFinalPosition = lightOrthoMatrix*snappedWorldPos;"+
	        		
	        		// Original
	        		"pos1 = shadowLightMatrix *  vec4(aPos, 1.0);" + 
	        		" vec4 snappedAgainPos1 = vec4(snap(pos1.x, 1.0f/1024.0), snap(pos1.y, 1.0f/1024.0), pos1.z, 1.0f);"+
	        		//"gl_Position = vec4(pos1.x,pos1.y,pos1.z,pos1.w);"+
	        		" gl_Position = snappedFinalPosition;"+
	        		//"  gl_Position = snappedAgainPos1;"+
	        		
	        		"}";
	        
	        
	       String shadowFShaderCode =


	    		   "#version 430 core\n" +

	    		   "out vec4 fragColor;" +
	    		   "void main()" +
	       		  "{" +             
	    		   " float c = 80.0;"+
	       		//"fragColor = vec4(exp(c*gl_FragCoord.z), 0.0, 0.0, 1.0);" +
	       		"fragColor = vec4(gl_FragCoord.z - 0.0,0.0,0.0,1.0);"+
		       	
	    		"}";


	    		
	        
	        

		
		
		
	        
	        shadowProgramId = glCreateProgram();
	        glUseProgram(shadowProgramId);
	        
	        if (shadowProgramId == 0) {
	            throw new Exception("Could not create Shader");
	        }
	        
	        
	        shadowVShaderId = glCreateShader(GL_VERTEX_SHADER);
	        if (shadowVShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
	        }

	        glShaderSource(shadowVShaderId, shadowVShaderCode);
	        glCompileShader(shadowVShaderId);

	        if (glGetShaderi(shadowVShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shadowVShaderId, 1024));
	        }

	        glAttachShader(shadowProgramId, shadowVShaderId);

	        
	        
	        
	        

	        shadowFShaderId = glCreateShader(GL_FRAGMENT_SHADER);
	        if (shadowFShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
	        }

	        glShaderSource(shadowFShaderId, shadowFShaderCode);
	        glCompileShader(shadowFShaderId);

	        if (glGetShaderi(shadowFShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shadowFShaderId, 1024));
	        }

	        glAttachShader(shadowProgramId, shadowFShaderId);
	        
	        
	        
	        
	        
	        
	        
	        
	        glLinkProgram(shadowProgramId);
	        if (glGetProgrami(shadowProgramId, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(shadowProgramId, 1024));
	        }

	        if (shadowVShaderId != 0) {
	            glDetachShader(shadowProgramId, shadowVShaderId);
	        }
	        if (shadowFShaderId != 0) {
	            glDetachShader(shadowProgramId, shadowFShaderId);
	        }

	        glValidateProgram(shadowProgramId);
	        if (glGetProgrami(shadowProgramId, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(shadowProgramId, 1024));
	        }
	    
		
	        glUseProgram(shadowProgramId);
	        
	        
	        
	        shadowLightMatrixLocation = glGetUniformLocation(shadowProgramId,"shadowLightMatrix");
	        lightOrthoMatrixLocation = glGetUniformLocation(shadowProgramId,"lightOrthoMatrix");
	        worldModelMatrixLocation = glGetUniformLocation(shadowProgramId,"worldModelMatrix");
		      

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        // Shadow Map Program with Temporal Filtering
	        

	        
	        

	        
	        
	        
	        String temporalShadowVShaderCode = 
	        		"#version 430 core\n" +
	        		"layout (location = 0) in vec3 aPos;" +
	        		
	        		"uniform mat4 shadowLightMatrix;" +

	        		"void main()" +
	        		"{" +
	        		    "gl_Position = shadowLightMatrix *  vec4(aPos, 1.0);" +
	        		"}";
	        
	        
	       String temporalShadowFShaderCode =


	    		   "#version 430 core\n" +

	    		   "out float fragColor;" +
	    		   "uniform sampler2D previousShadowMap;" +
	    		   
	    		   "void main()" +
	       		  "{" +             
	       		//"fragColor = vec2(gl_FragCoord.z,pow(gl_FragCoord.z,2.0));" +
	       		"fragColor = gl_FragCoord.z;"+
		       	
	    		"}";


	    		
	        
	        

		
		
		
	        
	        temporalShadowProgramId = glCreateProgram();
	        glUseProgram(temporalShadowProgramId);
	        
	        if (temporalShadowProgramId == 0) {
	            throw new Exception("Could not create Shader");
	        }
	        
	        
	        temporalShadowVShaderId = glCreateShader(GL_VERTEX_SHADER);
	        if (temporalShadowVShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
	        }

	        glShaderSource(temporalShadowVShaderId, temporalShadowVShaderCode);
	        glCompileShader(temporalShadowVShaderId);

	        if (glGetShaderi(temporalShadowVShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(temporalShadowVShaderId, 1024));
	        }

	        glAttachShader(temporalShadowProgramId, temporalShadowVShaderId);

	        
	        
	        
	        

	        temporalShadowFShaderId = glCreateShader(GL_FRAGMENT_SHADER);
	        if (temporalShadowFShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
	        }

	        glShaderSource(temporalShadowFShaderId, temporalShadowFShaderCode);
	        glCompileShader(temporalShadowFShaderId);

	        if (glGetShaderi(temporalShadowFShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(temporalShadowFShaderId, 1024));
	        }

	        glAttachShader(temporalShadowProgramId, temporalShadowFShaderId);
	        
	        
	        
	        
	        
	        
	        
	        
	        glLinkProgram(temporalShadowProgramId);
	        if (glGetProgrami(temporalShadowProgramId, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(temporalShadowProgramId, 1024));
	        }

	        if (temporalShadowVShaderId != 0) {
	            glDetachShader(temporalShadowProgramId, temporalShadowVShaderId);
	        }
	        if (temporalShadowFShaderId != 0) {
	            glDetachShader(temporalShadowProgramId, temporalShadowFShaderId);
	        }

	        glValidateProgram(temporalShadowProgramId);
	        if (glGetProgrami(temporalShadowProgramId, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(temporalShadowProgramId, 1024));
	        }
	    
		
	        glUseProgram(temporalShadowProgramId);
	        
	        
	        
	        temporalShadowLightMatrixLocation = glGetUniformLocation(temporalShadowProgramId,"shadowLightMatrix");
	        temporalShaderPreviousShadowMapSamplerLocation = glGetUniformLocation(temporalShadowProgramId,"previousShadowMap");

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
		 FloatBuffer verticesBuffer = null;
	//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
	            System.out.println("Length of vertices is " + vertices.length);
	//   verticesBuffer = 
		 vertexCount = vertices.length / 3;
	            (verticesBuffer.put(vertices)).flip();
//glGenVertexArrays();
	           vaoId = glGenVertexArrays();
	           System.out.println("Vao is " + vaoId);
	            glBindVertexArray(vaoId);

	            vboId = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, vboId);
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
	        
	        
		    
		    
		   
		    
		    
		    
		    
		    
	        Matrix4f testMatrix = new Matrix4f();
	        
	        Matrix4f translationMatrix = new Matrix4f();
	     
	        translationMatrix.translate(new Vector3f((float)x,(float)y,(float)1.0));
	        Matrix4f projMatrix = new Matrix4f();
	        projMatrix.perspective((float)(Math.PI/4.0), (float)(1920.0/1080.0),(float)(0.001),(float)(60.0));
	        
	        
	        
	        
	        testMatrix.translate((float)x,(float)y,(float)z);
	        
	        testMatrix.perspective((float)(Math.PI/4.0), (float)(1920.0/1080.0), (float)0.001, (float)60.0);
	        
	        testMatrix = testMatrix.mul(testMatrix);
	        
	        /*
	        glEnableVertexAttribArray(0);
	        
	        
	        glUseProgram(programId);
	        glBindVertexArray(vaoId);
	        glDrawArrays(GL_TRIANGLES,0,6);
	        
	        
	        */
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public double getX() {
		return x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getY() {
		return y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public double getZ() {
		return z;
	}
	
	
	public void setPreviousX(double x) {
		this.previousX = x;
	}
	
	public void setPreviousY(double y)  {
		this.previousY = y;
	}
	
	public double getPreviousX() {
	        return previousX;
	}
	
	public double getPreviousY() {
		return previousY;
	}
	
	public void setCurrentX(double X) {
		this.currentX = x;
	}
	
	public void setCurrentY(double y) {
		this.currentY = y;
	}
	
	public double getCurrentX() {
		return currentX;
	}
	
	public double getCurrentY() {
		return currentY;
	}
	
	public void setDX(double dx) {
		this.dx = dx;
	}
	public void setDY(double dy) {
		this.dy = dy;
	}
	public double getDX() {
		return dx;
	}
	public double getDY() {
		return dy;
	}
	
	public double getWidth() {
		return 0.0;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public double getFeetWidth() {
		return feetWidth;
	}
	
	public int getFeetHeight() {
		return feetHeight;
	}
	
	public int getFeetOSet() {
		return feetOSet;
	}
	
	public boolean getJumping() {
		return jumping;
	}
	
	public boolean getFalling() {
		return this.falling;
	}
	
	public void setFalling(boolean s) {
	if (this.getFalling() != s) {
		this.fallVelocity = this.initialFallVelocity;  // reset fall velocity to initial value when there is a change in fall status
	}                                                  // true to false, in air then landed-->reset,  false to true, on platform then in air-->reset
	this.falling = s;
	}
	
	public void setJumping(boolean s) {
		jumping = s;
	}
	public boolean getAlreadyMoved() {
		return alreadyMoved;
	}
	public void setAlreadyMoved(boolean s) {
		alreadyMoved = s;
	}
	
	
	
	public void setFreeToMove(boolean s) {
		freeToMove = s;
	}
	
	public boolean getFreeToMove() {
		return freeToMove;
	}
	
	public void move(int type) {
		
		//System.out.println("@ Beginning of player.move(), player.dx = " + dx + " and player.dy = " + dy);
		
		// type 1 = camera shift up, type 2 = camera shift down, type 3 = player progression
		
		x = x + dx;
		vPanX = x + dx;
	//	y = y + dy;
		
		
		
		if (type == 1) {
			vPanY = vPanY + dy;

			y = y + dy;
		}
		else if (type == 2) {
			
			vPanY = vPanY + dy;

			y = y + dy;
			
		}
		else if (type == 3) {
			
			//x = x + dx;
			y = y + dy;
			/*
			if (panningU ) {
				
			///ln("~~P1~~");
				//("Dy is " + dy);
				
				if (falling) {
				//y = y + dy;
					b.setDX(0.0);
					b.setDY(-dy);
					b.move(1.0, 1.0, 0, 3);
				}
				else {
					//b.setY(b.getY() - dy);
					y = y + dy;
				if (jumping && y > t2) {
					System.out.println("P.y went above t2 thold while panning up");
					//System.exit(0) ;
				}
			   }
			
				if (y > t2 || Math.abs(y - t2) <= landingPrecision) {
					System.out.println("Went to or past t2 while panning up");
					System.exit(0);
					vPanY = t2;       
					panningU = false;
					panningD = false;
					
					
				}
				
			}
			else if (panningD) {
				y = y + dy;
				if (y < t1 || Math.abs(y - t1) <= landingPrecision) {
					
					vPanY = t1;       
					panningU = true;
					panningD = false;
					
					
				}
			}
			else if ( (y > t1 || Math.abs(y - t1) <= landingPrecision)  &&  (y < t2 || Math.abs(t2 - y) <= landingPrecision)) {
				
			
			 if (y + dy < t1 || Math.abs(y  + dy - t1) <= landingPrecision) {
				//System.out.println("~~P2~~");
				//System.out.println("Dy is " + dy);


			//	System.exit(0);
				panningU = true;
				panningD = false;
				
				
		   	       
     
				if (Math.abs(y + dy - t1) <= landingPrecision) {
					//("~~P2A~~");

					//("Y IS " + y);

					vPanY = y + dy;

					y = y + dy;
					//b.setY(b.getY() - dy);

					//b.setDX(0.0);
					//b.setDY(-dy);
					//b.move(1.0, 1.0, 0, 3);
				}
				else {
					
					//("~~P2B~~");
					//("Y IS " + y);
					double pDY = Math.abs(y - t1);
					double bgDY = Math.abs(Math.abs(dy) - pDY);
					
					//("PDY IS " + pDY);
					//("bgDY IS " + bgDY);

					vPanY = y - pDY;
					
					y = y - pDY;

					b.setDX(0.0);
					b.setDY(bgDY);
					b.move(1.0, 1.0, 0, 3);
				}
			}
			 else if (y  + dy > t2 || Math.abs(y + dy - t2) <= landingPrecision) {
				//("~~P3~~");
				//("Dy is " + dy);


				panningU = false;
				panningD = true;
				
				if (Math.abs(y + dy - t2) <= landingPrecision) {
					vPanY = y + dy;
					y = y + dy;
				}
				else {
					double pDY = Math.abs(y - t2);
					double bgDY = Math.abs(Math.abs(dy) - pDY);
					vPanY = y + pDY;
					y = y + pDY;

					b.setDX(0.0);
					b.setDY(-bgDY);
					b.move(1.0, 1.0, 0, 3);
				}
			}
			else {
				y = y + dy;
			}
			
			
		  }
			else {
				//b.setY(b.getY() - dy);
//				System.out.println("Finisih the p move method");
	//			System.exit(0);
				
		y = y + dy;
		
		           if (y > t2) {
		        	   panningU = true;
		        	   panningD = false;
		           }
		           else if (y < t1) {
		        	   panningD = true;
		        	   panningU = false;
		           }
			}
			*/
		}
		
		//x = x + dx;
		//y = y + dy;
		
		
	}
	

	public void renderShadowMap(Matrix4f lightTransform, int shadowFBO, Matrix4f lightVT, Vector3f lightFCSnapped, Vector3f lightFCOriginal, int sMapRes, float mRange, float mnZ, float mxZ, Matrix4f cameraM,int cascadeNumber) {
		
		double sMapStartTime = System.nanoTime();
		

		Matrix4f shadowLightMatrix = new Matrix4f();
		Matrix4f scaleMatrix = new Matrix4f();
		Matrix4f translationMatrix = new Matrix4f();
		Matrix4f modelWorldMatrix = new Matrix4f();
		Matrix4f mVMatrix = new Matrix4f();
		
		scaleMatrix.scaling(this.scale);
		translationMatrix.translation(new Vector3f((float)(x  ),(float)(y  ),(float)(z )));
		translationMatrix.mul(scaleMatrix,modelWorldMatrix);
		//shadowLightMatrix.set(translationM);
		
		lightTransform.mul(modelWorldMatrix,shadowLightMatrix);
		cameraM.mul(modelWorldMatrix,mVMatrix);
		
		// FOR MONITORING CHANGE IN VERTEX POSITIONS IN LIGHT SPACE
		//Vector4f aModelSpacePlayerVertex = new Vector4f(-0.5f,  0.5f,  0.5f, 1.0f);
		Vector4f aModelSpacePlayerVertex = new Vector4f(0.0f,  0.0f,  0.0f, 1.0f);
		Vector4f testPositionLSpace = new Vector4f();
		Vector4f testPositionCameraSpace = new Vector4f();
		shadowLightMatrix.transform(aModelSpacePlayerVertex,testPositionLSpace);
		mVMatrix.transform(aModelSpacePlayerVertex,testPositionCameraSpace);
		
		/*
		 * Observations:
		 * With and without snapping of light frustum, lightspace position of player oscilates
		 */
		if (cascadeNumber == 5) {
			//System.out.println("PLAYER CAMERA SPACE VERTEX POSITION: "+testPositionCameraSpace);
			//System.out.println("PLAYER LIGHT SPACE VERTEX POSITION: "+testPositionLSpace);
			//System.out.println("DELTA PLAYER POS X LIGHT SPACE: "+(Math.abs(testPositionLSpace.x-prevLSpacePosX)));
		}
		
		prevLSpacePosX = testPositionLSpace.x;
		
		
		   try (MemoryStack stack = MemoryStack.stackPush()) {
		        FloatBuffer tMVPM = stack.mallocFloat(16);
		        FloatBuffer tModelWorld = stack.mallocFloat(16);
		        FloatBuffer tLOM = stack.mallocFloat(16);
			    
	     	  shadowLightMatrix.get(tMVPM);
	     	  modelWorldMatrix.get(tModelWorld);
	     	  lightTransform.get(tLOM);
	     //tMVP.get(tMVPM);
		        
		        glUseProgram(shadowProgramId);
		        
		        glUniformMatrix4fv(shadowLightMatrixLocation, false, tMVPM);
		        glUniformMatrix4fv(worldModelMatrixLocation, false, tModelWorld);
		        glUniformMatrix4fv(lightOrthoMatrixLocation, false, tLOM);
		        double b4Time = System.nanoTime();
		        double aftrTime = System.nanoTime();
	        
	       }
		   
	       glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        
	 


	        glUseProgram(shadowProgramId);
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        
	        glEnable(GL_CULL_FACE);
	        glCullFace(GL_FRONT);
	
	        glDrawArrays(GL_TRIANGLES,0,(vertices.length)/3);
	        
	        glDisable(GL_CULL_FACE);
	        glDisableVertexAttribArray(0);

	        
	        glDisableVertexAttribArray(0);
	        
	        glBindVertexArray(0);
	        glUseProgram(shadowProgramId);
	        glUseProgram(0);
		
	        double sMapEndTime = System.nanoTime();
		
		
	}
	
	public void render(Camera camera, Matrix4f lightMatrix1, Matrix4f lightMatrix2,Matrix4f lightMatrix3,Matrix4f lightMatrix4, Matrix4f lightMatrix5,int shadowMap1Unit,int shadowMap2Unit,int shadowMap3Unit,int shadowMap4Unit,int shadowMap5Unit,
			float screenBrightness,int renderToCubeMap,Matrix4f vMatrix, Matrix4f aProjectionMatrix) {

		int keyInput = -1;
		
		glUseProgram(programId);

		
		Matrix4f scalingM = new Matrix4f();
		scalingM.scaling(this.scale);
		Matrix4f rotation = new Matrix4f();
		rotation.rotationXYZ(this.rotationX,this.rotationY,this.rotationZ);
		Matrix4f aModelMatrix = new Matrix4f();
		rotation.mul(scalingM,aModelMatrix);
		Matrix4f aWorldMatrix = new Matrix4f();
		aWorldMatrix.translation((float)this.x,(float)(this.y),(float)this.z);
		Matrix4f aViewMatrix = new Matrix4f();
		aViewMatrix.lookAt((float)camera.getEyeX(),(float)camera.getEyeY(),(float)camera.getEyeZ(), (float)camera.getVPointX(), (float)camera.getVPointY(), (float)camera.getVPointZ(), 0.0f,1.0f,0.0f );
		Matrix4f normalMatrix = new Matrix4f();
		Vector3f cameraPosition = new Vector3f((float)camera.getEyeX(),(float)camera.getEyeY(),(float)camera.getEyeZ());
		
		Matrix4f modelWorldMatrix = new Matrix4f();
		Matrix4f modelWorldViewMatrix = new Matrix4f();
		Matrix4f modelWorldViewProjectionMatrix = new Matrix4f();
		Matrix4f viewProjectionMatrix = new Matrix4f();

		aWorldMatrix.mul(aModelMatrix,modelWorldMatrix);
		aViewMatrix.mul(modelWorldMatrix,modelWorldViewMatrix);
		aProjectionMatrix.mul(modelWorldViewMatrix,modelWorldViewProjectionMatrix);
		modelWorldMatrix.invert(normalMatrix);
		aProjectionMatrix.mul(aViewMatrix,viewProjectionMatrix);
		normalMatrix.transpose(normalMatrix);
		

		
		Matrix4f shadowLightMatrix1 = new Matrix4f();
		Matrix4f translationLightM = new Matrix4f();
		translationLightM.translation(new Vector3f((float)(x  ),(float)(y  ),(float)(z )));
		translationLightM.mul(aModelMatrix,translationLightM);
		shadowLightMatrix1.mul(translationLightM,shadowLightMatrix1);
		lightMatrix1.mul(shadowLightMatrix1,shadowLightMatrix1);
		Matrix4f shadowLightMatrix2 = new Matrix4f();
		shadowLightMatrix2.mul(translationLightM,shadowLightMatrix2);
		lightMatrix2.mul(shadowLightMatrix2,shadowLightMatrix2);
		Matrix4f shadowLightMatrix3 = new Matrix4f();
		shadowLightMatrix3.mul(translationLightM,shadowLightMatrix3);
		lightMatrix3.mul(shadowLightMatrix3,shadowLightMatrix3);
		Matrix4f shadowLightMatrix4 = new Matrix4f();
		shadowLightMatrix4.mul(translationLightM,shadowLightMatrix4);
		lightMatrix4.mul(shadowLightMatrix4,shadowLightMatrix4);
		Matrix4f shadowLightMatrix5 = new Matrix4f();
		shadowLightMatrix5.mul(translationLightM,shadowLightMatrix5);
		lightMatrix5.mul(shadowLightMatrix5,shadowLightMatrix5);
		
		

		Vector3f waterBody1P = new Vector3f(-59.1f,-15.971f,0.01f);
		Vector2f waterBody1D = new Vector2f(800.0f,800.0f);
		Vector2f waterBody1S = new Vector2f(24.0f,0.808f);
	
		
		   try (MemoryStack stack = MemoryStack.stackPush()) {
			   FloatBuffer shaderModelMatrix = stack.mallocFloat(16);
			   FloatBuffer shaderModelWorldMatrix = stack.mallocFloat(16);
			   FloatBuffer shaderModelWorldViewMatrix = stack.mallocFloat(16);
			   FloatBuffer shaderModelWorldViewProjectionMatrix = stack.mallocFloat(16);
			   FloatBuffer shaderViewMatrix = stack.mallocFloat(16);
			   FloatBuffer shaderViewProjectionMatrix = stack.mallocFloat(16);

		        
		        FloatBuffer shaderLightMatrix1 = stack.mallocFloat(16);
		        FloatBuffer shaderLightMatrix2 = stack.mallocFloat(16);
		        FloatBuffer shaderLightMatrix3 = stack.mallocFloat(16);
		        FloatBuffer shaderLightMatrix4 = stack.mallocFloat(16);
		        FloatBuffer shaderLightMatrix5 = stack.mallocFloat(16);
		        
		        aModelMatrix.get(shaderModelMatrix);
		        modelWorldMatrix.get(shaderModelWorldMatrix);
		        modelWorldViewMatrix.get(shaderModelWorldViewMatrix);
		        modelWorldViewProjectionMatrix.get(shaderModelWorldViewProjectionMatrix);
		        viewProjectionMatrix.get(shaderViewProjectionMatrix);
		        aViewMatrix.get(shaderViewMatrix);
		        //normalMatrix.get(shaderNormalMatrix);
		        //cameraPosition.get(shaderCameraPosition);
		        
	            
	            shadowLightMatrix1.get(shaderLightMatrix1);
	            shadowLightMatrix2.get(shaderLightMatrix2);
	            shadowLightMatrix3.get(shaderLightMatrix3);
	            shadowLightMatrix4.get(shaderLightMatrix4);
	            shadowLightMatrix5.get(shaderLightMatrix5);
	    		
	            
	            FloatBuffer waterBody1PV= stack.mallocFloat(3);
	    		FloatBuffer waterBody1DV= stack.mallocFloat(2);
	            FloatBuffer waterBody1SV = stack.mallocFloat(2);
	    		

    	        waterBody1P.get(waterBody1PV);
    	        waterBody1D.get(waterBody1DV);
    	        waterBody1S.get(waterBody1SV);
    			
    	        glUniformMatrix4fv(modelMatrixUniformLocation, false, shaderModelMatrix);
		        glUniformMatrix4fv(modelWorldMatrixUniformLocation, false, shaderModelWorldMatrix);
		        glUniformMatrix4fv(modelWorldViewMatrixUniformLocation, false, shaderModelWorldViewMatrix);
		        glUniformMatrix4fv(modelWorldViewProjectionMatrixUniformLocation, false, shaderModelWorldViewProjectionMatrix);
		        glUniformMatrix4fv(viewProjectionMatrixUniformLocation, false, shaderViewProjectionMatrix);
		        glUniformMatrix4fv(viewMatrixUniformLocation, false, shaderViewMatrix);
		        //glUniformMatrix4fv(normalMatrixUniformLocation, false, shaderNormalMatrix);
			    //glUniform3fv(cameraPositionUniformLocation, shaderCameraPosition);
		        
		        glUniformMatrix4fv(lightMatrix1Location, false, shaderLightMatrix1); 
		        glUniformMatrix4fv(lightMatrix2Location, false, shaderLightMatrix2); 
		        glUniformMatrix4fv(lightMatrix3Location, false, shaderLightMatrix3); 
		        glUniformMatrix4fv(lightMatrix4Location, false, shaderLightMatrix4); 
		        glUniformMatrix4fv(lightMatrix5Location, false, shaderLightMatrix5); 
		        
		        glActiveTexture(GL_TEXTURE0 + 2);
		        glBindTexture(GL_TEXTURE_2D,shadowMap1Unit);
		        glUniform1i(shadowMap1Location,2);
		       glActiveTexture(GL_TEXTURE0 + 3);
		        glBindTexture(GL_TEXTURE_2D,shadowMap2Unit);
		       glUniform1i(shadowMap2Location,3);
		       glActiveTexture(GL_TEXTURE0 + 4);
		        glBindTexture(GL_TEXTURE_2D,shadowMap3Unit);
		        glUniform1i(shadowMap3Location,4);
		       glActiveTexture(GL_TEXTURE0 + 5);
		        glBindTexture(GL_TEXTURE_2D,shadowMap4Unit);
		        glUniform1i(shadowMap4Location,5);
		        glActiveTexture(GL_TEXTURE0 + 6);
		        glBindTexture(GL_TEXTURE_2D,shadowMap5Unit);
		        glUniform1i(shadowMap5Location,6);
		        
			       glUniform3fv(waterBody1PosLocation,  waterBody1PV);
			       glUniform2fv(waterBody1DimensionsLocation,  waterBody1DV);
			       glUniform2fv(waterBody1ScaleLocation,  waterBody1SV);
			       

			   
		        	glActiveTexture(GL_TEXTURE0 + 14);
		        	glBindTexture(GL_TEXTURE_2D,this.waterHeightFields[0]);
		        	glUniform1i(waterBody1Location,14);

		   }
		   
		   
		if (keyInput == -1) {
			rightWPressed = true;
			leftWPressed = false;
		}
		if (keyInput == 1) {
			leftWPressed = true;
			rightWPressed = false;
		}
		if (rightWPressed) {
	        glUseProgram(programId);
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        glDrawArrays(GL_TRIANGLES,0,(vertices.length)/3);
	        
	        glDisableVertexAttribArray(0);
		}
		if (leftWPressed) {
	        glUseProgram(programId);
	        
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);

          glDrawArrays(GL_TRIANGLES,0,(vertices.length)/3);
		}
	}


	public double getTargetHorizontalThresholdLeft() {
		return targetHorizontalThresholdLeft;
	}

	public void setTargetHorizontalThresholdLeft(double targetHorizontalThresholdLeft) {
		this.targetHorizontalThresholdLeft = targetHorizontalThresholdLeft;
	}

	public double getTargetHorizontalThresholdRight() {
		return targetHorizontalThresholdRight;
	}

	public void setTargetHorizontalThresholdRight(double targetHorizontalThresholdRight) {
		this.targetHorizontalThresholdRight = targetHorizontalThresholdRight;
	}



	public boolean isKeepMovingBG() {
		return keepMovingBG;
	}

	public void setKeepMovingBG(boolean keepMovingBG) {
		this.keepMovingBG = keepMovingBG;
	}

	public boolean isPanningD() {
		return panningD;
	}

	public void setPanningD(boolean panningD) {
		this.panningD = panningD;
	}

	public boolean isPanningU() {
		return panningU;
	}

	public void setPanningU(boolean panningU) {
		this.panningU = panningU;
	}
	
	
	
	public void setBG(Background b) {
		this.b = b;
	}

	public double getVerticalPanDistance() {
		return verticalPanDistance;
	}

	public void setVerticalPanDistance(double verticalPanDistance) {
		this.verticalPanDistance = verticalPanDistance;
	}

	public double getvPanX() {
		return vPanX;
	}

	public void setvPanX(double vPanX) {
		this.vPanX = vPanX;
	}

	public double getvPanY() {
		return vPanY;
	}

	public void setvPanY(double vPanY) {
		this.vPanY = vPanY;
	}

	public void addWaterHeightField(int heightField) {
		waterHeightFields[0] = heightField;
	}
	
	public boolean isWithinCameraInterval() {
		return withinCameraInterval;
	}

	public void setWithinCameraInterval(boolean withinCameraInterval) {
		this.withinCameraInterval = withinCameraInterval;
	}
	

	public void setFallTime(double fallTime) {
		this.fallTime = fallTime;
	}
	public void setJumpTime(double jumpTime) {
		this.jumpTime = jumpTime;
			
	}
	public double getFallTime() {
		return this.fallTime;
	}
	public double getJumpTime() {
		return this.jumpTime;
			
	}
	
	private float snap(float f, float multiple) {
	System.out.println("Before snapping, " + f);
	System.out.println("After snapping, " + Math.round(f/multiple)*multiple);
	
		return Math.round(f/multiple)*multiple;
	
	}

	@Override
	public ArrayList<Vector2d> getFeetPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFeetPositions(Vector2f[] points) {
		// TODO Auto-generated method stub
		
	}

	public void setFallVelocity(double fallVelocity) {
		this.fallVelocity = fallVelocity;
	}
	
	public double getFallVelocity() {
		return this.fallVelocity;
	}
	
	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}
	
	public double getVelocityX() {
		return this.velocityX;
	}
	
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}
	
	public double getVelocityY() {
		return this.velocityY;
	}
	
	public String getFacingDirection() {
		return this.facingDirection;
	}
	
	public void setFacingDirection(String facingDirection) {
		this.facingDirection = facingDirection;
	}

	@Override
	public double getInitialFallVelocity() {
		return this.initialFallVelocity;
	}

	@Override
	public void setInitialFallVelocity(double initialFallVelocity) {
		this.initialFallVelocity = initialFallVelocity;
		
	}

	@Override
	public Vector2d getMotionVector() {
		return this.motionVector;
	}

	@Override
	public void setMotionVector(Vector2d motionVector) {
		this.motionVector = motionVector;
		
	}
	
	@Override
	public boolean getSlidingOnWall() {
		return this.slidingOnWall;
	}

	@Override
	public void setSlidingOnWall(boolean slidingOnWall) {
		this.slidingOnWall = slidingOnWall;
	}

	@Override
	public void setEntitiesSlopedWallAngle(double angle) {
		this.slopedWallAngle = angle;
	}

	@Override
	public double getEntitiesSlopedWallAngle() {
		return this.slopedWallAngle;
	}
	
	
	
	
}
