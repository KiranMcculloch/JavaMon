package Graphics;

import PokemonBasics.Main;
import PokemonBasics.Pokemon;
import org.lwjgl.BufferUtils;
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
import java.util.Random;


public class Window {
    // The window handle
    private static long window;


    public static State state = State.INTRO;
    static DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
    static DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
    Random rg = new Random();
    int rando1 = 1,rando2=2,rando3=3 ,rando4=4,rando5=5,rando6=6, slotSelected = 1;
    static int title_screen,main_menu,pokemon_editor,team_builder,options,battle_select;
    static int party1Sprite, party2Sprite,party3Sprite,party4Sprite,party5Sprite,party6Sprite;
    static BufferedImage party1,party2,party3,party4,party5,party6;


    public enum State {
        INTRO, MAIN_MENU, BATTLE_SELECT, TEAM_BUILDER, EDITOR, OPTIONS, BATTLE, POKEMON_SELECTOR, PC;
    }

    //top left and bottom right coords
    public static boolean mouseWithin(double x1, double y1, double x2, double y2){
        glfwGetCursorPos(window, xBuffer, yBuffer);
        double mouseX = xBuffer.get(0);
        double mouseY = yBuffer.get(0);
        if (mouseX >= x1 && mouseX <= x2){
            if (mouseY >= y1 && mouseY <= y2){
                return true;
            }
        }
        return false;
    }


    public static void mouseNormal(){
        glfwGetCursorPos(window, xBuffer, yBuffer);
        double mouseX = xBuffer.get(0);
        double mouseY = yBuffer.get(0);
        double normalizedX = -1.0 + 2.0 * mouseX / 1280;
        double normalizedY = 1.0 - 2.0 * mouseY / 720;
        System.out.println("("+normalizedX+","+normalizedY+")");
    }



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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be resizable


        // Create the window
        window = glfwCreateWindow(1280, 720, "Pokéclone", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ){
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
            if (key == GLFW_KEY_ENTER && action == GLFW_RELEASE){
                switch(state){
                    case INTRO:
                        state = State.MAIN_MENU;
                        break;
                    case TEAM_BUILDER:
                        for (int i=0; i<6; i++){
                            Main.party[i] = null;
                            rando1 = rg.nextInt(802) +1;
                            Main.addToParty(new Pokemon(rando1, 30, rando3, rando2, rando6, rando1, null, false));
                        }
                        System.out.println(Main.party[5].getID());
                        break;
                    case MAIN_MENU:
                        state = State.TEAM_BUILDER;
                        break;
                }
            }
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                mouseNormal();
                switch(state){
                    case TEAM_BUILDER:
                        if (mouseWithin(230,50,468,280)){
                            System.out.println("1");
                            slotSelected = 1;
                        }else if (mouseWithin(510,50,748,280)){
                            System.out.println("2");
                            slotSelected = 2;
                        }else if (mouseWithin(790,50,1028,280)){
                            System.out.println("3");
                            slotSelected = 3;
                        }else if (mouseWithin(230,379,468,609)){
                            System.out.println("4");
                            slotSelected = 4;
                        }else if (mouseWithin(510,379,748,609)){
                            System.out.println("5");
                            slotSelected = 5;
                        } else if (mouseWithin(790,379,1028,609)){
                            System.out.println("6");
                            slotSelected = 6;
                        }
                }
            }
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


    public static void loadPartySprites(){
        for (int i=0; i<6; i++){
            if (Main.party[i] == null){
                switch(i){
                    case 0:
                        party1 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
                        party1Sprite = TextureLoader.loadTexture(party1);
                    case 1:
                        party2 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
                        party2Sprite = TextureLoader.loadTexture(party2);
                    case 2:
                        party3 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
                        party3Sprite = TextureLoader.loadTexture(party3);
                    case 3:
                        party4 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
                        party4Sprite = TextureLoader.loadTexture(party4);
                    case 4:
                        party5 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
                        party5Sprite = TextureLoader.loadTexture(party5);
                    case 5:
                        party6 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
                        party6Sprite = TextureLoader.loadTexture(party6);
                }
            } else {
                switch(i){
                    case 0:
                        party1 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Front/"+Main.party[0].getID()+".png");
                        party1Sprite = party1Sprite = TextureLoader.loadTexture(party1);
                    case 1:
                        party2 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Front/"+Main.party[1].getID()+".png");
                        party2Sprite = party2Sprite = TextureLoader.loadTexture(party2);
                    case 2:
                        party3 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Front/"+Main.party[2].getID()+".png");
                        party3Sprite = party3Sprite = TextureLoader.loadTexture(party3);
                    case 3:
                        party4 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Front/"+Main.party[3].getID()+".png");
                        party4Sprite = party4Sprite = TextureLoader.loadTexture(party4);
                    case 4:
                        party5 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Front/"+Main.party[4].getID()+".png");
                        party5Sprite = party5Sprite = TextureLoader.loadTexture(party5);
                    case 5:
                        party6 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Front/"+Main.party[5].getID()+".png");
                        party6Sprite = party6Sprite = TextureLoader.loadTexture(party6);

                }
            }

        }

    }






    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        float pokemonAnim = -1.0f;
        int j  = 1;

