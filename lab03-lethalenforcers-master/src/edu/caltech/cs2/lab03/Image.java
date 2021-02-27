package edu.caltech.cs2.lab03;

import edu.caltech.cs2.libraries.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private Pixel[][] pixels;

    public Image(File imageFile) throws IOException {
        BufferedImage img = ImageIO.read(imageFile);
        this.pixels = new Pixel[img.getWidth()][img.getHeight()];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                this.pixels[i][j] = Pixel.fromInt(img.getRGB(i, j));
            }
        }
    }

    private Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Image transpose() {
        Pixel[][] newPixels = new Pixel[pixels[0].length][pixels.length];
        for (int i = 0; i < newPixels.length; i++) {
            for (int j = 0; j < newPixels[0].length; j++) {
                newPixels[i][j] = this.pixels[j][i];
            }
        }
        return new Image(newPixels);
    }

    public String decodeText() {
        String temp = "";
        String ans = "";
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                temp = pixels[i][j].getLowestBitOfR() + temp;
                if (temp.length() == 8){
                    if ((char) (Integer.parseInt(temp, 2)) != 0) {
                        ans += (char)(Integer.parseInt(temp, 2));
                    }
                    temp = "";
                }
            }
        }
        return ans;
    }

    public Image hideText(String text) {
        String binary = "";
        Pixel[][] newPixels = pixels;
        for (int i = 0; i < text.length(); i++){
            String bin = Integer.toBinaryString((int)text.charAt(i));
            while (bin.length() != 8){
                bin = "0" + bin;
            }
            bin = new StringBuilder(bin).reverse().toString();
            binary += bin;
        }
        int count = 0;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if(count < binary.length()){
                    int temp = binary.charAt(count) - '0';
                    newPixels[i][j] = newPixels[i][j].fixLowestBitOfR(temp);
                }
                else{
                    newPixels[i][j] = newPixels[i][j].fixLowestBitOfR(0);
                }
                count++;
            }
        }
        return new Image(newPixels);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage b = new BufferedImage(this.pixels.length, this.pixels[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < this.pixels.length; i++) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                b.setRGB(i, j, this.pixels[i][j].toInt());
            }
        }
        return b;
    }

    public void save(String filename) {
        File out = new File(filename);
        try {
            ImageIO.write(this.toBufferedImage(), filename.substring(filename.lastIndexOf(".") + 1, filename.length()), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
