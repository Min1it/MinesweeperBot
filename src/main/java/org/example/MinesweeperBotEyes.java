package org.example;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;
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

import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;

import javax.imageio.ImageIO;

//IMPORTANT: built for minesweeper.us with the "Windows 7" skin
public class MinesweeperBotEyes {

    public static void main(String[] args) throws Exception {
        Mat source = imread("C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\image.png");

        Mat template = imread("C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\0.png");
        Mat output = new Mat();
        //the results for matchTemplate mark the top left of where the template would be
        matchTemplate(source, template, output, TM_SQDIFF); //tm_ccoeff_normed seems to work the best  tm_sqdiff could also work because it's so specific

        imshow("source",source);
        imshow("results",output);
        waitKey(0);
        destroyAllWindows();
    }
//, 0=0, 9=unknown, 10=flag. The 0th list = 0, 10th list = 10, etc.
//    public static List<Boolean>[] getPositions(){
//
//        BufferedImage capture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//
//        String path = "C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\";
//        matchTemplate()
//
//        }




    }



