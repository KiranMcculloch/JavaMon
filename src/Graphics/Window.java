package Graphics;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.image.BufferedImage;
import java.nio.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
    // The window handle
    private long window;

    public void run() {
        init();
        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1280, 720, "Pokéclone", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);

        GL.createCapabilities();
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_TEXTURE_2D);

        BufferedImage image = Graphics.TextureLoader.loadImage("src/Graphics/Sprites/Male/Normal/Back/637.png");
        int sprite = Graphics.TextureLoader.loadTexture(image);

        BufferedImage image2 = Graphics.TextureLoader.loadImage("src/Graphics/Sprites/Male/Normal/Front/646.png");
        int spriteOpp = Graphics.TextureLoader.loadTexture(image2);
        float i = -1.0f;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            if (i <= -0.2f){
                i += 0.02f;
            }

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();


            glBindTexture(GL_TEXTURE_2D, sprite);
            glBegin(GL_QUADS);

                    glTexCoord2f(1,0);
                        glVertex2f(i,0.2f);
                    glTexCoord2f(1,1);
                        glVertex2f(i,-0.5f);
                    glTexCoord2f(0,1);
                        glVertex2f(i-0.5f,-0.5f);
                    glTexCoord2f(0,0);
                        glVertex2f(i-0.5f,0.2f);
                glEnd();


            glBindTexture(GL_TEXTURE_2D, spriteOpp);
            glBegin(GL_QUADS);

                glTexCoord2f(1,0);
                    glVertex2f(0.5f,0.6f);
                glTexCoord2f(1,1);
                    glVertex2f(0.5f,-0.1f);
                glTexCoord2f(0,1);
                    glVertex2f(0,-0.1f);
                glTexCoord2f(0,0);
                    glVertex2f(0,0.6f);
            glEnd();




            glfwSwapBuffers(window); // swap the color buffers


        }
    }

}
