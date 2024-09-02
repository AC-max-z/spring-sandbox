package generators;

import java.util.List;

public interface ObjectGenerator<T> {
    T generate();
    List<T> generateList(int size);
}
