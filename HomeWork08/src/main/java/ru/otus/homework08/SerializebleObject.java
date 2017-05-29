package ru.otus.homework08;

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

    private SerializebleObject objFld;

    private int[] arrayFld;
    private SerializebleObject[] objArrayFld;

    private List<Integer> listFld = new ArrayList<>();
    private Map<String, Integer> mapFld = new HashMap<>();

    private List<SerializebleObject> objListFld = new ArrayList<>();
    private Map<Integer, SerializebleObject> objMapFld = new HashMap<>();

    private Set<Integer> intSetFld = new HashSet<>();
    private Set<SerializebleObject> objSetFld = new HashSet<>();

    public SerializebleObject() {
        intFld = DEFAULT_VALUE_FOR_NUMBER_FIELDS;
        boxedIntFld = DEFAULT_VALUE_FOR_BOXED_NUMBER_FIELDS;

        doubleFld = DEFAULT_VALUE_FOR_NUMBER_FIELDS;
        boxedDoubleFld = (double) DEFAULT_VALUE_FOR_BOXED_NUMBER_FIELDS;

        stringFld = DEFAULT_VALUE_FOR_STRING_FIELD;
    }

    public SerializebleObject(int allFieldsValue) {
        this.intFld = allFieldsValue;
        this.boxedIntFld = allFieldsValue;
        this.doubleFld = (double)allFieldsValue;
        this.boxedDoubleFld = (double)allFieldsValue;
        this.stringFld = allFieldsValue + "";
    }

    public void fillPrivateCollectionsByFakeData() {
        objFld  = new SerializebleObject(DEFAULT_VALUE_FOR_BOXED_NUMBER_FIELDS);
        arrayFld = new int[DEFAULT_ARRAY_OR_COLLECTION_FIELDS_LEN];
        objArrayFld = new SerializebleObject[DEFAULT_ARRAY_OR_COLLECTION_FIELDS_LEN];

        for (int i = 1; i <= DEFAULT_ARRAY_OR_COLLECTION_FIELDS_LEN; i++) {
            listFld.add(i);
            mapFld.put("Key#" + i, i * DEFAULT_VALUE_FOR_NUMBER_FIELDS);
            objListFld.add(new SerializebleObject(i));
            objMapFld.put(i, new SerializebleObject(i));
            arrayFld[i - 1] = i;
            objArrayFld[i - 1] = new SerializebleObject(i);
            intSetFld.add(i);
            objSetFld.add(new SerializebleObject(i));

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializebleObject object = (SerializebleObject) o;

        if (intFld != object.intFld) return false;
        if (Double.compare(object.doubleFld, doubleFld) != 0) return false;
        if (boxedIntFld != null ? !boxedIntFld.equals(object.boxedIntFld) : object.boxedIntFld != null) return false;
        if (boxedDoubleFld != null ? !boxedDoubleFld.equals(object.boxedDoubleFld) : object.boxedDoubleFld != null)
            return false;
        if (stringFld != null ? !stringFld.equals(object.stringFld) : object.stringFld != null) return false;
        if (objFld != null ? !objFld.equals(object.objFld) : object.objFld != null) return false;
        if (!Arrays.equals(arrayFld, object.arrayFld)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(objArrayFld, object.objArrayFld)) return false;
        if (listFld != null ? !listFld.equals(object.listFld) : object.listFld != null) return false;
        if (mapFld != null ? !mapFld.equals(object.mapFld) : object.mapFld != null) return false;
        if (objListFld != null ? !objListFld.equals(object.objListFld) : object.objListFld != null) return false;
        return objMapFld != null ? objMapFld.equals(object.objMapFld) : object.objMapFld == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = intFld;
        result = 31 * result + (boxedIntFld != null ? boxedIntFld.hashCode() : 0);
        temp = Double.doubleToLongBits(doubleFld);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (boxedDoubleFld != null ? boxedDoubleFld.hashCode() : 0);
        result = 31 * result + (stringFld != null ? stringFld.hashCode() : 0);
        result = 31 * result + (objFld != null ? objFld.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(arrayFld);
        result = 31 * result + Arrays.hashCode(objArrayFld);
        result = 31 * result + (listFld != null ? listFld.hashCode() : 0);
        result = 31 * result + (mapFld != null ? mapFld.hashCode() : 0);
        result = 31 * result + (objListFld != null ? objListFld.hashCode() : 0);
        result = 31 * result + (objMapFld != null ? objMapFld.hashCode() : 0);
        return result;
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
}
