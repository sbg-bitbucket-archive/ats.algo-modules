package ats.algo.genericsupportfunctions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ZipUtil {

    /**
     * Reads a set of objects from a compressed file, converts from Json and returns as an array. Object names must be
     * of the form "xxx.json". objects with other extension types are ignored
     * 
     * @param filename file containing the compressed objects
     * @param headerObjectName the zipEntry name associated with the header object
     * @param headerClass the class for the header object
     * @param bodyClass the class for body objects
     * @return map of objects with their names. the ".json" recorded in the object name in the file is removed
     * @throws IOException
     */
    public static Map<String, Object> load(String pathName, String headerObjectName, Class<?> headerClass,
                    Class<?> bodyClass) throws IOException {
        Map<String, Object> map = new HashMap<>();
        FileSystem zipFileSystem = createZipFileSystem(pathName, false);
        final Path root = zipFileSystem.getPath("/");
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.printf("parsing file %s\n", file);
                addItemToMap(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.printf("Visiting folder %s\n", dir.toString());
                return FileVisitResult.CONTINUE;
            }

            private void addItemToMap(Path path) {
                String[] pathBits = path.toString().split("/");
                String fullName = pathBits[pathBits.length - 1];
                String name = fullName.substring(0, fullName.indexOf("."));
                String ext = fullName.substring(fullName.indexOf(".") + 1);
                boolean validObjectName = false;
                if (ext != null)
                    validObjectName = (ext.equals("json"));
                if (!validObjectName) {
                    System.out.println("Invalid object - name not of form xxx.json - ignoring");
                } else {
                    boolean readOk = false;
                    byte[] data = null;
                    @SuppressWarnings("unused")
                    String str = null;
                    Class<?> clazz = null;
                    try {
                        data = Files.readAllBytes(path);
                        str = new String(data);
                        clazz = name.equals(headerObjectName) ? headerClass : bodyClass;
                    } catch (IOException e) {
                    }
                    Object obj = null;
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        obj = mapper.readValue(data, clazz);
                        readOk = true;
                    } catch (IOException e) {
                        // do nothing
                    }
                    if (!readOk) {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.enableDefaultTyping();
                        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                        try {
                            obj = mapper.readValue(data, clazz);
                            readOk = true;
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            System.exit(1);
                        }
                    }
                    if (readOk)
                        map.put(name, obj);
                }

            }
        });
        return map;
    }

    /**
     * Returns a zip file system
     * 
     * @param zipFilename to construct the file system from
     * @param create true if the zip file should be created
     * @return a zip file system
     * @throws IOException
     */
    private static FileSystem createZipFileSystem(String zipFilename, boolean create) throws IOException {
        // convert the filename to a URI
        final Path path = Paths.get(zipFilename);
        final URI uri = URI.create("jar:file:" + path.toUri().getPath());

        final Map<String, String> env = new HashMap<>();
        if (create) {
            env.put("create", "true");
        }
        return FileSystems.newFileSystem(uri, env);
    }

    /**
     * saves a set of objects in json format to a compressed zip file with the supplied fileName
     * 
     * @param fileName
     * @param objects - map of objects to save. In the zip file each name is appended with ".json"
     * @throws IOException
     */
    public static void save(File file, Map<String, Object> objects) throws IOException {
        FileOutputStream dest = new FileOutputStream(file);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        for (Entry<String, Object> e : objects.entrySet()) {
            ZipEntry entry = new ZipEntry(e.getKey() + ".json");
            out.putNextEntry(entry);
            // String json = JsonUtil.marshalJson(e.getValue(),true);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(e.getValue());
            byte[] data = json.getBytes();
            out.write(data);
        }
        out.close();
    }

}
