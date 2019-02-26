/**
 * Creator: Zexuan-Li
 * Time: 27/4/2018
 * Version: 1.0
 * JDK: 1.8.0
 */

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;
public class Main {
    private static final  Logger logger = Logger.getLogger(Main.class.getCanonicalName());

    private static final int NUM_THREADS = 50;
    private static final  String INDEX_FILE = "index.html";

    private final File rootDirectory;
    private final int port;

    public Main(File rootDirectory, int port) throws IOException {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory + " does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
        this.port = port;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket server = new ServerSocket(port)){
            logger.info("Accepting connections on port " + server.getLocalPort());
            logger.info("Document Root:" + rootDirectory);

            while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new RequsetProcessor(rootDirectory, INDEX_FILE, request);
                    pool.submit(r);
                }
                catch (IOException ex) {
                    logger.log(Level.WARNING, "Error accepting connection", ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        File docroot;
        docroot = new File("/Users/lzx/IdeaProjects/LzxServer/Test");
        int port = 8080;
        try {
            Main webServer = new Main(docroot, port);
            System.out.println("----LzxServer has starup----");
            webServer.start();
        }
        catch (IOException ex) {
            logger.log(Level.WARNING, "Server could not start", ex);
        }

    }
}
