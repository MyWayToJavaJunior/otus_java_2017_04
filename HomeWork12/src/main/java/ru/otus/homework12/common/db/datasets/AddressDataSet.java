package ru.otus.homework12.common.db.datasets;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class AddressDataSet extends DataSet {
    @Column
    private String street;

    @Column(name = "mail_index")
    private int index;

    public AddressDataSet() {
    }

    public AddressDataSet(String street, int index) {
        this.street = street;
        this.index = index;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id='" + id + '\'' +
                ", street='" + street + '\'' +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressDataSet that = (AddressDataSet) o;

        if (id != that.id) return false;
        if (index != that.index) return false;
        return street != null ? street.equals(that.street) : that.street == null;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + index;
        return result;
    }
}
