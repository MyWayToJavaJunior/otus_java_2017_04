�������
----
�������� ���� ���������� ArrayList �� ������ �������.
class MyArrayList<T> implements List<T>{...}

���������, ��� �� ��� �������� ������
� addAll(Collection<? super T> c, T... elements)
� static <T> void copy(List<? super T> dest, List<? extends T> src)
� static <T> void sort(List<T> list, Comparator<? super T> c)
�� java.util.Collections