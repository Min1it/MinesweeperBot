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

    //probably should put setter for source to make it actually static and make sense;
    Mat source;

    boolean isSuccess = false;
    boolean isCompleteFailure = false;
    boolean isPartialFailure = false;
    int[][] board;
    int x_min = Integer.MAX_VALUE;
    int y_min = Integer.MAX_VALUE;
    int x_max = Integer.MIN_VALUE;
    int y_max = Integer.MIN_VALUE;

    int spacing;

    public MinesweeperBotEyes(){
        source = imread("C:\\Users\\fearl\\Pictures\\Screenshots\\Screenshot (286).png");
    }

    public MinesweeperBotEyes(Mat source){
        this.source = source;
    }

    public static void main(String[] args) throws Exception {

        MinesweeperBotEyes eyes = new MinesweeperBotEyes();
//        List<Point> temp = eyes.getPositions(1);
//        for (Point point : temp) {
//            System.out.println("" + point.x() + ", " + point.y());
//        }
//        System.out.println(temp.size());
        eyes.setBoard();

        System.out.println("x_min: " + eyes.x_min);
        System.out.println("y_min: " + eyes.y_min);
        System.out.println("x_max: " + eyes.x_max);
        System.out.println("y_max: " + eyes.y_max);

        System.out.println("Spacing: " + eyes.spacing);

        for(int i=0; i<eyes.board[0].length;i++){
            for (int j=0; j<eyes.board.length; j++){
                if(eyes.board[j][i] == -1) {
                    System.out.print("-");
                    continue;
                }
                System.out.print(eyes.board[j][i]);
            }
            System.out.println("");
        }







    }

    //-1 = unknown, 9 = flag
    public List<Point> getPositions(int target) {
        float THRESHHOLD = 2000000.0f;      //i feel like screaming case isn't supposed to be used here, but idk was 2000000
        String templatePath = "C:\\Users\\fearl\\Documents\\MinesweeperBot\\src\\main\\resources\\";
        switch (target) {
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
            case -1:
                templatePath += "unknown.png";
                break;
            case 9:
                templatePath += "flag.png";
        }
        Mat template = imread(templatePath);
        Mat result = new Mat();
        matchTemplate(source, template, result, TM_SQDIFF);


        List<Point> points = new ArrayList<Point>();     //pretty much directly taken from Waldemar Neto
        FloatIndexer indexer = result.createIndexer();
        for (int y = 0; y < result.rows(); y++) {
            for (int x = 0; x < result.cols(); x++) {
                if (indexer.get(y, x) < THRESHHOLD) {
                    points.add(new Point(x, y));
                }
            }
        }
//        if(target == 2){
//            System.out.println(points.size());
//        }


        return points;
    }

    //, 0=0, -1=unknown, 9=flag. The 0th list = 0, 10th list = 10, etc.
//    Note that the point locations mark the top left of the template, and also that x increases from left to right and Y INCREASES FROM TOP TO BOTTOM
//    sets [][] board and maxes and mins as well as the spacing
    public void setBoard() {
        //finding maxes and mins on first startup, probably should put this in the constructor eventually
        //TODO put this in the constructor instead
        if (x_min == Integer.MAX_VALUE) {
            for (int i = -1; i < 4; i++) {
                for (Point point : getPositions(i)) {
                    x_max = (point.x() > x_max) ? point.x() : x_max;
                    y_max = (point.y() > y_max) ? point.y() : y_max;
                    x_min = (point.x() < x_min) ? point.x() : x_min;
                    y_min = (point.y() < y_min) ? point.y() : y_min;
                }
            }

            //finding spacing
            //If there's a duplicate identification, this is screwed, so let's hope there's no duplicates ¯\_ (ツ)_/¯
            //also hopefully it's a perfect square grid
            spacing = Integer.MAX_VALUE;
            int temp = 0;
            for (int i = -1; i < 3; i++) {
                List<Point> points = getPositions(i);
                for (int j = 0; j < points.size() - 1; j++) {
                    temp = points.get(j + 1).x() - points.get(j).x();
                    if (temp <= 8) {
                        continue;
                    }
                    spacing = (temp < spacing) ? temp : spacing;
                }
            }
        }
        int temp;
        int temp2;
        int temp3;
        int temp4;
        int simplifiedPoint;


        //I don't want to look up the rounding stuff so yeah
        temp = (x_max - x_min) % spacing;
        temp2 = (((x_max - x_min) - temp) / spacing) + 1;
        if (temp >= (spacing - 8)) {
            temp2++;
        }

        temp3 = (y_max - y_min) % spacing;
        temp4 = (((y_max - y_min) - temp3) / spacing) + 1;
        if (temp3 >= (spacing - 8)) {
            temp4++;
        }
        board = new int[temp2][temp4];

        for (int i = -1; i < 10; i++) {
            for (Point point : getPositions(i)) {
                temp = (point.x() - x_min) % spacing;
                temp2 = ((point.x() - x_min) - temp) / spacing;
                if (temp >= (spacing - 8)) {
                    System.out.println("A has been triggered");
                    temp2++;
                }

                temp3 = (point.y() - y_min) % spacing;
                temp4 = ((point.y() - y_min) - temp3) / spacing;
                if (temp3 >= (spacing - 8)) {
                    System.out.println("B has been triggered");
                    temp4++;
                }
                board[temp2][temp4] = i;
            }
        }


    }

    //these aren't "real" getters but it works
    //also using -1 for the unknown tiles was kind of stupid in hindsight, but whatever
    public int getNW(int x, int y){
        if((x != 0) && (y!= 0)){
            return board[x-1][y-1];
        }
        return -2;
    }

    public int getN(int x, int y){
        if(y!= 0){
            return board[x][y-1];
        }
        return -2;
    }

    public int getNE(int x, int y){
        if((x != board.length - 1) && (y!= 0)){
            return board[x+1][y-1];
        }
        return -2;
    }

    public int getW(int x, int y){
        if(x != 0){
            return board[x-1][y];
        }
        return -2;
    }

    public int getE(int x, int y){
        if(x != board.length - 1){
            return board[x+1][y];
        }
        return -2;
    }

    public int getSW(int x, int y){
        if((x != 0) && (y!= board[0].length -1)) {
            return board[x - 1][y + 1];
        }
        return -2;
    }

    public int getS(int x, int y){
        if(y != board[0].length -1){
            return board[x][y+1];
        }
        return -2;
    }

    public int getSE(int x, int y){
        if((x != board.length -1) && (y!= board[0].length -1)){
            return board[x+1][y+1];
        }
        return -2;
    }

    public boolean is1Through8(int x, int y){
        if ((board[x][y] >= 1) && board[x][y] <= 8){
            return true;
        }
        return false;
    }

    public int getNumberOfAround(int target, int x, int y){
        int result = 0;
        if(target == getNW(x, y)){
            result ++;
        }
        if(target == getN(x, y)){
            result ++;
        }
        if(target == getNE(x, y)){
            result ++;
        }
        if(target == getW(x, y)){
            result ++;
        }
        if(target == getE(x, y)){
            result ++;
        }
        if(target == getSE(x, y)){
            result ++;
        }
        if(target == getS(x, y)){
            result ++;
        }
        if(target == getSW(x, y)){
            result ++;
        }
        return result;
    }
}



