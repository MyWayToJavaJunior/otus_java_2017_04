Задание
----
Создайте в базе таблицу:  
    +-------+--------------+------+-----+---------+----------------+  
| Field | Type         | Null | Key | Default | Extra          |  
+-------+--------------+------+-----+---------+----------------+  
| id    | bigint(20)   | NO   | PRI | NULL    | auto_increment |  
| name  | varchar(255) | YES  |     | NULL    |                |  
| age   | int(3)       | NO   |     | 0       |                |  
+-------+--------------+------+-----+---------+----------------+  
    
Создайте абстрактный класс DataSet, унаследуйте User от DataSet. Поместите long id в DataSet.  
Разметьте классы DataSet и User, аннотациями JPA так, чтобы они соответствовали таблице.  
Напишите Executor, который сохраняет объект класса User в базу и читает объект класса User из базы по id и классу.  
<T extends DataSet> void save(T user){…}  
<T extends DataSet> T load(long id, Class<T> clazz){...}  
