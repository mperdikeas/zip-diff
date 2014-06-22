package me.marcus.junius.brutus.zipdiff;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import com.beust.jcommander.Parameter;


public class ZipDiffCLI {


    @Parameter(names = {"-f1", "--file-1"}, description="zip file #1 to compare", required=true)
    public String f1;

    @Parameter(names = {"-f2", "--file-2"}, description="zip file #2 to compare", required=true)
    public String f2;

    /*
    @Parameter(names = {"-v", "--verbose"}, description="spit your heart out", required=false)
    public boolean verbose = false;


    */

    public static String checkValidCombinationOfParameters(ZipDiffCLI cli) {
        String f1msg = msgIfNotValidFile(cli.f1);
        if (f1msg!=null)
            return f1msg;
        String f2msg = msgIfNotValidFile(cli.f2);
        if (f2msg!=null)
            return f2msg;
        return null;
    }

    private static String msgIfNotValidFile(String fs) {
        File f = new File(fs);
        if ((!f.exists())||(!f.isFile()))
            return String.format("Path '%s' is not a file.", fs);
        return null;
    }
}