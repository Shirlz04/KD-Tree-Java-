public class Spell {
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] lines = in.readAllLines();
        in.close();

        // Create an ArrayST<String, String> object called st. 
        ArrayST<String, String> st = new ArrayST<String, String>();

        // For each line in lines, split it into two tokens using
        // "," as delimiter; insert into st the key-value pair
        // (token 1, token 2).
        for (String line : lines) {
            String [] hold = line.split(",");
            st.put(hold[0], hold[1]);
            //StdOut.println(hold[0]);
            //StdOut.println(hold[1]);
        }
        //StdOut.println(st.size());
            
        // Read from standard input one line at a time; increment
        // a line number counter; split the line into words using
        // "\\b" as the delimiter; for each word in words, if it
        // exists in st, write the (misspelled) word, its line number, and
        // corresponding value (correct spelling) from st.
        int i = 0; 
        while (!StdIn.isEmpty()) {
            i++;
            String str = StdIn.readLine();
            String [] words = str.split("\\b");
            for (String word : words) {
                //StdOut.println(word);
                if (st.contains(word)) {
                    //StdOut.println(1);
                    StdOut.println(word+":" + i+" -> "+st.get(word));
                }
            }
        }
    }
}
