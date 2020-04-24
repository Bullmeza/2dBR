import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.nio.ByteBuffer;


public class Texture {

    private int id;
    public int width;
    public int height;


    public Texture(String path){
        BufferedImage img;

        try{
            img = ImageIO.read(new File(path));

            width = img.getWidth();
            height = img.getHeight();

            int[] pixelsRaw = new int[width * height * 4];
            pixelsRaw = img.getRGB(0,0,width,height,null,0,width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);


            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    int pixel = pixelsRaw[x*width + y];
                    pixels.put((byte)((pixel >> 16) & 0xFF)); //RED
                    pixels.put((byte)((pixel >> 8) & 0xFF)); // GREEN
                    pixels.put((byte)(pixel & 0xFF)); //BLUE
                    pixels.put((byte)((pixel >> 25) & 0xFF)); //ALPHA

                }
            }
            pixels.flip();

            id=glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0 , GL_RGBA, width, height,0,GL_RGBA, GL_UNSIGNED_BYTE, pixels);


        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void bind(){
        glBindTexture(GL_TEXTURE_2D,id);
    }

}