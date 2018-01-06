package utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by xanarry on 18-1-1.
 */
public class Tools {
    public static BufferedImage getValidateCode(final int width, final int height, String randStr) {
        int codeX = (width - 10) / (randStr.length() + 1);
        //height - 10 集中显示验证码
        int fontHeight = height - 10;
        int codeY = height - 10;

        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = buffImg.createGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.LIGHT_GRAY);
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("consolas", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);
        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
        // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.gray);
        for (int i = 0; i < 16; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < randStr.length(); i++) {
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(randStr.charAt(i) + "", (i + 1) * codeX, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(randStr.charAt(i));
        }
        return buffImg;
    }


    public String TimeStamp2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }

    public static void showFiles(String path) {
        // get file list where the path has
        File file = new File(path);
        // get the folder list
        if (file.isDirectory()) {
            File[] array = file.listFiles();

            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile()) {
                    // only take file name
                    System.out.println("^^^^^" + array[i].getName());
                    // take file path and name
                    System.out.println("#####" + array[i]);
                    // take file path and name
                    System.out.println("*****" + array[i].getPath());
                } else if (array[i].isDirectory()) {
                    showFiles(array[i].getPath());
                }
            }
        } else {
            System.out.println("giving path is a file");
        }
    }

    public static boolean saveFile(InputStream inputStream, String path) {
        try {
            OutputStream outputStream = new FileOutputStream(new File(path));
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
            return false;
        }
        return true;
    }

    public static synchronized int saveTestPoint(String testPointDir, String inputData, String outputData) {
        System.out.println("testPointDir: " + testPointDir);

        File file = new File(testPointDir);
        if (!file.exists()) { //不存在则创建目录
            if (file.mkdir()) {
                file = new File(testPointDir); //重新打开目录
            } else {
                return -1;
            }
        } else if (file.isFile()) {//若存在且为文件, 则无法创建, 返回-1
            return -1;
        }

        File[] files = file.listFiles();
        ArrayList<Integer> testPointIDs = new ArrayList<>(10);
        for (File f : files) {
            String name = f.getName();
            if (name.endsWith(".in")) {
                int val = Integer.parseInt(name.substring(0, name.lastIndexOf(".")));
                testPointIDs.add(val);
            }
        }

        testPointIDs.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        int newID = testPointIDs.size() > 0 ? testPointIDs.get(testPointIDs.size() - 1) + 1 : 1;

        for (int i = 0; i < testPointIDs.size() - 1; i++) {
            if (testPointIDs.get(i) + 1 != testPointIDs.get(i + 1)) {
                newID = testPointIDs.get(i) + 1;
            }
        }

        //写入输入输出文件
        String inputTextPath = testPointDir + "/" + newID + ".in";
        String outputTextPath = testPointDir + "/" + newID + ".out";

        try {
            System.out.println("inputTextFile: " + inputTextPath);
            System.out.println("inputTextData: " + inputData);
            PrintWriter inPrintWriter = new PrintWriter(inputTextPath);
            inPrintWriter.write(inputData);
            inPrintWriter.flush();
            inPrintWriter.close();

            System.out.println("outputTextFile: " + outputTextPath);
            System.out.println("outputTextData: " + outputData);
            PrintWriter outPrintWriter = new PrintWriter(outputTextPath);
            outPrintWriter.write(outputData);
            outPrintWriter.flush();
            outPrintWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return newID;
    }

    public static synchronized boolean deleteTestPoint(String testPointDir, int testPointID) {
        String inputTextPath = testPointDir + "/" + testPointID + ".in";
        String outputTextPath = testPointDir + "/" + testPointID + ".out";

        System.out.println("delete inputText: " + inputTextPath);
        System.out.println("delete outputText: " + outputTextPath);
        File inputText = new File(inputTextPath);
        File outputText = new File(outputTextPath);

        return inputText.delete() && outputText.delete();
    }


    public static String readFileToString(String path) {
        System.out.println("read file path: " + path);
        String content = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            content = new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void main(String[] argv) {
        String path = "/home/xanarry/Desktop/filetest/1.txt";
        System.out.println(path);
        System.out.println(readFileToString(path));
    }
}
