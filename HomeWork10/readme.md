Задание
----
1. Доделать задание 9.
2. Оформить решение в виде DBService (interface DBService, class DBServiceImpl, UsersDAO, UsersDataSet, Executor)
3. Не меняя интерфейс DBSerivice сделать DBServiceHibernateImpl на Hibernate.
4. Добавить в UsersDataSet 

class AddressDataSet{  
 private String street;  
 private int index;  
} (OneToOne)
  
и телефон  
  
class PhoneDataSet{  
 private int code;  
 private String number;  
} (OneToMany).  
  
Добавить соответствущие датасеты и DAO. 