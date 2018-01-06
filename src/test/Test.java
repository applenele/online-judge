package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by xanarry on 18-1-2.
 */
public class Test {
    static String dir = "/home/xanarry/Desktop/filetest";

    public static void main(String[] argv) throws FileNotFoundException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(getTestPointFileID(dir));
                }
            }
        });

        thread1.start();


        //addfolder();

    }

    private static synchronized int getTestPointFileID(String testPointDir) {
        File file = new File(testPointDir);
        if (!file.exists()) { //不存在则创建目录
            file.mkdir();
        } else if (file.isFile()) {//若存在且为文件, 则无法创建, 返回-1
            return -1;
        }

        File[] files = file.listFiles();
        Integer[] IDs = new Integer[files.length];

        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            int val = Integer.parseInt(name.substring(0, name.lastIndexOf(".")));
            IDs[i] = val;
        }

        Arrays.sort(IDs, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        int newID = IDs.length > 0 ? IDs[IDs.length - 1] + 1 : 1;
        for (int i = 0; i < files.length - 1; i++) {
            if (IDs[i] + 1 != IDs[i + 1]) {
                newID = IDs[i] + 1;
            }
        }

        File newFile = new File(testPointDir + "/" + newID + ".txt");
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newID;
    }
}
