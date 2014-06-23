package me.marcus.junius.brutus.zipdiff;

import java.util.Map;
import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;




public class ZipDiff {



    public static void main(String args[]) throws IOException {
        ZipDiffCLI cli = cli(ZipDiff.class.getName(), args, ZipDiffCLI.class);
        exitIfMessage(ZipDiffCLI.checkValidCombinationOfParameters( cli ));
        Map<String, DiffDatum> results = ZipDiffCore.compare(new File(cli.f1), new File(cli.f2));
        for (String entry : results.keySet())
            System.out.printf("%s : %s\n", results.get(entry).getKey(), entry);
    }




    public static <T> void usage(String programName, Class<T> klass) {
        try {
            T cli = klass.getConstructor().newInstance();
            JCommander jc = new JCommander(cli);
            jc.setProgramName(programName);
            jc.usage();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T cli(String programName, String args[], Class<T> klass) {
        try {
            T cli = klass.getConstructor().newInstance();
            try {
                new JCommander(cli, args);
            } catch (ParameterException e) {
                JCommander jc = new JCommander(cli);
                jc.setProgramName(programName);
                jc.usage();
                System.exit(1);
            }
    
            try {
                if (klass.getField("help").getBoolean(cli)) {
                    JCommander jc = new JCommander(cli);
                    jc.setProgramName(programName);
                    jc.usage();
                    System.exit(0);
                }
            } catch (NoSuchFieldException | SecurityException e) {
                // silently suppress
            }
            return cli;    
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    protected static void exit(String message) {
        exit(message, 1);
    }

    protected static void exit(String message, int exitCode) {
        System.out.println(message);
        System.exit(exitCode);
    }

    protected static void exitIfMessage(String message) {
        if (message!=null)
            exit(message);
    }







}