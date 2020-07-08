
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

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageResize.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.*;
import java.nio.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import java.nio.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.BufferUtils.createByteBuffer;
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
import static org.lwjgl.opengl.GL21.*;







// package org.joml.lwjgl; 
 
 
 import org.joml.Matrix4f; 
 import org.joml.Matrix4d;
 import org.joml.Vector3f;
 import org.joml.Vector3d;
 import org.joml.Quaternionf; 

 import static org.lwjgl.stb.STBImage.*;
 import static org.lwjgl.stb.STBImageResize.*;


public class Wrap {

	private int txtId, txtUnit;
	
	
	public Wrap(String path, int txtUnit, int type, int loaderType) throws IOException {
		
		// Type: 1 = Diffuse/Albedo Map, 2 = Normal Map, 3 = Height
		// Loader Type: 1 = PNGDecoder, 2 = STB
		
		this.txtUnit = txtUnit;
		String imagePath = path;
		
		
		 

if (type == 1) {

	
  if (loaderType == 1) {

	  	InputStream in = new FileInputStream(path);
		try {
		   PNGDecoder decoder = new PNGDecoder(in);
		 
		   System.out.println("width="+decoder.getWidth());
		   System.out.println("height="+decoder.getHeight());
		 
		   ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
		   decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
		   buf.flip();
		   
		   txtId = glGenTextures();
		   glActiveTexture(GL_TEXTURE0 + this.txtUnit);
		   glBindTexture(GL_TEXTURE_2D,txtId);
		   glPixelStorei(GL_UNPACK_ALIGNMENT,1);



			glTexImage2D(GL_TEXTURE_2D, 0, GL_SRGB_ALPHA, decoder.getWidth(),
		    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
		
			glGenerateMipmap(GL_TEXTURE_2D);
			
			//glTexParameterf(GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f); 

		} finally {
			   in.close();
			}
			

    }
    
    else if (loaderType == 2) {

        ByteBuffer imageBuffer, image;
        try {
            imageBuffer = IOUtil.ioResourceToByteBuffer(imagePath, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // Use info to read image metadata without decoding the entire image.
            // We don't need this for this demo, just testing the API.
            if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            } else {
                System.out.println("OK with reason: " + stbi_failure_reason());
            }

            System.out.println("Image width: " + w.get(0));
            System.out.println("Image height: " + h.get(0));
            System.out.println("Image components: " + comp.get(0));
            System.out.println("Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

            // Decode the image
            image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }
            
            
            
     	   txtId = glGenTextures();
     	   glActiveTexture(GL_TEXTURE0 + this.txtUnit);
     	   glBindTexture(GL_TEXTURE_2D,txtId);
     	   glPixelStorei(GL_UNPACK_ALIGNMENT,1);


     	  if (image != null) {
     		  System.out.println("Width: " + w.get(0) + " Height: "+ h.get(0));
          	System.out.println("Image: "+ image);
          }
     		glTexImage2D(GL_TEXTURE_2D, 0, GL_SRGB_ALPHA, w.get(0),
     	    h.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
     		
     		
     		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
     		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
     		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
     		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
     	
     		glGenerateMipmap(GL_TEXTURE_2D);
     		
     		//glTexParameterf(GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f); 
        }

  }
}
else if (type == 2){

	InputStream in = new FileInputStream(path);
	try {
	   PNGDecoder decoder = new PNGDecoder(in);
	 
	   System.out.println("width="+decoder.getWidth());
	   System.out.println("height="+decoder.getHeight());
	 
	   ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
	   decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
	   buf.flip();
	   
	   txtId = glGenTextures();
	   glActiveTexture(GL_TEXTURE0 + this.txtUnit);
	   glBindTexture(GL_TEXTURE_2D,txtId);
	   glPixelStorei(GL_UNPACK_ALIGNMENT,1);

	
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(),
		    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

	glGenerateMipmap(GL_TEXTURE_2D);
	//glTexParameterf(GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f); 
	} finally {
		   in.close();
		}

}

else if (type == 3){

	
  if (loaderType == 1) {
	InputStream in = new FileInputStream(path);
	try {
	   PNGDecoder decoder = new PNGDecoder(in);
	 
	   System.out.println("width="+decoder.getWidth());
	   System.out.println("height="+decoder.getHeight());
	 
	   ByteBuffer buf = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight());
	   decoder.decode(buf, decoder.getWidth(), Format.ALPHA);
	   buf.flip();
	   
	   txtId = glGenTextures();
	   glActiveTexture(GL_TEXTURE0 + this.txtUnit);
	   glBindTexture(GL_TEXTURE_2D,txtId);
	   glPixelStorei(GL_UNPACK_ALIGNMENT,1);

	
	glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, decoder.getWidth(),
		    decoder.getHeight(), 0, GL_RED, GL_UNSIGNED_BYTE, buf);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

	//glGenerateMipmap(GL_TEXTURE_2D);
	//glTexParameterf(GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 8.0f); 
	} finally {
		   in.close();
		}

 }
  
  else if (loaderType == 2) {
	  
	    ByteBuffer imageBuffer, image;
	    try {
	        imageBuffer = IOUtil.ioResourceToByteBuffer(imagePath, 8 * 1024);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }

	    try (MemoryStack stack = stackPush()) {
	        IntBuffer w    = stack.mallocInt(1);
	        IntBuffer h    = stack.mallocInt(1);
	        IntBuffer comp = stack.mallocInt(1);

	        // Use info to read image metadata without decoding the entire image.
	        // We don't need this for this demo, just testing the API.
	        if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
	            throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
	        } else {
	            System.out.println("OK with reason: " + stbi_failure_reason());
	        }

	        System.out.println("Image width: " + w.get(0));
	        System.out.println("Image height: " + h.get(0));
	        System.out.println("Image components: " + comp.get(0));
	        System.out.println("Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

	        // Decode the image
	        image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
	        
	        if (image == null) {
	            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
	        }
	        
	 	   txtId = glGenTextures();
		   glActiveTexture(GL_TEXTURE0 + this.txtUnit);
		   glBindTexture(GL_TEXTURE_2D,txtId);
		   glPixelStorei(GL_UNPACK_ALIGNMENT,1);

		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_R16F, w.get(0),
			    h.get(0), 0, GL_RED, GL_UNSIGNED_BYTE, image);
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
		

		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameterf(GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 8.0f); 

	    }
	  
	  
  }
}



		
		     
	}
	
	public int getTxtId() {
		return txtId;
	}
	public int getTxtUnit() {
		return txtUnit;
	}
	public void setTxtId(int txtId) {
		this.txtId = txtId;
	}
	public void setTxtUnit(int txtUnit) {
		this.txtUnit = txtUnit;
	}
	
	
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
	
	
	
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
                while (fc.read(buffer) != -1) {
                    ;
                }
            }
        } else {
            try (
                InputStream source = IOUtil.class.getClassLoader().getResourceAsStream(resource);
                ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }
}
