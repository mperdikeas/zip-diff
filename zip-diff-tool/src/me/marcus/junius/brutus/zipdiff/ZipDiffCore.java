package me.marcus.junius.brutus.zipdiff;

import java.util.Map;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

public class ZipDiffCore {



    public static Map<String, DiffDatum> compare(File f1, File f2) throws IOException {
        Map<String, Long> f1entries2CRC = entryNames2CRC(new ZipFile(f1));
        Map<String, Long> f2entries2CRC = entryNames2CRC(new ZipFile(f2));
        return _compare(f1entries2CRC, f2entries2CRC);
    }


    public static Map<String, Long> entryNames2CRC(ZipFile zip) {
        Map<String, Long> rv = new LinkedHashMap<>();
        for (Enumeration e = zip.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) e.nextElement();

            String currentEntry = entry.getName();
            long crc = entry.getCrc();
            rv.put(currentEntry, crc);
        }
        return rv;
    }

    public static Map<String, DiffDatum> _compare(Map<String, Long> f1entries2CRC, Map<String, Long> f2entries2CRC) {
        Map<String, DiffDatum> rv = new LinkedHashMap<>();
        for (String f1Entry : f1entries2CRC.keySet()) {
            long f1CRC = f1entries2CRC.get(f1Entry);
            Long f2CRC = f2entries2CRC.get(f1Entry);
            rv.put(f1Entry, pronounceDiffDatum(f1CRC, f2CRC));
        }
        for (String f2Entry : f2entries2CRC.keySet())
            if (f1entries2CRC.containsKey(f2Entry))
                continue;
            else {
                Long f1CRC = null;
                long f2CRC = f2entries2CRC.get(f2Entry);
                rv.put(f2Entry, pronounceDiffDatum(f1CRC, f2CRC));
            }
        return rv;
    }

    private static DiffDatum pronounceDiffDatum(Long f1CRC, Long f2CRC) {
        if      ((f1CRC!=null) && (f2CRC!=null) && ( f1CRC.equals(f2CRC))) return DiffDatum.IN_BOTH_IDENTICAL;
        else if ((f1CRC!=null) && (f2CRC!=null) && (!f1CRC.equals(f2CRC))) return DiffDatum.IN_BOTH_DIFFERENT;
        else if ((f1CRC!=null) && (f2CRC==null)                          ) return DiffDatum.ONLY_IN_F1;
        else if ((f1CRC==null) && (f2CRC!=null)                          ) return DiffDatum.ONLY_IN_F2;
        else throw new RuntimeException(String.format("impossible %s - %s", f1CRC, f2CRC));
    }




}