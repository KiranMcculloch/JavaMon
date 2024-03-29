package Graphics;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.stb.*;
import java.net.URL;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

public class TextureLoader {


    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
    public static int loadTexture(BufferedImage image){
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using
        // whatever OpenGL method you want, for example:

        int textureID = glGenTextures(); //Generate texture ID
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return textureID;
    }

    public static BufferedImage loadImage(String loc)
    {

        try{
            URL url = Paths.get(loc).toUri().toURL();
            try {
                return ImageIO.read(url);
            } catch (IOException e) {
                System.out.println("heyo");
            }
            return null;
        } catch(java.net.MalformedURLException e) {
            System.out.println("uh oh");
        }
        return null;
    }

    public static void displaySquare(int texture, float xPos, float yPos, float size){
        //-.607, .830
        glBindTexture(GL_TEXTURE_2D, texture);
        glBegin(GL_QUADS);
        glTexCoord2f(0,0);
            glVertex2f(xPos,yPos);
        glTexCoord2f(1,0);
            glVertex2f(xPos+size,yPos);
        glTexCoord2f(1,1);
            glVertex2f(xPos+size,yPos-(size*1.7f));
        glTexCoord2f(0,1);
            glVertex2f(xPos,yPos-(size*1.7f));
        glEnd();
    }

    public static void FullScreen(int image){
        glBindTexture(GL_TEXTURE_2D, image);
        glBegin(GL_QUADS);
        glTexCoord2f(0,1);
        glVertex2f(-1,-1);
        glTexCoord2f(0,0);
        glVertex2f(-1,1);
        glTexCoord2f(1,0);
        glVertex2f(1,1);
        glTexCoord2f(1,1);
        glVertex2f(1,-1);
        glEnd();
    }
}