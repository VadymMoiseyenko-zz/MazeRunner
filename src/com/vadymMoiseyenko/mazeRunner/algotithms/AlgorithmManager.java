package com.vadymMoiseyenko.mazeRunner.algotithms;

import java.util.List;

public interface AlgorithmManager {
    List<String> getResolvePath(char[][] maze);

    void clearResolve();
}
