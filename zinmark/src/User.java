import java.util.HashMap;
import java.util.Map.Entry;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;




class User {
    private int user_name;                                          //ユーザの名前を番号で管理　入力の値と一致している
    Map<Integer,Double> book_score = new HashMap<>();               //自分が評価した本と評価値を管理
    Map<User,Double> similar_opponent_score = new HashMap<>();      //他人とその人との類似度を管理
    Map<Integer,Double> rec_book_score = new HashMap<>();           //自分に対し、推薦された本の番号と、その推薦度を管理

    //friend
    Map<User,Boolean> friends_map = new HashMap<>();                //友人かどうかを格納
    
    private int myrank = 0;                                         //自分のランク　子をいくつ持つか　友人関係で使用
    
    User(){
    }

    //userの名前を記録　入力通りの数字を使用
    public void set_UserName(int user_name){
        this.user_name = user_name;
    }

    //userの名前を返す
    public int get_UserName(){
        return this.user_name;
    }


    //自分が評価した本とその評価値を記録
    public void set_BookScore(int book_number,double score){
        if(score>0.0){
            this.book_score.put(book_number, score);
        }
    }

    //本の番号を受け取り、その本の評価を返す
    public double get_BookScore(int booknum){
        if(this.check_have_key(booknum)){
            return this.book_score.get(booknum);
        }
        else{
            return -2.0;
        }
    }

    //自らが評価した本とその評価値を出力する
    //friend も出力ここで
    public void disp_book_score(User target,int how_many_book){
        System.out.print("U" + (this.get_UserName())+"  ");
        if(!this.equals(target)){
            System.out.print(String.format("%.2f",this.get_similar_opponent_score(target)));
        }
        else{
            System.out.print("----");
        }
        for(int j=1;j<=how_many_book;j++){
            if(this.check_have_key(j)){
                System.out.print("  "+this.get_BookScore(j)+" ");
            }
            else{
                System.out.print("  --- ");
            }
        }
        this.disp_friend(target);
        //test 
        System.out.println();
    }


    //引数として受け取った番号の本を評価しているかtrue falseで返す
    public boolean check_have_key(int booknum){
        if(this.book_score.containsKey(booknum)){
            return true;
        }
        else{
            return false;
        }
    }



    //他人との類似度を記録する
    public void set_similar_opponent_score(User user,double score){
        similar_opponent_score.put(user, score);
    }
    
    //あるuserとの類似度を返す
    public double get_similar_opponent_score(User user){
        return similar_opponent_score.get(user);
    }

    //小数第2位までにしたもの　未使用
    public double get_similar_opponent_score_floor(User user){
        double pre_score = similar_opponent_score.get(user);
        double floor_score = Math.floor(pre_score*100)/100;
        return floor_score;

    }

    //類似度をソートしたものを出力する
    public void disp_similar_opponet_score_sort(){

        List<Entry<User, Double>> similar_list = new ArrayList<Entry<User, Double>>(similar_opponent_score.entrySet());

        Collections.sort(similar_list, new Comparator<Entry<User, Double>>(){
            
            public int compare(Entry<User, Double> o1, Entry<User, Double> o2){
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        System.out.println("降順でソート");

        for(Entry<User, Double> entry : similar_list){
            System.out.println("User : "+entry.getKey().user_name + "   " +"Similar score : "+ entry.getValue());
        }

    }







    //自らに対する、本の推薦度を記録
    //オブジェクトごと受け取る
    public void set_rec_book_score(Book_Recommendation book_rec_for_me){
        for(Integer booknum : book_rec_for_me.rec_book_score.keySet()){
            this.rec_book_score.put(booknum, book_rec_for_me.get_rec_book_score_for_target(booknum));
        }
    }

    //本の番号を受け取り、自らに対するその本の推薦度を返す
    public double get_rec_book_score(int book_number){
        if(this.rec_book_score.containsKey(book_number)){
            return rec_book_score.get(book_number);
        }
        else{
            return 0.0;
        }
    }

    //自らに推薦されている本を出力する
    public void disp_rec_book_score(int booknum){
        if(this.rec_book_score.containsKey(booknum)){
            System.out.println("Book Number : "+booknum + "   " +"Recommendation : "+ this.get_rec_book_score(booknum));
        }
        else{
            System.out.println("there is not recomendation book.");
        }
    }

    //自らに推薦されている本をソートして出力する
    public void disp_rec_book_score_sort(){
        List<Entry<Integer, Double>> rec_book_list = new ArrayList<Entry<Integer,Double>>(rec_book_score.entrySet());

        Collections.sort(rec_book_list, new Comparator<Entry<Integer ,Double>>() {
            //compareを使用して値を比較する
            public int compare(Entry<Integer, Double> s1, Entry<Integer, Double> s2)
            {
                //降順
                return s2.getValue().compareTo(s1.getValue());
            }
        });


        // 7. ループで要素順に値を取得する
        if(!rec_book_list.isEmpty()){
            System.out.println();        
            for(Entry<Integer, Double> entry : rec_book_list) {
                System.out.println("Book Number : "+entry.getKey() + "   " +"Recommendation : "+ entry.getValue());
            }
        }
        else{
            System.out.println("there is not recommendation book.");
        }
    }

    //以下改良部分　課題5を利用するため　以下のメソッドを追加した

    //自分との友人を設定
    public void set_User_friends(User u, boolean friend_check){
        this.friends_map.put(u, friend_check);
    }

    //友人かどうか判定
    public boolean check_User_friends(User u){
        if(this.friends_map.containsKey(u)){
            return friends_map.get(u);
        }
        else{
            return false;
        }
    }

    //友人かどうか出力
    public void disp_friend(User target){
        if(this.equals(target)){
            System.out.print("me");
        }
        else if(this.friends_map.containsKey(target)){
            System.out.print("friend");
        }
        else{
            System.out.print("not friend");
        }
    }

    //user rank set and get , according to Union_EX
    //user のランクをセット
    public void set_rank(int rank){
        this.myrank = rank;
    }

    //user のランクを返す
    public int get_rank(){
        return this.myrank;
    }

  

}