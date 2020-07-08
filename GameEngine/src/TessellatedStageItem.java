import java.awt.Graphics2D;



import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL40.*;

import static org.lwjgl.opengl.GL12.*;







// package org.joml.lwjgl; 
 
 
 import org.joml.Matrix4f;
import org.joml.Matrix3f;
import org.joml.Matrix4d;
 import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector3d;
 import org.joml.Quaternionf;
import org.joml.Vector2f;

import java.util.ArrayList;








public class TessellatedStageItem {

	private double x,y,z;
	private float[] vertices, normals, tangents, biTangents;
	private int[] indicies;
	private IntBuffer indexBuffer;
	private ArrayList<Wrap> materials;
	
	private ArrayList<float[]> uvsMaterials;
	private int programId, vShaderId, fShaderId;
	private int shadowProgramId, shadowVShaderId, shadowFShaderId;
	
	private int uMVPLocation, sampler1Location;
	private int directionalLightTypeLocation, directionalLightDirectionLocation, directionalLightColorLocation, directionalLightAmountLocation;
	
	private int shadowLightMatrixLocation, shadowMap1Location, shadowMap2Location, shadowMap3Location, shadowMap4Location;
	
	private int screenBrightnessLocation;
	private int vaoId, vboIdV, vboIdN, vboIdT1, vboIdT2, eboID;
	private int vertexCount, normalsCount, tCount;
	private Background b;
	private float lightCRedChannel, lightCGreenChannel, lightCBlueChannel;
	private float lightCRedChannelDelta;
	private float size;
	private float rotationX, rotationY, rotationZ;
	private int lightMatrixLocation;
	private int shadowLightMatrix1Location;
	private int shadowLightMatrix2Location;
	private int lightMatrix2Location;
	private int lightMatrix1Location;
	private int lightMatrix3Location;
	private int drawShadowProgramId;
	private int drawShadowVShaderId;
	private int drawShadowFShaderId;
	private int drawShadowUMVPLocation;
	private int drawShadowLightMatrix1Location;
	private int drawShadowLightMatrix2Location;
	private int drawShadowLightMatrix3Location;
	private int drawShadowShadowMap1Location;
	private int drawShadowShadowMap2Location;
	private int drawShadowShadowMap3Location;
	private int shadowsLocation;
	private int sceneShadowsLocation;
	private int shadowMapAnimated1Location;
	private int shadowMapAnimated2Location;
	private int shadowMapAnimated3Location;
	private int lightMatrixAnimatedLocation;
	private int lightMatrix4Location;
	private int randomRotationsLocation;
	private int uPLocation;
	private int isStandardShadowLocation;
	private int lightMatrix5Location;
	private int shadowMap5Location;
	private boolean normalMapped;
	private Wrap normalMap;
	private int normalMapLocation;
	private int hasNormalMapLocation;
	private int vboIdTans;
	private int tangentsCount;
	private Object bitangentsCount;
	private int vboIdBiTans;
	private int wMatrixLocation;
	private int vboIdNormalRotations;
	
	private boolean displacementMapped;
	private Wrap displacementMap;
	private int lightMatrixAnimated1Location;
	private int lightMatrixAnimated2Location;
	private int hasDisplacementMapLocation;
	private int displacementMapLocation;
	private int vMatrixLocation;
	private int vVecLocation;
	private int mMatrixLocation;
	private int materialModeLocation;
	
	private int materialM;
	
	private boolean hasMotion;
	private int testSearchRadiusLocation;
	private int waterBody1PosLocation;
	private int waterBody1DimensionsLocation;
	private int waterBody1ScaleLocation;
	private int waterBody1Location;
	private int waterHeightField1ID;
	private int lightMatrixAnimated3Location;
	private int normalTransformMLocation;
	private int depthPassProgramId;
	private int depthPassVShaderId;
	private int depthPassFShaderId;
//	private int depthPassMVPLocation;
	private int depthPassMVPMLocation;
	//private int depthPassNormalMLocation;
	private int depthPassMVMLocation;
	private int shadowCubeProgramId;
	private int shadowCubeVShaderId;
	private int shadowCubeFShaderId;
	private int shadowCubeLightMatrixLocation;
	private int shadowCubeLightPositionLocation;
	private int shadowCubeLightFarPlaneLocation;
	private int cameraPositionLocation;
	private int tControlShaderID;
	private int tEvaluationShaderID;
	private int tessellationEnabledLocation;
	private int wireFrameProgramId;
	private int fShaderIdWireFrame;
	private int vShaderIdWireFrame;
	private int uMVPWireFrameLocation;
	private int uWorldWireFrameLocation;
	private int tessellationEnabledWireFrameLocation;
	private int uModelWireFrameLocation;
	private int eyePositionWireFrameLocation;
	private int uVPWireFrameLocation;
	private int diffuseMapWireFrameLocation;
	private int displacementMapWireFrameLocation;
	private int vPMatrixLocation;
	private int eyePositionWorldSpaceLocation;
	
	public StageItem(double x, double y, double z, float[] vertices, float[] normals, int[] indicies, ArrayList<Wrap> materials, ArrayList<float[]> uvsMaterials, Background b, float size, float rotationX, float rotationY, float rotationZ, boolean isNormalMapped, Wrap normalMap, boolean isDisplacementMapped, Wrap displacementMap, boolean hasMotion, int waterHeightField) throws Exception {
		
		
		lightCRedChannel = 0.40f;
		lightCRedChannelDelta = 0.01f;
		
		this.indicies = indicies;
		
		System.out.println("This mesh has " + vertices.length + " vertices");
		this.x = x;
		this.y = y;
		this.z = z;
		this.vertices = vertices;
		this.normals = normals;
		this.materials = materials;
		this.uvsMaterials = uvsMaterials;
	
		this.setHasMotion(hasMotion);
		
		this.normalMapped = isNormalMapped;
		this.normalMap = normalMap;
		
		this.displacementMapped = isDisplacementMapped;
		this.displacementMap = displacementMap;
		
		this.waterHeightField1ID = waterHeightField;
		

		
		/* 
		 * * * * * * * * * * * * * * *
		 * GENERATE TANGENT VECTORS  *
		 * * * * * * * * * * * * * * *
		 */
		
		this.tangents = new float[vertices.length]; 
		this.biTangents = new float[vertices.length];
		
		for (int r = 0; r < tangents.length; r++) {
			
			this.tangents[r] = 0.0f;
			this.biTangents[r] = 0.0f;
			
			
		}
		
		for (int r = 0; r < indicies.length; r = r + 3) {
			
			
			// Get indices
			
			int i0 = this.indicies[r];
			int i1 = this.indicies[r + 1];
			int i2 = this.indicies[r + 2];
			System.out.println("Indicies: "+i0+", "+i1+", "+i2);
			
			// Get vertices for this polygon
			
			Vector3f v0 = new Vector3f(this.vertices[3*i0],this.vertices[3*i0 + 1],this.vertices[3*i0 + 2]);
			Vector3f v1 = new Vector3f(this.vertices[3*i1],this.vertices[3*i1 + 1], this.vertices[3*i1 + 2]);
			Vector3f v2 = new Vector3f(this.vertices[3*i2],this.vertices[3*i2 + 1],this.vertices[3*i2 + 2]);
			
			System.out.println("Vertices: "+v0+", "+v1+", "+v2);
			// Get uvs for this polygon
			
			Vector2f t0 = new Vector2f(this.uvsMaterials.get(0)[2*i0],this.uvsMaterials.get(0)[2*i0 +1]);
			Vector2f t1 = new Vector2f(this.uvsMaterials.get(0)[2*i1],this.uvsMaterials.get(0)[2*i1 + 1]);
			Vector2f t2 = new Vector2f(this.uvsMaterials.get(0)[2*i2],this.uvsMaterials.get(0)[2*i2 + 1]);
			
			System.out.println("UVs: "+t0+", "+t1+", "+t2);
			
            // Edges of the triangle : position delta
            Vector3f deltaPos1 = new Vector3f();
            v1.sub(v0,deltaPos1);
            Vector3f deltaPos2 = new Vector3f();
            v2.sub(v0,deltaPos2);

            System.out.println("deltaPos1 = "+deltaPos1+" and deltaPos2 = "+deltaPos2);
            
            // UV delta
            Vector2f deltaUV1 = new Vector2f();
            t1.sub(t0,deltaUV1);
            Vector2f deltaUV2 = new Vector2f();
            t2.sub(t0,deltaUV2);
            
            Vector3f placeHolder3f1 = new Vector3f();
            Vector3f placeHolder3f2 = new Vector3f();
            Vector3f placeHolder3f3 = new Vector3f();
            Vector3f placeHolder3f4 = new Vector3f();
            
            Vector2f placeHolder2f = new Vector2f();
            
            float d = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV1.y * deltaUV2.x);
            Vector3f tangent;
            (deltaPos1.mul(deltaUV2.y,placeHolder3f1).sub(deltaPos2.mul(deltaUV1.y,placeHolder3f2))).mul(d, tangent = new Vector3f());
            Vector3f bitangent;
            (deltaPos2.mul(deltaUV1.x,placeHolder3f3).sub(deltaPos1.mul(deltaUV2.x,placeHolder3f4))).mul(d, bitangent = new Vector3f());
            
            System.out.println("D is "+d);
            System.out.println("The tangent: "+tangent);
            
			this.tangents[3*i0] = this.tangents[3*i0] + tangent.x;
			this.tangents[3*i0 + 1] = this.tangents[3*i0 + 1] + tangent.y;
			this.tangents[3*i0 + 2] = this.tangents[3*i0 + 2] + tangent.z;
			
			this.tangents[3*i1] = this.tangents[3*i1] + tangent.x;
			this.tangents[3*i1 + 1] = this.tangents[3*i1 + 1] + tangent.y;
			this.tangents[3*i1 + 2] = this.tangents[3*i1 + 2] + tangent.z;
			
			this.tangents[3*i2] = this.tangents[3*i2] + tangent.x;
			this.tangents[3*i2 + 1] = this.tangents[3*i2 + 1] + tangent.y;
			this.tangents[3*i2 + 2] = this.tangents[3*i2 + 2] + tangent.z;
			
			
			this.biTangents[3*i0] = this.biTangents[3*i0] + bitangent.x;
			this.biTangents[3*i0 + 1] = this.biTangents[3*i0 + 1] + bitangent.y;
			this.biTangents[3*i0 + 2] = this.biTangents[3*i0 + 2] + bitangent.z;
			
			this.biTangents[3*i1] = this.biTangents[3*i1] + bitangent.x;
			this.biTangents[3*i1 + 1] = this.biTangents[3*i1 + 1] + bitangent.y;
			this.biTangents[3*i1 + 2] = this.biTangents[3*i1 + 2] + bitangent.z;
			
			this.biTangents[3*i2] = this.biTangents[3*i2] + bitangent.x;
			this.biTangents[3*i2 + 1] = this.biTangents[3*i2 + 1] + bitangent.y;
			this.biTangents[3*i2 + 2] = this.biTangents[3*i2 + 2] + bitangent.z;
			
			
		
		}
		

		for (int r = 0; r < indicies.length; r = r + 3) {
			
			int i0 = indicies[r];
			int i1 = indicies[r + 1];
			int i2 = indicies[r + 2];
			
			System.out.println(tangents[3*i2]);
			System.out.println("tangents and bitangents");
			System.out.println(new Vector3f(tangents[3*i0],tangents[3*i0 + 1],tangents[3*i0 + 2]));
			System.out.println(new Vector3f(tangents[3*i1],tangents[3*i1 + 1],tangents[3*i1 + 2]));
			System.out.println(new Vector3f(tangents[3*i2],tangents[3*i2 + 1],tangents[3*i2 + 2]));
			
			System.out.println(new Vector3f(biTangents[3*i0],biTangents[3*i0 + 1],biTangents[3*i0 + 2]));
			System.out.println(new Vector3f(biTangents[3*i1],biTangents[3*i1 + 1],biTangents[3*i1 + 2]));
			System.out.println(new Vector3f(biTangents[3*i2],biTangents[3*i2 + 1],biTangents[3*i2 + 2]));
			
		}
		

		
		
		
		
		this.b = b;
		
		this.size = size;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;

		
		
		/* ************************
		// Main Shader Programs
		************************ */
		
		//
		// Main Vertex Shader Code
		//
		
		
		String vShaderCode = "#version 430\n "+

"layout (location=0) in vec3 vCoord;\n"+
"layout (location=1) in vec3 nVec;"+
"layout (location=2) in vec2 tCoord;"+
"layout (location=3) in vec3 tangent;"+
"layout (location=4) in vec3 bitangent;"+

"struct LightSource {"
+ "int type;"
+ "vec3 directionOrLocation;"
+ "vec3 color;"
+ "float amount;"
+ "};"+






// UNDER CONSTRUCTION: TESSELLATION


"out vec4 fPosMVP;"+
"out vec4 fPosWorld;"+
"out vec4 fPosCamera;"+
"out vec3 tangentFPos;"+
"out vec3 tangentEyePos;"+
"out vec3 tangentDirectionToCamera;"+
" out vec3 normal2;"+
"out vec3 fNVec;"+
"out vec2 fTxt;"+
"out vec4 fPosLight1;"+
"out vec4 fPosLight2;"+
"out vec4 fPosLight3;"+
"out vec4 fPosLight4;"+
"out vec4 fPosLight5;"+
"out mat3 uTBN;"+

" out vec3 tang;"+

"out vec4 fPosLightAnimated1;"+
"out vec4 fPosLightAnimated2;"+
"out vec4 fPosLightAnimated3;"+





// Modifications for tessellation

	"out vec3 worldPosTCIn;"+
	"out vec2 tCoordTCIn;"+
	"out vec3 normalTCIn;"+
	"out vec3 tangentTCIn;"+
	"out vec3 bitangentTCIn;"+
	
"uniform mat4 mMatrix;"+
"uniform mat4 wMatrix\n;"+
"uniform mat4 vMatrix;\n"+
"uniform mat4 lightMatrix1;\n"+
"uniform mat4 lightMatrix2;\n"+
"uniform mat4 lightMatrix3;\n"+
"uniform mat4 lightMatrix4;\n"+
"uniform mat4 lightMatrix5;\n"+



"uniform int tessellationEnabled;"+

/*

" uniform mat3 normalTransformM;"+
"uniform int testSearchRadius;"+
"uniform LightSource directionalLight;"+

"uniform mat4 lightMatrixAnimated1;\n"+
"uniform mat4 lightMatrixAnimated2;\n"+
"uniform mat4 lightMatrixAnimated3;\n"+

"uniform int hasNormalMap;"+

"uniform mat4 uMVP;"+
"uniform mat4 uP;"+

"uniform vec3 vVec;"+

"uniform int materialMode;"+

"uniform vec3 cameraPosition;"+
*/
"void main()\n"+

"{\n"+

"float normalScaleBias = 0.0;"+

"if (tessellationEnabled == 1) {"+

"	  worldPosTCIn  = (wMatrix*mMatrix*vec4(vCoord,1.0)).xyz;"+
"     tCoordTCIn    = tCoord;"+
"     normalTCIn    = normalize(nVec);"+
"     tangentTCIn   = tangent;"+
"     bitangentTCIn = bitangent;"+

"     gl_Position = vec4(worldPosTCIn,1.0);"+

"} " +
/*
"else {"+

"     vec4 pos = uMVP*vec4(vCoord,1.0);"+
	" fPosMVP = pos;"+
	" fPosWorld = wMatrix*mMatrix*vec4(vCoord,1.0);"+
	
	" fNVec = normalize(mat3(mMatrix)*nVec);"+
	" fTxt = tCoord;"+

"     vec3 fPosM = (mMatrix*vec4(vCoord,1.0)).xyz;"+
	" fPosCamera = vMatrix*wMatrix*mMatrix*vec4(vCoord,1.0);"+
	" fPosLight1 = lightMatrix1*wMatrix*vec4((fPosM),1.0);"+
	" fPosLight2 = lightMatrix2*wMatrix*vec4((fPosM),1.0);"+
	" fPosLight3 = lightMatrix3*wMatrix*vec4((fPosM),1.0);"+
	" fPosLight4 = lightMatrix4*wMatrix*vec4((fPosM),1.0);"+
	" fPosLight5 = lightMatrix5*wMatrix*vec4((fPosM),1.0);"+


" 	 uTBN = mat3(normalize(mat3(mMatrix)*normalize(tangent)), normalize(mat3(mMatrix)*normalize(cross(normalize(nVec),normalize(tangent)))), normalize(mat3(mMatrix)*normalize(nVec)));"+

"	 mat3 invTBN = inverse(uTBN);"+

"	 vec3 worldDirectionToCamera = vec3(vVec.x - fPosWorld.x, vVec.y - fPosWorld.y, vVec.z - fPosWorld.z );"+


	"tangentDirectionToCamera = invTBN*(-1.0*(vMatrix*wMatrix*mMatrix*vec4(vCoord,1.0)).xyz);"+
	"tangentFPos = invTBN*(wMatrix*mMatrix*vec4(vCoord,1.0)).xyz;"+
	"tangentEyePos = invTBN*cameraPosition;"+



	" fPosLightAnimated1 = lightMatrixAnimated1*vec4(vCoord,1.0);"+
	" fPosLightAnimated2 = lightMatrixAnimated2*vec4(vCoord,1.0);"+
	" fPosLightAnimated3 = lightMatrixAnimated3*vec4(vCoord,1.0);"+
		
			
	
	

"    gl_Position = pos;\n" +

" }"+
*/
"}";
		
		
		
		//
		// Main Tessellation Shaders
		//
		
		
		
		
		// Main Tessellation Control Shader
		
		String tControlShaderCode = "#version 430\n"+


			//  define the number of CPs in the output patch
			"layout (vertices = 3) out;"+

			"uniform vec3 gEyeWorldPos;"+

			//  attributes of the input CPs
			"in vec3 worldPosTCIn[];"+
			"in vec2 tCoordTCIn[];"+
			"in vec3 normalTCIn[];"+
			"in vec3 tangentTCIn[];"+
			"in vec3 bitangentTCIn[];"+
			
			// uniforms
			
			"uniform vec3 eyePositionWorldSpace;"+
			"uniform mat4 vPMatrix;"+
			"uniform int hasDisplacementMap;"+
			"uniform sampler2D displacementMap;"+
				
			//	attributes of the output CPs
			
			"out vec3 worldPosTEIn[];"+
			"out vec2 tCoordTEIn[];"+
			"out vec3 normalTEIn[];"+		
			"out vec3 tangentTEIn[];"+
			"out vec3 bitangentTEIn[];"+
			"out vec3 actualNormalTEIn[];"+
			"out vec3 actualTangentTEIn[];"+
			
	 	// Determine Tessellation Levels
		
		"	float GetTessLevelV1(float Distance0, float Distance1)"+
			"{"+
				"float AvgDistance = (Distance0 + Distance1) / 2.0;"+
			
