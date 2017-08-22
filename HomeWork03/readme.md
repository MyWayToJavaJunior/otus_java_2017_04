Задание
----
Написать свою реализацию ArrayList на основе массива.
class MyArrayList<T> implements List<T>{...}

Проверить, что на ней работают методы
• addAll(Collection<? super T> c, T... elements)
• static <T> void copy(List<? super T> dest, List<? extends T> src)
• static <T> void sort(List<T> list, Comparator<? super T> c)
из java.util.Collections