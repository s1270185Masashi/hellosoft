import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class Book_only_Recommend {
    private int K=20;
    private double max_book_score;
    private double max_word_score;
    private Map<Integer,MyBook> books;

    private Map<MyBook,Double> pre_book_scores;
    private Map<MyBook,Double> new_book_scores;
    private Map<MyBook,Double> word_scores;

    private ArrayList<String> serch_word_list;

    Book_only_Recommend(){
        this.books = new HashMap<Integer,MyBook>();
        this.pre_book_scores =new HashMap<MyBook,Double>();
        this.new_book_scores = new HashMap<MyBook,Double>();
        this.word_scores = new HashMap<MyBook,Double>();
        this.serch_word_list = new ArrayList<String>();
    }


    public void set_book_info(int id, String book_name,String my_page){
        if(!books.containsKey(id)){
            MyBook book = new MyBook(id, book_name, my_page);
            books.put(id, book);
            pre_book_scores.put(book, 0.0);
            new_book_scores.put(book, 0.0);
            word_scores.put(book, 0.0);
        }
    }

    public void set_reference_book_from_db(int id, int reference_book_id){
        books.get(id).set_reference_book(books.get(reference_book_id));
    }

    public void set_book_has_word(int id, String word){
        books.get(id).set_word(word);
    }

    public void serch_reference_to_me(){
        for(MyBook me : books.values()){
            for(MyBook other : books.values()){
                if(!me.equals(other)){
                    for(MyBook other_refere_book : other.get_reference_book()){
                        if(other_refere_book.equals(me)){
                            me.set_referenced_to_me(other);
                        }
                    }
                }
            }
        }
    }


    public void set_pre_score(){
        for(MyBook book : books.values()){
            pre_book_scores.replace(book, book.get_book_score());
        }
    }

    public void cal_new_score(){
        set_pre_score();

        for(MyBook book : books.values()){
            double sigma = 0.0;
            double ans = 0.0;
            for(MyBook other : book.get_refernced_to_me()){
                sigma = sigma + (pre_book_scores.get(other) / other.get_refernced_to_me().size());
            }
            ans = 0.15 + 0.85 * sigma;
            new_book_scores.replace(book, ans);
        }

        set_new_score();
    
    }

    public void set_new_score(){
        max_book_score = 0.0;
        for(MyBook book : books.values()){
            book.set_book_score(new_book_scores.get(book));
            if(new_book_scores.get(book)>=max_book_score){
                max_book_score = new_book_scores.get(book);
            }
        }
    }
  
    public void cal_loop(){
        for(int i=0;i<K;i++){
            cal_new_score();;
        }
    }




    public void set_serch_word(String word){
        String[] serch_words = word.split("\\s+");
        for(String s : serch_words){
            serch_word_list.add(s);
        }
        
    }

    public void reset_serch_word(){
        serch_word_list.clear();
    }

    public void cal_word_score(){
        max_word_score = 0.0;
        for(MyBook book : books.values()){
            int has_book_count=0;
            for(String word : serch_word_list){
                if(book.check_has_word(word)){
                    has_book_count++;
                }
            }
            word_scores.replace(book, 1.0*has_book_count);
            if(1.0*has_book_count>max_word_score){
                max_word_score = 1.0*has_book_count;
            }
        }
    }

    
    public void set_book_word_score_0_1(){
        for(MyBook book : books.values()){
            double book_score = book.get_book_score() / max_book_score;
            book.set_book_score_0_1(book_score);
            if(max_word_score>=1.0){
                double word_score = book.get_word_score() / max_word_score;
                book.set_word_score_0_1(word_score);
            }
            else{
                max_word_score = 1.0;
                double word_score = book.get_word_score() / max_word_score;
                book.set_word_score_0_1(word_score);
            }
        }
    }

    public void display(){
        System.out.println("word\tpage\tscore\t");
        System.out.format("%10s", "word score");
        System.out.format("%10s", "book score");
        System.out.format("%10s", "add  score");

        boolean ctr = true;
                
        List<Entry<Integer, MyBook>> book_list = new ArrayList<Entry<Integer,MyBook>>(books.entrySet());

        Collections.sort(book_list, new Comparator<Entry<Integer,MyBook>>(){
            public int compare(Entry<Integer,MyBook> book1, Entry<Integer,MyBook> book2){
                //return book2.getValue().compareTo(book1.getValue());
                if(book1.getValue().get_all_score() > book2.getValue().get_all_score()){
                    return -1;
                }
                else if(book1.getValue().get_all_score() < book2.getValue().get_all_score()){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });


        for(Entry<Integer,MyBook> book : book_list){
            System.out.format("%9s", book.getValue().get_book_score_0_1());
            System.out.print(":");
            System.out.format("%9s", book.getValue().get_word_score_0_1());
            System.out.print(":");
            System.out.format("%9s", book.getValue().get_all_score());
            System.out.print(":");
            System.out.println();
        }


    }



}
