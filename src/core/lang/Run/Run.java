package Run;

import Scanner.Scanner;
import Scanner.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static Error.Error.hadError;

public class Run {
    public static void fileRunner(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        runner(new String(bytes, Charset.defaultCharset()));
        if (hadError){
            System.exit(65);
        }
    }

    public static void promptRunner() throws IOException {
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(in);

        for (;;) {
            System.out.print("==> ");
            String line = reader.readLine();
            if (line == null){
                break;
            }
            runner(line);
            hadError = false;
        }
    }

    private static void runner(String sourceCode){
        Scanner scanner = new Scanner(sourceCode);
        List<Token> tokens = scanner.tokenize();
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }
}
