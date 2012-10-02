package com.jpragma.snmp.agent;

import java.util.Comparator;

public class OidComparator implements Comparator {

    public OidComparator() {
    }

    public final int compare(Object o1, Object o2) {
        if (!(o1 instanceof int[]) || !(o2 instanceof int[]))
            throw new ClassCastException("Compared objects must be int[]");
        int intArray1[] = (int[]) (int[]) o1;
        int intArray2[] = (int[]) (int[]) o2;
        int shorterIntArray[];
        int longerIntArray[];
        if (intArray1.length > intArray2.length) {
            shorterIntArray = intArray2;
            longerIntArray = intArray1;
        } else {
            shorterIntArray = intArray1;
            longerIntArray = intArray2;
        }
        for (int i = 0; i < shorterIntArray.length; i++)
            if (shorterIntArray[i] != longerIntArray[i])
                if (shorterIntArray[i] > longerIntArray[i])
                    return shorterIntArray != intArray1 ? -1 : 1;
                else
                    return longerIntArray != intArray1 ? -1 : 1;

        if (shorterIntArray.length == longerIntArray.length)
            return 0;
        else
            return longerIntArray != intArray1 ? -1 : 1;
    }
}
