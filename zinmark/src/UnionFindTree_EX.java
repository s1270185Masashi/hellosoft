import java.util.HashMap;
import java.util.Map;

class UnionFindTree_EX {
    Map<User, User> friends_root_list = new HashMap<User, User>();      // Map<me , parent>で友人関係を保持する　

    UnionFindTree_EX() {

    }

    //user をセットする
    public void Make_Set(User user) {
        user.set_rank(0);
        friends_root_list.put(user, user);
    }

    //友人Treeを構築する　親を探し、ランクが高い親を新しい親にする
    public void Union(User user_x, User user_y) {
        User user_x_root = find(user_x);
        User user_y_root = find(user_y);

        if (user_x_root.get_rank() > user_y_root.get_rank()) {
            friends_root_list.replace(user_y_root, user_x_root);
            user_x_root.set_rank(user_x_root.get_rank() + 1);
        } else if (user_x_root.get_rank() < user_y_root.get_rank()) {
            friends_root_list.replace(user_x_root, user_y_root);
            user_y_root.set_rank(user_y_root.get_rank() + 1);
        } else if (!user_x_root.equals(user_y_root)) {
            friends_root_list.replace(user_y_root, user_x_root);
            user_x_root.set_rank(user_x_root.get_rank() + 1);
        }
    }

    //親を探す
    public User find(User user) {
        if (user != friends_root_list.get(user)) {
            user = find(friends_root_list.get(user));
        }
        return user;
    }


    //友人かどうかの判定
    public boolean judge_friends(User u1, User u2){
        if(find(u1).equals(find(u2))){
            return true;
        }
        else{
            return false;
        }
    }
}
