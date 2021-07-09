import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Book_Recommendation {

    private User target_user ;                     //ターゲットユーザー　推薦する対象
    private int number_of_books ;                  //本の数を設定
    ArrayList<User> users ;                        //全ユーザーを格納
    Map<Integer,Double> rec_book_score ;           //本の番号に対応する推薦度を格納
    Map<Integer,Double> all_user_similar_score ;   //推薦する本に対し、他のユーザーの類似度を合計する


    Book_Recommendation(){
        target_user = new User();
        number_of_books = 0;
        users = new ArrayList<User>();
        rec_book_score = new HashMap<>();
        all_user_similar_score = new HashMap<>();

    }

    //ユーザーをセット
    public void set_user(User U){
        users.add(U);
    }

    //ターゲットユーザーをセット
    public void set_targetuser(User u){
        target_user = u;
    }

    //本の本数をセット
    public void set_number_of_books(int books){
        this.number_of_books = books;
    }


    //　本の数でループする
    //　ターゲットユーザーが評価していない
    //　ターゲットユーザー意外でループ
    //　上記の条件に合ったとき、かつ他のユーザーが評価していた場合のみ計算
    //　友人の時だけ推薦度を計算
    public void cal_rec_score(){
        for(int b=1;b<=this.number_of_books;b++){
            if(!target_user.check_have_key(b)){
                for(User u_opponent : users){
                    if(target_user!=u_opponent){
                        // friend userの時だけ格納
                        if(target_user.check_User_friends(u_opponent)){
                            if(u_opponent.check_have_key(b)){
                                double rec_score = u_opponent.get_BookScore(b) * this.target_user.get_similar_opponent_score(u_opponent);
                                add_rec_book_score(b, rec_score);
                                add_all_user_similar_score(b, this.target_user.get_similar_opponent_score(u_opponent));
                            }
                        }
                    }
                }
            }
        }
    
    }

    //推薦度の集計
    public void add_rec_book_score(int booknum,double score){
        if(rec_book_score.containsKey(booknum)){
            double new_score = rec_book_score.get(booknum)+score;
            rec_book_score.replace(booknum, new_score);
        }
        else{
            rec_book_score.put(booknum, score);
        }
    }

    //他のユーザーの類似度の集計
    public void add_all_user_similar_score(int booknum,double similar_score){
        if(all_user_similar_score.containsKey(booknum)){
            double new_simiscore = all_user_similar_score.get(booknum) + similar_score;
            all_user_similar_score.replace(booknum, new_simiscore);
        }
        else{
            all_user_similar_score.put(booknum, similar_score);
        }
    }

    //ターゲットに対する、ある本の推薦度を返す
    public double get_rec_book_score_for_target(int booknum){
        if(this.rec_book_score.containsKey(booknum)){
            double pre_score = this.rec_book_score.get(booknum);
            double floor_score = pre_score;
            return floor_score/all_user_similar_score.get(booknum);

        }
        else{
            return 0;
        }
    }

}
