�������
----
1. �������� ������� 9.
2. �������� ������� � ���� DBService (interface DBService, class DBServiceImpl, UsersDAO, UsersDataSet, Executor)
3. �� ����� ��������� DBSerivice ������� DBServiceHibernateImpl �� Hibernate.
4. �������� � UsersDataSet 

class AddressDataSet{
	private String street;
	private int index;
} (OneToOne)

� �������

class PhoneDataSet{
	private int code;
	private String number;
}
(OneToMany).

�������� �������������� �������� � DAO. 