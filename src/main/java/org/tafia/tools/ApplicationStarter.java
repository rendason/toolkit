package org.tafia.tools;

import org.tafia.tools.application.ApplicationFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.tafia.tools.application.Global.*;

/**
 * 应用程序启动器
 *
 * 程序启动时，当前进程将原始jar文件复制到临时目录形成临时jar，
 * 利用临时jar启动子进程，并将原始jar路径传给子进程，当前进程退出，
 * 子进程可将配置文件更新回原始jar文件
 */
public class ApplicationStarter {

    public static void main(String[] args) throws IOException {
        String originJarKey = getGlobalConfig().getOriginJarKey(); //JVM参数的key，用于记录原始jar文件的绝对路径
        File codeSource;
        if (System.getProperty(originJarKey) == null && ((codeSource = codeSource().toFile()).isFile())) {
            //首次启动时将jar复制到临时目录，并以复制的jar文件启动子进程，当前进程退出
            File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            File backup = new File(tmpDir, codeSource.getName());
            Files.copy(codeSource.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            new ProcessBuilder()
                    .directory(tmpDir)
                    .command("java", "-D" + originJarKey + "=" + codeSource.getAbsolutePath(),
                            "-jar", backup.getName())
                    .redirectError(new File(tmpDir, "toolkit.err"))
                    .redirectOutput(new File(tmpDir, "toolkit.out"))
                    .start();
        } else {
            SwingUtilities.invokeLater(() -> {
                ApplicationFrame applicationFrame = new ApplicationFrame();
                applicationFrame.setVisible(true);
            });
        }
    }
}
