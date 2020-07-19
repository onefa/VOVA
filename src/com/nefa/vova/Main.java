package com.nefa.vova;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File filePath;
        File[] listFiles;
        int startX = 143;
        int startY = 585;
        final int width = 63;
        final int height = 88;
        final int step = 72;

        if (args.length != 1) {
            throw new IOException("Error in arguments");
        }
        filePath = new File(args[0]);
        listFiles = filePath.listFiles();

        if (listFiles != null) {
            for (File file : listFiles) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    throw new IOException("Error reading image: " + file.toString());
                }
                StringBuilder cardCode = new StringBuilder(file.toString().substring(filePath.toString().length()+1) + " - ");
                int i = 0;
                do {
                    BufferedImage card = image.getSubimage(startX+i++*step, startY, width, height);
                    String recognizeCard = ImageRecognizer.recognize(card);
                    if (!recognizeCard.equals("No card")) {
                        cardCode.append(recognizeCard);
                    } else {
                        if (i == 1) {
                            cardCode.append(recognizeCard);
                        }
                        break;
                    }
                } while (true);
                System.out.println(cardCode);
            }
        }
    }
}

class ImageRecognizer {

    static String recognize(BufferedImage card) {
        String result = "No card";
        if (isPoint(card, 40, 22) == 1) return result;
        int cardCode = (isPoint(card, 9, 7)<<11) + (isPoint(card, 15, 7)<<10) + (isPoint(card, 22, 7)<<9) +
                       (isPoint(card, 9, 13)<<8) + (isPoint(card, 17, 13)<<7) + (isPoint(card, 21, 13)<<6) +
                       (isPoint(card, 9, 21)<<5) + (isPoint(card, 15, 21)<<4) + (isPoint(card, 21, 21)<<3) +
                       (isPoint(card, 12, 27)<<2) + (isPoint(card, 15, 27)<<1) + isPoint(card, 24, 28);
        switch (cardCode) {
            case 1110: case 1111: case 1119: case 3159: result = "2"; break;
            case 3278: case 3790: result = "3"; break;
            case 760: case 761: result = "4"; break;
            case 1550: case 1806: case 3342: case 3854: case 3850: result = "5"; break;
            case 1326: case 1834: case 1838: result = "6"; break;
            case 3670: case 3798: result = "7"; break;
            case 1390: case 3438: result = "8"; break;
            case 1374: result = "9"; break;
            case 2997: result = "10"; break;
            case 78: case 206: result = "J"; break;
            case 1827: case 1835: case 1839: result = "Q"; break;
            case 3053: case 3068: case 3069: result = "K"; break;
            case 1145: case 1209: case 1213: case 1273: result = "A"; break;
        }
        return result + suit(card);
    }

    static int isPoint (BufferedImage image, int x, int y) {
        int result = 0;
        for (int i = -1; i<2; i++) {
            Color c = new Color(image.getRGB(x+i,y+i));
            if (c.getGreen()<100) result = 1;
        }
        return result;
    }

    private static String suit (BufferedImage card) {
        if (new Color(card.getRGB(40, 65)).getRed() < 80) {
            if (new Color(card.getRGB(36, 53)).getRed() < 100) {
                return "c";
            } else return "s";
        } else if (new Color(card.getRGB(42, 54)).getBlue() < 100) {
                return "d";
            } else return "h";
    }
}