 			" float value = 1.0;"+
 
			 "if (hasDisplacementMap == 1) {"+
			 
			
			 
			    "if (AvgDistance <= 14.0) {"+
			    "   return value;"+ // 21 then 11
			    "}"+
			    "else if (AvgDistance <= 23.0) {"+
			    "   return mix(value,1.0,clamp((AvgDistance - 14.0)/9.0,0.0,1.0));"+
			    "}"+
			   
				    "else {"+
				    "   return 1;"+
				    "}"+
			" } else {"+	
			      " return 1.0;"+
			   "}"+
			  
			   "return 1.0;"+
			  //  " return ( mix(20.0, 3.0, clamp(AvgDistance / 55.0, 0.0, 1.0)) );"+
			
			
			
			"}"+
	
			
			" float ComputeInnerTessLevel(vec3 pos) {"
			+ "  return ( mix(25.0,25.0,clamp( abs(pos.z)/12.0, 0.0, 1.0 ) ) );"
			+ ""
			+ "}"+
			
			
			
			
			"void main()"+
			"{"+
		    // Set the control points of the output patch
			// Here, the output patch is just another triangle, same one as triangle input. The output triangle patch will be tessellated
		    "	tCoordTEIn[gl_InvocationID] = tCoordTCIn[gl_InvocationID];"+
			"	normalTEIn[gl_InvocationID] = normalize(normalTCIn[gl_InvocationID]);"+
		    "	worldPosTEIn[gl_InvocationID] = worldPosTCIn[gl_InvocationID];"+
		    "   tangentTEIn[gl_InvocationID]  = tangentTCIn[gl_InvocationID];"+
		    "   bitangentTEIn[gl_InvocationID]  = bitangentTCIn[gl_InvocationID];"+
		    
		    
		    
		    
		    
		    // Compute the real normal vector to this polygon
		    
		    "  vec3 edge1 = worldPosTCIn[1] - worldPosTCIn[0];"+
		    "  vec3 edge2 = worldPosTCIn[2] - worldPosTCIn[0];"+
		    "  vec2 dUV1  = tCoordTCIn[1]   - tCoordTCIn[0];"+
		    "  vec2 dUV2  = tCoordTCIn[2]   - tCoordTCIn[0];"+
		    
		    "  vec3 actualNormal = normalize(cross(edge1,edge2));"+
		    
		    "  if (dot(actualNormal,normalTEIn[0]) < 0.0) {"+
		    "      actualNormal = -1.0*actualNormal;"+
		    "  }"+
		    
		    "  actualNormalTEIn[gl_InvocationID] = actualNormal;"+
		    
		    
		    // Compute the real tangent vector to this polygon
		    
		    "  float f = 1.0f / (dUV1.x * dUV2.y - dUV2.x * dUV1.y);" + 
		    
		    "  vec3 actualTangent = vec3(1.0,0.0,1.0);"+
		
			"  actualTangent.x = f * (dUV2.y * edge1.x - dUV1.y * edge2.x);"+
			"  actualTangent.y = f * (dUV2.y * edge1.y - dUV1.y * edge2.y);"+
			"  actualTangent.z = f * (dUV2.y * edge1.z - dUV1.y * edge2.z);"+
			
		    "  normalize(-actualTangent);"+
			
		    "  actualTangentTEIn[gl_InvocationID] = actualTangent;"+
		   
		    

		    // Calculate the distance from the camera to the three control points
		    "	float EyeToVertexDistance0 = distance(eyePositionWorldSpace, worldPosTCIn[0]);"+
		    "	float EyeToVertexDistance1 = distance(eyePositionWorldSpace, worldPosTCIn[1]);"+
		    "	float EyeToVertexDistance2 = distance(eyePositionWorldSpace, worldPosTCIn[2]);"+
 
		    // Calculate the tessellation levels
		    

		    
		    "   float dist12 = distance(worldPosTCIn[1], worldPosTCIn[2]);"+
		    "   float dist20 = distance(worldPosTCIn[2], worldPosTCIn[0]);"+
		    "   float dist01 = distance(worldPosTCIn[0], worldPosTCIn[1]);"+
		    "   float edge0Scalar;"+
		    "   float edge1Scalar;"+
		    "   float edge2Scalar;"+
		    
		    " float maxEdgeScalar = 1.0;"+           // 2.8
		    " float maxScalarLength = 6.0;"+         // 1.8
			" float startingDistanceValue = 0.60;"+
			
		    "  if (dist12 <= startingDistanceValue) {"+
		    "     edge0Scalar = 1.0;"+
		    "  } else {"+
		    "     edge0Scalar = mix(1.0,maxEdgeScalar, clamp( (dist12 - startingDistanceValue)/ maxScalarLength, 0.0, 1.0) );"+
		    "  }"+
		    "  if (dist20 <= startingDistanceValue) {"+
		    "     edge1Scalar = 1.0;"+
		    "  } else {"+
		    "     edge1Scalar = mix(1.0,maxEdgeScalar, clamp( (dist20 - startingDistanceValue)/ maxScalarLength, 0.0, 1.0) );"+
		    "  }"+
		    "  if (dist01 <= startingDistanceValue) {"+
		    "     edge2Scalar = 1.0;"+
		    "  } else {"+
		    "     edge2Scalar = mix(1.0,maxEdgeScalar, clamp( (dist01 - startingDistanceValue)/ maxScalarLength, 0.0, 1.0) );"+
		    "  }"+
		    
		    
		    /*
		    "float sampleRadius1 = 0.6;"+
		    "int loopValue = 3;"+
		    "float displacementValue1 = 0.0;"+
		    "float displacementValue = 0.0;"+
		    
    " for (int g = -loopValue; g <= loopValue; g++) {"+
    "   for (int b = -loopValue; b <= loopValue; b++) {"+
    "      displacementValue1 += texture(displacementMap, vec2(tCoordTCIn[1].x + sampleRadius1*g/textureSize(displacementMap,0).x, tCoordTCIn[1].y + sampleRadius1*b/textureSize(displacementMap,0).y)).r;" +
    "   }"+
    " }"+
    " displacementValue1 = displacementValue1/pow(2*loopValue + 1, 2);"+
    " displacementValue = displacementValue1 ;"+
*/

		    // Area of polygon 
		    
		    //"  float area = abs(dot(worldPosTCIn[0], cross(worldPosTCIn[1] - worldPosTCIn[0], worldPosTCIn[2] - worldPosTCIn[0])));"+
		    
	" if ( (EyeToVertexDistance1+ EyeToVertexDistance2)/2.0 > 18.0) {"+
	//"               edge0Scalar = 1.0;"+
	" }"+
	" if ( (EyeToVertexDistance2+ EyeToVertexDistance0)/2.0 > 18.0) {"+
	//"               edge1Scalar = 1.0;"+
	" }"+
	" if ( (EyeToVertexDistance0+ EyeToVertexDistance1)/2.0 > 18.0) {"+
	//"               edge2Scalar = 1.0;"+
	" }"+
	   "		gl_TessLevelOuter[0] = edge0Scalar*GetTessLevelV1(EyeToVertexDistance1, EyeToVertexDistance2);"+
		    "	gl_TessLevelOuter[1] = edge1Scalar*GetTessLevelV1(EyeToVertexDistance2, EyeToVertexDistance0);"+
		    "	gl_TessLevelOuter[2] = edge2Scalar*GetTessLevelV1(EyeToVertexDistance0, EyeToVertexDistance1);"+
	

		  // "	gl_TessLevelInner[0] = (gl_TessLevelOuter[2] + gl_TessLevelOuter[1] + gl_TessLevelOuter[0])/3.0;"+
		  //"	gl_TessLevelInner[0] = (gl_TessLevelOuter[2]);"+
	    " gl_TessLevelInner[0] = max(max(gl_TessLevelOuter[0],gl_TessLevelOuter[1]),gl_TessLevelOuter[2]);"+
		  
		/*
		" 	  gl_TessLevelOuter[0] = 1.0;"+
			" gl_TessLevelOuter[1] = 1.0;"+
			" gl_TessLevelOuter[2] = 1.0;"+
			" gl_TessLevelInner[0] = 1.0;"+
		*/
		    "}";
				
				
		
		
		
		//  Main Tessellation Evaluation Shader
		
		String tEvaluationShaderCode = "#version 430\n"+

"precision highp float;"+

		  "layout(triangles, fractional_odd_spacing, ccw) in;"+
		  //"layout(triangles, equal_spacing, ccw) in;"+

			"in vec3 worldPosTEIn[];"+
			"in vec2 tCoordTEIn[];"+
			"in vec3 normalTEIn[];"+		
			"in vec3 tangentTEIn[];"+
			"in vec3 bitangentTEIn[];"+
			"in vec3 actualNormalTEIn[];"+ 
			"in vec3 actualTangentTEIn[];"+ 
			
			"struct LightSource {"
			+ "int type;"
			+ "vec3 directionOrLocation;"
			+ "vec3 color;"
			+ "float amount;"
			+ "};"+
			
			"out vec4 fPosMVP;"+
			"out vec4 fPosWorld;"+
			"out vec4 fPosCamera;"+
			"out vec3 tangentFPos;"+
			"out vec3 tangentEyePos;"+
			"out vec3 tangentDirectionToCamera;"+
			"out vec3 normal2;"+
			"out vec3 fNVec;"+
			"out vec3 fActualNormal;"+
			"out vec3 fActualTangent;"+
			"out vec2 fTxt;"+
			"out vec4 fPosLight1;"+
			"out vec4 fPosLight2;"+
			"out vec4 fPosLight3;"+
			"out vec4 fPosLight4;"+
			"out vec4 fPosLight5;"+
			"out mat3 uTBN;"+
			"out vec3 tang;"+
			"out vec4 fPosLightAnimated1;"+
			"out vec4 fPosLightAnimated2;"+
			"out vec4 fPosLightAnimated3;"+
			
			
			// NEW Parallax Occlusion Mapping
			"out vec3 vViewPos;"+
			
			"uniform mat4 mMatrix;"+
			"uniform mat4 wMatrix\n;"+
			"uniform mat4 vMatrix;\n"+
			"uniform mat4 vPMatrix;\n"+
			"uniform mat4 lightMatrix1;\n"+
			"uniform mat4 lightMatrix2;\n"+
			"uniform mat4 lightMatrix3;\n"+
			"uniform mat4 lightMatrix4;\n"+
			"uniform mat4 lightMatrix5;\n"+
			"uniform mat3 normalTransformM;"+
			"uniform int testSearchRadius;"+
			"uniform LightSource directionalLight;"+
			"uniform mat4 lightMatrixAnimated1;\n"+
			"uniform mat4 lightMatrixAnimated2;\n"+
			"uniform mat4 lightMatrixAnimated3;\n"+
			"uniform int hasNormalMap;"+
			"uniform mat4 uMVP;"+
			"uniform mat4 uP;"+
			"uniform vec3 vVec;"+
			"uniform int materialMode;"+
			"uniform vec3 cameraPosition;"+
			"uniform sampler2D displacementMap;"+
			"uniform int hasDisplacementMap;"+
			
			
			
			
"			vec2 interpolate2D(vec2 v0, vec2 v1, vec2 v2)                                                   "+
"			{                                                                                               "+
"			    return vec2(gl_TessCoord.x) * v0 + vec2(gl_TessCoord.y) * v1 + vec2(gl_TessCoord.z) * v2;   "+
"			}                                                                                               "+
	                                                                                                
"			vec3 interpolate3D(vec3 v0, vec3 v1, vec3 v2)                                                   "+
"			{                                                                                              "+ 
"			    return vec3(gl_TessCoord.x) * v0 + vec3(gl_TessCoord.y) * v1 + vec3(gl_TessCoord.z) * v2;"+   
"			}"+                                                                                               
                                                                                        

		"    void main() {"+

		
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
					

			"     fTxt = interpolate2D(tCoordTEIn[0], tCoordTEIn[1], tCoordTEIn[2]);"+
			"     fNVec = normalize(interpolate3D(normalTEIn[0], normalTEIn[1], normalTEIn[2]));"+
			"     fActualNormal  = actualNormalTEIn[2];"+
			"     fActualTangent = actualTangentTEIn[2];"+
			"     float displacementValue1 = 0.0;"+	
			"     float displacementValue2 = 0.0;"+	
			"     float displacementValue = 0.0;"+	
			
			
			" float maxDisp = -1.0;"+
			" float minDisp = 1.0;"+
			
			"if (hasDisplacementMap == 1) {"+
				" float weight[] = {0.5,0.25};"+
				" int   loopValue    = 1;"+
				" float sampleRadius1 = 0.01;"+             // Originally 0.15, changed to 0.2 for slightly less artifacts
			
			    " for (int g = -loopValue; g <= loopValue; g++) {"+
			    "   for (int b = -loopValue; b <= loopValue; b++) {"+
			    "      displacementValue1 += texture(displacementMap, vec2(fTxt.x + sampleRadius1*g/textureSize(displacementMap,0).x, fTxt.y + 0.0*sampleRadius1*b/textureSize(displacementMap,0).y)).r;" +
			    "   }"+
			    " }"+
			    " displacementValue1 = displacementValue1/pow(2*loopValue + 1, 2);"+
			    " displacementValue = displacementValue1 ;"+
			    //" displacementValue = displacementValue + 0.5*(abs(maxDisp - minDisp));"+
			    //" displacementValue = maxDisp;"+
			    //"displacementValue = texture(displacementMap, fTxt).r;"+
			"}"+
			
			"float dispScalar = 0.0;"+
			"float dispOffset = 0.0;"+

			"if (materialMode == 0) {"+
			"     dispScalar = 0.0;"+
			"     dispOffset = 0.0;"+
			"}"+
			
			"     vec4 posWorld = vec4(interpolate3D(worldPosTEIn[0], worldPosTEIn[1], worldPosTEIn[2]), 1.0) + dispScalar*vec4(fNVec,0.0)*displacementValue "
			+ "- dispOffset*vec4(fNVec,0.0);"+
			
			
				" fPosMVP = vPMatrix*posWorld;"+
				" fPosWorld = posWorld;"+
				" fPosCamera = vMatrix*posWorld;"+
				" fPosLight1 = lightMatrix1*posWorld;"+
				" fPosLight2 = lightMatrix2*posWorld;"+
				" fPosLight3 = lightMatrix3*posWorld;"+
				" fPosLight4 = lightMatrix4*posWorld;"+
				" fPosLight5 = lightMatrix5*posWorld;"+
			
			"    vec3 tVec = normalize(interpolate3D(tangentTEIn[0], tangentTEIn[1], tangentTEIn[2]));"+
			
		" 	 uTBN = mat3(tVec, normalize(cross(fNVec,tVec)), normalize(fNVec));"+
			//"   uTBN = mat3(normalize(fActualTangent), normalize(cross(normalize(fActualNormal),normalize(fActualTangent))), normalize(fActualNormal));"+
			"	 mat3 invTBN = transpose(uTBN);"+
			
			"	 vec3 worldDirectionToCamera = vec3(vVec.x - fPosWorld.x, vVec.y - fPosWorld.y, vVec.z - fPosWorld.z );"+
			
			
				"tangentDirectionToCamera = invTBN*(-1.0*(vMatrix*posWorld).xyz);"+
				"tangentFPos = invTBN*(posWorld).xyz;"+
				"tangentEyePos = invTBN*cameraPosition;"+
			
			
			
				" fPosLightAnimated1 = lightMatrixAnimated1*posWorld;"+
				" fPosLightAnimated2 = lightMatrixAnimated2*posWorld;"+
				" fPosLightAnimated3 = lightMatrixAnimated3*posWorld;"+
				
				" vViewPos = -(vMatrix*posWorld).xyz;"+
			
			"    gl_Position = fPosMVP;\n" +

		
		  "}"
		  ;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//
		// Main Fragment Shader
		//
		
		String fShaderCode = "#version 430\n "+

"precision highp float;"+
"in vec4 fPosMVP;"+
"in vec4 fPosWorld;"+
"in vec3 tangentFPos;"+
"in vec3 tangentEyePos;"+
" in vec3 tangentDirectionToCamera;"+
" in vec3 normal2;"+
" in vec4 fPosCamera;"+
"in vec4 fPosLight1;"+
"in vec4 fPosLight2;"+
"in vec4 fPosLight3;"+
"in vec4 fPosLight4;"+
"in vec4 fPosLight5;"+
"in mat3 uTBN;"+
"in vec4 fPosLightAnimated1;"+
"in vec4 fPosLightAnimated2;"+
"in vec4 fPosLightAnimated3;"+


" in vec3 tang;"+

"in vec3 vViewPos;"+















"in vec3 fNVec;"+
"in vec3 fActualNormal;"+
"in vec3 fActualTangent;"+
"in vec2 fTxt;"+

"layout (location = 0) out vec4 albedoColor;\n"+
"layout (location = 1) out vec4 colorBright;\n"+
"layout (location = 2) out vec4 normal;\n"+
"layout (location = 3) out vec4 position;\n"+
"layout (location = 4) out vec4 sceneShadowAmount;\n"+
"layout (location = 5) out vec4 refractiveObject;\n"+
//"layout (location = 6) out vec4 shadowBlurRadius;\n"+

"struct LightSource {"
+ "int type;"
+ "vec3 directionOrLocation;"
+ "vec3 color;"
+ "float amount;"
+ "};"+

"struct WaterBody{"
+ "vec3 pos;"
+ "vec2 dimensions;"
+ "vec2 scale;"
+ "};"+
"uniform sampler2D waterBody1HeightField;"+
"uniform WaterBody waterBody1;"+


"uniform sampler2D sampler1;"+
"uniform sampler2D normalMap;"+
"uniform sampler2D displacementMap;"+
"uniform sampler2D shadowMap1;"+
"uniform sampler2D shadowMap2;"+
"uniform sampler2D shadowMap3;"+
"uniform sampler2D shadowMap4;"+
"uniform sampler2D shadowMap5;"+
"uniform int hasNormalMap;"+
"uniform int hasDisplacementMap;"+
"uniform sampler3D randomRotations;"+
"uniform sampler2D shadowMapAnimated1;"+
"uniform sampler2D shadowMapAnimated2;"+
"uniform sampler2D shadowMapAnimated3;"+

"uniform mat4 lightMatrix1;\n"+
"uniform mat4 lightMatrix2;\n"+
"uniform mat4 lightMatrix3;\n"+
"uniform mat4 lightMatrix4;\n"+
"uniform mat4 lightMatrix5;\n"+

"uniform vec3 cameraPosition;"+
"uniform mat4 vMatrix;"+
"uniform mat4 uP;"+

"uniform LightSource directionalLight;"+ 
"uniform vec3 vVec;"+

" uniform mat3 normalTransformM;"+

"uniform int materialMode;"+
"uniform int testSearchRadius;"+

"vec3 modifiedWSPosition = fPosWorld.xyz;"+









"vec2 parallaxMap(vec2 tC, vec3 vDirection, float scale, float sampleCount) {"+
 
" float layerCount = 32.0;"+
" int nMaxLayers= 28"
+ ";"+
" int nMinLayers = 8;"+

" vec3 E = normalize(tangentDirectionToCamera);"+
" vec3 N = normalize(normal2);"+

//"layerCount = mix(nMaxLayers, nMinLayers, pow(abs(dot(vec3(0.0, 0.0, 1.0), normalize(tangentDirectionToCamera) )), 1.0) );"+






"   float layerDepth = 1.0/layerCount;"+
"   float presentLayerDepth = 0.0;"+
"   float pScale = 0.0145;"+
//
// View
//
"    vec2 p;"+
"    p = (vDirection.xy*pScale)/(vDirection.z) ;"+

