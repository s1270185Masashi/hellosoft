import java.util.Scanner;

class Myscan_test{
    private String command;
    private int user;
    private int rec_book_num;
    private double new_score;
    private boolean ctr = true;
    private boolean rec_ctr1 = false;
    private boolean rec_ctr2 = false; 
    private boolean eval_ctr = false;
    private int input_length;
    
    Myscan_test(){

    }

    //コマンドを受け取る
    public String getCommand(){
        return this.command;
    }

    //ユーザーを受け取る
    public int getUser(){
        return this.user;
    }

    //推薦度を表示する本の番号
    public int getRec_book_num(){
        return this.rec_book_num;
    }

    //新しい本の評価値を返す
    public double getNew_score(){
        return this.new_score;
    }

    //入力の空白区切りの文字列の数
    public int getInput_length(){
        return this.input_length;
    }

    //コマンドを受け取る
    //どんなコマンドを受けとったかboolean型とメソッドで管理
    //ある特定のコマンドが呼び出されたら以下のメソッドを呼び出す
    //recCall1 recCall2 evalCall など
    //不適切な型の入力の時はエラー処理をする
    public void scan_command(){

            System.out.println("please input");


            Scanner scanmode = new Scanner(System.in);
            String input = scanmode.nextLine();
            String[] input_split = input.split("\\s+");
            this.input_length = input_split.length;
            this.rec_ctr1 = false;
            this.rec_ctr2 = false;
            this.eval_ctr = false;

            
            
            if(input_length<1){
                System.out.println("can't find command");
            }

            if(input_length>0){
                this.command=input_split[0];

                if(this.command.equals("rec")){
                    
                    if(input_length==2){
                        try{
                            this.user = Integer.parseInt(input_split[1]);
                            this.recCall1();
                        }
                        catch(IllegalArgumentException e){
                            System.out.println("Illegal input");
                        }
                    }
                    else if(input_length==3){
                        try{
                            this.rec_book_num = Integer.parseInt(input_split[2]);
                            this.recCall2();
                        }
                        catch(IllegalArgumentException e){
                            System.out.println("Illegal input");
                        }
                    }
                    else{
                        System.out.println("too many arguments");
                    }
                }

                else if(this.command.equals("eval")){
                    
                    if(input_length==4){
                        try{
                            this.user = Integer.parseInt(input_split[1]);
                            this.rec_book_num = Integer.parseInt(input_split[2]);
                            this.new_score = Double.parseDouble(input_split[3]);
                            this.evalCall();
                        }
                        catch(IllegalArgumentException e){
                            System.out.println("Illegal input");
                        }
                    }
                    else if(input_split.length<4){
                        System.out.println("please more arguments");
                    }
                    else{
                        System.out.println("too many arguments");
                    }
                }
                else if(input_split[0].equals("exit")){
                    this.ctr=false;
                }
                else{
                    System.out.println("not find command");
                }

            }  
    }

    //exitコマンドが入力されたときループを終了する
    public boolean stopCall(){
        return this.ctr;
    }

    //rec [user]　を受け取ったらtrue
    public void recCall1(){
        this.rec_ctr1 = true;
    }

    //rec [user] [booknum] を受け取ったらtrue
    public void recCall2(){
        this.rec_ctr2 = true;
    }

    //eval [user] [booknum] [score] を受けったらtrue
    public void evalCall(){
        this.eval_ctr = true;
    }

    //recCall1が呼ばれているときはtrueを返す
    public boolean getRecCall1(){
        return this.rec_ctr1;
    }

    //recCall2が呼ばれているときはtrueを返す
    public boolean getRecCall2(){
        return this.rec_ctr2;
    }

    //evalCallが呼ばれているときはtrueを返す
    public boolean getEvalCall(){
        return this.eval_ctr;
    }

}
