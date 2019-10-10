package org.pp.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * User: Bison
 * Date: 13-7-11
 * Time: 上午2:57
 */
public class FileUtil {
    public static byte[] readFile(String file) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return readFile(raf);
    }

    public static byte[] readFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return readFile(raf);
    }

    private static byte[] readFile(RandomAccessFile raf) {
        FileChannel channel = null;
        channel = raf.getChannel();
        try {
            long len = raf.length();
            if (len > Integer.MAX_VALUE)
                return null;
            ByteBuffer buffer = ByteBuffer.allocate((int) len);
            channel.read(buffer);
            byte[] data = buffer.array();
            raf.close();
            channel.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(byte[] bytes, String file) {
        FileChannel channel = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        channel = raf.getChannel();
        try {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            channel.write(buffer);
            raf.close();
            channel.close();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }


    public static void writeFile(byte[] bytes, File file) {
        FileChannel channel = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        channel = raf.getChannel();
        try {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            channel.write(buffer);
            raf.close();
            channel.close();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void appendFile(File file, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            String emptyLine = System.getProperty("line.separator");
            randomFile.writeBytes(content + emptyLine);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static File getSaveFile(Component dlgParent, String description, String... fileExtensions) {
        JFileChooser fChooser = getFileChooser(description, fileExtensions);
        fChooser.setDialogTitle("保存文件");
        File f;
        do {
            int rt = fChooser.showSaveDialog(dlgParent);
            if (rt != JFileChooser.APPROVE_OPTION) {
                if (rt == JFileChooser.ERROR_OPTION)
                    JOptionPane.showMessageDialog(dlgParent, "打开文件失败。");
                return null;
            }
            if (!endsWith(fChooser.getSelectedFile().getName(), fileExtensions) && fileExtensions != null && fileExtensions.length > 0) {
                f = new File(fChooser.getSelectedFile().getPath() + fileExtensions[0]);
            } else {
                f = fChooser.getSelectedFile();
            }
        }
        while (f != null && f.exists() && JOptionPane.showConfirmDialog(dlgParent, "文件已存在，是否覆盖？", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);

        return f;
    }

    public static JFileChooser getFileChooser(final String description, final String... fileExtensions) {
        JFileChooser fChooser = new JFileChooser();
        fChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || endsWith(f.getName(), fileExtensions);
            }

            @Override
            public String getDescription() {
                return description;
            }
        });
        return fChooser;
    }

    private static boolean endsWith(String fileName, String... fileExtensions) {
        if (fileExtensions == null || fileExtensions.length == 0)
            return true;

        fileName = fileName.toLowerCase();
        for (String ext : fileExtensions) {
            if (fileName.endsWith(ext.toLowerCase()))
                return true;
        }
        return false;
    }

}