    "vec2 dTC = p / layerCount;"+
    
    "vec2 presentTC = tC;"+
    "float presentDepth =    1.0 - texture(displacementMap, presentTC).r;"+
    
    "while (presentLayerDepth < presentDepth) {"+
    "	presentTC -= dTC;"+
    "	presentDepth =  1.0 - texture(displacementMap, presentTC).r ;"+
    "	presentLayerDepth += layerDepth;"+
    "}"+
    
    "vec2 previousTC = presentTC + dTC;"+
    "float afterDepth = presentDepth - presentLayerDepth;"+
    "float beforeDepth =    1.0 - texture(displacementMap,previousTC).r  - presentLayerDepth + layerDepth;"+
    "float weight = afterDepth / (afterDepth - beforeDepth);"+
    "vec2 finalTC = weight*previousTC + (1.0 - weight)*presentTC;"+
  
    //
    // Calculate modified World Space position
    //
    /*
    " float interpolatedDepth = weight*beforeDepth + (1.0 - weight)*afterDepth;"+
    
    " float wSDepth =  ( 20.0*pScale/(clamp(1.0, 0.0, 1.0)) ) * presentDepth;"+
    
    " vec3 norm = texture(normalMap, finalTC).rgb;"+
    " norm = 2.0*norm - vec3(1.0,1.0,1.0);"+
    " norm = fNVec;"+
    
    " vec3 bottomPos = fPosWorld.xyz - normalize(norm)*wSDepth;"+
    
    " float d1 = dot(normalize(norm), bottomPos - fPosWorld.xyz);"+
    " vec3 camToPix = normalize(fPosWorld.xyz - cameraPosition);"+
    " float d2 = dot(camToPix,normalize(norm));"+
    
    " modifiedWSPosition = fPosWorld.xyz;"+
    
    " if (d2 != 0.0) {"+
    
   // "   if (materialMode == 1) {"+    
    //
   "    modifiedWSPosition = modifiedWSPosition + camToPix*(d1/d2)   +  vec3(0.0,0.0,0.0);"+
     //"  }"+
    " }"+
    */
    "return finalTC;"+    

    //" return tC;"+






//"return (fTxt + vPresentOffset);"+  



"}"+



"void main()\n"+
"{\n"+






//
// Parallax Occlusion Mapping
//

" vec2 tCoordActual;"+

" if (hasDisplacementMap == 1 && fPosMVP.z <= 55.0) {"+
"  tCoordActual = fTxt;"+
"if (materialMode == 1) {"+
"  tCoordActual = parallaxMap(fTxt, normalize((tangentEyePos - tangentFPos)), 1.0 , 32);"+
"}"+
"  if (tCoordActual.x > 1.0 || tCoordActual.y > 1.0 || tCoordActual.x < 0.0 || tCoordActual.y < 0.0) {"+
"      discard;"+
" }"+

"}"+
" else {"+

"  tCoordActual = fTxt;"+
" }"+





"vec3 positionMVP = (uP*vMatrix*vec4(modifiedWSPosition,1.0)).xyz;"+






" vec2 sampleCoord = ((fPosMVP.xyz)/fPosMVP.w).xy;"+
" sampleCoord = 0.5*sampleCoord + 0.5;"+

"float shadow = 0.0;"+
"float shadowAnimated = 0.0;"+

" float biasDot = abs(dot(1.0*normalize(-1.0*directionalLight.directionOrLocation), 1.0*normalize(1.0*fNVec)));"+

"float biasMagnitude = 0.002;"+
"float biasDistanceSofteningMagnitude = 0.042;"+

" float bias = max( biasMagnitude * (  pow(1.0 - biasDot,1.250) ) , 0.001);" +
" bias = 0.001 + mix( 0.00, biasMagnitude, (  pow(1.0 - biasDot,4.250) ) );" +

//" if (biasDot < 0.25) { "+
" bias = 0.008 + max( biasMagnitude* (  pow(1.0 - biasDot,1.0) ), 0.0 );" +





"                             bias = 0.00;"+



//" }"+



" float cosTheta = abs(dot(normalize(-directionalLight.directionOrLocation), normalize(fNVec)));"+
" float scaleNormalBias = sqrt(1.0 - cosTheta*cosTheta);"+
" float slopeScaleBias = scaleNormalBias / cosTheta;"+

//" bias = min(0.005, 0.0008*slopeScaleBias);"+





//" bias = 0.00175;"+

" if (biasDot < 0.31) {"+
//"        bias = mix(0.018, 0.00144, biasDot/0.15);"+
" }"+
"float r = 80.0;"+
//" bias = 0.02;"+
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

" vec2 poissonDisk3[11] = vec2[]("+

"vec2(-0.326212f, -0.405805f),"+
"vec2(-0.840144f, -0.07358f),"+
"vec2(-0.695914f,  0.457137f),"+
"vec2(-0.203345f,  0.620716f),"+
"vec2(0.96234f,  -0.194983f),"+
"vec2(0.473434f, -0.480026f),"+
"vec2(0.519456f,  0.767022f),"+
"vec2(0.185461f, -0.893124f),"+
"vec2(0.507431f,  0.064425f),"+
"vec2(0.89642f,   0.412458f),"+
"vec2(-0.32194f,  -0.932615f)"+
");"+


//" float rotationAngle = texture( randomRotations, 10.0*(fPosWorld.xyz/fPosWorld.w) ).r;"+

"int sum = 0;"+
//" for (int k = 0; k < 256; k = k + 1) {"+
"  sum = sum + 1;"+
//" }"+


// For shadow 1, outer part
// Was 129.0
" float blurRadius1 = 127.0;"+
" float baseBlurRadiusAnimated = 0.0;"+
" float baseBlurRadiusRegular = 0.0;"+
" float baseBlurRadius = 0.0;"+

// For shadow 2, inner part
" float blurRadius2 = 136.0;"+



"float searchRadius = 1.0;"+
" searchRadius = 0.016;"+

//" float frequency1 = 1750;"+
" float frequency1 = 60;"+
"float dt1 = dot(floor(fPosWorld.xyz* frequency1), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle1 = 6.283285*fract(sin(dt1) * 2105.2354);"+

//" float frequency2 = 220;"+
" float frequency2 = 50;"+
" if (materialMode == 0) {"+
//"   frequency2 = 2;"+
 "}"+
"float dt2 = dot(floor(fPosWorld.xyz* frequency2), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle2 = 6.283285*fract(sin(dt2) * 2105.2354);"+

//" float frequency3 = 70;"+
" float frequency3 = 35;"+
"float dt3 = dot(floor(fPosWorld.xyz* frequency3), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle3 = 6.283285*fract(sin(dt3) * 2105.2354);"+

//" float frequency4 = 35;"+
" float frequency4 = 7;"+
"float dt4 = dot(floor(fPosWorld.xyz* frequency4), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle4 = 6.283285*fract(sin(dt4) * 2105.2354);"+

//" float frequency5 = 20;"+
" float frequency5 = 4;"+
"float dt5 = dot(floor(fPosWorld.xyz* frequency5), vec3(53.1215, 21.1352, 9.1322));"+
"float rotationAngle5 = 6.283285*fract(sin(dt5) * 2105.2354);"+



" float shadow1Amnt = 1.0;"+
" float shadow2Amnt = 0.0;"+

" float shadow1THold = 0.99;"+
" float shadow2THold = 0.05;"+
" float shadow1Comp = 0.3;"+
" float shadow2Comp = 1.4;"+

" float shadow1THold2 = 0.99;"+

//Search frequency
" float searchFrequency = 400.0;"+
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
"float a = 1.0;"+ 


//  shadow slope
         
//blurriest possible shadow blur radius
" float blurriestRadius = searchRadius;"+

" float k = 1.0;"+

" a = 0.024f;"+
" c = 0.0006f;"+ // USE 0.001

 "searchRadius = 0.02;"+
" blurriestRadius = 0.02;"+
 "closestObjectSearchSize = 8;"+

 "k = 0.041f;"+ // USE 0.03

 " if ( biasDot < 0.25 ) {"+
 //"      searchRadius = 0.0125;"+
 " }"+

"float baselineClosestObjectDistance = 0.0;"+
	
"int objectFound = 0;"+

" float biasScale = 0.38;"+


" float bias2 = 0.001;"+

"float distanceSofteningSampleCount = 8.0;"+
" float totalDistance = 0.0;"+
" float avgDistance = 0.0;"+


"float testBlurRadius = 1.0;"+


" float smllstTotalDistance = 0.0;"+
" float smllstAvgDistance = 0.0;"+
" float smllstDistanceSofteningSampleCount = closestObjectSearchSize;"+




" biasDistanceSofteningMagnitude = biasScale*bias;"+







" baseBlurRadiusAnimated =   1.0 * min(0.1*c + k*abs(avgDistance), blurriestRadius);" + 



" baselineClosestObjectDistance = 0.0;"+

" objectFound = 0;"+




" distanceSofteningSampleCount = 8.0;"+
" closestObjectSearchSize = 8;"+

" totalDistance = 0.0;"+
" avgDistance = 0.0;"+



" float normalBiasScale = 0.04;"+

"     normalBiasScale = 0.0;"+

" vec3 transformedNormal = normalize(normalTransformM*normalize(fNVec));"+

" vec3 normalBias = scaleNormalBias*normalBiasScale*transformedNormal;"+




// For biasing closest depth in PCSS
"float PCSSBias = 0.01;"+  


"float startSofteningDepth = 0.014f;"+






" if (positionMVP.z <= 6.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap5, 0);"+
				" vec3 lightSpaceCoords;"+
				" if (hasDisplacementMap == 1) {"+
				"     vec4 lightSpace = (lightMatrix5*vec4(modifiedWSPosition,1.0));"+
				"     lightSpaceCoords = lightSpace.xyz/lightSpace.w + normalBias;"+
				" } else {"+
		    	"     lightSpaceCoords = fPosLight5.xyz/fPosLight5.w  + normalBias ;"+
				" }"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+

   
	" float shadow1 = 0.0;"+
	" float shadow2 = 0.0;"+
	" float samplesTaken1 = 0.0;"+
	" float samplesTaken2 = 0.0;"+
	// Poisson Sample

	"float closestObjectDistance = 100.0;"+
   		
 	"for (int t=0;t< closestObjectSearchSize ;t++) {"+ 
 	
 	
   		" vec2 sampleC1 = vec2(0.0,0.0);"+
   		" if (testSearchRadius == 1) {"+
   		"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
   		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
   		" }"+
   	 " if (testSearchRadius == 0) {"+
		"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
		
   	
   	 " }"+
   		" float sampleDepth1 =  texture( shadowMap5, lightSpaceCoords.xy  + searchRadius*sampleC1).r;"+

   		" if (currentDepth - bias    > sampleDepth1  &&  abs(currentDepth - bias  - sampleDepth1) < closestObjectDistance) {"+
   		"    closestObjectDistance = abs(currentDepth - bias  - sampleDepth1);"+
   		" }"+
   
   		
   	
   	" if (sampleDepth1 == 1.0) {"+
   	//"          objectFound = 0;"+
   	"          distanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
   	"          smllstDistanceSofteningSampleCount = distanceSofteningSampleCount - 1.0;"+
   	
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
	
"     blurRadius1 = 1.0 * min(c + k*(abs(avgDistance) - startSofteningDepth), blurriestRadius);"+

" if (avgDistance <= startSofteningDepth) {"+
"    blurRadius1 = c;"+
" }"+

	
   	
   	
   	
   	
   	"for (int t=0;t<16;t++){"+
   	"   vec2 sampleC1;"+
        " sampleC1 = vec2(rSample1.x*(poissonDisk[t].x) - rSample1.y*(poissonDisk[t].y),  "+
   		" rSample1.y*(poissonDisk[t].x) + rSample1.x*(poissonDisk[t].y));"+
   		
   		" float sampleDepth1 =  texture( shadowMap5, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+

   		" samplesTaken1 = samplesTaken1 + 1.0;"+
   		" shadow1 = shadow1 + ( currentDepth - bias  > sampleDepth1 ? 1.0 : 0.0);"+    
	"}"+

	
	
	
	
	
	
	
	
	

	"    shadow1 = shadow1 / samplesTaken1;"+
" shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+
   " }\n"+

   



" else if (positionMVP.z <= 12.0) {\n"+ 
	
	"	vec2 texelSize = 1.0 / textureSize(shadowMap4, 0);"+
		    	" vec3 lightSpaceCoords;"+
		    	" if (hasDisplacementMap == 1) {"+
				"     vec4 lightSpace = (lightMatrix4*vec4(modifiedWSPosition,1.0));"+
				"     lightSpaceCoords = lightSpace.xyz/lightSpace.w + normalBias;"+
				" } else {"+
		    	"     lightSpaceCoords = fPosLight4.xyz/fPosLight4.w  + normalBias ;"+
				" }"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+



	"float closestObjectDistance = 100.0;"+
   	
 	
 	"for (int t=0;t< closestObjectSearchSize;t++){"+             
 	
   		" vec2 sampleC1 = vec2(0.0,0.0);"+
   		" if (testSearchRadius == 1) {"+
   		"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
   		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
   		" }"+
   	 " if (testSearchRadius == 0) {"+
		"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
		
   	
   	 " }"+

  	 //" if (testSearchRadius == 0) {"+
	//	"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
	 //" }"+
   	
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
	
	"}"+

	"}"+

	
  
	
	
	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.60 * min(c + k*(abs(avgDistance) - startSofteningDepth), blurriestRadius);"+

" if (avgDistance <= startSofteningDepth) {"+
"    blurRadius1 = 0.6*c;"+
" }"+

		" float shadow1 = 0.0;"+
		" float shadow2 = 0.0;"+

	" float samplesTaken1 = 0.0;"+

  
   
	" float samplesTaken2 = 0.0;"+

	// Poisson Sample
	
	
	
   		"for (int t=0;t<16;t++){"+ 

   		" vec2 sampleC1 = vec2(rSample2.x*(poissonDisk[t].x) - rSample2.y*(poissonDisk[t].y),  "+
   		"   rSample2.y*(poissonDisk[t].x) + rSample2.x*(poissonDisk[t].y));"+
   
		
  		" float sampleDepth1 =  texture( shadowMap4, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+

   		" samplesTaken1 = samplesTaken1 + 1.0;"+
   		" shadow1 = shadow1 + ( currentDepth - bias > sampleDepth1 ? 1.0 : 0.0);"+    

	"}"+
"    shadow1 = shadow1 / samplesTaken1;"+

" shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+

   " }\n"+





" else if (positionMVP.z <= 18.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap3, 0);"+
	" vec3 lightSpaceCoords;"+
	" if (hasDisplacementMap == 1) {"+
	"     vec4 lightSpace = (lightMatrix3*vec4(modifiedWSPosition,1.0));"+
	"     lightSpaceCoords = lightSpace.xyz/lightSpace.w + normalBias;"+
	" } else {"+
	"     lightSpaceCoords = fPosLight3.xyz/fPosLight3.w  + normalBias ;"+
	" }"+
		    	
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	
    	//" vec3 lightSpaceCoords2 = fPosLightAnimated.xyz/fPosLightAnimated.w;"+
    	//" lightSpaceCoords2 = 0.5*lightSpaceCoords2 + 0.5;"+

		    	
		    	" float currentDepth = lightSpaceCoords.z;"+

		    	

	"float closestObjectDistance = 100.0;"+
   		
 	"for (int t=0;t< closestObjectSearchSize ;t++){"+             
		" vec2 sampleC1 = vec2(0.0,0.0);"+
		" if (testSearchRadius == 1) {"+
		"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
		" }"+
	 " if (testSearchRadius == 0) {"+
	"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
	"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
	
	
	 " }"+
    // 	 " if (testSearchRadius == 0) {"+
   // 		"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
   // 	 " }"+
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
	
	"}"+


	"}"+
	
	

	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.40 * min(c + k*(abs(avgDistance) - startSofteningDepth), blurriestRadius);"+
" if (avgDistance <= startSofteningDepth) {"+
"    blurRadius1 = 0.4*c;"+
" }"+



			" float shadow1 = 0.0;"+
			" float shadow2 = 0.0;"+

		" float samplesTaken1 = 0.0;"+

	  
	   
		" float samplesTaken2 = 0.0;"+

		// Poisson Sample
		
	   		"for (int t=0;t<16;t++){"+ 

	   		" vec2 sampleC1 = vec2(rSample3.x*(poissonDisk[t].x) - rSample3.y*(poissonDisk[t].y),  "+
	   		"   rSample3.y*(poissonDisk[t].x) + rSample3.x*(poissonDisk[t].y));"+
	   
			
	   		" float sampleDepth1 =  texture( shadowMap3, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+


	   		" samplesTaken1 = samplesTaken1 + 1.0;"+
	 	" shadow1 = shadow1 + ( currentDepth - bias > sampleDepth1 ? 1.0 : 0.0);"+    
			
		"}"+
	"    shadow1 = shadow1 / samplesTaken1;"+
	

  " shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+

" }\n"+





" else if (positionMVP.z <= 28.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap2, 0);"+
	" vec3 lightSpaceCoords;"+
	" if (hasDisplacementMap == 1) {"+
	"     vec4 lightSpace = (lightMatrix2*vec4(modifiedWSPosition,1.0));"+
	"     lightSpaceCoords = lightSpace.xyz/lightSpace.w + normalBias;"+
	" } else {"+
	"     lightSpaceCoords = fPosLight2.xyz/fPosLight2.w  + normalBias ;"+
	" }"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	
    	//" vec3 lightSpaceCoords2 = fPosLightAnimated.xyz/fPosLightAnimated.w;"+
    	//" lightSpaceCoords2 = 0.5*lightSpaceCoords2 + 0.5;"+

		    	 
		    	" float currentDepth = lightSpaceCoords.z;"+
		    	
"float closestObjectDistance = 100.0;"+
	
 	"for (int t=0;t< closestObjectSearchSize ;t++){"+             
		" vec2 sampleC1 = vec2(0.0,0.0);"+
		" if (testSearchRadius == 1) {"+
		"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
		"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
		" }"+
	 " if (testSearchRadius == 0) {"+
	"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
	"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
	
	
	 " }"+
  
