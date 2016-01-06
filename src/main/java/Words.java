import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class Words {
    public static void forEachWord(Consumer<String> task) {
        try (InputStream in = Files.newInputStream(Paths.get("/usr/share/dict/words"))) {
            BufferedReader lines = new BufferedReader(new InputStreamReader(in));

            for (String line = lines.readLine(); line != null; line = lines.readLine()) {
                task.accept(line);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
