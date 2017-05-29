package ru.otus.homework08.working.classes;

import java.util.*;

public class SerializebleObject {
    private static final int DEFAULT_ARRAY_OR_COLLECTION_FIELDS_LEN = 5;
    private static final int DEFAULT_VALUE_FOR_NUMBER_FIELDS = 5;
    private static final int DEFAULT_VALUE_FOR_BOXED_NUMBER_FIELDS = 15;
    private static final String DEFAULT_VALUE_FOR_STRING_FIELD = "Mum washed the frame";

    private int intFld;
    private Integer boxedIntFld;

    private double doubleFld;
    private Double boxedDoubleFld;

    private String stringFld;

    private Dummy objFld;

    private int[] arrayFld;
    private Dummy[] objArrayFld;

    private List<Integer> listFld = new ArrayList<>();
    private List<Dummy> objListFld = new ArrayList<>();

    private Map<String, Integer> mapFld = new HashMap<>();
    private Map<Integer, Dummy> objMapFld = new HashMap<>();

    private Set<Integer> intSetFld = new HashSet<>();
    private Set<Dummy> objSetFld = new HashSet<>();

    public SerializebleObject() {
        objFld = new Dummy(DEFAULT_VALUE_FOR_NUMBER_FIELDS);

        intFld = DEFAULT_VALUE_FOR_NUMBER_FIELDS;
        boxedIntFld = DEFAULT_VALUE_FOR_BOXED_NUMBER_FIELDS;

        doubleFld = DEFAULT_VALUE_FOR_NUMBER_FIELDS;
        boxedDoubleFld = (double) DEFAULT_VALUE_FOR_BOXED_NUMBER_FIELDS;

        stringFld = DEFAULT_VALUE_FOR_STRING_FIELD;
    }

    public void fillPrivateCollectionsByFakeData() {

        arrayFld = new int[DEFAULT_ARRAY_OR_COLLECTION_FIELDS_LEN];
        objArrayFld = new Dummy[DEFAULT_ARRAY_OR_COLLECTION_FIELDS_LEN];

        for (int i = 1; i <= DEFAULT_ARRAY_OR_COLLECTION_FIELDS_LEN; i++) {
            listFld.add(i);
            mapFld.put("Key#" + i, i * DEFAULT_VALUE_FOR_NUMBER_FIELDS);
            objListFld.add(new Dummy(i));
            objMapFld.put(i, new Dummy(i));
            arrayFld[i - 1] = i;
            objArrayFld[i - 1] = new Dummy(i);
            intSetFld.add(i);
            objSetFld.add(new Dummy(i));

        }
    }

    @Override
    public String toString() {
        return "SerializebleObject{" +
                "intFld=" + intFld +
                ", boxedIntFld=" + boxedIntFld +
                ", doubleFld=" + doubleFld +
                ", boxedDoubleFld=" + boxedDoubleFld +
                ", stringFld='" + stringFld + '\'' +
                ", objFld=" + objFld +
                ", arrayFld=" + Arrays.toString(arrayFld) +
                ", objArrayFld=" + Arrays.toString(objArrayFld) +
                ", listFld=" + listFld +
                ", mapFld=" + mapFld +
                ", objListFld=" + objListFld +
                ", objMapFld=" + objMapFld +
                ", intSetFld=" + intSetFld +
                ", objSetFld=" + objSetFld +
                '}';
    }

    public boolean simpleFilldsEquals(SerializebleObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (intFld != o.intFld) return false;
        if (Double.compare(o.doubleFld, doubleFld) != 0) return false;
        if (boxedIntFld != null ? !boxedIntFld.equals(o.boxedIntFld) : o.boxedIntFld != null) return false;
        if (boxedDoubleFld != null ? !boxedDoubleFld.equals(o.boxedDoubleFld) : o.boxedDoubleFld != null)
            return false;
        return stringFld != null ? stringFld.equals(o.stringFld) : o.stringFld == null;
    }

    public boolean ojbectFieldsEquals(SerializebleObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return objFld != null ? objFld.equals(o.objFld) : o.objFld == null;
    }

    public boolean arrayFieldsEquals(SerializebleObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (!Arrays.equals(arrayFld, o.arrayFld)) return false;
        return Arrays.equals(objArrayFld, o.objArrayFld);
    }


    public String strCompare(SerializebleObject o, boolean showAll) {
        String res = "";
        res += (((!showAll && (intFld + "").equals(o.intFld + "")))? "" : ("intFld=" + intFld + "\n" + "intFld=" + o.intFld + "\n"));
        res += (((!showAll && (boxedIntFld + "").equals(o.boxedIntFld + "")))? "" : ("boxedIntFld=" + boxedIntFld + "\n" + "boxedIntFld=" + o.boxedIntFld + "\n"));
        res += (((!showAll && (doubleFld + "").equals(o.doubleFld + "")))? "" : ("doubleFld=" + doubleFld + "\n" + "doubleFld=" + o.doubleFld + "\n"));
        res += (((!showAll && (boxedDoubleFld + "").equals(o.boxedDoubleFld + "")))? "" : ("boxedDoubleFld=" + doubleFld + "\n" + "boxedDoubleFld=" + o.boxedDoubleFld + "\n"));
        res += (((!showAll && (stringFld + "").equals(o.stringFld + "")))? "" : ("stringFld=" + stringFld + "\n" + "stringFld=" + o.stringFld + "\n"));
        res += (((!showAll && (objFld + "").equals(o.objFld + "")))? "" : ("objFld=" + objFld + "\n" + "objFld=" + o.objFld + "\n"));
        res += (((!showAll && (stringFld + "").equals(o.stringFld + "")))? "" : ("stringFld=" + stringFld + "\n" + "stringFld=" + o.stringFld + "\n"));

        String arrStr1 = Arrays.toString(arrayFld);
        String arrStr2 = Arrays.toString(o.arrayFld);
        res += (((!showAll && (arrStr1 + "").equals(arrStr2 + "")))? "" : ("arrayFld=" + arrStr1 + "\n" + "arrayFld=" + arrStr2 + "\n"));

        arrStr1 = Arrays.toString(arrayFld);
        arrStr2 = Arrays.toString(o.arrayFld);
        res += (((!showAll && (arrStr1 + "").equals(arrStr2 + "")))? "" : ("objArrayFld=" + arrStr1 + "\n" + "objArrayFld=" + arrStr2 + "\n"));

        res += (((!showAll && (listFld + "").equals(o.listFld + "")))? "" : ("listFld=" + listFld + "\n" + "listFld=" + o.listFld + "\n"));
        res += (((!showAll && (mapFld + "").equals(o.mapFld + "")))? "" : ("mapFld=" + mapFld + "\n" + "mapFld=" + o.mapFld + "\n"));
        res += (((!showAll && (objListFld + "").equals(o.objListFld + "")))? "" : ("objListFld=" + objListFld + "\n" + "objListFld=" + o.objListFld + "\n"));
        res += (((!showAll && (objMapFld + "").equals(o.objMapFld + "")))? "" : ("objMapFld=" + objMapFld + "\n" + "objMapFld=" + o.objMapFld + "\n"));
        res += (((!showAll && (intSetFld + "").equals(o.intSetFld + "")))? "" : ("intSetFld=" + intSetFld + "\n" + "intSetFld=" + o.intSetFld + "\n"));
        res += (((!showAll && (objSetFld + "").equals(o.objSetFld + "")))? "" : ("objSetFld=" + objSetFld + "\n" + "objSetFld=" + o.objSetFld + "\n"));

        return res;
    }
}