   //		" if (testSearchRadius == 0) {"+
  //  		"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
 //   	 " }"+
     		" float sampleDepth1 =  texture( shadowMap2, lightSpaceCoords.xy  + 0.32*searchRadius*sampleC1).r;"+

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
	
	"}"+


	"}"+
	
	
	
	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.320 * min(c + k*abs(avgDistance), blurriestRadius);"+


	
		   
			" float shadow1 = 0.0;"+
			" float shadow2 = 0.0;"+

		" float samplesTaken1 = 0.0;"+

	  
	   
		" float samplesTaken2 = 0.0;"+

		// Poisson Sample
		
	   		"for (int t=0;t<16;t++){"+ 

	   		" vec2 sampleC1 = vec2(rSample4.x*(poissonDisk[t].x) - rSample4.y*(poissonDisk[t].y),  "+
	   		"   rSample4.y*(poissonDisk[t].x) + rSample4.x*(poissonDisk[t].y));"+
	   
			

" float sampleDepth1 =  texture( shadowMap2, lightSpaceCoords.xy  + blurRadius1*sampleC1).r;"+
	
	   		" samplesTaken1 = samplesTaken1 + 1.0;"+
	   	" shadow1 = shadow1 + ( currentDepth - bias > sampleDepth1 ? 1.0 : 0.0);"+    
			
		"}"+
	"    shadow1 = shadow1 / samplesTaken1;"+


  " shadow = shadow1Amnt*shadow1 + shadow2Amnt*shadow2 ;"+

    " }\n"+



" else if (positionMVP.z <= 100.0) {\n"+

	"	vec2 texelSize = 1.0 / textureSize(shadowMap1, 0);"+
	" vec3 lightSpaceCoords;"+
	" if (hasDisplacementMap == 1) {"+
	"     vec4 lightSpace = (lightMatrix1*vec4(modifiedWSPosition,1.0));"+
	"     lightSpaceCoords = lightSpace.xyz/lightSpace.w + normalBias;"+
	" } else {"+
	"     lightSpaceCoords = fPosLight1.xyz/fPosLight1.w  + normalBias ;"+
	" }"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+


"float closestObjectDistance = 1000.0;"+

"for (int t=0;t< closestObjectSearchSize;t++){"+             
	" vec2 sampleC1 = vec2(0.0,0.0);"+
	" if (testSearchRadius == 1) {"+
	"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
	"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+
	" }"+
" if (testSearchRadius == 0) {"+
"  sampleC1 = vec2(rSampleSearch.x*(poissonDisk[t].x) - rSampleSearch.y*(poissonDisk[t].y),  "+
"   rSampleSearch.y*(poissonDisk[t].x) + rSampleSearch.x*(poissonDisk[t].y));"+


" }"+

//" if (testSearchRadius == 0) {"+
//		"sampleC1 = vec2( poissonDisk[t].x, poissonDisk[t].y);"+
//	 " }"+
" float sampleDepth1 =  texture( shadowMap1, lightSpaceCoords.xy  + 0.3*searchRadius*sampleC1).r;"+

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
	
	"}"+


"}"+

	
	" if (objectFound == 1) {"+
	"    avgDistance = totalDistance / distanceSofteningSampleCount;"+
	"}"+
	" else {"+
	"    avgDistance = baselineClosestObjectDistance;  "+
	"}"+
	
"     blurRadius1 = 0.30 * min(c + k*abs(avgDistance), blurriestRadius);"+


	
	" float samplesTaken = 0.0;"+
"for (int t=0;t<16;t++){"+

		" vec2 sampleC = vec2(rSample5.x*(poissonDisk[t].x) - rSample5.y*(poissonDisk[t].y),  "+
		"   rSample5.y*(poissonDisk[t].x) + rSample5.x*(poissonDisk[t].y));"+
		
	
		" float sampleDepth =  texture( shadowMap1, lightSpaceCoords.xy  + blurRadius1*sampleC).r;"+


		" samplesTaken = samplesTaken + 1.0;"+
		
		" shadow = shadow + ( currentDepth - bias > sampleDepth ? 1.0 : 0.0);"+    
	
		
	"}"+
"    shadow = shadow / samplesTaken;"+

  	" }\n"+
  	
  	
" baseBlurRadiusRegular =   1.0 * min(c + k*abs(avgDistance), blurriestRadius);" + 






"float l = clamp(smoothstep( 0.0, 0.2, dot(normalize(fNVec),normalize(directionalLight.directionOrLocation))), 0.0, 1.0);"+
//"float t = smoothstep(randomValues.x * 0.5, 1.0f, l);"+ 


//" shadow = max(shadow,shadowAnimated);"+
//" baseBlurRadius = max(baseBlurRadiusRegular,baseBlurRadiusAnimated);"+
"baseBlurRadius = baseBlurRadiusRegular;"+
" shadow = clamp(shadow, 0.0, 1.0);"+

" float diffuseLightAmount = max(dot(normalize(fNVec), normalize(-1.0*directionalLight.directionOrLocation)), 0.0);"+

" if (dot(normalize(fNVec), normalize(-1.0*directionalLight.directionOrLocation)) <= 0.0) {"+
//"  shadow = 1.0;"+
" }"+

//" shadow = max(shadow, 1.0 - diffuseLightAmount);"+

//" float diffuseAmount = 1.28*max((1.0 )*(1.28*directionalLight.amount)*dot(normalize(-1.0*directionalLight.directionOrLocation),normalize(fNVec)),0.0);"+
" float testDiffuseAmount = 1.00*max((1.0 - 0.99*shadow)*dot(normalize(-1.0*directionalLight.directionOrLocation),normalize(fNVec)),0.0);"+

//"float testDisplacementMap = texture(displacementMap, fTxt).r;"+

"vec4 materialColor = vec4(0.0,0.0,0.0,1.0);"+


	
	
	// Sampling the map using normal or POM uv
	
	" materialColor = texture(sampler1, tCoordActual).rgba;"+
	//" float displacementMapValue = texture(displacementMap, tCoordActual).r;"+
	//" materialColor = vec4(displacementMapValue,displacementMapValue,displacementMapValue,1.0);"+

	




//" vec4 materialColor = vec4(testDisplacementMap, testDisplacementMap, testDisplacementMap, 1.0);"+
//"  vec3 sceneColor = vec3((1.0*diffuseAmount*1.0*  0.88* directionalLight.color*(materialColor.rgb) + 0.06"
 //  + "  *directionalLight.color*materialColor.rgb + 0.00*directionalLight.color));"+

"  vec3 testSceneColor = vec3((0.89*testDiffuseAmount*1.0*  directionalLight.color*(materialColor.rgb) + 0.11"
+ "  *directionalLight.color*materialColor.rgb ));"+


   
   
   " float brightness = dot(testSceneColor,vec3(0.2126,0.7152,0.0722));"+

   
   " if (brightness > 0.04) {"+
   "       colorBright = vec4(1.0*pow((1.92*brightness + 0.05 ),1.0) * testSceneColor, 1.0);"+
   //"       colorBright = vec4(0.0,0.0,0.0,1.0);"+
   
   " }"+
   " else {"+
   
   "       colorBright = vec4(0.0,0.0,0.0,1.0);"+
   " }"+
   
  //" color = vec4(sceneColor,1.0);"+
  
"  vec3 baseNormal;"+

" if (hasNormalMap == 1) { "+
  


  " baseNormal = texture(normalMap, tCoordActual).rgb;"+


"  baseNormal = normalize(2.0*baseNormal - 1.0);"+


//" float diffuseLightAmount = max(dot(normalize(fNVec), normalize(-1.0*directionalLight.directionOrLocation)), 0.0);"+
"float diffuseLightAmountNormalMap = max(dot(normalize(uTBN*baseNormal), normalize(-1.0*directionalLight.directionOrLocation)), 0.0);"+

" if (dot(normalize(fNVec), normalize(-1.0*directionalLight.directionOrLocation)) <= 0.0) {"+
//"  shadow = 1.0;"+
" }"+

" float maxDiffuseShadow = max(1.0 - diffuseLightAmount,1.0 - diffuseLightAmountNormalMap);"+
//" shadow = max(shadow, 1.0 - clamp(diffuseLightAmount + diffuseLightAmountNormalMap,0.0,1.0));"+
//" shadow = max(shadow, 1.0 - diffuseLightAmountNormalMap);"+
//"shadow = max(shadow, maxDiffuseShadow);"+
" sceneShadowAmount = vec4(shadow,0.0,0.0,1.0);"+
//"sceneShadowAmount = vec4(0.0,0.0,0.0,1.0);"+


  "  normal = vec4( normalize( (0.0*normalize(fNVec.xyz) + 1.0*normalize(uTBN*baseNormal))), 1.0);"+

 //" normal = vec4(normalize(fActualNormal),1.0);"+
  //" 	normal = vec4(normalize(fNVec.xyz), 1.0);"+
//"        normal = vec4(0.0,1.0,0.0,1.0);"+
 // "albedoColor = vec4(normalize(1.0*texture(normalMap,fTxt).rgb ),1.0);"+
//" albedoColor = vec4(fTxt.y,0.0,0.0,1.0);"+
" }"+
" else {"+
    //"shadow = max(shadow, 1.0 - diffuseLightAmount);"+
" baseNormal = normalize(fNVec.xyz);"+
" sceneShadowAmount = vec4(shadow,0.0,0.0,1.0);"+
" 	normal = vec4(normalize(fNVec.xyz), 1.0);"+
" }"+





" if (materialMode == 1) {"+
"       albedoColor = vec4(materialColor.r, materialColor.g, materialColor.b, 1.0);"+
//" albedoColor = vec4(clamp((1.0/80.0)*vec3(abs(modifiedWSPosition.z), abs(modifiedWSPosition.z), abs(modifiedWSPosition.z)),vec3(0.0,0.0,0.0),vec3(1.0,1.0,1.0)),1.0);"+
//" albedoColor = vec4(2.0*normalize(modifiedWSPosition),1.0);"+
//"   albedoColor = vec4(fActualNormal,1.0);"+
"}"+
" else{"+
//"albedoColor = vec4(1.0,1.0,1.0,1.0);"+
//"albedoColor = vec4(normalize(baseNormal),1.0);"+
" albedoColor = vec4(materialColor.r, materialColor.g, materialColor.b, 1.0);"+
//" albedoColor = vec4(normalize(tang),1.0);"+
//"albedoColor = vec4(fTxt.x,fTxt.y,1.0,1.0);"+

"}"+

//" albedoColor = vec4(-vViewPos.x,-vViewPos.y,-vViewPos.z,1.0);"+
//" albedoColor = vec4(fNVec.x,fNVec.y,fNVec.z,1.0);"+
//" albedoColor = vec4(0.0,0.0,-0.02*vViewPos.z,1.0);"+



" float zNear = 0.001;"+
" float zFar = 160.0;"+
" float zN = 2.0*gl_FragCoord.z - 1.0;"+
" float zF = 2.0*zNear/(zFar + zNear - zN * (zFar - zNear));"+
//" position = vec4( zF, 0.0, 0.0, 1.0);"+
" position = vec4(-fPosCamera.z/1000.0,0.0,0.0,1.0);"+


//" position = vec4(fPosCamera.xyz,1.0);"+
// Determine if fragment is within a water body bounds

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

" if ( abs(distLToNGP - distRToNGP) <= 0.0000000001) {"+
"        v2 = vec2(x1,y1);"+
" }"+
" else if ( distLToNGP > distRToNGP) {"+
"    v2 = vec2(x2,y2);"+
"}"+
" else {"+
"    v2 = vec2(x1,y1);"+
" }"+

" vec2 v3 = vec2(x2,y1);"+
//         A               B
// 
//                       
//                      P
//            P
//        B                C

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

" if ( fPosWorld.y <= ((waterBody1.pos).y + waterHeight) + 0.004  ) {"+ 
" refractiveObject = vec4(0.5,0.5,0.5,1.0);"+
"}"+
" else {"+
" refractiveObject = vec4(0.0,0.0,0.0,1.0);"+
" }"+
"}"+
"else {"+
" refractiveObject = vec4(0.0, 0.0, 0.0, 1.0);"+
"}"+
//" shadowBlurRadius = vec4(baseBlurRadius, 0.0, 0.0, 1.0);"+

" if (hasDisplacementMap == 1) {"+
//"  albedoColor = vec4(-7.0*(vMatrix*( vec4(modifiedWSPosition,1.0) ) ).z/1000.0 ,0.0,0.0,1.0);"+
" position = vec4(-(vMatrix*vec4(modifiedWSPosition,1.0)).z/1000.0,0.0,0.0,1.0);"+
//" position = vec4(-fPosCamera.z/1000.0,0.0,0.0,1.0);"+
//" sceneShadowAmount = vec4(0.0,0.0,0.0,1.0);"+
//"normal = vec4(0.0,1.0,0.0,1.0);"+


" }"+
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
	        
	        

	        // Tessellation Shaders Setup
	        
	        
	        //     Tessellation Control Shader

	        tControlShaderID = glCreateShader(GL_TESS_CONTROL_SHADER);
	        if (tControlShaderID == 0) {
	        	throw new Exception("Error creating shader. Type: " + GL_TESS_CONTROL_SHADER);
	        }
	        glShaderSource(tControlShaderID, tControlShaderCode);
	        glCompileShader(tControlShaderID );
	        if (glGetShaderi(tControlShaderID, GL_COMPILE_STATUS) == 0) {
	        	throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(tControlShaderID, 1024));
	        }

	        glAttachShader(programId, tControlShaderID);

	        
	        //     Tessellation Evaluation Shader
	        
	        tEvaluationShaderID = glCreateShader(GL_TESS_EVALUATION_SHADER);
	        if (tEvaluationShaderID == 0) {
	        	throw new Exception("Error creating shader. Type: " + GL_TESS_EVALUATION_SHADER);
	        }
	        glShaderSource(tEvaluationShaderID, tEvaluationShaderCode);
	        glCompileShader(tEvaluationShaderID );
	        if (glGetShaderi(tEvaluationShaderID, GL_COMPILE_STATUS) == 0) {
	        	throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(tEvaluationShaderID, 1024));
	        }

	        glAttachShader(programId, tEvaluationShaderID);
	        
	
	        

	        
	        glLinkProgram(programId);
	        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
	        }

	        if (vShaderId != 0) {
	            glDetachShader(programId, vShaderId);
	        }
	  /*      if (tControlShaderID != 0) {
	            glDetachShader(programId, tControlShaderID);
	        }
	        if (tEvaluationShaderID != 0) {
	            glDetachShader(programId, tEvaluationShaderID);
	        }
*/	        if (fShaderId != 0) {
	            glDetachShader(programId, fShaderId);
	        }

	        glValidateProgram(programId);
	        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
	        }
	    
		
	        glUseProgram(programId);
	        
	        
	        // Uniforms
	        
	        
	        uMVPLocation = glGetUniformLocation(programId,"uMVP");
	        uPLocation = glGetUniformLocation(programId,"uP");
	        mMatrixLocation = glGetUniformLocation(programId,"mMatrix");
	        wMatrixLocation = glGetUniformLocation(programId,"wMatrix");
	        vMatrixLocation = glGetUniformLocation(programId,"vMatrix");
	        
	        vVecLocation = glGetUniformLocation(programId,"vVec");
	        
	        lightMatrix1Location = glGetUniformLocation(programId,"lightMatrix1");
	        lightMatrix2Location = glGetUniformLocation(programId,"lightMatrix2");
	        lightMatrix3Location = glGetUniformLocation(programId,"lightMatrix3");
	        lightMatrix4Location = glGetUniformLocation(programId,"lightMatrix4");
	        lightMatrix5Location = glGetUniformLocation(programId,"lightMatrix5");

	        
	        normalTransformMLocation = glGetUniformLocation(programId,"normalTransformM");

	        lightMatrixAnimated1Location = glGetUniformLocation(programId,"lightMatrixAnimated1");
	        lightMatrixAnimated2Location = glGetUniformLocation(programId,"lightMatrixAnimated2");
	        lightMatrixAnimated3Location = glGetUniformLocation(programId,"lightMatrixAnimated3");
	        
	        
	        
	        
	        
	        shadowMap1Location = glGetUniformLocation(programId,"shadowMap1");
	        shadowMap2Location = glGetUniformLocation(programId,"shadowMap2");
	        shadowMap3Location = glGetUniformLocation(programId,"shadowMap3");
	        shadowMap4Location = glGetUniformLocation(programId,"shadowMap4");
	        shadowMap5Location = glGetUniformLocation(programId,"shadowMap5");

	        shadowMapAnimated1Location = glGetUniformLocation(programId,"shadowMapAnimated1");
	      shadowMapAnimated2Location = glGetUniformLocation(programId,"shadowMapAnimated2");
	      shadowMapAnimated3Location = glGetUniformLocation(programId,"shadowMapAnimated3");

	        randomRotationsLocation = glGetUniformLocation(programId,"randomRotations");



	        sampler1Location = glGetUniformLocation(programId,"sampler1");       
	        normalMapLocation = glGetUniformLocation(programId,"normalMap");       
	        displacementMapLocation = glGetUniformLocation(programId,"displacementMap");       
	        
	        hasNormalMapLocation = glGetUniformLocation(programId,"hasNormalMap");
	        hasDisplacementMapLocation = glGetUniformLocation(programId,"hasDisplacementMap");
	        directionalLightTypeLocation = glGetUniformLocation(programId,"directionalLight.type");
	        directionalLightDirectionLocation = glGetUniformLocation(programId,"directionalLight.directionOrLocation");
	        directionalLightColorLocation = glGetUniformLocation(programId,"directionalLight.color");
	        directionalLightAmountLocation = glGetUniformLocation(programId,"directionalLight.amount");
	        
	        waterBody1PosLocation = glGetUniformLocation(programId,"waterBody1.pos");
	        waterBody1DimensionsLocation = glGetUniformLocation(programId,"waterBody1.dimensions");
	        waterBody1ScaleLocation = glGetUniformLocation(programId,"waterBody1.scale");
	        
	        setWaterBody1Location(glGetUniformLocation(programId,"waterBody1HeightField"));
	        
	        screenBrightnessLocation = glGetUniformLocation(programId,"screenBrightness");
	        materialModeLocation = glGetUniformLocation(programId,"materialMode");
	        testSearchRadiusLocation = glGetUniformLocation(programId,"testSearchRadius");
	        
	        cameraPositionLocation = glGetUniformLocation(programId,"cameraPosition");
	           
	        tessellationEnabledLocation = glGetUniformLocation(programId,"tessellationEnabled");
	        
	        vPMatrixLocation = glGetUniformLocation(programId,"vPMatrix");
	        eyePositionWorldSpaceLocation = glGetUniformLocation(programId,"eyePositionWorldSpace");
	        glUniform1i(sampler1Location,materials.get(0).getTxtUnit());
	      
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        




	        

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        // Depth Pass Shader Program
	        
	        

	        
	        
	        
	        String depthPassVShaderCode = 
	        		"#version 430 core\n" +
	        
	        		"layout (location=0) in vec3 vPos;" +
	        		
	        		"out vec3 fPosCamera;"+
	        		
	        		"uniform mat4 uMVP;" +
	        		"uniform mat4 uMV;"+
	        		
	        		"void main()" +
	        		"{" +
	        		"    fPosCamera = (uMV*vec4(vPos,1.0)).xyz;"+
	        		    "gl_Position = uMVP*vec4(vPos, 1.0);" +
	        		"}";
	        
	        
	       String depthPassFShaderCode =


	    		   "#version 430 core\n" +

			        " in vec3 fPosCamera;"+
			        
					"layout (location = 0) out vec4 p;\n"+
	
	    		   "void main()" +
	       		  "{" +             
	    		   
	       		  
					//"position= vec4(-fPosCamera.z/160.0,0.0,0.0,1.0);"+
					"p = vec4(-fPosCamera.z/160.0,0.0,0.0, 1.0);"+
					
					"}";


	    		
	         
	        

		
		
		
	        
	        depthPassProgramId = glCreateProgram();
	        glUseProgram(depthPassProgramId);
	        
	        if (depthPassProgramId == 0) {
	            throw new Exception("Could not create Shader");
	        }
	        
	        
	        depthPassVShaderId = glCreateShader(GL_VERTEX_SHADER);
	        if (depthPassVShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
	        }

	        glShaderSource(depthPassVShaderId, depthPassVShaderCode);
	        glCompileShader(depthPassVShaderId);

	        if (glGetShaderi(depthPassVShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(depthPassVShaderId, 1024));
	        }

	        glAttachShader(depthPassProgramId, depthPassVShaderId);

	        
	        
	        depthPassFShaderId = glCreateShader(GL_FRAGMENT_SHADER);
	        if (depthPassFShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
	        }

	        glShaderSource(depthPassFShaderId, depthPassFShaderCode);
	        glCompileShader(depthPassFShaderId);

	        if (glGetShaderi(depthPassFShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(depthPassFShaderId, 1024));
	        }

	        glAttachShader(depthPassProgramId, depthPassFShaderId);
	        
	        
	        
	        
	        glLinkProgram(depthPassProgramId);
	        if (glGetProgrami(depthPassProgramId, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(depthPassProgramId, 1024));
	        }

	        if (depthPassVShaderId != 0) {
	            glDetachShader(depthPassProgramId, depthPassVShaderId);
	        }
	        if (depthPassFShaderId != 0) {
	            glDetachShader(depthPassProgramId, depthPassFShaderId);
	        }

	        glValidateProgram(depthPassProgramId);
	        if (glGetProgrami(depthPassProgramId, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(depthPassProgramId, 1024));
	        }
	    
		
	        glUseProgram(depthPassProgramId);
	        
	        
	        System.out.println("DEPTH PASS PROGRAM ID : " + depthPassProgramId);
	        //shadowLightMatrixLocation = glGetUniformLocation(depthPassProgramId,"shadowLightMatrix");
	       // isStandardShadowStorageLocation = glGetUniformLocation(depthPassProgramId,"isStandardShadowStorage");

	        depthPassMVPMLocation = glGetUniformLocation(depthPassProgramId,"uMVP");
	        depthPassMVMLocation = glGetUniformLocation(depthPassProgramId,"uMV");

	        //depthPassNormalMLocation = glGetUniformLocation(depthPassProgramId,"uNormal");

	        

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        String shadowVShaderCode = 
	        		"#version 430 core\n" +
	        		"layout (location = 0) in vec3 aPos;" +
	        		
	        		"uniform mat4 shadowLightMatrix;" +

	        		"void main()" +
	        		"{" +
	        		    "gl_Position = shadowLightMatrix *  vec4(aPos, 1.0);" +
	        		"}";
	        
	        
	       String shadowFShaderCode =


	    		   "#version 430 core\n" +

	    		   
	    		   "out vec4 fragColor;" +
	    		  
	    		   "void main()" +
	       		  "{" +             
	    		   
	       		  "float c = 80.0;"+
	       		  
