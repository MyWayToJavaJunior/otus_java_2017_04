package ru.otus.homework09.orm;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UsersDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id = null;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "name")
    private String name;

    public UsersDataSet() {
    }

    public UsersDataSet(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public UsersDataSet(Long id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UsersDataSet{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersDataSet set = (UsersDataSet) o;

        if (age != set.age) return false;
        if (id != null ? !id.equals(set.id) : set.id != null) return false;
        return name != null ? name.equals(set.name) : set.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + age;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
