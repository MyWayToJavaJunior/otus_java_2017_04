package ru.otus.homework13.common.db.datasets;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class PhoneDataSet extends DataSet {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @Column(name = "phone_code")
    private int code;

    @Column(name = "phone_number")
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(int code, String number) {
        this.code = code;
        this.number = number;
    }

    public PhoneDataSet(UserDataSet user, int code, String number) {
        this.user = user;
        this.code = code;
        this.number = number;
    }


    public UserDataSet getUser() {
        return user;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id='" + id + '\'' +
                ", code=" + code +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneDataSet that = (PhoneDataSet) o;

        if (id != that.id) return false;
        if (code != that.code) return false;
        return number != null ? number.equals(that.number) : that.number == null;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