//	       		  " fragColor = vec4( exp(c*gl_FragCoord.z), 0.0, 0.0, 1.0);"+
					"		fragColor = vec4(gl_FragCoord.z + 0.0125,0.0,0.0,1.0);"+
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
	       // isStandardShadowStorageLocation = glGetUniformLocation(shadowProgramId,"isStandardShadowStorage");
			

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        // Shader programs for point light shadow maps
	        

	        
	        
	        
	        String shadowCubeVShaderCode = 
	        		"#version 430 core\n" +
	        		"layout (location = 0) in vec3 aPos;" +
	        		
	        		"out vec3 fPos;"+
	        		
	        		"uniform mat4 shadowLightMatrix;" +

	        		"void main()" +
	        		"{" +
	        		    "gl_Position = shadowLightMatrix *  vec4(aPos, 1.0);" +
	        		"}";
	        
	        
	       String shadowCubeFShaderCode =


	    		   "#version 430 core\n" +

	    		   "in vec3 fPos;"+
	    		   
	    		   "out vec4 fragColor;" +
	    		   
	    		   "uniform vec3 lightPosition;"+
	    		   "uniform float lightFarPlane;"+
	    		   
	    		   "void main()" +
	       		  "{" +             
	    		   
	       		  "		float lightDistance = length(fPos - lightPosition);"+
	       		  "		lightDistance = lightDistance / lightFarPlane;"+
				  "		fragColor = vec4(lightDistance + 0.0,0.0,0.0,1.0);"+
				  "}";


	        shadowCubeProgramId = glCreateProgram();
	        glUseProgram(shadowCubeProgramId);
	        
	        if (shadowCubeProgramId == 0) {
	            throw new Exception("Could not create Shader");
	        }
	        
	        shadowCubeVShaderId = glCreateShader(GL_VERTEX_SHADER);
	        if (shadowCubeVShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
	        }

	        glShaderSource(shadowCubeVShaderId, shadowCubeVShaderCode);
	        glCompileShader(shadowCubeVShaderId);

	        if (glGetShaderi(shadowCubeVShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shadowCubeVShaderId, 1024));
	        }

	        glAttachShader(shadowCubeProgramId, shadowCubeVShaderId);

	        
	        

	        shadowCubeFShaderId = glCreateShader(GL_FRAGMENT_SHADER);
	        if (shadowCubeFShaderId == 0) {
	            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
	        }

	        glShaderSource(shadowCubeFShaderId, shadowCubeFShaderCode);
	        glCompileShader(shadowCubeFShaderId);

	        if (glGetShaderi(shadowCubeFShaderId, GL_COMPILE_STATUS) == 0) {
	            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shadowCubeFShaderId, 1024));
	        }

	        glAttachShader(shadowCubeProgramId, shadowCubeFShaderId);
	        
	        
	        
	        
	        glLinkProgram(shadowCubeProgramId);
	        if (glGetProgrami(shadowCubeProgramId, GL_LINK_STATUS) == 0) {
	            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(shadowCubeProgramId, 1024));
	        }

	        if (shadowCubeVShaderId != 0) {
	            glDetachShader(shadowCubeProgramId, shadowCubeVShaderId);
	        }
	        if (shadowCubeFShaderId != 0) {
	            glDetachShader(shadowCubeProgramId, shadowCubeFShaderId);
	        }

	        glValidateProgram(shadowCubeProgramId);
	        if (glGetProgrami(shadowCubeProgramId, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(shadowCubeProgramId, 1024));
	        }
	    
		
	        glUseProgram(shadowCubeProgramId);
	        
	        
	        
	        shadowCubeLightMatrixLocation = glGetUniformLocation(shadowCubeProgramId,"shadowLightMatrix");
	        shadowCubeLightPositionLocation = glGetUniformLocation(shadowCubeProgramId,"lightPosition");
	        shadowCubeLightFarPlaneLocation = glGetUniformLocation(shadowCubeProgramId,"lightFarPlane");
	       

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
		 FloatBuffer verticesBuffer = null;
	//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
		    try {
	            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);

		vertexCount = vertices.length / 3;
	            (verticesBuffer.put(vertices)).flip();
	           vaoId = glGenVertexArrays();
	            glBindVertexArray(vaoId);

	            vboIdV = glGenBuffers();
	            glBindBuffer(GL_ARRAY_BUFFER, vboIdV);
	            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
	            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
	            
	            glBindVertexArray(0);
	            glBindBuffer(GL_ARRAY_BUFFER, 0);
  
	        } finally {
	            if (verticesBuffer  != null) {
	                MemoryUtil.memFree(verticesBuffer);
	            }
	        }
	        
		    
		    

	        eboID = glGenBuffers();
	        

	        indexBuffer = MemoryUtil.memAllocInt(indicies.length);
	         indexBuffer.put(indicies).flip();
		    
	         glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
	         glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);
		    
			 FloatBuffer normalsBuffer = null;
		//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
			    try {
		            normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			normalsCount = normals.length / 3;
		            (normalsBuffer.put(normals)).flip();
		          glBindVertexArray(vaoId);

		            vboIdN = glGenBuffers();
		            glBindBuffer(GL_ARRAY_BUFFER, vboIdN);
		            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);            
		            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		            
		            glBindVertexArray(0);
		            glBindBuffer(GL_ARRAY_BUFFER, 0);
   
		        } finally {
		            if (normalsBuffer  != null) {
		                MemoryUtil.memFree(normalsBuffer);
		            }
		        }

			    
			    
			    
			    
			    

					    
					    
			    
			    for (int k = 0; k < uvsMaterials.size(); k++) {
			    	

					 FloatBuffer tBuffer = null;
				//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
					    try {
				            tBuffer = MemoryUtil.memAllocFloat(uvsMaterials.get(k).length);
 
					tCount = uvsMaterials.get(k).length / 3;
				            (tBuffer.put(uvsMaterials.get(k))).flip();

				          glBindVertexArray(vaoId);

				            vboIdT1 = glGenBuffers();
				            glBindBuffer(GL_ARRAY_BUFFER, vboIdT1);
				            glBufferData(GL_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);            
				            glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
				            
				            glBindVertexArray(0);
				            glBindBuffer(GL_ARRAY_BUFFER, 0);

         
				        } finally {
				            if (tBuffer  != null) {
				                MemoryUtil.memFree(tBuffer);
				            }
				        }
				        
					    
					    
					    
					    
					    
					    
					    
			    }
			    
			    
			    
			    
			    

				 FloatBuffer tangentsBuffer = null;
			//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
				    try {
			            tangentsBuffer = MemoryUtil.memAllocFloat(tangents.length);

				tangentsCount = tangents.length / 3;
			            (tangentsBuffer.put(tangents)).flip();

			          glBindVertexArray(vaoId);

			            vboIdTans = glGenBuffers();
			            glBindBuffer(GL_ARRAY_BUFFER, vboIdTans);
			            glBufferData(GL_ARRAY_BUFFER, tangentsBuffer, GL_STATIC_DRAW);            
			            glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
			            
			            glBindVertexArray(0);
			            glBindBuffer(GL_ARRAY_BUFFER, 0);

			            //glBindVertexArray(0);         
			        } finally {
			            if (tangentsBuffer  != null) {
			         //   	System.exit(0);
			                MemoryUtil.memFree(tangentsBuffer);
			            }
			        }

				    
				    

					 FloatBuffer bitangentsBuffer = null;
				//  float verticesBuffer[] = {1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f};
					    try {
				            bitangentsBuffer = MemoryUtil.memAllocFloat(biTangents.length);
 
					bitangentsCount = biTangents.length / 3;
				            (bitangentsBuffer.put(biTangents)).flip();
				          glBindVertexArray(vaoId);

				            vboIdBiTans = glGenBuffers();
				            glBindBuffer(GL_ARRAY_BUFFER, vboIdBiTans);
				            glBufferData(GL_ARRAY_BUFFER, bitangentsBuffer, GL_STATIC_DRAW);            
				            glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);
				            
				            glBindVertexArray(0);
				            glBindBuffer(GL_ARRAY_BUFFER, 0);

				            //glBindVertexArray(0);         
				        } finally {
				            if (bitangentsBuffer  != null) {
				         //   	System.exit(0);
				                MemoryUtil.memFree(bitangentsBuffer);
				            }
				        }

			    
			    

			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
			    
				String drawShadowVShaderCode = "#version 430\n "+

		"layout (location=0) in vec3 vCoord;\n"+
		"layout (location=1) in vec3 nVec;"+
		"layout (location=2) in vec2 tCoord;"+

		"out vec3 fPosMVP;"+
		"out vec3 fNVec;"+
		"out vec2 fTxt;"+
		"out vec4 fPosLight1;"+
		"out vec4 fPosLight2;"+
		"out vec4 fPosLight3;"+

		"uniform mat4 projMatrix\n;"+
		"uniform mat4 translationMatrix;\n"+
		"uniform mat4 lightMatrix1;\n"+
		"uniform mat4 lightMatrix2;\n"+
		"uniform mat4 lightMatrix3;\n"+

		"uniform mat4 uMVP;"+






		"void main()\n"+
		"{\n"+
		" vec4 pos = uMVP*vec4(vCoord,1.0);"+
		" fPosMVP = pos.xyz;"+
		" fNVec = nVec;"+
		" fTxt = tCoord;"+
		" fPosLight1 = lightMatrix1*vec4(vCoord,1.0);"+
		" fPosLight2 = lightMatrix2*vec4(vCoord,1.0);"+
		" fPosLight3 = lightMatrix3*vec4(vCoord,1.0);"+

		"   gl_Position = pos;\n" +
		"}";
				
				
				
				
			    
			    
			    
			    
			    
			    
			    
			    
			    
				
				
				
				
				String drawShadowFShaderCode = "#version 430\n "+


		"in vec3 fPosMVP;"+
		"in vec4 fPosLight1;"+
		"in vec4 fPosLight2;"+
		"in vec4 fPosLight3;"+

		"in vec3 fNVec;"+
		"in vec2 fTxt;"+


		"out float fragColor;\n"+


		"struct LightSource {"
		+ "int type;"
		+ "vec3 directionOrLocation;"
		+ "vec3 color;"
		+ "float amount;"
		+ "};"+

		"uniform sampler2D sampler1;"+
		"uniform sampler2D shadowMap1;"+
		"uniform sampler2D shadowMap2;"+
		"uniform sampler2D shadowMap3;"+
		"uniform LightSource directionalLight;"+
		"uniform float screenBrightness;"+

		"void main()\n"+
		"{\n"+


		" float shadow = 0.0;"+
		//1.0*directionalLight.directionOrLocation
		" float bias = max( 0.05 * ( 1.0 - dot( 1.0*normalize(vec3(0.0,1.0,0.0)), normalize(fNVec))), 0.005f);" +
		
		" bias = 0.001;"+

		//" vec4 c;"+
" float p = 0.4;"+

		" if (fPosMVP.z <= 16.0) {\n"+
//		"     c = vec4(1.0,0.0,0.0,1.0);"+
		"	vec2 texelSize = 1.0 / textureSize(shadowMap3, 0);"+
	//	"	for(int x = 0; x <= 0; ++x)"+
	//	"	{"+
		   // "for(int y = 0; y <= 0; ++y)"+
		    //"{"+
		    	" vec3 lightSpaceCoords = fPosLight3.xyz/fPosLight3.w;"+
		    	" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
		    	" float currentDepth = lightSpaceCoords.z;"+
	"float pcfDepth = texture(shadowMap3, lightSpaceCoords.xy ).r;"+ 
	" p = pcfDepth;"+
    
	"shadow = currentDepth - bias > pcfDepth ? 1.0 : 0.0;"+        
	//	    "}    "+
	//	"	}"+
		" }\n"+
