package org.tafia.tools;

import org.tafia.tools.application.ApplicationFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.tafia.tools.application.Global.*;

/**
 * Created by Dason on 2018/4/20.
 */
public class ApplicationStarter {

    public static void main(String[] args) throws IOException {
        String originJarKey = getGlobalConfig().getOriginJarKey();
        File codeSource;
        if (System.getProperty(originJarKey) == null && ((codeSource = codeSource().toFile()).isFile())) {
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
