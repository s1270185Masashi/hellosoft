import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.lang.Iterable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.List;


class hSystemGo2 {
    File dir;
    private int K=0;
    private double max_score;
    private double max_word;
    ArrayList<File> files;
    Myfile2[] myfile;
    Map<String,Double> pre_score = new HashMap<>();
    Map<String,Double> new_score = new HashMap<>();
    Map<String,Double> word_score = new HashMap<>();
    Map<String,Double> all_score = new HashMap<>();
    //環境によって変更する
    String dirname = new String("/home/gigi/link/soft/pro2_8/source");

    //String query
    
    Set<String> query = new HashSet<>();

    hSystemGo2(){
        
        files = new ArrayList<File>();
        
        
        K=20;
        loadDir();
        set_file();
        setup_map();
        search_link();
        cal_loop();
        scan_query();
        cal_word_score();
        set_result();
    }


    // file setting start
    public void loadDir(){
        try{
            dir = new File(dirname);
            File[] fFile = dir.listFiles();
            for(File f : fFile){
                if(f.getName().contains(".html")){
                    files.add(f);
                }
            }

        }
        catch(NullPointerException e){
            System.out.println("file not founnd");
        }
    }

    public void set_file(){
        myfile = new Myfile2[files.size()];
        for(int i=0;i<files.size();i++){
            myfile[i] = new Myfile2(this.files.get(i));
        }
    }
    // file setting end



    //cal score start
    public void setup_map(){
        for(int i=0;i<files.size();i++){
            this.pre_score.put(myfile[i].get_Name(),0.0);
            this.new_score.put(myfile[i].get_Name(),0.0);
            this.word_score.put(myfile[i].get_Name(), 0.0);
            this.all_score.put(myfile[i].get_Name(),0.0);
        }
    }

    public void set_pre_score(){
        for(int i=0;i<files.size();i++){ 
            this.pre_score.replace(myfile[i].get_Name(),myfile[i].get_score());
        }
    }
    
    public void search_link(){
        for(int i=0;i<files.size();i++){
            for(int j=0;j<files.size();j++){
                if(i!=j){
                    for(String link : myfile[j].link_list){
                        if(myfile[i].get_Name().equals(link)){
                            myfile[i].set_link_to_me(myfile[j].get_Name(),myfile[j].numberOfLink());
                        }
                    }
                }
            }
        }
    }

    public void cal_new_score(){
        set_pre_score();

        for(int i=0;i<files.size();i++){
        double sigma = 0.0;
        double ans  = 0.0;    
            for(Map.Entry<String,Integer> link : myfile[i].link_to_me.entrySet()){
                sigma = sigma + (pre_score.get(link.getKey()) / link.getValue());
            }
            ans = 0.15 + 0.85 * sigma;
            this.new_score.replace(myfile[i].get_Name(), ans);
        }

        set_new_score();
    }

    public void set_new_score(){
        max_score = 0.0;
        for(int i=0;i<files.size();i++){
            myfile[i].set_score(new_score.get(myfile[i].get_Name()));
            if(new_score.get(myfile[i].get_Name())>=max_score){
                max_score = new_score.get(myfile[i].get_Name());
            }
        }
    }

    public void cal_loop(){
        for(int i= 0;i<K;i++){
            cal_new_score();
        }
    }
    //cal score end




    //scan query start 
    public void scan_query(){
        
        System.out.println("please input");
        Scanner scanmode = new Scanner(System.in);
        String input = scanmode.nextLine();
        String[] input_query = input.split("\\s+");
        for(int i=0;i<input_query.length;i++){
            this.query.add(input_query[i]);
        }
        scanmode.close();
        
    }

    public void cal_word_score(){
        max_word = 0.0;
        for(int i=0;i<files.size();i++){
            int count=0;
            for(String q : query){
                if(myfile[i].has_word(q)){
                    count++;
                }
            }
            word_score.replace(myfile[i].get_Name(), 1.0*count);
            if(1.0*count>max_word){
                this.max_word = 1.0*count;
            }
        }
    }

    public void set_result(){
        for(int i=0;i<files.size();i++){
            double sc = new_score.get(myfile[i].get_Name()) / max_score;
            if(max_word<1.0){
                max_word=1.0;
                double ws = word_score.get(myfile[i].get_Name()) / max_word;
                myfile[i].set_scores(sc,ws);
            }
            else{
                double ws = word_score.get(myfile[i].get_Name()) / max_word;
                myfile[i].set_scores(sc,ws);
            }
            
            
        }
    }
    //scan query end
    
    // nomal display start
    public void display(){
        Arrays.sort(myfile);
        System.out.println("word\tpage\tscore\t");
        for(Myfile2 m : myfile){
            System.out.println( String.format("%.2f",word_score.get(m.get_Name())/max_word) + " : " + String.format("%.2f",new_score.get(m.get_Name())/max_score) + " : " + String.format("%.2f",m.get_all_score()) + " : " +m.get_Name());
        }
    } 
    // nomal display end

    //test display
    public void disp_link_to_me(){
        for(int i=0;i<files.size();i++){
            System.out.println(myfile[i].get_Name());

            for(Map.Entry<String,Integer> link : myfile[i].link_to_me.entrySet()){
                System.out.println(link.getKey() + " " + link.getValue());
                
            }
            System.out.println();
        }
    }

    public void disp_score(){
        for(int i=0;i<files.size();i++){
            System.out.println(myfile[i].get_Name() + " : " + myfile[i].get_score());
        }
    }

    
    public void sort_score(){
        boolean ctr = true;
        double format = 0.0;
        List<Entry<String, Double>> score_list = new ArrayList<Entry<String, Double>>(new_score.entrySet());

        Collections.sort(score_list, new Comparator<Entry<String, Double>>(){
            
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2){
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        System.out.println("降順でソート");

        for(Entry<String, Double> entry : score_list){
            //if(ctr){format = entry.getValue();}
            System.out.println("Page : "+entry.getKey() + "\t " +"Important score : "+ String.format("%.2f",entry.getValue()/this.max_score));

            //ctr = false;
        }

    }

    public void dip_html(){
        for(int i=0;i<files.size();i++){
            System.out.println(myfile[i].file.getName());
            myfile[i].disp();
            System.out.println();
        }
    }
    
    public void diplayDirFile(){
        for(File f : files){
            System.out.println("This is File. File name is " + f.getName());
        }
    }

    public void disp_test(){
        for(int i=0;i<files.size();i++){
            System.out.println(myfile[i].get_Name() + "\t: " + String.format("%.2f",myfile[i].get_all_score()));
        }
        Arrays.sort(myfile);
        System.out.println("\nsorted\n");
        for(Myfile2 m : myfile){
            System.out.println(m.get_Name() + "\t: " + String.format("%.2f",m.get_all_score()));
        }

    }


}
