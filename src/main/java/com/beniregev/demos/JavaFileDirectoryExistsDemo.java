package com.beniregev.demos;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class JavaFileDirectoryExistsDemo {
    private final String BASE_WORKING_DIR = (new File(".")).getAbsolutePath().replace("\\", "/");
    private final String BASE_RESOURCES_DIR = "target/classes";

    private StringBuilder resourcesDir = new StringBuilder(BASE_WORKING_DIR);

    private Locale userLocate;
    private String patientCsvFile;

    public JavaFileDirectoryExistsDemo(final String localeString) {
        this(localeString, new Locale(localeString));
    }

    public JavaFileDirectoryExistsDemo(final String localeString, Locale userLocate) {
        this.userLocate = userLocate;
        int indexOf = resourcesDir.lastIndexOf(".");
        System.out.println("indexOf(\".\") = " + indexOf);

        userLocate = new Locale(localeString);
        patientCsvFile = "practices_" + localeString + ".csv";

        resourcesDir = resourcesDir.deleteCharAt(indexOf).append(BASE_RESOURCES_DIR).append("/");
    }

    public String getBASE_WORKING_DIR() {
        return BASE_WORKING_DIR;
    }

    public String getBASE_RESOURCES_DIR() {
        return BASE_RESOURCES_DIR;
    }

    public StringBuilder getResourcesDir() {
        return resourcesDir;
    }

    public void setResourcesDir(StringBuilder resourcesDir) {
        this.resourcesDir = resourcesDir;
    }

    public Locale getUserLocate() {
        return userLocate;
    }

    public void setUserLocate(Locale userLocate) {
        this.userLocate = userLocate;
    }

    public String getPatientCsvFile() {
        return patientCsvFile;
    }

    public void setPatientCsvFile(String patientCsvFile) {
        this.patientCsvFile = patientCsvFile;
    }

    public Path getCurrentWorkingPath() {
        return Paths.get("").toAbsolutePath();
    }

    public static void main(String[] args) {

        JavaFileDirectoryExistsDemo javaFIleDirectoryExistsDemo = new JavaFileDirectoryExistsDemo("iw_IL");

        System.out.println("------- public static void main(String[] args) -------");
        System.out.println("Going to create fileOrDirectory");
        System.out.println("BASE_WORKING_DIR = \"" + javaFIleDirectoryExistsDemo.getBASE_WORKING_DIR() + "\"");
        System.out.println("BASE_RESOURCES_DIR = \"" + javaFIleDirectoryExistsDemo.getBASE_RESOURCES_DIR() + "\"");
        System.out.println("resourcesDir = \"" + javaFIleDirectoryExistsDemo.getResourcesDir() + "\"");

        // test "/var/tmp" directory
        File fileOrDirectory = new File(String.valueOf(javaFIleDirectoryExistsDemo.getResourcesDir()));
        System.out.println("fileOrDirectory value is " + (fileOrDirectory == null ? "null" : fileOrDirectory.toString()));

        String currentDirectory = fileOrDirectory.getAbsolutePath();
        System.out.println("Current working directory is \"" + currentDirectory + "\"");

        System.out.println("\"" + fileOrDirectory + "\"" + (fileOrDirectory.exists() ? " does exists." : " does NOT exists."));
        System.out.println("\"" + fileOrDirectory + "\"" + (fileOrDirectory.isDirectory() ? " is a directory" : " is NOT a directory"));

        // test to see if a file exists
        File csvFile = new File(javaFIleDirectoryExistsDemo.getResourcesDir().toString() + javaFIleDirectoryExistsDemo.getPatientCsvFile());
        System.out.println(csvFile == null ? "\"" + csvFile + "\" is null" : "\"" + csvFile + "\" is NOT null");
        boolean exists = csvFile.exists();
        if (csvFile.exists() && csvFile.isFile()) {
            System.out.println("file exists, and it is a file");
        }

        String currentSystemUserDir = System.getProperty("user.dir");
        System.out.println("currentSystemUserDir: " + (currentSystemUserDir != null ? currentSystemUserDir : " null"));

        Path currentWorkingPath = javaFIleDirectoryExistsDemo.getCurrentWorkingPath();
        System.out.println("currentWorkingPath:   " + (currentWorkingPath != null ? currentWorkingPath.normalize().toString() : "null"));

        Locale locale = new Locale("es", "ES");
        System.out.println("------- Locale Information -------");
        System.out.println("Country: " + (locale.getCountry() == null ? "null" : locale.getCountry()) );
        System.out.println("Language: " + (locale.getLanguage() == null ? "null" : locale.getLanguage()) );
        System.out.println("Variant: " + (locale.getVariant() == null ? "null" : locale.getVariant()) );
        System.out.println("Display Name: " + (locale.getDisplayName() == null ? "null" : locale.getDisplayName()));
        System.out.println("Display Country: " + (locale.getDisplayCountry() == null ? "null" : locale.getDisplayCountry()));
        System.out.println("Display Language: " + (locale.getDisplayLanguage() == null ? "null" : locale.getDisplayLanguage()) );


    }
}
