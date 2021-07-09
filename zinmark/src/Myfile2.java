import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Comparator;


class Myfile2 implements Comparable<Myfile2>{
    private double score;
    private int word_count;

    private double result_score;
    private double word_score;

    private double all_score;
    
    File file;
    ArrayList<String> link_list = new ArrayList<>();
    Map<String,Integer> link_to_me = new HashMap<>();
    Set<String> word_list = new HashSet<>();

    Myfile2(File file){
        this.file = file;
        this.score = 1.0;
        this.result_score = 0.0;
        this.word_score = 0.0;
        this.word_count=0;
        fileread3();
        read_word();
    }    

    public void fileread(){
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] text_split = text.split("\\s+");
                for(int i=0;i<text_split.length;i++){
                    if(text_split[i].contains(".html")){
                        link_list.add(text_split[i]);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("error");
        }

    }

    public void fileread2(){
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            String text;
            while ((text = br.readLine()) != null) {
                if(text.matches("[a-zA-Z0-9].html")){
                    System.out.println(text);
                }
            }
        }
        catch (Exception e) {
            System.out.println("error");
        }       
    }

    public void fileread3() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            String text;
            while ((text = br.readLine()) != null) {
                //Pattern htmlPattern = Pattern.compile("[\\w\\.\\-:\\#\\?\\=\\&\\;\\%\\~\\+]+html");
                Pattern htmlPattern2 = Pattern.compile("[-_.!~*\\'()a-zA-Z0-9;\\?:\\@&=+\\$,%#]+html");
                Matcher matcher = htmlPattern2.matcher(text);
                while(matcher.find()){
                    link_list.add(matcher.group());
                }
            }
        }
        catch (Exception e) {
            System.out.println("error");
        }
    }

    public int numberOfLink(){
        return link_list.size();
    }

    public double get_score(){
        return this.score;
    }

    public void set_score(double new_score){
        this.score = new_score;
    }

    public void disp(){
        for(String s : link_list){
            System.out.println(s);
        }
    }

    public String get_Name(){
        return this.file.getName();
    }

    public void set_link_to_me(String s, int booknum){
        link_to_me.put(s, booknum);
    }

    public void read_word(){
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] text_split = text.split("\\s+");
                for(int i=0;i<text_split.length;i++){
                    word_list.add(text_split[i]);
                }
            }
        }
        catch (Exception e) {
            System.out.println("error");
        }
    }

    public boolean has_word(String s){
        if(word_list.contains(s)){
            return true;
        }
        else{
            return false;
        }
    }


    //score 
    public void set_scores(double score, double word_score){
        this.result_score = score;
        this.word_score = word_score;
        this.all_score = this.result_score + this.word_score;
    }

    public double get_result_score(){
        return this.result_score;
    }

    public double get_word_score(){
        return this.word_score;
    }

    public double get_all_score(){
        return this.all_score;
    }

    @Override
    public int compareTo(Myfile2 mf){
        if(this.all_score > mf.get_all_score()){
            return -1;
        }
        else if(this.all_score < mf.get_all_score()){
            return 1;
        }
        else{
            return 0;
        }
    }



}
