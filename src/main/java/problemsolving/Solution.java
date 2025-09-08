package problemsolving;

import java.io.IOException;
import java.util.List;

/*
 * m - 5
 * 1,2,3,4,5
 *
 * User_1 -> 1,2,3
 * User_3 -> 2, 4
 * User_2 -> 5
 *
 * join(List(1,2,3)) -> 1
 * join(List(1,2)) -> 2
 * join(List(4)) -> 3
 *
 * request(3, 2) -> List(1,2)
 * request(3, 5) -> List()
 *
 * leave(2)
 *
 * join(List(5)) -> 2
 * */
// 	Design a File Sharing System
interface DummyBittorrent {
    int join(List<Integer> ownedChunks);

    void leave(int userID);

    List<Integer> request(int userID, int chunkID);
}

public class Solution {
    public static void main(String[] args) throws IOException {

    }
}
