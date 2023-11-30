package tasktracker;

import java.util.ArrayList;

public enum SizeEnum {
    S, M, L, XL;

    public static ArrayList<String> nameString() {
        ArrayList<String> nameArray = new ArrayList<String>();
        for(SizeEnum s : SizeEnum.values()) {
            nameArray.add(s.toString());
        }
        return nameArray;
    }
}
