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
    private LinkedList<Integer> listFld2 = new LinkedList<>();
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
            listFld2.add(i);
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
                ", listFld=2" + listFld2 +
                ", mapFld=" + mapFld +
                ", objListFld=" + objListFld +
                ", objMapFld=" + objMapFld +
                ", intSetFld=" + intSetFld +
                ", objSetFld=" + objSetFld +
                '}';
    }

    public boolean simpleFieldsEquals(SerializebleObject o) {
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

    public boolean listsFieldsEquals(SerializebleObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (listFld != null ? !listFld.equals(o.listFld) : o.listFld != null) return false;
        if (listFld2 != null ? !listFld2.equals(o.listFld2) : o.listFld2 != null) return false;
        return objListFld != null ? objListFld.equals(o.objListFld) : o.objListFld == null;
    }

    public boolean setFieldsEquals(SerializebleObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (intSetFld != null ? !intSetFld.equals(o.intSetFld) : o.intSetFld != null) return false;
        return objSetFld != null ? objSetFld.equals(o.objSetFld) : o.objSetFld == null;
    }

    public boolean mapFieldsEquals(SerializebleObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (mapFld != null ? !mapFld.equals(o.mapFld) : o.mapFld != null) return false;
        return objMapFld != null ? objMapFld.equals(o.objMapFld) : o.objMapFld == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializebleObject that = (SerializebleObject) o;

        if (intFld != that.intFld) return false;
        if (Double.compare(that.doubleFld, doubleFld) != 0) return false;
        if (boxedIntFld != null ? !boxedIntFld.equals(that.boxedIntFld) : that.boxedIntFld != null) return false;
        if (boxedDoubleFld != null ? !boxedDoubleFld.equals(that.boxedDoubleFld) : that.boxedDoubleFld != null)
            return false;
        if (stringFld != null ? !stringFld.equals(that.stringFld) : that.stringFld != null) return false;
        if (objFld != null ? !objFld.equals(that.objFld) : that.objFld != null) return false;
        if (!Arrays.equals(arrayFld, that.arrayFld)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(objArrayFld, that.objArrayFld)) return false;
        if (listFld != null ? !listFld.equals(that.listFld) : that.listFld != null) return false;
        if (listFld2 != null ? !listFld2.equals(that.listFld2) : that.listFld2 != null) return false;
        if (objListFld != null ? !objListFld.equals(that.objListFld) : that.objListFld != null) return false;
        if (mapFld != null ? !mapFld.equals(that.mapFld) : that.mapFld != null) return false;
        if (objMapFld != null ? !objMapFld.equals(that.objMapFld) : that.objMapFld != null) return false;
        if (intSetFld != null ? !intSetFld.equals(that.intSetFld) : that.intSetFld != null) return false;
        return objSetFld != null ? objSetFld.equals(that.objSetFld) : that.objSetFld == null;
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
        result = 31 * result + (listFld2 != null ? listFld2.hashCode() : 0);
        result = 31 * result + (objListFld != null ? objListFld.hashCode() : 0);
        result = 31 * result + (mapFld != null ? mapFld.hashCode() : 0);
        result = 31 * result + (objMapFld != null ? objMapFld.hashCode() : 0);
        result = 31 * result + (intSetFld != null ? intSetFld.hashCode() : 0);
        result = 31 * result + (objSetFld != null ? objSetFld.hashCode() : 0);
        return result;
    }
}
