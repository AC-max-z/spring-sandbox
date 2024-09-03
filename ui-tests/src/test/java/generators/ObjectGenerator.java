package generators;

import java.util.ArrayList;
import java.util.List;

public interface ObjectGenerator<T> {
    T generate();

    default List<T> generateList(int size) {
        var list = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            list.add(this.generate());
        }
        return list.stream().toList();
    }

}