/*




		" else if (fPosMVP.z <= 28.0) {\n"+
		"	 c = vec4(0.0,1.0,0.0,1.0);"+

		"	vec2 texelSize = 1.0 / textureSize(shadowMap2, 0);"+
		"	for(int x = 0; x <= 0; ++x)"+
		"	{"+
		    "for(int y = 0; y <= 0; ++y)"+
		    "{"+
		    
				" vec3 lightSpaceCoords = fPosLight2.xyz/fPosLight2.w;"+
				" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
				" float currentDepth = lightSpaceCoords.z;"+

		        "float pcfDepth = texture(shadowMap2, lightSpaceCoords.xy ).r;"+ 
		   //     "c = vec4(currentDepth,0.0,0.0,1.0);"+
		        "shadow = currentDepth - bias > pcfDepth ? 1.0 : 0.0;"+        
		    "}    "+
		"	}"+
		"	"+
		" }\n"+


		" else if (fPosMVP.z <= 50.0) {\n"+

		"	 c = vec4(0.0,0.0,1.0,1.0);"+

		"	vec2 texelSize = 1.0 / textureSize(shadowMap1, 0);"+
		"	for(int x = 0; x <= 0; ++x)"+
		"	{"+
		    "for(int y = 0; y <= 0; ++y)"+
		    "{"+
		    
				" vec3 lightSpaceCoords = fPosLight1.xyz/fPosLight1.w;"+
				" lightSpaceCoords = 0.5*lightSpaceCoords + 0.5;"+
				" float currentDepth = lightSpaceCoords.z;"+

		        "float pcfDepth = texture(shadowMap1, lightSpaceCoords.xy ).r;"+ 
		 //       "c = vec4(currentDepth,0.0,0.0,1.0);"+
		        "shadow = currentDepth - bias > pcfDepth ? 1.0 : 0.0;"+        
		    "}    "+
		"	}"+
		"	"+
		" }\n"+
*/

		//" }"+
				
		" if (shadow == 1.0) {"+
		"     fragColor = 1.0;"+
		//" fragColor = 1.0;"+
		" }" +
		" else{ "+
		"   fragColor = 0.0;"+
	//	" fragColor = 0.0;"+
		" }"+
		  "}";
				
				
				
				
			    


				
				
				
		        
		        drawShadowProgramId = glCreateProgram();
		        glUseProgram(drawShadowProgramId);


		        
		        if (drawShadowProgramId == 0) {
		            throw new Exception("Could not create Shader");
		        }
		        
		        
		        drawShadowVShaderId = glCreateShader(GL_VERTEX_SHADER);
		        if (drawShadowVShaderId == 0) {
		            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
		        }

		        glShaderSource(drawShadowVShaderId, drawShadowVShaderCode);
		        glCompileShader(drawShadowVShaderId);

		        if (glGetShaderi(drawShadowVShaderId, GL_COMPILE_STATUS) == 0) {
		            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(drawShadowVShaderId, 1024));
		        }

		        glAttachShader(drawShadowProgramId, drawShadowVShaderId);

		        
		        
		        
		        

		        drawShadowFShaderId = glCreateShader(GL_FRAGMENT_SHADER);
		        if (drawShadowFShaderId == 0) {
		            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
		        }

		        glShaderSource(drawShadowFShaderId, drawShadowFShaderCode);
		        glCompileShader(drawShadowFShaderId);

		        if (glGetShaderi(drawShadowFShaderId, GL_COMPILE_STATUS) == 0) {
		            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(drawShadowFShaderId, 1024));
		        }

		        glAttachShader(drawShadowProgramId, drawShadowFShaderId);
		        
		        
		        
		        
		        
		        
		        
		        
		        glLinkProgram(drawShadowProgramId);
		        if (glGetProgrami(drawShadowProgramId, GL_LINK_STATUS) == 0) {
		            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(drawShadowProgramId, 1024));
		        }

		        if (drawShadowVShaderId != 0) {
		            glDetachShader(drawShadowProgramId, drawShadowVShaderId);
		        }
		        if (drawShadowFShaderId != 0) {
		            glDetachShader(drawShadowProgramId, drawShadowFShaderId);
		        }

		        glValidateProgram(drawShadowProgramId);
		        if (glGetProgrami(drawShadowProgramId, GL_VALIDATE_STATUS) == 0) {
		            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(drawShadowProgramId, 1024));
		        }
		    
			
		        glUseProgram(drawShadowProgramId);
		        
		        
		        
		        drawShadowUMVPLocation = glGetUniformLocation(drawShadowProgramId,"uMVP");
		        drawShadowLightMatrix1Location = glGetUniformLocation(drawShadowProgramId,"lightMatrix1");
		        drawShadowLightMatrix2Location = glGetUniformLocation(drawShadowProgramId,"lightMatrix2");
		        drawShadowLightMatrix3Location = glGetUniformLocation(drawShadowProgramId,"lightMatrix3");
		        
		        drawShadowShadowMap1Location = glGetUniformLocation(drawShadowProgramId,"shadowMap1");
		        drawShadowShadowMap2Location = glGetUniformLocation(drawShadowProgramId,"shadowMap2");
		        drawShadowShadowMap3Location = glGetUniformLocation(drawShadowProgramId,"shadowMap3");

	
		        
		        
		        
		        
		        
		    
		    
		    
		        
		        
		        
		        
		        
		        
		        // Wire Frame
		    
		    
		        

				String vShaderCodeWireFrame = "#version 430\n "+

		"layout (location=0) in vec3 vCoord;\n"+
		"layout (location=1) in vec3 nVec;"+
		"layout (location=2) in vec2 tCoord;"+


		"out vec4 fPosMVP;"+
		"out vec4 fPosWorld;"+



		// Modifications for tessellation

			"out vec3 worldPosTCIn;"+
			"out vec2 tCoordTCIn;"+
			"out vec3 normalTCIn;"+

		"uniform mat4 mMatrix\n;"+
		"uniform mat4 wMatrix\n;"+
		"uniform mat4 vMatrix;\n"+

		"uniform mat4 uMVP;"+

		"uniform mat4 vPMatrix;"+
		
		"uniform vec3 cameraPosition;"+

		"uniform int tessellationEnabled;"+
		
		
		"void main()\n"+

		"{\n"+


		"if (tessellationEnabled == 1) {"+

		"   worldPosTCIn = (wMatrix*mMatrix*vec4(vCoord,1.0)).xyz;"+
		"  normalTCIn = nVec;"+
		"  tCoordTCIn = tCoord;"+
		
		
		" } else {"+
		
		"   gl_Position = vPMatrix*wMatrix*mMatrix*vec4(vCoord,1.0);\n" +
		
		"}"+
	"}";

	        
				// Tessellation Control Shader
				
				String tControlShaderCodeWireFrame = "#version 430\n"+

			//  define the number of CPs in the output patch
			"layout (vertices = 3) out;"+

			"uniform vec3 gEyeWorldPos;"+

			//  attributes of the input CPs
			"in vec3 worldPosTCIn[];"+
			"in vec2 tCoordTCIn[];"+
			"in vec3 normalTCIn[];"+

			// uniforms
			
			"uniform vec3 eyePositionWorldSpace;"+
			"uniform mat4 vPMatrix;"+
				
			//	attributes of the output CPs
			
			"out vec3 worldPosTEIn[];"+
			"out vec2 tCoordTEIn[];"+
			"out vec3 normalTEIn[];"+		
			

	 	// Determine Tessellation Levels
		
		"	float GetTessLevelV1(float Distance0, float Distance1)"+
			"{"+
			    "float AvgDistance = (Distance0 + Distance1) / 2.0;"+
			
	

			    "if (AvgDistance <= 28.0) {"+
			    "    return 8.0;"+
			    "}"+
				    " else if (AvgDistance <= 36.0) {"+
				    "    return (mix(8.0,4.0, clamp((AvgDistance - 28.0)/8.0, 0.0, 1.0) ) );"+
				    "}"+
			    " else if (AvgDistance <= 80.0) {"+
			    "    return 4.0;"+
			    "}"+
				    " else if (AvgDistance <= 86.0) {"+
				    "    return (mix(4.0,2.0, clamp((AvgDistance - 80.0)/6.0, 0.0, 1.0) ) );"+
				    "}"+
				"else {"+
			        "return 2.0;"+
			    "}"+
			    

	//		      " return 1.0;"+                   
			  //  " return ( mix(20.0, 3.0, clamp(AvgDistance / 55.0, 0.0, 1.0)) );"+
			
			
			
			"}"+
	
			"	float GetTessLevelV2(vec3 posWorld1, vec3 posWorld2)"+
			"{"+
			                       
				" return 10.0;"+
			
			"}"+
			
			
			" float ComputeInnerTessLevel(vec3 pos) {"
			+ "  return ( mix(25.0,25.0,clamp( abs(pos.z)/12.0, 0.0, 1.0 ) ) );"
			+ ""
			+ "}"+
			
			
			
			
			"void main()"+
			"{"+
		    // Set the control points of the output patch
		    "	tCoordTEIn[gl_InvocationID] = tCoordTCIn[gl_InvocationID];"+
			"	normalTEIn[gl_InvocationID] = normalTCIn[gl_InvocationID];"+
		    "	worldPosTEIn[gl_InvocationID] = worldPosTCIn[gl_InvocationID];"+
		    

		    // Calculate the distance from the camera to the three control points
		    "	float EyeToVertexDistance0 = distance(eyePositionWorldSpace, worldPosTCIn[0]);"+
		    "	float EyeToVertexDistance1 = distance(eyePositionWorldSpace, worldPosTCIn[1]);"+
		    "	float EyeToVertexDistance2 = distance(eyePositionWorldSpace, worldPosTCIn[2]);"+
 
		    // Calculate the tessellation levels
		    

			"   float startingDistanceValue = 1.50;"+

		    "   float maxScalarLength = 16.0;"+
		    "   float dist12 = distance(worldPosTCIn[1], worldPosTCIn[2]);"+
		    "   float dist20 = distance(worldPosTCIn[2], worldPosTCIn[0]);"+
		    "   float dist01 = distance(worldPosTCIn[0], worldPosTCIn[1]);"+
		    "   float edge0Scalar;"+
		    "   float edge1Scalar;"+
		    "   float edge2Scalar;"+
		    "  if (dist12 <= startingDistanceValue) {"+
		    "     edge0Scalar = 1.0;"+
		    "  } else {"+
		    "     edge0Scalar = mix(1.0,8.0, clamp( (dist12 - startingDistanceValue)/ maxScalarLength, 0.0, 1.0) );"+
		    "  }"+
		    "  if (dist20 <= startingDistanceValue) {"+
		    "     edge1Scalar = 1.0;"+
		    "  } else {"+
		    "     edge1Scalar = mix(1.0,8.0, clamp( (dist20 - startingDistanceValue)/ maxScalarLength, 0.0, 1.0) );"+
		    "  }"+
		    "  if (dist01 <= startingDistanceValue) {"+
		    "     edge2Scalar = 1.0;"+
		    "  } else {"+
		    "     edge2Scalar = mix(1.0,8.0, clamp( (dist01 - startingDistanceValue)/ maxScalarLength, 0.0, 1.0) );"+
		    "  }"+
		    
		    // Area of polygon 
		    
		    //"  float area = abs(dot(worldPosTCIn[0], cross(worldPosTCIn[1] - worldPosTCIn[0], worldPosTCIn[2] - worldPosTCIn[0])));"+
		    
		    /*
		    "	gl_TessLevelOuter[0] = edge0Scalar*GetTessLevelV1(EyeToVertexDistance1, EyeToVertexDistance2);"+
		    "	gl_TessLevelOuter[1] = edge1Scalar*GetTessLevelV1(EyeToVertexDistance2, EyeToVertexDistance0);"+
		    "	gl_TessLevelOuter[2] = edge2Scalar*GetTessLevelV1(EyeToVertexDistance0, EyeToVertexDistance1);"+
		    */
		    
		    "  gl_TessLevelOuter[0] = GetTessLevelV2(worldPosTCIn[1], worldPosTCIn[2]);"+
		    "  gl_TessLevelOuter[1] = GetTessLevelV2(worldPosTCIn[0], worldPosTCIn[2]);"+
		    "  gl_TessLevelOuter[2] = GetTessLevelV2(worldPosTCIn[0], worldPosTCIn[1]);"+
		    
		    "	gl_TessLevelInner[0] = (gl_TessLevelOuter[2] + gl_TessLevelOuter[1] + gl_TessLevelOuter[0])/3.0;"+
		    //" gl_TessLevelInner[0] = mix(1.0,64.0,clamp(area/30.0, 0.0, 1.0));"+
			
		    "}";
				
				
				
				
				
				// Tessellation Evaluation Shader
				
				String tEvaluationShaderCodeWireFrame = "#version 430\n"+

				 //"layout(triangles, equal_spacing, ccw) in;"+
				 "layout(triangles, fractional_odd_spacing, ccw) in;"+

				//"uniform sampler2D gDisplacementMap;"+
				//	"uniform float gDispFactor;"+
				"uniform mat4 mMatrix\n;"+
				"uniform mat4 wMatrix\n;"+
				"uniform mat4 vMatrix;\n"+

				"uniform mat4 uMVP;"+

				"uniform mat4 vPMatrix;"+
				
				"uniform vec3 cameraPosition;"+
				
				"uniform sampler2D displacementMap;"+
					
				"	in vec3 worldPosTEIn[];"+
				"	in vec2 tCoordTEIn[];"+
				"	in vec3 normalTEIn[];"+
			"  out vec4 fPosMVP;"+
			"  out vec4 fPosWorld;"+
			"  out vec2 fTxt;"+
			"  out vec3 fNVec;"+
			//	"	out vec3 WorldPos_FS_in;"+
			//	"	out vec2 TexCoord_FS_in;"+
			//	"	out vec3 Normal_FS_in;"+
			

			
"			vec2 interpolate2D(vec2 v0, vec2 v1, vec2 v2)                                                   "+
"			{                                                                                               "+
"			    return vec2(gl_TessCoord.x) * v0 + vec2(gl_TessCoord.y) * v1 + vec2(gl_TessCoord.z) * v2;   "+
"			}                                                                                               "+
			                                                                                                
