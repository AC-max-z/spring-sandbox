package generators;

import java.util.ArrayList;
import java.util.List;

public interface ObjectGenerator<T> {
    T generate();

    default List<T> generateMany(int howMuch) {
        var list = new ArrayList<T>(howMuch);
        for (int i = 0; i < howMuch; i++) {
            list.add(this.generate());
        }
        return list.stream().toList();
    }

}