        BufferedImage mm = TextureLoader.loadImage("src/Graphics/mainmenu.png");
        main_menu = TextureLoader.loadTexture(mm);
        BufferedImage op = TextureLoader.loadImage("src/Graphics/options.png");
        options = TextureLoader.loadTexture(op);
        BufferedImage ed = TextureLoader.loadImage("src/Graphics/pokemoneditor.png");
        pokemon_editor = TextureLoader.loadTexture(ed);
        BufferedImage se = TextureLoader.loadImage("src/Graphics/teambuilder.png");
        team_builder = TextureLoader.loadTexture(se);
        BufferedImage ti = TextureLoader.loadImage("src/Graphics/title.png");
        title_screen = TextureLoader.loadTexture(ti);
        BufferedImage tr = TextureLoader.loadImage("src/Graphics/tree.png");
        battle_select = TextureLoader.loadTexture(tr);
        BufferedImage party1 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
        party1Sprite = TextureLoader.loadTexture(party1);
        BufferedImage party2 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
        party2Sprite = TextureLoader.loadTexture(party2);
        BufferedImage party3 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
        party3Sprite = TextureLoader.loadTexture(party3);
        BufferedImage party4 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
        party4Sprite = TextureLoader.loadTexture(party4);
        BufferedImage party5 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
        party5Sprite = TextureLoader.loadTexture(party5);
        BufferedImage party6 = TextureLoader.loadImage("src/Graphics/Pokemon/none.png");
        party6Sprite = TextureLoader.loadTexture(party6);


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwPollEvents();

            switch (state) {
                case INTRO:
                    TextureLoader.FullScreen(title_screen);
                    break;



                case MAIN_MENU:
                    TextureLoader.FullScreen(main_menu);
                    break;



                case OPTIONS:
                    TextureLoader.FullScreen(options);
                    break;



                case TEAM_BUILDER:

                    loadPartySprites();

                    TextureLoader.FullScreen(team_builder);

                    TextureLoader.displaySquare(party1Sprite,-0.613f,0.870f,0.350f);
                    TextureLoader.displaySquare(party2Sprite,-0.177f,0.870f,0.350f);
                    TextureLoader.displaySquare(party3Sprite,0.261f,0.870f,0.350f);
                    TextureLoader.displaySquare(party4Sprite,-0.613f,-0.0438f,0.350f);
                    TextureLoader.displaySquare(party5Sprite,-0.177f,-0.0438f,0.350f);
                    TextureLoader.displaySquare(party6Sprite,0.261f,-0.0438f,0.350f);

                    break;

                case POKEMON_SELECTOR:
                    switch (slotSelected){
                        case 1:
                            party1 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Front/"+rando1+".png");
                            party1Sprite = party1Sprite = TextureLoader.loadTexture(party1);
                        case 2:
                            party2 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Front/"+rando2+".png");
                            party2Sprite = party2Sprite = TextureLoader.loadTexture(party2);
                        case 3:
                            party3 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Front/"+rando3+".png");
                            party3Sprite = party3Sprite = TextureLoader.loadTexture(party3);
                        case 4:
                            party4 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Front/"+rando4+".png");
                            party4Sprite = party4Sprite = TextureLoader.loadTexture(party4);
                        case 5:
                            party5 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Front/"+rando5+".png");
                            party5Sprite = party5Sprite = TextureLoader.loadTexture(party5);
                        case 6:
                            party6 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Front/"+rando6+".png");
                            party6Sprite = party6Sprite = TextureLoader.loadTexture(party6);
                    }


                case EDITOR:
                    TextureLoader.FullScreen(pokemon_editor);
                    break;



                case BATTLE_SELECT:
                    TextureLoader.FullScreen(battle_select);
                    break;


                case BATTLE:
                    if (j == 802){
                        j=1;
                    } else {
                        j++;
                    }
                    glColor4f(1, 1, 1, 0);
                    BufferedImage bg = TextureLoader.loadImage("src/Graphics/Backgrounds/Grass1.png");
                    int background = TextureLoader.loadTexture(bg);

                    BufferedImage image = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Back/150.png");
                    int sprite = TextureLoader.loadTexture(image);

                    BufferedImage image2 = TextureLoader.loadImage("src/Graphics/Pokemon/Male/Normal/Front/646.png");
                    int spriteOpp = TextureLoader.loadTexture(image2);

                    if (pokemonAnim <= -0.2f){
                        pokemonAnim += 0.02f;
                    }

                    glBindTexture(GL_TEXTURE_2D, background);
                    glBegin(GL_QUADS);
                    glTexCoord2f(1,1);
                        glVertex2f(-1,-1);
                    glTexCoord2f(1,0);
                        glVertex2f(-1,1);
                    glTexCoord2f(0,0);
                        glVertex2f(1,1);
                    glTexCoord2f(0,1);
                        glVertex2f(1,-1);
                    glEnd();

                            glBindTexture(GL_TEXTURE_2D, sprite);
                            glBegin(GL_QUADS);
                            glTexCoord2f(1,0);
                            glVertex2f(pokemonAnim,0.2f);
                            glTexCoord2f(1,1);
                            glVertex2f(pokemonAnim,-0.5f);
                            glTexCoord2f(0,1);
                            glVertex2f(pokemonAnim-0.5f,-0.5f);
                            glTexCoord2f(0,0);
                            glVertex2f(pokemonAnim-0.5f,0.2f);
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
                    break;



            }
            glfwSwapBuffers(window); // swap the color buffers

        }
    }

}