"			vec3 interpolate3D(vec3 v0, vec3 v1, vec3 v2)                                                   "+
"			{                                                                                              "+ 
"			    return vec3(gl_TessCoord.x) * v0 + vec3(gl_TessCoord.y) * v1 + vec3(gl_TessCoord.z) * v2;"+   
"			}"+                                                                                               
                                                                                                
			
			" void main() {\n"+
			"  fTxt = interpolate2D(tCoordTEIn[0], tCoordTEIn[1], tCoordTEIn[2]);"+
			"  fNVec = interpolate3D(normalTEIn[0], normalTEIn[1], normalTEIn[2]);"+
			//"  fNVec = normalize(fNVec);"+
			
			"  float displacementValue = texture(displacementMap,fTxt).r;"+
			"  fPosWorld = vec4(interpolate3D(worldPosTEIn[0], worldPosTEIn[1], worldPosTEIn[2]), 1.0) + 0.4*vec4(fNVec,0.0)*displacementValue;"+
			"  fPosMVP = vPMatrix*fPosWorld;"+
			"  gl_Position = fPosMVP;"+
			
			" }";
				
				
		    
		String fShaderCodeWireFrame = "#version 430\n "+
		"in vec4 fPosMVP;"+
		"in vec4 fPosWorld;"+
		"in vec2 fTxt;"+
		"in vec3 fNVec;"+
		
		"out vec4 color;"+
		
		"uniform sampler2D diffuseMap;"+
		"uniform sampler2D displacementMap;"+
		//"uniform sampler2D normalMap;"+
		
		"void main()\n"+
		"{"+
		
		
			" vec3 diffuseColor = texture(diffuseMap,fTxt).rgb;"+
		//"     color = vec4(texture(diffuseMap,fTxt).rgb,1.0);"+
			" color = vec4(0.0,0.0,1.0,1.0);"+
		"}";
		   
		    
		    
		    
		

        
        wireFrameProgramId = glCreateProgram();
        glUseProgram(wireFrameProgramId);
        
        if (wireFrameProgramId == 0) {
            throw new Exception("Could not create Shader");
        }
        
        
        vShaderIdWireFrame = glCreateShader(GL_VERTEX_SHADER);
        if (vShaderIdWireFrame == 0) {
            throw new Exception("Error creating shader. Type: " + GL_VERTEX_SHADER);
        }

        glShaderSource(vShaderIdWireFrame, vShaderCodeWireFrame);
        glCompileShader(vShaderIdWireFrame);

        if (glGetShaderi(vShaderIdWireFrame, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(vShaderIdWireFrame, 1024));
        }

        glAttachShader(wireFrameProgramId, vShaderIdWireFrame);

        
        
        
        

        fShaderIdWireFrame = glCreateShader(GL_FRAGMENT_SHADER);
        if (fShaderIdWireFrame == 0) {
            throw new Exception("Error creating shader. Type: " + GL_FRAGMENT_SHADER);
        }

        glShaderSource(fShaderIdWireFrame, fShaderCodeWireFrame);
        glCompileShader(fShaderIdWireFrame);

        if (glGetShaderi(fShaderIdWireFrame, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(fShaderIdWireFrame, 1024));
        }

        glAttachShader(wireFrameProgramId, fShaderIdWireFrame);
        
        

        // Tessellation Shaders Setup
        
        
        //     Tessellation Control Shader

        tControlShaderID = glCreateShader(GL_TESS_CONTROL_SHADER);
        if (tControlShaderID == 0) {
        	throw new Exception("Error creating shader. Type: " + GL_TESS_CONTROL_SHADER);
        }
        glShaderSource(tControlShaderID, tControlShaderCodeWireFrame);
        glCompileShader(tControlShaderID );
        if (glGetShaderi(tControlShaderID, GL_COMPILE_STATUS) == 0) {
        	throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(tControlShaderID, 1024));
        }

        glAttachShader(wireFrameProgramId, tControlShaderID);

        
        //     Tessellation Evaluation Shader
        
        tEvaluationShaderID = glCreateShader(GL_TESS_EVALUATION_SHADER);
        if (tEvaluationShaderID == 0) {
        	throw new Exception("Error creating shader. Type: " + GL_TESS_EVALUATION_SHADER);
        }
        glShaderSource(tEvaluationShaderID, tEvaluationShaderCodeWireFrame);
        glCompileShader(tEvaluationShaderID );
        if (glGetShaderi(tEvaluationShaderID, GL_COMPILE_STATUS) == 0) {
        	throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(tEvaluationShaderID, 1024));
        }

        glAttachShader(wireFrameProgramId, tEvaluationShaderID);
        


        
        
        glLinkProgram(wireFrameProgramId);
        if (glGetProgrami(wireFrameProgramId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(wireFrameProgramId, 1024));
        }

        if (vShaderIdWireFrame != 0) {
            glDetachShader(wireFrameProgramId, vShaderIdWireFrame);
        }
        if (tControlShaderID != 0) {
            glDetachShader(wireFrameProgramId, tControlShaderID);
        }
        if (tEvaluationShaderID != 0) {
            glDetachShader(wireFrameProgramId, tEvaluationShaderID);
        }
        if (fShaderIdWireFrame != 0) {
            glDetachShader(wireFrameProgramId, fShaderIdWireFrame);
        }

        glValidateProgram(wireFrameProgramId);
        if (glGetProgrami(wireFrameProgramId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(wireFrameProgramId, 1024));
        }
    
	
        glUseProgram(wireFrameProgramId);
        
        
        
        uMVPWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"uMVP");
        uWorldWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"wMatrix");
        uModelWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"mMatrix");
        uVPWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"vPMatrix");
        eyePositionWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"eyePositionWorldSpace");
        diffuseMapWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"diffuseMap");
        displacementMapWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"displacementMap");
        
        tessellationEnabledWireFrameLocation = glGetUniformLocation(wireFrameProgramId,"tessellationEnabled");
        
        
		    
	}
	
	public void renderShadowMap(Matrix4f lightTransform, int shadowFBO, int shadowMapUnit, Matrix4f lightProjection, Matrix4f lightV, float mRange, float sMapRes) {
		
		double sMapStartTime = System.nanoTime();
		
		
		
		
		

		
	
		Matrix4f scalingM = new Matrix4f();
		scalingM.scaling(this.size);
		Matrix4f rotation = new Matrix4f();
		rotation.rotationXYZ(this.rotationX,this.rotationY,this.rotationZ);
		Matrix4f shadowLightMatrix = new Matrix4f();
		Matrix4f translationM = new Matrix4f();
		translationM.translation(new Vector3f((float)(x),(float)(y),(float)(z)));
	//	translationM.translate(new Vector3f((float)(x),(float)(y),(float)(z)));
		
		shadowLightMatrix.set(rotation);
		shadowLightMatrix.mul(scalingM,shadowLightMatrix);
		translationM.mul(shadowLightMatrix,shadowLightMatrix);
		
		
		
		lightTransform.mul(shadowLightMatrix,shadowLightMatrix);
		
	
		   try (MemoryStack stack = MemoryStack.stackPush()) {
		        FloatBuffer tMVPM = stack.mallocFloat(16);
	    
	     	  shadowLightMatrix.get(tMVPM);
	     //tMVP.get(tMVPM);
		        
		        glUseProgram(shadowProgramId);
		        
		        glUniformMatrix4fv(shadowLightMatrixLocation, false, tMVPM);
		        double b4Time = System.nanoTime();
		        double aftrTime = System.nanoTime();
		        
		        
	       }
		   
		   
		   ////("For Stage Item, Program Id and Vao Id are " + programId + " and " + vaoId);

	       
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        
	        

	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
	        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);
	        
	   //     //("Indicies length is " + indicies.length);
	        

			//glActiveTexture(GL_TEXTURE0 + shadowMapUnit);
			//glBindTexture(GL_TEXTURE_2D,testSMap);
			
	        //glEnable(GL_CULL_FACE);
	        //glCullFace(GL_BACK);
	        
	        glDrawElements(GL_TRIANGLES,indicies.length,GL_UNSIGNED_INT,0);
	     //   //("MADEIT400");
	        //glCullFace(GL_BACK);
	       //glDisable(GL_CULL_FACE);
	        

	        
	        glDisableVertexAttribArray(0);
	        
	        glBindVertexArray(0);
	        glUseProgram(shadowProgramId);
	        glUseProgram(0);
		
	        double sMapEndTime = System.nanoTime();
			
	        //("Took " + (sMapEndTime - sMapStartTime) + " to render Shadow Map");
		
		
		
	}
	
	
	public void renderDepthCubeMap(float screenBrightness, int renderCube, Vector3f lightPosition, float lightRadius, Matrix4f shadowProjectionLeft, Matrix4f shadowProjectionRight, Matrix4f shadowProjectionFront, Matrix4f shadowProjectionBack, Matrix4f shadowProjectionTop, Matrix4f shadowProjectionBottom) {
		
		// Set up projection matrices
		
		/*
		Matrix4f scalingM = new Matrix4f();
		scalingM.scaling(this.size);
		Matrix4f rotationY = new Matrix4f();
		rotationY.rotationY(this.rotationY);
		Matrix4f translationM = new Matrix4f();
		translationM.translation(new Vector3f((float)(x),(float)(y),(float)(z)));
	//	translationM.translate(new Vector3f((float)(x),(float)(y),(float)(z)));

		Matrix4f shadowLightLeftMatrix = new Matrix4f();
		Matrix4f shadowLightRightMatrix = new Matrix4f();
		Matrix4f shadowLightFrontMatrix = new Matrix4f();
		Matrix4f shadowLightBackMatrix = new Matrix4f();
		Matrix4f shadowLightTopMatrix = new Matrix4f();
		Matrix4f shadowLightBottomMatrix = new Matrix4f();
		
		Matrix4f wVMatrix = new Matrix4f();
		wVMatrix.set(rotationY);
		wVMatrix.mul(scalingM,wVMatrix);
		translationM.mul(wVMatrix,wVMatrix);
		
		shadowProjectionLeft.mul(wVMatrix,shadowLightLeftMatrix);
		shadowProjectionRight.mul(wVMatrix,shadowLightRightMatrix);
		shadowProjectionFront.mul(wVMatrix,shadowLightFrontMatrix);
		shadowProjectionBack.mul(wVMatrix,shadowLightBackMatrix);
		shadowProjectionTop.mul(wVMatrix,shadowLightTopMatrix);
		shadowProjectionBottom.mul(wVMatrix,shadowLightBottomMatrix);
		
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
		        FloatBuffer tShadowLightLeft = stack.mallocFloat(16);
		        shadowLightLeftMatrix.get(tShadowLightLeft);
		        glUseProgram(shadowCubeProgramId);
		        glUniformMatrix4fv(shadowCubeLightMatrixLocation, false, tShadowLightLeft);
		}
		

	    glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
	        GL_TEXTURE_CUBE_MAP_POSITIVE_X,renderCube,0);
		
		
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer tShadowLightRight = stack.mallocFloat(16);
	        shadowLightRightMatrix.get(tShadowLightRight);
	        glUseProgram(shadowCubeProgramId);
	        glUniformMatrix4fv(shadowCubeLightMatrixLocation, false, tShadowLightRight);
	}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer tShadowLightFront = stack.mallocFloat(16);
	        shadowLightFrontMatrix.get(tShadowLightFront);
	        glUseProgram(shadowCubeProgramId);
	        glUniformMatrix4fv(shadowCubeLightMatrixLocation, false, tShadowLightFront);
	}
		
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer tShadowLightBack = stack.mallocFloat(16);
	        shadowLightBackMatrix.get(tShadowLightBack);
	        glUseProgram(shadowCubeProgramId);
	        glUniformMatrix4fv(shadowCubeLightMatrixLocation, false, tShadowLightBack);
	}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer tShadowLightTop = stack.mallocFloat(16);
	        shadowLightTopMatrix.get(tShadowLightTop);
	        glUseProgram(shadowCubeProgramId);
	        glUniformMatrix4fv(shadowCubeLightMatrixLocation, false, tShadowLightTop);
	}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer tShadowLightBottom = stack.mallocFloat(16);
	        shadowLightLeftMatrix.get(tShadowLightBottom);
	        glUseProgram(shadowCubeProgramId);
	        glUniformMatrix4fv(shadowCubeLightMatrixLocation, false, tShadowLightBottom);
	}

		*/
		
		
	       
        glUseProgram(shadowCubeProgramId);
        
        
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_POSITIVE_X,renderCube,0);
	    glClearColor(0.5f,0.5f,0.5f,1.0f);
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   

        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_NEGATIVE_X,renderCube,0);
	    glClearColor(0.5f,0.5f,0.5f,1.0f);
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	    
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_POSITIVE_Y,renderCube,0);
	    glClearColor(0.5f,0.5f,0.5f,1.0f);
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   

        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,renderCube,0);
	    glClearColor(0.5f,0.5f,0.5f,1.0f);
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	    
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_POSITIVE_Z,renderCube,0);
	    glClearColor(0.5f,0.5f,0.5f,1.0f);
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   

        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,renderCube,0);
	    glClearColor(0.5f,0.5f,0.5f,1.0f);
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


    //glBindVertexArray(vaoId);
    //glEnableVertexAttribArray(0);
    
  //  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
   // glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);
    
    
   // glDrawElements(GL_TRIANGLES,indicies.length,GL_UNSIGNED_INT,0);
    
   // glDisableVertexAttribArray(0);
    
   // glBindVertexArray(0);
   // glUseProgram(shadowProgramId);
    glUseProgram(0);

		
		
	}
	
	
	
	
	
	public void renderDepth(float screenBrightness, Camera camera) {
		
		
		//System.out.println("Rendering Depth");
    	//System.exit(0);
		
		//glUseProgram(0);
        
		glUseProgram(depthPassProgramId);
        
		Matrix4f scalingM = new Matrix4f();
		scalingM.scaling(this.size);
		Matrix4f rotationY = new Matrix4f();
		rotationY.rotationY(this.rotationY);
		Matrix4f worldM = new Matrix4f();
		worldM.translation(new Vector3f((float)x, (float)y, (float)z));
		Matrix4f projectionM = new Matrix4f();
		projectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
		Matrix4f cameraM = new Matrix4f();
		cameraM.translation(new Vector3f((float)(-camera.getEyeX() ),(float)(-camera.getEyeY()),(float)(-camera.getEyeZ() )));
		
		Matrix4f mV = new Matrix4f();
		rotationY.mul(scalingM,mV);
		worldM.mul(mV,mV);
		cameraM.mul(mV,mV);
		
		Matrix4f mVP = new Matrix4f();
		projectionM.mul(mV,mVP);
		
		
		glUseProgram(depthPassProgramId);
        

		   try (MemoryStack stack = MemoryStack.stackPush()) {
			   
		        FloatBuffer tMV = stack.mallocFloat(16);
		        FloatBuffer tMVP = stack.mallocFloat(16);

		        mV.get(tMV);
		        mVP.get(tMVP);
	        			
		        glUseProgram(depthPassProgramId);
		        
		        glUniformMatrix4fv(depthPassMVMLocation, false, tMV);
		        glUniformMatrix4fv(depthPassMVPMLocation, false, tMVP);
		        

		   }

		   
	    //    glEnable(GL_TEXTURE_2D);
	      
		   glEnable(GL_DEPTH_TEST);
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        //glEnableVertexAttribArray(1);
	        //glEnableVertexAttribArray(2);
	        glDisableVertexAttribArray(1);
	        glDisableVertexAttribArray(2);
	        
	        
	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
	        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);
	        
	        glDrawElements(GL_TRIANGLES,indicies.length,GL_UNSIGNED_INT,0);
	        
	        glDisableVertexAttribArray(0);
	        glDisableVertexAttribArray(1);
	        glDisableVertexAttribArray(2);
	        
	        glBindVertexArray(0);
	        glUseProgram(0);

		
	}
	public void renderShadow(Matrix4f lightMatrix1, Matrix4f lightMatrix2, Matrix4f lightMatrix3, int shadowMap1Unit, int shadowMap2Unit, int shadowMap3Unit, float screenBrightness, int testWater, boolean isShadowMap, int testUnit, boolean isShadowMapQuad) {
	
	double totalRenderStartTime = System.nanoTime();
		
		
		//this.renderShadowMap(lightMatrix, shadowMap1FBO, 1, screenBrightness, testUnit);
		
		double standardRenderStartTime = System.nanoTime();
		
		

		Matrix4f scalingM = new Matrix4f();
		scalingM.scaling(this.size);
		Matrix4f rotationY = new Matrix4f();
		rotationY.rotationY(this.rotationY);
		Matrix4f translationM = new Matrix4f();
		if (isShadowMapQuad) {
			translationM.translate(new Vector3f((float)( x ),(float)( y),(float)(z)));
				
		}
		else {
		translationM.translation(new Vector3f((float)(b.getX() + x ),(float)(b.getY() + y),(float)(z)));
		}
		
		
		
		Matrix4f wMatrix = new Matrix4f();
		rotationY.mul(scalingM,wMatrix);
		translationM.mul(wMatrix,wMatrix);
		Matrix4f projectionM = new Matrix4f();
		projectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(60.0));
		Matrix4f tMVP = new Matrix4f();
		projectionM.mul(wMatrix,tMVP);
		
		

		Matrix4f shadowLightMatrix1 = new Matrix4f();
		Matrix4f translationShadowLightM1 = new Matrix4f();
		translationShadowLightM1.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		
		shadowLightMatrix1.mul(rotationY,shadowLightMatrix1);
		shadowLightMatrix1.mul(scalingM,shadowLightMatrix1);
		translationShadowLightM1.mul(shadowLightMatrix1,shadowLightMatrix1);
		lightMatrix1.mul(shadowLightMatrix1,shadowLightMatrix1);
		
		
		
		
		
		
		Matrix4f shadowLightMatrix2 = new Matrix4f();
		
		
		Matrix4f translationShadowLightM2 = new Matrix4f();
		translationShadowLightM2.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		
		shadowLightMatrix2.mul(rotationY,shadowLightMatrix2);
		shadowLightMatrix2.mul(scalingM,shadowLightMatrix2);
		translationShadowLightM2.mul(shadowLightMatrix2,shadowLightMatrix2);
		
		lightMatrix2.mul(shadowLightMatrix2,shadowLightMatrix2);
		
		
		
		
		Matrix4f shadowLightMatrix3 = new Matrix4f();
		
		
		Matrix4f translationShadowLightM3 = new Matrix4f();
		translationShadowLightM3.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		
		shadowLightMatrix3.mul(rotationY,shadowLightMatrix3);
		shadowLightMatrix3.mul(scalingM,shadowLightMatrix3);
		translationShadowLightM3.mul(shadowLightMatrix3,shadowLightMatrix3);
		
		lightMatrix3.mul(shadowLightMatrix3,shadowLightMatrix3);
		
		
		
	
		   try (MemoryStack stack = MemoryStack.stackPush()) {
		        FloatBuffer tMVPM = stack.mallocFloat(16);
		        FloatBuffer tLightMatrix1 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrix2 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrix3 = stack.mallocFloat(16);
			     
		
	        	        tMVP.get(tMVPM);
	        	        shadowLightMatrix1.get(tLightMatrix1);
	        	        shadowLightMatrix2.get(tLightMatrix2);
	        	        shadowLightMatrix3.get(tLightMatrix3);
	    		        
	    
		        glUseProgram(drawShadowProgramId);
		        
		        glUniformMatrix4fv(drawShadowUMVPLocation, false, tMVPM);
		        glUniformMatrix4fv(drawShadowLightMatrix1Location, false, tLightMatrix1);
		       glUniformMatrix4fv(drawShadowLightMatrix2Location, false, tLightMatrix2);
		       glUniformMatrix4fv(drawShadowLightMatrix3Location, false, tLightMatrix3);
		         
		        double b4Time = System.nanoTime();
		        double aftrTime = System.nanoTime();
		        ////("TXTR SWAP TIME IS " +  (aftrTime - b4Time));
		        
    
		        
		        
		        glActiveTexture(GL_TEXTURE0 + 2);
		        glBindTexture(GL_TEXTURE_2D,shadowMap1Unit);
		        
		        glUniform1i(drawShadowShadowMap1Location,2);
		       
		        glActiveTexture(GL_TEXTURE0 + 3);
		        glBindTexture(GL_TEXTURE_2D,shadowMap2Unit);
		        
		        glUniform1i(drawShadowShadowMap2Location,3);
		       
		        glActiveTexture(GL_TEXTURE0 + 4);
		        glBindTexture(GL_TEXTURE_2D,shadowMap3Unit);
		        
		        glUniform1i(drawShadowShadowMap3Location,4);
		       
		  }
		   
		   
		   ////("For Stage Item, Program Id and Vao Id are " + programId + " and " + vaoId);

	       // glUseProgram(programId);
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        glEnableVertexAttribArray(1);
	        glEnableVertexAttribArray(2);
	        
	        
           //glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
