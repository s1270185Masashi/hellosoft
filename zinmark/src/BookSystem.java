import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

public class BookSystem {
    public static void main(String[] args) {

        int TargetUser = 1;
        String bookscore_data = "/home/gigi/link/soft/upgrade_project/zinmark/data/sample3.txt";
        String friend_data = "/home/gigi/link/soft/upgrade_project/zinmark/data/friends.txt";

        SystemGo core = new SystemGo(TargetUser,bookscore_data,friend_data);

        
    }


}
