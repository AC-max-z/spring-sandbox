package generators;

import java.util.ArrayList;
import java.util.List;

public interface ObjectGenerator<T> {
    T generate();

    default List<T> generateMany(int howMany) {
        if (howMany <= 0) {
           return List.of();
        }
        var list = new ArrayList<T>(howMany);
        for (int i = 0; i < howMany; i++) {
            list.add(this.generate());
        }
        return list.stream().toList();
    }

}
