import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Program to take in a text file containing terms and their definitions and
 * create a glossary index page for them.
 *
 * @author Allen Zhang
 *
 */
public final class Glossary {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary() {
    }

    /**
     * Compare {@code String}s in lexicographic order.(for test purpose made it
     * public).
     */
    public static final class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Outputs a HTML page for a term. These are the expected elements generated
     * by this method:
     *
     * <html> <head> <title>the term</title> </head> <body>
     * <h2><b><i><font color="red">the term </font></i></b></h2>
     * <blockquote>definition of the term(hyperlink any word in the definition
     * that's also in this glossary)</blockquote>
     * <hr>
     * (a horizontal line to divide the page) <main>
     * <p>
     * return to index page(with link)
     * </p>
     * </main> </body></html>
     *
     * @param glossary
     *            the term to definition
     * @param term
     *            the term you want to create page for
     * @param out
     *            the output stream
     * @param linkMap
     *            the term to HTML hyperlink tag to its detailed page,also
     *            contain the link to indexPage, key is #indexPage.
     * @update out
     * @requires out is open, {@code term} is a key in {@code glossary},
     *           {@code glossary} contains at least one <term, the term 's
     *           definition> pair(both terms and definition should not be empty
     *           string), {@code linkMap} contains at least one <term , html
     *           hyperlink to term's detailed page, link text is the term>
     *           pair(both terms and html hyperlink should not be empty string)
     *           excluding the special pair with key "#indexPage"
     * @ensures out.content = #out.content * [the HTML page for a term]
     */
    public static void outputTermPage(Map<String, String> glossary, String term,
            SimpleWriter out, Map<String, String> linkMap) {
        assert glossary != null : "Violation of: glossary is not null";
        assert linkMap != null : "Violation of: linkMap is not null";
        assert term != null : "Violation of: term is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        //get necessary content for this page
        String definitionWithLinks = linkingWords(glossary.value(term), glossary,
                linkMap);

        //write the HTML code
        out.println("<html>");
        out.println("   <head>");
        out.println("     <title>" + term + "</title>");
        out.println("   </head>");
        out.println("   <body>");
        out.println("     <h2>");
        out.println("       <b>");
        out.println("        <i>");
        out.println("         <font color=\"red\">" + term + "</font>");
        out.println("        </i>");
        out.println("       </b>");
        out.println("     </h2>");
        out.println("     <blockquote>" + definitionWithLinks + "</blockquote>");
        out.println("     <hr>");
        out.println("     <main>");
        out.println("       <p>Return to " + linkMap.value("#indexPage") + ".</p>");
        out.println("     </main>");
        out.println("   </body>");
        out.print("</html>");

    }

    /**
     * Outputs a HTML page for a term. These are the expected elements generated
     * by this method:
     *
     * <html> <head> <title>Glossary</title> </head> <body>
     * <h2>Glossary</h2>
     * <hr>
     * (a horizontal line to divide the page) <main>
     * <h3>Index</h3>
     * <ul>
     * ....(content of this list, terms in glossary with links to their detailed
     * page, in alphabetical order)
     * </ul>
     * </main> </body></html>
     *
     * @param glossary
     *            the term to definition
     * @param out
     *            the output stream
     * @param linkMap
     *            the term to HTML hyperlink tag to its detailed page
     * @update out
     * @requires out is open,{@code glossary} contains at least one <term, the
     *           term 's definition> pair(both terms and definition should not
     *           be empty string), {@code linkMap} contains at least one <term ,
     *           html hyperlink to term's detailed page, link text is the term>
     *           pair(both terms and html hyperlink should not be empty string)
     *           excluding the special pair with key "#indexPage"
     * @ensures out.content = #out.content * [the HTML page for index page]
     */
    public static void outputIndexPage(Map<String, String> glossary, SimpleWriter out,
            Map<String, String> linkMap) {
        assert glossary != null : "Violation of: glossary is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        //write the HTML code
        out.println("<html>");
        out.println("   <head>");
        out.println("     <title>Glossary</title>");
        out.println("   </head>");
        out.println("   <body>");
        out.println("     <h2>Glossary</h2>");
        out.println("     <hr>");
        out.println("     <main>");
        out.println("       <h3>Index</h3>");
        out.println("       <ul>");

        //put keys in glossary into a queue and sort them in alphabetical order
        Queue<String> queue = new Queue1L<>();
        for (Map.Pair<String, String> pair : glossary) {
            queue.enqueue(pair.key());
        }
        Comparator<String> order = new StringLT();
        queue.sort(order);

        //print the content of the list in alphabetical order
        for (int i = 0; i < queue.length(); i++) {
            String term = queue.dequeue();
            out.println("       <li>" + linkMap.value(term) + "</li>");
            queue.enqueue(term);
        }

        out.println("       </ul>");
        out.println("     </main>");
        out.println("   </body>");
        out.print("</html>");

    }

    /**
     * replace words in the {@code definition} that are also in {@code glossary}
     * with html anchor tags and the tag should contain the word as anchor text
     * and a link to its detailed page.
     *
     * @param definition
     *            the string whose words need to be add link to
     * @param glossary
     *            the term to its definition in English
     * @param linkMap
     *            term to a HTML hyperlink tag to its detailed page, also
     *            conatian a special pair <#indexPage,index page html hyperlink>
     * @requires {@code definition} is a value in {@code glossary},
     *           {@code glossary} contains at least one <term, the term 's
     *           definition> pair(both terms and definition should not be empty
     *           string).{@code linkMap} contains at least one <term , html
     *           hyperlink to term's detailed page, link text is the term>
     *           pair(both terms and html hyperlink should not be empty string)
     *           excluding the special pair with key "#indexPage"
     * @ensures return a string where certain words(words in glossary) are
     *          replaced with HTML anchor tags hyperlinks the word to their
     *          detailed page.
     *
     * @return a string where certain words(words in glossary) are replaced with
     *         HTML anchor tags hyperlinks the word to their detailed page.
     */
    public static String linkingWords(String definition, Map<String, String> glossary,
            Map<String, String> linkMap) {

        assert glossary != null : "Violation of: glossary is not null";
        assert linkMap != null : "Violation of: linkMap is not null";

        //initialize variables
        String result = definition;
        int position = 0;
        Set<Character> separators = new Set1L<>();
        generateElements(",.\"\';:()[]@!#$%^&*-=+{}\\?<>/|~`" + " ", separators);

        //use while loop to go through every token of the given definition String
        boolean reachEnd = false;
        while (!reachEnd) {
            String token = nextWordOrSeparator(definition, position, separators);
            if (glossary.hasKey(token)) {
                result = result.replace(token, linkMap.value(token));
            }

            //update position and make sure it's <|definition|
            position += token.length();
            if (position >= definition.length()) {
                reachEnd = true;
            }

        }
        return result;
    }

    /**
     * Create a glossary map ( term to definition) based on the file on
     * {@code filePath}.
     *
     * @param filePath
     *            the filePath of the file with terms and definitions
     *
     * @requires The filePath should point to a file consist of a single term on
     *           the first line, its definition on the next one or more lines
     *           (terminated by an empty line), another term on the next line,
     *           its definition on the next one or more lines (terminated by an
     *           empty line), etc. The input shall continue in this fashion
     *           through the definition of the last term, which shall end with
     *           its terminating empty line.
     * @ensures [returned Map contains words->its definition]
     * @return A map: words->its definition
     */
    public static Map<String, String> createGlossary(String filePath) {

        assert filePath != null : "Violation of: filePath is not null";

        //declare/initialize variables
        Map<String, String> glossary = new Map1L<>();
        SimpleReader input = new SimpleReader1L(filePath);

        //loops through the file to add all <word,definition> pairs to glossary
        while (!input.atEOS()) {
            //create useful variables
            String word = input.nextLine();
            boolean reachEmptyLine = false;
            StringBuilder definition = new StringBuilder();

            //get definition
            while (!reachEmptyLine) {
                String line = input.nextLine();
                if (!line.equals("")) {

                    definition.append(" " + line);

                } else {
                    reachEmptyLine = true;
                }
            }
            glossary.add(word, definition.toString().trim());
        }

        input.close();
        return glossary;
    }

    /**
     * Create a link map (key to HTML hyperlink ) the linked address is based on
     * {@code folderPath}.
     *
     * @param glossary
     *            term to definition
     * @requires {@code glossary} contains at least one <term, the term 's
     *           definition> pair(both terms and definition should not be empty
     *           string).
     * @ensures [returned Map contains words in {@code glossary} hyperlink to
     *          its detailed page ,also contain a special key #indexPage -> HTML
     *          hyperlink to index page]
     * @return a Map, words in {@code glossary}->HTML hyperlink to its detailed
     *         page, also contain a special key #indexPage -> HTML hyperlink to
     *         index page
     */
    public static Map<String, String> createLinkMap(Map<String, String> glossary) {

        assert glossary != null : "Violation of: glossary is not null";

        //declare/initialize variables
        Map<String, String> linkMap = new Map1L<>();
        linkMap.add("#indexPage", "<a href=\"index.html\">index</a>");

        //loops through map glossary to add all <word,HTML hyperlink to word's
        //detailed page> pairs to linkMap
        for (Map.Pair<String, String> pair : glossary) {
            String term = pair.key();
            linkMap.add(term, "<a href=\"" + term + ".html\">" + term + "</a>");
        }

        return linkMap;
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        //declare/initialize variables
        int endIndex = text.length();
        boolean findNextWord = !separators.contains(text.charAt(position));
        boolean findEndPoint = false;

        //loop through the text till it find a character of the other type,or index
        //exceed text length
        int i = position + 1;
        while (!findEndPoint && i < text.length()) {

            if (findNextWord && separators.contains(text.charAt(i))) {
                findEndPoint = true;
                endIndex = i;
            } else if (!findNextWord && !separators.contains(text.charAt(i))) {
                findEndPoint = true;
                endIndex = i;
            }

            i++;
        }

        //return the the substring which is either a separator or a word.
        return text.substring(position, endIndex);

    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";

        Set<Character> result = new Set1L<>();
        for (int i = 0; i < str.length(); i++) {
            if (!result.contains(str.charAt(i))) {
                result.add(str.charAt(i));
            }
        }
        charSet.transferFrom(result);

    }

    /**
     * Main method.Ask user for a file containing terms and their definitions
     * folder to store generated HTML files. Generate an index page of a
     * glossary all terms in the index page has a link to its detailed page,
     * stored in folder path user provided.
     *
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        //ask user for input file path, and folder to store HTML files
        out.println("Enter the file path of glossary text file: ");
        out.println("(This input file shall consist of a single term on the first line, "
                + "\nits definition on the next one or more lines (terminated by an "
                + "\nempty line), another term on the next line, its definition on the "
                + "\nnext one or more lines (terminated by an empty line), etc. The "
                + "\ninput shall continue in this fashion through the definition of "
                + "\nthe last term, which shall end with its terminating empty line. )");
        String filePath = in.nextLine();
        out.println("Enter the folder path you want to store golssary html "
                + "files(the folder must exist!!)");
        String folderPath = in.nextLine();

        //create glossary map and link map
        Map<String, String> glossary = createGlossary(filePath);
        Map<String, String> linkMap = createLinkMap(glossary);

        //write index page
        SimpleWriter writeIndexPage = new SimpleWriter1L(folderPath + "/index.html");
        outputIndexPage(glossary, writeIndexPage, linkMap);

        //write terms' detailed pages
        //loop through glossary map, create detailed page for every term
        for (Map.Pair<String, String> pair : glossary) {
            String term = pair.key();
            SimpleWriter writeTermPages = new SimpleWriter1L(
                    folderPath + "/" + term + ".html");
            outputTermPage(glossary, term, writeTermPages, linkMap);
            writeTermPages.close();
        }

        writeIndexPage.close();
        in.close();
        out.close();
    }

}
