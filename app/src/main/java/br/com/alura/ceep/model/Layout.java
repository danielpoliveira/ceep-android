package br.com.alura.ceep.model;

public enum Layout {
    LINEAR,
    GRID;

    public static Layout toLayout(String layoutString) {
        try {
            return valueOf(layoutString);
        } catch (Exception e) {
            // For error cases
            return LINEAR;
        }
    }


}
