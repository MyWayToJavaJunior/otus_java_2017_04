�������
----
�������� � ���� �������:
+-------+--------------+------+-----+---------+----------------+
| Field | Type         | Null | Key | Default | Extra          |
+-------+--------------+------+-----+---------+----------------+
| id    | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| name  | varchar(255) | YES  |     | NULL    |                |
| age   | int(3)       | NO   |     | 0       |                |
+-------+--------------+------+-----+---------+----------------+
�������� ����������� ����� DataSet, ����������� User �� DataSet. ��������� long id � DataSet.
��������� ������ DataSet � User, ����������� JPA ���, ����� ��� ��������������� �������.
�������� Executor, ������� ��������� ������ ������ User � ���� � ������ ������ ������ User �� ���� �� id � ������.
<T extends DataSet> void save(T user){�}
<T extends DataSet> T load(long id, Class<T> clazz){...}