////("Going there with vertex count of " + vertexCount);
////("Vao is " + vaoId + " and Vbo is " + vboId + " and programId is " + programId);
	       // glDrawArrays(GL_TRIANGLES,0,3);
	        
	        
	        
	        

	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
	        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);
	        
	   //     //("Indicies length is " + indicies.length);
	        
	        glDrawElements(GL_TRIANGLES,indicies.length,GL_UNSIGNED_INT,0);
	     //   //("MADEIT400");


	        
	        glDisableVertexAttribArray(0);
	        glDisableVertexAttribArray(1);
	        glDisableVertexAttribArray(2);
	        
	        glBindVertexArray(0);
	        glUseProgram(drawShadowProgramId);
	        glUseProgram(0);
		   
	        double standardRenderEndTime = System.nanoTime();
			   
	        //("Took " + (standardRenderEndTime - standardRenderStartTime) + " to do standard render");
	
	        double totalRenderEndTime = System.nanoTime();
	        
	        //("Total render time is " + (totalRenderEndTime - totalRenderStartTime));
			

		
		
		
	}
	
	
	public void render(Camera camera, Matrix4f lightMatrix1, Matrix4f lightMatrix2, Matrix4f lightMatrix3, Matrix4f lightMatrix4, Matrix4f lightMatrix5,  Matrix3f normalTransformMatrix, Matrix4f lightMatrixAnimated1, Matrix4f lightMatrixAnimated2, Matrix4f lightMatrixAnimated3, int shadows, int shadowMap1Unit, int shadowMap2Unit, int shadowMap3Unit, int shadowMap4Unit, int shadowMap5Unit, int shadowMapAnimated1Unit, int shadowMapAnimated2Unit, int shadowMapAnimated3Unit, int randomRotationsUnit, float screenBrightness, int testWater, boolean isShadowMap, int testUnit, boolean isShadowMapQuad, Vector3f sunDirection, int materialMode, int shouldTestSearchRadius, int tessellationEnabled, Matrix4f projectionMatrix) {
		
		//System.out.println("STAGE ITEM WITH X ="+this.x+", Y = "+this.y+", Z = "+this.z);
		double totalRenderStartTime = System.nanoTime();
		
		
		//this.renderShadowMap(lightMatrix, shadowMap1FBO, 1, screenBrightness, testUnit);
		
		double standardRenderStartTime = System.nanoTime();
		
		

		Matrix4f scalingM = new Matrix4f();
		scalingM.scaling(this.size);
		Matrix4f rotation = new Matrix4f();
		rotation.rotationXYZ(this.rotationX,this.rotationY,this.rotationZ);
		Matrix4f vM = new Matrix4f();
	
			
		
		//vM.translation(new Vector3f((float)(-camera.getEyeX() ),(float)(-camera.getEyeY()),(float)(-camera.getEyeZ() )));
		vM.lookAt((float)camera.getEyeX(),(float)camera.getEyeY(),(float)camera.getEyeZ(), (float)camera.getVPointX(), (float)camera.getVPointY(), (float)camera.getVPointZ(), 0.0f,1.0f,0.0f );
			
		
		
		Matrix4f mMatrix = new Matrix4f();
		//Matrix4f scalingMatrix = (new Matrix4f()).scaling(this.size);
		//mMatrix.scaling(this.size);
		rotation.mul(scalingM,mMatrix);
		Matrix4f wPosM = (new Matrix4f()).translation((float)x, (float)y, (float)z);
		Matrix4f wMatrix = new Matrix4f();
		rotation.mul(scalingM,wMatrix);
		wPosM.mul(wMatrix,wMatrix);
		//Matrix4f projectionM = new Matrix4f();
 		//projectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(160.0));
		Matrix4f tMVP = new Matrix4f();
		vM.mul(wMatrix,tMVP);
		projectionMatrix.mul(tMVP,tMVP);

		Matrix4f vPM = new Matrix4f();
 		projectionMatrix.mul(vM,vPM);
 		
 		
		Matrix4f wMatrix2 = new Matrix4f();
		rotation.mul(scalingM,wMatrix2);
		wPosM.mul(wMatrix2,wMatrix2);
	
		Matrix4f shadowLightMatrix1 = new Matrix4f();
		Matrix4f translationShadowLightM1 = new Matrix4f();
		translationShadowLightM1.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		shadowLightMatrix1.mul(rotation,shadowLightMatrix1);
		shadowLightMatrix1.mul(scalingM,shadowLightMatrix1);
		translationShadowLightM1.mul(shadowLightMatrix1,shadowLightMatrix1);
		lightMatrix1.mul(shadowLightMatrix1,shadowLightMatrix1);
		
		
		
		Matrix4f shadowLightMatrix2 = new Matrix4f();
		Matrix4f translationShadowLightM2 = new Matrix4f();
		translationShadowLightM2.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		shadowLightMatrix2.mul(rotation,shadowLightMatrix2);
		shadowLightMatrix2.mul(scalingM,shadowLightMatrix2);
		translationShadowLightM2.mul(shadowLightMatrix2,shadowLightMatrix2);
		lightMatrix2.mul(shadowLightMatrix2,shadowLightMatrix2);
		
		
		Matrix4f shadowLightMatrix3 = new Matrix4f();
		Matrix4f translationShadowLightM3 = new Matrix4f();
		translationShadowLightM3.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		shadowLightMatrix3.mul(rotation,shadowLightMatrix3);
		shadowLightMatrix3.mul(scalingM,shadowLightMatrix3);
		translationShadowLightM3.mul(shadowLightMatrix3,shadowLightMatrix3);
		lightMatrix3.mul(shadowLightMatrix3,shadowLightMatrix3);
		
		
		Matrix4f shadowLightMatrix4 = new Matrix4f();
		Matrix4f translationShadowLightM4 = new Matrix4f();
		translationShadowLightM4.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		shadowLightMatrix4.mul(rotation,shadowLightMatrix4);
		shadowLightMatrix4.mul(scalingM,shadowLightMatrix4);
		translationShadowLightM4.mul(shadowLightMatrix4,shadowLightMatrix4);
		lightMatrix4.mul(shadowLightMatrix4,shadowLightMatrix4);
		
	
		Matrix4f shadowLightMatrix5 = new Matrix4f();
		Matrix4f translationShadowLightM5 = new Matrix4f();
		translationShadowLightM5.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		shadowLightMatrix5.mul(rotation,shadowLightMatrix5);
		shadowLightMatrix5.mul(scalingM,shadowLightMatrix5);
		translationShadowLightM5.mul(shadowLightMatrix5,shadowLightMatrix5);
		lightMatrix5.mul(shadowLightMatrix5,shadowLightMatrix5);
		
		
		
		

		
		Matrix4f shadowLightMatrixAnimated1 = new Matrix4f();
		
		
		Matrix4f translationShadowLightMAnimated1 = new Matrix4f();
		translationShadowLightMAnimated1.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		
		shadowLightMatrixAnimated1.mul(rotation,shadowLightMatrixAnimated1);
		shadowLightMatrixAnimated1.mul(scalingM,shadowLightMatrixAnimated1);
		translationShadowLightMAnimated1.mul(shadowLightMatrixAnimated1,shadowLightMatrixAnimated1);
		
		lightMatrixAnimated1.mul(shadowLightMatrixAnimated1,shadowLightMatrixAnimated1);
		
		
		
		

		
		Matrix4f shadowLightMatrixAnimated2 = new Matrix4f();
		
		
		Matrix4f translationShadowLightMAnimated2 = new Matrix4f();
		translationShadowLightMAnimated2.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		
		shadowLightMatrixAnimated2.mul(rotation,shadowLightMatrixAnimated2);
		shadowLightMatrixAnimated2.mul(scalingM,shadowLightMatrixAnimated2);
		translationShadowLightMAnimated2.mul(shadowLightMatrixAnimated2,shadowLightMatrixAnimated2);
		
		lightMatrixAnimated2.mul(shadowLightMatrixAnimated2,shadowLightMatrixAnimated2);
		
		
		
Matrix4f shadowLightMatrixAnimated3 = new Matrix4f();
		
		
		Matrix4f translationShadowLightMAnimated3 = new Matrix4f();
		translationShadowLightMAnimated3.translation(new Vector3f((float)(x),(float)( y),(float)( z)));
		//translationShadowLightM.translate(new Vector3f((float)(x),(float)( y),(float)( z)));
		
		shadowLightMatrixAnimated3.mul(rotation,shadowLightMatrixAnimated3);
		shadowLightMatrixAnimated3.mul(scalingM,shadowLightMatrixAnimated3);
		translationShadowLightMAnimated3.mul(shadowLightMatrixAnimated3,shadowLightMatrixAnimated3);
		
		lightMatrixAnimated3.mul(shadowLightMatrixAnimated3,shadowLightMatrixAnimated3);
		
		
		
		Vector3f theVVec = new Vector3f( -1.0f*(float)b.getX(), -1.0f*(float)b.getY(), -1.0f*(float)b.getZ());
	
		
		Vector3f eyePosWSpace = new Vector3f((float)camera.getEyeX(),(float)camera.getEyeY(),(float)camera.getEyeZ());
 		
		
		
		if ( lightCRedChannel >= 1.0f || lightCRedChannel <= -1.0f ) {
			lightCRedChannelDelta = -1.0f*lightCRedChannelDelta;
		}
	
		lightCRedChannel = lightCRedChannel + lightCRedChannelDelta;
		
		Vector3f lightD = new Vector3f(-0.8f,-0.6f,1.4f);
	Vector3f lightC = new Vector3f(4.0f,3.54f,1.0f);
		//Vector3f lightC = new Vector3f(4.0f,4.0f,5.2f);
		
	
	// Water uniforms
	
		Vector3f waterBody1P = new Vector3f(-59.1f,-15.971f,0.01f);
		Vector2f waterBody1D = new Vector2f(800.0f,800.0f);
		Vector2f waterBody1S = new Vector2f(24.0f,0.808f);
		
		   try (MemoryStack stack = MemoryStack.stackPush()) {
		        FloatBuffer tMVPM = stack.mallocFloat(16);
		        FloatBuffer tPM = stack.mallocFloat(16);
		        FloatBuffer tMM = stack.mallocFloat(16);
		        FloatBuffer tWM = stack.mallocFloat(16);
		        FloatBuffer tVM = stack.mallocFloat(16);
		        
				   FloatBuffer tVP= stack.mallocFloat(16);
				   FloatBuffer tEyePosition = stack.mallocFloat(3);
				   
		        FloatBuffer tVVec = stack.mallocFloat(4);
		        
		        FloatBuffer tLightMatrix1 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrix2 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrix3 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrix4 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrix5 = stack.mallocFloat(16);
		       FloatBuffer tNormalTransformMatrix = stack.mallocFloat(9);
				       
		       FloatBuffer tLightMatrixAnimated1 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrixAnimated2 = stack.mallocFloat(16);
		       FloatBuffer tLightMatrixAnimated3 = stack.mallocFloat(16);
			     					     
		        FloatBuffer lightDV = stack.mallocFloat(3);
		FloatBuffer lightCV = stack.mallocFloat(3);
		
		FloatBuffer cameraPositionV = stack.mallocFloat(3);
		
        FloatBuffer waterBody1PV= stack.mallocFloat(3);
		FloatBuffer waterBody1DV= stack.mallocFloat(2);
        FloatBuffer waterBody1SV = stack.mallocFloat(2);
		
	        	        tMVP.get(tMVPM);
	        	        wPosM.get(tWM);
	        	        vM.get(tVM);
	        	        mMatrix.get(tMM);
	        	       
	        	        vPM.get(tVP);
	        			eyePosWSpace.get(tEyePosition);
	     		       
	        	        theVVec.get(tVVec);
	        	        
	        	        projectionMatrix.get(tPM);
	        	        
	        	        lightMatrix1.get(tLightMatrix1);
	        	        lightMatrix2.get(tLightMatrix2);
	        	        lightMatrix3.get(tLightMatrix3);
	        	        lightMatrix4.get(tLightMatrix4);
	        	        lightMatrix5.get(tLightMatrix5);
	        	        
	        	        normalTransformMatrix.get(tNormalTransformMatrix);
	        	        
	        	        shadowLightMatrixAnimated1.get(tLightMatrixAnimated1);
	        	        shadowLightMatrixAnimated2.get(tLightMatrixAnimated2);
	        	        shadowLightMatrixAnimated3.get(tLightMatrixAnimated3);
	    		        
	        	        sunDirection.get(lightDV);
	        	        lightC.get(lightCV);
	        	        
	        	        (new Vector3f((float)camera.getEyeX(),(float)camera.getEyeY(),(float)camera.getEyeZ())).get(cameraPositionV);
	        	        
	        	        waterBody1P.get(waterBody1PV);
	        	        waterBody1D.get(waterBody1DV);
	        	        waterBody1S.get(waterBody1SV);
	    
	        	        
		        glUseProgram(programId);
		        
		        glUniformMatrix4fv(uMVPLocation, false, tMVPM);
		        glUniformMatrix4fv(wMatrixLocation, false, tWM);
		        glUniformMatrix4fv(vMatrixLocation, false, tVM);
		        glUniformMatrix4fv(mMatrixLocation, false, tMM);
		        glUniformMatrix4fv(uPLocation, false, tPM);
		        
		        glUniformMatrix4fv(vPMatrixLocation, false, tVP);
			       glUniform3fv(eyePositionWorldSpaceLocation,tEyePosition);
			       
		        glUniformMatrix4fv(lightMatrix1Location, false, tLightMatrix1);
		       glUniformMatrix4fv(lightMatrix2Location, false, tLightMatrix2);
		       glUniformMatrix4fv(lightMatrix3Location, false, tLightMatrix3);
		       glUniformMatrix4fv(lightMatrix4Location, false, tLightMatrix4);
		       glUniformMatrix4fv(lightMatrix5Location, false, tLightMatrix5);
		       glUniformMatrix3fv(normalTransformMLocation, false, tNormalTransformMatrix);
		       
		       glUniformMatrix4fv(lightMatrixAnimated1Location, false, tLightMatrixAnimated1);
		       glUniformMatrix4fv(lightMatrixAnimated2Location, false, tLightMatrixAnimated2);
		       glUniformMatrix4fv(lightMatrixAnimated3Location, false, tLightMatrixAnimated3);
		       
		       glUniform3fv(vVecLocation, tVVec);
		       
		       glUniform1i(materialModeLocation, materialMode);
		       glUniform1i(testSearchRadiusLocation, shouldTestSearchRadius);
		       
		       if (normalMapped) {
		    	   glUniform1i(hasNormalMapLocation,1);
		       }
		       else {
		    	   glUniform1i(hasNormalMapLocation,0);
			          
		       }
		       
		       glUniform3fv(cameraPositionLocation, cameraPositionV);
		       
		       glUniform3fv(waterBody1PosLocation,  waterBody1PV);
		       glUniform2fv(waterBody1DimensionsLocation,  waterBody1DV);
		       glUniform2fv(waterBody1ScaleLocation,  waterBody1SV);
		       

	        	glActiveTexture(GL_TEXTURE0 + 14);
	        	glBindTexture(GL_TEXTURE_2D,this.waterHeightField1ID);
	        	glUniform1i(waterBody1Location,14);
	        	
		        double b4Time = System.nanoTime();
		        double aftrTime = System.nanoTime();
		        ////("TXTR SWAP TIME IS " +  (aftrTime - b4Time));
		        
		        glActiveTexture(GL_TEXTURE0 + 1);
		        
		        if (isShadowMap) {
		        	glBindTexture(GL_TEXTURE_2D,testUnit);
			        	
		        }
		        else {
		        glBindTexture(GL_TEXTURE_2D,this.materials.get(0).getTxtId());
		        }
		        glUniform1i(sampler1Location,1 );
		        
		       

		        if (this.normalMapped) { 
		        	glActiveTexture(GL_TEXTURE0 + 10);
		        	glBindTexture(GL_TEXTURE_2D,this.normalMap.getTxtId());
		        	glUniform1i(normalMapLocation,10);
		        }
		        

			       if (displacementMapped) {
			    	   glUniform1i(hasDisplacementMapLocation,1);
			       }
			       else {
			    	   glUniform1i(hasDisplacementMapLocation,0);
				          
			       }
		        
		        

			        if (displacementMapped) { 
			        	glActiveTexture(GL_TEXTURE0 + 15);
			        	glBindTexture(GL_TEXTURE_2D,this.displacementMap.getTxtId());
			        	glUniform1i(displacementMapLocation,15);
			        }
			        

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
		       
		        
		        //glEnable(GL_TEXTURE_3D);
		        /*
		        glActiveTexture(GL_TEXTURE0 + 8);
		        glBindTexture(GL_TEXTURE_3D,randomRotationsUnit);
		        glUniform1i(randomRotationsLocation,8);
		       */
		        glEnable(GL_TEXTURE_2D);
		        
		       
		        glActiveTexture(GL_TEXTURE0 + 8);
		        glBindTexture(GL_TEXTURE_2D,shadowMapAnimated1Unit);
		        glUniform1i(shadowMapAnimated1Location,8);
		       
		        
		      
		        glActiveTexture(GL_TEXTURE0 + 9);
		        glBindTexture(GL_TEXTURE_2D,shadowMapAnimated2Unit);
		        glUniform1i(shadowMapAnimated2Location,9);
		       
		       
		        glActiveTexture(GL_TEXTURE0 + 11);
		        glBindTexture(GL_TEXTURE_2D,shadowMapAnimated3Unit);
		        glUniform1i(shadowMapAnimated3Location,11);

		        //glActiveTexture(GL_TEXTURE0);
		        //glBindTexture(GL_TEXTURE_2D,testWater);
		        
		        glUniform1i(directionalLightTypeLocation, 1);
		        glUniform3fv(directionalLightDirectionLocation,  lightDV);
	            glUniform3fv(directionalLightColorLocation, lightCV);
	            glUniform1f(directionalLightAmountLocation,0.7f);
	            
	            glUniform1f(screenBrightnessLocation,screenBrightness);
	            glUniform1i(tessellationEnabledLocation,tessellationEnabled);
		   }
		   
		   
		   ////("For Stage Item, Program Id and Vao Id are " + programId + " and " + vaoId);

	       // glUseProgram(programId);
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        glEnableVertexAttribArray(1);
	        glEnableVertexAttribArray(2);
	        glEnableVertexAttribArray(3);
	        glEnableVertexAttribArray(4);
	        
	        
           //glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
////("Going there with vertex count of " + vertexCount);
////("Vao is " + vaoId + " and Vbo is " + vboId + " and programId is " + programId);
	       // glDrawArrays(GL_TRIANGLES,0,3);
	        
	        
	        
	        

	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
	        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);
	        
	   //     //("Indicies length is " + indicies.length);
	 //       System.out.println("Indicies length is " + indicies.length);
	      //glEnable(GL_CULL_FACE);
	        //glCullFace(GL_BACK);
	        
	        //glCullFace(GL_BACK);
		       glDisable(GL_CULL_FACE);
		        
	        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	     //  glDrawElements(GL_TRIANGLES,indicies.length,GL_UNSIGNED_INT,0);
		     glDrawElements(GL_PATCHES,indicies.length,GL_UNSIGNED_INT,0);

	        glCullFace(GL_BACK);
	       glDisable(GL_CULL_FACE);
	        
	        
	        glDisableVertexAttribArray(0);
	        glDisableVertexAttribArray(1);
	        glDisableVertexAttribArray(2);
	        glDisableVertexAttribArray(3);
	        glDisableVertexAttribArray(4);
	        
	        glBindVertexArray(0);
	        glUseProgram(programId);
	        glUseProgram(0);
		  
	        double standardRenderEndTime = System.nanoTime();
			   
	        //("Took " + (standardRenderEndTime - standardRenderStartTime) + " to do standard render");
	
	        double totalRenderEndTime = System.nanoTime();
	        
	        //("Total render time is " + (totalRenderEndTime - totalRenderStartTime));
			
	}
	
	public void renderWireFrame(Camera camera, Matrix3f normalTransformMatrix, float screenBrightness, int tessellationEnabled) {
			

		Matrix4f mMatrix = (new Matrix4f()).rotationXYZ((float)this.rotationX,(float)this.rotationY,(float)this.rotationZ);
		mMatrix.mul((new Matrix4f()).scaling(this.size),mMatrix);
		
		Matrix4f wMatrix = (new Matrix4f()).translation((float)this.x,(float)this.y,(float)this.z);
	
		Matrix4f vM = new Matrix4f();
		vM.lookAt((float)camera.getEyeX(),(float)camera.getEyeY(),(float)camera.getEyeZ(), (float)camera.getVPointX(), (float)camera.getVPointY(), (float)camera.getVPointZ(), 0.0f,1.0f,0.0f );
			
		Matrix4f projectionM = new Matrix4f();
 		projectionM.setPerspective((float)(Math.PI/3.0),(float)(1920.0/1080.0),(float)(0.001),(float)(360.0));
 		
 		Matrix4f mVPM = (new Matrix4f());
 		wMatrix.mul(mMatrix,mVPM);
 		vM.mul(mVPM,mVPM);
 		projectionM.mul(mVPM,mVPM);

 		Matrix4f vPM = new Matrix4f();
 		projectionM.mul(vM,vPM);
 		

 		Vector3f eyePosWSpace = new Vector3f((float)camera.getEyeX(),(float)camera.getEyeY(),(float)camera.getEyeZ());
 		
 		try (MemoryStack stack = MemoryStack.stackPush()) {
		        
			   FloatBuffer tMVPM = stack.mallocFloat(16);
			   FloatBuffer tWM = stack.mallocFloat(16);
			   FloatBuffer tMM = stack.mallocFloat(16);
			   FloatBuffer tVP= stack.mallocFloat(16);

			   FloatBuffer tEyePosition = stack.mallocFloat(3);
			   
		       mVPM.get(tMVPM);
		       mMatrix.get(tMM);
		       wMatrix.get(tWM);
		       vPM.get(tVP);
		
		       eyePosWSpace.get(tEyePosition);
		       
		       glUseProgram(wireFrameProgramId);
		        
		       glUniformMatrix4fv(uMVPWireFrameLocation, false, tMVPM);
		       glUniformMatrix4fv(uWorldWireFrameLocation, false, tWM);
		       glUniformMatrix4fv(uModelWireFrameLocation, false, tMM);
		       glUniformMatrix4fv(uVPWireFrameLocation, false, tVP);
		       glUniform3fv(eyePositionWireFrameLocation,tEyePosition);
		       glUniform1i(tessellationEnabledWireFrameLocation,tessellationEnabled);
		       
		       
		       glActiveTexture(GL_TEXTURE0 + 1);
		        glBindTexture(GL_TEXTURE_2D,this.materials.get(0).getTxtId());
		        glUniform1i(diffuseMapWireFrameLocation,1 );
		        
		        glActiveTexture(GL_TEXTURE0 + 15);
	        	glBindTexture(GL_TEXTURE_2D,this.displacementMap.getTxtId());
	        	glUniform1i(displacementMapWireFrameLocation,15);
		       
 		}
 		

	       glUseProgram(wireFrameProgramId);
	        glBindVertexArray(vaoId);
	        glEnableVertexAttribArray(0);
	        glEnableVertexAttribArray(1);
	        glEnableVertexAttribArray(2);
	        

	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
	        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);
	            
	        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

//	        glDrawElements(GL_TRIANGLES,indicies.length,GL_UNSIGNED_INT,0);
	        glDrawElements(GL_PATCHES,indicies.length,GL_UNSIGNED_INT,0);

	       
	        
	        glDisableVertexAttribArray(0);
	        glDisableVertexAttribArray(1);
	        glDisableVertexAttribArray(2);
	        
	        glBindVertexArray(0);
	        glUseProgram(wireFrameProgramId);
	        glUseProgram(0);
		   
 		
	}
	
	
	

	

	private float snap(float f, float multiple) {
	System.out.println("Before snapping, " + f);
	System.out.println("After snapping, " + Math.round(f/multiple)*multiple);
	
		return Math.round(f/multiple)*multiple;
	
	}

	public boolean isNormalMapped() {
		return normalMapped;
	}

	public void setNormalMapped(boolean normalMapped) {
		this.normalMapped = normalMapped;
	}

	public int getMaterialM() {
		return materialM;
	}

	public void setMaterialM(int materialM) {
		this.materialM = materialM;
	}

	public boolean isHasMotion() {
		return hasMotion;
	}

	public void setHasMotion(boolean hasMotion) {
		this.hasMotion = hasMotion;
	}

	public int getWaterBody1Location() {
		return waterBody1Location;
	}

	public void setWaterBody1Location(int waterBody1Location) {
		this.waterBody1Location = waterBody1Location;
	}

}
