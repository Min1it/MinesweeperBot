package org.example;

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

//IMPORTANT: built for minesweeper.us with the "Windows 7" skin
public class MinesweeperBotEyes {
    Mat source = imread("C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\image.png");

    public static void main(String[] args) throws Exception {

        List<Point> temp = new MinesweeperBotEyes().getPositions(1);
        for (Point point : temp){
            System.out.println("" + point.x() + ", " + point.y());
        }
        System.out.println(temp.size());
        waitKey(0);
        destroyAllWindows();
    }
        //9 = unknown, 10 = flag
      public List<Point> getPositions(int target){
        float THRESHHOLD = 1000000.0f;      //i feel like screaming case isn't supposed to be used here, but idk
        String templatePath = "C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\";
        switch(target){
              case 0:
                  templatePath += "0.png";
                  break;
              case 1:
                  templatePath += "1.png";
                  break;
              case 2:
                  templatePath += "2.png";
                  break;
              case 3:
                  templatePath += "3.png";
                  break;
              case 4:
                  templatePath += "4.png";
                  break;
              case 5:
                  templatePath += "5.png";
                  break;
              case 6:
                  templatePath += "6.png";
                  break;
              case 7:
                  templatePath += "7.png";
                  break;
              case 8:
                  templatePath += "8.png";
                  break;
              case 9:
                  templatePath += "unknown.png";
                  break;
              case 10:
                  templatePath += "flag.png";
        }
        Mat template = imread(templatePath);
        Mat result = new Mat();
        matchTemplate(source, template, result, TM_SQDIFF);
        imshow("result", result);
        imshow("source", source);

        List<Point> points = new ArrayList<Point>();     //pretty much directly taken from Waldemar Neto
          FloatIndexer indexer = result.createIndexer();
          for (int y = 0; y < result.rows(); y++) {
              for (int x = 0; x < result.cols(); x++) {
                  if (indexer.get(y,x)<THRESHHOLD) {
                      points.add(new Point(x, y));
                  }
              }
          }


          return points;
      }
//, 0=0, 9=unknown, 10=flag. The 0th list = 0, 10th list = 10, etc.
//    public List[][] getBoard(){
//
//        BufferedImage capture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//
//        String path = "C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\";
//        matchTemplate()
//
//        }




    }



