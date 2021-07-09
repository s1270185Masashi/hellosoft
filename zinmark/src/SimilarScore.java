import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class SimilarScore {
    User u1;
    User u2;

    /*計算イメージ

        dist1 =  (u1_book1 - u2_book1)^2 
        dist2 =  (u1_book2 - u2_book2)^2
          :
          :
        distX =  (u1_bookX - u2_bookX)^2


        all_dist = dist1+dist2+...distX

                                    1
        calculated_score = --------------------
                            { √(all_dist) + 1 }

        
    */
    private double all_dist = 0.0;          //ユーザー間の距離の合計
    private double dist = 0.0;              //本に対するユーザー間距離
    private double calculated_score = 0.0;  //類似度計算結果
    private boolean cal_check =false;       //どちらのユーザーも評価してない場合ユーザー間距離が0なので類似度が１になってしまう
                                            //上記のような場合を防ぐため、計算をしたかチェック


    SimilarScore(){
        u1 = new User();
        u2 = new User();
    }
    
    //計算するユーザーをセット
    public void set_users(User u1,User u2){
        this.u1 = u1;
        this.u2 = u2;
        this.calculate_dist();
        this.calculate_score();        
    }
    
    //計算結果を返す
    public double get_similar_score(){
        return this.calculated_score;
    }

    //ある本に対して、お互いに評価しているときのみ、ユーザー間を計算する
    public void calculate_dist(){
            for(Integer key : u1.book_score.keySet()){
                if(u2.check_have_key(key)){
                    this.dist = (u1.get_BookScore(key) - u2.get_BookScore(key));
                    this.add_dist(this.dist*this.dist);
                    //計算したが判定
                    this.cal_check = true;
                }
            }
    }

    //ユーザー間の合計
    public void add_dist(double dist){
        this.all_dist =  this.all_dist + dist;
    }

    //類似度を計算する
    public void calculate_score(){
        if(this.cal_check){
            this.calculated_score = ( 1.0 / ( 1.0 + Math.sqrt(this.all_dist)));
        }
        else{
            this.calculated_score = 0.0;
        }
    }

    //計算しているユーザーを呼び出す　テスト時のみ使用
    public String get_users_name(){
        return "User1:"+u1.get_UserName()+" User2:"+u2.get_UserName();
    }

    //今計算しているユーザーが正しいか確認　テスト時のみ使用
    public boolean check_have_user(User u1,User u2){
        if(this.u1 == u1 && this.u2 == u2){
            return true;
        }
        else{
            return false;
        }
    }

}



