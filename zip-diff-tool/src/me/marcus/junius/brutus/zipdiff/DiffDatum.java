package me.marcus.junius.brutus.zipdiff;


public enum DiffDatum {

    ONLY_IN_F1("-X"), IN_BOTH_IDENTICAL("=="), IN_BOTH_DIFFERENT("!="), ONLY_IN_F2("X-");

    private String key;    

    private DiffDatum(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

}