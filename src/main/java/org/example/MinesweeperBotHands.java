package org.example;

import org.bytedeco.opencv.opencv_core.Mat;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;
import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_highgui.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.List;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_cudaimgproc.TemplateMatching;
import org.opencv.core.Core;

import javax.imageio.ImageIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import static org.bytedeco.opencv.global.opencv_highgui.destroyAllWindows;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

public class MinesweeperBotHands {
    public static void main(String[] args) {
        try {
            Thread.sleep(4000);
            while(true) {
                Thread.sleep(1000);
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_WINDOWS);
                robot.keyPress(KeyEvent.VK_PRINTSCREEN);
                robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
                robot.keyRelease(KeyEvent.VK_WINDOWS);
                Thread.sleep(1000);
                //IMPORTANT You will need to modify the path to your username, or OS
                File dir = new File("C:\\Users\\fearl\\Pictures\\Screenshots");
                File[] files = dir.listFiles();

                File lastModifiedFile = files[0];
                for (int i = 1; i < files.length; i++) {
                    if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                        lastModifiedFile = files[i];
                    }
                }
                MinesweeperBotEyes eyes = new MinesweeperBotEyes(imread(lastModifiedFile.getAbsolutePath()));

//            MinesweeperBotEyes eyes = new MinesweeperBotEyes(imread("C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\image.png"));

                eyes.setBoard();

                System.out.println("x_min: " + eyes.x_min);
                System.out.println("y_min: " + eyes.y_min);
                System.out.println("x_max: " + eyes.x_max);
                System.out.println("y_max: " + eyes.y_max);

                System.out.println("Spacing: " + eyes.spacing);

                //I get it's an array of arrays, but I still feel like clone should go as deep as possible
                int[][] originalBoard = new int[eyes.board.length][];
                for (int i = 0; i < eyes.board.length; i++) {
                    originalBoard[i] = eyes.board[i].clone();
                }

                for (int i = 0; i < eyes.board[0].length; i++) {
                    for (int j = 0; j < eyes.board.length; j++) {
                        if (eyes.board[j][i] == -1) {
                            System.out.print("-");
                            continue;
                        }
                        System.out.print(eyes.board[j][i]);
                    }
                    System.out.println("");
                }

                boolean isComplete = true;
                for (int i = 0; i < eyes.board[0].length; i++) {
                    for (int j = 0; j < eyes.board.length; j++) {
                        if (eyes.board[j][i] == -1) {
                            isComplete = false;
                            break;
                        }
                    }
                }
                if(isComplete){
                    System.out.println("No unknowns detected. Game is complete");
                    break;
                }

                //since realistically, the main time constraints is the template matching, running comparePairUsingSets first is probably faster in the long run
                System.out.println("pair comparison with mathematical sets is being used");
                System.out.println("simple mine and safe detection is being used");
                eyes = MinesweeperBotBrain.comparePairUsingSets(eyes);
                eyes = MinesweeperBotBrain.simpleFindMine(eyes);
                eyes = MinesweeperBotBrain.simpleFindZero(eyes);


                boolean isIdentical = true;
                for (int i = 0; i < eyes.board.length; i++) {
                    if (!Arrays.equals(eyes.board[i], originalBoard[i])) {
                        isIdentical = false;
                    }
                }
                if (isIdentical) {
                    System.out.println("\t simple finds and pair set comparison has yielded no results");
                    System.out.println("backtracking is being used");
                    System.out.println("note that the backtracking algorithm may guess and fail due to the nature of minesweeper");
                    eyes = MinesweeperBotBrain.backtrackingRecursion(eyes, 0, 20);
                    eyes = MinesweeperBotBrain.comparePairUsingSets(eyes);
                    eyes = MinesweeperBotBrain.simpleFindMine(eyes);
                    eyes = MinesweeperBotBrain.simpleFindZero(eyes);
                }

                isIdentical = true;
                for (int i = 0; i < eyes.board.length; i++) {
                    if (!Arrays.equals(eyes.board[i], originalBoard[i])) {
                        isIdentical = false;
                    }
                }
                //i'm pretty sure this will never be used because of the way backtracking is written, but it's nice to have ig
                if (isIdentical) {
                    System.out.println("\t backtracking has yielded no results. \nA guess will be executed");
                }

                for (int i = 0; i < eyes.board[0].length; i++) {
                    for (int j = 0; j < eyes.board.length; j++) {
                        if (eyes.board[j][i] == -1) {
                            System.out.print("-");
                            continue;
                        }
                        System.out.print(eyes.board[j][i]);
                    }
                    System.out.println("");
                }


                //getting screen dimensions code taken from mdma on stack overflow, I wonder if this counts as "common knowledge"
                //TODO: check how the program works on mac, especially this part
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                int width = gd.getDisplayMode().getWidth();

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double fakeWidth = screenSize.getWidth();
                double fakeHeight = screenSize.getHeight();

                double conversionRatio = fakeWidth / width;
                System.out.println("Conversion Ratio: " + conversionRatio);


                //I don't need a sleep statement because the image processing takes so long lol
//            Thread.sleep(3000);
                //for some reason java thinks my monitor is 1152p instead of 1440p, it got the aspect ratio right though


                for (int i = 0; i < eyes.board.length; i++) {
                    for (int j = 0; j < eyes.board[0].length; j++) {
                        if (eyes.board[i][j] != originalBoard[i][j]) {
                            if (((eyes.board[i][j] == 0) || (eyes.board[i][j] == 1338)) && (originalBoard[i][j] == -1)) {
                                int xPosition = eyes.x_min + (50 * i);
                                int yPosition = eyes.y_min + (50 * j);
                                Thread.sleep(5);
                                robot.mouseMove((int) (conversionRatio * (xPosition + 25)), (int) (conversionRatio * (yPosition + 25)));
                                Thread.sleep(5);
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                Thread.sleep(5);
                                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                                continue;
                            }
                            if ((eyes.board[i][j] == 9) && (originalBoard[i][j] == -1)) {
                                int xPosition = eyes.x_min + (50 * i);
                                int yPosition = eyes.y_min + (50 * j);
                                Thread.sleep(5);
                                robot.mouseMove((int) (conversionRatio * (xPosition + 25)), (int) (conversionRatio * (yPosition + 25)));
                                Thread.sleep(5);
                                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                                Thread.sleep(5);
                                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                                continue;
                            }
                            System.out.println("Something has gone very wrong");
                        }
                    }
                }
                robot.mouseMove((int) (conversionRatio * (eyes.x_max + 100)), (int) (conversionRatio * (eyes.y_max + 100)));
                System.out.println("cycle complete");
            }



        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println(ex.getStackTrace());
            System.out.println(ex.getCause());
            ex.printStackTrace();
        }
    }
}
