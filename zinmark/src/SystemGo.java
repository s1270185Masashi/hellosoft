import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

class SystemGo {
    User [] user;                   //ユーザを管理
    Book_Recommendation book_rec;   //ターゲットユーザに対する推薦度を計算

    boolean scan_ctr;               //コマンド入力の際のwhileループを制御
    int target_user;                //ターゲットユーザを設定

    Myscan_test myscan;             //コマンド入力を管理    
    int N;                          //user の数
    int M;                          //本の数   
    int E;                          //評価の数
    int R;                          //user 関係の入力の数

    String bookscore_data_file;
    String friend_data_file;

    UnionFindTree_EX union_ex ;     //test test

    SystemGo(int target_u, String bookscore_data_file, String friend_data_file){
        this.bookscore_data_file = bookscore_data_file;
        this.friend_data_file = friend_data_file;
        user = new User[10000];
        scan_ctr = true;
        target_user = target_u;
        user[0] = new User();       //user[0]は存在しない
        this.SettingData();
        this.cal_Similar_score();
        this.ScanFriendInfo();
        this.set_users_friend();    //Union_EXにクラスを変更
        this.cal_target_rec_book_score();
        this.all_display(target_u);
    }

    //.txtファイルで入力を受け取り、データをセットする
    public void SettingData(){
        try{
            File file = new File(bookscore_data_file);
            Scanner scan = new Scanner(file);


            N = scan.nextInt();
            M = scan.nextInt();
            E = scan.nextInt();

            for(int i=1;i<=N;i++){
                user[i] = new User();
                user[i].set_UserName(i);
            }

            for(int i=1;i<=E;i++){
                int u = scan.nextInt();
                int b = scan.nextInt();
                double sc_score = scan.nextDouble();
                user[u].set_BookScore(b, sc_score);

            }
            scan.close();
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }

    }

    //類似度を計算する　
    //改良　ループを減らせるかも
    public void cal_Similar_score(){
        for(int i=1;i<=N;i++){
            for(int j=1;j<=N;j++){
                if(i!=j){
                    SimilarScore simi_score = new SimilarScore();
                    simi_score.set_users(user[i],user[j]);
                    user[i].set_similar_opponent_score(user[j],simi_score.get_similar_score());
                }
            }
        }
    }

    //推薦度を計算
    public void cal_target_rec_book_score(){
        book_rec = new Book_Recommendation();
        for(int i=1;i<=N;i++){
            book_rec.set_user(user[i]);
        }
        book_rec.set_number_of_books(M);
        book_rec.set_targetuser(user[target_user]);
        book_rec.cal_rec_score();
        user[target_user].set_rec_book_score(book_rec);
    }
   
    //画面に出力
    public void all_display(int targetUser){
        System.out.println();
        System.out.print("    Score");
        for(int i=1;i<=M;i++){
            System.out.print("  B"+(i)+"  ");
        }
        System.out.println();
        
        for(int i=1;i<=N;i++){
            user[i].disp_book_score(user[targetUser],M);
        }       
        user[targetUser].disp_rec_book_score_sort();
    }


    //command入力制御
    public void ScanMode(){
        myscan = new Myscan_test();
        while(this.scan_ctr){
            myscan.scan_command();
            if(myscan.getRecCall1()){
                this.ScanRec1();
            }
            if(myscan.getRecCall2()){
                this.ScanRec2();
            }
            if(myscan.getEvalCall()){
                this.ScanEval();
            }
            this.scan_ctr=myscan.stopCall();
        }

    }

    //rec userに対する制御
    public void ScanRec1(){
        int scanUser = myscan.getUser();
        if(scanUser>=1 && scanUser<=N){
            target_user = scanUser;
            cal_target_rec_book_score();
            user[target_user].disp_rec_book_score_sort();
        }
        else{
            System.out.println("cannot find User");
        }
    }

    //rec user booknum に対する制御
    public void ScanRec2(){
        int scanUser = myscan.getUser();        
        if(scanUser>=1 && scanUser<=N){
            target_user = scanUser;
            cal_target_rec_book_score();
            user[target_user].disp_rec_book_score(myscan.getRec_book_num());
        }
        else{
            System.out.println("cannot find User");
        }
    }

    //eval user booknum scoreに対する制御
    //更新作業も行う
    public void ScanEval(){
        int scanUser = myscan.getUser();
        int booknum = myscan.getRec_book_num();
        double score = myscan.getNew_score();
        if(scanUser>=1 && scanUser<=N){
            user[scanUser].set_BookScore(booknum,score);
            cal_Similar_score();
            cal_target_rec_book_score();
            user[scanUser].rec_book_score.remove(booknum);
            all_display(scanUser);
        }
        else{
            System.out.println("cannot find User");
        }
    }

    //友人設定　テスト
    public void test_friend(){
        user[target_user].set_User_friends(user[2],true);
        user[target_user].set_User_friends(user[4],true);
        user[target_user].set_User_friends(user[6],true);
        user[target_user].set_User_friends(user[7],true);
        user[target_user].set_User_friends(user[8],true);
    }


    //ユーザーの友人関係情報設定
    public void ScanFriendInfo(){
        try{
            File file2 = new File(friend_data_file);
            Scanner scan2 = new Scanner(file2);

            R = scan2.nextInt();

            //New friends Union
            union_ex = new UnionFindTree_EX();

            //New friends Union set users
            for(int i=1;i<=N;i++){
                union_ex.Make_Set(user[i]);
            }

            for(int i=0;i<R;i++){
                int u1 = scan2.nextInt();
                int u2 = scan2.nextInt();
                //New friedns Union
                union_ex.Union(user[u1], user[u2]);
            }

            scan2.close();
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }

    }

    //それぞれのユーザーに友人を登録
    public void set_users_friend(){
        for(int i=1;i<=N;i++){
            for(int j=1;j<=N;j++){
                if(i!=j){
                    if(union_ex.judge_friends(user[i], user[j])){
                        user[i].set_User_friends(user[j],true);
                        user[j].set_User_friends(user[i],true);
                    }
                }
            }
        }
    }

}
